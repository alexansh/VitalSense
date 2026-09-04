package com.vitalsense.app.feature.doctor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.core.data.model.Referral
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialistFindingsDialog(
    referral: Referral,
    onDismiss: () -> Unit,
    onSubmitFindings: (findings: String, recommendations: String, followUpNeeded: Boolean) -> Unit
) {
    var findings by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var followUpNeeded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
            shape = DialogShape,
            color = GlumeSurfaceCard,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, GlumeBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.md)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "📝", fontSize = 22.sp)
                            Text(
                                text = "Specialist Loop Closure",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = GlumeTextPrimary
                            )
                        }
                        Text(
                            text = "Handoff to Dr. ${referral.referringUserName} · Patient: ${referral.patientName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlumeTextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(
                            text = "✕",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GlumeTextSecondary
                        )
                    }
                }

                HorizontalDivider(color = GlumeBorder, modifier = Modifier.padding(vertical = Spacing.xs))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    // Clinical Question being answered
                    VitalSenseCard(
                        backgroundColor = GlumePrimaryPurpleContainer.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, GlumePrimaryPurple.copy(alpha = 0.4f))
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Referring Ask / Clinical Question:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GlumePrimaryPurpleLight
                            )
                            Text(
                                text = "\"${referral.clinicalQuestion}\"",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = GlumeTextPrimary
                            )
                        }
                    }

                    // 1. Clinical Findings & Diagnostic Assessment
                    Text(
                        text = "1. Clinical Findings & Diagnostic Assessment *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = GlumeTextPrimary
                    )
                    OutlinedTextField(
                        value = findings,
                        onValueChange = { findings = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Document your clinical evaluation, exam results, diagnostic conclusions...", fontSize = 12.sp, color = GlumeTextSecondary) },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GlumePrimaryPurple,
                            unfocusedBorderColor = GlumeBorder,
                            focusedTextColor = GlumeTextPrimary,
                            unfocusedTextColor = GlumeTextPrimary
                        )
                    )

                    // 2. Recommendations for Referring Physician
                    Text(
                        text = "2. Ongoing Care Plan & Recommendations *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = GlumeTextPrimary
                    )
                    OutlinedTextField(
                        value = recommendations,
                        onValueChange = { recommendations = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Advise treatment adjustments, medication doses, lifestyle advice, or monitoring frequency...", fontSize = 12.sp, color = GlumeTextSecondary) },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GlumePrimaryPurple,
                            unfocusedBorderColor = GlumeBorder,
                            focusedTextColor = GlumeTextPrimary,
                            unfocusedTextColor = GlumeTextPrimary
                        )
                    )

                    // 3. Specialist Follow-Up Needed Toggle
                    Surface(
                        shape = CardShape,
                        color = GlumeSurfaceElevated,
                        border = BorderStroke(1.dp, GlumeBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Specialist Follow-Up Required",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = GlumeTextPrimary
                                )
                                Text(
                                    text = if (followUpNeeded) "Specialist will re-evaluate patient in future" else "Referring physician handles ongoing primary care",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = GlumeTextSecondary
                                )
                            }
                            Switch(
                                checked = followUpNeeded,
                                onCheckedChange = { followUpNeeded = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = GlumeSuccessMint,
                                    checkedTrackColor = GlumeSuccessContainer
                                )
                            )
                        }
                    }

                    errorMessage?.let { err ->
                        Text(
                            text = "⚠️ $err",
                            style = MaterialTheme.typography.bodySmall.copy(color = GlumeAlertCoral, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                HorizontalDivider(color = GlumeBorder, modifier = Modifier.padding(vertical = Spacing.xs))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = PillShape,
                        border = BorderStroke(1.dp, GlumeBorder)
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelSmall, color = GlumeTextPrimary)
                    }

                    Button(
                        onClick = {
                            if (findings.isBlank()) {
                                errorMessage = "Please document your diagnostic findings."
                                return@Button
                            }
                            if (recommendations.isBlank()) {
                                errorMessage = "Please provide care recommendations for the referring doctor."
                                return@Button
                            }

                            onSubmitFindings(findings.trim(), recommendations.trim(), followUpNeeded)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.5f).height(44.dp),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = GlumeSuccessMint)
                    ) {
                        Text(
                            text = "Send Findings & Close Loop",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = GlumeBackground
                        )
                    }
                }
            }
        }
    }
}
