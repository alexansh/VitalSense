package com.vitalsense.app.feature.biomedical

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.BioMedicalEquipment
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun BioMedicalScreen(
    equipmentList: List<BioMedicalEquipment>,
    onBackClick: () -> Unit,
    onUpdateEquipment: (BioMedicalEquipment) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedEquipmentForMaint by remember { mutableStateOf<BioMedicalEquipment?>(null) }

    val filterOptions = listOf("ALL", "OPERATIONAL", "CALIBRATION_DUE", "UNDER_MAINTENANCE")

    val filteredList = remember(equipmentList, selectedFilter) {
        if (selectedFilter == "ALL") equipmentList else equipmentList.filter { it.status == selectedFilter }
    }

    val operationalCount = equipmentList.count { it.status == "OPERATIONAL" }
    val attentionCount = equipmentList.count { it.status != "OPERATIONAL" }

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
                        text = stringResource(R.string.hospitalCareBme),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_PrimaryContainer,
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                    )
                }
            }
        }

        // 2. Hero Bio-Medical HUD
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
                                text = "⚡ ${stringResource(R.string.bioMedicalTracker)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = stringResource(R.string.bioMedicalSubtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = VS_SuccessContainer
                        ) {
                            Text(
                                text = "$operationalCount / ${equipmentList.size} Active",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Success,
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = VS_Outline)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.operational), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("$operationalCount Units", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_Success)
                        }
                        Column {
                            Text(stringResource(R.string.maintenanceDue), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("$attentionCount Units", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = if (attentionCount > 0) VS_Error else VS_Success)
                        }
                        Column {
                            Text(stringResource(R.string.bmeEngineering), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(stringResource(R.string.twentyFourSevenOnCall), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = VS_PrimaryContainer)
                        }
                    }
                }
            }
        }

        // 3. Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        onClick = { selectedFilter = filter },
                        shape = PillShape,
                        color = if (isSelected) VS_Primary else VS_Surface,
                        border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline)
                    ) {
                        Text(
                            text = filter.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) VS_OnBackground else VS_OnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                        )
                    }
                }
            }
        }

        // 4. Equipment Cards
        items(filteredList, key = { it.id }) { equip ->
            val isOperational = equip.status == "OPERATIONAL"
            val isCalibrationDue = equip.status == "CALIBRATION_DUE"

            VitalSenseCard(
                backgroundColor = VS_Surface,
                border = BorderStroke(
                    1.dp,
                    when {
                        isOperational -> VS_Outline
                        isCalibrationDue -> VS_Warning.copy(alpha = 0.5f)
                        else -> VS_Error.copy(alpha = 0.5f)
                    }
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = PillShape,
                            color = VS_SurfaceVariant
                        ) {
                            Text(
                                text = equip.assetCode,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground,
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = when {
                                isOperational -> VS_SuccessContainer
                                isCalibrationDue -> VS_WarningContainer
                                else -> VS_ErrorContainer
                            }
                        ) {
                            Text(
                                text = equip.status.replace("_", " "),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = when {
                                    isOperational -> VS_Success
                                    isCalibrationDue -> VS_Warning
                                    else -> VS_Error
                                },
                                modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = equip.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    Text(
                        text = "Department: ${equip.department} · Location: ${equip.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )

                    HorizontalDivider(color = VS_Outline)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.lastServiced), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(equip.lastServiceDate, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                        }
                        Column {
                            Text(stringResource(R.string.nextDueDate), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text(equip.nextServiceDue, style = MaterialTheme.typography.bodySmall, color = if (isCalibrationDue) VS_Warning else VS_OnBackground)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "In-Charge: ${equip.inChargeContact}",
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_PrimaryContainer
                        )

                        OutlinedButton(
                            onClick = { selectedEquipmentForMaint = equip },
                            shape = PillShape,
                            border = BorderStroke(1.dp, VS_Outline),
                            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = 2.dp),
                            modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                        ) {
                            Text(stringResource(R.string.updateStatusBtn), style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                        }
                    }
                }
            }
        }
    }

    // Status update dialog
    selectedEquipmentForMaint?.let { equip ->
        var status by remember { mutableStateOf(equip.status) }

        AlertDialog(
            onDismissRequest = { selectedEquipmentForMaint = null },
            title = {
                Text(
                    text = "Update ${equip.assetCode}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Text(equip.name, style = MaterialTheme.typography.bodyMedium, color = VS_OnBackground)

                    Text(stringResource(R.string.selectOperationalStatus), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)

                    listOf("OPERATIONAL", "CALIBRATION_DUE", "UNDER_MAINTENANCE").forEach { opt ->
                        Surface(
                            onClick = { status = opt },
                            shape = PillShape,
                            color = if (status == opt) VS_Primary else VS_Surface,
                            border = BorderStroke(1.dp, if (status == opt) VS_Primary else VS_Outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = opt.replace("_", " "),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (status == opt) VS_OnBackground else VS_OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateEquipment(equip.copy(status = status))
                        selectedEquipmentForMaint = null
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                ) {
                    Text(stringResource(R.string.saveStatusBtn), style = MaterialTheme.typography.labelSmall)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedEquipmentForMaint = null }) {
                    Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant)
                }
            },
            containerColor = VS_Surface,
            tonalElevation = 6.dp
        )
    }
}
