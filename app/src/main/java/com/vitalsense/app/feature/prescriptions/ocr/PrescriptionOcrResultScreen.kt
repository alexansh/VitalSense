package com.vitalsense.app.feature.prescriptions.ocr

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.PrescribedMedicine
import com.vitalsense.app.core.data.model.Prescription
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen displaying the recognized OCR text, editable field,
 * parsed medicine items, and direct save action.
 */
@Composable
fun PrescriptionOcrResultScreen(
    patient: Patient,
    photoFile: File?,
    onSavePrescription: (Prescription) -> Unit,
    onRetakePhoto: () -> Unit,
    onManualEntryFallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isOcrRunning by remember { mutableStateOf(true) }
    var rawOcrText by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("PHC Attending (Digitized)") }
    var instructions by remember { mutableStateOf("Take medicines as directed with clean drinking water.") }
    val medicinesList = remember { mutableStateListOf<PrescribedMedicine>() }

    // Run on-device ML Kit text recognition on the confirmed photo
    LaunchedEffect(photoFile) {
        if (photoFile != null && photoFile.exists()) {
            isOcrRunning = true
            val extracted = PrescriptionOcrHelper.recognizeTextFromFile(context, photoFile)
            rawOcrText = if (extracted.startsWith("OCR Processing Error")) "" else extracted
            val parsed = PrescriptionOcrHelper.parseMedicinesFromText(rawOcrText)
            medicinesList.clear()
            medicinesList.addAll(parsed)
            isOcrRunning = false
        } else {
            isOcrRunning = false
        }
    }

    if (isOcrRunning) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(VS_Background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                CircularProgressIndicator(color = VS_Primary, modifier = Modifier.size(48.dp))
                Text(
                    text = "🔍 Reading prescription on-device...",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "Running local ML Kit OCR without network",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            }
        }
        return
    }

    // If no text was recognized at all
    if (rawOcrText.isBlank()) {
        NoTextDetectedView(
            onRetake = onRetakePhoto,
            onManualEntry = onManualEntryFallback,
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(Spacing.md)
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
                    text = "📋 Review & Confirm OCR Scan",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "Patient: ${patient.name} · Verify extracted text below",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            }
            // Listen to recognized text
            IconButton(
                onClick = {
                    AudioGuidanceHelper.speak(context, "Extracted prescription text: $rawOcrText")
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(VS_PrimaryContainer, RoundedCornerShape(10.dp))
            ) {
                Text("🔊", fontSize = 18.sp)
            }
        }

        // 1. Raw Extracted Text (User Editable)
        VitalSenseCard {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Extracted Text (Tap to Edit):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Surface(
                        shape = PillShape,
                        color = VS_SuccessContainer
                    ) {
                        Text(
                            text = "ON-DEVICE OCR",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VS_OnSuccessContainer),
                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = rawOcrText,
                    onValueChange = { newText ->
                        rawOcrText = newText
                        val updatedMeds = PrescriptionOcrHelper.parseMedicinesFromText(newText)
                        medicinesList.clear()
                        medicinesList.addAll(updatedMeds)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 160.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = VS_OnBackground),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VS_Primary,
                        unfocusedBorderColor = VS_Outline
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // 2. Structured Medicines
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = "Identified Medicines (${medicinesList.size}):",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )

            if (medicinesList.isEmpty()) {
                Surface(
                    shape = CardShape,
                    color = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No standard medicine names matched automatically. The raw text above will be saved as a Digitized Prescription note.",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant,
                        modifier = Modifier.padding(Spacing.md)
                    )
                }
            } else {
                medicinesList.forEachIndexed { index, med ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = VS_Surface),
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.sm),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "💊 ${med.name} (${med.dosage})",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "${med.frequency} · Duration: ${med.duration}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { medicinesList.removeAt(index) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("✕", color = VS_Error, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Attending Doctor Name & Notes
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = "Prescribing Doctor / Health Post:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
            OutlinedTextField(
                value = doctorName,
                onValueChange = { doctorName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Text(
                text = "Instructions / Dosage Directions:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                shape = RoundedCornerShape(10.dp)
            )
        }

        // 4. Save & Retake Actions
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            VitalSenseButton(
                text = "💾 Save Digitized Prescription",
                style = ButtonStyle.PRIMARY,
                onClick = {
                    val finalMedicines = if (medicinesList.isNotEmpty()) {
                        medicinesList.toList()
                    } else {
                        listOf(
                            PrescribedMedicine(
                                name = "Digitized Prescription Note",
                                dosage = "As indicated",
                                frequency = "See notes",
                                duration = "7 Days",
                                quantity = 1
                            )
                        )
                    }

                    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

                    val prescription = Prescription(
                        id = "rx_ocr_${System.currentTimeMillis()}",
                        patientId = patient.id,
                        patientName = patient.name,
                        doctorId = "doc_ocr_scan",
                        doctorName = doctorName.ifBlank { "PHC Attending (Digitized)" },
                        doctorSpecialty = "General Medicine",
                        timestamp = System.currentTimeMillis(),
                        dateFormatted = dateStr,
                        medicines = finalMedicines,
                        instructions = if (instructions.isNotBlank()) instructions else rawOcrText,
                        isOcrExtracted = true
                    )

                    onSavePrescription(prescription)
                }
            )

            OutlinedButton(
                onClick = onRetakePhoto,
                shape = PillShape,
                border = BorderStroke(1.dp, VS_Outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🔁 Retake Photo", color = VS_OnBackground)
            }
        }
    }
}

/**
 * Clean fallback state when ML Kit detects no text on the captured photo
 */
@Composable
private fun NoTextDetectedView(
    onRetake: () -> Unit,
    onManualEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
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
                Text(text = "🔍", fontSize = 48.sp)

                Text(
                    text = "We couldn't read any text",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Text(
                    text = "The photo might be too blurry, too dark, or taken at an angle. Please try again with better lighting and hold the camera steady.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VS_OnSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Button(
                    onClick = onRetake,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    shape = PillShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔁 Retake Photo", color = Color.White, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onManualEntry,
                    shape = PillShape,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✍️ Enter Prescription Manually", color = VS_OnBackground)
                }
            }
        }
    }
}
