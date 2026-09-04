package com.vitalsense.app.feature.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.QueueEntry
import com.vitalsense.app.core.data.model.QueueEntryStatus
import com.vitalsense.app.core.ui.components.TabularStatusChip
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.AdaptiveScreenContainer
import com.vitalsense.app.core.ui.util.touchSpring

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueStatusScreen(
    entry: QueueEntry?,
    position: Int,
    estimatedWaitMinutes: Long,
    onBackClick: () -> Unit,
    onCancelEntry: (entryId: String) -> Unit,
    onJoinWalkIn: (() -> Unit)? = null
) {
    AdaptiveScreenContainer {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Live Visit Queue",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = VS_OnBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = VS_Background
        ) { paddingValues ->
            if (entry == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = VS_Surface),
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("📋", fontSize = 36.sp)
                            Text(
                                text = "No Active Queue Ticket",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "Check in to a scheduled appointment or join a doctor's walk-in queue to receive your token.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            if (onJoinWalkIn != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onJoinWalkIn,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                    modifier = Modifier.touchSpring()
                                ) {
                                    Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Get Instant Token for Today", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Main Ticket Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = VS_Surface),
                        border = BorderStroke(1.5.dp, VS_Primary.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Header Chip
                            val (chipBg, chipFg) = when (entry.status) {
                                QueueEntryStatus.WAITING -> Pair(NagarSevaStatusProgressBg, VS_Warning)
                                QueueEntryStatus.CALLED -> Pair(Color(0xFFEDE9FE), Color(0xFF7C5CFF))
                                QueueEntryStatus.IN_CONSULTATION -> Pair(NagarSevaStatusNormalBg, VS_Success)
                                QueueEntryStatus.COMPLETED -> Pair(Color(0xFFF1F5F9), VS_OnSurfaceVariant)
                                else -> Pair(NagarSevaStatusUrgentBg, VS_Error)
                            }

                            TabularStatusChip(
                                statusText = when (entry.status) {
                                    QueueEntryStatus.WAITING -> "WAITING IN LINE"
                                    QueueEntryStatus.CALLED -> "IT'S YOUR TURN! PROCEED TO DOCTOR"
                                    QueueEntryStatus.IN_CONSULTATION -> "CURRENTLY IN CONSULTATION"
                                    QueueEntryStatus.COMPLETED -> "VISIT COMPLETED"
                                    else -> entry.status.name
                                },
                                containerColor = chipBg,
                                textColor = chipFg
                            )

                            // Big Token Number
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "YOUR TOKEN NUMBER",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        letterSpacing = 1.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = VS_OnSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                if (entry.provisionalToken) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = VS_WarningContainer,
                                        border = BorderStroke(1.dp, VS_Error)
                                    ) {
                                        Text(
                                            text = "Confirming your position…",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = VS_Error,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "#${entry.tokenNumber}",
                                        style = MaterialTheme.typography.displayMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 64.sp
                                        ),
                                        color = VS_Primary
                                    )
                                }
                            }

                            HorizontalDivider(color = VS_Outline)

                            // Metrics: Position Ahead & Estimated Wait
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (position <= 0) "Next in Line" else "$position Ahead",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "Queue Position",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(1.dp)
                                        .background(VS_Outline)
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (position <= 0) "< 2 min" else "~$estimatedWaitMinutes min",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "Estimated Wait",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                            }

                            HorizontalDivider(color = VS_Outline)

                            // Doctor Information
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Attending Physician",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                    Text(
                                        text = "Dr. ${entry.doctorName}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = VS_SurfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🩺", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Cancel Ticket Action (only if WAITING)
                    if (entry.status == QueueEntryStatus.WAITING) {
                        OutlinedButton(
                            onClick = { onCancelEntry(entry.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .touchSpring(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, VS_Error.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                                tint = VS_Error,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Cancel Token",
                                color = VS_Error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


