package com.vitalsense.app.feature.doctor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Doctor
import com.vitalsense.app.core.data.model.PrescribedMedicine
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun PrescriptionCreatorScreen(
    patientId: String,
    patientName: String,
    doctor: Doctor,
    onIssuePrescription: (List<PrescribedMedicine>, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var instructions by remember { mutableStateOf("Take medicines after food. Drink plenty of boiled water.") }
    var medName by remember { mutableStateOf("Paracetamol") }
    var medDosage by remember { mutableStateOf("500mg 1x daily") }
    var medQty by remember { mutableStateOf("10 tablets") }

    var medicineList by remember {
        mutableStateOf(
            listOf(
                PrescribedMedicine("Paracetamol", "500mg", "1-0-1", "5 days", 10),
                PrescribedMedicine("ORS Sachet", "1 packet in 1L water", "1-0-0", "5 days", 5)
            )
        )
    }

    var isSubmitted by remember { mutableStateOf(false) }

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
                    text = "Issue Digital Prescription",
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
                title = "Prescription Composer",
                message = "Issue a timestamped digital prescription to $patientName. It will be cached locally on the patient's device."
            )
        }

        if (isSubmitted) {
            item {
                VitalSenseCard(backgroundColor = SoftMintSuccess) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "✓ Prescription Issued!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Digital prescription saved to Room database for $patientName.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to Doctor Dashboard", onClick = onBack, style = ButtonStyle.DARK)
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
                            Icon(Icons.Default.Medication, contentDescription = null, tint = DarkCharcoal)
                            Text(
                                text = "Prescription for: $patientName",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                        }

                        Text(
                            text = "Prescribing Doctor: ${doctor.name} (${doctor.specialty.displayName})",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )

                        HorizontalDivider(color = TextSecondaryMuted.copy(alpha = 0.2f))

                        Text(
                            text = "Added Medicines (${medicineList.size})",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        medicineList.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• ${item.name} (${item.dosage})",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextPrimaryNearBlack
                                )
                                Text(
                                    text = "${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryMuted
                                )
                            }
                        }

                        HorizontalDivider(color = TextSecondaryMuted.copy(alpha = 0.2f))

                        Text(
                            text = "Add Another Medicine Item",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        OutlinedTextField(
                            value = medName,
                            onValueChange = { medName = it },
                            label = { Text("Medicine Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = medDosage,
                                onValueChange = { medDosage = it },
                                label = { Text("Dosage") },
                                modifier = Modifier.weight(1f),
                                shape = InputShape
                            )
                            OutlinedTextField(
                                value = medQty,
                                onValueChange = { medQty = it },
                                label = { Text("Quantity") },
                                modifier = Modifier.weight(1f),
                                shape = InputShape
                            )
                        }

                        VitalSenseButton(
                            text = "+ Add Item",
                            onClick = {
                                if (medName.isNotBlank()) {
                                    medicineList = medicineList + PrescribedMedicine(
                                        name = medName,
                                        dosage = medDosage,
                                        frequency = "As prescribed", // Default placeholder
                                        duration = "5 days", // Default placeholder
                                        quantity = medQty.toIntOrNull() ?: 1
                                    )
                                    medName = ""
                                }
                            },
                            style = ButtonStyle.SECONDARY
                        )

                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("General Advice & Dosage Instructions") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = InputShape
                        )
                    }
                }
            }

            item {
                VitalSenseButton(
                    text = "Sign & Issue Digital Prescription",
                    onClick = {
                        if (medicineList.isNotEmpty()) {
                            onIssuePrescription(medicineList, instructions)
                            isSubmitted = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.PRIMARY
                )
            }
        }
    }
}
