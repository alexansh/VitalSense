package com.vitalsense.app.feature.admin

import androidx.compose.foundation.background
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
import com.vitalsense.app.core.data.model.BroadcastNotice
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun AdminHomeScreen(
    villages: List<Village>,
    notices: List<BroadcastNotice>,
    onSendBroadcast: (String, String, String) -> Unit,
    onViewVillages: () -> Unit = {},
    onViewOutbreakGrid: () -> Unit = {},
    onViewBroadcast: () -> Unit = {},
    onViewAccounts: () -> Unit = {},
    onViewDepartments: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // 1. Header
        item {
            Column {
                Text(
                    text = "District Health CMO Dashboard",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = "Sub-District: Rampur Block · District HQ",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        }

        // 2. Inline Banner
        item {
            InlineHelpBanner(
                title = "Chief Medical Officer Control Room",
                message = "Monitor village epidemic risk, dispatch emergency notices, and manage healthcare personnel."
            )
        }

        // 3. Quick Action Buttons for Admin Services
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VitalSenseButton(
                        text = "🗺️ Outbreak Map Grid",
                        onClick = onViewOutbreakGrid,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.DARK
                    )
                    VitalSenseButton(
                        text = "🏛️ Village Directory",
                        onClick = onViewVillages,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.PRIMARY
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VitalSenseButton(
                        text = "📢 District Broadcast",
                        onClick = onViewBroadcast,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                    VitalSenseButton(
                        text = "🛡️ Staff Credentials",
                        onClick = onViewAccounts,
                        modifier = Modifier.weight(1f),
                        style = ButtonStyle.SECONDARY
                    )
                }

                VitalSenseButton(
                    text = "🏥 Manage Departments",
                    onClick = onViewDepartments,
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.DARK
                )
            }
        }

        // 4. Outbreak Summary Card
        item {
            VitalSenseCard(
                backgroundColor = CoralAlert.copy(alpha = 0.2f),
                elevation = 3.dp,
                onClick = onViewOutbreakGrid
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔴 ACTIVE OUTBREAK ALERT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CoralAlert
                            )
                        )
                        Text(
                            text = "View Outbreak Grid →",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                    }

                    Text(
                        text = "Sundarpura Village Cluster: High Fever Spike",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimaryNearBlack
                    )

                    Text(
                        text = "6 severe fever cases logged today. ASHA helper Priya Devi dispatches ORS & water boiling advisories.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimaryNearBlack
                    )
                }
            }
        }

        // 5. Active Village Clusters List
        item {
            Text(
                text = "Registered Villages (${villages.size})",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = TextPrimaryNearBlack
            )
        }

        items(villages) { village ->
            VitalSenseCard(elevation = 2.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = village.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "Population: ${village.population} · District: ${village.district}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }

                    Surface(
                        shape = PillShape,
                        color = if (false) CoralAlert else SoftMintSuccess
                    ) {
                        Text(
                            text = if (false) "ALERT 🔴" else "NORMAL 🟢",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (false) SurfaceWhite else TextPrimaryNearBlack
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 6. Active Broadcast Directives
        if (notices.isNotEmpty()) {
            item {
                Text(
                    text = "Active District Directives",
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
                            text = "Issued by: ${notice.senderName} (${notice.targetVillage})",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        }
    }
}

