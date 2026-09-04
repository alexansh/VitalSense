package com.vitalsense.app.feature.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.vitalsense.app.feature.admin.components.DistrictOutbreakMapView
import com.vitalsense.app.core.util.DismissedNoticeHelper
import com.vitalsense.app.core.util.AudioGuidanceHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun AdminHomeScreen(
    villages: List<Village>,
    notices: List<BroadcastNotice>,
    dispensaryStock: List<DispensaryItem> = emptyList(),
    onSendBroadcast: (title: String, message: String, village: String?) -> Unit,
    onNavigateToDispensary: () -> Unit,
    onNavigateToDiseaseTrends: () -> Unit,
    onNavigateToIpdBeds: () -> Unit = {},
    onNavigateToOtScheduler: () -> Unit = {},
    onNavigateToExternalReferrals: () -> Unit = {},
    onNavigateToBioMedical: () -> Unit = {},
    onNavigateToQueueOversight: () -> Unit = {},
    onNavigateToFacilityQuality: () -> Unit = {},
    onNavigateToDiagnostics: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var broadcastTitle by remember { mutableStateOf("") }
    var broadcastMessage by remember { mutableStateOf("") }
    var selectedVillageName by remember { mutableStateOf("All Villages") }
    var selectedMapVillage by remember { mutableStateOf<Village?>(villages.firstOrNull()) }
    var isFormError by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    var dismissedDirectiveIds by remember {
        mutableStateOf(DismissedNoticeHelper.getDismissedDirectiveIds(context))
    }
    var dismissedRestockReminderIds by remember {
        mutableStateOf(DismissedNoticeHelper.getDismissedRestockReminderIds(context))
    }

    val adminIssuedDirectives = remember(notices, dismissedDirectiveIds) {
        notices.filter { it.senderRole == UserRole.ADMIN && it.id !in dismissedDirectiveIds }
    }
    val doctorRestockReminders = remember(notices, dismissedRestockReminderIds) {
        notices.filter {
            it.senderRole == UserRole.DOCTOR &&
                (it.title.contains("Restock Reminder", ignoreCase = true) || it.title.contains("Restock", ignoreCase = true)) &&
                it.id !in dismissedRestockReminderIds
        }
    }
    val totalActiveCases = villages.sumOf { it.activeCases }
    val totalPopulation = villages.sumOf { it.population }
    val outbreakCount = villages.count { it.highRiskCount > 0 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.sm, bottom = Spacing.xxl)
    ) {
        // 1. Admin Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                Text(
                    text = stringResource(R.string.districtCommand),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = VS_OnBackground
                )
                Text(
                    text = "Surveillance Region: Rampur District, UP (Pop: $totalPopulation)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VS_OnSurfaceVariant
                )
            }
        }

        // 1.1 Doctor Restock Reminders Alert (High-Priority Action Required)
        if (doctorRestockReminders.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .touchSpring(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VS_WarningContainer),
                    border = BorderStroke(1.5.dp, VS_Warning.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("🔔", fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = "Doctor Restock Reminders (${doctorRestockReminders.size})",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = stringResource(R.string.doctorsFlaggedLowMeds),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                            }
                            Surface(shape = PillShape, color = VS_Warning) {
                                Text(
                                    text = stringResource(R.string.restockAction),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        doctorRestockReminders.forEach { reminder ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = VS_Surface),
                                border = BorderStroke(1.dp, VS_Outline)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = reminder.title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_OnBackground,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(reminder.timestamp)),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = reminder.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnBackground
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = onNavigateToDispensary,
                                            shape = PillShape,
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.restockNowBtn),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        OutlinedButton(
                                            onClick = {
                                                DismissedNoticeHelper.dismissRestockReminder(context, reminder.id)
                                                dismissedRestockReminderIds = dismissedRestockReminderIds + reminder.id
                                                AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                            },
                                            shape = PillShape,
                                            border = BorderStroke(1.dp, VS_Outline),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.dismissReminder),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = VS_OnSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Summary stats (Glume 3-Column Compact Grid)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                GlumeStatCard(
                    label = stringResource(R.string.totalActiveCases),
                    value = "$totalActiveCases",
                    icon = "🚨",
                    modifier = Modifier.weight(1f),
                    badgeText = if (totalActiveCases > 0) "Active" else null,
                    badgeColor = VS_Error
                )
                GlumeStatCard(
                    label = stringResource(R.string.assignedVillages),
                    value = "${villages.size}",
                    icon = "🏡",
                    modifier = Modifier.weight(1f),
                    badgeText = "Villages",
                    badgeColor = VS_Primary
                )
                GlumeStatCard(
                    label = stringResource(R.string.outbreakSurveillance),
                    value = "$outbreakCount",
                    icon = "⚠️",
                    modifier = Modifier.weight(1f),
                    badgeText = if (outbreakCount > 0) "Clusters" else "Safe",
                    badgeColor = if (outbreakCount > 0) VS_Error else VS_Success
                )
            }
        }

        // 3. Section: Village Disease Trend Heat Map Cards
        item {
            Text(
                text = "🗺️ ${stringResource(R.string.outbreakSurveillance)}",
                style = MaterialTheme.typography.headlineMedium,
                color = VS_OnBackground
            )
        }

        // 3.1 Google Maps Outbreak Surveillance View
        item {
            DistrictOutbreakMapView(
                villages = villages,
                selectedVillage = selectedMapVillage,
                onSelectVillage = { selectedMapVillage = it },
                onBroadcastToVillage = { village ->
                    selectedVillageName = village.name
                    broadcastTitle = "Health Advisory for ${village.name}"
                    broadcastMessage = "Urgent: Heightened medical monitoring active for ${village.name}. Please consult your nearest ASHA worker."
                    showBroadcastDialog = true
                }
            )
        }

        // 3.2 Monitored Village Cards List
        item {
            Text(
                text = "Village Health Telemetry (${villages.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
        }

        items(villages) { village ->
            val isHighRisk = village.highRiskCount > 0
            val isSelected = selectedMapVillage?.id == village.id
            val riskLevel = if (village.highRiskCount > 2) SeverityLevel.SEVERE else if (village.highRiskCount > 0) SeverityLevel.HIGH else if (village.activeCases > 5) SeverityLevel.MODERATE else SeverityLevel.LOW

            VitalSenseCard(
                backgroundColor = if (isSelected) VS_SurfaceVariant else if (isHighRisk) VS_ErrorContainer else VS_Surface,
                border = BorderStroke(1.dp, if (isSelected) VS_Primary else if (isHighRisk) VS_Error.copy(alpha = 0.4f) else VS_Outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMapVillage = village },
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                            ) {
                                Text(
                                    text = village.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                if (isSelected) {
                                    Surface(shape = PillShape, color = VS_Primary) {
                                        Text(
                                            text = stringResource(R.string.pinnedOnMap),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Population: ${village.population} · Active Cases: ${village.activeCases} · High Risk: ${village.highRiskCount}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                        SeverityBadge(severity = riskLevel)
                    }

                    // Progress Bar
                    val ratio = (village.activeCases.toFloat() / max(village.population, 1) * 100f).coerceIn(0f, 100f)
                    LinearProgressIndicator(
                        progress = { (ratio / 10f).coerceIn(0.05f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(PillShape),
                        color = when (riskLevel) {
                            SeverityLevel.SEVERE -> VS_Error
                            SeverityLevel.HIGH -> VS_Error
                            SeverityLevel.MODERATE -> VS_Warning
                            SeverityLevel.LOW -> VS_Success
                        },
                        trackColor = VS_SurfaceVariant,
                    )
                }
            }
        }

        // 4. Admin Management Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                VitalSenseButton(
                    text = stringResource(R.string.manageDispensary),
                    onClick = onNavigateToDispensary,
                    style = ButtonStyle.SECONDARY,
                    modifier = Modifier.weight(1f)
                )
                VitalSenseButton(
                    text = stringResource(R.string.diseaseTrendsTitle),
                    onClick = onNavigateToDiseaseTrends,
                    style = ButtonStyle.SECONDARY,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.xs))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                VitalSenseButton(
                    text = stringResource(R.string.diagnosticsLabs),
                    onClick = onNavigateToDiagnostics,
                    style = ButtonStyle.SECONDARY,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4.2 Hospital Operations & Infrastructure Desk
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    text = stringResource(R.string.hospitalOpsCareDesk),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = stringResource(R.string.hospitalOpsCareDesc),
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        onClick = onNavigateToIpdBeds
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🛏️", fontSize = 22.sp)
                            Text(stringResource(R.string.ipdWardsBeds), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                            Text(stringResource(R.string.occupancyAdmission), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                        }
                    }

                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        onClick = onNavigateToOtScheduler
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🔪", fontSize = 22.sp)
                            Text(stringResource(R.string.otSurgeryDesk), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                            Text(stringResource(R.string.pacSurgeonRoster), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        onClick = onNavigateToExternalReferrals
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🏛️", fontSize = 22.sp)
                            Text(stringResource(R.string.externalReferralsDesk), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                            Text(stringResource(R.string.aiimsCashlessDesk), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                        }
                    }

                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        onClick = onNavigateToBioMedical
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("⚡", fontSize = 22.sp)
                            Text(stringResource(R.string.bioMedicalRegistry), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                            Text(stringResource(R.string.oxygenEquipment), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = VS_OnSurfaceVariant)
                        }
                    }
                }
            }
        }

        // 4.1 Live District Queue Oversight Card
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
                                Text("📊", fontSize = 22.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.liveClinicQueueOversight),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.monitorDoctorQueues),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToQueueOversight,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(stringResource(R.string.monitorBtn), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onNavigateToFacilityQuality)
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
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = VS_PrimaryContainer.copy(alpha = 0.15f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🏥", fontSize = 22.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.facilityQualityMetrics),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.monitorPhcInfrastructure),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToFacilityQuality,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(stringResource(R.string.viewBtn), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }


        // 5. Broadcast Action Button (Single Full-Width Purple CTA)
        item {
            VitalSenseButton(
                text = stringResource(R.string.broadcastDistrictDirective),
                onClick = {
                    selectedVillageName = "All Villages"
                    showBroadcastDialog = true
                },
                style = ButtonStyle.PRIMARY
            )
        }

        // 6. Active Directives Sent by Admin
        if (adminIssuedDirectives.isNotEmpty()) {
            item {
                Text(
                    text = "Dispatched Health Directives (${adminIssuedDirectives.size})",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VS_OnBackground
                )
            }

            items(adminIssuedDirectives) { directive ->
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = directive.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Surface(shape = PillShape, color = VS_SuccessContainer) {
                                Text(
                                    text = stringResource(R.string.dispatchedStatus),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VS_OnSuccessContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
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
                                text = "Target: ${directive.targetVillage ?: "All Villages"} · Sender: ${directive.senderName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    DismissedNoticeHelper.dismissDirective(context, directive.id)
                                    dismissedDirectiveIds = dismissedDirectiveIds + directive.id
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

        // 7. District Dispensary Stock Check (Summary)
        if (dispensaryStock.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dispensaryLowStockAlerts),
                        style = MaterialTheme.typography.headlineMedium,
                        color = VS_OnBackground
                    )
                }
            }

            val lowStockItems = dispensaryStock.filter { it.isLowStock }
            if (lowStockItems.isNotEmpty()) {
                items(lowStockItems) { item ->
                    VitalSenseCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
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
                                        color = VS_Error
                                    )
                                )
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
                }
            } else {
                item {
                    Text(stringResource(R.string.allStockAboveThresholds), color = VS_OnSurfaceVariant)
                }
            }
        }
    }

    // Broadcast Notice Modal Dialog
    if (showBroadcastDialog) {
        VitalSenseDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = "Broadcast Health Directive",
            subtitle = "Push real-time alert to Doctors, ASHA workers & Patients",
            icon = { Text("📢", fontSize = 22.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        if (broadcastTitle.isBlank() || broadcastMessage.isBlank()) {
                            isFormError = true
                        } else {
                            val villageParam = if (selectedVillageName == "All Villages") null else selectedVillageName
                            onSendBroadcast(broadcastTitle.trim(), broadcastMessage.trim(), villageParam)
                            broadcastTitle = ""
                            broadcastMessage = ""
                            showBroadcastDialog = false
                            isFormError = false
                        }
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                ) {
                    Text(stringResource(R.string.broadcastNowBtn), color = VS_OnBackground, style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBroadcastDialog = false },
                    shape = PillShape
                ) {
                    Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant, style = MaterialTheme.typography.labelLarge)
                }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                VitalSenseTextField(
                    value = broadcastTitle,
                    onValueChange = { broadcastTitle = it },
                    label = "Directive Title",
                    placeholder = "e.g. Water Contamination Boil Notice",
                    isError = isFormError && broadcastTitle.isBlank(),
                    errorMessage = "Title is required"
                )

                VitalSenseTextField(
                    value = broadcastMessage,
                    onValueChange = { broadcastMessage = it },
                    label = "Directive Message",
                    placeholder = "Detailed guidance, precautionary steps, and dispatch protocols...",
                    singleLine = false,
                    maxLines = 4,
                    isError = isFormError && broadcastMessage.isBlank(),
                    errorMessage = "Message is required"
                )

                Text(
                    text = stringResource(R.string.targetVillageAudience),
                    style = MaterialTheme.typography.labelSmall,
                    color = VS_OnSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    val villageOptions = listOf("All Villages") + villages.map { it.name }
                    villageOptions.forEach { vName ->
                        val isSelected = selectedVillageName == vName
                        Surface(
                            onClick = { selectedVillageName = vName },
                            shape = PillShape,
                            color = if (isSelected) VS_PrimaryContainer else VS_Surface,
                            border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline)
                        ) {
                            Text(
                                text = vName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                ),
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                            )
                        }
                    }
                }
            }
        }
    }
}
