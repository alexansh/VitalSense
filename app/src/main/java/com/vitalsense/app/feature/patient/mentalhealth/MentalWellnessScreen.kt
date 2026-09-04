package com.vitalsense.app.feature.patient.mentalhealth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@Composable
fun MentalWellnessScreen(
    patient: Patient,
    onLogMood: (String, SeverityLevel) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var moodLogged by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Header
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) {
                    Text("← Back", color = VS_OnBackground, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = "Mental Wellness",
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                color = VS_OnBackground
            )
            Text(
                text = "Take a moment for yourself, ${patient.name.split(" ").first()}.",
                style = MaterialTheme.typography.bodyMedium,
                color = VS_OnSurfaceVariant
            )
        }

        // Mood Check-in
        item {
            VitalSenseCard(backgroundColor = VS_PrimaryContainer.copy(alpha = 0.3f)) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "How are you feeling today?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MoodSelector(emoji = "😊", label = "Good", isSelected = selectedMood == "Good") {
                            selectedMood = "Good"
                        }
                        MoodSelector(emoji = "😐", label = "Okay", isSelected = selectedMood == "Okay") {
                            selectedMood = "Okay"
                        }
                        MoodSelector(emoji = "😟", label = "Stressed", isSelected = selectedMood == "Stressed") {
                            selectedMood = "Stressed"
                        }
                        MoodSelector(emoji = "😫", label = "Anxious", isSelected = selectedMood == "Anxious") {
                            selectedMood = "Anxious"
                        }
                    }

                    if (selectedMood != null && !moodLogged) {
                        VitalSenseButton(
                            text = "Save Check-in",
                            onClick = {
                                val severity = when (selectedMood) {
                                    "Good" -> SeverityLevel.LOW
                                    "Okay" -> SeverityLevel.MODERATE
                                    "Stressed" -> SeverityLevel.HIGH
                                    "Anxious" -> SeverityLevel.SEVERE
                                    else -> SeverityLevel.LOW
                                }
                                onLogMood("Feeling $selectedMood", severity)
                                moodLogged = true
                            }
                        )
                    }

                    if (moodLogged) {
                        Text(
                            text = "✅ Check-in saved. A doctor or ASHA worker will check on you if needed.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Breathing Exercise
        item {
            VitalSenseCard(backgroundColor = VS_Surface) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🌬️ Guided Breathing",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Breathe in for 4 seconds, hold for 4, exhale for 4.",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(VS_PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tap to Start", color = VS_Surface, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MoodSelector(emoji: String, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isSelected) VS_Primary else VS_Surface),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
