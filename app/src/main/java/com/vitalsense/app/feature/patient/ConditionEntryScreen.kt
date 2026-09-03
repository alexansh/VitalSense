package com.vitalsense.app.feature.patient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun ConditionEntryScreen(
    patientId: String,
    patientName: String,
    villageId: String,
    villageName: String,
    initialCategory: ConditionCategory = ConditionCategory.GENERAL_MEDICINE,
    onLogCondition: (ConditionRecord) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedSeverity by remember { mutableStateOf(SeverityLevel.LOW) }
    var selectedDoctorType by remember { mutableStateOf(DoctorSpecialty.GENERAL_PHYSICIAN) }
    var notes by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

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
                    text = "Report Symptom / Health Need",
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
                title = "Easy Symptom Log",
                message = "Select how you feel. Your ASHA helper and doctors will receive this update automatically."
            )
        }

        if (submitted) {
            item {
                VitalSenseCard(backgroundColor = SoftMintSuccess) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "✓ Logged Successfully!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Your symptom has been saved locally and sent to your doctor.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to Home", onClick = onBack, style = ButtonStyle.DARK)
                    }
                }
            }
        } else {
            // Category Selection
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "1. Select Symptom Category",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(ConditionCategory.values()) { category ->
                                FilterChip(
                                    selected = category == selectedCategory,
                                    onClick = { selectedCategory = category },
                                    label = { Text("${category.emoji} ${category.displayName}") },
                                    leadingIcon = if (category == selectedCategory) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null,
                                    shape = PillShape
                                )
                            }
                        }
                    }
                }
            }

            // Severity Selection
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "2. How severe is the discomfort?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SeverityLevel.values().forEach { level ->
                                FilterChip(
                                    selected = level == selectedSeverity,
                                    onClick = { selectedSeverity = level },
                                    label = { Text(level.displayName) },
                                    modifier = Modifier.weight(1f),
                                    shape = PillShape
                                )
                            }
                        }
                    }
                }
            }

            // Doctor Type Selection
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "3. Preferred Specialist",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(DoctorSpecialty.values()) { specialty ->
                                FilterChip(
                                    selected = specialty == selectedDoctorType,
                                    onClick = { selectedDoctorType = specialty },
                                    label = { Text(specialty.displayName) },
                                    shape = PillShape
                                )
                            }
                        }
                    }
                }
            }

            // Optional Notes
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "4. Additional Details (Optional)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            placeholder = { Text("e.g. High fever since yesterday evening...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = InputShape
                        )
                    }
                }
            }

            // Submit Button
            item {
                VitalSenseButton(
                    text = "Submit Health Log",
                    onClick = {
                        val record = ConditionRecord(
                            id = UUID.randomUUID().toString(),
                            patientId = patientId,
                            patientName = patientName,
                            villageId = villageId,
                            villageName = villageName,
                            category = selectedCategory,
                            severity = selectedSeverity,
                            requestedDoctorType = selectedDoctorType,
                            notes = notes,
                            timestamp = System.currentTimeMillis()
                        )
                        onLogCondition(record)
                        submitted = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.PRIMARY
                )
            }
        }
    }
}