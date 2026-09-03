package com.vitalsense.app.feature.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun AdminBroadcastScreen(
    onSendBroadcast: (BroadcastNotice) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedTargetRole by remember { mutableStateOf("All Roles") }
    var isUrgent by remember { mutableStateOf(true) }
    var isSentSuccess by remember { mutableStateOf(false) }

    val roleOptions = listOf("All Roles", "ASHA Workers Only", "Doctors Only", "Patients Only")

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
                    text = "District CMO Broadcast Center",
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
                title = "District Outbreak Broadcaster",
                message = "Dispatch high-priority health advisories and epidemic containment orders across all user roles."
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
                        Text(text = "📢 District Advisory Dispatched!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Broadcast has been sent to $selectedTargetRole.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to Admin Dashboard", onClick = onBack, style = ButtonStyle.DARK)
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
                                text = "Compose Official Advisory",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Advisory Title") },
                            placeholder = { Text("e.g. High Alert: Sundarpura Waterborne Outbreak") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Directive / Order Message") },
                            placeholder = { Text("e.g. All ASHA workers to distribute ORS and boil water advisories immediately.") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = InputShape
                        )

                        Text(
                            text = "Target Audience",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(roleOptions) { role ->
                                FilterChip(
                                    selected = role == selectedTargetRole,
                                    onClick = { selectedTargetRole = role },
                                    label = { Text(role) },
                                    shape = PillShape
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mark as High-Priority Emergency Notice",
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
                    text = "Dispatch Official Directive",
                    onClick = {
                        if (title.isNotBlank() && message.isNotBlank()) {
                            val notice = BroadcastBroadcastNotice(
                                id = UUID.randomUUID().toString(),
                                
                                senderName = "District CMO (Rampur)",
                                senderRole = "District CMO",
                                title = title,
                                message = message,
                                targetVillage = selectedTargetRole,
                                isUrgent = isUrgent,
                                timestamp = System.currentTimeMillis()
                            )
                            onSendBroadcast(notice)
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
