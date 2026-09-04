package com.vitalsense.app.feature.patient.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogSymptomDialog(
    patient: Patient,
    initialCategory: ConditionCategory = ConditionCategory.GENERAL_MEDICINE,
    onDismiss: () -> Unit,
    onSubmit: (ConditionRecord) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedSeverity by remember { mutableStateOf(SeverityLevel.MODERATE) }
    var customNotes by remember { mutableStateOf("") }
    val selectedSymptoms = remember { mutableStateListOf<String>() }

    val quickSymptoms = remember(selectedCategory) {
        when (selectedCategory) {
            ConditionCategory.GENERAL_MEDICINE -> listOf(
                "High Spiking Fever (>101°F)",
                "Persistent Cough",
                "Severe Headache",
                "Chest Congestion",
                "Shortness of Breath",
                "Stomach Ache",
                "Nausea / Vomiting",
                "Body Chills"
            )
            ConditionCategory.MATERNAL_HEALTH -> listOf(
                "Prenatal Routine Checkup",
                "Morning Sickness / Nausea",
                "Lower Back Pain",
                "Leg Cramps & Swelling",
                "Mild Fatigue / Anemia",
                "Blood Pressure Check",
                "Fetal Movement Query"
            )
            ConditionCategory.FITNESS -> listOf(
                "Joint Swelling / Arthritis",
                "Knee Pain during Walking",
                "Post-Trauma Muscle Strain",
                "Lower Back Stiffness",
                "Chronic Neck Pain",
                "Physical Mobility Limitation"
            )
            ConditionCategory.NUTRITION -> listOf(
                "Severe Weight Loss",
                "Loss of Appetite",
                "Nutritional Anemia (Low Hb)",
                "Childhood Malnutrition Query",
                "Vitamin Deficiency Symptoms",
                "Digestive Discomfort"
            )
            ConditionCategory.MENTAL_HEALTH -> listOf(
                "Severe Sleep Disruption",
                "Agricultural / Financial Anxiety",
                "Persistent Low Mood",
                "Overwhelming Stress",
                "Frequent Panic Episodes"
            )
            else -> listOf(
                "Acute Pain or Trauma",
                "Sudden Physical Weakness",
                "High Uncontrolled Fever",
                "Severe Dehydration"
            )
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = CardShape,
            color = VS_Surface,
            border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f)),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(selectedCategory.emoji, fontSize = 18.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Log Health Symptoms",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "For ${patient.name} (${patient.villageName})",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Text("✕", color = VS_OnSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // 1. Category Switcher Tabs
                Text(
                    text = "CATEGORY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ConditionCategory.values().filter { it != ConditionCategory.EMERGENCY }) { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) VS_Primary else VS_SurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) VS_PrimaryContainer else VS_Outline),
                            modifier = Modifier.clickable {
                                selectedCategory = cat
                                selectedSymptoms.clear()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(cat.emoji, fontSize = 12.sp)
                                Text(
                                    text = cat.displayName.split(" ").first(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) Color.White else VS_OnBackground,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }

                // 2. Quick Symptoms Flow Row
                Text(
                    text = "SELECT COMMON SYMPTOMS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickSymptoms.forEach { symptom ->
                        val isPicked = selectedSymptoms.contains(symptom)
                        Surface(
                            shape = PillShape,
                            color = if (isPicked) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = BorderStroke(
                                1.dp,
                                if (isPicked) VS_Primary else VS_Outline
                            ),
                            modifier = Modifier.clickable {
                                if (isPicked) selectedSymptoms.remove(symptom)
                                else selectedSymptoms.add(symptom)
                            }
                        ) {
                            Text(
                                text = (if (isPicked) "✓ " else "+ ") + symptom,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isPicked) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isPicked) VS_PrimaryContainer else VS_OnBackground
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // 3. Severity Level
                Text(
                    text = "SEVERITY LEVEL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    SeverityLevel.values().forEach { sev ->
                        val isSelected = sev == selectedSeverity
                        val sevColor = when (sev) {
                            SeverityLevel.LOW -> VS_Success
                            SeverityLevel.MODERATE -> VS_Warning
                            SeverityLevel.HIGH -> VS_Error.copy(alpha = 0.8f)
                            SeverityLevel.SEVERE -> VS_Error
                        }

                        Surface(
                            shape = PillShape,
                            color = if (isSelected) sevColor.copy(alpha = 0.25f) else VS_SurfaceVariant,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) sevColor else VS_Outline
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedSeverity = sev }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = sev.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) sevColor else VS_OnSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Custom Notes
                VitalSenseTextField(
                    value = customNotes,
                    onValueChange = { customNotes = it },
                    label = "Additional Notes (Optional)",
                    placeholder = "Describe duration, pain intensity, or other details..."
                )

                // 5. Submit Button
                Button(
                    onClick = {
                        val combinedNotes = buildString {
                            if (selectedSymptoms.isNotEmpty()) {
                                append("Reported Symptoms: ")
                                append(selectedSymptoms.joinToString(", "))
                            }
                            if (customNotes.isNotBlank()) {
                                if (selectedSymptoms.isNotEmpty()) append("\n")
                                append("Notes: ")
                                append(customNotes.trim())
                            }
                            if (isEmpty()) {
                                append("Routine ${selectedCategory.displayName} consultation logged.")
                            }
                        }

                        val requestedDoctorType = when (selectedCategory) {
                            ConditionCategory.MATERNAL_HEALTH -> DoctorSpecialty.GYNECOLOGIST
                            ConditionCategory.MENTAL_HEALTH -> DoctorSpecialty.PSYCHOLOGIST
                            ConditionCategory.FITNESS -> DoctorSpecialty.ORTHOPLASTIC_SURGEON
                            else -> DoctorSpecialty.GENERAL_PHYSICIAN
                        }

                        val calculatedSeverity = com.vitalsense.app.core.util.TriageEngine.evaluateSeverity(
                            category = selectedCategory,
                            symptoms = selectedSymptoms
                        )
                        val finalSeverity = maxOf(calculatedSeverity, selectedSeverity)

                        val record = ConditionRecord(
                            id = "cond_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}",
                            patientId = patient.id,
                            patientName = patient.name,
                            villageId = patient.villageId,
                            villageName = patient.villageName,
                            category = selectedCategory,
                            severity = finalSeverity,
                            requestedDoctorType = requestedDoctorType,
                            notes = combinedNotes,
                            timestamp = System.currentTimeMillis(),
                            ashaProxyLogged = false
                        )

                        onSubmit(record)
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VS_Primary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = "🚀 Submit to PHC Doctor Triage",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
