package com.vitalsense.app.feature.prescriptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.R
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.PrescribedMedicine
import com.vitalsense.app.core.data.model.Prescription
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.prescriptions.ocr.CameraCaptureView
import com.vitalsense.app.feature.prescriptions.ocr.PrescriptionOcrResultScreen
import com.vitalsense.app.feature.prescriptions.ocr.PrescriptionPhotoReviewScreen
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

enum class OcrStep {
    CAPTURE,
    REVIEW,
    RESULT
}

@Composable
fun PrescriptionUploadDialog(
    patient: Patient,
    isAshaProxy: Boolean = false,
    onDismiss: () -> Unit,
    onSavePrescription: (Prescription) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Camera / AI Scan, 1: Write Down (Manual)
    var ocrStep by remember { mutableStateOf(OcrStep.CAPTURE) }
    var capturedPhotoFile by remember { mutableStateOf<File?>(null) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? Activity

    val scannerOptions = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()
    }

    val docScannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            val pageUri = scanResult?.pages?.firstOrNull()?.imageUri
            if (pageUri != null) {
                try {
                    val cacheFile = File(context.cacheDir, "doc_scan_${System.currentTimeMillis()}.jpg")
                    context.contentResolver.openInputStream(pageUri)?.use { input ->
                        cacheFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    capturedPhotoFile = cacheFile
                    ocrStep = OcrStep.RESULT
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val launchDocumentScanner: () -> Unit = {
        if (activity != null) {
            val scannerClient = GmsDocumentScanning.getClient(scannerOptions)
            scannerClient.getStartScanIntent(activity)
                .addOnSuccessListener { intentSender ->
                    docScannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                }
                .addOnFailureListener { e ->
                    ocrStep = OcrStep.CAPTURE
                }
        } else {
            ocrStep = OcrStep.CAPTURE
        }
    }

    // --- Manual Entry State ---
    var manualDoctorName by remember { mutableStateOf("") }
    var manualSpecialty by remember { mutableStateOf("General Physician") }
    var manualInstructions by remember { mutableStateOf("") }
    val manualMedicines = remember { mutableStateListOf<PrescribedMedicine>() }

    var currentMedName by remember { mutableStateOf("") }
    var currentDosage by remember { mutableStateOf("500 mg") }
    var currentFrequency by remember { mutableStateOf("Twice daily (after meals)") }
    var currentDuration by remember { mutableStateOf("5 Days") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header (shown on Manual Tab and OCR Result Tab, or when not in full camera mode)
                if (selectedTab == 1 || ocrStep == OcrStep.RESULT) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isAshaProxy) "Upload Prescription (for ${patient.name})" else stringResource(R.string.uploadPrescriptionTitle),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.digitizePaperPrescription),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                            Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                        }
                    }

                    // Segmented Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.md, vertical = Spacing.xxs),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        val tabs = listOf("📷 ${stringResource(R.string.cameraAiScan)}", "✍️ ${stringResource(R.string.writeDown)}")
                        tabs.forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            Surface(
                                onClick = { selectedTab = index },
                                shape = PillShape,
                                color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                                border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline),
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                        )
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xs))
                }

                // Tab 0: Camera / AI Scan Flow
                if (selectedTab == 0) {
                    when (ocrStep) {
                        OcrStep.CAPTURE -> {
                            CameraCaptureView(
                                onPhotoCaptured = { photo ->
                                    capturedPhotoFile = photo
                                    ocrStep = OcrStep.REVIEW
                                },
                                onManualEntryFallback = {
                                    selectedTab = 1
                                },
                                onClose = onDismiss,
                                onLaunchDocumentScanner = launchDocumentScanner,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        OcrStep.REVIEW -> {
                            val photo = capturedPhotoFile
                            if (photo != null && photo.exists()) {
                                PrescriptionPhotoReviewScreen(
                                    photoFile = photo,
                                    onConfirmUsePhoto = {
                                        ocrStep = OcrStep.RESULT
                                    },
                                    onRetakePhoto = {
                                        ocrStep = OcrStep.CAPTURE
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                ocrStep = OcrStep.CAPTURE
                            }
                        }
                        OcrStep.RESULT -> {
                            PrescriptionOcrResultScreen(
                                patient = patient,
                                photoFile = capturedPhotoFile,
                                onSavePrescription = { rx ->
                                    onSavePrescription(rx)
                                    onDismiss()
                                },
                                onRetakePhoto = {
                                    ocrStep = OcrStep.CAPTURE
                                },
                                onManualEntryFallback = {
                                    selectedTab = 1
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Tab 1: Write Down (Manual Entry)
                if (selectedTab == 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Spacing.md)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        VitalSenseTextField(
                            value = manualDoctorName,
                            onValueChange = { manualDoctorName = it },
                            label = "Doctor Name",
                            placeholder = "e.g. Dr. A. Sharma"
                        )

                        VitalSenseTextField(
                            value = manualSpecialty,
                            onValueChange = { manualSpecialty = it },
                            label = "Specialty / Clinic",
                            placeholder = "e.g. General Physician / District Hospital"
                        )

                        Text(
                            text = stringResource(R.string.addPrescribedMedicines),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )

                        VitalSenseCard(
                            backgroundColor = VS_SurfaceVariant,
                            border = BorderStroke(1.dp, VS_Outline)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                VitalSenseTextField(
                                    value = currentMedName,
                                    onValueChange = { currentMedName = it },
                                    label = "Medicine Name",
                                    placeholder = "e.g. Amoxicillin / Paracetamol"
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        VitalSenseTextField(
                                            value = currentDosage,
                                            onValueChange = { currentDosage = it },
                                            label = "Dosage",
                                            placeholder = "500 mg"
                                        )
                                    }
                                    Box(modifier = Modifier.weight(1f)) {
                                        VitalSenseTextField(
                                            value = currentDuration,
                                            onValueChange = { currentDuration = it },
                                            label = "Duration",
                                            placeholder = "5 Days"
                                        )
                                    }
                                }

                                VitalSenseTextField(
                                    value = currentFrequency,
                                    onValueChange = { currentFrequency = it },
                                    label = "Frequency",
                                    placeholder = "Twice daily after meals"
                                )

                                Button(
                                    onClick = {
                                        if (currentMedName.isNotBlank()) {
                                            manualMedicines.add(
                                                PrescribedMedicine(
                                                    name = currentMedName.trim(),
                                                    dosage = currentDosage.trim(),
                                                    frequency = currentFrequency.trim(),
                                                    duration = currentDuration.trim(),
                                                    quantity = 10
                                                )
                                            )
                                            currentMedName = ""
                                        }
                                    },
                                    shape = PillShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                    modifier = Modifier.align(Alignment.End),
                                    enabled = currentMedName.isNotBlank()
                                ) {
                                    Text(stringResource(R.string.addMedicineBtn), color = Color.White, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        // Added Medicines List
                        if (manualMedicines.isNotEmpty()) {
                            Text(
                                text = "Medicines to Include (${manualMedicines.size})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            manualMedicines.forEachIndexed { index, med ->
                                Surface(
                                    shape = PillShape,
                                    color = VS_SurfaceVariant,
                                    border = BorderStroke(1.dp, VS_Outline),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${med.name} (${med.dosage}) - ${med.frequency} · ${med.duration}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnBackground
                                        )
                                        IconButton(onClick = { manualMedicines.removeAt(index) }, modifier = Modifier.size(24.dp)) {
                                            Text(text = "✕", color = VS_Error, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        VitalSenseTextField(
                            value = manualInstructions,
                            onValueChange = { manualInstructions = it },
                            label = "Additional Notes / Precautions",
                            placeholder = "e.g. Drink plenty of warm water and avoid oily food",
                            singleLine = false,
                            maxLines = 2
                        )

                        VitalSenseButton(
                            text = stringResource(R.string.savePrescriptionRecord),
                            onClick = {
                                val newRx = Prescription(
                                    id = "rx_${System.currentTimeMillis()}",
                                    patientId = patient.id,
                                    patientName = patient.name,
                                    doctorId = "doc_attending",
                                    doctorName = manualDoctorName.ifBlank { "Attending Medical Officer" },
                                    doctorSpecialty = manualSpecialty.ifBlank { "General Physician" },
                                    timestamp = System.currentTimeMillis(),
                                    dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                                    medicines = manualMedicines.toList(),
                                    instructions = manualInstructions,
                                    isOcrExtracted = false
                                )
                                onSavePrescription(newRx)
                                onDismiss()
                            },
                            style = ButtonStyle.PRIMARY,
                            enabled = manualMedicines.isNotEmpty(),
                            modifier = Modifier.padding(bottom = Spacing.lg)
                        )
                    }
                }
            }
        }
    }
}
