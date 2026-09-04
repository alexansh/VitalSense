package com.vitalsense.app.feature.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.ConditionCategory
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.data.model.DoctorSpecialty
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun ConditionEntryScreen(
    patientId: String,
    patientName: String,
    villageId: String,
    villageName: String,
    category: ConditionCategory,
    onLogCondition: (ConditionRecord) -> Unit
) {
    var severity by remember { mutableStateOf(SeverityLevel.LOW) }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Text(
                text = "Log ${category.displayName} Symptom",
                style = MaterialTheme.typography.displayMedium,
                color = VS_OnBackground
            )
            Text(
                text = "Patient: $patientName · $villageName",
                style = MaterialTheme.typography.bodyMedium,
                color = VS_OnSurfaceVariant
            )
        }

        VitalSenseCard {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    text = "Select Severity Level:",
                    style = MaterialTheme.typography.labelMedium,
                    color = VS_OnSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    SeverityLevel.values().forEach { level ->
                        val isSelected = severity == level
                        Surface(
                            onClick = { severity = level },
                            shape = PillShape,
                            color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline),
                            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 6.dp)) {
                                Text(
                                    text = level.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                    )
                                )
                            }
                        }
                    }
                }

                VitalSenseTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Symptoms & Observations",
                    placeholder = "Describe how you are feeling, pain duration, fever level...",
                    singleLine = false,
                    maxLines = 4
                )

                VitalSenseButton(
                    text = "Submit to Doctor Queue ✓",
                    onClick = {
                        onLogCondition(
                            ConditionRecord(
                                id = UUID.randomUUID().toString(),
                                patientId = patientId,
                                patientName = patientName,
                                villageId = villageId,
                                villageName = villageName,
                                category = category,
                                severity = severity,
                                requestedDoctorType = DoctorSpecialty.GENERAL_PHYSICIAN,
                                notes = notes,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    },
                    style = ButtonStyle.PRIMARY,
                    enabled = notes.isNotBlank()
                )
            }
        }
    }
}