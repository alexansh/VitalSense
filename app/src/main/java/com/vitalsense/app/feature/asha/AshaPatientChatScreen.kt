package com.vitalsense.app.feature.asha

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.InlineHelpBanner
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

data class ChatMessage(
    val id: String,
    val senderName: String,
    val isAsha: Boolean,
    val text: String,
    val timeFormatted: String
)

@Composable
fun AshaPatientChatScreen(
    patient: Patient,
    ashaName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("1", patient.name, false, "Namaste Priya ji, I have a mild fever since morning.", "10:15 AM"),
                ChatMessage("2", ashaName, true, "Namaste ${patient.name.split(" ").first()}. Please drink clean water and rest. I have logged your symptom.", "10:18 AM")
            )
        )
    }
    var typedText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(16.dp)
    ) {
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
            Column {
                Text(
                    text = "Chat: ${patient.name}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = "Village: ${patient.villageName} · Ph: ${patient.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        InlineHelpBanner(
            title = "ASHA ↔ Patient Messaging Thread",
            message = "Offline message thread cached locally for direct guidance."
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val bg = if (msg.isAsha) LimePrimary.copy(alpha = 0.8f) else SurfaceWhite
                val alignment = if (msg.isAsha) Alignment.End else Alignment.Start

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = alignment
                ) {
                    VitalSenseCard(
                        modifier = Modifier.widthIn(max = 280.dp),
                        backgroundColor = bg
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = msg.senderName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimaryNearBlack
                            )
                            Text(
                                text = msg.timeFormatted,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryMuted
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = typedText,
                onValueChange = { typedText = it },
                placeholder = { Text("Type advisory message...") },
                modifier = Modifier.weight(1f),
                shape = InputShape
            )

            IconButton(
                onClick = {
                    if (typedText.isNotBlank()) {
                        messages = messages + ChatMessage(
                            id = java.util.UUID.randomUUID().toString(),
                            senderName = ashaName,
                            isAsha = true,
                            text = typedText,
                            timeFormatted = "Just now"
                        )
                        typedText = ""
                    }
                },
                modifier = Modifier.background(DarkCharcoal, androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = LimePrimary
                )
            }
        }
    }
}
