package com.vitalsense.app.feature.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import com.vitalsense.app.core.state.AppStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val repository: VitalSenseRepository,
    private val appStateHolder: AppStateHolder
) : ViewModel() {

    val activeDoctor: StateFlow<Doctor> = appStateHolder.activeDoctor

    // Strictly scoped cases: Only cases routed to active doctor's specialty or assigned directly
    val scopedCases: StateFlow<List<ConditionRecord>> = activeDoctor.flatMapLatest { doctor ->
        repository.getCasesForDoctor(doctor.id, doctor.specialty)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Scoped appointments for active doctor
    val appointments: StateFlow<List<Appointment>> = activeDoctor.flatMapLatest { doctor ->
        repository.getAppointmentsForDoctor(doctor.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dispensary stock for availability checks
    val dispensaryStock: StateFlow<List<DispensaryItem>> = repository.getDispensaryStock()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected case for Case Detail View (§6.4)
    private val _selectedCase = MutableStateFlow<ConditionRecord?>(null)
    val selectedCase: StateFlow<ConditionRecord?> = _selectedCase.asStateFlow()

    // Prior prescriptions for the currently viewed patient
    val patientPrescriptions: StateFlow<List<Prescription>> = _selectedCase.flatMapLatest { case ->
        if (case != null) repository.getPrescriptionsForPatient(case.patientId)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // View-Only patient profile / health card
    val patientProfile: StateFlow<Patient?> = _selectedCase.flatMapLatest { case ->
        if (case != null) repository.getPatientById(case.patientId)
        else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Doctor Case Analytics computed from scopedCases
    val caseAnalytics: StateFlow<DoctorCaseAnalytics> = scopedCases.map { cases ->
        DoctorCaseAnalytics(
            totalCases = cases.size,
            lowCount = cases.count { it.severity == SeverityLevel.LOW },
            moderateCount = cases.count { it.severity == SeverityLevel.MODERATE },
            highCount = cases.count { it.severity == SeverityLevel.HIGH },
            severeCount = cases.count { it.severity == SeverityLevel.SEVERE },
            respondedCount = cases.count { it.status == CaseStatus.RESPONDED || it.status == CaseStatus.CLOSED },
            pendingCount = cases.count { it.status == CaseStatus.PENDING_REVIEW },
            referredCount = cases.count { it.status == CaseStatus.REFERRED }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DoctorCaseAnalytics(0, 0, 0, 0, 0, 0, 0, 0))

    // Patient Medical History for selected case
    val patientMedicalHistory: StateFlow<List<MedicalHistoryEntry>> = _selectedCase.flatMapLatest { case ->
        if (case != null) repository.getMedicalHistoryForPatient(case.patientId)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCase(record: ConditionRecord) {
        _selectedCase.value = record
    }

    fun clearSelectedCase() {
        _selectedCase.value = null
    }

    fun sendNotice(notice: BroadcastNotice) {
        viewModelScope.launch {
            repository.sendNotice(notice)
        }
    }

    /**
     * Submit free-text medical response attached to the case (§2.2, §4.2, §4.4)
     */
    fun submitMedicalResponse(
        caseId: String,
        responseText: String,
        privateNotes: String? = null,
        newStatus: CaseStatus = CaseStatus.RESPONDED
    ) {
        val doctor = activeDoctor.value
        viewModelScope.launch {
            repository.respondToCase(
                caseId = caseId,
                doctorId = doctor.id,
                doctorName = doctor.name,
                responseText = responseText,
                privateNotes = privateNotes,
                newStatus = newStatus
            )
            // Update selected case in memory
            _selectedCase.update { current ->
                if (current?.id == caseId) {
                    current.copy(
                        status = newStatus,
                        doctorResponse = responseText,
                        doctorResponseTimestamp = System.currentTimeMillis(),
                        doctorResponseDoctorName = doctor.name,
                        privateDoctorNotes = privateNotes ?: current.privateDoctorNotes
                    )
                } else current
            }
        }
    }

    /**
     * Re-route / refer case to another specialist (§4.3)
     */
    fun referCase(
        caseId: String,
        targetSpecialty: DoctorSpecialty,
        referralNotes: String
    ) {
        val doctor = activeDoctor.value
        viewModelScope.launch {
            repository.referCaseToSpecialist(
                caseId = caseId,
                referringDoctor = doctor,
                targetSpecialty = targetSpecialty,
                referralNotes = referralNotes
            )
            _selectedCase.update { current ->
                if (current?.id == caseId) {
                    current.copy(
                        status = CaseStatus.REFERRED,
                        requestedDoctorType = targetSpecialty,
                        referredByDoctorId = doctor.id,
                        referredByDoctorName = doctor.name,
                        referralNotes = referralNotes
                    )
                } else current
            }
        }
    }

    /**
     * Issue a structured prescription tied to a case (§2.3, §5)
     */
    fun issuePrescription(
        caseId: String,
        patientId: String,
        patientName: String,
        medicines: List<PrescribedMedicine>,
        instructions: String
    ) {
        val doctor = activeDoctor.value
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val newPrescription = Prescription(
            id = "rx_${System.currentTimeMillis()}",
            caseId = caseId,
            patientId = patientId,
            patientName = patientName,
            doctorId = doctor.id,
            doctorName = doctor.name,
            doctorSpecialty = doctor.specialty.displayName,
            timestamp = System.currentTimeMillis(),
            dateFormatted = dateFormat.format(Date()),
            medicines = medicines,
            instructions = instructions,
            isOcrExtracted = false
        )

        val historyEntry = MedicalHistoryEntry(
            id = "mh_${System.currentTimeMillis()}",
            patientId = patientId,
            type = MedicalHistoryType.MEDICATION,
            title = "Prescription Issued",
            details = "Prescribed ${medicines.size} medicines. Instructions: $instructions",
            severity = null,
            doctorId = doctor.id,
            doctorName = doctor.name,
            caseId = caseId,
            prescriptionId = newPrescription.id,
            timestamp = System.currentTimeMillis(),
            dateFormatted = dateFormat.format(Date())
        )

        viewModelScope.launch {
            repository.savePrescription(newPrescription)
            repository.addMedicalHistoryEntry(historyEntry)
        }
    }

    /**
     * Doctor proposes an appointment slot to the patient (§2.4)
     */
    fun proposeAppointment(
        patientId: String,
        patientName: String,
        dateFormatted: String,
        timeSlot: String
    ) {
        val doctor = activeDoctor.value
        val appointment = Appointment(
            id = "appt_${System.currentTimeMillis()}",
            patientId = patientId,
            patientName = patientName,
            doctorId = doctor.id,
            doctorName = doctor.name,
            doctorSpecialty = doctor.specialty.displayName,
            dateFormatted = dateFormatted,
            timeSlot = timeSlot,
            status = "Pending Patient Confirmation",
            proposedBy = UserRole.DOCTOR
        )

        viewModelScope.launch {
            repository.scheduleAppointment(appointment)
        }
    }

    /**
     * Accept incoming patient appointment (§2.4)
     */
    fun acceptAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, "Confirmed")
        }
    }

    /**
     * Decline incoming appointment (§2.4)
     */
    fun declineAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, "Declined")
        }
    }

    /**
     * Reschedule appointment with proposed new slot (§2.4)
     */
    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(
                appointmentId = appointmentId,
                newStatus = "Rescheduled by Doctor ($newDate, $newTime)"
            )
        }
    }

    // --- Live Queue & Day Slots ---

    val todayFormatted: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todaysQueue: StateFlow<List<QueueEntry>> = activeDoctor.flatMapLatest { doctor ->
        repository.observeDoctorQueue(doctor.id, todayFormatted)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doctorDaySlots: StateFlow<List<DoctorDaySlotConfig>> = activeDoctor.flatMapLatest { doctor ->
        repository.observeDoctorSlots(doctor.id, todayFormatted)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todaySlotConfig: StateFlow<DoctorDaySlotConfig?> = doctorDaySlots.map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeConsultation: StateFlow<QueueEntry?> = todaysQueue.map { list ->
        list.firstOrNull { it.status == QueueEntryStatus.IN_CONSULTATION }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val calledEntry: StateFlow<QueueEntry?> = todaysQueue.map { list ->
        list.firstOrNull { it.status == QueueEntryStatus.CALLED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val waitingEntries: StateFlow<List<QueueEntry>> = todaysQueue.map { list ->
        list.filter { it.status == QueueEntryStatus.WAITING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedEntries: StateFlow<List<QueueEntry>> = todaysQueue.map { list ->
        list.filter { it.status == QueueEntryStatus.COMPLETED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun callNext() {
        val doctor = activeDoctor.value
        viewModelScope.launch {
            repository.callNext(doctor.id, todayFormatted)
        }
    }

    fun startConsultation(entryId: String) {
        viewModelScope.launch {
            repository.startConsultation(entryId)
        }
    }

    fun completeConsultation(entryId: String, outcomeNotes: String?) {
        viewModelScope.launch {
            repository.completeConsultation(entryId, outcomeNotes)
        }
    }

    fun markNoShow(entryId: String) {
        viewModelScope.launch {
            repository.markNoShow(entryId)
        }
    }

    fun skipEntry(entryId: String) {
        viewModelScope.launch {
            repository.skipEntry(entryId)
        }
    }

    fun prioritizeEntry(entryId: String) {
        viewModelScope.launch {
            repository.prioritizeEntry(entryId)
        }
    }

    fun addWalkInPatient(patientId: String, patientName: String) {
        val doctor = activeDoctor.value
        viewModelScope.launch {
            repository.joinWalkInQueue(doctor.id, doctor.name, patientId, patientName)
        }
    }

    fun updateSlotConfig(capacity: Int, isWalkInOpen: Boolean, startTime: String = "09:00", endTime: String = "17:00") {
        val doctor = activeDoctor.value
        val config = DoctorDaySlotConfig(
            id = "slot_${doctor.id}_$todayFormatted",
            doctorId = doctor.id,
            dateFormatted = todayFormatted,
            startTime = startTime,
            endTime = endTime,
            capacity = capacity,
            isWalkInOpen = isWalkInOpen
        )
        viewModelScope.launch {
            repository.defineDoctorSlot(config)
        }
    }

    // --- Doctor-to-Doctor Specialist Referrals ---
    val allReferrals: StateFlow<List<Referral>> = repository.getAllReferrals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doctorReferrals: StateFlow<List<Referral>> = activeDoctor.flatMapLatest { doctor ->
        repository.getReferralsForDoctorOrSpecialty(doctor.id, doctor.specialty.displayName)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createReferral(referral: Referral) {
        viewModelScope.launch {
            repository.createReferral(referral)
        }
    }

    fun acceptReferral(referralId: String) {
        viewModelScope.launch {
            val doctor = activeDoctor.value
            val ref = allReferrals.value.find { it.id == referralId } ?: return@launch
            val updated = ref.copy(
                status = ReferralStatus.ACCEPTED,
                targetDoctorId = doctor.id,
                targetDoctorName = doctor.name,
                respondedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                statusHistory = ref.statusHistory + ReferralStatusHistory(
                    status = ReferralStatus.ACCEPTED,
                    changedByUserId = doctor.id,
                    note = "Accepted by Specialist"
                )
            )
            repository.updateReferral(updated)
        }
    }

    fun declineReferral(referralId: String, reason: String, suggestedReroute: String?) {
        viewModelScope.launch {
            val doctor = activeDoctor.value
            val ref = allReferrals.value.find { it.id == referralId } ?: return@launch
            val updated = ref.copy(
                status = ReferralStatus.DECLINED,
                declineReason = reason,
                suggestedSpecialtyOrDoctor = suggestedReroute,
                respondedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                statusHistory = ref.statusHistory + ReferralStatusHistory(
                    status = ReferralStatus.DECLINED,
                    changedByUserId = doctor.id,
                    note = "Declined: $reason"
                )
            )
            repository.updateReferral(updated)
        }
    }

    fun requestMoreInfo(referralId: String, infoNote: String) {
        viewModelScope.launch {
            val doctor = activeDoctor.value
            val ref = allReferrals.value.find { it.id == referralId } ?: return@launch
            val updated = ref.copy(
                status = ReferralStatus.INFO_REQUESTED,
                infoRequestNote = infoNote,
                respondedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                statusHistory = ref.statusHistory + ReferralStatusHistory(
                    status = ReferralStatus.INFO_REQUESTED,
                    changedByUserId = doctor.id,
                    note = infoNote
                )
            )
            repository.updateReferral(updated)
        }
    }

    fun submitSpecialistFindings(
        referralId: String,
        findings: String,
        recommendations: String,
        followUpNeeded: Boolean
    ) {
        viewModelScope.launch {
            val doctor = activeDoctor.value
            val ref = allReferrals.value.find { it.id == referralId } ?: return@launch
            val newStatus = if (followUpNeeded) ReferralStatus.FOLLOW_UP else ReferralStatus.CONSULTATION_COMPLETED
            val updated = ref.copy(
                status = newStatus,
                specialistFindings = findings,
                specialistRecommendations = recommendations,
                specialistFollowUpNeeded = followUpNeeded,
                completedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                statusHistory = ref.statusHistory + ReferralStatusHistory(
                    status = newStatus,
                    changedByUserId = doctor.id,
                    note = "Specialist findings submitted"
                )
            )
            repository.updateReferral(updated)
        }
    }
}
