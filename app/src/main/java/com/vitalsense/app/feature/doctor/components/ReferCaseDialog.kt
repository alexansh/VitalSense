package com.vitalsense.app.feature.doctor.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun ReferCaseDialog(
    patientName: String,
    currentSpecialty: DoctorSpecialty,
    onDismiss: () -> Unit,
    onRefer: (targetSpecialty: DoctorSpecialty, referralNotes: String) -> Unit
) {
    var selectedSpecialty by remember {
        mutableStateOf(
            if (currentSpecialty == DoctorSpecialty.GENERAL_PHYSICIAN) DoctorSpecialty.PSYCHOLOGIST
            else DoctorSpecialty.GENERAL_PHYSICIAN
        )
    }
    var referralNotes by remember { mutableStateOf("Patient requires specialized clinical consultation. Case history and initial symptoms attached.") }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.referCaseToSpecialist),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Patient: $patientName (§4.3 Escalation)",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline)

                Text(
                    text = stringResource(R.string.selectTargetSpecialtyColon),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    DoctorSpecialty.values().filter { it != currentSpecialty }.forEach { specialty ->
                        val isSelected = selectedSpecialty == specialty
                        Surface(
                            shape = CardShape,
                            color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = if (isSelected) BorderStroke(1.5.dp, VS_Primary) else BorderStroke(1.dp, VS_Outline),
                            onClick = { selectedSpecialty = specialty },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = specialty.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) VS_PrimaryContainer else VS_OnBackground
                                )
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedSpecialty = specialty },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = VS_Primary,
                                        unselectedColor = VS_OnSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.clinicalReferralNotesColon),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                OutlinedTextField(
                    value = referralNotes,
                    onValueChange = { referralNotes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp),
                    shape = InputShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VS_SurfaceVariant,
                        unfocusedContainerColor = VS_Surface,
                        focusedBorderColor = VS_Primary,
                        unfocusedBorderColor = VS_Outline,
                        focusedTextColor = VS_OnBackground,
                        unfocusedTextColor = VS_OnBackground
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).defaultMinSize(minHeight = 44.dp),
                        shape = PillShape,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Text(stringResource(R.string.cancel), color = VS_OnSurfaceVariant)
                    }

                    Button(
                        onClick = {
                            onRefer(selectedSpecialty, referralNotes)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.3f).defaultMinSize(minHeight = 44.dp),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary, contentColor = VS_OnBackground)
                    ) {
                        Text(stringResource(R.string.transferCaseArrow), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}
