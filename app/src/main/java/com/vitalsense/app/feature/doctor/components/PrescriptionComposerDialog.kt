package com.vitalsense.app.feature.doctor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.medicine.*
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import kotlinx.coroutines.delay
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

sealed class AvailabilityCheckState {
    object Idle : AvailabilityCheckState()
    object Checking : AvailabilityCheckState()
    data class Available(val count: Int, val stores: List<StoreAvailabilityResult>) : AvailabilityCheckState()
    data class Unavailable(
        val medicine: Medicine?,
        val candidateSubstitutes: List<Pair<Medicine, Int>> // (substitute, availableStoresCount)
    ) : AvailabilityCheckState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionComposerDialog(
    patient: Patient?,
    patientNameFallback: String,
    caseId: String,
    dispensaryStock: List<DispensaryItem>,
    onDismiss: () -> Unit,
    onIssuePrescription: (medicines: List<PrescribedMedicine>, instructions: String) -> Unit,
    medicineRepoOverride: MedicineAvailabilityRepository? = null
) {
    val context = LocalContext.current
    val medicineRepo = remember {
        medicineRepoOverride ?: run {
            val db = VitalSenseDatabase.getDatabase(context)
            MedicineAvailabilityRepositoryImpl(db, context)
        }
    }

    val patientLat = when (patient?.villageId) {
        "v_sundarpura" -> 26.8467
        "v_kalyanpur" -> 26.8821
        "v_bhimnagar" -> 26.8150
        else -> 26.8467
    }
    val patientLng = when (patient?.villageId) {
        "v_sundarpura" -> 80.9462
        "v_kalyanpur" -> 80.9812
        "v_bhimnagar" -> 80.9120
        else -> 80.9462
    }

    var instructions by remember { mutableStateOf("Take medications after meals as directed. Drink plenty of boiled lukewarm water.") }
    val medicinesList = remember {
        mutableStateListOf(
            PrescribedMedicine(
                name = "Amoxicillin 500mg",
                dosage = "1 capsule",
                frequency = "3 times daily after food",
                duration = "5 days",
                quantity = 15,
                medicineId = "med_amoxicillin_500",
                hasAlternativeAvailable = false
            ),
            PrescribedMedicine(
                name = "Paracetamol 650mg",
                dosage = "1 tablet",
                frequency = "SOS (if fever > 100°F)",
                duration = "3 days",
                quantity = 6,
                medicineId = "med_paracetamol_650",
                hasAlternativeAvailable = false
            )
        )
    }

    var newMedName by remember { mutableStateOf("") }
    var newMedDosage by remember { mutableStateOf("1 tablet") }
    var newMedFrequency by remember { mutableStateOf("Twice daily after meals") }
    var newMedDuration by remember { mutableStateOf("5 days") }
    var newMedQuantity by remember { mutableStateOf("10") }

    var availabilityState by remember { mutableStateOf<AvailabilityCheckState>(AvailabilityCheckState.Idle) }
    var showAlternativeSuggestions by remember { mutableStateOf(false) }

    // Check availability live as the doctor types
    LaunchedEffect(newMedName) {
        val trimmed = newMedName.trim()
        if (trimmed.length < 3) {
            availabilityState = AvailabilityCheckState.Idle
            showAlternativeSuggestions = false
            return@LaunchedEffect
        }
        delay(300)
        availabilityState = AvailabilityCheckState.Checking
        val matchedMed = medicineRepo.findMedicineByName(trimmed)
        val medId = matchedMed?.id ?: trimmed
        val results = medicineRepo.getNearbyStoresWithAvailability(medId, patientLat, patientLng)
        val inStockCount = results.count { it.inStock }

        if (inStockCount > 0) {
            availabilityState = AvailabilityCheckState.Available(inStockCount, results)
            showAlternativeSuggestions = false
        } else {
            val candidateSubs = if (matchedMed != null) {
                medicineRepo.suggestAlternatives(matchedMed.id, results).map { sub ->
                    val subResults = medicineRepo.getNearbyStoresWithAvailability(sub.id, patientLat, patientLng)
                    Pair(sub, subResults.count { it.inStock })
                }
            } else emptyList()
            availabilityState = AvailabilityCheckState.Unavailable(matchedMed, candidateSubs)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.95f),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "💊 ${stringResource(R.string.issueRx)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Patient: ${patient?.name ?: patientNameFallback} (${patient?.villageName ?: "Rural PHC"})",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xs))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    // Current Medicines in this Prescription
                    item {
                        Text(
                            text = "Prescribed Medicines (${medicinesList.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                    }

                    itemsIndexed(medicinesList) { index, med ->
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = med.name,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_OnBackground
                                        )
                                        Text(
                                            text = "${med.dosage} · ${med.frequency} for ${med.duration}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                        Text(
                                            text = "Qty to dispense: ${med.quantity} units",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = VS_PrimaryContainer
                                        )
                                    }
                                    IconButton(onClick = { medicinesList.removeAt(index) }) {
                                        Text(text = "🗑️", fontSize = 16.sp)
                                    }
                                }

                                // Status Badge
                                if (med.hasAlternativeAvailable) {
                                    Surface(
                                        shape = PillShape,
                                        color = VS_ErrorContainer,
                                        border = BorderStroke(1.dp, VS_Error)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.outOfStockNearPatientWarning),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = VS_OnErrorContainer,
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        shape = PillShape,
                                        color = VS_SuccessContainer
                                    ) {
                                        Text(
                                            text = stringResource(R.string.likelyAvailableNearPatient),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = VS_OnSuccessContainer,
                                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Add Medicine Form
                    item {
                        VitalSenseCard(
                            backgroundColor = VS_SurfaceVariant,
                            border = BorderStroke(1.dp, VS_Outline)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                Text(
                                    text = stringResource(R.string.addAnotherMedicineBtn),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )

                                OutlinedTextField(
                                    value = newMedName,
                                    onValueChange = { newMedName = it },
                                    label = { Text(stringResource(R.string.medicineName), color = VS_OnSurfaceVariant) },
                                    placeholder = { Text(stringResource(R.string.medicineNamePlaceholder), color = VS_OnSurfaceVariant) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = InputShape,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = VS_Surface,
                                        unfocusedContainerColor = VS_Surface,
                                        focusedBorderColor = VS_Primary,
                                        unfocusedBorderColor = VS_Outline,
                                        focusedTextColor = VS_OnBackground,
                                        unfocusedTextColor = VS_OnBackground
                                    )
                                )

                                // Live Availability Indicator
                                when (val state = availabilityState) {
                                    is AvailabilityCheckState.Checking -> {
                                        Text(
                                            text = "🔍 Checking nearby pharmacy availability near ${patient?.villageName ?: "patient"}...",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }

                                    is AvailabilityCheckState.Available -> {
                                        Surface(
                                            shape = PillShape,
                                            color = VS_SuccessContainer
                                        ) {
                                            Text(
                                                text = "✅ Likely available at ${state.count} nearby pharmacies",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = VS_OnSuccessContainer,
                                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    is AvailabilityCheckState.Unavailable -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(InputShape)
                                                .background(VS_ErrorContainer)
                                                .padding(Spacing.xs)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = stringResource(R.string.notFoundNearPatientLocation),
                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = VS_OnErrorContainer
                                                    )
                                                    Text(
                                                        text = "Likely out of stock at nearby stores in ${patient?.villageName ?: "this area"}.",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = VS_OnSurfaceVariant
                                                    )
                                                }

                                                if (state.candidateSubstitutes.isNotEmpty()) {
                                                    Button(
                                                        onClick = { showAlternativeSuggestions = !showAlternativeSuggestions },
                                                        shape = PillShape,
                                                        colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = if (showAlternativeSuggestions) "Hide Options" else "Suggest Alternative",
                                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                            color = VS_OnBackground
                                                        )
                                                    }
                                                }
                                            }

                                            // Suggest Alternative Panel
                                            if (showAlternativeSuggestions && state.candidateSubstitutes.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(Spacing.xs))
                                                Text(
                                                    text = "Clinically Interchangeable Alternatives (${state.candidateSubstitutes.size}):",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = VS_OnBackground
                                                )

                                                state.candidateSubstitutes.forEach { (substitute, subInStockCount) ->
                                                    Card(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 3.dp),
                                                        shape = InputShape,
                                                        colors = CardDefaults.cardColors(containerColor = VS_SurfaceVariant),
                                                        border = BorderStroke(1.dp, VS_Outline)
                                                    ) {
                                                        Column(modifier = Modifier.padding(Spacing.xs)) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Column(modifier = Modifier.weight(1f)) {
                                                                    Text(
                                                                        text = substitute.name,
                                                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                                        color = VS_OnBackground
                                                                    )
                                                                    Text(
                                                                        text = "Generic: ${substitute.genericName}",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        color = VS_OnSurfaceVariant
                                                                    )
                                                                    Text(
                                                                        text = "Class: ${substitute.therapeuticClass} · ${substitute.commonUseDescription}",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        color = VS_PrimaryContainer
                                                                    )
                                                                    Text(
                                                                        text = if (subInStockCount > 0) "✅ Likely in stock at $subInStockCount nearby stores" else "⚠️ Limited local stock",
                                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                                        color = if (subInStockCount > 0) VS_OnSuccessContainer else VS_OnErrorContainer
                                                                    )
                                                                }

                                                                Button(
                                                                    onClick = {
                                                                        newMedName = substitute.name
                                                                        if (substitute.commonDosage.isNotBlank()) {
                                                                            newMedDosage = substitute.commonDosage
                                                                        }
                                                                        showAlternativeSuggestions = false
                                                                    },
                                                                    shape = PillShape,
                                                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                                                ) {
                                                                    Text(
                                                                        text = stringResource(R.string.swapMedicineBtn),
                                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                                        color = VS_OnBackground
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                Text(
                                                    text = stringResource(R.string.medicineSuggestionDisclaimer),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = VS_OnSurfaceVariant,
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        }
                                    }

                                    is AvailabilityCheckState.Idle -> { /* No check yet */ }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                                ) {
                                    OutlinedTextField(
                                        value = newMedDosage,
                                        onValueChange = { newMedDosage = it },
                                        label = { Text(stringResource(R.string.dosageLabel), color = VS_OnSurfaceVariant) },
                                        modifier = Modifier.weight(1f),
                                        shape = InputShape,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = VS_Surface,
                                            unfocusedContainerColor = VS_Surface,
                                            focusedBorderColor = VS_Primary,
                                            unfocusedBorderColor = VS_Outline,
                                            focusedTextColor = VS_OnBackground,
                                            unfocusedTextColor = VS_OnBackground
                                        )
                                    )
                                    OutlinedTextField(
                                        value = newMedQuantity,
                                        onValueChange = { newMedQuantity = it },
                                        label = { Text(stringResource(R.string.quantityShort), color = VS_OnSurfaceVariant) },
                                        modifier = Modifier.weight(0.7f),
                                        shape = InputShape,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = VS_Surface,
                                            unfocusedContainerColor = VS_Surface,
                                            focusedBorderColor = VS_Primary,
                                            unfocusedBorderColor = VS_Outline,
                                            focusedTextColor = VS_OnBackground,
                                            unfocusedTextColor = VS_OnBackground
                                        )
                                    )
                                }

                                OutlinedTextField(
                                    value = newMedFrequency,
                                    onValueChange = { newMedFrequency = it },
                                    label = { Text(stringResource(R.string.frequencyAndTiming), color = VS_OnSurfaceVariant) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = InputShape,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = VS_Surface,
                                        unfocusedContainerColor = VS_Surface,
                                        focusedBorderColor = VS_Primary,
                                        unfocusedBorderColor = VS_Outline,
                                        focusedTextColor = VS_OnBackground,
                                        unfocusedTextColor = VS_OnBackground
                                    )
                                )

                                OutlinedTextField(
                                    value = newMedDuration,
                                    onValueChange = { newMedDuration = it },
                                    label = { Text(stringResource(R.string.durationLabel), color = VS_OnSurfaceVariant) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = InputShape,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = VS_Surface,
                                        unfocusedContainerColor = VS_Surface,
                                        focusedBorderColor = VS_Primary,
                                        unfocusedBorderColor = VS_Outline,
                                        focusedTextColor = VS_OnBackground,
                                        unfocusedTextColor = VS_OnBackground
                                    )
                                )

                                Button(
                                    onClick = {
                                        if (newMedName.isNotBlank()) {
                                            val isUnavailable = availabilityState is AvailabilityCheckState.Unavailable
                                            val matched = medicineRepo.findMedicineByName(newMedName.trim())
                                            medicinesList.add(
                                                PrescribedMedicine(
                                                    name = newMedName.trim(),
                                                    dosage = newMedDosage.trim(),
                                                    frequency = newMedFrequency.trim(),
                                                    duration = newMedDuration.trim(),
                                                    quantity = newMedQuantity.toIntOrNull() ?: 10,
                                                    medicineId = matched?.id,
                                                    hasAlternativeAvailable = isUnavailable
                                                )
                                            )
                                            newMedName = ""
                                            availabilityState = AvailabilityCheckState.Idle
                                            showAlternativeSuggestions = false
                                        }
                                    },
                                    shape = PillShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(stringResource(R.string.addToPrescriptionBtn), color = VS_OnBackground, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    // Special Instructions
                    item {
                        Text(
                            text = stringResource(R.string.dietaryFollowUpInstructions),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text(stringResource(R.string.instructionsPatientAsha), color = VS_OnSurfaceVariant) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
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
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.sm))

                // Issue Button
                Button(
                    onClick = {
                        onIssuePrescription(medicinesList.toList(), instructions)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VS_Primary,
                        contentColor = VS_OnBackground
                    ),
                    enabled = medicinesList.isNotEmpty()
                ) {
                    Text(
                        text = "${stringResource(R.string.savePrescriptionRecord)} ✓",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
