package com.vitalsense.app.feature.asha

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
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

@Composable
fun AshaHomeScreen(
    asha: AshaWorker,
    patients: List<Patient>,
    notices: List<BroadcastNotice>,
    onSelectProxyPatient: (Patient) -> Unit,
    onRegisterPatientClick: () -> Unit = {},
    onSendNoticeClick: () -> Unit = {},
    onOpenPatientChat: (Patient) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedRiskFilter by remember { mutableStateOf<SeverityLevel?>(null) }
    var selectedVillageFilter by remember { mutableStateOf("All") }

    val villageOptions = remember(patients) {
        listOf("All") + patients.map { it.villageName }.distinct()
    }

    val filteredPatients = remember(patients, selectedRiskFilter, selectedVillageFilter) {
        patients.filter { patient ->
            (selectedRiskFilter == null || patient.currentRiskLevel == selectedRiskFilter) &&
            (selectedVillageFilter == "All" || patient.villageName == selectedVillageFilter)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // 1. Header with Greeting & ASHA ID Card
        item {
            Column {
                Text(
                    text = "Namaste, ${asha.name}",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = "Assigned Villages: ${asha.assignedVillages.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        }

        // 2. ASHA Unique ID Card
        item {
            VitalSenseCard(
                backgroundColor = LavenderSecondary.copy(alpha = 0.4f),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "UNIQUE ASHA HELPER ID",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = TextSecondaryMuted
                        )
                        Text(
                            text = asha.ashaUniqueId,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "Share this ID with patients to add you as helper",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextSecondaryMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🆔", fontSize = 20.sp)
                    }
                }
            }
        }

        // 3. Quick Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VitalSenseButton(
                    text = "+ New Patient",
                    onClick = onRegisterPatientClick,
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.DARK
                )
                VitalSenseButton(
                    text = "📢 Send Notice",
                    onClick = onSendNoticeClick,
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.PRIMARY
                )
            }
        }

        // 4. Caseload Filter Controls (PRD §4.2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Caseload Filters",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimaryNearBlack
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedRiskFilter == null,
                            onClick = { selectedRiskFilter = null },
                            label = { Text("All Risks") },
                            shape = PillShape
                        )
                    }
                    items(SeverityLevel.values()) { level ->
                        FilterChip(
                            selected = selectedRiskFilter == level,
                            onClick = { selectedRiskFilter = level },
                            label = { Text(level.displayName) },
                            shape = PillShape
                        )
                    }
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(villageOptions) { village ->
                        FilterChip(
                            selected = selectedVillageFilter == village,
                            onClick = { selectedVillageFilter = village },
                            label = { Text(village) },
                            shape = PillShape
                        )
                    }
                }
            }
        }

        // 5. Section Header: Active Caseload & Proxy Access
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Patient Caseload (${filteredPatients.size} of ${patients.size})",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = "Tap 'Proxy' to act for patient",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryMuted
                )
            }
        }

        // 6. Patient Caseload Cards with Proxy & Chat Triggers
        items(filteredPatients) { patient ->
            VitalSenseCard(elevation = 2.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${patient.name} (${patient.gender}, ${patient.age}y)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimaryNearBlack
                            )
                            Text(
                                text = "Village: ${patient.villageName} · Ph: ${patient.phone}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryMuted
                            )
                        }
                        SeverityBadge(severity = patient.currentRiskLevel)
                    }

                    Text(
                        text = "Recent Condition: ${patient.lastCondition}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = TextPrimaryNearBlack
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { onOpenPatientChat(patient) }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextPrimaryNearBlack)
                                Text("Chat Thread", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimaryNearBlack)
                            }
                        }

                        Button(
                            onClick = { onSelectProxyPatient(patient) },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LimePrimary,
                                contentColor = TextPrimaryNearBlack
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "🤝 Act as Proxy",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // 7. Recent Broadcast Notices
        if (notices.isNotEmpty()) {
            item {
                Text(
                    text = "Village Health Advisories",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )
            }

            items(notices) { notice ->
                VitalSenseCard(
                    backgroundColor = if (notice.isUrgent) CoralAlert.copy(alpha = 0.15f) else SurfaceWhite
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = notice.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (notice.isUrgent) CoralAlert else TextPrimaryNearBlack
                            )
                        )
                        Text(
                            text = notice.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "From: ${notice.senderName} (${notice.targetVillage})",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        }
    }
}
