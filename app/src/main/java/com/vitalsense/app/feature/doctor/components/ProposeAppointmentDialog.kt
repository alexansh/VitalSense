package com.vitalsense.app.feature.doctor.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

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

@Composable
fun ProposeAppointmentDialog(
    patient: Patient?,
    patientNameFallback: String,
    onDismiss: () -> Unit,
    onPropose: (date: String, timeSlot: String) -> Unit
) {
    var selectedDate by remember { mutableStateOf("18 Aug 2026") }
    var selectedTimeSlot by remember { mutableStateOf("11:30 AM") }

    val sampleDates = listOf("16 Aug 2026", "17 Aug 2026", "18 Aug 2026", "19 Aug 2026", "20 Aug 2026")
    val sampleSlots = listOf("09:30 AM", "10:30 AM", "11:30 AM", "02:00 PM", "03:30 PM", "04:30 PM")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            color = VS_Background,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.proposeAppt),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "To: ${patient?.name ?: patientNameFallback}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Text(text = "✕", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Date Selection
                Text(
                    text = stringResource(R.string.selectProposedDate),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sampleDates.take(3).forEach { date ->
                        val isSelected = selectedDate == date
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDate = date },
                            label = { Text(date, fontSize = 11.sp) },
                            shape = PillShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VS_SurfaceVariant,
                                selectedLabelColor = VS_Primary
                            )
                        )
                    }
                }

                // Time Slot Selection
                Text(
                    text = stringResource(R.string.selectTimeSlotDialog),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sampleSlots.take(3).forEach { slot ->
                        val isSelected = selectedTimeSlot == slot
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTimeSlot = slot },
                            label = { Text(slot, fontSize = 11.sp) },
                            shape = PillShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VS_SurfaceVariant,
                                selectedLabelColor = VS_Primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = PillShape
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onPropose(selectedDate, selectedTimeSlot)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.3f),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_PrimaryContainer, contentColor = VS_OnBackground)
                    ) {
                        Text(stringResource(R.string.sendProposalBtn), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
