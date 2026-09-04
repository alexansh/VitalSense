package com.vitalsense.app.feature.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.touchSpring
import com.vitalsense.app.feature.doctor.components.*
import com.vitalsense.app.feature.doctor.components.PatientHistoryDialog
import com.vitalsense.app.feature.doctor.components.PrescriptionComposerDialog
import com.vitalsense.app.feature.doctor.components.ProposeAppointmentDialog
import com.vitalsense.app.feature.doctor.components.ReferCaseDialog
import com.vitalsense.app.feature.doctor.components.TeleConsultationModal
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaseDetailScreen(
    record: ConditionRecord,
    patient: Patient?,
    priorPrescriptions: List<Prescription>,
    dispensaryStock: List<DispensaryItem>,
    currentDoctor: Doctor,
    medicalHistory: List<MedicalHistoryEntry> = emptyList(),
    allConditions: List<ConditionRecord> = emptyList(),
    allAppointments: List<Appointment> = emptyList(),
    onBack: () -> Unit,
    onSubmitResponse: (responseText: String, privateNotes: String?) -> Unit,
    onIssuePrescription: (medicines: List<PrescribedMedicine>, instructions: String) -> Unit,
    onProposeAppointment: (date: String, timeSlot: String) -> Unit,
    onReferCase: (targetSpecialty: DoctorSpecialty, referralNotes: String) -> Unit,
    onOrderLabTest: (LabReport) -> Unit = {},
    onIssueMedicalCertificate: (MedicalCertificate) -> Unit = {},
    referrals: List<Referral> = emptyList(),
    onSendStructuredReferral: (Referral) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var responseText by remember(record) { mutableStateOf(record.doctorResponse ?: "") }
    var privateNotes by remember(record) { mutableStateOf(record.privateDoctorNotes ?: "") }
    var showPrivateNotes by remember { mutableStateOf(record.privateDoctorNotes?.isNotBlank() == true) }

    var showHealthCardDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var showAppointmentDialog by remember { mutableStateOf(false) }
    var showReferDialog by remember { mutableStateOf(false) }
    var showTeleConsultModal by remember { mutableStateOf(false) }
    var showMedicalCertDialog by remember { mutableStateOf(false) }
    var showOrderLabDialog by remember { mutableStateOf(false) }
    var showScanPrescriptionDialog by remember { mutableStateOf(false) }
    var isMedicalHistoryExpanded by remember { mutableStateOf(false) }

    val isMentalHealthCase = record.category == ConditionCategory.MENTAL_HEALTH ||
            record.requestedDoctorType == DoctorSpecialty.PSYCHOLOGIST

    val quickReplies = listOf(
        "💧 Rest, hydration & light fluids for 3 days",
        "🌡️ If fever exceeds 102°F, report to PHC",
        "💊 Continue antibiotics for full course",
        "🥗 High iron diet with green leafy vegetables",
        "🧘 Practice daily 4-7-8 breathing exercises"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GlumeBackground)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.sm, bottom = Spacing.xxl)
    ) {
        // 1. Minimal Header & Back Navigation Pill
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = PillShape,
                    color = GlumeSurfaceCard,
                    border = BorderStroke(1.dp, GlumeBorder),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(text = "←", style = MaterialTheme.typography.labelLarge, color = GlumeTextPrimary)
                        Text(
                            text = stringResource(R.string.caseQueue),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = GlumeTextPrimary
                        )
                    }
                }

                Surface(
                    shape = PillShape,
                    color = GlumeSurfaceElevated
                ) {
                    Text(
                        text = record.status.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp),
                        color = GlumeTextSecondary
                    )
                }
            }
        }

        // 2. Patient & Condition Summary Card
        item {
            VitalSenseCard(
                backgroundColor = GlumeSurfaceCard,
                elevation = 0.dp
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = record.patientName,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = GlumeTextPrimary
                            )
                            Text(
                                text = "${stringResource(R.string.village)}: ${record.villageName} · ${record.category.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary
                            )
                        }
                        SeverityBadge(severity = record.severity)
                    }

                    HorizontalDivider(color = GlumeBorder)

                    Text(
                        text = stringResource(R.string.reportedSymptoms),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = GlumeTextSecondary
                    )
                    Text(
                        text = record.notes,
                        style = MaterialTheme.typography.bodyLarge,
                        color = GlumeTextPrimary
                    )

                    HorizontalDivider(color = GlumeBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (record.ashaProxyLogged) {
                            Surface(shape = PillShape, color = GlumePrimaryPurpleContainer) {
                                Text(
                                    text = "🤝 Submitted via ASHA Helper",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = GlumePrimaryPurpleLight
                                    ),
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Direct Patient Submission",
                                style = MaterialTheme.typography.labelSmall,
                                color = GlumeTextSecondary
                            )
                        }

                        // View-Only Health Card & Past Records Triggers
                        patient?.let { _ ->
                            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                OutlinedButton(
                                    onClick = { showHistoryDialog = true },
                                    shape = PillShape,
                                    border = BorderStroke(1.dp, GlumeBorder),
                                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                    modifier = Modifier.defaultMinSize(minHeight = 34.dp)
                                ) {
                                    Text(text = "📋 History & Rx", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                                }

                                OutlinedButton(
                                    onClick = { showHealthCardDialog = true },
                                    shape = PillShape,
                                    border = BorderStroke(1.dp, GlumeBorder),
                                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                    modifier = Modifier.defaultMinSize(minHeight = 34.dp)
                                ) {
                                    Text(text = "🪪 Health Card", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Mental Health Flag Banner
        if (isMentalHealthCase) {
            item {
                Surface(
                    color = GlumePrimaryPurpleContainer,
                    shape = CardShape,
                    border = BorderStroke(1.dp, GlumePrimaryPurple.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(Spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Text(text = "🧠", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "Mental Health Case Flag",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = GlumePrimaryPurpleLight
                            )
                            Text(
                                text = "Patient logged psychological stress/anxiety symptoms. Approached with empathy and holistic care.",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 4. Clinical Referral Badge & Specialist Loop Closure Findings
        val patientReferrals = referrals.filter { it.patientId == record.patientId }
        if (patientReferrals.isNotEmpty() || record.referredByDoctorId != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    if (record.referredByDoctorId != null) {
                        VitalSenseCard(
                            backgroundColor = GlumeSurfaceElevated,
                            border = BorderStroke(1.dp, GlumePrimaryPurple.copy(alpha = 0.4f))
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                                Text(
                                    text = "↗ Referred by Dr. ${record.referredByDoctorName ?: "Colleague"}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GlumePrimaryPurpleLight
                                )
                                if (!record.referralNotes.isNullOrBlank()) {
                                    Text(
                                        text = "Referral Notes: ${record.referralNotes}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GlumeTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    patientReferrals.forEach { ref ->
                        val isCompleted = ref.status == ReferralStatus.COMPLETED
                        VitalSenseCard(
                            backgroundColor = if (isCompleted) GlumeSuccessContainer.copy(alpha = 0.25f) else GlumeSurfaceElevated,
                            border = BorderStroke(1.dp, if (isCompleted) GlumeSuccessMint else GlumePrimaryPurple.copy(alpha = 0.5f))
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isCompleted) "✅ Specialist Consultation Completed" else "🔄 Referral: ${ref.targetSpecialty}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isCompleted) GlumeSuccessMint else GlumePrimaryPurpleLight
                                    )
                                    Surface(
                                        shape = PillShape,
                                        color = when (ref.urgency) {
                                            ReferralUrgency.EMERGENCY -> GlumeAlertCoral.copy(alpha = 0.2f)
                                            ReferralUrgency.URGENT -> GlumeWarningAmber.copy(alpha = 0.2f)
                                            ReferralUrgency.ROUTINE -> GlumeSuccessMint.copy(alpha = 0.2f)
                                        }
                                    ) {
                                        Text(
                                            text = ref.urgency.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when (ref.urgency) {
                                                    ReferralUrgency.EMERGENCY -> GlumeAlertCoral
                                                    ReferralUrgency.URGENT -> GlumeWarningAmber
                                                    ReferralUrgency.ROUTINE -> GlumeSuccessMint
                                                }
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Clinical Ask: \"${ref.clinicalQuestion}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = GlumeTextPrimary
                                )

                                if (isCompleted) {
                                    ref.specialistFindings?.let { f ->
                                        Text(
                                            text = "Specialist Diagnostic Findings: $f",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GlumeTextPrimary
                                        )
                                    }
                                    ref.specialistRecommendations?.let { r ->
                                        Text(
                                            text = "Specialist Recommendations: $r",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = GlumeSuccessMint
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Status: ${ref.status.displayName} · Target: ${ref.targetDoctorName ?: ref.targetSpecialty}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GlumeTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Section: Doctor Medical Response Composer
        item {
            Text(
                text = stringResource(R.string.doctorAdviceTitle),
                style = MaterialTheme.typography.headlineMedium,
                color = GlumeTextPrimary
            )
        }

        // 5.1 Quick Reply Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                Text(
                    text = stringResource(R.string.quickTemplates),
                    style = MaterialTheme.typography.labelSmall,
                    color = GlumeTextSecondary
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    quickReplies.forEach { reply ->
                        Surface(
                            onClick = {
                                responseText = if (responseText.isBlank()) reply else "$responseText\n$reply"
                            },
                            shape = PillShape,
                            color = GlumeSurfaceCard,
                            border = BorderStroke(1.dp, GlumeBorder)
                        ) {
                            Text(
                                text = reply,
                                style = MaterialTheme.typography.labelSmall,
                                color = GlumeTextPrimary,
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                            )
                        }
                    }
                }
            }
        }

        // 5.2 Response Text Input Box (Glume Dark Style with Purple Focus)
        item {
            VitalSenseCard {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    VitalSenseTextField(
                        value = responseText,
                        onValueChange = { responseText = it },
                        label = "Medical Advice & Treatment Plan",
                        placeholder = "Type clear, vernacular medical guidance for patient and ASHA worker...",
                        singleLine = false,
                        maxLines = 6
                    )

                    // Private Doctor Notes Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔒 Confidential Clinical Notes (Doctor-Only)",
                            style = MaterialTheme.typography.labelMedium,
                            color = GlumeTextSecondary
                        )
                        Switch(
                            checked = showPrivateNotes,
                            onCheckedChange = { showPrivateNotes = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GlumeTextPrimary,
                                checkedTrackColor = GlumePrimaryPurple,
                                uncheckedThumbColor = GlumeTextSecondary,
                                uncheckedTrackColor = GlumeSurfaceElevated
                            )
                        )
                    }

                    if (showPrivateNotes) {
                        VitalSenseTextField(
                            value = privateNotes,
                            onValueChange = { privateNotes = it },
                            label = "Private Internal Notes",
                            placeholder = "Diagnostic reasoning, differential diagnoses, internal follow-up observations...",
                            singleLine = false,
                            maxLines = 3
                        )
                    }

                    // Single Full-Width Purple Submit Button
                    VitalSenseButton(
                        text = if (record.doctorResponse != null) stringResource(R.string.updateAdvice) else stringResource(R.string.submitAdvice),
                        onClick = {
                            onSubmitResponse(
                                responseText.trim(),
                                if (showPrivateNotes && privateNotes.isNotBlank()) privateNotes.trim() else null
                            )
                        },
                        style = ButtonStyle.PRIMARY,
                        enabled = responseText.isNotBlank()
                    )
                }
            }
        }

        // 6. Clinical Tool Actions
        item {
            Text(
                text = "Clinical Actions",
                style = MaterialTheme.typography.headlineMedium,
                color = GlumeTextPrimary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                VitalSenseButton(
                    text = "📹 Start Tele-Consultation Call",
                    onClick = { showTeleConsultModal = true },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.PRIMARY
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseButton(
                        text = stringResource(R.string.issueRx),
                        onClick = { showPrescriptionDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.DARK
                    )
                    VitalSenseButton(
                        text = stringResource(R.string.proposeAppt),
                        onClick = { showAppointmentDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                    VitalSenseButton(
                        text = stringResource(R.string.refer),
                        onClick = { showReferDialog = true },
                        modifier = Modifier.weight(0.9f),
                        style = ButtonStyle.OUTLINED
                    )
                }

                // Clinical Hospital Actions (Lab Investigation & Medical Leave/Fitness Certificate)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseButton(
                        text = "🧪 Order Lab Test",
                        onClick = { showOrderLabDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                    VitalSenseButton(
                        text = "📜 Issue Certificate",
                        onClick = { showMedicalCertDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                }

                VitalSenseButton(
                    text = "📷 Scan External Rx (OCR)",
                    onClick = { showScanPrescriptionDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.OUTLINED
                )
            }
        }

        // 7. Prior Prescriptions on Record
        if (priorPrescriptions.isNotEmpty()) {
            item {
                Text(
                    text = "Patient's Active Prescriptions (${priorPrescriptions.size})",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GlumeTextPrimary
                )
            }

            items(priorPrescriptions) { rx ->
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Issued by ${rx.doctorName} (${rx.doctorSpecialty})",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GlumeTextPrimary
                                )
                                Text(
                                    text = "Date: ${rx.dateFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GlumeTextSecondary
                                )
                            }
                            if (rx.isOcrExtracted) {
                                Surface(shape = PillShape, color = GlumeSuccessContainer) {
                                    Text(
                                        text = "OCR Digitized",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = GlumeSuccessText
                                        ),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        rx.medicines.forEach { med ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• ${med.name} (${med.dosage})",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = GlumeTextPrimary
                                )
                                Text(
                                    text = "${med.frequency} · ${med.duration}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GlumeTextSecondary
                                )
                            }
                        }

                        if (rx.instructions.isNotBlank()) {
                            Text(
                                text = "Instructions: ${rx.instructions}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 8. Patient Medical History (Collapsible)
        item {
            VitalSenseCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .touchSpring()
                            .clickable { isMedicalHistoryExpanded = !isMedicalHistoryExpanded }
                            .padding(vertical = Spacing.xxs),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(R.string.medicalHistoryTitle)} (${medicalHistory.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GlumeTextPrimary
                        )
                        Text(
                            text = if (isMedicalHistoryExpanded) "▲" else "▼",
                            style = MaterialTheme.typography.titleMedium,
                            color = GlumeTextSecondary
                        )
                    }

                    if (isMedicalHistoryExpanded) {
                        if (medicalHistory.isEmpty()) {
                            Text(
                                text = stringResource(R.string.noMedicalHistory),
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary,
                                modifier = Modifier.padding(top = Spacing.xs)
                            )
                        } else {
                            medicalHistory.sortedByDescending { it.timestamp }.forEach { entry ->
                                HorizontalDivider(color = GlumeBorder, modifier = Modifier.padding(vertical = Spacing.xxs))
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = entry.title,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GlumeTextPrimary
                                        )
                                        Text(
                                            text = entry.dateFormatted,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GlumeTextSecondary
                                        )
                                    }
                                    Text(
                                        text = entry.details,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GlumeTextPrimary
                                    )
                                    Text(
                                        text = "By ${entry.doctorName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GlumeTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showHistoryDialog && patient != null) {
        PatientHistoryDialog(
            patient = patient,
            conditions = allConditions,
            prescriptions = priorPrescriptions,
            appointments = allAppointments,
            medicalHistory = medicalHistory,
            onDismiss = { showHistoryDialog = false }
        )
    }

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

    if (showReferDialog) {
        CreateReferralDialog(
            patient = patient,
            patientNameFallback = record.patientName,
            currentDoctor = currentDoctor,
            priorPrescriptions = priorPrescriptions,
            allConditions = allConditions,
            onDismiss = { showReferDialog = false },
            onSendReferral = { ref ->
                onSendStructuredReferral(ref)
                onReferCase(
                    DoctorSpecialty.values().find { it.displayName == ref.targetSpecialty } ?: DoctorSpecialty.GENERAL_PHYSICIAN,
                    "${ref.reason}\n\nClinical Ask: ${ref.clinicalQuestion}"
                )
            },
            onEmergencyCallTrigger = {
                showTeleConsultModal = true
            }
        )
    }

    if (showTeleConsultModal) {
        TeleConsultationModal(
            patientName = record.patientName,
            doctorName = currentDoctor.name,
            specialty = currentDoctor.specialty.displayName,
            villageName = record.villageName,
            patientAge = patient?.age ?: 32,
            onDismiss = { showTeleConsultModal = false },
            onEndCall = { notes ->
                showTeleConsultModal = false
                responseText = if (responseText.isNotBlank()) "$responseText\n$notes" else notes
            }
        )
    }

    if (showMedicalCertDialog) {
        MedicalCertificateDialog(
            condition = record,
            doctor = currentDoctor,
            onDismiss = { showMedicalCertDialog = false },
            onIssueCertificate = { cert ->
                onIssueMedicalCertificate(cert)
                showMedicalCertDialog = false
            }
        )
    }

    if (showOrderLabDialog) {
        com.vitalsense.app.feature.lab.OrderLabTestDialog(
            patient = patient ?: Patient(
                id = record.patientId,
                name = record.patientName,
                age = 35,
                gender = "Adult",
                phone = "9876543210",
                villageId = record.villageId,
                villageName = record.villageName,
                ashaWorkerId = "asha_1",
                ashaWorkerName = "Priya Devi",
                currentRiskLevel = SeverityLevel.LOW,
                lastCondition = record.notes,
                lastVisitDate = "Today",
                nextAppointmentDate = null,
                emergencyContact = "9876543210"
            ),
            onDismiss = { showOrderLabDialog = false },
            onConfirmOrder = { newReport ->
                onOrderLabTest(newReport)
                showOrderLabDialog = false
            }
        )
    }

    if (showScanPrescriptionDialog) {
        val fallbackPatient = patient ?: Patient(
            id = record.patientId,
            name = record.patientName,
            age = 35,
            gender = "Adult",
            phone = "9876543210",
            villageId = record.villageId,
            villageName = record.villageName,
            ashaWorkerId = "asha_1",
            ashaWorkerName = "Priya Devi",
            currentRiskLevel = SeverityLevel.LOW,
            lastCondition = record.notes,
            lastVisitDate = "Today",
            nextAppointmentDate = null,
            emergencyContact = "9876543210"
        )
        com.vitalsense.app.feature.prescriptions.PrescriptionUploadDialog(
            patient = fallbackPatient,
            isAshaProxy = false,
            onDismiss = { showScanPrescriptionDialog = false },
            onSavePrescription = { rx ->
                onIssuePrescription(rx.medicines, rx.instructions)
                showScanPrescriptionDialog = false
            }
        )
    }
}

