package com.vitalsense.app.core.state

import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateHolder @Inject constructor() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.PATIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _activePatient = MutableStateFlow(
        Patient("demo_patient_1", "Ramesh Kumar", 45, "Male", "9811100000", "vil_1", "Rampur", "asha_1", "Sita Devi", SeverityLevel.MODERATE, "Fever", "Today", "Tomorrow", "9811122222", null)
    )
    val activePatient: StateFlow<Patient> = _activePatient.asStateFlow()

    private val _activeAsha = MutableStateFlow(
        AshaWorker("demo_asha_1", "Sita Devi", "ASHA-7701", "9988776655", listOf("Rampur", "Shantipur"), 45, 0)
    )
    val activeAsha: StateFlow<AshaWorker> = _activeAsha.asStateFlow()

    private val _activeDoctor = MutableStateFlow(
        Doctor(
            id = "demo_doc_1", 
            name = "Dr. Rajesh Sharma", 
            specialty = DoctorSpecialty.GENERAL_PHYSICIAN, 
            qualification = "MBBS, MD", 
            hospitalName = "District Hospital", 
            distanceKm = 5.0, 
            phone = "9876543210", 
            availableDays = "Mon-Sat",
            departmentId = "dept_general_medicine",
            departmentName = "General Medicine"
        )
    )
    val activeDoctor: StateFlow<Doctor> = _activeDoctor.asStateFlow()

    private val _activeProxyPatient = MutableStateFlow<Patient?>(null)
    val activeProxyPatient: StateFlow<Patient?> = _activeProxyPatient.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    fun login(role: UserRole) {
        _currentRole.value = role
        _isLoggedIn.value = true
    }

    fun loginAsPatient(patient: Patient) {
        _activePatient.value = patient
        _currentRole.value = UserRole.PATIENT
        _isLoggedIn.value = true
    }

    fun loginAsAsha(asha: AshaWorker) {
        _activeAsha.value = asha
        _currentRole.value = UserRole.ASHA
        _isLoggedIn.value = true
    }

    fun loginAsDoctor(doctor: Doctor) {
        _activeDoctor.value = doctor
        _currentRole.value = UserRole.DOCTOR
        _isLoggedIn.value = true
    }

    fun loginAsAdmin() {
        _currentRole.value = UserRole.ADMIN
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
        _activeProxyPatient.value = null
    }

    fun switchRole(newRole: UserRole) {
        _currentRole.value = newRole
    }

    fun selectPatient(patient: Patient) {
        _activePatient.value = patient
    }

    fun selectAsha(asha: AshaWorker) {
        _activeAsha.value = asha
    }

    fun selectDoctor(doctor: Doctor) {
        _activeDoctor.value = doctor
    }

    fun setProxyPatient(patient: Patient?) {
        _activeProxyPatient.value = patient
    }

    fun clearProxy() {
        _activeProxyPatient.value = null
    }

    fun toggleOffline() {
        _isOffline.value = !_isOffline.value
    }
}

