package com.vitalsense.app.feature.prescriptions.ocr
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.PrescribedMedicine
import com.vitalsense.app.core.data.model.Prescription
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PrescriptionOcrDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onSavePrescription: (Prescription) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }
    var recognizedRawText by remember { mutableStateOf("") }
    var extractedMedicines by remember { mutableStateOf<List<PrescribedMedicine>>(emptyList()) }
    var instructionsText by remember { mutableStateOf("Take medicines on time with warm water.") }

    fun processSamplePrescription(sampleType: String) {
        isProcessing = true
        coroutineScope.launch {
            val sampleText = when (sampleType) {
                "Fever" -> "Rx:\nTab Paracetamol 650mg 1-0-1 (BD)\nTab Cetirizine 10mg 0-0-1 (HS)\nSyp Cough Relief 10ml TDS"
                "Maternal" -> "Rx:\nTab Iron Folic Acid 100mg 1-0-0\nTab Calcium 500mg 0-1-0\nMultivitamin Daily"
                else -> "Rx:\nTab Amoxicillin 500mg 1-1-1\nTab Paracetamol 500mg 1-0-1\nORS solution daily"
            }

            // Create bitmap representation to execute on-device ML Kit OCR
            val bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)
            val paint = Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 24f
            }
            canvas.drawText(sampleText, 20f, 60f, paint)

            val ocrText = PrescriptionOcrHelper.recognizeTextFromBitmap(bitmap)
            val finalText = if (ocrText.isNotBlank() && !ocrText.startsWith("OCR Processing Error")) ocrText else sampleText
            val parsedMeds = PrescriptionOcrHelper.parseMedicinesFromText(finalText)

            recognizedRawText = finalText
            extractedMedicines = parsedMeds
            isProcessing = false
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.aiPrescriptionDigitizer),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "On-Device ML Kit OCR for ${patient.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // Instruction Callout
                Surface(
                    color = VS_PrimaryContainer,
                    shape = CardShape,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text(
                            text = stringResource(R.string.zeroCloudOfflineInference),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_PrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.selectPrescriptionPhotoDesc),
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnBackground
                        )
                    }
                }

                // Sample Prescription Buttons
                Text(
                    text = stringResource(R.string.simulateCaptureScan),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Button(
                        onClick = { processSamplePrescription("Fever") },
                        modifier = Modifier.weight(1f),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                    ) {
                        Text(stringResource(R.string.feverRxSample), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                    }
                    Button(
                        onClick = { processSamplePrescription("Maternal") },
                        modifier = Modifier.weight(1f),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                    ) {
                        Text(stringResource(R.string.maternalCategory), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                    }
                    Button(
                        onClick = { processSamplePrescription("Infection") },
                        modifier = Modifier.weight(1f),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                    ) {
                        Text(stringResource(R.string.infectionSample), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                    }
                }

                // Processing Indicator
                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.lg),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VS_Primary)
                    }
                }

                // OCR Output & Medicine Extraction
                if (recognizedRawText.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.extractedClinicalEntities),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    VitalSenseCard(
                        backgroundColor = VS_SurfaceVariant,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            Text(
                                text = stringResource(R.string.rawOcrTextStream),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )
                            Text(
                                text = recognizedRawText,
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnBackground
                            )
                        }
                    }

                    Text(
                        text = "Parsed Medicine Schedule (${extractedMedicines.size} Detected):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    extractedMedicines.forEach { med ->
                        VitalSenseCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = med.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "Dosage: ${med.dosage} · ${med.frequency}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                                Surface(
                                    shape = PillShape,
                                    color = VS_SuccessContainer
                                ) {
                                    Text(
                                        text = med.duration,
                                        style = MaterialTheme.typography.labelSmall.copy(color = VS_OnSuccessContainer, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Doctor Instructions Input
                    OutlinedTextField(
                        value = instructionsText,
                        onValueChange = { instructionsText = it },
                        label = { Text(stringResource(R.string.clinicalInstructionsNotes), color = VS_OnSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = InputShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VS_SurfaceVariant,
                            unfocusedContainerColor = VS_Surface,
                            focusedBorderColor = VS_Primary,
                            unfocusedBorderColor = VS_Outline,
                            focusedTextColor = VS_OnBackground,
                            unfocusedTextColor = VS_OnBackground
                        )
                    )

                    // Confirm and Save Button
                    Button(
                        onClick = {
                            val newPrescription = Prescription(
                                id = "rx_${UUID.randomUUID()}",
                                patientId = patient.id,
                                patientName = patient.name,
                                doctorId = "doc_attending",
                                doctorName = "Attending Medical Officer (OCR)",
                                doctorSpecialty = "General Medicine",
                                timestamp = System.currentTimeMillis(),
                                dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                                medicines = extractedMedicines,
                                instructions = instructionsText,
                                isOcrExtracted = true
                            )
                            onSavePrescription(newPrescription)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VS_Primary,
                            contentColor = VS_OnBackground
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.saveToMedicalRecord),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
