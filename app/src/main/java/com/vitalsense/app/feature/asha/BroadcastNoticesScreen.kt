package com.vitalsense.app.feature.asha

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.BroadcastNotice
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun BroadcastNoticesScreen(
    ashaId: String,
    ashaName: String,
    onSendNotice: (BroadcastNotice) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var targetVillage by remember { mutableStateOf("All Assigned Villages") }
    var isUrgent by remember { mutableStateOf(false) }
    var isSentSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimaryNearBlack
                    )
                }
                Text(
                    text = "Broadcast Notice to Caseload",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextPrimaryNearBlack
                )
            }
        }

        item {
            InlineHelpBanner(
                title = "Health Advisory Broadcaster",
                message = "Send health alerts, vaccination drive notices, or general advisories to all patients in your assigned caseload."
            )
        }

        if (isSentSuccess) {
            item {
                VitalSenseCard(backgroundColor = SoftMintSuccess) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📢 Notice Broadcasted!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Your advisory has been dispatched to all patients in $targetVillage.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to ASHA Dashboard", onClick = onBack, style = ButtonStyle.DARK)
                    }
                }
            }
        } else {
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = DarkCharcoal)
                            Text(
                                text = "Notice Details",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Advisory Title") },
                            placeholder = { Text("e.g. Polio Vaccination Drive") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Message Body") },
                            placeholder = { Text("e.g. Free immunization camp at Anganwadi center tomorrow 9 AM.") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = targetVillage,
                            onValueChange = { targetVillage = it },
                            label = { Text("Target Village / Area") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mark as Urgent Outbreak Advisory",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TextPrimaryNearBlack
                            )
                            Switch(
                                checked = isUrgent,
                                onCheckedChange = { isUrgent = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = CoralAlert)
                            )
                        }
                    }
                }
            }

            item {
                VitalSenseButton(
                    text = "Dispatch Broadcast Notice",
                    onClick = {
                        if (title.isNotBlank() && message.isNotBlank()) {
                            val notice = BroadcastBroadcastNotice(
                                id = UUID.randomUUID().toString(),
                                senderId = ashaId,
                                senderName = ashaName,
                                senderRole = com.vitalsense.app.core.data.model.UserRole.ASHA,
                                title = title,
                                message = message,
                                targetVillage = targetVillage,
                                isUrgent = isUrgent,
                                timestamp = System.currentTimeMillis()
                            )
                            onSendNotice(notice)
                            isSentSuccess = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.PRIMARY
                )
            }
        }
    }
}
