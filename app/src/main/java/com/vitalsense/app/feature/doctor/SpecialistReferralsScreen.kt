package com.vitalsense.app.feature.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.doctor.components.SpecialistFindingsDialog
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialistReferralsScreen(
    doctor: Doctor,
    referrals: List<Referral>,
    onBack: () -> Unit,
    onAcceptReferral: (referralId: String) -> Unit,
    onDeclineReferral: (referralId: String, reason: String, suggestion: String?) -> Unit,
    onRequestMoreInfo: (referralId: String, note: String) -> Unit,
    onSubmitFindings: (referralId: String, findings: String, recommendations: String, followUpNeeded: Boolean) -> Unit,
    onStartConsultCall: (referral: Referral) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "PENDING", "ACTIVE", "COMPLETED"
    var activeDeclineReferral by remember { mutableStateOf<Referral?>(null) }
    var activeInfoRequestReferral by remember { mutableStateOf<Referral?>(null) }
    var activeFindingsReferral by remember { mutableStateOf<Referral?>(null) }

    // Filter referrals
    val filteredReferrals = remember(referrals, selectedFilter) {
        val list = when (selectedFilter) {
            "PENDING" -> referrals.filter { it.status == ReferralStatus.CREATED || it.status == ReferralStatus.SENT }
            "ACTIVE" -> referrals.filter { it.status in listOf(ReferralStatus.ACCEPTED, ReferralStatus.APPOINTMENT_SCHEDULED, ReferralStatus.PATIENT_REACHED, ReferralStatus.IN_PROGRESS, ReferralStatus.INFO_REQUESTED) }
            "COMPLETED" -> referrals.filter { it.status in listOf(ReferralStatus.CONSULTATION_COMPLETED, ReferralStatus.FOLLOW_UP, ReferralStatus.COMPLETED, ReferralStatus.DECLINED) }
            else -> referrals
        }
        // Sort: EMERGENCY top, then URGENT, then ROUTINE; within each tier, newest first
        list.sortedWith(
            compareBy<Referral> {
                when (it.urgency) {
                    ReferralUrgency.EMERGENCY -> 1
                    ReferralUrgency.URGENT -> 2
                    ReferralUrgency.ROUTINE -> 3
                }
            }.thenByDescending { it.createdAt }
        )
    }

    val pendingCount = referrals.count { it.status == ReferralStatus.CREATED || it.status == ReferralStatus.SENT }
    val activeCount = referrals.count { it.status in listOf(ReferralStatus.ACCEPTED, ReferralStatus.APPOINTMENT_SCHEDULED, ReferralStatus.PATIENT_REACHED, ReferralStatus.IN_PROGRESS, ReferralStatus.INFO_REQUESTED) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md)
    ) {
        Spacer(modifier = Modifier.height(Spacing.sm))

        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                IconButton(onClick = onBack) {
                    Text("←", fontSize = 24.sp, color = VS_OnBackground, fontWeight = FontWeight.Bold)
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.doctorReferralsTitle),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        if (pendingCount > 0) {
                            Surface(shape = PillShape, color = VS_Error) {
                                Text(
                                    text = "$pendingCount NEW",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Triage pool & direct referrals for Dr. ${doctor.name} (${doctor.specialty.displayName})",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xs))

        // Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            listOf(
                "ALL" to "All (${referrals.size})",
                "PENDING" to "Pending ($pendingCount)",
                "ACTIVE" to "Active ($activeCount)",
                "COMPLETED" to "Archived"
            ).forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Surface(
                    modifier = Modifier
                        .clickable { selectedFilter = key },
                    shape = PillShape,
                    color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) VS_PrimaryContainer else VS_OnSurfaceVariant,
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xs))

        // List of Referrals
        if (filteredReferrals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("📥", fontSize = 48.sp)
                    Text(
                        text = "No referrals in this queue view.",
                        style = MaterialTheme.typography.titleMedium,
                        color = VS_OnSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
                contentPadding = PaddingValues(vertical = Spacing.sm)
            ) {
                items(filteredReferrals, key = { it.id }) { ref ->
                    val (urgencyBg, urgencyBorder, urgencyText) = when (ref.urgency) {
                        ReferralUrgency.ROUTINE -> Triple(VS_SuccessContainer, VS_Success, VS_Success)
                        ReferralUrgency.URGENT -> Triple(VS_WarningContainer, VS_Warning, VS_Warning)
                        ReferralUrgency.EMERGENCY -> Triple(VS_ErrorContainer, VS_Error, VS_Error)
                    }

                    VitalSenseCard(
                        backgroundColor = VS_Surface,
                        border = BorderStroke(
                            if (ref.urgency == ReferralUrgency.EMERGENCY) 2.dp else 1.dp,
                            if (ref.urgency == ReferralUrgency.EMERGENCY) VS_Error else VS_Outline
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            // Top row: Patient Name + Urgency Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ref.patientName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "Referred by ${ref.referringUserName} (${ref.referringUserSpecialty})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Surface(
                                        shape = PillShape,
                                        color = urgencyBg,
                                        border = BorderStroke(1.dp, urgencyBorder)
                                    ) {
                                        Text(
                                            text = ref.urgency.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = urgencyText
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        shape = PillShape,
                                        color = when (ref.status) {
                                            ReferralStatus.COMPLETED -> VS_SuccessContainer
                                            ReferralStatus.DECLINED -> VS_ErrorContainer
                                            ReferralStatus.INFO_REQUESTED -> VS_WarningContainer
                                            ReferralStatus.ACCEPTED, ReferralStatus.IN_PROGRESS -> VS_PrimaryContainer
                                            else -> VS_SurfaceVariant
                                        }
                                    ) {
                                        Text(
                                            text = ref.status.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when (ref.status) {
                                                    ReferralStatus.COMPLETED -> VS_Success
                                                    ReferralStatus.DECLINED -> VS_Error
                                                    ReferralStatus.INFO_REQUESTED -> VS_Warning
                                                    ReferralStatus.ACCEPTED, ReferralStatus.IN_PROGRESS -> VS_PrimaryContainer
                                                    else -> VS_OnSurfaceVariant
                                                }
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = 2.dp))

                            // Clinical Reason
                            Text(
                                text = "Reason: ${ref.reason}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnBackground
                            )

                            // Clinical Question Front-and-Center
                            Surface(
                                shape = CardShape,
                                color = VS_PrimaryContainer.copy(alpha = 0.35f),
                                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(Spacing.xs)) {
                                    Text(
                                        text = "🎯 SPECIFIC CLINICAL QUESTION / ASK:",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = VS_PrimaryContainer
                                        )
                                    )
                                    Text(
                                        text = ref.clinicalQuestion,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = VS_OnBackground
                                        )
                                    )
                                }
                            }

                            // Attached Records preview
                            if (ref.attachedRecordIds.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState())
                                ) {
                                    Text("📎 Attached Records:", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                                    ref.attachedRecordIds.forEach { recId ->
                                        Surface(shape = PillShape, color = VS_SurfaceVariant) {
                                            Text(
                                                text = "📄 ${recId.take(6)}...",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = VS_OnSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            // State: COMPLETED - Closed Loop Summary
                            if (ref.status == ReferralStatus.COMPLETED) {
                                Surface(
                                    shape = CardShape,
                                    color = VS_SuccessContainer.copy(alpha = 0.3f),
                                    border = BorderStroke(1.dp, VS_Success.copy(alpha = 0.6f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(Spacing.xs), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text("✅", fontSize = 14.sp)
                                            Text(
                                                text = "CLOSED LOOP: SPECIALIST FINDINGS RECORDED",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = VS_Success
                                                )
                                            )
                                        }
                                        ref.specialistFindings?.let { f ->
                                            Text(text = "Findings: $f", style = MaterialTheme.typography.bodySmall, color = VS_OnBackground)
                                        }
                                        ref.specialistRecommendations?.let { r ->
                                            Text(text = "Recommendations: $r", style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                                        }
                                    }
                                }
                            }

                            // State: DECLINED
                            if (ref.status == ReferralStatus.DECLINED) {
                                Text(
                                    text = "Declined reason: ${ref.declineReason ?: "Specialist unavailable"}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = VS_Error)
                                )
                            }

                            // State: INFO_REQUESTED
                            if (ref.status == ReferralStatus.INFO_REQUESTED) {
                                Text(
                                    text = "Inquiry sent to referring doctor: ${ref.infoRequestNote ?: "Additional diagnostic clarity requested"}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = VS_Warning)
                                )
                            }

                            // Interactive Buttons based on State
                            when (ref.status) {
                                ReferralStatus.CREATED, ReferralStatus.SENT -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = { onAcceptReferral(ref.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                                            shape = PillShape,
                                            modifier = Modifier.weight(1.2f).height(38.dp)
                                        ) {
                                            Text("✓ Accept Referral", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = VS_Background)
                                        }

                                        OutlinedButton(
                                            onClick = { activeInfoRequestReferral = ref },
                                            shape = PillShape,
                                            border = BorderStroke(1.dp, VS_Warning),
                                            modifier = Modifier.weight(1f).height(38.dp)
                                        ) {
                                            Text("❓ Ask Info", style = MaterialTheme.typography.labelSmall, color = VS_Warning)
                                        }

                                        OutlinedButton(
                                            onClick = { activeDeclineReferral = ref },
                                            shape = PillShape,
                                            border = BorderStroke(1.dp, VS_Error),
                                            modifier = Modifier.weight(0.9f).height(38.dp)
                                        ) {
                                            Text("✕ Decline", style = MaterialTheme.typography.labelSmall, color = VS_Error)
                                        }
                                    }
                                }

                                ReferralStatus.ACCEPTED, ReferralStatus.APPOINTMENT_SCHEDULED, ReferralStatus.PATIENT_REACHED, ReferralStatus.IN_PROGRESS -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = { onStartConsultCall(ref) },
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                            shape = PillShape,
                                            modifier = Modifier.weight(1.2f).height(38.dp)
                                        ) {
                                            Text("📹 Call Patient (Consult)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                        }

                                        Button(
                                            onClick = { activeFindingsReferral = ref },
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                                            shape = PillShape,
                                            modifier = Modifier.weight(1.2f).height(38.dp)
                                        ) {
                                            Text("📝 Send Findings Back", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = VS_Background)
                                        }
                                    }
                                }

                                else -> { /* No active actions needed for completed/declined */ }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal 1: Decline Referral Dialog
    activeDeclineReferral?.let { refToDecline ->
        var declineReason by remember { mutableStateOf("") }
        var suggestedReroute by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { activeDeclineReferral = null },
            title = { Text("Decline Referral", fontWeight = FontWeight.Bold, color = VS_OnBackground) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text("Please provide the clinical rationale for declining this referral handoff:", style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                    OutlinedTextField(
                        value = declineReason,
                        onValueChange = { declineReason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Beyond department scope, bed capacity reached, refer to Oncology instead...", fontSize = 12.sp) },
                        minLines = 2
                    )
                    Text("Suggested Specialist / Department (Optional):", style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                    OutlinedTextField(
                        value = suggestedReroute,
                        onValueChange = { suggestedReroute = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Dr. Meera Nambiar / Psychiatry", fontSize = 12.sp) },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (declineReason.isNotBlank()) {
                            onDeclineReferral(refToDecline.id, declineReason.trim(), suggestedReroute.takeIf { it.isNotBlank() }?.trim())
                            activeDeclineReferral = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                    shape = PillShape
                ) {
                    Text("Decline & Notify", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { activeDeclineReferral = null }) {
                    Text("Cancel", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                }
            }
        )
    }

    // Modal 2: Request More Information Dialog
    activeInfoRequestReferral?.let { refToAsk ->
        var infoQuestion by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { activeInfoRequestReferral = null },
            title = { Text("Request More Information", fontWeight = FontWeight.Bold, color = VS_OnBackground) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text("Specify the clinical details or diagnostic tests you need before accepting:", style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                    OutlinedTextField(
                        value = infoQuestion,
                        onValueChange = { infoQuestion = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Please provide recent serum creatinine and 12-lead ECG strip...", fontSize = 12.sp) },
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (infoQuestion.isNotBlank()) {
                            onRequestMoreInfo(refToAsk.id, infoQuestion.trim())
                            activeInfoRequestReferral = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Warning),
                    shape = PillShape
                ) {
                    Text("Send Request", style = MaterialTheme.typography.labelSmall, color = VS_Background)
                }
            },
            dismissButton = {
                TextButton(onClick = { activeInfoRequestReferral = null }) {
                    Text("Cancel", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                }
            }
        )
    }

    // Modal 3: Specialist Findings Dialog (Loop Closure)
    activeFindingsReferral?.let { refToClose ->
        SpecialistFindingsDialog(
            referral = refToClose,
            onDismiss = { activeFindingsReferral = null },
            onSubmitFindings = { findings, recommendations, followUp ->
                onSubmitFindings(refToClose.id, findings, recommendations, followUp)
                activeFindingsReferral = null
            }
        )
    }
}
