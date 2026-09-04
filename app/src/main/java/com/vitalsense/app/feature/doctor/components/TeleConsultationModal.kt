package com.vitalsense.app.feature.doctor.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.core.ui.theme.*
import kotlinx.coroutines.delay
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource
import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TeleConsultationModal(
    patientName: String,
    doctorName: String,
    specialty: String = "General Physician",
    villageName: String = "Sundarpura",
    patientAge: Int = 34,
    onDismiss: () -> Unit,
    onEndCall: (consultationNotes: String) -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isLowBandwidthMode by remember { mutableStateOf(false) }
    var callSeconds by remember { mutableIntStateOf(0) }
    var consultationNotes by remember { mutableStateOf("") }
    var showRxSheet by remember { mutableStateOf(false) }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val cameraGranted = permissionState.permissions.find { it.permission == Manifest.permission.CAMERA }?.status?.isGranted == true
    val audioGranted = permissionState.permissions.find { it.permission == Manifest.permission.RECORD_AUDIO }?.status?.isGranted == true

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    // Real microphone audio level monitor
    var micLevel by remember { mutableFloatStateOf(0f) }
    var isSpeaking by remember { mutableStateOf(false) }

    LaunchedEffect(audioGranted, isMuted) {
        if (!audioGranted || isMuted) {
            micLevel = 0f
            isSpeaking = false
            return@LaunchedEffect
        }

        withContext(Dispatchers.IO) {
            val sampleRate = 8000
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat).coerceAtLeast(1024)

            var audioRecord: AudioRecord? = null
            try {
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize
                )

                if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
                    audioRecord.startRecording()
                    val buffer = ShortArray(bufferSize / 2)

                    while (isActive && !isMuted) {
                        val read = audioRecord.read(buffer, 0, buffer.size)
                        if (read > 0) {
                            var sum = 0.0
                            for (i in 0 until read) {
                                sum += buffer[i] * buffer[i]
                            }
                            val rms = Math.sqrt(sum / read)
                            val normalized = (rms / 2500.0).toFloat().coerceIn(0.1f, 1.0f)
                            withContext(Dispatchers.Main) {
                                micLevel = normalized
                                isSpeaking = rms > 350
                            }
                        }
                        kotlinx.coroutines.delay(80)
                    }
                }
            } catch (e: SecurityException) {
                // Permission not granted
            } catch (e: Exception) {
                // Audio hardware busy
            } finally {
                try {
                    if (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                        audioRecord.stop()
                    }
                    audioRecord?.release()
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
    }

    // Live call duration timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    val formattedTime = remember(callSeconds) {
        val minutes = callSeconds / 60
        val seconds = callSeconds % 60
        "%02d:%02d".format(minutes, seconds)
    }

    // Audio waveform animation
    val infiniteTransition = rememberInfiniteTransition(label = "Waveform")
    val waveAnim1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave1"
    )
    val waveAnim2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave2"
    )
    val waveAnim3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave3"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(GlumeBackground),
            color = GlumeBackground
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // 1. Video Simulation Canvas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF13131D),
                                    Color(0xFF1F1F2E),
                                    Color(0xFF151522)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLowBandwidthMode) {
                        // Ultra-low bandwidth audio-only visualizer
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GlumePrimaryPurpleContainer,
                                border = BorderStroke(2.dp, GlumePrimaryPurple),
                                modifier = Modifier.size(110.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👤", fontSize = 54.sp)
                                }
                            }
                            Text(
                                text = patientName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = GlumeTextPrimary
                            )
                            Text(
                                text = "📡 Ultra-Low Bandwidth Mode (2G Audio Only)",
                                style = MaterialTheme.typography.bodySmall.copy(color = GlumeSuccessMint, fontWeight = FontWeight.Bold)
                            )

                            // Live Audio Bars
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(30.dp)
                            ) {
                                val dynamicWave = if (audioGranted && !isMuted && isSpeaking) micLevel else waveAnim1
                                listOf(dynamicWave, waveAnim2, waveAnim3, waveAnim2, dynamicWave).forEach { heightFraction ->
                                    Box(
                                        modifier = Modifier
                                            .width(5.dp)
                                            .fillMaxHeight(heightFraction)
                                            .clip(CircleShape)
                                            .background(GlumeSuccessMint)
                                    )
                                }
                            }
                        }
                    } else {
                        // Simulated Remote Patient Video Feed
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.md)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                GlumePrimaryPurpleContainer,
                                                GlumeSurfaceElevated
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👩🏽‍🌾", fontSize = 82.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$patientName ($patientAge yrs)",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = GlumeTextPrimary
                                )
                                Text(
                                    text = "Connected from Sundarpura PHC Tele-Kiosk",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GlumeTextSecondary
                                )
                            }
                        }
                    }
                }

                // 2. Top Bar HUD (Timer, Quality, Village Info)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = PillShape,
                        color = Color(0x99000000),
                        border = BorderStroke(1.dp, GlumeBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GlumeAlertCoral)
                            )
                            Text(
                                text = "REC  $formattedTime",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Surface(
                        shape = PillShape,
                        color = Color(0x99000000),
                        border = BorderStroke(1.dp, GlumeBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("📡", fontSize = 12.sp)
                            Text(
                                text = if (isLowBandwidthMode) "2G Optimized (32 kbps)" else "HD 720p · 42ms",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isLowBandwidthMode) GlumeWarningAmber else GlumeSuccessMint,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                // 3. Live Tele-Vitals HUD Card (Left Side Overlay) & Full Vitals Toggle
                var showVitalsSidePanel by remember { mutableStateOf(false) }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xCC181824),
                    border = BorderStroke(1.dp, GlumeBorder),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = Spacing.md)
                        .width(140.dp)
                        .clickable { showVitalsSidePanel = !showVitalsSidePanel }
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.liveTeleVitals),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GlumeTextSecondary
                                )
                            )
                            Text(if (showVitalsSidePanel) "◀" else "▶", fontSize = 10.sp, color = GlumePrimaryPurple)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("❤️ Pulse", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = GlumeTextSecondary))
                            Text("74 bpm", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GlumeSuccessMint))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🩸 BP", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = GlumeTextSecondary))
                            Text("118/78", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GlumeTextPrimary))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🫁 SpO2", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = GlumeTextSecondary))
                            Text("98%", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GlumeSuccessMint))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🌡️ Temp", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = GlumeTextSecondary))
                            Text("98.4°F", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GlumeTextPrimary))
                        }

                        Text(
                            text = "Tap to expand",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = GlumePrimaryPurple)
                        )
                    }
                }

                // Vitals Side Panel Overlay
                if (showVitalsSidePanel) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xF0181826),
                        border = BorderStroke(1.5.dp, GlumePrimaryPurple),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = Spacing.md)
                            .width(220.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Patient Health Vitals",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "✕",
                                    fontSize = 14.sp,
                                    color = GlumeTextSecondary,
                                    modifier = Modifier.clickable { showVitalsSidePanel = false }
                                )
                            }
                            HorizontalDivider(color = GlumeBorder)
                            Text("Patient: $patientName ($patientAge yrs)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = GlumeTextPrimary)
                            Text("Village: $villageName", style = MaterialTheme.typography.labelSmall, color = GlumeTextSecondary)
                            Text("• Blood Pressure: 118/78 mmHg (Normal)", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                            Text("• Heart Rate: 74 bpm (Stable)", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                            Text("• Blood Oxygen: 98% SpO2 (Healthy)", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                            Text("• Temperature: 98.4°F", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                            Text("• Chronic Condition: None", style = MaterialTheme.typography.labelSmall, color = GlumeTextSecondary)
                            Text("• Last Visit: 12 days ago (PHC OPD)", style = MaterialTheme.typography.labelSmall, color = GlumeTextSecondary)
                        }
                    }
                }

                // 4. Picture-in-Picture (PiP) Floating Doctor View
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isCameraOff) GlumeSurfaceElevated else Color(0xFF28283C),
                    border = BorderStroke(1.5.dp, GlumePrimaryPurple),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = Spacing.md, bottom = 135.dp)
                        .size(width = 100.dp, height = 140.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCameraOff) {
                            Text("📷 Off", style = MaterialTheme.typography.labelSmall, color = GlumeTextSecondary)
                        } else {
                            if (cameraGranted) {
                                CameraPreview(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)))
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.clickable {
                                        permissionState.launchMultiplePermissionRequest()
                                    }
                                ) {
                                    Text("👨‍⚕️", fontSize = 42.sp)
                                    Text(
                                        text = "Tap to Enable Cam",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GlumePrimaryPurpleLight
                                        )
                                    )
                                }
                            }
                        }

                        if (isMuted) {
                            Surface(
                                shape = CircleShape,
                                color = GlumeAlertCoral,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(18.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🔇", fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }

                // 5. In-Call Controls Bottom Bar
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = Color(0xEB14141E),
                    border = BorderStroke(1.dp, GlumeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        // Always-visible Switch to Voice call banner during video mode
                        if (!isLowBandwidthMode) {
                            Surface(
                                shape = PillShape,
                                color = GlumeWarningAmber.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, GlumeWarningAmber),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isLowBandwidthMode = true
                                        isCameraOff = true
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("🎙️", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Switch to Voice Call (Save Bandwidth / Weak Signal)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GlumeWarningAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mic Button (56dp touch target) with real speaking indicator
                            Surface(
                                shape = CircleShape,
                                color = when {
                                    isMuted -> GlumeAlertCoral
                                    isSpeaking -> GlumeSuccessMint.copy(alpha = 0.85f)
                                    else -> GlumeSurfaceElevated
                                },
                                border = if (isSpeaking && !isMuted) BorderStroke(2.dp, GlumeSuccessMint) else null,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable { isMuted = !isMuted }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (isMuted) "🔇" else if (isSpeaking) "🗣️" else "🎙️", fontSize = 22.sp)
                                }
                            }

                            // Camera Button (56dp touch target)
                            Surface(
                                shape = CircleShape,
                                color = if (isCameraOff) GlumeSurfaceSubtle else GlumeSurfaceElevated,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable { isCameraOff = !isCameraOff }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (isCameraOff) "🚫" else "📹", fontSize = 22.sp)
                                }
                            }

                            // 2G Mode / Voice Button (56dp touch target)
                            Surface(
                                shape = CircleShape,
                                color = if (isLowBandwidthMode) GlumeWarningAmber.copy(alpha = 0.3f) else GlumeSurfaceElevated,
                                border = BorderStroke(
                                    1.dp,
                                    if (isLowBandwidthMode) GlumeWarningAmber else Color.Transparent
                                ),
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable {
                                        isLowBandwidthMode = !isLowBandwidthMode
                                        if (isLowBandwidthMode) isCameraOff = true
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📡", fontSize = 22.sp)
                                }
                            }

                            // End Call Button (Red Circle, 64dp touch target)
                            Surface(
                                shape = CircleShape,
                                color = GlumeAlertCoral,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clickable {
                                        onEndCall("Tele-consultation completed ($formattedTime). Vitals verified, medication advised.")
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CallEnd,
                                        contentDescription = stringResource(R.string.endCall),
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                val cameraProvider = ProcessCameraProvider.getInstance(context).get()
                cameraProvider.unbindAll()
            } catch (e: Exception) {
                // Ignore cleanup error
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}
