package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Referral
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReportScreen(
    viewModel: DoctorViewModel,
    referral: Referral,
    onBackClick: () -> Unit,
    onSubmitComplete: () -> Unit
) {
    var reportText by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submit Report", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VitalSenseCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Patient: ${referral.patientName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Requested by: ${referral.fromDoctorName} (${referral.fromDepartmentName})", style = MaterialTheme.typography.bodyMedium)
                    Text("Reason: ${referral.reason}", style = MaterialTheme.typography.bodyMedium)
                    if (referral.clinicalNotes.isNotBlank()) {
                        Text("Notes: ${referral.clinicalNotes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            OutlinedTextField(
                value = reportText,
                onValueChange = { reportText = it },
                label = { Text("Diagnostic / Service Report Findings") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.weight(1f))

            VitalSenseButton(
                text = "Submit Report",
                icon = { Icon(Icons.Default.Send, contentDescription = null) },
                onClick = {
                    viewModel.submitServiceReport(
                        referralId = referral.id,
                        reportText = reportText
                    )
                    onSubmitComplete()
                },
                enabled = reportText.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

