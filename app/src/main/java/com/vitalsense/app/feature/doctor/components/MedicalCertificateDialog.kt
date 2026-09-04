package com.vitalsense.app.feature.doctor.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.data.model.Doctor
import com.vitalsense.app.core.data.model.MedicalCertificate
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MedicalCertificateDialog(
    condition: ConditionRecord,
    doctor: Doctor,
    onDismiss: () -> Unit,
    onIssueCertificate: (MedicalCertificate) -> Unit
) {
    var certificateType by remember { mutableStateOf("Sick Leave Certificate") }
    var diagnosis by remember { mutableStateOf(condition.notes.ifBlank { "Acute Febrile Illness" }) }
    var restStartDate by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())) }
    var restEndDate by remember { mutableStateOf("7 Days from today") }
    var fitDate by remember { mutableStateOf("Upon recovery review") }
    var licenseNumber by remember { mutableStateOf("MCI-48201 / UP-MED-778") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.issueMedicalCertificateTitle),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Text(
                        text = stringResource(R.string.certifiedClinicalLeaveFitness),
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_PrimaryContainer
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = VS_OnSurfaceVariant)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.certificateTypeLabel),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        listOf("Sick Leave Certificate", "Medical Fitness Certificate").forEach { type ->
                            val isSelected = certificateType == type
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { certificateType = type }
                            ) {
                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) VS_Primary else VS_OnBackground,
                                    modifier = Modifier.padding(Spacing.sm)
                                )
                            }
                        }
                    }
                }

                item {
                    VitalSenseCard(
                        backgroundColor = VS_SurfaceVariant,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Patient: ${condition.patientName} (Age: 45)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                            Text("Village: ${condition.villageName}", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("Attending Doctor: ${doctor.name} (${doctor.specialty.name})", style = MaterialTheme.typography.labelSmall, color = VS_PrimaryContainer)
                        }
                    }
                }

                item {
                    VitalSenseTextField(
                        value = diagnosis,
                        onValueChange = { diagnosis = it },
                        label = "Clinical Diagnosis & Findings"
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            VitalSenseTextField(
                                value = restStartDate,
                                onValueChange = { restStartDate = it },
                                label = "Rest From"
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            VitalSenseTextField(
                                value = restEndDate,
                                onValueChange = { restEndDate = it },
                                label = "Rest To"
                            )
                        }
                    }
                }

                item {
                    VitalSenseTextField(
                        value = fitDate,
                        onValueChange = { fitDate = it },
                        label = "Fit to Resume Normal Duty Date"
                    )
                }

                item {
                    VitalSenseTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = "Doctor Medical Council Registration Number"
                    )
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VS_SuccessContainer.copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, VS_OnSuccessContainer.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(Spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.VerifiedUser,
                                contentDescription = null,
                                tint = VS_OnSuccessContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(R.string.certificateSealedStampNotice),
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSuccessContainer
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            VitalSenseButton(
                text = stringResource(R.string.digitallySignIssue),
                onClick = {
                    val dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                    val certNum = "MC-2026-${(1000..9999).random()}"
                    val cert = MedicalCertificate(
                        id = "cert_${System.currentTimeMillis()}",
                        certificateNumber = certNum,
                        patientId = condition.patientId,
                        patientName = condition.patientName,
                        patientAge = 45,
                        patientGender = "Male",
                        doctorName = doctor.name,
                        doctorRegistrationNumber = licenseNumber.ifBlank { "MCI-48201" },
                        diagnosis = diagnosis,
                        restStartDate = restStartDate,
                        restEndDate = restEndDate,
                        fitDate = fitDate,
                        certificateType = certificateType,
                        issuedDateFormatted = dateFormatted
                    )
                    onIssueCertificate(cert)
                },
                style = ButtonStyle.PRIMARY
            )
        },
        dismissButton = {
            VitalSenseButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss,
                style = ButtonStyle.SECONDARY
            )
        },
        containerColor = VS_Background
    )
}
