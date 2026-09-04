package com.vitalsense.app.feature.ipd

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.IpdBed
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun IpdBedTrackerScreen(
    beds: List<IpdBed>,
    patients: List<Patient> = emptyList(),
    onBackClick: () -> Unit,
    onSaveBed: (IpdBed) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedWard by remember { mutableStateOf("All") }
    var showAdmitDialogForBed by remember { mutableStateOf<IpdBed?>(null) }

    val wards = listOf("All", "Male Medical Ward", "Female & Maternal Ward", "Emergency Trauma Ward", "Intensive Care Unit (ICU)")

    val filteredBeds = remember(beds, selectedWard) {
        if (selectedWard == "All") beds else beds.filter { it.wardName == selectedWard }
    }

    val totalBeds = beds.size
    val occupiedBeds = beds.count { it.isOccupied }
    val vacantBeds = totalBeds - occupiedBeds
    val occupancyRate = if (totalBeds > 0) (occupiedBeds.toFloat() / totalBeds) * 100 else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.md, bottom = Spacing.xxl)
    ) {
        // 1. Header with Back Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBackClick,
                    shape = PillShape,
                    color = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text("←", color = VS_OnBackground, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.hospitalClinicalServices), style = MaterialTheme.typography.labelMedium, color = VS_OnBackground)
                    }
                }

                Surface(
                    shape = PillShape,
                    color = VS_PrimaryContainer,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = stringResource(R.string.hospitalCareIpd),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_PrimaryContainer,
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                    )
                }
            }
        }

        // 2. Hero Bed Occupancy HUD
        item {
            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(1.dp, VS_Outline)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🛏️ ${stringResource(R.string.ipdBedTracker)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.ipdSubtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = if (occupancyRate > 80) VS_ErrorContainer else VS_SuccessContainer
                        ) {
                            Text(
                                text = "${occupancyRate.toInt()}% ${stringResource(R.string.occupied)}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (occupancyRate > 80) VS_Error else VS_Success,
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                            )
                        }
                    }

                    // Linear Capacity Indicator
                    LinearProgressIndicator(
                        progress = { occupancyRate / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(PillShape),
                        color = if (occupancyRate > 80) VS_Error else VS_Primary,
                        trackColor = VS_SurfaceVariant
                    )

                    // Quick Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.totalCapacity), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("$totalBeds Beds", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        }
                        Column {
                            Text(stringResource(R.string.admittedPatients), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("$occupiedBeds", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_Error)
                        }
                        Column {
                            Text(stringResource(R.string.availableVacant), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("$vacantBeds Beds", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_Success)
                        }
                    }
                }
            }
        }

        // 3. Ward Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(wards) { ward ->
                    val isSelected = selectedWard == ward
                    Surface(
                        onClick = { selectedWard = ward },
                        shape = PillShape,
                        color = if (isSelected) VS_Primary else VS_Surface,
                        border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline)
                    ) {
                        Text(
                            text = ward,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) VS_OnBackground else VS_OnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                        )
                    }
                }
            }
        }

        // 4. Bed Cards List
        items(filteredBeds, key = { it.id }) { bed ->
            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(1.dp, if (bed.isOccupied) VS_Outline else VS_Success.copy(alpha = 0.4f))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (bed.isOccupied) VS_Error else VS_Success)
                            )
                            Text(
                                text = bed.bedNumber,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "· ${bed.wardName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = if (bed.isOccupied) VS_ErrorContainer else VS_SuccessContainer
                        ) {
                            Text(
                                text = if (bed.isOccupied) "OCCUPIED" else "VACANT",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = if (bed.isOccupied) VS_Error else VS_Success,
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                            )
                        }
                    }

                    if (bed.isOccupied) {
                        HorizontalDivider(color = VS_Outline)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "👤 Patient: ${bed.patientName ?: "Admitted Inmate"}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            if (!bed.diagnosis.isNullOrBlank()) {
                                Text(
                                    text = "🩺 Diagnosis: ${bed.diagnosis}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "👨‍⚕️ ${bed.attendingDoctorName ?: "Dr. Rajesh Kumar"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VS_PrimaryContainer
                                )
                                Text(
                                    text = "Admitted: ${bed.admissionDate ?: "Recent"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            if (!bed.nurseInCharge.isNullOrBlank()) {
                                Text(
                                    text = "👩‍⚕️ Nurse: ${bed.nurseInCharge}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                        }

                        // Discharge Clearance Button
                        OutlinedButton(
                            onClick = {
                                onSaveBed(
                                    bed.copy(
                                        isOccupied = false,
                                        patientId = null,
                                        patientName = null,
                                        admissionDate = null,
                                        diagnosis = null,
                                        attendingDoctorName = null,
                                        nurseInCharge = null
                                    )
                                )
                            },
                            shape = PillShape,
                            border = BorderStroke(1.dp, VS_Outline),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 36.dp)
                        ) {
                            Text(stringResource(R.string.clearDischargeBed), style = MaterialTheme.typography.labelSmall, color = VS_Error)
                        }
                    } else {
                        // Vacant Bed Action
                        Button(
                            onClick = { showAdmitDialogForBed = bed },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VS_Primary,
                                contentColor = VS_OnBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 36.dp)
                        ) {
                            Text("📥 Admit Patient to ${bed.bedNumber}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }

    // In-Patient Admission Modal Dialog
    showAdmitDialogForBed?.let { bedToAdmit ->
        var patientName by remember { mutableStateOf("") }
        var diagnosis by remember { mutableStateOf("") }
        var doctorName by remember { mutableStateOf("Dr. Rajesh Kumar") }
        var nurseName by remember { mutableStateOf("Sister Sunita R.") }

        AlertDialog(
            onDismissRequest = { showAdmitDialogForBed = null },
            title = {
                Text(
                    text = "Admit Patient to ${bedToAdmit.bedNumber}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Text(
                        text = "Ward: ${bedToAdmit.wardName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )

                    VitalSenseTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = "Patient Full Name",
                        placeholder = "e.g. Ramesh Kumar"
                    )

                    VitalSenseTextField(
                        value = diagnosis,
                        onValueChange = { diagnosis = it },
                        label = "Admission Diagnosis / Indication",
                        placeholder = "e.g. Acute Decompensated Heart Failure"
                    )

                    VitalSenseTextField(
                        value = doctorName,
                        onValueChange = { doctorName = it },
                        label = "Attending Physician",
                        placeholder = "e.g. Dr. Rajesh Kumar"
                    )

                    VitalSenseTextField(
                        value = nurseName,
                        onValueChange = { nurseName = it },
                        label = "Ward Nurse in Charge",
                        placeholder = "e.g. Sister Rekha M."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        onSaveBed(
                            bedToAdmit.copy(
                                isOccupied = true,
                                patientName = patientName.ifBlank { "Admitted Inmate" },
                                diagnosis = diagnosis.ifBlank { "Clinical Observation" },
                                attendingDoctorName = doctorName.ifBlank { "Dr. Rajesh Kumar" },
                                nurseInCharge = nurseName.ifBlank { "Duty Sister" },
                                admissionDate = currentDate
                            )
                        )
                        showAdmitDialogForBed = null
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    enabled = patientName.isNotBlank()
                ) {
                    Text(stringResource(R.string.confirmAdmission), style = MaterialTheme.typography.labelSmall)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdmitDialogForBed = null }) {
                    Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant)
                }
            },
            containerColor = VS_Surface,
            tonalElevation = 6.dp
        )
    }
}
