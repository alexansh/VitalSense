package com.vitalsense.app.feature.referrals

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.ExternalReferral
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExternalReferralScreen(
    referrals: List<ExternalReferral>,
    patients: List<Patient> = emptyList(),
    onBackClick: () -> Unit,
    onIssueReferral: (ExternalReferral) -> Unit,
    modifier: Modifier = Modifier
) {
    var showIssueDialog by remember { mutableStateOf(false) }

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
                        Text("Hospital Desk", style = MaterialTheme.typography.labelMedium, color = VS_OnBackground)
                    }
                }

                Surface(
                    shape = PillShape,
                    color = VS_PrimaryContainer,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Hospital Network · External Referrals",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_PrimaryContainer,
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                    )
                }
            }
        }

        // 2. Hero HUD
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
                                text = "🏛️ Super-Specialty External Referrals",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "Empanelled Apex Hospitals & Cashless Requisition Desk",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showIssueDialog = true },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VS_Primary,
                                contentColor = VS_OnBackground
                            ),
                            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xxs),
                            modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                        ) {
                            Text("+ Issue Voucher", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    HorizontalDivider(color = VS_Outline)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Active Referral Passes", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("${referrals.size} Active", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        }
                        Column {
                            Text("Tie-up Network", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("AIIMS, Central Rly, KGMU", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = VS_PrimaryContainer)
                        }
                    }
                }
            }
        }

        // 3. Referral Passes List
        items(referrals, key = { it.id }) { ref ->
            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(1.dp, VS_Outline)
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
                            Surface(
                                shape = PillShape,
                                color = VS_PrimaryContainer
                            ) {
                                Text(
                                    text = ref.referralLetterId,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_PrimaryContainer,
                                    modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "· Issued ${ref.issuedDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        if (ref.isCashlessApproved) {
                            Surface(
                                shape = PillShape,
                                color = VS_SuccessContainer
                            ) {
                                Text(
                                    text = "✓ CASHLESS APPROVED",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                    color = VS_Success,
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "🏥 ${ref.empanelledHospitalName}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    Text(
                        text = "Specialty: ${ref.specialtyRequired}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = VS_PrimaryContainer
                    )

                    Text(
                        text = "Clinical Summary: ${ref.clinicalSummary}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )

                    HorizontalDivider(color = VS_Outline)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Beneficiary Patient", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(ref.patientName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        }

                        if (ref.ambulanceRequisitioned) {
                            Surface(
                                shape = PillShape,
                                color = VS_ErrorContainer
                            ) {
                                Text(
                                    text = "🚑 Ambulance Requisitioned",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = VS_Error,
                                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Issue Referral Modal Dialog
    if (showIssueDialog) {
        var patientName by remember { mutableStateOf("") }
        var hospitalName by remember { mutableStateOf("Railway Central Hospital, New Delhi") }
        var specialty by remember { mutableStateOf("Cardiothoracic Surgery") }
        var summary by remember { mutableStateOf("") }
        var ambulanceNeeded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showIssueDialog = false },
            title = {
                Text(
                    text = "Issue Super-Specialty Referral Voucher",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    VitalSenseTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = "Patient Full Name",
                        placeholder = "e.g. Ramesh Kumar"
                    )

                    VitalSenseTextField(
                        value = hospitalName,
                        onValueChange = { hospitalName = it },
                        label = "Empanelled Hospital / Medical College",
                        placeholder = "e.g. AIIMS New Delhi"
                    )

                    VitalSenseTextField(
                        value = specialty,
                        onValueChange = { specialty = it },
                        label = "Specialty / Department Required",
                        placeholder = "e.g. Neurosurgery, Oncology"
                    )

                    VitalSenseTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = "Clinical Justification & Case Summary",
                        placeholder = "Describe indications requiring tertiary care...",
                        singleLine = false,
                        maxLines = 3
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Requisition Emergency Transport / Ambulance",
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_OnBackground
                        )
                        Switch(
                            checked = ambulanceNeeded,
                            onCheckedChange = { ambulanceNeeded = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VS_OnBackground,
                                checkedTrackColor = VS_Primary
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        val randNum = (1000..9999).random()
                        val newRef = ExternalReferral(
                            id = "ref_${System.currentTimeMillis()}",
                            referralLetterId = "REF-2026-$randNum",
                            patientId = "pat_ref",
                            patientName = patientName.ifBlank { "Beneficiary Inmate" },
                            referringDoctorName = "Dr. Rajesh Kumar",
                            empanelledHospitalName = hospitalName.ifBlank { "Railway Central Hospital" },
                            specialtyRequired = specialty.ifBlank { "Tertiary Super-Specialty" },
                            clinicalSummary = summary.ifBlank { "Tertiary evaluation and management." },
                            isCashlessApproved = true,
                            ambulanceRequisitioned = ambulanceNeeded,
                            issuedDate = currentDate,
                            status = "Active"
                        )
                        onIssueReferral(newRef)
                        showIssueDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    enabled = patientName.isNotBlank()
                ) {
                    Text("Issue & Sign Voucher", style = MaterialTheme.typography.labelSmall)
                }
            },
            dismissButton = {
                TextButton(onClick = { showIssueDialog = false }) {
                    Text("Cancel", color = VS_OnSurfaceVariant)
                }
            },
            containerColor = VS_Surface,
            tonalElevation = 6.dp
        )
    }
}
