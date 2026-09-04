package com.vitalsense.app.feature.ot

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.OtSurgeryBooking
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun OtSchedulerScreen(
    bookings: List<OtSurgeryBooking>,
    onBackClick: () -> Unit,
    onBookSurgery: (OtSurgeryBooking) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBookSurgeryDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.md, bottom = Spacing.xxl)
    ) {
        // 1. Header with Back Navigation
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBackClick,
                    shape = PillShape,
                    color = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text("←", color = VS_OnBackground, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.roleDoctor), style = MaterialTheme.typography.labelMedium, color = VS_OnBackground)
                    }
                }

                Surface(
                    shape = PillShape,
                    color = VS_PrimaryContainer,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Surgical Care · OT Module",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_PrimaryContainer,
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                    )
                }
            }
        }

        // 2. Hero Operation Theatre HUD
        item {
            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(1.dp, VS_Outline)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🔪 ${stringResource(R.string.otScheduler)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.otSubtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showBookSurgeryDialog = true },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VS_Primary,
                                contentColor = VS_OnBackground
                            ),
                            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                            modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                        ) {
                            Text("+ ${stringResource(R.string.bookOtSlot)}", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    HorizontalDivider(color = VS_Outline)

                    // Chief Surgeon Spotlight
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("👨‍⚕️", fontSize = 20.sp)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lead Surgeon: Dr. Ayushman Dev Singh",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "MDS, Maxillofacial Trauma & Reconstructive Surgery",
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_PrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // 3. Upcoming Surgical Schedule
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Surgical Roster & Bookings (${bookings.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "PAC Validated",
                    style = MaterialTheme.typography.labelSmall,
                    color = VS_Success
                )
            }
        }

        if (bookings.isEmpty()) {
            item {
                VitalSenseCard {
                    Text(
                        text = "No surgical procedures currently scheduled in OT.",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                }
            }
        }

        items(bookings, key = { it.id }) { booking ->
            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(1.dp, if (booking.status == "Completed") VS_Outline else VS_Primary.copy(alpha = 0.4f))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Surface(
                                shape = PillShape,
                                color = VS_SurfaceVariant,
                                border = BorderStroke(1.dp, VS_Outline)
                            ) {
                                Text(
                                    text = booking.otRoomName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground,
                                    modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "· ${booking.scheduledDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = if (booking.pacCleared) VS_SuccessContainer else VS_ErrorContainer
                        ) {
                            Text(
                                text = if (booking.pacCleared) "✓ PAC CLEARED" else "⚠ PAC PENDING",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = if (booking.pacCleared) VS_Success else VS_Error,
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = booking.surgeryName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    HorizontalDivider(color = VS_Outline)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Patient", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(booking.patientName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        }
                        Column {
                            Text("Time Slot", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(booking.scheduledTimeSlot, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Operating Surgeon", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(booking.surgeonName, style = MaterialTheme.typography.labelSmall, color = VS_PrimaryContainer)
                        }
                        Column {
                            Text("Anesthetist", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(booking.anesthetistName, style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    // Book OT Slot Dialog
    if (showBookSurgeryDialog) {
        var patientName by remember { mutableStateOf("") }
        var surgeryName by remember { mutableStateOf("") }
        var otRoom by remember { mutableStateOf("Trauma & Ortho OT-2") }
        var surgeonName by remember { mutableStateOf("Dr. Ayushman Dev Singh") }
        var anesthetistName by remember { mutableStateOf("Dr. S. K. Verma (Sr. Anesthetist)") }
        var date by remember { mutableStateOf("Tomorrow") }
        var timeSlot by remember { mutableStateOf("09:00 AM - 11:30 AM") }
        var pacCleared by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showBookSurgeryDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.bookOtSlot),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    VitalSenseTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = "Patient Full Name",
                        placeholder = "e.g. Ramesh Kumar"
                    )

                    VitalSenseTextField(
                        value = surgeryName,
                        onValueChange = { surgeryName = it },
                        label = "Surgical Procedure Name",
                        placeholder = "e.g. Open Reduction & Internal Fixation"
                    )

                    VitalSenseTextField(
                        value = otRoom,
                        onValueChange = { otRoom = it },
                        label = "OT Suite Room",
                        placeholder = "e.g. Major OT-1 or Trauma OT-2"
                    )

                    VitalSenseTextField(
                        value = surgeonName,
                        onValueChange = { surgeonName = it },
                        label = "Primary Operating Surgeon",
                        placeholder = "Dr. Ayushman Dev Singh"
                    )

                    VitalSenseTextField(
                        value = anesthetistName,
                        onValueChange = { anesthetistName = it },
                        label = "Attending Anesthetist",
                        placeholder = "Dr. S. K. Verma"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        VitalSenseTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = "Date",
                            placeholder = "Tomorrow",
                            modifier = Modifier.weight(1f)
                        )
                        VitalSenseTextField(
                            value = timeSlot,
                            onValueChange = { timeSlot = it },
                            label = "Time Slot",
                            placeholder = "09:00 AM",
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pre-Anesthesia Checkup (PAC) Cleared",
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_OnBackground
                        )
                        Switch(
                            checked = pacCleared,
                            onCheckedChange = { pacCleared = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VS_OnBackground,
                                checkedTrackColor = VS_Primary
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newBooking = OtSurgeryBooking(
                            id = "ot_${System.currentTimeMillis()}",
                            otRoomName = otRoom.ifBlank { "Major OT-1" },
                            patientId = "pat_booked",
                            patientName = patientName.ifBlank { "Admitted Patient" },
                            surgeryName = surgeryName.ifBlank { "Surgical Exploration" },
                            surgeonName = surgeonName.ifBlank { "Dr. Ayushman Dev Singh" },
                            anesthetistName = anesthetistName.ifBlank { "Duty Anesthetist" },
                            scheduledDate = date.ifBlank { "Tomorrow" },
                            scheduledTimeSlot = timeSlot.ifBlank { "10:00 AM - 12:00 PM" },
                            pacCleared = pacCleared,
                            status = "Scheduled"
                        )
                        onBookSurgery(newBooking)
                        showBookSurgeryDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    enabled = patientName.isNotBlank() && surgeryName.isNotBlank()
                ) {
                    Text("Confirm OT Slot", style = MaterialTheme.typography.labelSmall)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookSurgeryDialog = false }) {
                    Text("Cancel", color = VS_OnSurfaceVariant)
                }
            },
            containerColor = VS_Surface,
            tonalElevation = 6.dp
        )
    }
}
