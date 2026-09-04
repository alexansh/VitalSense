package com.vitalsense.app.core.data.repository

import com.google.gson.Gson
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.local.entity.*
import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.data.remote.FirestoreDataSource
import com.vitalsense.app.core.network.NetworkMonitor
import com.vitalsense.app.core.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitalSenseRepositoryImpl @Inject constructor(
    private val database: VitalSenseDatabase,
    private val firestoreDataSource: FirestoreDataSource,
    private val syncManager: SyncManager,
    private val networkMonitor: NetworkMonitor
) : VitalSenseRepository {

    private val gson = Gson()
    private val dao = database.vitalSenseDao()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("VitalSenseRepo", "Recovered from background coroutine exception: ${throwable.message}", throwable)
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)
    private val activeQueueJobs = ConcurrentHashMap<String, Job>()
    private val activeSlotJobs = ConcurrentHashMap<String, Job>()

    // In-memory reactive state caches for instant UI response (zero lag on stage)
    private val _villages = MutableStateFlow(SeedDataProvider.initialVillages)
    private val _patients = MutableStateFlow(SeedDataProvider.initialPatients)
    private val _ashaWorkers = MutableStateFlow(SeedDataProvider.initialAshaWorkers)
    private val _doctors = MutableStateFlow(SeedDataProvider.initialDoctors)
    private val _conditions = MutableStateFlow(SeedDataProvider.initialConditionRecords)
    private val _prescriptions = MutableStateFlow(SeedDataProvider.initialPrescriptions)
    private val _appointments = MutableStateFlow(SeedDataProvider.initialAppointments)
    private val _notices = MutableStateFlow(SeedDataProvider.initialNotices)
    private val _dispensary = MutableStateFlow(SeedDataProvider.initialDispensaryItems)
    private val _schemes = MutableStateFlow(SeedDataProvider.initialSchemes)
    private val _immunizations = MutableStateFlow(SeedDataProvider.initialImmunizations)
    private val _dailyRounds = MutableStateFlow(SeedDataProvider.initialDailyRounds)
    private val _ashaMedicines = MutableStateFlow(SeedDataProvider.initialAshaMedicines)
    private val _diseaseTrends = MutableStateFlow(SeedDataProvider.initialDiseaseTrendRecords)
    private val _labReports = MutableStateFlow(SeedDataProvider.initialLabReports)
    private val _opdTokens = MutableStateFlow(SeedDataProvider.initialOpdTokens)
    private val _medicalCertificates = MutableStateFlow(SeedDataProvider.initialMedicalCertificates)
    private val _bloodStock = MutableStateFlow(SeedDataProvider.initialBloodStock)
    private val _familyMembers = MutableStateFlow(SeedDataProvider.initialFamilyMembers)
    private val _ipdBeds = MutableStateFlow(SeedDataProvider.initialIpdBeds)
    private val _otSurgeryBookings = MutableStateFlow(SeedDataProvider.initialOtSurgeryBookings)
    private val _externalReferrals = MutableStateFlow(SeedDataProvider.initialExternalReferrals)
    private val _bioMedicalEquipment = MutableStateFlow(SeedDataProvider.initialBioMedicalEquipment)
    private val _queueEntries = MutableStateFlow<List<QueueEntry>>(emptyList())
    private val _doctorSlots = MutableStateFlow<List<DoctorDaySlotConfig>>(emptyList())

    init {
        // 1. Pre-seed local Room database on first launch
        scope.launch {
            try {
                dao.insertVillages(SeedDataProvider.getVillageEntities())
                dao.insertAshaWorkers(SeedDataProvider.getAshaEntities())
                dao.insertDoctors(SeedDataProvider.getDoctorEntities())
                dao.insertPatients(SeedDataProvider.getPatientEntities())
                dao.insertConditionRecords(SeedDataProvider.getConditionEntities())
                dao.insertPrescriptions(SeedDataProvider.getPrescriptionEntities())
                dao.insertAppointments(SeedDataProvider.getAppointmentEntities())
                dao.insertDispensaryItems(SeedDataProvider.getDispensaryEntities())
                dao.insertNotices(SeedDataProvider.getNoticeEntities())
                dao.insertSchemes(SeedDataProvider.getSchemeEntities())
                
                SeedDataProvider.getImmunizationEntities().forEach { dao.insertImmunizationRecord(it) }
                SeedDataProvider.getDailyRoundEntities().forEach { dao.insertDailyRound(it) }
                SeedDataProvider.getAshaMedicineEntities().forEach { dao.insertAshaMedicine(it) }
                SeedDataProvider.getDiseaseTrendRecordEntities().forEach { dao.insertDiseaseTrendRecord(it) }
                SeedDataProvider.getLabReportEntities().forEach { dao.insertLabReport(it) }
                SeedDataProvider.getOpdTokenEntities().forEach { dao.insertOpdToken(it) }
                SeedDataProvider.getMedicalCertificateEntities().forEach { dao.insertMedicalCertificate(it) }
                SeedDataProvider.getBloodStockEntities().forEach { dao.insertBloodStockItem(it) }
                SeedDataProvider.getIpdBedEntities().forEach { dao.insertIpdBed(it) }
                SeedDataProvider.getOtSurgeryBookingEntities().forEach { dao.insertOtSurgeryBooking(it) }
                SeedDataProvider.getExternalReferralEntities().forEach { dao.insertExternalReferral(it) }
                SeedDataProvider.getBioMedicalEquipmentEntities().forEach { dao.insertBioMedicalEquipment(it) }
                SeedDataProvider.getReferralEntities().forEach { dao.insertReferral(it) }
            } catch (e: Exception) {
                // Fallback to in-memory state
            }
        }

        // 2. Start real-time Firestore listeners when online
        scope.launch {
            try {
                firestoreDataSource.getConditionRecordsStream().collect { remoteRecords ->
                    if (remoteRecords.isNotEmpty()) {
                        _conditions.update { remoteRecords }
                    }
                }
            } catch (e: Exception) {
                // Offline fallback
            }
        }

        scope.launch {
            try {
                firestoreDataSource.getBroadcastNoticesStream().collect { remoteNotices ->
                    if (remoteNotices.isNotEmpty()) {
                        _notices.update { remoteNotices }
                    }
                }
            } catch (e: Exception) {
                // Offline fallback
            }
        }
    }

    // --- Villages ---
    override fun getVillages(): Flow<List<Village>> = _villages.asStateFlow()

    override suspend fun addVillage(village: Village) {
        _villages.update { it + village }
        scope.launch {
            dao.insertVillages(listOf(
                VillageEntity(
                    village.id, village.name, village.district, village.state,
                    village.population, village.latitude, village.longitude,
                    village.activeCases, village.highRiskCount
                )
            ))
        }
    }

    // --- Patients ---
    override fun getPatients(): Flow<List<Patient>> = _patients.asStateFlow()

    override fun getPatientById(id: String): Flow<Patient?> = _patients.map { list ->
        list.find { it.id == id }
    }

    override fun getPatientsForAsha(ashaId: String): Flow<List<Patient>> = _patients.map { list ->
        list.filter { it.ashaWorkerId == ashaId }
    }

    override suspend fun savePatient(patient: Patient) {
        // 1. Instant local state update
        _patients.update { list ->
            val index = list.indexOfFirst { it.id == patient.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, patient) }
            } else {
                list + patient
            }
        }

        // 2. Persist to Room SQLite & Outbox Queue
        scope.launch {
            dao.insertPatient(
                PatientEntity(
                    patient.id, patient.name, patient.age, patient.gender, patient.phone,
                    patient.villageId, patient.villageName, patient.ashaWorkerId,
                    patient.ashaWorkerName, patient.currentRiskLevel, patient.lastCondition,
                    patient.lastVisitDate, patient.nextAppointmentDate, patient.emergencyContact,
                    patient.profilePhotoUrl
                )
            )

            val outboxId = "outbox_patient_${patient.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "PATIENT",
                    entityId = patient.id,
                    payloadJson = gson.toJson(patient),
                    timestamp = System.currentTimeMillis()
                )
            )

            // 3. Remote Cloud Firestore sync attempt
            try {
                firestoreDataSource.uploadPatient(patient)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                // Offline: remains saved locally in Room; WorkManager flushes upon network return
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- ASHA Workers ---
    override fun getAshaWorkers(): Flow<List<AshaWorker>> = _ashaWorkers.asStateFlow()

    override fun getAshaWorkerById(id: String): Flow<AshaWorker?> = _ashaWorkers.map { list ->
        list.find { it.id == id || it.ashaUniqueId == id }
    }

    // --- Doctors ---
    override fun getDoctors(): Flow<List<Doctor>> = _doctors.asStateFlow()

    override fun getDoctorById(id: String): Flow<Doctor?> = _doctors.map { list ->
        list.find { it.id == id }
    }

    // --- Condition Records ---
    override fun getConditionRecords(): Flow<List<ConditionRecord>> = _conditions.asStateFlow()

    override fun getConditionRecordsForPatient(patientId: String): Flow<List<ConditionRecord>> = _conditions.map { list ->
        list.filter { it.patientId == patientId }
    }

    override fun getCasesForDoctor(doctorId: String, specialty: DoctorSpecialty): Flow<List<ConditionRecord>> = _conditions.map { list ->
        list.filter { record ->
            record.requestedDoctorType == specialty || 
            record.assignedDoctorId == doctorId ||
            (specialty == DoctorSpecialty.MAXILLOFACIAL_RECONSTRUCTIVE_SURGEON && (
                record.requestedDoctorType == DoctorSpecialty.ORTHOPLASTIC_SURGEON ||
                record.requestedDoctorType == DoctorSpecialty.ORTHOGNATHIC_SURGEON ||
                record.requestedDoctorType == DoctorSpecialty.ONCOGENIC_SURGEON ||
                record.requestedDoctorType == DoctorSpecialty.TRAUMA_SURGEON ||
                record.requestedDoctorType == DoctorSpecialty.ORAL_MAXILLOFACIAL_SURGEON ||
                record.requestedDoctorType == DoctorSpecialty.COSMETIC_SURGEON
            ))
        }.sortedWith(
            compareBy<ConditionRecord> { record ->
                when (record.severity) {
                    SeverityLevel.SEVERE -> 0
                    SeverityLevel.HIGH -> 1
                    SeverityLevel.MODERATE -> 2
                    SeverityLevel.LOW -> 3
                }
            }.thenByDescending { it.timestamp }
        )
    }

    override suspend fun logCondition(record: ConditionRecord) {
        // 1. Instant in-memory update
        _conditions.update { listOf(record) + it }

        // Update patient's current risk level
        _patients.update { patients ->
            patients.map { p ->
                if (p.id == record.patientId) {
                    p.copy(
                        currentRiskLevel = record.severity,
                        lastCondition = record.notes.ifBlank { "${record.category.displayName} (${record.severity.displayName})" },
                        lastVisitDate = "Today"
                    )
                } else p
            }
        }

        // Update village outbreak count
        _villages.update { villages ->
            villages.map { v ->
                if (v.id == record.villageId) {
                    v.copy(
                        activeCases = v.activeCases + 1,
                        highRiskCount = if (record.severity == SeverityLevel.HIGH || record.severity == SeverityLevel.SEVERE) v.highRiskCount + 1 else v.highRiskCount
                    )
                } else v
            }
        }

        // 2. Persist to Room & Cloud Firestore via Durable Outbox
        scope.launch {
            dao.insertConditionRecord(
                ConditionRecordEntity(
                    record.id, record.patientId, record.patientName, record.villageId,
                    record.villageName, record.category, record.severity,
                    record.requestedDoctorType, record.notes, record.timestamp,
                    record.ashaProxyLogged, record.status, record.assignedDoctorId,
                    record.assignedDoctorName, record.doctorResponse, record.doctorResponseTimestamp,
                    record.doctorResponseDoctorName, record.privateDoctorNotes,
                    record.referredByDoctorId, record.referredByDoctorName,
                    record.referralNotes, isPendingSync = true
                )
            )

            // Queue in Outbox
            val outboxId = "outbox_cond_${record.id}"
            dao.insertOutboxRecord(
                com.vitalsense.app.core.data.local.entity.OutboxEntity(
                    id = outboxId,
                    actionType = "CONDITION_RECORD",
                    entityId = record.id,
                    payloadJson = gson.toJson(record)
                )
            )

            try {
                firestoreDataSource.uploadConditionRecord(record)
                dao.deleteOutboxRecord(outboxId)
                dao.insertConditionRecord(
                    ConditionRecordEntity(
                        record.id, record.patientId, record.patientName, record.villageId,
                        record.villageName, record.category, record.severity,
                        record.requestedDoctorType, record.notes, record.timestamp,
                        record.ashaProxyLogged, record.status, record.assignedDoctorId,
                        record.assignedDoctorName, record.doctorResponse, record.doctorResponseTimestamp,
                        record.doctorResponseDoctorName, record.privateDoctorNotes,
                        record.referredByDoctorId, record.referredByDoctorName,
                        record.referralNotes, isPendingSync = false
                    )
                )
            } catch (e: Exception) {
                // Device offline: Outbox record remains persisted in Room SQLite; WorkManager will sync on network reconnection
                syncManager.triggerImmediateSync()
            }
        }
    }

    override suspend fun respondToCase(
        caseId: String,
        doctorId: String,
        doctorName: String,
        responseText: String,
        privateNotes: String?,
        newStatus: CaseStatus
    ) {
        val now = System.currentTimeMillis()
        var updatedRecord: ConditionRecord? = null

        _conditions.update { list ->
            list.map { record ->
                if (record.id == caseId) {
                    val updated = record.copy(
                        status = newStatus,
                        doctorResponse = responseText,
                        doctorResponseTimestamp = now,
                        doctorResponseDoctorName = doctorName,
                        privateDoctorNotes = privateNotes ?: record.privateDoctorNotes,
                        assignedDoctorId = doctorId,
                        assignedDoctorName = doctorName
                    )
                    updatedRecord = updated
                    updated
                } else record
            }
        }

        updatedRecord?.let { record ->
            scope.launch {
                dao.insertConditionRecord(
                    ConditionRecordEntity(
                        record.id, record.patientId, record.patientName, record.villageId,
                        record.villageName, record.category, record.severity,
                        record.requestedDoctorType, record.notes, record.timestamp,
                        record.ashaProxyLogged, record.status, record.assignedDoctorId,
                        record.assignedDoctorName, record.doctorResponse, record.doctorResponseTimestamp,
                        record.doctorResponseDoctorName, record.privateDoctorNotes,
                        record.referredByDoctorId, record.referredByDoctorName,
                        record.referralNotes, isPendingSync = false
                    )
                )
                try {
                    firestoreDataSource.uploadConditionRecord(record)
                } catch (e: Exception) {
                    // Stays in Room
                }
            }
        }
    }

    override suspend fun referCaseToSpecialist(
        caseId: String,
        referringDoctor: Doctor,
        targetSpecialty: DoctorSpecialty,
        referralNotes: String
    ) {
        var updatedRecord: ConditionRecord? = null

        _conditions.update { list ->
            list.map { record ->
                if (record.id == caseId) {
                    val updated = record.copy(
                        status = CaseStatus.REFERRED,
                        requestedDoctorType = targetSpecialty,
                        referredByDoctorId = referringDoctor.id,
                        referredByDoctorName = referringDoctor.name,
                        referralNotes = referralNotes,
                        assignedDoctorId = null,
                        assignedDoctorName = null
                    )
                    updatedRecord = updated
                    updated
                } else record
            }
        }

        updatedRecord?.let { record ->
            scope.launch {
                dao.insertConditionRecord(
                    ConditionRecordEntity(
                        record.id, record.patientId, record.patientName, record.villageId,
                        record.villageName, record.category, record.severity,
                        record.requestedDoctorType, record.notes, record.timestamp,
                        record.ashaProxyLogged, record.status, record.assignedDoctorId,
                        record.assignedDoctorName, record.doctorResponse, record.doctorResponseTimestamp,
                        record.doctorResponseDoctorName, record.privateDoctorNotes,
                        record.referredByDoctorId, record.referredByDoctorName,
                        record.referralNotes, isPendingSync = false
                    )
                )
                try {
                    firestoreDataSource.uploadConditionRecord(record)
                } catch (e: Exception) {
                    // Stays in Room
                }
            }
        }
    }

    // --- Prescriptions ---
    override fun getPrescriptions(): Flow<List<Prescription>> = _prescriptions.asStateFlow()

    override fun getPrescriptionsForPatient(patientId: String): Flow<List<Prescription>> = _prescriptions.map { list ->
        list.filter { it.patientId == patientId }
    }

    override fun getPrescriptionsByCase(caseId: String): Flow<List<Prescription>> = _prescriptions.map { list ->
        list.filter { it.caseId == caseId }
    }

    override suspend fun savePrescription(prescription: Prescription) {
        _prescriptions.update { listOf(prescription) + it }

        // Also mark the case as RESPONDED if tied to a case
        if (prescription.caseId != null) {
            _conditions.update { list ->
                list.map { c ->
                    if (c.id == prescription.caseId && c.status == CaseStatus.PENDING_REVIEW) {
                        c.copy(status = CaseStatus.RESPONDED)
                    } else c
                }
            }
        }

        scope.launch {
            dao.insertPrescription(
                PrescriptionEntity(
                    prescription.id, prescription.caseId, prescription.patientId, prescription.patientName,
                    prescription.doctorId, prescription.doctorName, prescription.doctorSpecialty,
                    prescription.timestamp, prescription.dateFormatted,
                    gson.toJson(prescription.medicines), prescription.instructions,
                    prescription.isOcrExtracted
                )
            )

            val outboxId = "outbox_rx_${prescription.id}"
            dao.insertOutboxRecord(
                com.vitalsense.app.core.data.local.entity.OutboxEntity(
                    id = outboxId,
                    actionType = "PRESCRIPTION",
                    entityId = prescription.id,
                    payloadJson = gson.toJson(prescription)
                )
            )

            try {
                firestoreDataSource.uploadPrescription(prescription)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Appointments ---
    override fun getAppointments(): Flow<List<Appointment>> = _appointments.asStateFlow()

    override fun getAppointmentsForPatient(patientId: String): Flow<List<Appointment>> = _appointments.map { list ->
        list.filter { it.patientId == patientId }
    }

    override fun getAppointmentsForDoctor(doctorId: String): Flow<List<Appointment>> = _appointments.map { list ->
        list.filter { it.doctorId == doctorId }
    }

    override suspend fun scheduleAppointment(appointment: Appointment) {
        _appointments.update { listOf(appointment) + it }

        _patients.update { patients ->
            patients.map { p ->
                if (p.id == appointment.patientId) {
                    p.copy(nextAppointmentDate = "${appointment.dateFormatted} (${appointment.timeSlot})")
                } else p
            }
        }

        scope.launch {
            dao.insertAppointment(
                AppointmentEntity(
                    appointment.id, appointment.patientId, appointment.patientName,
                    appointment.doctorId, appointment.doctorName, appointment.doctorSpecialty,
                    appointment.dateFormatted, appointment.timeSlot, appointment.status,
                    appointment.proposedBy, appointment.outcomeNotes,
                    appointment.callType.name, appointment.scheduledTimestamp
                )
            )

            val outboxId = "outbox_appt_${appointment.id}"
            dao.insertOutboxRecord(
                com.vitalsense.app.core.data.local.entity.OutboxEntity(
                    id = outboxId,
                    actionType = "APPOINTMENT",
                    entityId = appointment.id,
                    payloadJson = gson.toJson(appointment)
                )
            )

            try {
                firestoreDataSource.uploadAppointment(appointment)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    override suspend fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        outcomeNotes: String?
    ) {
        var updatedAppointment: Appointment? = null

        _appointments.update { list ->
            list.map { appt ->
                if (appt.id == appointmentId) {
                    val updated = appt.copy(
                        status = newStatus,
                        outcomeNotes = outcomeNotes ?: appt.outcomeNotes
                    )
                    updatedAppointment = updated
                    updated
                } else appt
            }
        }

        updatedAppointment?.let { appt ->
            scope.launch {
                dao.insertAppointment(
                    AppointmentEntity(
                        appt.id, appt.patientId, appt.patientName,
                        appt.doctorId, appt.doctorName, appt.doctorSpecialty,
                        appt.dateFormatted, appt.timeSlot, appt.status,
                        appt.proposedBy, appt.outcomeNotes,
                        appt.callType.name, appt.scheduledTimestamp
                    )
                )

                val outboxId = "outbox_appt_status_${appt.id}"
                dao.insertOutboxRecord(
                    com.vitalsense.app.core.data.local.entity.OutboxEntity(
                        id = outboxId,
                        actionType = "APPOINTMENT",
                        entityId = appt.id,
                        payloadJson = gson.toJson(appt)
                    )
                )

                try {
                    firestoreDataSource.uploadAppointment(appt)
                    dao.deleteOutboxRecord(outboxId)
                } catch (e: Exception) {
                    syncManager.triggerImmediateSync()
                }
            }
        }
    }

    // --- Broadcast Notices ---
    override fun getNotices(): Flow<List<BroadcastNotice>> = _notices.asStateFlow()

    override suspend fun sendNotice(notice: BroadcastNotice) {
        android.util.Log.d("VitalSenseFirebase", "📢 sendNotice triggered: ${notice.title}")
        _notices.update { listOf(notice) + it }

        scope.launch {
            dao.insertNotice(
                BroadcastNoticeEntity(
                    notice.id, notice.senderRole, notice.senderName, notice.targetRole,
                    notice.targetVillage, notice.title, notice.message, notice.timestamp,
                    notice.isUrgent
                )
            )

            val outboxId = "outbox_notice_${notice.id}"
            dao.insertOutboxRecord(
                com.vitalsense.app.core.data.local.entity.OutboxEntity(
                    id = outboxId,
                    actionType = "BROADCAST_NOTICE",
                    entityId = notice.id,
                    payloadJson = gson.toJson(notice)
                )
            )

            try {
                firestoreDataSource.uploadNotice(notice)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Dispensary Stock ---
    override fun getDispensaryStock(): Flow<List<DispensaryItem>> = _dispensary.asStateFlow()

    override suspend fun saveDispensaryItem(item: DispensaryItem) {
        _dispensary.update { list ->
            val index = list.indexOfFirst { it.id == item.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, item) }
            } else {
                list + item
            }
        }
        scope.launch {
            dao.insertDispensaryItem(
                DispensaryEntity(
                    item.id, item.medicineName, item.category, item.availableQuantity,
                    item.unit, item.reorderThreshold, item.lastRestockDateFormatted
                )
            )
            val outboxId = "outbox_disp_${item.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "DISPENSARY_ITEM",
                    entityId = item.id,
                    payloadJson = gson.toJson(item),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadDispensaryItem(item)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Disease Trend Records ---
    override fun getDiseaseTrendRecords(): Flow<List<DiseaseTrendRecord>> = _diseaseTrends.asStateFlow()

    override suspend fun saveDiseaseTrendRecord(record: DiseaseTrendRecord) {
        _diseaseTrends.update { list ->
            val index = list.indexOfFirst { it.id == record.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, record) }
            } else {
                list + record
            }
        }
        scope.launch {
            dao.insertDiseaseTrendRecord(
                DiseaseTrendRecordEntity(
                    record.id, record.villageName, record.diseaseName, record.caseCount,
                    record.dateFormatted, record.severity
                )
            )
            val outboxId = "outbox_trend_${record.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "DISEASE_TREND",
                    entityId = record.id,
                    payloadJson = gson.toJson(record),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadDiseaseTrend(record)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Government Schemes ---
    override fun getGovernmentSchemes(): Flow<List<GovernmentScheme>> = _schemes.asStateFlow()

    // --- Emergency SOS ---
    override suspend fun triggerEmergencySos(
        patient: Patient,
        locationLat: Double?,
        locationLng: Double?
    ): Boolean {
        android.util.Log.d("VitalSenseFirebase", "🚨 triggerEmergencySos called for patient: ${patient.name}")
        val isOnline = networkMonitor.isOnline()
        val sosNotice = BroadcastNotice(
            id = "sos_${System.currentTimeMillis()}",
            senderRole = UserRole.PATIENT,
            senderName = "${patient.name} (SOS ALERT)",
            targetRole = "ASHA_DOCTOR",
            targetVillage = patient.villageName,
            title = "🚨 EMERGENCY SOS: ${patient.name}",
            message = "Patient ${patient.name} (${patient.villageName}, Age ${patient.age}) triggered an Emergency SOS! Contact: ${patient.phone}. Location: Lat ${locationLat ?: 26.8467}, Lng ${locationLng ?: 80.9462}.",
            timestamp = System.currentTimeMillis(),
            isUrgent = true
        )

        // Instant in-memory update
        _notices.update { listOf(sosNotice) + it }

        // Local Room persistence
        dao.insertNotice(
            BroadcastNoticeEntity(
                sosNotice.id, sosNotice.senderRole, sosNotice.senderName, sosNotice.targetRole,
                sosNotice.targetVillage, sosNotice.title, sosNotice.message, sosNotice.timestamp,
                sosNotice.isUrgent
            )
        )

        val outboxId = "outbox_sos_${sosNotice.id}"
        dao.insertOutboxRecord(
            OutboxEntity(
                id = outboxId,
                actionType = "SOS_ALERT",
                entityId = sosNotice.id,
                payloadJson = gson.toJson(sosNotice),
                timestamp = System.currentTimeMillis()
            )
        )

        if (isOnline) {
            return try {
                firestoreDataSource.uploadNotice(sosNotice)
                dao.deleteOutboxRecord(outboxId)
                true
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
                false
            }
        } else {
            // Offline: Enqueued in outbox, returns false to signal UI that server alert is pending
            return false
        }
    }

    // --- ASHA Features ---
    override fun getImmunizationRecords(): Flow<List<ImmunizationRecord>> = _immunizations.asStateFlow()

    override suspend fun saveImmunizationRecord(record: ImmunizationRecord) {
        _immunizations.update { list ->
            val index = list.indexOfFirst { it.id == record.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, record) }
            } else {
                list + record
            }
        }
        scope.launch {
            dao.insertImmunizationRecord(
                ImmunizationRecordEntity(
                    record.id, record.childName, record.motherName, record.dobFormatted,
                    record.gender, record.villageName, record.ashaWorkerId,
                    gson.toJson(record.vaccines)
                )
            )
            val outboxId = "outbox_imm_${record.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "IMMUNIZATION_RECORD",
                    entityId = record.id,
                    payloadJson = gson.toJson(record),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadImmunizationRecord(record)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    override fun getDailyRounds(): Flow<List<DailyRound>> = _dailyRounds.asStateFlow()

    override suspend fun saveDailyRound(round: DailyRound) {
        _dailyRounds.update { list ->
            val index = list.indexOfFirst { it.id == round.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, round) }
            } else {
                list + round
            }
        }
        scope.launch {
            dao.insertDailyRound(
                DailyRoundEntity(
                    round.id, round.dateFormatted, round.villageName, round.householdName,
                    round.personName, round.ashaWorkerId, round.purpose,
                    round.isPregnancyChecked, round.isChildHealthChecked,
                    round.isImmunizationChecked, round.isMedicineGiven,
                    round.isCounsellingDone, round.notes, round.status
                )
            )
            val outboxId = "outbox_round_${round.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "DAILY_ROUND",
                    entityId = round.id,
                    payloadJson = gson.toJson(round),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadDailyRound(round)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    override fun getAshaMedicines(): Flow<List<AshaMedicine>> = _ashaMedicines.asStateFlow()

    override suspend fun saveAshaMedicine(medicine: AshaMedicine) {
        _ashaMedicines.update { list ->
            val index = list.indexOfFirst { it.id == medicine.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, medicine) }
            } else {
                list + medicine
            }
        }
        scope.launch {
            dao.insertAshaMedicine(
                AshaMedicineEntity(
                    medicine.id, medicine.ashaWorkerId, medicine.medicineName,
                    medicine.availableQuantity, medicine.unit, medicine.minStockQuantity,
                    medicine.expiryDateFormatted, medicine.lastRestockDateFormatted
                )
            )
            val outboxId = "outbox_ashamed_${medicine.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "ASHA_MEDICINE",
                    entityId = medicine.id,
                    payloadJson = gson.toJson(medicine),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadAshaMedicine(medicine)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Diagnostic Lab Reports ---
    override fun getLabReports(): Flow<List<LabReport>> = _labReports.asStateFlow()

    override fun getLabReportsForPatient(patientId: String): Flow<List<LabReport>> =
        _labReports.map { list -> list.filter { it.patientId == patientId } }

    override suspend fun saveLabReport(report: LabReport) {
        _labReports.update { list ->
            val index = list.indexOfFirst { it.id == report.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, report) }
            } else {
                listOf(report) + list
            }
        }
        scope.launch {
            dao.insertLabReport(
                LabReportEntity(
                    report.id, report.patientId, report.patientName, report.testCategory,
                    report.doctorName, report.dateFormatted, report.items, report.notes, report.status
                )
            )
            val outboxId = "outbox_lab_${report.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "LAB_REPORT",
                    entityId = report.id,
                    payloadJson = gson.toJson(report),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadLabReport(report)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Live OPD Queue Tokens ---
    override fun getOpdTokens(): Flow<List<OpdToken>> = _opdTokens.asStateFlow()

    override fun getOpdTokensForPatient(patientId: String): Flow<List<OpdToken>> =
        _opdTokens.map { list -> list.filter { it.patientId == patientId } }

    override suspend fun bookOpdToken(token: OpdToken) {
        _opdTokens.update { list ->
            val index = list.indexOfFirst { it.id == token.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, token) }
            } else {
                listOf(token) + list
            }
        }
        scope.launch {
            dao.insertOpdToken(
                OpdTokenEntity(
                    token.id, token.tokenNumber, token.patientId, token.patientName,
                    token.doctorName, token.department, token.cabinNumber,
                    token.currentServingToken, token.estimatedWaitMinutes, token.status, token.dateFormatted
                )
            )
            val outboxId = "outbox_opd_${token.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "OPD_TOKEN",
                    entityId = token.id,
                    payloadJson = gson.toJson(token),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadOpdToken(token)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }

        // Bridge to live doctor queue HUD
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val matchedDoctor = _doctors.value.find { doc ->
            doc.name.contains("Rajesh", ignoreCase = true) || doc.name.equals(token.doctorName, ignoreCase = true)
        } ?: _doctors.value.first()

        val numericToken = token.tokenNumber.filter { it.isDigit() }.toIntOrNull() ?: (_queueEntries.value.size + 1)

        val queueEntry = QueueEntry(
            id = "queue_opd_${token.id}",
            doctorId = matchedDoctor.id,
            doctorName = matchedDoctor.name,
            dateFormatted = today,
            tokenNumber = numericToken,
            provisionalToken = false,
            appointmentId = null,
            patientId = token.patientId,
            patientName = token.patientName,
            source = QueueEntrySource.WALK_IN,
            status = QueueEntryStatus.WAITING,
            priorityFlag = false,
            checkedInAt = System.currentTimeMillis(),
            isPendingSync = false
        )

        _queueEntries.update { current ->
            if (current.none { it.id == queueEntry.id }) current + queueEntry else current
        }
        scope.launch {
            dao.upsertQueueEntry(queueEntry.toEntity())
        }
    }

    // --- Medical Certificates ---
    override fun getMedicalCertificates(): Flow<List<MedicalCertificate>> = _medicalCertificates.asStateFlow()

    override fun getMedicalCertificatesForPatient(patientId: String): Flow<List<MedicalCertificate>> =
        _medicalCertificates.map { list -> list.filter { it.patientId == patientId } }

    override suspend fun saveMedicalCertificate(certificate: MedicalCertificate) {
        _medicalCertificates.update { list ->
            val index = list.indexOfFirst { it.id == certificate.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, certificate) }
            } else {
                listOf(certificate) + list
            }
        }
        scope.launch {
            dao.insertMedicalCertificate(
                MedicalCertificateEntity(
                    certificate.id, certificate.certificateNumber, certificate.patientId,
                    certificate.patientName, certificate.patientAge, certificate.patientGender,
                    certificate.doctorName, certificate.doctorRegistrationNumber,
                    certificate.diagnosis, certificate.restStartDate, certificate.restEndDate,
                    certificate.fitDate, certificate.certificateType, certificate.issuedDateFormatted
                )
            )
            val outboxId = "outbox_cert_${certificate.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "MEDICAL_CERTIFICATE",
                    entityId = certificate.id,
                    payloadJson = gson.toJson(certificate),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadMedicalCertificate(certificate)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Blood Bank Inventory ---
    override fun getBloodStock(): Flow<List<BloodStockItem>> = _bloodStock.asStateFlow()

    override suspend fun updateBloodStock(item: BloodStockItem) {
        _bloodStock.update { list ->
            val index = list.indexOfFirst { it.id == item.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, item) }
            } else {
                list + item
            }
        }
        scope.launch {
            dao.insertBloodStockItem(
                BloodStockEntity(
                    item.id, item.bloodGroup, item.unitsAvailable, item.hospitalName,
                    item.contactPhone, item.status
                )
            )
        }
    }

    // --- Family Linkage ---
    override fun getFamilyMembers(primaryPatientId: String): Flow<List<FamilyMember>> =
        _familyMembers.map { list -> list.filter { it.primaryPatientId == primaryPatientId } }

    // --- In-Patient Care (IPD) Beds ---
    override fun getIpdBeds(): Flow<List<IpdBed>> = _ipdBeds.asStateFlow()

    override suspend fun saveIpdBed(bed: IpdBed) {
        _ipdBeds.update { list ->
            val index = list.indexOfFirst { it.id == bed.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, bed) }
            } else {
                list + bed
            }
        }
        scope.launch {
            dao.insertIpdBed(
                IpdBedEntity(
                    bed.id, bed.wardName, bed.bedNumber, bed.isOccupied,
                    bed.patientId, bed.patientName, bed.admissionDate,
                    bed.attendingDoctorName, bed.diagnosis, bed.nurseInCharge
                )
            )
        }
    }

    // --- Operation Theatre (OT) Surgery Bookings ---
    override fun getOtSurgeryBookings(): Flow<List<OtSurgeryBooking>> = _otSurgeryBookings.asStateFlow()

    override suspend fun saveOtSurgeryBooking(booking: OtSurgeryBooking) {
        _otSurgeryBookings.update { list ->
            val index = list.indexOfFirst { it.id == booking.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, booking) }
            } else {
                listOf(booking) + list
            }
        }
        scope.launch {
            dao.insertOtSurgeryBooking(
                OtSurgeryBookingEntity(
                    booking.id, booking.otRoomName, booking.patientId, booking.patientName,
                    booking.surgeryName, booking.surgeonName, booking.anesthetistName,
                    booking.scheduledDate, booking.scheduledTimeSlot, booking.pacCleared, booking.status
                )
            )
        }
    }

    // --- External Hospital Referrals ---
    override fun getExternalReferrals(): Flow<List<ExternalReferral>> = _externalReferrals.asStateFlow()

    override fun getExternalReferralsForPatient(patientId: String): Flow<List<ExternalReferral>> =
        _externalReferrals.map { list -> list.filter { it.patientId == patientId } }

    override suspend fun saveExternalReferral(referral: ExternalReferral) {
        _externalReferrals.update { list ->
            val index = list.indexOfFirst { it.id == referral.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, referral) }
            } else {
                listOf(referral) + list
            }
        }
        scope.launch {
            dao.insertExternalReferral(
                ExternalReferralEntity(
                    referral.id, referral.referralLetterId, referral.patientId, referral.patientName,
                    referringDoctorName = referral.referringDoctorName,
                    empanelledHospitalName = referral.empanelledHospitalName,
                    specialtyRequired = referral.specialtyRequired,
                    clinicalSummary = referral.clinicalSummary,
                    isCashlessApproved = referral.isCashlessApproved,
                    ambulanceRequisitioned = referral.ambulanceRequisitioned,
                    issuedDate = referral.issuedDate,
                    status = referral.status
                )
            )
            val outboxId = "outbox_extref_${referral.id}"
            dao.insertOutboxRecord(
                OutboxEntity(
                    id = outboxId,
                    actionType = "EXTERNAL_REFERRAL",
                    entityId = referral.id,
                    payloadJson = gson.toJson(referral),
                    timestamp = System.currentTimeMillis()
                )
            )
            try {
                firestoreDataSource.uploadExternalReferral(referral)
                dao.deleteOutboxRecord(outboxId)
            } catch (e: Exception) {
                syncManager.triggerImmediateSync()
            }
        }
    }

    // --- Bio-Medical Equipment ---
    override fun getBioMedicalEquipment(): Flow<List<BioMedicalEquipment>> = _bioMedicalEquipment.asStateFlow()

    override suspend fun saveBioMedicalEquipment(equipment: BioMedicalEquipment) {
        _bioMedicalEquipment.update { list ->
            val index = list.indexOfFirst { it.id == equipment.id }
            if (index >= 0) {
                list.toMutableList().apply { set(index, equipment) }
            } else {
                list + equipment
            }
        }
        scope.launch {
            dao.insertBioMedicalEquipment(
                BioMedicalEquipmentEntity(
                    equipment.id, equipment.assetCode, equipment.name,
                    equipment.department, equipment.status, equipment.lastServiceDate,
                    equipment.nextServiceDue, equipment.location, equipment.inChargeContact
                )
            )
        }
    }

    // --- Live Clinic Queue & Day Slots ---

    override fun observeDoctorQueue(doctorId: String, date: String): Flow<List<QueueEntry>> {
        val key = "$doctorId-$date"
        if (activeQueueJobs[key]?.isActive != true) {
            activeQueueJobs[key] = scope.launch {
                try {
                    firestoreDataSource.observeDoctorQueueStream(doctorId, date).collect { remoteEntries ->
                        if (remoteEntries.isNotEmpty()) {
                            _queueEntries.update { current ->
                                val remoteMap = remoteEntries.associateBy { it.id }
                                current.map { local -> remoteMap[local.id] ?: local } +
                                    remoteEntries.filter { remote -> current.none { it.id == remote.id } }
                            }
                            dao.upsertQueueEntries(remoteEntries.map { it.toEntity() })
                        }
                    }
                } catch (e: Exception) {
                    // Offline fallback
                }
            }
        }

        return _queueEntries.map { list ->
            val forDoctor = list.filter { 
                (it.doctorId == doctorId || (doctorId == "doc_rajesh" && it.doctorName.contains("Rajesh", ignoreCase = true))) && 
                (it.dateFormatted == date || it.dateFormatted == "Today" || it.dateFormatted.startsWith(date.take(7)))
            }
            val (waiting, nonWaiting) = forDoctor.partition { it.status == QueueEntryStatus.WAITING }
            com.vitalsense.app.core.util.QueueEtaCalculator.sortWaitingEntries(waiting) +
                nonWaiting.sortedBy { it.checkedInAt }
        }
    }

    override fun observePatientQueueEntry(patientId: String, date: String): Flow<QueueEntry?> {
        return _queueEntries.map { list ->
            list.firstOrNull { 
                it.patientId == patientId && 
                (it.dateFormatted == date || it.dateFormatted == "Today" || it.dateFormatted.startsWith(date.take(7))) &&
                it.status != QueueEntryStatus.COMPLETED
            }
        }
    }

    override fun observeDoctorSlots(doctorId: String, date: String): Flow<List<DoctorDaySlotConfig>> {
        val key = "$doctorId-$date"
        if (activeSlotJobs[key]?.isActive != true) {
            activeSlotJobs[key] = scope.launch {
                try {
                    firestoreDataSource.observeDoctorSlotsStream(doctorId, date).collect { remoteSlots ->
                        if (remoteSlots.isNotEmpty()) {
                            _doctorSlots.update { remoteSlots }
                        }
                    }
                } catch (e: Exception) {
                    // Offline
                }
            }
        }
        return _doctorSlots.map { list -> list.filter { it.doctorId == doctorId && it.dateFormatted == date } }
    }

    override fun observeAllDoctorQueueSummaries(date: String): Flow<List<DoctorQueueSummary>> {
        return combine(_doctors, _queueEntries, _doctorSlots) { doctors, entries, slots ->
            doctors.map { doc ->
                val docEntries = entries.filter { it.doctorId == doc.id && it.dateFormatted == date }
                val waitingCount = docEntries.count { it.status == QueueEntryStatus.WAITING }
                val activeOrCalled = docEntries.firstOrNull { it.status == QueueEntryStatus.IN_CONSULTATION || it.status == QueueEntryStatus.CALLED }
                val currentToken = activeOrCalled?.tokenNumber ?: 0
                val avgWait = com.vitalsense.app.core.util.QueueEtaCalculator.averageConsultationSeconds(docEntries)
                val isQueueOpen = slots.find { it.doctorId == doc.id && it.dateFormatted == date }?.isWalkInOpen ?: true

                DoctorQueueSummary(
                    doctorId = doc.id,
                    doctorName = doc.name,
                    dateFormatted = date,
                    waitingCount = waitingCount,
                    currentToken = currentToken,
                    avgWaitSeconds = avgWait,
                    isQueueOpen = isQueueOpen
                )
            }
        }
    }

    override suspend fun defineDoctorSlot(slot: DoctorDaySlotConfig) {
        _doctorSlots.update { list ->
            val idx = list.indexOfFirst { it.id == slot.id }
            if (idx >= 0) list.toMutableList().apply { set(idx, slot) } else list + slot
        }
        dao.upsertDoctorSlot(
            com.vitalsense.app.core.data.local.entity.DoctorDaySlotEntity(
                slot.id, slot.doctorId, slot.dateFormatted, slot.startTime, slot.endTime, slot.capacity, slot.isWalkInOpen
            )
        )
        try {
            firestoreDataSource.uploadDoctorSlot(slot)
        } catch (e: Exception) {
            val outbox = OutboxEntity(
                id = UUID.randomUUID().toString(),
                actionType = "DOCTOR_DAY_SLOT",
                entityId = slot.id,
                payloadJson = gson.toJson(slot),
                timestamp = System.currentTimeMillis()
            )
            dao.insertOutboxRecord(outbox)
            syncManager.triggerImmediateSync()
        }
    }

    override suspend fun checkInAppointment(appointmentId: String): QueueEntry {
        val appt = _appointments.value.find { it.id == appointmentId }
            ?: throw IllegalArgumentException("Appointment $appointmentId not found")

        val entryId = "queue_${UUID.randomUUID()}"
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val provisionalEntry = QueueEntry(
            id = entryId,
            doctorId = appt.doctorId,
            doctorName = appt.doctorName,
            dateFormatted = today,
            tokenNumber = -1,
            provisionalToken = true,
            appointmentId = appt.id,
            patientId = appt.patientId,
            patientName = appt.patientName,
            source = QueueEntrySource.SCHEDULED,
            status = QueueEntryStatus.WAITING,
            priorityFlag = false,
            checkedInAt = System.currentTimeMillis(),
            isPendingSync = true
        )

        _queueEntries.update { it + provisionalEntry }
        dao.upsertQueueEntry(provisionalEntry.toEntity())

        return try {
            val authoritative = firestoreDataSource.assignAuthoritativeTokenAndSave(provisionalEntry)
            _queueEntries.update { list ->
                list.map { if (it.id == authoritative.id) authoritative else it }
            }
            dao.upsertQueueEntry(authoritative.toEntity())
            authoritative
        } catch (e: Exception) {
            val outbox = OutboxEntity(
                id = UUID.randomUUID().toString(),
                actionType = "QUEUE_ENTRY",
                entityId = provisionalEntry.id,
                payloadJson = gson.toJson(provisionalEntry),
                timestamp = System.currentTimeMillis()
            )
            dao.insertOutboxRecord(outbox)
            syncManager.triggerImmediateSync()
            provisionalEntry
        }
    }

    override suspend fun joinWalkInQueue(
        doctorId: String,
        doctorName: String,
        patientId: String,
        patientName: String
    ): QueueEntry {
        val entryId = "queue_${UUID.randomUUID()}"
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val provisionalEntry = QueueEntry(
            id = entryId,
            doctorId = doctorId,
            doctorName = doctorName,
            dateFormatted = today,
            tokenNumber = -1,
            provisionalToken = true,
            appointmentId = null,
            patientId = patientId,
            patientName = patientName,
            source = QueueEntrySource.WALK_IN,
            status = QueueEntryStatus.WAITING,
            priorityFlag = false,
            checkedInAt = System.currentTimeMillis(),
            isPendingSync = true
        )

        _queueEntries.update { it + provisionalEntry }
        dao.upsertQueueEntry(provisionalEntry.toEntity())

        return try {
            val authoritative = firestoreDataSource.assignAuthoritativeTokenAndSave(provisionalEntry)
            _queueEntries.update { list ->
                list.map { if (it.id == authoritative.id) authoritative else it }
            }
            dao.upsertQueueEntry(authoritative.toEntity())
            authoritative
        } catch (e: Exception) {
            val outbox = OutboxEntity(
                id = UUID.randomUUID().toString(),
                actionType = "QUEUE_ENTRY",
                entityId = provisionalEntry.id,
                payloadJson = gson.toJson(provisionalEntry),
                timestamp = System.currentTimeMillis()
            )
            dao.insertOutboxRecord(outbox)
            syncManager.triggerImmediateSync()
            provisionalEntry
        }
    }

    override suspend fun callNext(doctorId: String, date: String) {
        val waiting = _queueEntries.value.filter {
            it.doctorId == doctorId && it.dateFormatted == date && it.status == QueueEntryStatus.WAITING
        }
        val nextEntry = com.vitalsense.app.core.util.QueueEtaCalculator.sortWaitingEntries(waiting).firstOrNull()
            ?: return

        val updated = nextEntry.copy(
            status = QueueEntryStatus.CALLED,
            calledAt = System.currentTimeMillis()
        )
        mutateQueueEntry(updated)
    }

    override suspend fun startConsultation(entryId: String) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return

        val existingActive = _queueEntries.value.find {
            it.doctorId == entry.doctorId && it.dateFormatted == entry.dateFormatted && it.status == QueueEntryStatus.IN_CONSULTATION && it.id != entryId
        }
        if (existingActive != null) {
            throw IllegalStateException("Another consultation is already in progress with ${existingActive.patientName} (Token #${existingActive.tokenNumber}).")
        }

        val updated = entry.copy(
            status = QueueEntryStatus.IN_CONSULTATION,
            consultationStartedAt = System.currentTimeMillis()
        )
        mutateQueueEntry(updated)
    }

    override suspend fun completeConsultation(entryId: String, outcomeNotes: String?) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return
        val updated = entry.copy(
            status = QueueEntryStatus.COMPLETED,
            completedAt = System.currentTimeMillis(),
            outcomeNotes = outcomeNotes
        )
        mutateQueueEntry(updated)
    }

    override suspend fun markNoShow(entryId: String) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return
        val updated = entry.copy(status = QueueEntryStatus.NO_SHOW)
        mutateQueueEntry(updated)
    }

    override suspend fun skipEntry(entryId: String) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return
        val isAlreadySkipped = entry.outcomeNotes?.contains("[SKIPPED_ONCE]") == true
        val updated = if (isAlreadySkipped) {
            entry.copy(status = QueueEntryStatus.NO_SHOW)
        } else {
            entry.copy(
                status = QueueEntryStatus.WAITING,
                checkedInAt = System.currentTimeMillis(),
                priorityFlag = false,
                outcomeNotes = "[SKIPPED_ONCE]"
            )
        }
        mutateQueueEntry(updated)
    }

    override suspend fun prioritizeEntry(entryId: String) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return
        val updated = entry.copy(priorityFlag = !entry.priorityFlag)
        mutateQueueEntry(updated)
    }

    override suspend fun cancelQueueEntry(entryId: String) {
        val entry = _queueEntries.value.find { it.id == entryId } ?: return
        val updated = entry.copy(status = QueueEntryStatus.CANCELLED)
        mutateQueueEntry(updated)
    }

    private suspend fun mutateQueueEntry(entry: QueueEntry) {
        _queueEntries.update { list ->
            list.map { if (it.id == entry.id) entry else it }
        }
        dao.upsertQueueEntry(entry.toEntity())
        try {
            firestoreDataSource.uploadQueueEntry(entry)
        } catch (e: Exception) {
            val outbox = OutboxEntity(
                id = UUID.randomUUID().toString(),
                actionType = "QUEUE_ENTRY",
                entityId = entry.id,
                payloadJson = gson.toJson(entry),
                timestamp = System.currentTimeMillis()
            )
            dao.insertOutboxRecord(outbox)
            syncManager.triggerImmediateSync()
        }
    }

    // --- Patient Medical History ---

    override fun getMedicalHistoryForPatient(patientId: String): Flow<List<MedicalHistoryEntry>> {
        return dao.getMedicalHistoryForPatient(patientId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addMedicalHistoryEntry(entry: MedicalHistoryEntry) {
        // 1. Save to local cache
        dao.insertMedicalHistoryEntry(entry.toEntity())
        
        // 2. Try pushing to Firestore
        try {
            firestoreDataSource.uploadMedicalHistory(entry)
        } catch (e: Exception) {
            // 3. Store in Outbox for offline sync
            val outbox = OutboxEntity(
                id = UUID.randomUUID().toString(),
                actionType = "MEDICAL_HISTORY_ENTRY",
                entityId = entry.id,
                payloadJson = gson.toJson(entry),
                timestamp = System.currentTimeMillis()
            )
            dao.insertOutboxRecord(outbox)
            syncManager.triggerImmediateSync()
        }
    }

    private fun com.vitalsense.app.core.data.local.entity.MedicalHistoryEntity.toDomain(): MedicalHistoryEntry {
        return MedicalHistoryEntry(
            id = id,
            patientId = patientId,
            type = runCatching { MedicalHistoryType.valueOf(type) }.getOrDefault(MedicalHistoryType.CONDITION),
            title = title,
            details = details,
            severity = severity?.takeIf { it.isNotBlank() }?.let { runCatching { SeverityLevel.valueOf(it) }.getOrNull() },
            doctorId = doctorId,
            doctorName = doctorName,
            caseId = caseId?.takeIf { it.isNotBlank() },
            prescriptionId = prescriptionId?.takeIf { it.isNotBlank() },
            timestamp = timestamp,
            dateFormatted = dateFormatted
        )
    }

    private fun MedicalHistoryEntry.toEntity(): com.vitalsense.app.core.data.local.entity.MedicalHistoryEntity {
        return com.vitalsense.app.core.data.local.entity.MedicalHistoryEntity(
            id = id,
            patientId = patientId,
            type = type.name,
            title = title,
            details = details,
            severity = severity?.name,
            doctorId = doctorId,
            doctorName = doctorName,
            caseId = caseId,
            prescriptionId = prescriptionId,
            timestamp = timestamp,
            dateFormatted = dateFormatted
        )
    }

    private fun QueueEntry.toEntity(): com.vitalsense.app.core.data.local.entity.QueueEntryEntity {
        return com.vitalsense.app.core.data.local.entity.QueueEntryEntity(
            id = id,
            doctorId = doctorId,
            doctorName = doctorName,
            dateFormatted = dateFormatted,
            tokenNumber = tokenNumber,
            provisionalToken = provisionalToken,
            appointmentId = appointmentId,
            patientId = patientId,
            patientName = patientName,
            source = source,
            status = status,
            priorityFlag = priorityFlag,
            checkedInAt = checkedInAt,
            calledAt = calledAt,
            consultationStartedAt = consultationStartedAt,
            completedAt = completedAt,
            outcomeNotes = outcomeNotes,
            isPendingSync = isPendingSync
        )
    }

    override fun getCallLogs(): Flow<List<CallLog>> {
        return dao.getAllCallLogs().map { entities ->
            entities.map { it.toCallLog() }
        }
    }

    override suspend fun saveCallLog(callLog: CallLog) {
        val entity = CallLogEntity(
            id = callLog.id,
            callType = callLog.callType.name,
            callMode = callLog.callMode,
            patientId = callLog.patientId,
            patientName = callLog.patientName,
            doctorId = callLog.doctorId,
            doctorName = callLog.doctorName,
            timestamp = callLog.timestamp,
            durationSeconds = callLog.durationSeconds,
            outcome = callLog.outcome.name,
            outcomeNotes = callLog.outcomeNotes
        )
        dao.insertCallLog(entity)
        val outboxId = "outbox_call_${callLog.id}"
        dao.insertOutboxRecord(
            com.vitalsense.app.core.data.local.entity.OutboxEntity(
                id = outboxId,
                actionType = "CALL_LOG",
                entityId = callLog.id,
                payloadJson = gson.toJson(callLog)
            )
        )
    }

    private fun CallLogEntity.toCallLog(): CallLog {
        return CallLog(
            id = id,
            callType = try { CallType.valueOf(callType) } catch (e: Exception) { CallType.VIDEO },
            callMode = callMode,
            patientId = patientId,
            patientName = patientName,
            doctorId = doctorId,
            doctorName = doctorName,
            timestamp = timestamp,
            durationSeconds = durationSeconds,
            outcome = try { EmergencyCallOutcome.valueOf(outcome) } catch (e: Exception) { EmergencyCallOutcome.CONNECTED },
            outcomeNotes = outcomeNotes
        )
    }

    // --- Doctor-to-Doctor Specialist Referrals ---
    override fun getAllReferrals(): Flow<List<Referral>> {
        return dao.getAllReferrals().map { list -> list.map { it.toModel() } }
    }

    override fun getReferralsForPatient(patientId: String): Flow<List<Referral>> {
        return dao.getReferralsForPatient(patientId).map { list -> list.map { it.toModel() } }
    }

    override fun getReferralsByReferringDoctor(doctorId: String): Flow<List<Referral>> {
        return dao.getReferralsByReferringDoctor(doctorId).map { list -> list.map { it.toModel() } }
    }

    override fun getReferralsForDoctorOrSpecialty(doctorId: String, specialty: String): Flow<List<Referral>> {
        return dao.getReferralsForDoctorOrSpecialty(doctorId, specialty).map { list -> list.map { it.toModel() } }
    }

    override suspend fun createReferral(referral: Referral) {
        dao.insertReferral(referral.toEntity())
        val outboxId = "outbox_ref_${referral.id}"
        dao.insertOutboxRecord(
            com.vitalsense.app.core.data.local.entity.OutboxEntity(
                id = outboxId,
                actionType = "CREATE_REFERRAL",
                entityId = referral.id,
                payloadJson = gson.toJson(referral)
            )
        )
    }

    override suspend fun updateReferral(referral: Referral) {
        dao.updateReferral(referral.toEntity())
        val outboxId = "outbox_ref_upd_${referral.id}_${System.currentTimeMillis()}"
        dao.insertOutboxRecord(
            com.vitalsense.app.core.data.local.entity.OutboxEntity(
                id = outboxId,
                actionType = "UPDATE_REFERRAL",
                entityId = referral.id,
                payloadJson = gson.toJson(referral)
            )
        )
    }

    // --- Audit Logs ---
    override fun getAllAuditLogs(): Flow<List<AuditLog>> {
        return dao.getAllAuditLogs().map { list -> list.map { it.toModel() } }
    }

    override fun getAuditLogsForPatient(patientId: String): Flow<List<AuditLog>> {
        return dao.getAuditLogsForPatient(patientId).map { list -> list.map { it.toModel() } }
    }

    override suspend fun logAuditAction(auditLog: AuditLog) {
        dao.insertAuditLog(auditLog.toEntity())
        
        val outboxId = "outbox_audit_${auditLog.id}"
        dao.insertOutboxRecord(
            com.vitalsense.app.core.data.local.entity.OutboxEntity(
                id = outboxId,
                actionType = "AUDIT_LOG",
                entityId = auditLog.id,
                payloadJson = gson.toJson(auditLog)
            )
        )
    }
}
