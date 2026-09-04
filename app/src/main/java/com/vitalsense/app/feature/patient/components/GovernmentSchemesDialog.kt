package com.vitalsense.app.feature.patient.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.GovernmentScheme
import com.vitalsense.app.core.ui.theme.*

@Composable
fun GovernmentSchemesDialog(
    schemes: List<GovernmentScheme>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = VS_Surface,
            border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.6f)),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(vertical = Spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                // Header
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
                                Text("🏛️", fontSize = 18.sp)
                            }
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.governmentHealthSchemes),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.ruralWelfarePrograms),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Text("✕", color = VS_OnSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                // Scheme list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    items(schemes) { scheme ->
                        Surface(
                            shape = CardShape,
                            color = VS_SurfaceVariant,
                            border = BorderStroke(1.dp, VS_Outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Spacing.sm),
                                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = scheme.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        shape = PillShape,
                                        color = VS_SuccessContainer
                                    ) {
                                        Text(
                                            text = stringResource(R.string.eligibleBadge),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = VS_Success,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Beneficiaries: ${scheme.targetBeneficiary}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = VS_PrimaryContainer)
                                )

                                Text(
                                    text = scheme.benefitsSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(color = VS_OnBackground)
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = VS_Background,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Criteria: ${scheme.eligibility}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = VS_OnSurfaceVariant
                                        ),
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Footer
                Button(
                    onClick = onDismiss,
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.closeSchemesView), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
