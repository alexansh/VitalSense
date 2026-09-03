package com.vitalsense.app.feature.patient

import android.Manifest
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun PrescriptionOcrScreen(
    onSavePrescriptionText: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var isScanning by remember { mutableStateOf(false) }
    var scannedText by remember { mutableStateOf("") }
    var isManualEntry by remember { mutableStateOf(false) }
    var isSavedSuccess by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (granted) isScanning = true
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimaryNearBlack
                    )
                }
                Text(
                    text = "Upload Prescription (OCR)",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextPrimaryNearBlack
                )
            }
        }

        item {
            InlineHelpBanner(
                title = "Smart Prescription Scanner",
                message = "Scan a physical paper prescription using ML Kit text recognition or enter medicine details manually."
            )
        }

        if (isSavedSuccess) {
            item {
                VitalSenseCard(backgroundColor = SoftMintSuccess) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "✓ Prescription Saved!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "The extracted prescription details have been cached locally to your Health Card.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to Home", onClick = onBack, style = ButtonStyle.DARK)
                    }
                }
            }
        } else {
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Choose Input Method",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            VitalSenseButton(
                                text = "📷 Scan Paper Rx",
                                onClick = {
                                    if (hasCameraPermission) {
                                        isScanning = true
                                        isManualEntry = false
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                style = ButtonStyle.PRIMARY
                            )

                            VitalSenseButton(
                                text = "✏️ Manual Entry",
                                onClick = {
                                    isManualEntry = true
                                    isScanning = false
                                },
                                modifier = Modifier.weight(1f),
                                style = ButtonStyle.SECONDARY
                            )
                        }
                    }
                }
            }
            
            if (isScanning && hasCameraPermission) {
                item {
                    VitalSenseCard {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Point camera at prescription", fontWeight = FontWeight.Bold)
                            
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)) {
                                AndroidView(
                                    factory = { ctx ->
                                        val previewView = PreviewView(ctx).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                        }
                                        
                                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                        val executor = Executors.newSingleThreadExecutor()
                                        
                                        cameraProviderFuture.addListener({
                                            val cameraProvider = cameraProviderFuture.get()
                                            
                                            val preview = Preview.Builder().build().also {
                                                it.setSurfaceProvider(previewView.surfaceProvider)
                                            }
                                            
                                            val imageAnalyzer = ImageAnalysis.Builder()
                                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                                .build()
                                                
                                            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                                            
                                            imageAnalyzer.setAnalyzer(executor) { imageProxy ->
                                                val mediaImage = imageProxy.image
                                                if (mediaImage != null) {
                                                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                                    recognizer.process(image)
                                                        .addOnSuccessListener { visionText ->
                                                            if (visionText.text.isNotBlank()) {
                                                                scannedText = visionText.text
                                                            }
                                                        }
                                                        .addOnCompleteListener {
                                                            imageProxy.close()
                                                        }
                                                } else {
                                                    imageProxy.close()
                                                }
                                            }
                                            
                                            try {
                                                cameraProvider.unbindAll()
                                                cameraProvider.bindToLifecycle(
                                                    lifecycleOwner,
                                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                                    preview,
                                                    imageAnalyzer
                                                )
                                            } catch(exc: Exception) {
                                                // Handle error
                                            }
                                        }, ContextCompat.getMainExecutor(ctx))
                                        
                                        previewView
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            
                            VitalSenseButton(
                                text = "Stop Scanning",
                                onClick = { isScanning = false },
                                style = ButtonStyle.SECONDARY
                            )
                        }
                    }
                }
            }

            if (scannedText.isNotBlank() || isManualEntry) {
                item {
                    VitalSenseCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = if (isManualEntry) "Manual Medicine Entry" else "Extracted Text (Review & Edit)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )

                            OutlinedTextField(
                                value = scannedText,
                                onValueChange = { scannedText = it },
                                label = { Text("Prescription Details") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                shape = InputShape
                            )

                            Text(
                                text = "⚠️ NEEDS_HUMAN_CLINICAL_REVIEW: Please verify the extracted text matches your doctor's handwritten paper prescription before confirming.",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryMuted
                            )

                            VitalSenseButton(
                                text = "Explicitly Confirm & Save",
                                onClick = {
                                    if (scannedText.isNotBlank()) {
                                        onSavePrescriptionText(scannedText)
                                        isSavedSuccess = true
                                        isScanning = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                style = ButtonStyle.DARK
                            )
                        }
                    }
                }
            }
        }
    }
}