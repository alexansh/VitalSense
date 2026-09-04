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
import com.vitalsense.app.core.data.model.DailyRound
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LogDailyRoundDialog(
    ashaWorkerId: String,
    defaultVillage: String = "Sundarpura",
    onDismiss: () -> Unit,
    onSaveRound: (DailyRound) -> Unit
) {
    var householdName by remember { mutableStateOf("") }
    var personName by remember { mutableStateOf("") }
    var villageName by remember { mutableStateOf(defaultVillage) }
    var purpose by remember { mutableStateOf("Routine Household Health Round") }
    var notes by remember { mutableStateOf("") }

    var isPregnancyChecked by remember { mutableStateOf(false) }
    var isChildHealthChecked by remember { mutableStateOf(true) }
    var isImmunizationChecked by remember { mutableStateOf(false) }
    var isMedicineGiven by remember { mutableStateOf(false) }
    var isCounsellingDone by remember { mutableStateOf(true) }

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
                                Text("🏡", fontSize = 16.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Log Village Round Visit",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "Door-to-Door Health Record",
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

                VitalSenseTextField(
                    value = householdName,
                    onValueChange = { householdName = it ; errorMessage = "" },
                    label = "Household / Family Name",
                    placeholder = "e.g. Verma Family (House #42)"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = personName,
                            onValueChange = { personName = it ; errorMessage = "" },
                            label = "Person Examined",
                            placeholder = "e.g. Sunita Verma"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        VitalSenseTextField(
                            value = villageName,
                            onValueChange = { villageName = it },
                            label = "Village",
                            placeholder = "e.g. Sundarpura"
                        )
                    }
                }

                VitalSenseTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = "Visit Purpose",
                    placeholder = "e.g. Maternal Checkup, Postnatal Visit"
                )

                // Checkup items checklist
                Text(
                    text = "SERVICES PROVIDED DURING VISIT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = VS_OnSurfaceVariant
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = isPregnancyChecked,
                            onClick = { isPregnancyChecked = !isPregnancyChecked },
                            label = { Text("🤰 Maternal / ANC", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = isChildHealthChecked,
                            onClick = { isChildHealthChecked = !isChildHealthChecked },
                            label = { Text("👶 Child Health", fontSize = 11.sp) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = isImmunizationChecked,
                            onClick = { isImmunizationChecked = !isImmunizationChecked },
                            label = { Text("💉 Immunization", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = isMedicineGiven,
                            onClick = { isMedicineGiven = !isMedicineGiven },
                            label = { Text("💊 Medicine / IFA", fontSize = 11.sp) }
                        )
                    }
                }

                VitalSenseTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Observations & Action Taken",
                    placeholder = "e.g. Vitals stable, advised ORS and IFA supplementation"
                )

                Spacer(modifier = Modifier.height(Spacing.xxs))

                Button(
                    onClick = {
                        if (householdName.isBlank()) {
                            errorMessage = "Please enter household name."
                            return@Button
                        }
                        if (personName.isBlank()) {
                            errorMessage = "Please enter person examined."
                            return@Button
                        }

                        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        val round = DailyRound(
                            id = "round_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}",
                            dateFormatted = dateStr,
                            villageName = villageName.trim(),
                            householdName = householdName.trim(),
                            personName = personName.trim(),
                            ashaWorkerId = ashaWorkerId,
                            purpose = purpose.trim(),
                            isPregnancyChecked = isPregnancyChecked,
                            isChildHealthChecked = isChildHealthChecked,
                            isImmunizationChecked = isImmunizationChecked,
                            isMedicineGiven = isMedicineGiven,
                            isCounsellingDone = isCounsellingDone,
                            notes = notes.trim().ifEmpty { "Vitals checked, healthy and stable." },
                            status = "Completed"
                        )

                        onSaveRound(round)
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
                        text = "✓ Save Village Round Visit",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
