package com.vitalsense.app.feature.asha.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun RegisterPatientDialog(
    asha: AshaWorker,
    onDismiss: () -> Unit,
    onRegister: (Patient) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Female") }
    var village by remember { mutableStateOf(asha.assignedVillages.firstOrNull() ?: "Sundarpura") }
    var phone by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var initialCondition by remember { mutableStateOf("Routine Checkup / General Health") }
    var selectedRiskLevel by remember { mutableStateOf(SeverityLevel.LOW) }

    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = VS_Surface,
            border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.6f)),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.sm)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
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
                                Text("➕", fontSize = 16.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Register New Villager",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "ASHA: ${asha.name} (${asha.ashaUniqueId})",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Text("✕", color = VS_OnSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall.copy(color = VS_Error, fontWeight = FontWeight.Bold)
                    )
                }

                // Full Name
                VitalSenseTextField(
                    value = name,
                    onValueChange = { name = it ; errorMessage = "" },
                    label = "Villager Full Name",
                    placeholder = "e.g. Meera Devi"
                )

                // Age & Gender Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = ageText,
                            onValueChange = { ageText = it.filter { ch -> ch.isDigit() } },
                            label = "Age (Years)",
                            placeholder = "e.g. 29"
                        )
                    }

                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "GENDER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = VS_OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Female", "Male").forEach { g ->
                                val isSelected = gender == g
                                Surface(
                                    shape = PillShape,
                                    color = if (isSelected) VS_Primary else VS_SurfaceVariant,
                                    border = BorderStroke(1.dp, if (isSelected) VS_PrimaryContainer else VS_Outline),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { gender = g }
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                        Text(
                                            text = g,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) Color.White else VS_OnBackground,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Phone & Emergency Contact
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Mobile Phone",
                            placeholder = "98765 00000"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = emergencyContact,
                            onValueChange = { emergencyContact = it },
                            label = "Emergency Phone",
                            placeholder = "Family / ASHA"
                        )
                    }
                }

                // Village Selector
                Text(
                    text = "ASSIGNED VILLAGE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    asha.assignedVillages.forEach { v ->
                        val isSelected = village == v
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) VS_Primary else VS_SurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) VS_PrimaryContainer else VS_Outline),
                            modifier = Modifier.clickable { village = v }
                        ) {
                            Text(
                                text = "🏡 $v",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White else VS_OnBackground,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Initial Condition
                VitalSenseTextField(
                    value = initialCondition,
                    onValueChange = { initialCondition = it },
                    label = "Initial Health Condition / Reason",
                    placeholder = "e.g. Prenatal checkup, Hypertension"
                )

                // Risk Level
                Text(
                    text = "INITIAL RISK LEVEL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SeverityLevel.values().forEach { sev ->
                        val isSelected = sev == selectedRiskLevel
                        val sevColor = when (sev) {
                            SeverityLevel.LOW -> VS_Success
                            SeverityLevel.MODERATE -> VS_Warning
                            SeverityLevel.HIGH -> VS_Error.copy(alpha = 0.8f)
                            SeverityLevel.SEVERE -> VS_Error
                        }
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) sevColor.copy(alpha = 0.25f) else VS_SurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) sevColor else VS_Outline),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedRiskLevel = sev }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 6.dp)) {
                                Text(
                                    text = sev.displayName.split(" ").first(),
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

                Spacer(modifier = Modifier.height(Spacing.xxs))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Please enter patient name."
                            return@Button
                        }
                        val ageInt = ageText.toIntOrNull() ?: 25
                        val finalPhone = if (phone.isNotBlank()) phone else "+91 98765 ${UUID.randomUUID().toString().take(5)}"
                        val finalEmergency = if (emergencyContact.isNotBlank()) emergencyContact else asha.phone

                        val newPatient = Patient(
                            id = "pat_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}",
                            name = name.trim(),
                            age = ageInt,
                            gender = gender,
                            phone = finalPhone,
                            villageId = "vil_${village.lowercase().replace(" ", "_")}",
                            villageName = village,
                            ashaWorkerId = asha.id,
                            ashaWorkerName = asha.name,
                            currentRiskLevel = selectedRiskLevel,
                            lastCondition = initialCondition.trim().ifEmpty { "Routine Health Checkup" },
                            lastVisitDate = "Today",
                            nextAppointmentDate = "Scheduled via ASHA",
                            emergencyContact = finalEmergency
                        )

                        onRegister(newPatient)
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
                        text = "✓ Register Villager into Caseload",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
