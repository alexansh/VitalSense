package com.vitalsense.app.feature.asha

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun PatientRegistrationScreen(
    ashaId: String,
    ashaName: String,
    onSavePatient: (Patient) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Female") }
    var phone by remember { mutableStateOf("") }
    var villageName by remember { mutableStateOf("Sundarpura") }
    var emergencyContact by remember { mutableStateOf("") }
    var initialCondition by remember { mutableStateOf("Routine Checkup") }
    var initialRisk by remember { mutableStateOf(SeverityLevel.LOW) }
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
                    text = "New Patient Registration",
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
                title = "ASHA Registration Form",
                message = "Register a new villager to your active caseload. Records persist locally in Room database."
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
                        Text(text = "✓ Patient Registered!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = "$name has been added to your ASHA caseload.", style = MaterialTheme.typography.bodySmall)
                        VitalSenseButton(text = "Back to Caseload", onClick = onBack, style = ButtonStyle.DARK)
                    }
                }
            }
        } else {
            item {
                VitalSenseCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Patient Personal Details",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Age (Years)") },
                                modifier = Modifier.weight(1f),
                                shape = InputShape
                            )
                            OutlinedTextField(
                                value = gender,
                                onValueChange = { gender = it },
                                label = { Text("Gender") },
                                modifier = Modifier.weight(1f),
                                shape = InputShape
                            )
                        }

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = villageName,
                            onValueChange = { villageName = it },
                            label = { Text("Village Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = emergencyContact,
                            onValueChange = { emergencyContact = it },
                            label = { Text("Emergency Contact Number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )

                        OutlinedTextField(
                            value = initialCondition,
                            onValueChange = { initialCondition = it },
                            label = { Text("Initial Condition / Symptom") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputShape
                        )
                    }
                }
            }

            item {
                VitalSenseButton(
                    text = "Save Patient to Caseload",
                    onClick = {
                        if (name.isNotBlank()) {
                            val newPatient = Patient(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                age = age.toIntOrNull() ?: 30,
                                gender = gender,
                                phone = if (phone.isBlank()) "9876543210" else phone,
                                villageId = if (villageName.contains("Kalyan", true)) "v2" else "v1",
                                villageName = villageName,
                                ashaWorkerId = ashaId,
                                ashaWorkerName = ashaName,
                                currentRiskLevel = initialRisk,
                                lastCondition = initialCondition,
                                lastVisitDate = "Today",
                                nextAppointmentDate = null,
                                emergencyContact = if (emergencyContact.isBlank()) "112" else emergencyContact
                            )
                            onSavePatient(newPatient)
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