package com.vitalsense.app.feature.doctor
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Prescription
import com.vitalsense.app.core.data.model.Doctor
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.ui.components.VitalSenseButton
import java.util.UUID
@Composable
fun PrescriptionCreatorScreen(doctor: Doctor, condition: ConditionRecord, onSave: (Prescription) -> Unit) {
    var medName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Write Prescription for ${condition.patientName}", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text(stringResource(R.string.medicineName)) })
        OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text(stringResource(R.string.dosageLabel)) })
        VitalSenseButton("Issue Prescription", onClick = {
            onSave(Prescription(id = UUID.randomUUID().toString(), patientId = condition.patientId, patientName = condition.patientName, doctorId = doctor.id, doctorName = doctor.name, doctorSpecialty = doctor.specialty.name, timestamp = System.currentTimeMillis(), dateFormatted = "Today", medicines = emptyList(), instructions = "$medName - $dosage", isOcrExtracted = false))
        })
    }
}