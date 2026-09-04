package com.vitalsense.app.feature.prescriptions.ocr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import java.io.File
import java.util.concurrent.Executor

/**
 * Full CameraX capture screen with document-frame overlay guide,
 * torch toggle, audio guidance, and 68dp shutter button.
 */
@Composable
fun CameraCaptureView(
    onPhotoCaptured: (File) -> Unit,
    onManualEntryFallback: () -> Unit,
    onClose: () -> Unit,
    onLaunchDocumentScanner: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionDeniedCount by remember { mutableStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            permissionDeniedCount++
        }
    }

    if (!hasCameraPermission) {
        CameraPermissionRationale(
            deniedCount = permissionDeniedCount,
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onOpenSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onManualEntryFallback = onManualEntryFallback,
            onClose = onClose
        )
        return
    }

    var camera by remember { mutableStateOf<Camera?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }

    val mainExecutor: Executor = remember(context) { ContextCompat.getMainExecutor(context) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. CameraX PreviewView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                        .also {
                            imageCapture = it
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            capture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, mainExecutor)

                previewView
            }
        )

        // 2. Document Frame Overlay Guide
        DocumentFrameOverlay(
            modifier = Modifier.fillMaxSize()
        )

        // 3. Top Action Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.lg)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text("✕", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                // Flash / Torch toggle
                IconButton(
                    onClick = {
                        val currentCamera = camera
                        if (currentCamera != null && currentCamera.cameraInfo.hasFlashUnit()) {
                            val nextState = !isFlashOn
                            currentCamera.cameraControl.enableTorch(nextState)
                            isFlashOn = nextState
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isFlashOn) VS_Primary else Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Text(if (isFlashOn) "⚡" else "🔦", fontSize = 18.sp)
                }

                // Audio Guidance
                IconButton(
                    onClick = {
                        AudioGuidanceHelper.speak(
                            context = context,
                            text = "Point your camera at the prescription paper inside the frame, and tap the large button to take a photo."
                        )
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Text("🔊", fontSize = 18.sp)
                }
            }
        }

        // 4. Bottom Controls (Shutter & Manual Fallback)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(bottom = Spacing.xl, top = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(
                text = "📄 Position prescription inside the frame",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center
            )

            // 68dp Shutter Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(if (isCapturing) Color.Gray else Color.White)
                        .clickable(enabled = !isCapturing && imageCapture != null) {
                            val activeCapture = imageCapture ?: return@clickable
                            isCapturing = true
                            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)

                            val photoFile = File(
                                context.cacheDir,
                                "rx_capture_${System.currentTimeMillis()}.jpg"
                            )

                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            activeCapture.takePicture(
                                outputOptions,
                                mainExecutor,
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        isCapturing = false
                                        onPhotoCaptured(photoFile)
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        isCapturing = false
                                        exception.printStackTrace()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            color = VS_Primary,
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text("📷", fontSize = 26.sp)
                    }
                }
            }

            if (onLaunchDocumentScanner != null) {
                Button(
                    onClick = onLaunchDocumentScanner,
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VS_Primary,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.xs)
                ) {
                    Text(
                        text = "✨ Google Auto-Crop & Clean Scanner",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            TextButton(
                onClick = onManualEntryFallback,
                modifier = Modifier.padding(horizontal = Spacing.md)
            ) {
                Text(
                    text = "✍️ Can't scan? Enter details manually",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun DocumentFrameOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(horizontal = 28.dp, vertical = 130.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.78f)
                .border(
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            val accentLength = 28.dp
            val accentThickness = 4.dp
            val accentColor = VS_Primary

            Box(Modifier.size(accentLength, accentThickness).background(accentColor).align(Alignment.TopStart))
            Box(Modifier.size(accentThickness, accentLength).background(accentColor).align(Alignment.TopStart))

            Box(Modifier.size(accentLength, accentThickness).background(accentColor).align(Alignment.TopEnd))
            Box(Modifier.size(accentThickness, accentLength).background(accentColor).align(Alignment.TopEnd))

            Box(Modifier.size(accentLength, accentThickness).background(accentColor).align(Alignment.BottomStart))
            Box(Modifier.size(accentThickness, accentLength).background(accentColor).align(Alignment.BottomStart))

            Box(Modifier.size(accentLength, accentThickness).background(accentColor).align(Alignment.BottomEnd))
            Box(Modifier.size(accentThickness, accentLength).background(accentColor).align(Alignment.BottomEnd))
        }
    }
}

@Composable
private fun CameraPermissionRationale(
    deniedCount: Int,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onManualEntryFallback: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VS_Surface),
            border = BorderStroke(1.dp, VS_Outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                Text(text = "📷", fontSize = 48.sp)

                Text(
                    text = "Camera Permission Needed",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "VitalSense uses your camera to capture prescription documents and extract medicines offline on your device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VS_OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                if (deniedCount > 1) {
                    Surface(
                        color = VS_ErrorContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Camera access was declined. Please open device settings to enable camera permissions for VitalSense.",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnErrorContainer,
                            modifier = Modifier.padding(Spacing.sm),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = onOpenSettings,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                        shape = PillShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("⚙️ Open App Settings", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onRequestPermission,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                        shape = PillShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Allow Camera Access", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onManualEntryFallback,
                    shape = PillShape,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✍️ Enter Details Manually", color = VS_OnBackground)
                }

                TextButton(onClick = onClose) {
                    Text("Cancel", color = VS_OnSurfaceVariant)
                }
            }
        }
    }
}
