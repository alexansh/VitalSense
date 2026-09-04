package com.vitalsense.app.feature.patient.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.feature.patient.components.PatientTimelineDialog
import kotlinx.coroutines.launch

@Composable
fun HealthCardDialog(
    patient: Patient,
    familyMembers: List<com.vitalsense.app.core.data.model.FamilyMember> = emptyList(),
    onDismiss: () -> Unit
) {
    var isSunlightMode by remember { mutableStateOf(false) }
    var selectedFamilyMember by remember { mutableStateOf<com.vitalsense.app.core.data.model.FamilyMember?>(null) }
    var showTimelineDialog by remember { mutableStateOf(false) }

    if (showTimelineDialog) {
        PatientTimelineDialog(patient = patient, onDismiss = { showTimelineDialog = false })
    }

    val activeName = selectedFamilyMember?.name ?: patient.name
    val activeAge = selectedFamilyMember?.age ?: patient.age
    val activeGender = selectedFamilyMember?.gender ?: patient.gender
    val activeAbha = selectedFamilyMember?.abhaId ?: "91-${patient.phone.takeLast(6)}-${patient.id.takeLast(4).uppercase()}"
    val activeBloodGroup = selectedFamilyMember?.bloodGroup ?: "O+"

    val cardBg = if (isSunlightMode) Color.White else VS_Surface
    val cardTextPrimary = if (isSunlightMode) Color(0xFF111111) else VS_OnBackground
    val cardTextSecondary = if (isSunlightMode) Color(0xFF555555) else VS_OnSurfaceVariant
    val cardBorder = if (isSunlightMode) Color(0xFFCCCCCC) else VS_Primary.copy(alpha = 0.6f)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = cardBg,
            border = BorderStroke(1.5.dp, cardBorder),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                // Top Header: National Health Digital Card Header
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
                                Text("🪪", fontSize = 18.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.digitalHealthCardUmid),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = VS_Primary
                            )
                            Text(
                                text = stringResource(R.string.vitalSenseIdentity),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = cardTextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Text("✕", color = cardTextSecondary, fontWeight = FontWeight.Bold)
                    }
                }

                // Family & Dependent Switcher Chips
                if (familyMembers.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.linkedBeneficiariesFamily),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnSurfaceVariant
                        )
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                val isSelf = selectedFamilyMember == null
                                Surface(
                                    shape = PillShape,
                                    color = if (isSelf) VS_Primary else VS_SurfaceVariant,
                                    border = BorderStroke(1.dp, if (isSelf) VS_Primary else VS_Outline),
                                    modifier = Modifier.clickable { selectedFamilyMember = null }
                                ) {
                                    Text(
                                        text = stringResource(R.string.primarySelf),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelf) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelf) Color.White else cardTextPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            items(familyMembers.size) { idx ->
                                val member = familyMembers[idx]
                                val isSelected = selectedFamilyMember?.id == member.id
                                Surface(
                                    shape = PillShape,
                                    color = if (isSelected) VS_Primary else VS_SurfaceVariant,
                                    border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline),
                                    modifier = Modifier.clickable { selectedFamilyMember = member }
                                ) {
                                    Text(
                                        text = "${member.name} (${member.relationship})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) Color.White else cardTextPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))
                
                VitalSenseButton(
                    text = stringResource(R.string.viewCareJourneyTimeline),
                    onClick = { showTimelineDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    style = ButtonStyle.SECONDARY
                )

                HorizontalDivider(color = if (isSunlightMode) Color(0xFFEEEEEE) else VS_Outline)

                // Patient Profile Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activeName,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = cardTextPrimary
                        )
                        Text(
                            text = "$activeAge Yrs · $activeGender · $activeBloodGroup · ${patient.villageName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = cardTextSecondary
                        )
                        Text(
                            text = "ABHA ID: $activeAbha",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = VS_PrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    SeverityBadge(severity = patient.currentRiskLevel)
                }

                // High-Contrast QR Code Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.xs)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        // Vector QR Code representation
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .background(Color.White)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val blockSize = size.width / 11f
                                val primaryColor = Color(0xFF111111)

                                // Top-Left Corner Box
                                drawRect(primaryColor, Offset(0f, 0f), Size(blockSize * 3.5f, blockSize * 3.5f))
                                drawRect(Color.White, Offset(blockSize * 0.7f, blockSize * 0.7f), Size(blockSize * 2.1f, blockSize * 2.1f))
                                drawRect(primaryColor, Offset(blockSize * 1.2f, blockSize * 1.2f), Size(blockSize * 1.1f, blockSize * 1.1f))

                                // Top-Right Corner Box
                                drawRect(primaryColor, Offset(size.width - blockSize * 3.5f, 0f), Size(blockSize * 3.5f, blockSize * 3.5f))
                                drawRect(Color.White, Offset(size.width - blockSize * 2.8f, blockSize * 0.7f), Size(blockSize * 2.1f, blockSize * 2.1f))
                                drawRect(primaryColor, Offset(size.width - blockSize * 2.3f, blockSize * 1.2f), Size(blockSize * 1.1f, blockSize * 1.1f))

                                // Bottom-Left Corner Box
                                drawRect(primaryColor, Offset(0f, size.height - blockSize * 3.5f), Size(blockSize * 3.5f, blockSize * 3.5f))
                                drawRect(Color.White, Offset(blockSize * 0.7f, size.height - blockSize * 2.8f), Size(blockSize * 2.1f, blockSize * 2.1f))
                                drawRect(primaryColor, Offset(blockSize * 1.2f, size.height - blockSize * 2.3f), Size(blockSize * 1.1f, blockSize * 1.1f))

                                // Center Data Dots
                                drawRect(primaryColor, Offset(blockSize * 4.5f, blockSize * 2f), Size(blockSize * 1.2f, blockSize * 1.2f))
                                drawRect(primaryColor, Offset(blockSize * 4.5f, blockSize * 5f), Size(blockSize * 2f, blockSize * 2f))
                                drawRect(primaryColor, Offset(blockSize * 7.5f, blockSize * 4f), Size(blockSize * 1.2f, blockSize * 2f))
                                drawRect(primaryColor, Offset(blockSize * 2f, blockSize * 7.5f), Size(blockSize * 1.5f, blockSize * 1.5f))
                                drawRect(primaryColor, Offset(blockSize * 5.5f, blockSize * 8f), Size(blockSize * 2f, blockSize * 1.2f))
                                drawRect(primaryColor, Offset(blockSize * 8.5f, blockSize * 7f), Size(blockSize * 1.5f, blockSize * 1.5f))
                            }
                        }

                        Text(
                            text = stringResource(R.string.scanAtClinicDispensary),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = Color(0xFF333333)
                            )
                        )
                    }
                }

                // Emergency & Care Details Grid
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSunlightMode) Color(0xFFF7F7F7) else VS_SurfaceVariant,
                    border = BorderStroke(1.dp, if (isSunlightMode) Color(0xFFE0E0E0) else VS_Outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(stringResource(R.string.emergencyContactLabel), style = MaterialTheme.typography.labelSmall, color = cardTextSecondary)
                                Text(patient.emergencyContact, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = cardTextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(stringResource(R.string.assignedAshaLabel), style = MaterialTheme.typography.labelSmall, color = cardTextSecondary)
                                Text(patient.ashaWorkerName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = cardTextPrimary)
                            }
                        }

                        HorizontalDivider(color = if (isSunlightMode) Color(0xFFE5E5E5) else VS_Outline)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(stringResource(R.string.activeClinicalConditionLabel), style = MaterialTheme.typography.labelSmall, color = cardTextSecondary)
                                Text(patient.lastCondition, style = MaterialTheme.typography.bodySmall, color = cardTextPrimary)
                            }
                        }
                    }
                }

                // ABDM Sync & Consent Simulation
                var isSyncingAbdm by remember { mutableStateOf(false) }
                var abdmSyncSuccess by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (abdmSyncSuccess) VS_Success.copy(alpha = 0.1f) else VS_PrimaryContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, if (abdmSyncSuccess) VS_Success else VS_Primary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(if (abdmSyncSuccess) "✅" else "🔗", fontSize = 16.sp)
                                Text(
                                    text = if (abdmSyncSuccess) "ABDM Network Synchronized" else "ABHA ID Integration (ABDM)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (abdmSyncSuccess) VS_Success else VS_Primary
                                    )
                                )
                            }
                            if (!abdmSyncSuccess && !isSyncingAbdm) {
                                TextButton(
                                    onClick = {
                                        isSyncingAbdm = true
                                        scope.launch {
                                            kotlinx.coroutines.delay(1500)
                                            abdmSyncSuccess = true
                                            isSyncingAbdm = false
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(stringResource(R.string.linkAbhaBtn), style = MaterialTheme.typography.labelSmall)
                                }
                            } else if (isSyncingAbdm) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = VS_Primary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        
                        Text(
                            text = if (abdmSyncSuccess) 
                                "Digital records for $activeAbha are now securely linked to the Ayushman Bharat Digital Mission via Health Information Provider (HIP) gateway." 
                            else 
                                "Link this health profile to the national ABDM sandbox to allow cross-facility health record sharing and consent management.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = cardTextSecondary
                        )
                    }
                }

                // Footer Actions: Offline Status & Sunlight Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🔒", fontSize = 12.sp)
                        Text(
                            text = stringResource(R.string.offlineSqliteEncrypted),
                            style = MaterialTheme.typography.labelSmall.copy(color = VS_Success, fontWeight = FontWeight.Bold)
                        )
                    }

                    TextButton(
                        onClick = { isSunlightMode = !isSunlightMode },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isSunlightMode) "🌙 Dark Mode" else "☀️ Sunlight Mode",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = VS_Primary
                            )
                        )
                    }
                }
            }
        }
    }

    if (showTimelineDialog) {
        PatientTimelineDialog(
            patient = patient,
            onDismiss = { showTimelineDialog = false }
        )
    }
}
