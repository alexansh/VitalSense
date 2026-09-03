package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.data.model.PatientHistory
import com.vitalsense.app.core.ui.components.CategoryChip
import com.vitalsense.app.core.ui.components.VitalSenseCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHistoryScreen(
    viewModel: DoctorViewModel,
    patientId: String,
    onBackClick: () -> Unit
) {
    val history by viewModel.getPatientFullHistory(patientId).collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (history == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    VitalSenseCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(history!!.patient.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("${history!!.patient.age} yrs | ${history!!.patient.gender} | ${history!!.patient.villageName}", style = MaterialTheme.typography.bodyMedium)
                            Text("Current Risk: ${history!!.patient.currentRiskLevel.displayName}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                if (history!!.conditions.isNotEmpty()) {
                    item { Text("Past Conditions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                    items(history!!.conditions) { condition ->
                        VitalSenseCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                                Text(dateFormat.format(Date(condition.timestamp)), style = MaterialTheme.typography.labelSmall)
                                Text(condition.category.displayName, fontWeight = FontWeight.Bold)
                                Text("Notes: ${condition.notes}")
                                if (condition.doctorResponse != null) {
                                    Text("Doctor: ${condition.doctorResponseDoctorName}")
                                    Text("Response: ${condition.doctorResponse}")
                                }
                            }
                        }
                    }
                }

                if (history!!.referrals.isNotEmpty()) {
                    item { Text("Referral History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                    items(history!!.referrals) { referral ->
                        VitalSenseCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                                Text(dateFormat.format(Date(referral.createdAt)), style = MaterialTheme.typography.labelSmall)
                                Text("${referral.fromDepartmentName} → ${referral.toDepartmentName}", fontWeight = FontWeight.Bold)
                                Text("Reason: ${referral.reason}")
                                if (referral.serviceReportText != null) {
                                    Text("Report: ${referral.serviceReportText}", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }

                if (history!!.prescriptions.isNotEmpty()) {
                    item { Text("Past Prescriptions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                    items(history!!.prescriptions) { rx ->
                        VitalSenseCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("${rx.dateFormatted} by ${rx.doctorName}", style = MaterialTheme.typography.labelSmall)
                                rx.medicines.forEach { med ->
                                    Text("• ${med.name} - ${med.dosage} (${med.frequency})", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
