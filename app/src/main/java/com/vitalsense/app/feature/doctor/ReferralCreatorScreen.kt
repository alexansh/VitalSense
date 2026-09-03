package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralCreatorScreen(
    viewModel: DoctorViewModel,
    patientId: String,
    patientName: String,
    caseId: String,
    onBackClick: () -> Unit,
    onReferralCreated: () -> Unit
) {
    val activeDoctor by viewModel.activeDoctor.collectAsStateWithLifecycle()
    val departments by viewModel.departments.collectAsStateWithLifecycle()

    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
    var referralType by remember { mutableStateOf(ReferralType.CLINICAL) }
    var urgency by remember { mutableStateOf(ReferralUrgency.ROUTINE) }
    var reason by remember { mutableStateOf("") }
    var clinicalNotes by remember { mutableStateOf("") }
    var showDepartmentDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Referral", fontWeight = FontWeight.Bold) },
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
            Text("Patient: $patientName", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            // Department Selection
            VitalSenseCard(modifier = Modifier.clickable { showDepartmentDialog = true }) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Target Department", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = selectedDepartment?.name ?: "Select Department",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text("▼", color = MaterialTheme.colorScheme.primary)
                }
            }

            // Referral Type Selection
            Text("Referral Type", style = MaterialTheme.typography.labelLarge)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReferralType.entries.forEach { type ->
                    FilterChip(
                        selected = referralType == type,
                        onClick = { referralType = type },
                        label = { Text(type.displayName) },
                        leadingIcon = { Text(type.emoji) }
                    )
                }
            }

            // Urgency Selection
            Text("Urgency", style = MaterialTheme.typography.labelLarge)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReferralUrgency.entries.forEach { urg ->
                    FilterChip(
                        selected = urgency == urg,
                        onClick = { urgency = urg },
                        label = { Text(urg.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(urg.colorHex),
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            // Text Inputs
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Primary Reason") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = clinicalNotes,
                onValueChange = { clinicalNotes = it },
                label = { Text("Clinical Notes / History") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.weight(1f))

            VitalSenseButton(
                text = "Send Referral",
                icon = Icons.Default.Send,
                onClick = {
                    selectedDepartment?.let { dept ->
                        viewModel.referCase(
                            caseId = caseId,
                            patientId = patientId,
                            patientName = patientName,
                            targetDepartmentId = dept.id,
                            targetDepartmentName = dept.name,
                            referralType = referralType,
                            urgency = urgency,
                            reason = reason,
                            clinicalNotes = clinicalNotes
                        )
                        onReferralCreated()
                    }
                },
                enabled = selectedDepartment != null && reason.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showDepartmentDialog) {
        AlertDialog(
            onDismissRequest = { showDepartmentDialog = false },
            title = { Text("Select Department") },
            text = {
                LazyColumn {
                    items(departments.filter { it.id != activeDoctor.departmentId }) { dept ->
                        ListItem(
                            headlineContent = { Text(dept.name) },
                            supportingContent = { Text(dept.type.displayName) },
                            leadingContent = { Text(dept.emoji) },
                            modifier = Modifier.clickable {
                                selectedDepartment = dept
                                // Auto-select type based on department type
                                referralType = if (dept.type == DepartmentType.SERVICE) ReferralType.SERVICE else ReferralType.CLINICAL
                                showDepartmentDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDepartmentDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}



