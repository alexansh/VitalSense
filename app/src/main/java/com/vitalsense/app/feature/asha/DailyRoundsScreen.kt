package com.vitalsense.app.feature.asha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.DailyRound
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.asha.components.LogDailyRoundDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRoundsScreen(
    rounds: List<DailyRound>,
    onBackClick: () -> Unit,
    onSaveRound: (DailyRound) -> Unit = {}
) {
    var showLogRoundDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Village Rounds") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VS_SurfaceVariant,
                    titleContentColor = VS_OnBackground,
                    navigationIconContentColor = VS_OnBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showLogRoundDialog = true },
                containerColor = VS_Primary,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Log Round")
                    Text("Log Visit", fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = VS_Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            contentPadding = PaddingValues(top = Spacing.sm, bottom = Spacing.xxl)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Village Rounds & Door-to-Door Visits",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                }
            }

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

            if (rounds.isEmpty()) {
                item {
                    Text(
                        text = "No village rounds logged yet. Tap '+ Log Visit' to record door-to-door checkups.",
                        color = VS_OnSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(rounds) { round ->
                    VitalSenseCard(
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = round.householdName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = round.dateFormatted,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            Text(
                                text = "Person: ${round.personName} | Village: ${round.villageName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                            
                            HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = 4.dp))
                            
                            Text(
                                text = "Purpose: ${round.purpose}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = VS_PrimaryContainer
                            )
                            Text(
                                text = "Notes: ${round.notes}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnBackground
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (round.isPregnancyChecked) {
                                    Surface(shape = PillShape, color = VS_PrimaryContainer) {
                                        Text("🤰 Maternal", fontSize = 10.sp, color = VS_PrimaryContainer, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                if (round.isChildHealthChecked) {
                                    Surface(shape = PillShape, color = VS_SuccessContainer) {
                                        Text("👶 Child", fontSize = 10.sp, color = VS_Success, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                if (round.isImmunizationChecked) {
                                    Surface(shape = PillShape, color = VS_WarningContainer) {
                                        Text("💉 Vaccine", fontSize = 10.sp, color = VS_Warning, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showLogRoundDialog) {
        LogDailyRoundDialog(
            ashaWorkerId = rounds.firstOrNull()?.ashaWorkerId ?: "asha_priya",
            onDismiss = { showLogRoundDialog = false },
            onSaveRound = { newRound ->
                onSaveRound(newRound)
                showLogRoundDialog = false
                successMessage = "✓ Visit for ${newRound.householdName} saved!"
            }
        )
    }
}

