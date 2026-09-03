package com.vitalsense.app.feature.patient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun AppointmentsScreen(
    appointments: List<Appointment>,
    patient: Patient,
    onProposeAppointment: (Appointment) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var showRequestDialog by remember { mutableStateOf(false) }

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
                    text = "My Doctor Appointments",
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
                title = "Appointments Portal",
                message = "Propose a checkup date or view status of doctor consultation requests."
            )
        }

        item {
            VitalSenseButton(
                text = "+ Propose New Appointment",
                onClick = { showRequestDialog = true },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.DARK
            )
        }

        if (appointments.isEmpty()) {
            item {
                VitalSenseCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📅", fontSize = 32.sp)
                        Text(
                            text = "No Active Appointments",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "Tap above to propose a consultation date with a doctor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        } else {
            items(appointments) { appt ->
                VitalSenseCard(elevation = 2.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = DarkCharcoal)
                                Text(
                                    text = appt.doctorName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimaryNearBlack
                                )
                            }

                            val (statusBg, statusText) = when (appt.status) {
                                "Confirmed" -> SoftMintSuccess to "CONFIRMED ✓"
                                "Pending" -> AmberWarning to "PENDING ⏳"
                                "Declined" -> CoralAlert to "DECLINED ✕"
                                "Completed" -> LavenderSecondary to "COMPLETED ✓"
                                else -> WarmCreamBackground to appt.status.uppercase()
                            }

                            Surface(shape = PillShape, color = statusBg) {
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = TextPrimaryNearBlack
                                )
                            }
                        }

                        Text(
                            text = "Date: ${appt.dateFormatted} · Slot: ${appt.timeSlot}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = TextPrimaryNearBlack
                        )

                        if (!appt.outcomeNotes.isNullOrBlank()) {
                            Text(
                                text = "Notes: ${appt.outcomeNotes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryMuted
                            )
                        }
                    }
                }
            }
        }
    }

    if (showRequestDialog) {
        var requestedDate by remember { mutableStateOf("Tomorrow, 10:00 AM") }
        var selectedSlot by remember { mutableStateOf("Morning (10:00 - 12:00)") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showRequestDialog = false },
            title = { Text("Propose Appointment", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = requestedDate,
                        onValueChange = { requestedDate = it },
                        label = { Text("Preferred Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = selectedSlot,
                        onValueChange = { selectedSlot = it },
                        label = { Text("Time Window") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Reason for visit") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                VitalSenseButton(
                    text = "Submit Request",
                    onClick = {
                        val newAppt = Appointment(
                            id = java.util.UUID.randomUUID().toString(),
                            patientId = patient.id,
                            patientName = patient.name,
                            doctorId = "doc-01",
                            doctorName = "Dr. Rajesh Varma",
                            doctorSpecialty = "General Physician",
                            dateFormatted = requestedDate,
                            timeSlot = selectedSlot,
                            status = "Pending",
                            proposedBy = com.vitalsense.app.core.data.model.UserRole.PATIENT,
                            outcomeNotes = notes
                        )
                        onProposeAppointment(newAppt)
                        showRequestDialog = false
                    },
                    style = ButtonStyle.PRIMARY
                )
            },
            dismissButton = {
                TextButton(onClick = { showRequestDialog = false }) {
                    Text("Cancel", color = TextSecondaryMuted)
                }
            }
        )
    }
}