package com.vitalsense.app.feature.doctor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAppointmentDialog(
    patients: List<Patient>,
    onDismiss: () -> Unit,
    onPropose: (patientId: String, patientName: String, date: String, timeSlot: String) -> Unit
) {
    var selectedPatient by remember { mutableStateOf(patients.firstOrNull()) }
    var selectedDate by remember { mutableStateOf("19 Aug 2026") }
    var selectedTimeSlot by remember { mutableStateOf("10:30 AM") }
    var patientExpanded by remember { mutableStateOf(false) }

    val sampleDates = listOf("18 Aug 2026", "19 Aug 2026", "20 Aug 2026", "21 Aug 2026")
    val sampleSlots = listOf("09:30 AM", "10:30 AM", "11:30 AM", "02:30 PM", "04:00 PM")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📅 Schedule New Appointment",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Propose consultation time to patient",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // Select Patient Dropdown
                Text(
                    text = "Select Patient:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                ExposedDropdownMenuBox(
                    expanded = patientExpanded,
                    onExpandedChange = { patientExpanded = !patientExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedPatient?.let { "${it.name} (${it.villageName})" } ?: "Select Patient",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = patientExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
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

                    ExposedDropdownMenu(
                        expanded = patientExpanded,
                        onDismissRequest = { patientExpanded = false }
                    ) {
                        patients.forEach { pat ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = pat.name, fontWeight = FontWeight.Bold, color = VS_OnBackground)
                                        Text(text = "${pat.villageName} · ${pat.phone}", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                                    }
                                },
                                onClick = {
                                    selectedPatient = pat
                                    patientExpanded = false
                                }
                            )
                        }
                    }
                }

                // Date Picker Chips
                Text(
                    text = "Select Date:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    sampleDates.forEach { date ->
                        val isSelected = selectedDate == date
                        Surface(
                            onClick = { selectedDate = date },
                            shape = PillShape,
                            color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = date.split(" ").take(2).joinToString(" "),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                    )
                                )
                            }
                        }
                    }
                }

                // Time Slots Chips
                Text(
                    text = "Available Time Slot:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    sampleSlots.take(3).forEach { slot ->
                        val isSelected = selectedTimeSlot == slot
                        Surface(
                            onClick = { selectedTimeSlot = slot },
                            shape = PillShape,
                            color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = slot,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Submit Proposal Button
                Button(
                    onClick = {
                        selectedPatient?.let {
                            onPropose(it.id, it.name, selectedDate, selectedTimeSlot)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VS_Primary,
                        contentColor = VS_OnBackground
                    ),
                    enabled = selectedPatient != null
                ) {
                    Text(
                        text = "Send Appointment Proposal ✓",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
