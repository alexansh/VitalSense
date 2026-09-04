package com.vitalsense.app.feature.doctor.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.DoctorDaySlotConfig
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*

@Composable
fun DoctorSlotConfigDialog(
    currentConfig: DoctorDaySlotConfig?,
    onDismiss: () -> Unit,
    onSave: (capacity: Int, isWalkInOpen: Boolean, startTime: String, endTime: String) -> Unit
) {
    var capacityText by remember { mutableStateOf((currentConfig?.capacity ?: 20).toString()) }
    var isWalkInOpen by remember { mutableStateOf(currentConfig?.isWalkInOpen ?: true) }
    var startTime by remember { mutableStateOf(currentConfig?.startTime ?: "09:00 AM") }
    var endTime by remember { mutableStateOf(currentConfig?.endTime ?: "05:00 PM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = stringResource(R.string.configureClinicQueueSlots),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = stringResource(R.string.manageCapacityWalkInRules),
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                VitalSenseTextField(
                    value = capacityText,
                    onValueChange = { capacityText = it.filter { ch -> ch.isDigit() } },
                    label = "Max Daily Scheduled Capacity",
                    placeholder = "e.g. 25"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = "Start Time"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = endTime,
                            onValueChange = { endTime = it },
                            label = "End Time"
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VS_SurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.acceptWalkInQueue),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.allowDirectCheckinNoBooking),
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isWalkInOpen,
                            onCheckedChange = { isWalkInOpen = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = VS_Primary
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            VitalSenseButton(
                text = stringResource(R.string.saveConfiguration),
                onClick = {
                    val cap = capacityText.toIntOrNull() ?: 20
                    onSave(cap, isWalkInOpen, startTime, endTime)
                    onDismiss()
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = VS_Surface
    )
}
