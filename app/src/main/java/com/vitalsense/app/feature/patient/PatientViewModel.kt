package com.vitalsense.app.feature.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalsense.app.core.data.model.ConditionCategory
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.data.model.DoctorSpecialty
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val repository: VitalSenseRepository
) : ViewModel() {

    private val _uiEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun getPatientFullHistory(patientId: String) = repository.getPatientFullHistory(patientId)

    fun logMentalWellness(
        patient: Patient,
        moodNotes: String,
        severityLevel: SeverityLevel,
        isProxy: Boolean
    ) {
        viewModelScope.launch {
            try {
                val record = ConditionRecord(
                    id = UUID.randomUUID().toString(),
                    patientId = patient.id,
                    patientName = patient.name,
                    villageId = patient.villageId,
                    villageName = patient.villageName,
                    category = ConditionCategory.MENTAL_HEALTH,
                    severity = severityLevel,
                    requestedDoctorType = DoctorSpecialty.PSYCHOLOGIST,
                    notes = moodNotes,
                    timestamp = System.currentTimeMillis(),
                    ashaProxyLogged = isProxy,
                    syncState = com.vitalsense.app.core.data.model.SyncState.PENDING
                )
                repository.logCondition(record)
                _uiEvent.emit("Mental wellness check-in saved successfully")
            } catch (e: Exception) {
                _uiEvent.emit("Error saving check-in: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}
