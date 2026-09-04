package com.vitalsense.app.feature.asha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.vitalsense.app.feature.asha.components.RegisterPatientDialog
import com.vitalsense.app.feature.asha.components.SendNoticeDialog
import com.vitalsense.app.core.util.DismissedNoticeHelper
import com.vitalsense.app.core.util.AudioGuidanceHelper
import kotlinx.coroutines.launch
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun AshaHomeScreen(
    asha: AshaWorker,
    patients: List<Patient>,
    notices: List<BroadcastNotice>,
    onSelectProxyPatient: (Patient) -> Unit,
    onRegisterPatientClick: () -> Unit = {},
    onSendNoticeClick: () -> Unit = {},
    onSavePatient: (Patient) -> Unit = {},
    onSendNotice: (BroadcastNotice) -> Unit = {},
    onSavePrescription: (Prescription) -> Unit = {},
    onTriggerSosForPatient: suspend (Patient) -> Boolean = { true },
    onImmunizationClick: () -> Unit = {},
    onDailyRoundsClick: () -> Unit = {},
    onMedicineRestockClick: () -> Unit = {},
    referrals: List<com.vitalsense.app.core.data.model.Referral> = emptyList(),
    onCompleteReferral: (com.vitalsense.app.core.data.model.Referral) -> Unit = {},
    scrollState: LazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() },
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var ocrTargetPatient by remember { mutableStateOf<Patient?>(null) }

    var showRegisterPatientDialog by remember { mutableStateOf(false) }
    var showSendNoticeDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Per-patient Emergency SOS state management
    var sosConfirmationPatient by remember { mutableStateOf<Patient?>(null) }
    var loadingSosPatientId by remember { mutableStateOf<String?>(null) }
    var sosFailedPatient by remember { mutableStateOf<Patient?>(null) }

    val totalPatients = patients.size
    val highRiskPatients = patients.count { it.currentRiskLevel == SeverityLevel.HIGH || it.currentRiskLevel == SeverityLevel.SEVERE }
    val visitedPatients = patients.count { it.lastVisitDate.isNotBlank() && it.lastVisitDate != "Never" }
    val followUpFraction = if (totalPatients > 0) visitedPatients.toFloat() / totalPatients else 1.0f

    val context = androidx.compose.ui.platform.LocalContext.current

    var clearedSosIds by remember { mutableStateOf(DismissedNoticeHelper.getClearedSosIds(context)) }
    var dismissedAdvisoryIds by remember(asha.id) { mutableStateOf(DismissedNoticeHelper.getDismissedAdvisoryIds(context, "asha")) }
    var sosToClear by remember { mutableStateOf<BroadcastNotice?>(null) }

    val emergencySosAlerts = remember(notices, clearedSosIds) {
        notices.filter { it.isUrgent && it.senderRole == UserRole.PATIENT && it.id !in clearedSosIds }
    }
    val adminAdvisories = remember(notices, dismissedAdvisoryIds) {
        notices.filter { it.senderRole == UserRole.ADMIN && it.id !in dismissedAdvisoryIds }
    }

    LazyColumn(
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.sm, bottom = Spacing.xxl)
    ) {
        // 1. Header with Glume Greeting
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                Text(
                    text = "Hi, ${asha.name}!",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = VS_OnBackground
                )
                Text(
                    text = "${stringResource(R.string.assignedVillages)} ${asha.assignedVillages.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VS_OnSurfaceVariant
                )
            }
        }

        // 2. Glume Hero Completion Ring Card: Community Care Progress
        item {
            VitalSenseCard(
                backgroundColor = VS_Surface,
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(
                            text = stringResource(R.string.villageCaseload).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = VS_OnSurfaceVariant
                        )
                        Text(
                            text = "$visitedPatients of $totalPatients Patients Monitored",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = if (highRiskPatients > 0) "$highRiskPatients patients need home checkup" else "All village caseloads stable",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (highRiskPatients > 0) VS_Error else VS_OnSuccessContainer
                        )
                    }

                    GlumeProgressRing(
                        progressFraction = followUpFraction,
                        size = 72.dp,
                        strokeWidth = 7.dp,
                        ringColor = VS_Primary,
                        trackColor = VS_SurfaceVariant
                    )
                }
            }
        }

        // 3. Glume Stat Display Pattern (3-Column Grid)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                GlumeStatCard(
                    label = stringResource(R.string.villageCaseload),
                    value = "$totalPatients",
                    icon = "👥",
                    modifier = Modifier.weight(1f),
                    badgeText = "Total",
                    badgeColor = VS_Primary
                )
                GlumeStatCard(
                    label = stringResource(R.string.criticalCases),
                    value = "$highRiskPatients",
                    icon = "⚠️",
                    modifier = Modifier.weight(1f),
                    badgeText = if (highRiskPatients > 0) "Alert" else "None",
                    badgeColor = if (highRiskPatients > 0) VS_Error else VS_Success
                )
                GlumeStatCard(
                    label = stringResource(R.string.assignedVillages),
                    value = "${asha.assignedVillages.size}",
                    icon = "🏡",
                    modifier = Modifier.weight(1f),
                    badgeText = "Active",
                    badgeColor = VS_Success
                )
            }
        }

        // 4. ASHA Unique ID Card (Glume Dark Elevated Style)
        item {
            VitalSenseCard(
                backgroundColor = VS_SurfaceVariant,
                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text(
                            text = stringResource(R.string.uniqueAshaCardTitle),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = VS_PrimaryContainer
                        )
                        Text(
                            text = asha.ashaUniqueId,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = stringResource(R.string.shareAshaIdDesc),
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(VS_PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🆔", fontSize = 22.sp)
                    }
                }
            }
        }

        // Success banner
        if (successMessage != null) {
            item {
                Surface(
                    shape = PillShape,
                    color = VS_SuccessContainer,
                    border = BorderStroke(1.dp, VS_Success),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = successMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_Success
                        )
                        IconButton(onClick = { successMessage = null }, modifier = Modifier.size(24.dp)) {
                            Text("✕", color = VS_Success, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // SOS Failure banner with Retry option
        if (sosFailedPatient != null) {
            item {
                val failedPatient = sosFailedPatient!!
                Surface(
                    shape = PillShape,
                    color = VS_ErrorContainer,
                    border = BorderStroke(1.dp, VS_Error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("⚠️", fontSize = 14.sp)
                            Text(
                                text = "${stringResource(R.string.sosFailedForPatient)} (${failedPatient.name})",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Error
                            )
                        }
                        val sosDispatchedText = stringResource(R.string.sosDispatchedForPatient)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TextButton(
                                onClick = {
                                    val pToRetry = failedPatient
                                    sosFailedPatient = null
                                    loadingSosPatientId = pToRetry.id
                                    coroutineScope.launch {
                                        try {
                                            val success = onTriggerSosForPatient(pToRetry)
                                            loadingSosPatientId = null
                                            if (success) {
                                                successMessage = "✓ $sosDispatchedText ${pToRetry.name}!"
                                            } else {
                                                sosFailedPatient = pToRetry
                                            }
                                        } catch (e: Exception) {
                                            loadingSosPatientId = null
                                            sosFailedPatient = pToRetry
                                        }
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.retry), color = VS_Error, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                            IconButton(onClick = { sosFailedPatient = null }, modifier = Modifier.size(24.dp)) {
                                Text("✕", color = VS_Error, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Action Buttons (Glume Primary & Secondary Pill Buttons)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseButton(
                        text = stringResource(R.string.newPatient),
                        onClick = { showRegisterPatientDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.PRIMARY
                    )
                    VitalSenseButton(
                        text = stringResource(R.string.sendNotice),
                        onClick = { showSendNoticeDialog = true },
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.DARK
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseButton(
                        text = "Immunization Tracker",
                        onClick = onImmunizationClick,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                    VitalSenseButton(
                        text = "Daily Rounds",
                        onClick = onDailyRoundsClick,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseButton(
                        text = "Medicine Restock",
                        onClick = onMedicineRestockClick,
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.SECONDARY
                    )
                }
            }
        }

        // 5.9 Today's Worklist (Follow-ups & Pending Referrals)
        val followUpReferrals = referrals.filter { ref -> patients.any { it.id == ref.patientId } && ref.status == com.vitalsense.app.core.data.model.ReferralStatus.FOLLOW_UP }
        val followUps = patients.filter { it.currentRiskLevel == SeverityLevel.MODERATE }.take(2)
        
        if (followUpReferrals.isNotEmpty() || followUps.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text(
                        text = "📅 Today's Worklist",
                        style = MaterialTheme.typography.headlineMedium,
                        color = VS_OnBackground
                    )

                    if (followUps.isNotEmpty()) {
                        Text(
                            text = "Routine Follow-ups Due (${followUps.size})",
                            style = MaterialTheme.typography.titleSmall,
                            color = VS_OnSurfaceVariant,
                            modifier = Modifier.padding(top = Spacing.xxs)
                        )
                        followUps.forEach { patient ->
                            VitalSenseCard(
                                backgroundColor = VS_Surface,
                                border = BorderStroke(1.dp, VS_Outline),
                                onClick = { onSelectProxyPatient(patient) }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = patient.name,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_OnBackground
                                        )
                                        Text(
                                            text = "Routine Follow-up",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }
                                    VitalSenseButton(
                                        text = "Visit",
                                        onClick = { onSelectProxyPatient(patient) },
                                        style = ButtonStyle.SECONDARY
                                    )
                                }
                            }
                        }
                    }

                    if (followUpReferrals.isNotEmpty()) {
                        Text(
                            text = "Referrals Need Follow-up (${followUpReferrals.size})",
                            style = MaterialTheme.typography.titleSmall,
                            color = VS_OnSurfaceVariant,
                            modifier = Modifier.padding(top = Spacing.xxs)
                        )
                        followUpReferrals.forEach { ref ->
                            VitalSenseCard(
                                backgroundColor = VS_Surface,
                                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f))
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${ref.patientName} ➔ ${ref.targetSpecialty}",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_OnBackground
                                        )
                                        Surface(
                                            shape = PillShape,
                                            color = when (ref.urgency) {
                                                com.vitalsense.app.core.data.model.ReferralUrgency.EMERGENCY -> VS_Error
                                                com.vitalsense.app.core.data.model.ReferralUrgency.URGENT -> VS_Warning
                                                com.vitalsense.app.core.data.model.ReferralUrgency.ROUTINE -> VS_Success
                                            }
                                        ) {
                                            Text(
                                                text = ref.urgency.displayName,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Referred by ${ref.referringUserName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                    
                                    VitalSenseButton(
                                        text = "Mark Follow-up Done",
                                        onClick = { onCompleteReferral(ref) },
                                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.xxs),
                                        style = ButtonStyle.PRIMARY
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5.9.5 High-Risk Registry
        val highRiskPatientList = patients.filter { it.currentRiskLevel == SeverityLevel.HIGH || it.currentRiskLevel == SeverityLevel.SEVERE }
        if (highRiskPatientList.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🚨 High-Risk Registry",
                            style = MaterialTheme.typography.headlineMedium,
                            color = VS_Error
                        )
                        Surface(shape = PillShape, color = VS_ErrorContainer) {
                            Text(
                                text = "${highRiskPatientList.size} Priority",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VS_Error),
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            items(highRiskPatientList) { patient ->
                val isAssignedToThisAsha = patient.ashaWorkerId == asha.id ||
                    patient.villageName in asha.assignedVillages ||
                    asha.assignedVillages.any { it.equals(patient.villageName, ignoreCase = true) } ||
                    patient.villageId in asha.assignedVillages
                val isSosInFlight = loadingSosPatientId == patient.id

                VitalSenseCard(
                    backgroundColor = VS_ErrorContainer,
                    border = BorderStroke(1.dp, VS_Error.copy(alpha = 0.4f))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = patient.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "${patient.age} yrs • ${patient.gender}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            SeverityBadge(severity = patient.currentRiskLevel)
                        }

                        HorizontalDivider(color = VS_Outline)

                        Text(
                            text = "Last Visit: ${patient.lastVisitDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            VitalSenseButton(
                                text = "View Profile",
                                onClick = { onSelectProxyPatient(patient) },
                                modifier = Modifier.weight(1f),
                                style = ButtonStyle.OUTLINED
                            )
                            VitalSenseButton(
                                text = "Log Vitals",
                                onClick = { onSelectProxyPatient(patient) },
                                modifier = Modifier.weight(1f),
                                style = ButtonStyle.PRIMARY
                            )
                        }
                    }
                }
            }
        }

        // 6. Caseload Summary Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.villageCaseload)} (${patients.size})",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VS_OnBackground
                )
                if (asha.alertCount > 0) {
                    Surface(shape = PillShape, color = VS_ErrorContainer) {
                        Text(
                            text = "${asha.alertCount} ${stringResource(R.string.highRisk)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = VS_Error
                            ),
                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // 7. Patient Caseload Cards (Glume Dark Slate Card Style)
        if (patients.isEmpty()) {
            item {
                VitalSenseCard {
                    Column(
                        modifier = Modifier.padding(Spacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.noPatientsYet),
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }
            }
        } else {
            val generalPatients = patients.filter { it.currentRiskLevel != SeverityLevel.HIGH && it.currentRiskLevel != SeverityLevel.SEVERE }
            
            if (generalPatients.isEmpty() && patients.isNotEmpty()) {
                item {
                    VitalSenseCard {
                        Column(
                            modifier = Modifier.padding(Spacing.sm),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "All patients are in the High-Risk Registry.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(generalPatients) { patient ->
                    val isHighRisk = false
                val isAssignedToThisAsha = patient.ashaWorkerId == asha.id ||
                    patient.villageName in asha.assignedVillages ||
                    asha.assignedVillages.any { it.equals(patient.villageName, ignoreCase = true) } ||
                    patient.villageId in asha.assignedVillages
                val isSosInFlight = loadingSosPatientId == patient.id

                VitalSenseCard(
                    backgroundColor = if (isHighRisk) VS_ErrorContainer else VS_Surface,
                    border = BorderStroke(1.dp, if (isHighRisk) VS_Error.copy(alpha = 0.4f) else VS_Outline)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        // Patient name & Risk badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = patient.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "Age: ${patient.age} (${patient.gender}) · ${patient.villageName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            SeverityBadge(severity = patient.currentRiskLevel)
                        }

                        // Last condition & Next visit
                        Text(
                            text = "Condition: ${patient.lastCondition}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = VS_OnBackground
                        )

                        Text(
                            text = "Last Visit: ${patient.lastVisitDate} · Next: ${patient.nextAppointmentDate ?: stringResource(R.string.noneScheduled)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )

                        HorizontalDivider(color = VS_Outline, thickness = 1.dp)

                        // Action Buttons: Per-Patient Emergency SOS, Scan Rx, & Proxy Mode
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Red Per-Patient Emergency SOS Action Button
                            Button(
                                onClick = { sosConfirmationPatient = patient },
                                enabled = isAssignedToThisAsha && !isSosInFlight,
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VS_Error,
                                    contentColor = VS_OnBackground,
                                    disabledContainerColor = VS_Error.copy(alpha = 0.4f),
                                    disabledContentColor = VS_OnBackground.copy(alpha = 0.6f)
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 40.dp, minWidth = 40.dp)
                            ) {
                                if (isSosInFlight) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = VS_OnBackground,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(text = "🚨", fontSize = 14.sp)
                                        Text(
                                            text = "SOS",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }

                            // 2. Scan Rx Button
                            OutlinedButton(
                                onClick = { ocrTargetPatient = patient },
                                modifier = Modifier.weight(1f).defaultMinSize(minHeight = 40.dp),
                                shape = PillShape,
                                border = BorderStroke(1.dp, VS_Outline)
                            ) {
                                Text(text = stringResource(R.string.scanRx), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                            }

                            // 3. Proxy Mode Button
                            Button(
                                onClick = { onSelectProxyPatient(patient) },
                                modifier = Modifier.weight(1.2f).defaultMinSize(minHeight = 40.dp),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VS_Primary,
                                    contentColor = VS_OnBackground
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.proxyMode),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
        }
        
        // 8. Emergency Patient SOS Alerts (With Subtle Coral Glow)
        if (emergencySosAlerts.isNotEmpty()) {
            item {
                Text(
                    text = "${stringResource(R.string.emergencyPatientAlerts)} (${emergencySosAlerts.size})",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VS_Error
                )
            }

            items(emergencySosAlerts) { sos ->
                VitalSenseCard(
                    backgroundColor = VS_ErrorContainer,
                    border = BorderStroke(1.dp, VS_Error.copy(alpha = 0.5f))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sos.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = VS_OnErrorContainer
                            )
                            Surface(shape = PillShape, color = VS_Error) {
                                Text(
                                    text = stringResource(R.string.highPriority),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VS_OnBackground
                                    ),
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = sos.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnBackground
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "From: ${sos.senderName} · Village: ${sos.targetVillage ?: "General"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = { sosToClear = sos },
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Mark Emergency Clear",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // 9. District Health Advisories
        if (adminAdvisories.isNotEmpty() || dismissedAdvisoryIds.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.districtAdvisories),
                        style = MaterialTheme.typography.headlineMedium,
                        color = VS_OnBackground
                    )
                    if (dismissedAdvisoryIds.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                DismissedNoticeHelper.clearDismissedAdvisories(context)
                                dismissedAdvisoryIds = emptySet()
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🔄 Restore (${dismissedAdvisoryIds.size})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Primary
                            )
                        }
                    }
                }
            }

            items(adminAdvisories) { notice ->
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text(
                            text = notice.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (notice.isUrgent) VS_OnErrorContainer else VS_OnBackground
                        )
                        Text(
                            text = notice.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnBackground
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${stringResource(R.string.issuedBy)} ${notice.senderName} (${notice.senderRole.name})",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    DismissedNoticeHelper.dismissAdvisory(context, "asha", notice.id)
                                    dismissedAdvisoryIds = dismissedAdvisoryIds + notice.id
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = PillShape
                            ) {
                                Text(
                                    text = "✕ Dismiss",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_Primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Per-patient Emergency SOS Confirmation Dialog
    sosConfirmationPatient?.let { targetPatient ->
        VitalSenseDialog(
            onDismissRequest = { sosConfirmationPatient = null },
            title = stringResource(R.string.sosAlertForPatient),
            icon = { Text("🚨", fontSize = 22.sp) },
            confirmButton = {
                val sosDispatchedText = stringResource(R.string.sosDispatchedForPatient)
                Button(
                    onClick = {
                        val pToTrigger = targetPatient
                        sosConfirmationPatient = null
                        loadingSosPatientId = pToTrigger.id
                        coroutineScope.launch {
                            try {
                                val success = onTriggerSosForPatient(pToTrigger)
                                loadingSosPatientId = null
                                if (success) {
                                    successMessage = "✓ $sosDispatchedText ${pToTrigger.name}!"
                                } else {
                                    sosFailedPatient = pToTrigger
                                }
                            } catch (e: Exception) {
                                loadingSosPatientId = null
                                sosFailedPatient = pToTrigger
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                    shape = PillShape,
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(stringResource(R.string.yesSendAlert), color = VS_OnBackground, style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { sosConfirmationPatient = null },
                    shape = PillShape,
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant, style = MaterialTheme.typography.labelLarge)
                }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    text = "${stringResource(R.string.confirmSosPatientMsg)} ${targetPatient.name}?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "• ${stringResource(R.string.village)}: ${targetPatient.villageName}\n• Age: ${targetPatient.age} (${targetPatient.gender})\n• Emergency Contact: ${targetPatient.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
                Text(
                    text = "This will immediately dispatch a high-priority SOS alert to doctors and emergency response.",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_Error
                )
            }
        }
    }

    ocrTargetPatient?.let { targetPatient ->
        com.vitalsense.app.feature.prescriptions.PrescriptionUploadDialog(
            patient = targetPatient,
            isAshaProxy = true,
            onDismiss = { ocrTargetPatient = null },
            onSavePrescription = onSavePrescription
        )
    }

    if (showRegisterPatientDialog) {
        RegisterPatientDialog(
            asha = asha,
            onDismiss = { showRegisterPatientDialog = false },
            onRegister = { newPatient ->
                onSavePatient(newPatient)
                showRegisterPatientDialog = false
                successMessage = "✓ Registered ${newPatient.name} into your caseload!"
            }
        )
    }

    if (showSendNoticeDialog) {
        SendNoticeDialog(
            asha = asha,
            onDismiss = { showSendNoticeDialog = false },
            onSend = { notice ->
                onSendNotice(notice)
                showSendNoticeDialog = false
                successMessage = "✓ Broadcast advisory sent to village!"
            }
        )
    }

    if (sosToClear != null) {
        AlertDialog(
            onDismissRequest = { sosToClear = null },
            title = {
                Text(
                    text = "Confirm Emergency Resolved",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Text(
                    text = "Are you sure this emergency alert for ${sosToClear?.senderName} has been addressed and the patient is safe?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VS_OnSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = sosToClear!!.id
                        DismissedNoticeHelper.clearSos(context, id)
                        clearedSosIds = clearedSosIds + id
                        sosToClear = null
                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                    shape = PillShape
                ) {
                    Text(
                        text = "Yes, Mark Clear & Dismiss",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { sosToClear = null },
                    shape = PillShape
                ) {
                    Text("Cancel", color = VS_OnSurfaceVariant)
                }
            }
        )
    }
}
