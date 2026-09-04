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
import com.vitalsense.app.feature.doctor.components.DashboardAccordionItem
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
    var expandedDepartments by remember { mutableStateOf(false) }

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
            .background(GlumeBackground)
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
                    color = GlumeTextPrimary
                )
                Text(
                    text = "Surveillance Region: Rampur District, UP (Pop: $totalPopulation)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlumeTextSecondary
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
                    colors = CardDefaults.cardColors(containerColor = NagarSevaStatusProgressContainer),
                    border = BorderStroke(1.5.dp, NagarSevaStatusProgress.copy(alpha = 0.5f)),
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
                                        color = GlumeTextPrimary
                                    )
                                    Text(
                                        text = "Doctors have flagged low dispensary medicines",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GlumeTextSecondary
                                    )
                                }
                            }
                            Surface(shape = PillShape, color = NagarSevaStatusProgress) {
                                Text(
                                    text = "RESTOCK",
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
                                colors = CardDefaults.cardColors(containerColor = GlumeSurfaceCard),
                                border = BorderStroke(1.dp, GlumeBorder)
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
                                            color = GlumeTextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(reminder.timestamp)),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GlumeTextSecondary
                                        )
                                    }
                                    Text(
                                        text = reminder.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GlumeTextPrimary
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = onNavigateToDispensary,
                                            shape = PillShape,
                                            colors = ButtonDefaults.buttonColors(containerColor = NagarSevaPrimary),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "📦 Restock Now",
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
                                            border = BorderStroke(1.dp, GlumeBorder),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "✕ Dismiss Reminder",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
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
                    badgeColor = GlumeAlertCoral
                )
                GlumeStatCard(
                    label = stringResource(R.string.assignedVillages),
                    value = "${villages.size}",
                    icon = "🏡",
                    modifier = Modifier.weight(1f),
                    badgeText = "Villages",
                    badgeColor = GlumePrimaryPurple
                )
                GlumeStatCard(
                    label = stringResource(R.string.outbreakSurveillance),
                    value = "$outbreakCount",
                    icon = "⚠️",
                    modifier = Modifier.weight(1f),
                    badgeText = if (outbreakCount > 0) "Clusters" else "Safe",
                    badgeColor = if (outbreakCount > 0) GlumeAlertCoral else GlumeSuccessMint
                )
            }
        }

        // 3. Section: Village Disease Trend Heat Map Cards
        item {
            Text(
                text = "🗺️ ${stringResource(R.string.outbreakSurveillance)}",
                style = MaterialTheme.typography.headlineMedium,
                color = GlumeTextPrimary
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
                color = GlumeTextPrimary
            )
        }

        items(villages) { village ->
            val isHighRisk = village.highRiskCount > 0
            val isSelected = selectedMapVillage?.id == village.id
            val riskLevel = if (village.highRiskCount > 2) SeverityLevel.SEVERE else if (village.highRiskCount > 0) SeverityLevel.HIGH else if (village.activeCases > 5) SeverityLevel.MODERATE else SeverityLevel.LOW

            VitalSenseCard(
                backgroundColor = if (isSelected) GlumeSurfaceElevated else if (isHighRisk) GlumeAlertContainer else GlumeSurfaceCard,
                border = BorderStroke(1.dp, if (isSelected) GlumePrimaryPurple else if (isHighRisk) GlumeAlertCoral.copy(alpha = 0.4f) else GlumeBorder)
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
                                    color = GlumeTextPrimary
                                )
                                if (isSelected) {
                                    Surface(shape = PillShape, color = GlumePrimaryPurple) {
                                        Text(
                                            text = "PINNED ON MAP 📍",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Population: ${village.population} · Active Cases: ${village.activeCases} · High Risk: ${village.highRiskCount}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary
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
                            SeverityLevel.SEVERE -> GlumeAlertCoral
                            SeverityLevel.HIGH -> GlumeAlertCoral
                            SeverityLevel.MODERATE -> GlumeWarningAmber
                            SeverityLevel.LOW -> GlumeSuccessMint
                        },
                        trackColor = GlumeSurfaceElevated,
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
                    text = "Manage Dispensary",
                    onClick = onNavigateToDispensary,
                    style = ButtonStyle.SECONDARY,
                    modifier = Modifier.weight(1f)
                )
                VitalSenseButton(
                    text = "Disease Trends",
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
                    text = "Diagnostics & Labs",
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
                    text = "Hospital Operations & Care Desk",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = GlumeTextPrimary
                )
                Text(
                    text = "Real-time in-patient wards, surgical suites, tertiary referrals, and critical biomedical assets.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GlumeTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = GlumeSurfaceCard,
                        border = BorderStroke(1.dp, GlumeBorder),
                        onClick = onNavigateToIpdBeds
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🛏️", fontSize = 22.sp)
                            Text("IPD Wards & Beds", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = GlumeTextPrimary)
                            Text("Occupancy & Admission", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = GlumeTextSecondary)
                        }
                    }

                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = GlumeSurfaceCard,
                        border = BorderStroke(1.dp, GlumeBorder),
                        onClick = onNavigateToOtScheduler
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🔪", fontSize = 22.sp)
                            Text("OT Surgery Desk", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = GlumeTextPrimary)
                            Text("PAC & Surgeon Roster", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = GlumeTextSecondary)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = GlumeSurfaceCard,
                        border = BorderStroke(1.dp, GlumeBorder),
                        onClick = onNavigateToExternalReferrals
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("🏛️", fontSize = 22.sp)
                            Text("External Referrals", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = GlumeTextPrimary)
                            Text("AIIMS & Cashless Desk", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = GlumeTextSecondary)
                        }
                    }

                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = GlumeSurfaceCard,
                        border = BorderStroke(1.dp, GlumeBorder),
                        onClick = onNavigateToBioMedical
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text("⚡", fontSize = 22.sp)
                            Text("Bio-Medical Registry", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = GlumeTextPrimary)
                            Text("Oxygen & Equipment", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = GlumeTextSecondary)
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
                colors = CardDefaults.cardColors(containerColor = NagarSevaSurfaceLight),
                border = BorderStroke(1.5.dp, NagarSevaPrimary.copy(alpha = 0.3f)),
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
                            color = NagarSevaPrimary.copy(alpha = 0.12f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("📊", fontSize = 22.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Live Clinic Queue Oversight",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GlumeTextPrimary
                            )
                            Text(
                                text = "Monitor doctor queues, wait times and clinic load",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToQueueOversight,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NagarSevaPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Monitor", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                            color = GlumePrimaryPurpleLight.copy(alpha = 0.15f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🏥", fontSize = 22.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Facility Quality Metrics",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GlumeTextPrimary
                            )
                            Text(
                                text = "Monitor PHC/CHC infrastructure and feedback",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToFacilityQuality,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NagarSevaPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("View", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // 4.5 Hospital Departments Categorization (Admin View)
        item {
            DashboardAccordionItem(
                icon = "🏥",
                iconBackgroundColor = GlumePrimaryPurple,
                title = "Hospital Departments",
                subtitle = "Manage and view department doctors.",
                expanded = expandedDepartments,
                onToggle = { expandedDepartments = !expandedDepartments }
            ) {
                com.vitalsense.app.core.ui.components.DepartmentsSection(isAdmin = true)
            }
        }

        // 5. Broadcast Action Button (Single Full-Width Purple CTA)
        item {
            VitalSenseButton(
                text = "📢 Broadcast District-Wide Health Directive",
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
                    color = GlumeTextPrimary
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
                                color = GlumeTextPrimary
                            )
                            Surface(shape = PillShape, color = GlumeSuccessContainer) {
                                Text(
                                    text = "DISPATCHED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GlumeSuccessText
                                    ),
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = directive.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GlumeTextPrimary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Target: ${directive.targetVillage ?: "All Villages"} · Sender: ${directive.senderName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlumeTextSecondary,
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
                                    text = "✕ Dismiss",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NagarSevaPrimary
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
                        text = "Dispensary Low Stock Alerts",
                        style = MaterialTheme.typography.headlineMedium,
                        color = GlumeTextPrimary
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
                                    color = GlumeTextPrimary
                                )
                                Text(
                                    text = item.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GlumeTextSecondary
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
                                        color = GlumeAlertCoral
                                    )
                                )
                                Surface(shape = PillShape, color = GlumeAlertContainer) {
                                    Text(
                                        text = "LOW",
                                        style = MaterialTheme.typography.labelSmall.copy(color = GlumeAlertCoral, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Text("All stock is above reorder thresholds.", color = GlumeTextSecondary)
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
                    colors = ButtonDefaults.buttonColors(containerColor = GlumePrimaryPurple)
                ) {
                    Text("Broadcast Now", color = GlumeTextPrimary, style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBroadcastDialog = false },
                    shape = PillShape
                ) {
                    Text(stringResource(R.string.cancel), color = GlumeTextSecondary, style = MaterialTheme.typography.labelLarge)
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
                    text = "Target Village / Audience",
                    style = MaterialTheme.typography.labelSmall,
                    color = GlumeTextSecondary
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
                            color = if (isSelected) GlumePrimaryPurpleContainer else GlumeSurfaceCard,
                            border = if (isSelected) BorderStroke(1.5.dp, GlumePrimaryPurple) else BorderStroke(1.dp, GlumeBorder)
                        ) {
                            Text(
                                text = vName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GlumePrimaryPurpleLight else GlumeTextPrimary
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
