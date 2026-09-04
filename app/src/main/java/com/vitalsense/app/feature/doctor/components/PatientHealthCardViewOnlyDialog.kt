package com.vitalsense.app.feature.doctor.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@Composable
fun PatientHealthCardViewOnlyDialog(
    patient: Patient,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                // Header with Read-Only Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.patientHealthCardTitle),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Surface(
                            shape = PillShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.viewOnlyAccessRule),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp),
                                color = VS_PrimaryContainer
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // Demographic Card
                VitalSenseCard(
                    backgroundColor = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Outline)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = patient.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "Age: ${patient.age} yrs · Gender: ${patient.gender} · Village: ${patient.villageName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            SeverityBadge(severity = patient.currentRiskLevel)
                        }

                        HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xxs))

                        Text(
                            text = "📞 Contact: ${patient.phone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnBackground
                        )
                        Text(
                            text = "🚨 Emergency Contact: ${patient.emergencyContact}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnBackground
                        )
                        Text(
                            text = "🤝 Assigned ASHA: ${patient.ashaWorkerName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }

                // Clinical History Card
                VitalSenseCard(
                    backgroundColor = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Outline)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text(
                            text = stringResource(R.string.latestReportedCondition),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = patient.lastCondition,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VS_OnBackground
                        )
                        Row(
                            modifier = Modifier.padding(top = Spacing.xxs),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                        ) {
                            Text(
                                text = "Last Checkup: ${patient.lastVisitDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                            Text(
                                text = "Next Appt: ${patient.nextAppointmentDate ?: "None"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                }

                VitalSenseButton(
                    text = stringResource(R.string.closeHealthCard),
                    onClick = onDismiss,
                    style = com.vitalsense.app.core.ui.components.ButtonStyle.PRIMARY
                )
            }
        }
    }
}
