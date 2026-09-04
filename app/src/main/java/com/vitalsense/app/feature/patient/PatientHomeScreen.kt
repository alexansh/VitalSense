package com.vitalsense.app.feature.patient

import androidx.compose.animation.*
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
import com.vitalsense.app.core.ui.util.touchSpring
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
import com.vitalsense.app.feature.patient.components.LogSymptomDialog
import com.vitalsense.app.feature.patient.components.HealthCardDialog
import com.vitalsense.app.feature.patient.components.GovernmentSchemesDialog
import com.vitalsense.app.feature.patient.components.SensorPairingDialog
import com.vitalsense.app.feature.patient.components.SmartEmergencyDialog
import com.vitalsense.app.core.util.DismissedNoticeHelper
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun PatientHomeScreen(
    patient: Patient,
    notices: List<BroadcastNotice> = emptyList(),
    prescriptions: List<Prescription> = emptyList(),
    schemes: List<GovernmentScheme> = emptyList(),
    familyMembers: List<com.vitalsense.app.core.data.model.FamilyMember> = emptyList(),
    onCategoryClick: (ConditionCategory) -> Unit = {},
    onLogCondition: (ConditionRecord) -> Unit = {},
    onViewHealthCard: () -> Unit = {},
    onTriggerSos: () -> Unit = {},
    onSavePrescription: (Prescription) -> Unit = {},
    onNavigateToLabReports: () -> Unit = {},
    onNavigateToOpdQueue: () -> Unit = {},
    onNavigateToBloodBank: () -> Unit = {},
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToLiveQueue: () -> Unit = {},
    referrals: List<com.vitalsense.app.core.data.model.Referral> = emptyList(),
    scrollState: LazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() },
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    var showSmartEmergencyDialog by remember { mutableStateOf(false) }
    var showSensorPairingDialog by remember { mutableStateOf(false) }
    var showPrescriptionUploadDialog by remember { mutableStateOf(false) }
    var selectedMedicineForNearby by remember { mutableStateOf<com.vitalsense.app.core.data.model.PrescribedMedicine?>(null) }

    // Live vitals readings as per UX Architecture §3.2
    var heartRate by remember { mutableStateOf(76) }
    var spO2 by remember { mutableStateOf(98) }
    var bloodPressure by remember { mutableStateOf("120/80") }
    var temperature by remember { mutableStateOf("98.4°F") }
    var readingCapturedSuccess by remember { mutableStateOf(false) }

    // Dialog states for complete functional implementation
    var showHealthCardDialog by remember { mutableStateOf(false) }
    var showLogSymptomDialog by remember { mutableStateOf(false) }
    var activeSymptomCategory by remember { mutableStateOf(ConditionCategory.GENERAL_MEDICINE) }
    var showSchemesDialog by remember { mutableStateOf(false) }
    var conditionLoggedSuccess by remember { mutableStateOf(false) }

    // Toggle to evaluate Glume Dark Mode vs Sunlight High-Contrast Mode for Patient
    var isSunlightMode by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current

    var dismissedAdvisoryIds by remember(patient.id) {
        mutableStateOf(DismissedNoticeHelper.getDismissedAdvisoryIds(context, "patient"))
    }

    val adminAdvisories = remember(notices, dismissedAdvisoryIds) {
        notices.filter {
            (it.senderRole == UserRole.ADMIN || it.targetRole == "ALL" || it.targetRole == "PATIENT") &&
                it.id !in dismissedAdvisoryIds
        }
    }

    // Colors dynamically adapt based on Dark vs Sunlight High-Contrast Mode
    val bgColor = if (isSunlightMode) VS_Background else VS_Background
    val cardBgColor = if (isSunlightMode) VS_Surface else VS_Surface
    val elevatedBgColor = if (isSunlightMode) VS_SurfaceVariant else VS_SurfaceVariant
    val cardBorderColor = if (isSunlightMode) VS_Outline else VS_Outline
    val textPrimaryColor = if (isSunlightMode) VS_OnBackground else VS_OnBackground
    val textSecondaryColor = if (isSunlightMode) VS_OnSurfaceVariant else VS_OnSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            contentPadding = PaddingValues(top = Spacing.sm, bottom = 140.dp)
        ) {
        // 1. Personalized Greeting (NagarSeva Modern Typography)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${stringResource(R.string.namaste)}, ${patient.name.split(" ").first()}!",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            ),
                            color = textPrimaryColor
                        )
                        Text(
                            text = "${stringResource(R.string.village)}: ${patient.villageName} · ${stringResource(R.string.ashaAssigned)}: ${patient.ashaWorkerName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textSecondaryColor
                        )
                    }
                    SeverityBadge(severity = patient.currentRiskLevel)
                }
            }
        }

        // 2. THE STATUS HALO & 2x2 VITAL TILES (Hero Element as per UX Architecture §3.2 & §5.2)
        item {
            StatusHaloCard(
                patient = patient,
                heartRate = heartRate,
                spO2 = spO2,
                bloodPressure = bloodPressure,
                temperature = temperature,
                language = language,
                onTakeReadingClick = { showSensorPairingDialog = true }
            )
        }

        // Success banner when new sensor vitals reading is captured
        if (readingCapturedSuccess) {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Text("✓", color = VS_Success, fontWeight = FontWeight.Bold)
                            Text(
                                text = "New health reading recorded: $heartRate bpm · $spO2% SpO2",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Success
                            )
                        }
                        IconButton(onClick = { readingCapturedSuccess = false }, modifier = Modifier.size(24.dp)) {
                            Text("✕", color = VS_Success, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 3. Offline Health Card & Daily Status (Glume Rounded Card)
        item {
            VitalSenseCard(
                backgroundColor = cardBgColor,
                border = BorderStroke(1.dp, cardBorderColor),
                onClick = { showHealthCardDialog = true }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(VS_PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🪪", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = stringResource(R.string.offlineHealthCard),
                                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.5.sp),
                                    color = VS_Primary
                                )
                                Text(
                                    text = stringResource(R.string.permanentQrOfflineRecord),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondaryColor
                                )
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = VS_Primary,
                            modifier = Modifier.clickable { showHealthCardDialog = true }
                        ) {
                            Text(
                                text = stringResource(R.string.viewCard),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = VS_OnBackground,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = cardBorderColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.activeCondition),
                                style = MaterialTheme.typography.labelSmall,
                                color = textSecondaryColor
                            )
                            Text(
                                text = patient.lastCondition,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = VS_SuccessContainer
                        ) {
                            Text(
                                text = stringResource(R.string.cachedOffline),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = VS_OnSuccessContainer
                                ),
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "${stringResource(R.string.nextCheckup)} ${patient.nextAppointmentDate ?: stringResource(R.string.noneScheduled)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondaryColor
                    )
                }
            }
        }

        // Success banner when symptoms logged
        if (conditionLoggedSuccess) {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Text("✓", color = VS_Success, fontWeight = FontWeight.Bold)
                            Text(
                                text = stringResource(R.string.symptomsSubmittedTriage),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Success
                            )
                        }
                        IconButton(onClick = { conditionLoggedSuccess = false }, modifier = Modifier.size(24.dp)) {
                            Text("✕", color = VS_Success, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 3. Section Title: Health Categories (Icon-First & High Contrast)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                Text(
                    text = stringResource(R.string.howCanWeHelp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = textPrimaryColor
                )
                Text(
                    text = stringResource(R.string.tapServiceDesc),
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor
                )
            }
        }

        // 3.1 Categories 2-Column Grid (Large accessible tap targets)
        item {
            val categories = listOf(
                ConditionCategory.GENERAL_MEDICINE,
                ConditionCategory.MATERNAL_HEALTH,
                ConditionCategory.FITNESS,
                ConditionCategory.NUTRITION,
                ConditionCategory.MENTAL_HEALTH
            )

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                categories.chunked(2).forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        rowCategories.forEach { category ->
                            CategoryChip(
                                category = category,
                                isSelected = false,
                                onClick = {
                                    if (category == ConditionCategory.MENTAL_HEALTH) {
                                        onCategoryClick(category)
                                    } else {
                                        activeSymptomCategory = category
                                        showLogSymptomDialog = true
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowCategories.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // 3.1 Live Clinic Queue & Appointments Entry
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .touchSpring(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VS_Surface),
                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.3f)),
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
                                Text("🎫", fontSize = 22.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.liveQueueAndAppointments),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.liveQueueDesc),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = onNavigateToLiveQueue,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(stringResource(R.string.hud), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        OutlinedButton(
                            onClick = onNavigateToAppointments,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, VS_Primary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(stringResource(R.string.book), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VS_Primary)
                        }
                    }
                }
            }
        }

        // 3.2 Hospital Services Hub (Lab Reports, OPD Queue, Blood Bank)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    text = stringResource(R.string.hospitalClinicalServices),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textPrimaryColor
                )
                Text(
                    text = stringResource(R.string.hospitalServicesDesc),
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    // Lab Reports Card
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = elevatedBgColor,
                        border = BorderStroke(1.dp, cardBorderColor),
                        onClick = onNavigateToLabReports
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🧪", fontSize = 24.sp)
                            Text(
                                text = stringResource(R.string.labReports),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                            Text(
                                text = stringResource(R.string.labReportsSub),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = textSecondaryColor
                            )
                        }
                    }

                    // OPD Queue Card
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = elevatedBgColor,
                        border = BorderStroke(1.dp, cardBorderColor),
                        onClick = onNavigateToOpdQueue
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🎟️", fontSize = 24.sp)
                            Text(
                                text = stringResource(R.string.opdQueue),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                            Text(
                                text = stringResource(R.string.opdQueueSub),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = textSecondaryColor
                            )
                        }
                    }

                    // Blood Bank Card
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = elevatedBgColor,
                        border = BorderStroke(1.dp, cardBorderColor),
                        onClick = onNavigateToBloodBank
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🩸", fontSize = 24.sp)
                            Text(
                                text = stringResource(R.string.bloodBank),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                            Text(
                                text = stringResource(R.string.bloodBankSub),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = textSecondaryColor
                            )
                        }
                    }
                }

                // Dedicated Routine Video & Voice Consultation Card (Step 3A - Independent of SOS)
                VitalSenseCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = elevatedBgColor,
                    border = BorderStroke(1.5.dp, VS_Primary.copy(alpha = 0.6f)),
                    onClick = onNavigateToAppointments
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("📹", fontSize = 22.sp)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.consultationCardTitle),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = textPrimaryColor
                                )
                                Surface(shape = PillShape, color = VS_PrimaryContainer) {
                                    Text(
                                        text = stringResource(R.string.routineBadge),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VS_PrimaryContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = stringResource(R.string.consultationCardDesc),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = textSecondaryColor
                            )
                        }
                        Text("→", fontSize = 20.sp, color = VS_Primary)
                    }
                }
            }
        }

        // 3.9 Doctor-to-Doctor Specialist Consultations (Plain Language Patient View)
        if (referrals.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text(
                        text = "${stringResource(R.string.doctorReferralsTitle)} (${referrals.size})",
                        style = MaterialTheme.typography.headlineMedium,
                        color = textPrimaryColor
                    )

                    referrals.forEach { ref ->
                        com.vitalsense.app.feature.patient.components.ReferralStatusCard(
                            referral = ref,
                            language = language,
                            onScheduleCall = onNavigateToAppointments
                        )
                    }
                }
            }
        }

        // 4. My Prescriptions Section (Glume Dark Slate Card Style)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.myPrescriptions)} (${prescriptions.size})",
                    style = MaterialTheme.typography.headlineMedium,
                    color = textPrimaryColor
                )

                Button(
                    onClick = { showPrescriptionUploadDialog = true },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VS_Primary,
                        contentColor = VS_OnBackground
                    ),
                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Text(stringResource(R.string.uploadRx), style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        if (prescriptions.isEmpty()) {
            item {
                VitalSenseCard(
                    backgroundColor = cardBgColor,
                    border = BorderStroke(1.dp, cardBorderColor)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.noPrescriptions),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                            Text(
                                text = stringResource(R.string.scanOrWrite),
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor
                            )
                        }
                        OutlinedButton(
                            onClick = { showPrescriptionUploadDialog = true },
                            shape = PillShape,
                            border = BorderStroke(1.dp, VS_Primary)
                        ) {
                            Text(stringResource(R.string.uploadRx), style = MaterialTheme.typography.labelSmall, color = VS_Primary)
                        }
                    }
                }
            }
        } else {
            items(prescriptions) { rx ->
                VitalSenseCard(
                    backgroundColor = cardBgColor,
                    border = BorderStroke(1.dp, cardBorderColor)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = rx.doctorName,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = textPrimaryColor
                                )
                                Text(
                                    text = "${rx.doctorSpecialty} · ${rx.dateFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondaryColor
                                )
                            }
                            if (rx.isOcrExtracted) {
                                Surface(shape = PillShape, color = VS_SuccessContainer) {
                                    Text(
                                        text = stringResource(R.string.aiScannedBadge),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VS_OnSuccessContainer),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        rx.medicines.forEach { med ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "• ${med.name} (${med.dosage})",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = textPrimaryColor
                                        )
                                        Text(
                                            text = "${med.frequency} · ${med.duration}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = textSecondaryColor
                                        )
                                        if (med.hasAlternativeAvailable) {
                                            Text(
                                                text = stringResource(R.string.docSuggestedAlternativePlain),
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = VS_Error
                                            )
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = { selectedMedicineForNearby = med },
                                        modifier = Modifier.height(34.dp),
                                        shape = PillShape,
                                        border = BorderStroke(1.dp, VS_Primary),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.findNearbyLink),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_Primary
                                        )
                                    }
                                }
                            }
                        }

                        if (rx.instructions.isNotBlank()) {
                            Text(
                                text = "Note: ${rx.instructions}",
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor
                            )
                        }
                    }
                }
            }
        }

        // 5. District Health Advisories
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
                        color = textPrimaryColor
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

            items(adminAdvisories) { advisory ->
                VitalSenseCard(
                    backgroundColor = if (advisory.isUrgent) VS_ErrorContainer else cardBgColor,
                    border = BorderStroke(1.dp, if (advisory.isUrgent) VS_Error.copy(alpha = 0.4f) else cardBorderColor)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = advisory.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (advisory.isUrgent) VS_OnErrorContainer else textPrimaryColor
                            )
                            if (advisory.isUrgent) {
                                Surface(shape = PillShape, color = VS_Error) {
                                    Text(
                                        text = stringResource(R.string.urgent),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = VS_OnBackground
                                        ),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = advisory.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textPrimaryColor
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${stringResource(R.string.issuedBy)} ${advisory.senderName} (${advisory.senderRole.name})",
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    DismissedNoticeHelper.dismissAdvisory(context, "patient", advisory.id)
                                    dismissedAdvisoryIds = dismissedAdvisoryIds + advisory.id
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = PillShape
                            ) {
                                Text(
                                    text = stringResource(R.string.dismissBtn),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (advisory.isUrgent) VS_OnErrorContainer else VS_Primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Rural Welfare & Government Schemes Card
        item {
            VitalSenseCard(
                backgroundColor = cardBgColor,
                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f)),
                onClick = { showSchemesDialog = true }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🏛️", fontSize = 20.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.ruralHealthSchemesPmjay),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = textPrimaryColor
                            )
                            Text(
                                text = stringResource(R.string.freeTreatment5Lakh),
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor
                            )
                        }
                    }
                    Surface(
                        shape = PillShape,
                        color = VS_Primary,
                        modifier = Modifier.clickable { showSchemesDialog = true }
                    ) {
                        Text(
                            text = stringResource(R.string.viewSchemesBtn),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp)
                        )
                    }
                }
            }
        }

    }

        // Floating Emergency SOS Button anchored to BottomEnd
        ExtendedFloatingActionButton(
            onClick = { showSmartEmergencyDialog = true },
            icon = { Text(text = "🚨", fontSize = 20.sp) },
            text = {
                Text(
                    text = stringResource(R.string.emergencySos),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            },
            containerColor = VS_Error,
            contentColor = VS_OnBackground,
            shape = PillShape,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 12.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Spacing.md)
                .navigationBarsPadding()
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
        )
    }

    // Smart Emergency Dialog with 3s Countdown & Auto-GPS/SMS (UX Architecture §3.4)
    if (showSmartEmergencyDialog) {
        SmartEmergencyDialog(
            patient = patient,
            language = language,
            onDismiss = { showSmartEmergencyDialog = false },
            onSosDispatched = {
                onTriggerSos()
            }
        )
    }

    // Step-by-Step Bluetooth Sensor Pairing Flow (UX Architecture §3.3 & §5.3)
    if (showSensorPairingDialog) {
        SensorPairingDialog(
            patient = patient,
            language = language,
            onDismiss = { showSensorPairingDialog = false },
            onReadingCaptured = { newHr, newSpo2, newBp, newTemp ->
                heartRate = newHr
                spO2 = newSpo2
                bloodPressure = newBp
                temperature = newTemp
                readingCapturedSuccess = true
            }
        )
    }

    if (showPrescriptionUploadDialog) {
        com.vitalsense.app.feature.prescriptions.PrescriptionUploadDialog(
            patient = patient,
            isAshaProxy = false,
            onDismiss = { showPrescriptionUploadDialog = false },
            onSavePrescription = onSavePrescription
        )
    }

    if (showHealthCardDialog) {
        HealthCardDialog(
            patient = patient,
            familyMembers = familyMembers,
            onDismiss = { showHealthCardDialog = false }
        )
    }

    if (showLogSymptomDialog) {
        LogSymptomDialog(
            patient = patient,
            initialCategory = activeSymptomCategory,
            onDismiss = { showLogSymptomDialog = false },
            onSubmit = { record ->
                onLogCondition(record)
                showLogSymptomDialog = false
                conditionLoggedSuccess = true
            }
        )
    }

    if (showSchemesDialog) {
        GovernmentSchemesDialog(
            schemes = schemes,
            onDismiss = { showSchemesDialog = false }
        )
    }

    selectedMedicineForNearby?.let { med ->
        FindMedicineNearbySheet(
            patient = patient,
            medicine = med,
            language = language,
            onDismiss = { selectedMedicineForNearby = null }
        )
    }
}
