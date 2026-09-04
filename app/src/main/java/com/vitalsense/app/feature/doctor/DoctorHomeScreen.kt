package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.ui.util.touchSpring
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.call.*
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.doctor.components.CreateReferralDialog
import com.vitalsense.app.feature.doctor.components.DashboardAccordionItem
import com.vitalsense.app.feature.doctor.components.PatientHistoryDialog
import com.vitalsense.app.feature.doctor.components.ScheduleAppointmentDialog
import com.vitalsense.app.feature.doctor.components.TeleConsultationModal
import com.vitalsense.app.feature.doctor.components.CaseAnalyticsCard
import com.vitalsense.app.core.util.DismissedNoticeHelper
import com.vitalsense.app.core.util.AudioGuidanceHelper
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun DoctorHomeScreen(
    doctor: Doctor,
    cases: List<ConditionRecord>,
    caseAnalytics: DoctorCaseAnalytics,
    appointments: List<Appointment>,
    dispensaryStock: List<DispensaryItem>,
    patients: List<Patient> = emptyList(),
    notices: List<BroadcastNotice> = emptyList(),
    allConditions: List<ConditionRecord> = emptyList(),
    allPrescriptions: List<Prescription> = emptyList(),
    onSelectCase: (ConditionRecord) -> Unit,
    onAcceptAppointment: (String) -> Unit = {},
    onDeclineAppointment: (String) -> Unit = {},
    onProposeAppointment: (patientId: String, patientName: String, date: String, timeSlot: String) -> Unit = { _, _, _, _ -> },
    onNavigateToOtScheduler: () -> Unit = {},
    onNavigateToIpdBeds: () -> Unit = {},
    onNavigateToExternalReferrals: () -> Unit = {},
    onNavigateToLiveQueue: () -> Unit = {},
    onNavigateToSpecialistReferrals: () -> Unit = {},
    referrals: List<Referral> = emptyList(),
    onRemindAdminRestock: (DispensaryItem) -> Unit = {},
    todaysQueue: List<QueueEntry> = emptyList(),
    onSendReferral: (Referral) -> Unit = {},
    scrollState: LazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() },
    modifier: Modifier = Modifier
) {
    var showScheduleDialog by remember { mutableStateOf(false) }
    var selectedPatientForHistory by remember { mutableStateOf<Patient?>(null) }
    var selectedPatientForReferral by remember { mutableStateOf<Patient?>(null) }
    var activeTeleConsultationPatient by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var restockToastMessage by remember { mutableStateOf<String?>(null) }
    var onCallStatus by remember { mutableStateOf(doctor.onCallStatus) }

    // Live calling session
    val activeCallSession by TeleCallingManager.currentSession.collectAsStateWithLifecycle()

    // Accordion expansion states
    var expandedSos by remember { mutableStateOf(false) }
    var expandedTriage by remember { mutableStateOf(true) }
    var expandedAppointments by remember { mutableStateOf(false) }
    var expandedDispensary by remember { mutableStateOf(false) }
    var expandedPatientRecords by remember { mutableStateOf(false) }

    val pendingCases = cases.filter { it.status == CaseStatus.PENDING_REVIEW || it.status == CaseStatus.IN_PROGRESS }
    val respondedCases = cases.count { it.status == CaseStatus.RESPONDED || it.status == CaseStatus.CLOSED }
    val totalCases = cases.size
    val completionFraction = if (totalCases > 0) respondedCases.toFloat() / totalCases else 1.0f

    val severeCount = cases.count { it.severity == SeverityLevel.SEVERE || it.severity == SeverityLevel.HIGH }
    val lowStockCount = dispensaryStock.count { it.isLowStock }

    val context = androidx.compose.ui.platform.LocalContext.current
    var clearedSosIds by remember { mutableStateOf(DismissedNoticeHelper.getClearedSosIds(context)) }
    var remindedMedicineIds by remember { mutableStateOf(DismissedNoticeHelper.getRemindedMedicineIds(context)) }
    var dismissedAdvisoryIds by remember(doctor.id) { mutableStateOf(DismissedNoticeHelper.getDismissedAdvisoryIds(context, "doctor")) }
    var sosToClear by remember { mutableStateOf<BroadcastNotice?>(null) }

    val emergencySosAlerts = remember(notices, clearedSosIds) {
        notices.filter { it.isUrgent && it.senderRole == UserRole.PATIENT && it.id !in clearedSosIds }
    }
    val adminDirectives = remember(notices, dismissedAdvisoryIds) {
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
        // 1. Glume Header Greeting: "Hi, Dr. Rajesh!" & On-Call Status Toggle
        item {
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
                        text = "Hi, ${doctor.name}!",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = VS_OnBackground
                    )
                    Text(
                        text = "${doctor.specialty.displayName} · ${doctor.hospitalName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VS_OnSurfaceVariant
                    )
                }

                // On-Call Status Toggle Button (Available / Busy / Offline)
                Surface(
                    shape = PillShape,
                    color = when (onCallStatus) {
                        DoctorAvailabilityStatus.AVAILABLE -> VS_SuccessContainer
                        DoctorAvailabilityStatus.BUSY -> VS_WarningContainer
                        DoctorAvailabilityStatus.OFFLINE -> VS_SurfaceVariant
                    },
                    border = BorderStroke(
                        1.dp,
                        when (onCallStatus) {
                            DoctorAvailabilityStatus.AVAILABLE -> VS_Success
                            DoctorAvailabilityStatus.BUSY -> VS_Warning
                            DoctorAvailabilityStatus.OFFLINE -> VS_Outline
                        }
                    ),
                    modifier = Modifier.clickable {
                        onCallStatus = when (onCallStatus) {
                            DoctorAvailabilityStatus.AVAILABLE -> DoctorAvailabilityStatus.BUSY
                            DoctorAvailabilityStatus.BUSY -> DoctorAvailabilityStatus.OFFLINE
                            DoctorAvailabilityStatus.OFFLINE -> DoctorAvailabilityStatus.AVAILABLE
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when (onCallStatus) {
                                        DoctorAvailabilityStatus.AVAILABLE -> VS_Success
                                        DoctorAvailabilityStatus.BUSY -> VS_Warning
                                        DoctorAvailabilityStatus.OFFLINE -> VS_OnSurfaceVariant
                                    }
                                )
                        )
                        Text(
                            text = when (onCallStatus) {
                                DoctorAvailabilityStatus.AVAILABLE -> "🟢 On-Call"
                                DoctorAvailabilityStatus.BUSY -> "🟡 Busy"
                                DoctorAvailabilityStatus.OFFLINE -> "🔴 Offline"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (onCallStatus) {
                                    DoctorAvailabilityStatus.AVAILABLE -> VS_Success
                                    DoctorAvailabilityStatus.BUSY -> VS_Warning
                                    DoctorAvailabilityStatus.OFFLINE -> VS_OnSurfaceVariant
                                }
                            )
                        )
                    }
                }
            }
        }

        // Success banner for restock reminder
        if (restockToastMessage != null) {
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
                            text = restockToastMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_Success
                        )
                        IconButton(onClick = { restockToastMessage = null }, modifier = Modifier.size(24.dp)) {
                            Text("✕", color = VS_Success, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 2. Glume Hero Completion Ring Card
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
                            text = stringResource(R.string.clinicalTriageToday),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = VS_OnSurfaceVariant
                        )
                        Text(
                            text = "$respondedCases of $totalCases Cases Responded",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = if (pendingCases.isEmpty()) "All caught up! Excellent work." else "${pendingCases.size} cases awaiting review",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (pendingCases.isEmpty()) VS_OnSuccessContainer else VS_PrimaryContainer
                        )
                    }

                    GlumeProgressRing(
                        progressFraction = completionFraction,
                        size = 72.dp,
                        strokeWidth = 7.dp,
                        ringColor = VS_Success,
                        trackColor = VS_SurfaceVariant
                    )
                }
            }
        }

        // 2.5 Case Analytics Card
        item {
            CaseAnalyticsCard(
                analytics = caseAnalytics,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Glume Stat Display Pattern (3-Column Compact Grid)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                // Pending Cases Stat Card
                GlumeStatCard(
                    label = stringResource(R.string.pendingCases),
                    value = "${pendingCases.size}",
                    icon = "⏳",
                    modifier = Modifier.weight(1f),
                    badgeText = if (pendingCases.isNotEmpty()) "Queue" else null,
                    badgeColor = VS_Primary
                )

                // Critical Cases Stat Card
                GlumeStatCard(
                    label = stringResource(R.string.criticalCases),
                    value = "$severeCount",
                    icon = "🚨",
                    modifier = Modifier.weight(1f),
                    badgeText = if (severeCount > 0) "Urgent" else null,
                    badgeColor = if (severeCount > 0) VS_Error else VS_Success
                )

                // Scheduled Appointments Stat Card
                GlumeStatCard(
                    label = stringResource(R.string.scheduledAppts),
                    value = "${appointments.size}",
                    icon = "📅",
                    modifier = Modifier.weight(1f),
                    badgeText = "Today",
                    badgeColor = VS_Success
                )
            }
        }

        // 3.1 Specialist Referrals Queue Card
        item {
            val pendingReferrals = referrals.filter { it.status == ReferralStatus.CREATED || it.status == ReferralStatus.SENT }
            val hasEmergency = pendingReferrals.any { it.urgency == ReferralUrgency.EMERGENCY }
            val hasUrgent = pendingReferrals.any { it.urgency == ReferralUrgency.URGENT }

            VitalSenseCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (hasEmergency) VS_ErrorContainer.copy(alpha = 0.35f) else VS_Surface,
                border = BorderStroke(
                    1.5.dp,
                    when {
                        hasEmergency -> VS_Error
                        hasUrgent -> VS_Warning
                        pendingReferrals.isNotEmpty() -> VS_PrimaryContainer
                        else -> VS_Outline
                    }
                ),
                onClick = onNavigateToSpecialistReferrals
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (hasEmergency) VS_Error.copy(alpha = 0.2f) else VS_PrimaryContainer,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (hasEmergency) "🚨" else "📥", fontSize = 20.sp)
                            }
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.specialistReferralsQueue),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                if (pendingReferrals.isNotEmpty()) {
                                    Surface(
                                        shape = PillShape,
                                        color = if (hasEmergency) VS_Error else VS_Primary
                                    ) {
                                        Text(
                                            text = "${pendingReferrals.size} PENDING",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = stringResource(R.string.triageIncomingConsults),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                    Text("➔", fontSize = 20.sp, color = VS_OnSurfaceVariant)
                }
            }
        }

        // 3.2 Doctor Clinical Quick Hub
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    onClick = onNavigateToOtScheduler
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text("🔪", fontSize = 20.sp)
                        Text(stringResource(R.string.otDeskTab), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Text(stringResource(R.string.surgeriesAndPac), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                    }
                }

                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    onClick = onNavigateToIpdBeds
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text("🛏️", fontSize = 20.sp)
                        Text(stringResource(R.string.ipdBedsTab), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Text(stringResource(R.string.wardOccupancy), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                    }
                }

                VitalSenseCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    onClick = onNavigateToExternalReferrals
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text("🏛️", fontSize = 20.sp)
                        Text(stringResource(R.string.referralsTab), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Text(stringResource(R.string.aiimsTieUp), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                    }
                }
            }
        }

        // 3.1 Live Clinic Queue Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .touchSpring(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VS_Surface),
                border = BorderStroke(1.5.dp, VS_Primary.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_Primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🩺", fontSize = 22.sp)
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.liveQueueTitle),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                val waitingCount = todaysQueue.count { it.status == QueueEntryStatus.WAITING }
                                if (waitingCount > 0) {
                                    Surface(
                                        shape = PillShape,
                                        color = NagarSevaStatusProgressBg,
                                        border = BorderStroke(1.dp, VS_Warning)
                                    ) {
                                        Text(
                                            text = "$waitingCount waiting",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_Warning,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = stringResource(R.string.liveQueueDesc),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToLiveQueue,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(stringResource(R.string.openHud), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // ━━━ ACCORDION DROPDOWN MENU ━━━
        // 4. Emergency Patients SOS Alerts Accordion
        item {
            DashboardAccordionItem(
                icon = "🚨",
                iconBackgroundColor = VS_Error,
                title = "Emergency Patients SOS Alerts",
                subtitle = "Instant SOS alerts for critical cases to notify ASHA workers, doctors, and nearby facilities.",
                expanded = expandedSos,
                onToggle = { expandedSos = !expandedSos }
            ) {
                if (emergencySosAlerts.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noActiveSosAlerts),
                        style = MaterialTheme.typography.bodyMedium,
                        color = VS_OnSurfaceVariant
                    )
                } else {
                    emergencySosAlerts.forEach { sos ->
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
                                    Surface(
                                        shape = PillShape,
                                        color = VS_Error
                                    ) {
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
                                        text = "${stringResource(R.string.village)}: ${sos.targetVillage ?: "General"}",
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
                                            text = stringResource(R.string.markEmergencyClear),
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
            }
        }

        // 5. Specialist Triage Queue Accordion
        item {
            DashboardAccordionItem(
                icon = "👤",
                iconBackgroundColor = VS_Primary,
                title = "Specialist Triage Queue",
                subtitle = "Prioritized queue for specialist review and triage based on severity and urgency.",
                expanded = expandedTriage,
                onToggle = { expandedTriage = !expandedTriage }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${cases.size} cases · ${doctor.specialty.displayName} Stream",
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_OnSurfaceVariant
                    )
                }

                if (cases.isEmpty()) {
                    VitalSenseCard {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(Spacing.md),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Text(text = "🎉", fontSize = 32.sp)
                            Text(
                                text = stringResource(R.string.noPendingCases),
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                } else {
                    cases.forEach { record ->
                        val isSevere = record.severity == SeverityLevel.SEVERE || record.severity == SeverityLevel.HIGH
                        val isMentalHealth = record.category == ConditionCategory.MENTAL_HEALTH || record.requestedDoctorType == DoctorSpecialty.PSYCHOLOGIST

                        VitalSenseCard(
                            backgroundColor = if (isSevere) VS_ErrorContainer else VS_Surface,
                            border = BorderStroke(1.dp, if (isSevere) VS_Error.copy(alpha = 0.4f) else VS_Outline)
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
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = VS_OnBackground
                                        )
                                        Text(
                                            text = "${stringResource(R.string.village)}: ${record.villageName} · ${record.category.displayName}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }
                                    SeverityBadge(severity = record.severity)
                                }

                                if (isMentalHealth) {
                                    Surface(shape = PillShape, color = VS_PrimaryContainer) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                                        ) {
                                            Text(text = "🧠", style = MaterialTheme.typography.labelSmall)
                                            Text(
                                                text = stringResource(R.string.mentalHealthReferral),
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = VS_PrimaryContainer
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "${stringResource(R.string.symptoms)} ${record.notes}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = VS_OnBackground
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = PillShape,
                                        color = VS_SurfaceVariant
                                    ) {
                                        Text(
                                            text = record.status.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 4.dp),
                                            color = VS_OnSurfaceVariant
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                        OutlinedButton(
                                            onClick = {
                                                selectedPatientForHistory = patients.find { it.id == record.patientId }
                                                    ?: Patient(
                                                        id = record.patientId,
                                                        name = record.patientName,
                                                        age = 30,
                                                        gender = "Not specified",
                                                        phone = "N/A",
                                                        villageId = "vil_1",
                                                        villageName = record.villageName,
                                                        ashaWorkerId = "asha_1",
                                                        ashaWorkerName = "ASHA Assigned",
                                                        currentRiskLevel = record.severity,
                                                        lastCondition = record.notes,
                                                        lastVisitDate = "Recent",
                                                        nextAppointmentDate = null,
                                                        emergencyContact = "108"
                                                    )
                                            },
                                            shape = PillShape,
                                            border = BorderStroke(1.dp, VS_Outline),
                                            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                            modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                        ) {
                                            Text(text = stringResource(R.string.history), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                                        }

                                        Button(
                                            onClick = { onSelectCase(record) },
                                            shape = PillShape,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = VS_Primary,
                                                contentColor = VS_OnBackground
                                            ),
                                            contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.xxs),
                                            modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                        ) {
                                            Text(text = stringResource(R.string.review), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Scheduled Appointments Accordion
        item {
            DashboardAccordionItem(
                icon = "📅",
                iconBackgroundColor = Color(0xFF3B82F6),
                title = "Scheduled Appointments",
                subtitle = "Manage upcoming and past appointments with patients and specialists.",
                expanded = expandedAppointments,
                onToggle = { expandedAppointments = !expandedAppointments }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showScheduleDialog = true },
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VS_Primary,
                            contentColor = VS_OnBackground
                        ),
                        contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                        modifier = Modifier.defaultMinSize(minHeight = 34.dp)
                    ) {
                        Text(text = stringResource(R.string.proposeAppt), style = MaterialTheme.typography.labelSmall)
                    }
                }

                if (appointments.isEmpty()) {
                    VitalSenseCard {
                        Text(
                            text = stringResource(R.string.noAppointmentsScheduled),
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnSurfaceVariant
                        )
                    }
                } else {
                    appointments.forEach { appointment ->
                        val isPending = appointment.status.contains("Pending", ignoreCase = true)
                        VitalSenseCard(
                            backgroundColor = if (isPending) VS_WarningContainer else VS_Surface
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = appointment.patientName,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = VS_OnBackground
                                        )
                                        Text(
                                            text = "${appointment.dateFormatted} at ${appointment.timeSlot}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        // Call Type Badge
                                        Surface(
                                            shape = PillShape,
                                            color = VS_PrimaryContainer
                                        ) {
                                            Text(
                                                text = if (appointment.callType == CallType.VOICE) "🎙️ Voice" else "📹 Video",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp),
                                                color = VS_PrimaryContainer
                                            )
                                        }

                                        Surface(
                                            shape = PillShape,
                                            color = if (isPending) VS_WarningContainer else VS_SuccessContainer
                                        ) {
                                            Text(
                                                text = appointment.status,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp),
                                                color = if (isPending) VS_Warning else VS_OnSuccessContainer
                                            )
                                        }
                                    }
                                }

                                if (isPending) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs, Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            onClick = { onDeclineAppointment(appointment.id) },
                                            shape = PillShape
                                        ) {
                                            Text(stringResource(R.string.declineAction), color = VS_Error, style = MaterialTheme.typography.labelSmall)
                                        }
                                        Button(
                                            onClick = { onAcceptAppointment(appointment.id) },
                                            shape = PillShape,
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                                            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                            modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                                        ) {
                                            Text(stringResource(R.string.acceptCheckAction), color = VS_Background, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                } else {
                                    val joinWindowStatus = AppointmentScheduleHelper.evaluateJoinWindow(appointment)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        when (joinWindowStatus) {
                                            JoinWindowStatus.JOIN_ACTIVE -> {
                                                Surface(
                                                    shape = PillShape,
                                                    color = VS_SuccessContainer
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.roomOpenStatus),
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            color = VS_Success,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }

                                                Button(
                                                    onClick = {
                                                        activeTeleConsultationPatient = appointment.patientName
                                                    },
                                                    shape = PillShape,
                                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                                    modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(if (appointment.callType == CallType.VOICE) "🎙️" else "📹", fontSize = 12.sp)
                                                        Text(
                                                            text = if (appointment.callType == CallType.VOICE) "Join Voice Call" else "Join Video Call",
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                color = Color.White,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                            JoinWindowStatus.BEFORE_WINDOW -> {
                                                Text(
                                                    text = "Room opens 10m before ${appointment.timeSlot}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = VS_OnSurfaceVariant
                                                )
                                                OutlinedButton(
                                                    onClick = { showScheduleDialog = true },
                                                    shape = PillShape,
                                                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                                    modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                                                ) {
                                                    Text(stringResource(R.string.rescheduleAction), style = MaterialTheme.typography.labelSmall, color = VS_PrimaryContainer)
                                                }
                                            }
                                            JoinWindowStatus.AFTER_WINDOW_MISSED -> {
                                                Text(
                                                    text = stringResource(R.string.patientDidntJoinWindow),
                                                    style = MaterialTheme.typography.labelSmall.copy(color = VS_Error),
                                                )
                                                Button(
                                                    onClick = { showScheduleDialog = true },
                                                    shape = PillShape,
                                                    colors = ButtonDefaults.buttonColors(containerColor = VS_SurfaceVariant),
                                                    border = BorderStroke(1.dp, VS_Outline),
                                                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                                    modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                                                ) {
                                                    Text(stringResource(R.string.rescheduleAction), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Dispensary Stock Accordion
        item {
            DashboardAccordionItem(
                icon = "💊",
                iconBackgroundColor = VS_Warning,
                title = "Dispensary Stock",
                subtitle = "Track medicine inventory, stock levels, and low stock alerts in real-time.",
                expanded = expandedDispensary,
                onToggle = { expandedDispensary = !expandedDispensary }
            ) {
                if (lowStockCount > 0) {
                    Surface(shape = PillShape, color = VS_ErrorContainer) {
                        Text(
                            text = "$lowStockCount ${stringResource(R.string.lowStock)}",
                            style = MaterialTheme.typography.labelSmall.copy(color = VS_Error, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                        )
                    }
                }

                dispensaryStock.forEach { item ->
                    val isReminded = item.id in remindedMedicineIds
                    VitalSenseCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.medicineName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = item.category,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                                ) {
                                    Text(
                                        text = "${item.availableQuantity} ${item.unit}",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.isLowStock) VS_Error else VS_OnBackground
                                        )
                                    )
                                    if (item.isLowStock) {
                                        Surface(shape = PillShape, color = VS_ErrorContainer) {
                                            Text(
                                                text = stringResource(R.string.lowStockTag),
                                                style = MaterialTheme.typography.labelSmall.copy(color = VS_Error, fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Doctor Restock Action Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isReminded) {
                                    Surface(
                                        shape = PillShape,
                                        color = VS_SuccessContainer,
                                        border = BorderStroke(1.dp, VS_Success.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = stringResource(R.string.adminRemindedBadge),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = VS_OnSuccessContainer
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            DismissedNoticeHelper.recordRemindedMedicine(context, item.id)
                                            remindedMedicineIds = remindedMedicineIds + item.id
                                            onRemindAdminRestock(item)
                                            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                            restockToastMessage = "Restock reminder sent to Admin for ${item.medicineName}!"
                                        },
                                        shape = PillShape,
                                        border = BorderStroke(1.dp, if (item.isLowStock) VS_Error else VS_Primary),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.remindAdminBtn),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.isLowStock) VS_Error else VS_Primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 8. Patient Records Directory Accordion
        if (patients.isNotEmpty()) {
            item {
                DashboardAccordionItem(
                    icon = "📁",
                    iconBackgroundColor = Color(0xFFEC4899),
                    title = "Patient Record History",
                    subtitle = "Access complete patient history including visits, prescriptions, and test reports.",
                    expanded = expandedPatientRecords,
                    onToggle = { expandedPatientRecords = !expandedPatientRecords }
                ) {
                    VitalSenseTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = stringResource(R.string.searchPatient),
                        placeholder = stringResource(R.string.searchPlaceholder)
                    )

                    val filteredPatients = patients.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                            it.villageName.contains(searchQuery, ignoreCase = true)
                    }

                    filteredPatients.forEach { pat ->
                        VitalSenseCard {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = pat.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "${pat.villageName} · Age: ${pat.age} (${pat.gender}) · ${stringResource(R.string.ashaAssigned)}: ${pat.ashaWorkerName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            val routineAppt = Appointment(
                                                id = "appt_${pat.id}_${System.currentTimeMillis()}",
                                                patientId = pat.id,
                                                patientName = pat.name,
                                                doctorId = doctor.id,
                                                doctorName = doctor.name,
                                                doctorSpecialty = doctor.specialty.displayName,
                                                dateFormatted = "Today",
                                                timeSlot = "Now",
                                                status = "Confirmed",
                                                proposedBy = UserRole.DOCTOR,
                                                callType = CallType.VIDEO,
                                                scheduledTimestamp = System.currentTimeMillis()
                                            )
                                            TeleCallingManager.startAppointmentCall(routineAppt, isDoctor = true)
                                            activeTeleConsultationPatient = pat.name
                                        },
                                        shape = PillShape,
                                        border = BorderStroke(1.dp, VS_Success),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                    ) {
                                        Text(stringResource(R.string.callActionBtn), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = VS_Success)
                                    }

                                    Button(
                                        onClick = { selectedPatientForHistory = pat },
                                        shape = PillShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = VS_PrimaryContainer,
                                            contentColor = VS_OnPrimaryContainer
                                        ),
                                        contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.history),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = VS_OnPrimaryContainer
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = { selectedPatientForReferral = pat },
                                        shape = PillShape,
                                        border = BorderStroke(1.dp, VS_Primary),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.refer),
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
        }

        // 9. District Health Advisories & Directives
        if (adminDirectives.isNotEmpty() || dismissedAdvisoryIds.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📢 ${stringResource(R.string.districtAdvisories)}",
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

            items(adminDirectives) { directive ->
                VitalSenseCard(
                    backgroundColor = if (directive.isUrgent) VS_ErrorContainer else VS_Surface
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = directive.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (directive.isUrgent) VS_Error else VS_OnBackground
                            )
                            if (directive.isUrgent) {
                                Surface(shape = PillShape, color = VS_Error) {
                                    Text(
                                        text = stringResource(R.string.directiveLabel),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VS_OnBackground),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = directive.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnBackground
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${stringResource(R.string.issuedBy)} ${directive.senderName} (${directive.senderRole.name})",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    DismissedNoticeHelper.dismissAdvisory(context, "doctor", directive.id)
                                    dismissedAdvisoryIds = dismissedAdvisoryIds + directive.id
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = PillShape
                            ) {
                                Text(
                                    text = stringResource(R.string.dismissBtn),
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

    if (showScheduleDialog) {
        ScheduleAppointmentDialog(
            patients = patients,
            onDismiss = { showScheduleDialog = false },
            onPropose = { patientId, patientName, date, timeSlot ->
                onProposeAppointment(patientId, patientName, date, timeSlot)
            }
        )
    }

    selectedPatientForHistory?.let { patient ->
        PatientHistoryDialog(
            patient = patient,
            conditions = allConditions,
            prescriptions = allPrescriptions,
            appointments = appointments,
            onDismiss = { selectedPatientForHistory = null }
        )
    }

    selectedPatientForReferral?.let { targetPatient ->
        CreateReferralDialog(
            patient = targetPatient,
            patientNameFallback = targetPatient.name,
            currentDoctor = doctor,
            priorPrescriptions = allPrescriptions.filter { it.patientId == targetPatient.id },
            allConditions = allConditions.filter { it.patientId == targetPatient.id },
            onDismiss = { selectedPatientForReferral = null },
            onSendReferral = { ref ->
                onSendReferral(ref)
                selectedPatientForReferral = null
            },
            onEmergencyCallTrigger = {
                val routineAppt = Appointment(
                    id = "appt_${targetPatient.id}_${System.currentTimeMillis()}",
                    patientId = targetPatient.id,
                    patientName = targetPatient.name,
                    doctorId = doctor.id,
                    doctorName = doctor.name,
                    doctorSpecialty = doctor.specialty.displayName,
                    dateFormatted = "Today",
                    timeSlot = "Now",
                    status = "Confirmed",
                    proposedBy = UserRole.DOCTOR,
                    callType = CallType.VIDEO,
                    scheduledTimestamp = System.currentTimeMillis()
                )
                TeleCallingManager.startAppointmentCall(routineAppt, isDoctor = true)
                activeTeleConsultationPatient = targetPatient.name
                selectedPatientForReferral = null
            }
        )
    }

    activeTeleConsultationPatient?.let { patName ->
        TeleConsultationModal(
            patientName = patName,
            doctorName = doctor.name,
            specialty = doctor.specialty.displayName,
            isDoctorViewer = true,
            onDismiss = {
                activeTeleConsultationPatient = null
                TeleCallingManager.endCall("Doctor dismissed call modal")
            },
            onEndCall = { notes ->
                activeTeleConsultationPatient = null
                TeleCallingManager.endCall(notes)
            }
        )
    }

    // Incoming Call Modal Dialog (Distinct UI for Appointment vs Emergency)
    val incomingSession = activeCallSession
    if (incomingSession != null && incomingSession.state == CallSessionState.INCOMING_RINGING) {
        val isEmergency = incomingSession.mode == CallMode.EMERGENCY

        AlertDialog(
            onDismissRequest = {
                TeleCallingManager.declineCall()
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(if (isEmergency) "🚨" else "📅", fontSize = 24.sp)
                    Text(
                        text = if (isEmergency) "CRITICAL EMERGENCY CALL" else "Incoming Appointment Call",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isEmergency) VS_Error else VS_Primary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Patient: ${incomingSession.patientName} (${incomingSession.patientAge} yrs)",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Text(
                        text = "Village: ${incomingSession.villageName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )

                    if (isEmergency) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = VS_ErrorContainer,
                            border = BorderStroke(1.dp, VS_Error)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.liveVitalsStatusHalo),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VS_OnErrorContainer
                                    )
                                )
                                Text(
                                    text = incomingSession.patientVitalsSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Type: ${if (incomingSession.type == CallType.VOICE) "🎙️ Voice Consultation" else "📹 Video Consultation"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        TeleCallingManager.acceptCall()
                        activeTeleConsultationPatient = incomingSession.patientName
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEmergency) VS_Error else VS_Success
                    ),
                    shape = PillShape,
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(
                        text = if (isEmergency) "🚨 Accept Emergency Call" else "Accept Call ✓",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                if (isEmergency) {
                    OutlinedButton(
                        onClick = {
                            val targetPatient = patients.firstOrNull { it.id == incomingSession.patientId } ?: Patient(
                                id = incomingSession.patientId,
                                name = incomingSession.patientName,
                                age = incomingSession.patientAge,
                                gender = "M",
                                phone = "9876543210",
                                villageId = "vil_sundarpura",
                                villageName = incomingSession.villageName,
                                ashaWorkerId = "asha_1",
                                ashaWorkerName = "Sarita Devi",
                                currentRiskLevel = SeverityLevel.MODERATE,
                                lastCondition = "Emergency triage",
                                lastVisitDate = "Today",
                                nextAppointmentDate = null,
                                emergencyContact = "108"
                            )
                            TeleCallingManager.escalateEmergencyCall(context, targetPatient, listOf(doctor))
                        },
                        shape = PillShape
                    ) {
                        Text(stringResource(R.string.transferToNextOnCall), color = VS_Warning)
                    }
                } else {
                    TextButton(
                        onClick = {
                            TeleCallingManager.declineCall()
                        },
                        shape = PillShape
                    ) {
                        Text(stringResource(R.string.declineAction), color = VS_OnSurfaceVariant)
                    }
                }
            }
        )
    }

    if (sosToClear != null) {
        AlertDialog(
            onDismissRequest = { sosToClear = null },
            title = {
                Text(
                    text = stringResource(R.string.confirmEmergencyResolved),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Text(
                    text = "Confirm that emergency clinical action has been taken and patient ${sosToClear?.senderName} is stabilized?",
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
                        text = stringResource(R.string.yesMarkClearDismiss),
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
                    Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant)
                }
            }
        )
    }
}
