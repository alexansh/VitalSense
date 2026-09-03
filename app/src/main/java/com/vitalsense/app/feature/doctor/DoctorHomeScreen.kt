package com.vitalsense.app.feature.doctor

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
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.doctor.components.ScheduleAppointmentDialog

@Composable
fun DoctorHomeScreen(
    doctor: Doctor,
    cases: List<ConditionRecord>,
    appointments: List<Appointment>,
    dispensaryStock: List<DispensaryItem>,
    patients: List<Patient> = emptyList(),
    onSelectCase: (ConditionRecord) -> Unit,
    onAcceptAppointment: (String) -> Unit = {},
    onDeclineAppointment: (String) -> Unit = {},
    onProposeAppointment: (patientId: String, patientName: String, date: String, timeSlot: String) -> Unit = { _, _, _, _ -> },
    onViewIncomingReferrals: () -> Unit = {},
    onViewSentReferrals: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showScheduleDialog by remember { mutableStateOf(false) }

    val pendingCases = cases.filter { it.status == CaseStatus.PENDING_REVIEW || it.status == CaseStatus.IN_PROGRESS }
    val severeCount = cases.count { it.severity == SeverityLevel.SEVERE || it.severity == SeverityLevel.HIGH }
    val lowStockCount = dispensaryStock.count { it.isLowStock }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 36.dp)
    ) {
        // 1. Doctor Header
        item {
            Column {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 22.sp),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = "${doctor.specialty.displayName} · ${doctor.hospitalName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        }

        // 2. Metrics summary KPI Cards (§4.6)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = LimePrimary.copy(alpha = 0.5f)
                ) {
                    Column {
                        Text(
                            text = "${pendingCases.size}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(text = "Pending Cases", style = MaterialTheme.typography.labelSmall)
                    }
                }

                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = if (severeCount > 0) CoralAlert.copy(alpha = 0.25f) else SoftMintSuccess.copy(alpha = 0.35f)
                ) {
                    Column {
                        Text(
                            text = "$severeCount",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (severeCount > 0) CoralAlert else TextPrimaryNearBlack
                            )
                        )
                        Text(text = "High / Severe Alerts", style = MaterialTheme.typography.labelSmall)
                    }
                }

                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = LavenderSecondary.copy(alpha = 0.4f)
                ) {
                    Column {
                        Text(
                            text = "${appointments.size}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(text = "Appointments", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // 2.5 Quick Referral Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VitalSenseButton(
                    text = "📥 Incoming Referrals",
                    onClick = onViewIncomingReferrals,
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.DARK
                )
                VitalSenseButton(
                    text = "📤 Sent Referrals",
                    onClick = onViewSentReferrals,
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.SECONDARY
                )
            }
        }

        // 3. Section: Triage Patient Cases Queue (§2.1, §4.1)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Specialist Case Queue (${cases.size})",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )

                Surface(shape = PillShape, color = SurfaceWhite) {
                    Text(
                        text = "Scoped to ${doctor.specialty.displayName}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = TextSecondaryMuted
                    )
                }
            }
        }

        if (cases.isEmpty()) {
            item {
                VitalSenseCard(backgroundColor = SurfaceWhite) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "✨", fontSize = 28.sp)
                        Text(
                            text = "No pending cases in your specialty queue",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "All patient cases for ${doctor.specialty.displayName} have been reviewed.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        } else {
            items(cases) { record ->
                val isMentalHealth = record.category == ConditionCategory.MENTAL_HEALTH ||
                        record.requestedDoctorType == DoctorSpecialty.PSYCHOLOGIST

                VitalSenseCard(elevation = 2.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = record.patientName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Village: ${record.villageName} · ${record.category.displayName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryMuted
                                )
                            }
                            SeverityBadge(severity = record.severity)
                        }

                        // Mental Health Referral Flag (§2.6)
                        if (isMentalHealth) {
                            Surface(shape = PillShape, color = LavenderSecondary.copy(alpha = 0.45f)) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "🧠", fontSize = 11.sp)
                                    Text(
                                        text = "Mental Health Referral (Patient Stress Section)",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimaryNearBlack
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Symptoms: ${record.notes}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryNearBlack
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Status Pill
                            Surface(
                                shape = PillShape,
                                color = Color(record.status.colorHex).copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = record.status.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    color = TextPrimaryNearBlack
                                )
                            }

                            Button(
                                onClick = { onSelectCase(record) },
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(containerColor = DarkCharcoal)
                            ) {
                                Text(text = "Review Case →", color = LimePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 4. Section: Upcoming Appointments (§2.4)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scheduled Appointments (${appointments.size})",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )

                // Prominent Propose / Schedule Appointment Button
                Button(
                    onClick = { showScheduleDialog = true },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LavenderSecondary, contentColor = TextPrimaryNearBlack),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(text = "➕ Propose Appt", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (appointments.isEmpty()) {
            item {
                Text(
                    text = "No scheduled appointments found.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        } else {
            items(appointments) { appointment ->
                val isPending = appointment.status.contains("Pending", ignoreCase = true)

                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = appointment.patientName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${appointment.dateFormatted} at ${appointment.timeSlot}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryMuted
                                )
                            }
                            Surface(
                                shape = PillShape,
                                color = if (appointment.status == "Confirmed") SoftMintSuccess.copy(alpha = 0.5f) else AmberWarning.copy(alpha = 0.4f)
                            ) {
                                Text(
                                    text = appointment.status,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (isPending) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { onDeclineAppointment(appointment.id) },
                                    shape = PillShape
                                ) {
                                    Text("Decline", color = CoralAlert, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = { onAcceptAppointment(appointment.id) },
                                    shape = PillShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = SoftMintSuccess),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("Accept ✓", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Section: Dispensary Stock & Availability Check (§2.3)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dispensary Stock Check",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )
                if (lowStockCount > 0) {
                    Surface(shape = PillShape, color = CoralAlert.copy(alpha = 0.2f)) {
                        Text(
                            text = "$lowStockCount LOW STOCK",
                            style = MaterialTheme.typography.labelSmall.copy(color = CoralAlert, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        items(dispensaryStock) { item ->
            VitalSenseCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = item.medicineName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${item.availableQuantity} ${item.unit}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (item.isLowStock) CoralAlert else TextPrimaryNearBlack
                            )
                        )
                        if (item.isLowStock) {
                            Surface(shape = PillShape, color = CoralAlert.copy(alpha = 0.2f)) {
                                Text(
                                    text = "LOW",
                                    style = MaterialTheme.typography.labelSmall.copy(color = CoralAlert, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showScheduleDialog) {
        ScheduleAppointmentDialog(
            patients = patients,
            onDismiss = { showScheduleDialog = false },
            onPropose = { patientId, patientName, date, timeSlot ->
                onProposeAppointment(patientId, patientName, date, timeSlot)
            }
        )
    }
}
