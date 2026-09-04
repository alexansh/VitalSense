package com.vitalsense.app.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.DepartmentDataStore
import com.vitalsense.app.core.data.model.Department
import com.vitalsense.app.core.data.model.HardcodedDoctor
import com.vitalsense.app.core.ui.theme.VS_Surface
import com.vitalsense.app.core.ui.theme.VS_OnBackground
import com.vitalsense.app.core.ui.theme.VS_OnSurfaceVariant
import com.vitalsense.app.core.ui.theme.Spacing

@Composable
fun DepartmentsSection(
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    var expandedDeptId by remember { mutableStateOf<String?>(null) }
    var selectedDoctor by remember { mutableStateOf<HardcodedDoctor?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        DepartmentDataStore.departments.forEach { dept ->
            DepartmentItem(
                department = dept,
                isExpanded = expandedDeptId == dept.id,
                onToggle = { 
                    expandedDeptId = if (expandedDeptId == dept.id) null else dept.id 
                },
                isAdmin = isAdmin,
                onDoctorClick = { doc -> selectedDoctor = doc }
            )
        }
    }

    if (selectedDoctor != null && isAdmin) {
        AlertDialog(
            onDismissRequest = { selectedDoctor = null },
            title = { Text("Doctor Details", fontWeight = FontWeight.Bold, color = VS_OnBackground) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Name: ${selectedDoctor?.name}", fontWeight = FontWeight.Medium, color = VS_OnBackground)
                    Text("Specialization: ${selectedDoctor?.specialization}", color = VS_OnSurfaceVariant)
                    Text("Contact: ${selectedDoctor?.contact}", color = VS_OnSurfaceVariant)
                    Text("Experience: ${selectedDoctor?.experienceYears} years", color = VS_OnSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedDoctor = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun DepartmentItem(
    department: Department,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    isAdmin: Boolean,
    onDoctorClick: (HardcodedDoctor) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = VS_Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md)
        ) {
            Text(
                text = department.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VS_OnBackground
            )
            
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    department.doctors.forEach { doctor ->
                        DoctorItem(
                            doctor = doctor,
                            isAdmin = isAdmin,
                            onClick = { onDoctorClick(doctor) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoctorItem(
    doctor: HardcodedDoctor,
    isAdmin: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable(enabled = isAdmin) { onClick() }
            .padding(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = VS_OnBackground
            )
            if (isAdmin) {
                Text(
                    text = "Tap to view details",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
