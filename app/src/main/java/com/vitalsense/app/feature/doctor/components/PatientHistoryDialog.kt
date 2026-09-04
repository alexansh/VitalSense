package com.vitalsense.app.feature.doctor.components

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
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun PatientHistoryDialog(
    patient: Patient,
    conditions: List<ConditionRecord>,
    prescriptions: List<Prescription>,
    appointments: List<Appointment> = emptyList(),
    medicalHistory: List<MedicalHistoryEntry> = emptyList(),
    onDismiss: () -> Unit
) {
    val patientConditions = conditions.filter { it.patientId == patient.id }
    val patientPrescriptions = prescriptions.filter { it.patientId == patient.id }
    val patientAppointments = appointments.filter { it.patientId == patient.id }

    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    var selectedTab by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                // 1. Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.medicalHistoryAndRecords),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Patient: ${patient.name} (${patient.age}y / ${patient.gender})",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // 2. Patient Demographics & Health Profile Card
                VitalSenseCard(
                    backgroundColor = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Outline)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Village: ${patient.villageName}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            SeverityBadge(severity = patient.currentRiskLevel)
                        }

                        Text(
                            text = "Last Condition: ${patient.lastCondition} · Last Visit: ${patient.lastVisitDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                        Text(
                            text = "Assigned ASHA: ${patient.ashaWorkerName} · Emergency: ${patient.emergencyContact}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_PrimaryContainer
                        )
                    }
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = VS_Primary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(stringResource(R.string.recordsHeading)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(stringResource(R.string.medicalHistoryTab)) }
                    )
                }

                if (selectedTab == 0) {
                    // 3. Past Conditions Log
                    Text(
                    text = "Condition Submissions (${patientConditions.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                if (patientConditions.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noConditionRecordsLogged),
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                } else {
                    patientConditions.forEach { record ->
                        VitalSenseCard {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${record.category.displayName} (${dateFormat.format(Date(record.timestamp))})",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    SeverityBadge(severity = record.severity)
                                }

                                Text(
                                    text = "Symptoms: ${record.notes}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = VS_OnBackground
                                )

                                if (record.doctorResponse != null) {
                                    Surface(
                                        color = VS_PrimaryContainer,
                                        shape = CardShape,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(Spacing.sm)) {
                                            Text(
                                                text = "Doctor Advice: ${record.doctorResponse}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = VS_PrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Past Prescriptions Log
                Text(
                    text = "Prescriptions on File (${patientPrescriptions.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                if (patientPrescriptions.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noPriorPrescriptionsUploaded),
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                } else {
                    patientPrescriptions.forEach { rx ->
                        VitalSenseCard {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "By ${rx.doctorName} (${rx.dateFormatted})",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    if (rx.isOcrExtracted) {
                                        Surface(shape = PillShape, color = VS_SuccessContainer) {
                                            Text(
                                                text = stringResource(R.string.aiDigitizedBadge),
                                                style = MaterialTheme.typography.labelSmall.copy(color = VS_OnSuccessContainer, fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                rx.medicines.forEach { med ->
                                    Text(
                                        text = "• ${med.name} (${med.dosage}) - ${med.frequency} for ${med.duration}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnBackground
                                    )
                                }

                                if (rx.instructions.isNotBlank()) {
                                    Text(
                                        text = "Note: ${rx.instructions}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Past / Scheduled Appointments
                if (patientAppointments.isNotEmpty()) {
                    Text(
                        text = "Appointments History (${patientAppointments.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    patientAppointments.forEach { appt ->
                        VitalSenseCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${appt.dateFormatted} at ${appt.timeSlot}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "Status: ${appt.status}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                                Surface(
                                    shape = PillShape,
                                    color = if (appt.status.contains("Pending", true)) VS_WarningContainer else VS_SuccessContainer
                                ) {
                                    Text(
                                        text = appt.status,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (appt.status.contains("Pending", true)) VS_Warning else VS_OnSuccessContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                    // Medical History Chronological Tab
                    if (medicalHistory.isEmpty()) {
                        Text(
                            text = stringResource(R.string.noMedicalHistory),
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    } else {
                        val steps = medicalHistory.sortedByDescending { it.timestamp }.map { entry ->
                            com.vitalsense.app.core.ui.components.VSTimelineStep(
                                title = entry.title,
                                timestamp = entry.dateFormatted,
                                description = "${entry.details}\nBy ${entry.doctorName}",
                                completed = true
                            )
                        }
                        com.vitalsense.app.core.ui.components.VSTimeline(steps = steps, modifier = Modifier.fillMaxWidth())
                    }
                }

                // Close Button
                VitalSenseButton(
                    text = stringResource(R.string.closeMedicalHistory),
                    onClick = onDismiss,
                    style = com.vitalsense.app.core.ui.components.ButtonStyle.PRIMARY
                )
            }
        }
    }
}
