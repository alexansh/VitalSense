package com.vitalsense.app.feature.doctor.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReferralDialog(
    patient: Patient?,
    patientNameFallback: String,
    currentDoctor: Doctor,
    priorPrescriptions: List<Prescription> = emptyList(),
    allConditions: List<ConditionRecord> = emptyList(),
    onDismiss: () -> Unit,
    onSendReferral: (Referral) -> Unit,
    onEmergencyCallTrigger: () -> Unit = {}
) {
    val patientName = patient?.name ?: patientNameFallback
    val patientId = patient?.id ?: "pat_unknown"

    // Supported specialties
    val availableSpecialties = remember {
        listOf(
            DoctorSpecialty.CARDIOLOGIST,
            DoctorSpecialty.DERMATOLOGIST,
            DoctorSpecialty.PEDIATRICIAN,
            DoctorSpecialty.GYNECOLOGIST,
            DoctorSpecialty.ORTHOPEDIC_SURGEON,
            DoctorSpecialty.PSYCHOLOGIST,
            DoctorSpecialty.NEUROLOGIST,
            DoctorSpecialty.GENERAL_PHYSICIAN
        )
    }

    var selectedSpecialty by remember {
        mutableStateOf(availableSpecialties.firstOrNull() ?: DoctorSpecialty.CARDIOLOGIST)
    }

    // Named doctor vs Specialty Queue
    var routeToNamedDoctor by remember { mutableStateOf(false) }
    val specialistsForSpecialty = remember(selectedSpecialty) {
        SpecialistDirectoryProvider.getSpecialistsForSpecialty(selectedSpecialty.displayName)
    }
    var selectedSpecialistDoctor by remember(selectedSpecialty) {
        mutableStateOf(specialistsForSpecialty.firstOrNull())
    }

    // Urgency level
    var selectedUrgency by remember { mutableStateOf(ReferralUrgency.ROUTINE) }
    var showEmergencyWarning by remember { mutableStateOf(false) }

    // Clinical details
    var reason by remember { mutableStateOf("") }
    var clinicalQuestion by remember { mutableStateOf("") }

    // Attached records selection
    val availableRecords = remember(priorPrescriptions, allConditions) {
        val list = mutableListOf<Pair<String, String>>()
        allConditions.take(3).forEach { cond ->
            val notePreview = cond.notes.take(30)
            list.add(cond.id to "Condition: $notePreview... (${cond.severity.name})")
        }
        priorPrescriptions.take(3).forEach { rx ->
            val meds = rx.medicines.joinToString { it.name }
            list.add(rx.id to "Prescription: $meds (${rx.dateFormatted})")
        }
        list
    }
    var selectedRecordIds by remember(availableRecords) {
        mutableStateOf(availableRecords.map { it.first }.toSet())
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
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
                            Text(text = "🔄", fontSize = 22.sp)
                            Text(
                                text = "Doctor-to-Doctor Referral",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                        }
                        Text(
                            text = "Patient: $patientName · Ref by Dr. ${currentDoctor.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Text(
                            text = "✕",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xs))

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    // 1. Target Medical Specialty (Required)
                    Text(
                        text = "1. Select Target Medical Specialty *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        availableSpecialties.chunked(2).forEach { rowSpecialties ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowSpecialties.forEach { spec ->
                                    val isSelected = selectedSpecialty == spec
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                selectedSpecialty = spec
                                                selectedSpecialistDoctor = SpecialistDirectoryProvider
                                                    .getSpecialistsForSpecialty(spec.displayName)
                                                    .firstOrNull()
                                            },
                                        shape = CardShape,
                                        color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                                        border = BorderStroke(
                                            if (isSelected) 1.5.dp else 1.dp,
                                            if (isSelected) VS_Primary else VS_Outline
                                        )
                                    ) {
                                        Text(
                                            text = spec.displayName,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            ),
                                            color = if (isSelected) VS_PrimaryContainer else VS_OnBackground,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                                if (rowSpecialties.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    // 2. Routing Mode: Queue vs Named Specialist
                    Text(
                        text = "2. Routing & Triage Assignment",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { routeToNamedDoctor = false },
                            shape = CardShape,
                            color = if (!routeToNamedDoctor) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = BorderStroke(
                                if (!routeToNamedDoctor) 1.5.dp else 1.dp,
                                if (!routeToNamedDoctor) VS_Primary else VS_Outline
                            )
                        ) {
                            Column(modifier = Modifier.padding(Spacing.xs)) {
                                Text(
                                    text = "🏢 Specialty Queue",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (!routeToNamedDoctor) VS_PrimaryContainer else VS_OnBackground
                                )
                                Text(
                                    text = "Any available ${selectedSpecialty.displayName}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = VS_OnSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { routeToNamedDoctor = true },
                            shape = CardShape,
                            color = if (routeToNamedDoctor) VS_PrimaryContainer else VS_SurfaceVariant,
                            border = BorderStroke(
                                if (routeToNamedDoctor) 1.5.dp else 1.dp,
                                if (routeToNamedDoctor) VS_Primary else VS_Outline
                            )
                        ) {
                            Column(modifier = Modifier.padding(Spacing.xs)) {
                                Text(
                                    text = "👨‍⚕️ Named Specialist",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (routeToNamedDoctor) VS_PrimaryContainer else VS_OnBackground
                                )
                                Text(
                                    text = "Direct specific physician handoff",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = VS_OnSurfaceVariant
                                )
                            }
                        }
                    }

                    // If named doctor selected, show dropdown
                    if (routeToNamedDoctor) {
                        if (specialistsForSpecialty.isEmpty()) {
                            Text(
                                text = "No specific named specialist registered for this specialty. Will fallback to department queue.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_Warning
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                specialistsForSpecialty.forEach { specDoc ->
                                    val isSelected = selectedSpecialistDoctor?.id == specDoc.id
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedSpecialistDoctor = specDoc },
                                        shape = PillShape,
                                        color = if (isSelected) VS_SuccessContainer else VS_SurfaceVariant,
                                        border = BorderStroke(1.dp, if (isSelected) VS_Success else VS_Outline)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = specDoc.name,
                                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = if (isSelected) VS_Success else VS_OnBackground
                                                )
                                                Text(
                                                    text = "${specDoc.qualification} · ${specDoc.hospitalAffiliation}",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = VS_OnSurfaceVariant
                                                )
                                            }
                                            Surface(
                                                shape = PillShape,
                                                color = if (specDoc.availabilityStatus == DoctorAvailabilityStatus.AVAILABLE) VS_Success.copy(alpha = 0.2f) else VS_Warning.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = specDoc.availabilityStatus.displayName,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 9.sp,
                                                        color = if (specDoc.availabilityStatus == DoctorAvailabilityStatus.AVAILABLE) VS_Success else VS_Warning
                                                    ),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Urgency Level Selector
                    Text(
                        text = "3. Urgency Level *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReferralUrgency.values().forEach { urgency ->
                            val isSelected = selectedUrgency == urgency
                            val (bgColor, borderColor, textColor) = when (urgency) {
                                ReferralUrgency.ROUTINE -> Triple(
                                    if (isSelected) VS_SuccessContainer else VS_SurfaceVariant,
                                    if (isSelected) VS_Success else VS_Outline,
                                    if (isSelected) VS_Success else VS_OnBackground
                                )
                                ReferralUrgency.URGENT -> Triple(
                                    if (isSelected) VS_WarningContainer else VS_SurfaceVariant,
                                    if (isSelected) VS_Warning else VS_Outline,
                                    if (isSelected) VS_Warning else VS_OnBackground
                                )
                                ReferralUrgency.EMERGENCY -> Triple(
                                    if (isSelected) VS_ErrorContainer else VS_SurfaceVariant,
                                    if (isSelected) VS_Error else VS_Outline,
                                    if (isSelected) VS_Error else VS_OnBackground
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedUrgency = urgency
                                        showEmergencyWarning = (urgency == ReferralUrgency.EMERGENCY)
                                    },
                                shape = CardShape,
                                color = bgColor,
                                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
                            ) {
                                Column(
                                    modifier = Modifier.padding(Spacing.xs),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = when (urgency) {
                                            ReferralUrgency.ROUTINE -> "🟢 Routine"
                                            ReferralUrgency.URGENT -> "🟡 Urgent"
                                            ReferralUrgency.EMERGENCY -> "🔴 Emergency"
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = textColor
                                    )
                                    Text(
                                        text = when (urgency) {
                                            ReferralUrgency.ROUTINE -> "Standard review"
                                            ReferralUrgency.URGENT -> "< 24 Hours"
                                            ReferralUrgency.EMERGENCY -> "Immediate critical"
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // EMERGENCY WARNING BANNER
                    if (showEmergencyWarning || selectedUrgency == ReferralUrgency.EMERGENCY) {
                        VitalSenseCard(
                            backgroundColor = VS_ErrorContainer,
                            border = BorderStroke(1.5.dp, VS_Error)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🚨", fontSize = 20.sp)
                                    Text(
                                        text = "Emergency Warning: Queue Delay Risk",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_Error
                                    )
                                }
                                Text(
                                    text = "A referral queue is an asynchronous clinical handoff, NOT an acute response mechanism. If this patient has unstable vitals or life-threatening symptoms, please launch an immediate Emergency SOS call in addition to this record.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = VS_OnBackground
                                )
                                Button(
                                    onClick = {
                                        onDismiss()
                                        onEmergencyCallTrigger()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                                    shape = PillShape,
                                    modifier = Modifier.fillMaxWidth().height(36.dp)
                                ) {
                                    Text("🚨 Launch Emergency Video/Voice SOS Now", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                            }
                        }
                    }

                    // 4. Clinical Reason for Referral
                    Text(
                        text = "4. Clinical Reason for Referral *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Describe clinical findings, progression, and why specialist input is required...", fontSize = 12.sp, color = VS_OnSurfaceVariant) },
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VS_Primary,
                            unfocusedBorderColor = VS_Outline,
                            focusedTextColor = VS_OnBackground,
                            unfocusedTextColor = VS_OnBackground
                        )
                    )

                    // 5. Clinical Question / Specific Ask
                    Text(
                        text = "5. Specific Clinical Question / Ask *",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Text(
                        text = "Clearly specify what you need from the specialist (e.g. 'Confirm diagnosis of stage 2 HTN and advise titration')",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = VS_OnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = clinicalQuestion,
                        onValueChange = { clinicalQuestion = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Confirm diagnosis of X, evaluate for surgical intervention, or advise on drug titration...", fontSize = 12.sp, color = VS_OnSurfaceVariant) },
                        minLines = 2,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VS_Primary,
                            unfocusedBorderColor = VS_Outline,
                            focusedTextColor = VS_OnBackground,
                            unfocusedTextColor = VS_OnBackground
                        )
                    )

                    // 6. Attached Records Checklist
                    if (availableRecords.isNotEmpty()) {
                        Text(
                            text = "6. Attach Supporting Records (${selectedRecordIds.size}/${availableRecords.size})",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            availableRecords.forEach { (recId, label) ->
                                val isChecked = recId in selectedRecordIds
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedRecordIds = if (isChecked) {
                                                selectedRecordIds - recId
                                            } else {
                                                selectedRecordIds + recId
                                            }
                                        },
                                    shape = CardShape,
                                    color = if (isChecked) VS_PrimaryContainer.copy(alpha = 0.5f) else VS_SurfaceVariant,
                                    border = BorderStroke(1.dp, if (isChecked) VS_PrimaryContainer.copy(alpha = 0.6f) else VS_Outline)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checked ->
                                                selectedRecordIds = if (checked) selectedRecordIds + recId else selectedRecordIds - recId
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = VS_Primary,
                                                uncheckedColor = VS_Outline
                                            )
                                        )
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = VS_OnBackground
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Validation Error
                    errorMessage?.let { err ->
                        Text(
                            text = "⚠️ $err",
                            style = MaterialTheme.typography.bodySmall.copy(color = VS_Error, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xs))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = PillShape,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                    }

                    Button(
                        onClick = {
                            if (reason.isBlank()) {
                                errorMessage = "Please provide the clinical reason for this referral."
                                return@Button
                            }
                            if (clinicalQuestion.isBlank()) {
                                errorMessage = "Please specify the clinical question or ask for the specialist."
                                return@Button
                            }

                            val referral = Referral(
                                id = "ref_${System.currentTimeMillis()}",
                                patientId = patientId,
                                patientName = patientName,
                                referringUserId = currentDoctor.id,
                                referringUserName = currentDoctor.name,
                                referringUserSpecialty = currentDoctor.specialty.displayName,
                                targetDoctorId = if (routeToNamedDoctor) selectedSpecialistDoctor?.id else null,
                                targetDoctorName = if (routeToNamedDoctor) selectedSpecialistDoctor?.name else null,
                                targetSpecialty = selectedSpecialty.displayName,
                                reason = reason.trim(),
                                clinicalQuestion = clinicalQuestion.trim(),
                                urgency = selectedUrgency,
                                attachedRecordIds = selectedRecordIds.toList(),
                                status = ReferralStatus.CREATED,
                                statusHistory = listOf(
                                    ReferralStatusHistory(
                                        status = ReferralStatus.CREATED,
                                        changedByUserId = currentDoctor.id,
                                        note = "Referral created"
                                    )
                                ),
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            onSendReferral(referral)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.5f).height(44.dp),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (selectedUrgency) {
                                ReferralUrgency.ROUTINE -> VS_Primary
                                ReferralUrgency.URGENT -> VS_Warning
                                ReferralUrgency.EMERGENCY -> VS_Error
                            }
                        )
                    ) {
                        Text(
                            text = "Send Referral to Specialist",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
