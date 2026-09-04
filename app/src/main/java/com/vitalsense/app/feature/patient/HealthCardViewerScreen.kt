package com.vitalsense.app.feature.patient
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@Composable
fun HealthCardViewerScreen(patient: Patient) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = stringResource(R.string.offlineHealthCard),
            style = MaterialTheme.typography.displayMedium,
            color = VS_OnBackground
        )

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
                            text = patient.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Age: ${patient.age} · Gender: ${patient.gender} · Village: ${patient.villageName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    SeverityBadge(severity = patient.currentRiskLevel)
                }

                HorizontalDivider(color = VS_Outline)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(R.string.bloodGroupLabel), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                        Text(stringResource(R.string.oPositiveSample), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                    }
                    Column {
                        Text(stringResource(R.string.allergiesLabel), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                        Text(stringResource(R.string.noneReported), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                    }
                    Column {
                        Text(stringResource(R.string.emergencyLabel), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                        Text(patient.emergencyContact, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = VS_Error)
                    }
                }

                Surface(
                    shape = CardShape,
                    color = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Text(text = "🔲", fontSize = 28.sp)
                        Column {
                            Text(
                                text = stringResource(R.string.permanentOfflineQrIdentity),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )
                            Text(
                                text = "UID: ${patient.id} · Cached Offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}