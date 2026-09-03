package com.vitalsense.app.feature.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.doctor.components.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaseDetailScreen(
    record: ConditionRecord,
    patient: Patient?,
    priorPrescriptions: List<Prescription>,
    dispensaryStock: List<DispensaryItem>,
    currentDoctor: Doctor,
    onBack: () -> Unit,
    onSubmitResponse: (responseText: String, privateNotes: String?) -> Unit,
    onIssuePrescription: (medicines: List<PrescribedMedicine>, instructions: String) -> Unit,
    onProposeAppointment: (date: String, timeSlot: String) -> Unit,
    onReferCase: () -> Unit,
    modifier: Modifier = Modifier
) {
    var responseText by remember(record) { mutableStateOf(record.doctorResponse ?: "") }
    var privateNotes by remember(record) { mutableStateOf(record.privateDoctorNotes ?: "") }
    var showPrivateNotes by remember { mutableStateOf(record.privateDoctorNotes?.isNotBlank() == true) }

    var showHealthCardDialog by remember { mutableStateOf(false) }
    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var showAppointmentDialog by remember { mutableStateOf(false) }

    val isMentalHealthCase = record.category == ConditionCategory.MENTAL_HEALTH ||
            record.requestedDoctorType == DoctorSpecialty.PSYCHOLOGIST

    // §4.7 Quick Reply Chips
    val quickReplies = listOf(
        "💧 Rest, hydration & light boiled fluids for 3 days",
        "🌡️ If fever exceeds 102°F, report immediately to PHC",
        "💊 Continue prescribed antibiotics for full course",
        "🥗 Maintain high iron diet with green leafy vegetables",
        "🧘 Practice daily 4-7-8 deep breathing exercises"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 36.dp)
    ) {
        // 1. Header & Back Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = PillShape,
                    color = SurfaceWhite,
                    shadowElevation = 1.dp,
                    modifier = Modifier.clickable { onBack() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "←", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Case Queue", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Surface(
                    shape = PillShape,
                    color = Color(record.status.colorHex).copy(alpha = 0.35f)
                ) {
                    Text(
                        text = record.status.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = TextPrimaryNearBlack
                    )
                }
            }
        }

        // 2. Patient Banner & View-Only Health Card Trigger (§2.1 & §3)
        item {
            VitalSenseCard(backgroundColor = SurfaceWhite, elevation = 2.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = record.patientName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                            Text(
                                text = "Village: ${record.villageName} · Category: ${record.category.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryMuted
                            )
                        }
                        SeverityBadge(severity = record.severity)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (record.ashaProxyLogged) {
                            Surface(shape = PillShape, color = LavenderSecondary.copy(alpha = 0.4f)) {
                                Text(
                                    text = "🤝 Submitted via ASHA Helper",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Direct Patient Submission",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryMuted
                            )
                        }

                        // View-Only Health Card Trigger
                        patient?.let { pat ->
                            OutlinedButton(
                                onClick = { showHealthCardDialog = true },
                                shape = PillShape,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(text = "🪪 View Health Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Mental Health Origin Flag (§2.6)
        if (isMentalHealthCase) {
            item {
                Surface(
                    color = LavenderSecondary.copy(alpha = 0.35f),
                    shape = CardShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🧠", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "Mental Health Referral Flag (§2.6)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                            Text(
                                text = "Originates from the Patient Mental Stress Relief section. Patient reports high emotional distress and sleep disturbance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimaryNearBlack
                            )
                        }
                    }
                }
            }
        }

        // 4. Referral Origin Info (if case was transferred by another doctor §4.3)
        if (record.referredByDoctorName != null) {
            item {
                Surface(
                    color = BlushPinkTertiary.copy(alpha = 0.35f),
                    shape = CardShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "🔄 Transferred by: ${record.referredByDoctorName}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "Referral Notes: ${record.referralNotes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimaryNearBlack
                        )
                    }
                }
            }
        }

        // 5. Clinical Symptoms Description
        item {
            VitalSenseCard(backgroundColor = SurfaceWhite) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Reported Symptoms & Clinical Notes",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimaryNearBlack
                    )
                    Text(
                        text = record.notes.ifBlank { "No detailed symptom notes provided." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimaryNearBlack
                    )
                }
            }
        }

        // 6. Action Launchers (Prescription, Propose Appt, Refer)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showPrescriptionDialog = true },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LimePrimary, contentColor = TextPrimaryNearBlack),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "💊 Issue Rx", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { showAppointmentDialog = true },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LavenderSecondary, contentColor = TextPrimaryNearBlack),
                    modifier = Modifier.weight(1.1f)
                ) {
                    Text(text = "📅 Propose Appt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onReferCase,
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BlushPinkTertiary, contentColor = TextPrimaryNearBlack),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "🔄 Refer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 7. Medical Response Composer (§2.2)
        item {
            VitalSenseCard(backgroundColor = SurfaceWhite, elevation = 2.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "✍️ Compose Medical Guidance (Patient Facing)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimaryNearBlack
                    )

                    // Quick replies (§4.7)
                    Text(
                        text = "Quick Clinical Templates (§4.7):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextSecondaryMuted
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickReplies.forEach { template ->
                            SuggestionChip(
                                onClick = {
                                    responseText = if (responseText.isBlank()) template else "$responseText\n$template"
                                },
                                label = { Text(text = template, fontSize = 11.sp) },
                                shape = PillShape
                            )
                        }
                    }

                    OutlinedTextField(
                        value = responseText,
                        onValueChange = { responseText = it },
                        label = { Text("Medical Guidance & Advice for Patient") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCreamBackground,
                            unfocusedContainerColor = WarmCreamBackground
                        )
                    )

                    // Private Notes Toggle (§4.4)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivateNotes = !showPrivateNotes },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🔒 Private Doctor Observations (§4.4)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondaryMuted
                        )
                        Text(text = if (showPrivateNotes) "▲ Hide" else "▼ Add Notes", fontSize = 12.sp, color = TextSecondaryMuted)
                    }

                    AnimatedVisibility(visible = showPrivateNotes) {
                        OutlinedTextField(
                            value = privateNotes,
                            onValueChange = { privateNotes = it },
                            placeholder = { Text("Internal notes (visible only to doctors & clinical team)...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = WarmCreamBackground,
                                unfocusedContainerColor = WarmCreamBackground
                            )
                        )
                    }

                    Button(
                        onClick = {
                            if (responseText.isNotBlank()) {
                                onSubmitResponse(responseText, privateNotes.takeIf { it.isNotBlank() })
                            }
                        },
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkCharcoal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Submit & Attach Guidance to Case ✓", color = LimePrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 8. Prior Prescriptions & Patient History (§2.1, §4.5)
        item {
            Text(
                text = "Prior Prescriptions History (${priorPrescriptions.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimaryNearBlack
            )
        }

        if (priorPrescriptions.isEmpty()) {
            item {
                Text(
                    text = "No prior prescriptions found for this patient.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        } else {
            items(priorPrescriptions) { rx ->
                VitalSenseCard(backgroundColor = SurfaceWhite) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Prescribed by ${rx.doctorName}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                            Text(text = rx.dateFormatted, style = MaterialTheme.typography.labelSmall, color = TextSecondaryMuted)
                        }
                        Text(
                            text = rx.medicines.joinToString(separator = " • ") { "${it.name} (${it.dosage})" },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimaryNearBlack
                        )
                        if (rx.instructions.isNotBlank()) {
                            Text(
                                text = "Advice: ${rx.instructions}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryMuted
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showHealthCardDialog && patient != null) {
        PatientHealthCardViewOnlyDialog(
            patient = patient,
            onDismiss = { showHealthCardDialog = false }
        )
    }

    if (showPrescriptionDialog) {
        PrescriptionComposerDialog(
            patient = patient,
            patientNameFallback = record.patientName,
            caseId = record.id,
            dispensaryStock = dispensaryStock,
            onDismiss = { showPrescriptionDialog = false },
            onIssuePrescription = onIssuePrescription
        )
    }

    if (showAppointmentDialog) {
        ProposeAppointmentDialog(
            patient = patient,
            patientNameFallback = record.patientName,
            onDismiss = { showAppointmentDialog = false },
            onPropose = onProposeAppointment
        )
    }
}
