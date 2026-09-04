package com.vitalsense.app.core.data.local.dao

import androidx.room.*
import com.vitalsense.app.core.data.local.entity.*
import com.vitalsense.app.core.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalSenseDao {

    // --- Villages ---
    @Query("SELECT * FROM villages ORDER BY activeCases DESC")
    fun getAllVillages(): Flow<List<VillageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillages(villages: List<VillageEntity>)

    // --- Patients ---
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :id")
    fun getPatientById(id: String): Flow<PatientEntity?>

    @Query("SELECT * FROM patients WHERE ashaWorkerId = :ashaId")
    fun getPatientsByAsha(ashaId: String): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    // --- ASHA Workers ---
    @Query("SELECT * FROM asha_workers")
    fun getAllAshaWorkers(): Flow<List<AshaWorkerEntity>>

    @Query("SELECT * FROM asha_workers WHERE id = :id OR ashaUniqueId = :uniqueId LIMIT 1")
    fun getAshaWorker(id: String, uniqueId: String): Flow<AshaWorkerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAshaWorkers(ashaWorkers: List<AshaWorkerEntity>)

    // --- Doctors ---
    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE id = :id")
    fun getDoctorById(id: String): Flow<DoctorEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    // --- Condition Records ---
    @Query("SELECT * FROM condition_records ORDER BY timestamp DESC")
    fun getAllConditionRecords(): Flow<List<ConditionRecordEntity>>

    @Query("SELECT * FROM condition_records WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getConditionsForPatient(patientId: String): Flow<List<ConditionRecordEntity>>

    @Query("SELECT * FROM condition_records WHERE requestedDoctorType = :specialty OR assignedDoctorId = :doctorId ORDER BY CASE severity WHEN 'SEVERE' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MODERATE' THEN 3 ELSE 4 END, timestamp DESC")
    fun getCasesForDoctor(specialty: DoctorSpecialty, doctorId: String): Flow<List<ConditionRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditionRecord(record: ConditionRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditionRecords(records: List<ConditionRecordEntity>)

    // --- Prescriptions ---
    @Query("SELECT * FROM prescriptions ORDER BY timestamp DESC")
    fun getAllPrescriptions(): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getPrescriptionsForPatient(patientId: String): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions WHERE caseId = :caseId ORDER BY timestamp DESC")
    fun getPrescriptionsByCase(caseId: String): Flow<List<PrescriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescriptions(prescriptions: List<PrescriptionEntity>)

    // --- Appointments ---
    @Query("SELECT * FROM appointments ORDER BY dateFormatted ASC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE patientId = :patientId")
    fun getAppointmentsForPatient(patientId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId")
    fun getAppointmentsForDoctor(doctorId: String): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    // --- Broadcast Notices ---
    @Query("SELECT * FROM broadcast_notices ORDER BY timestamp DESC")
    fun getAllNotices(): Flow<List<BroadcastNoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: BroadcastNoticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<BroadcastNoticeEntity>)

    // --- Dispensary Stock ---
    @Query("SELECT * FROM dispensary_stock ORDER BY medicineName ASC")
    fun getAllDispensaryItems(): Flow<List<DispensaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDispensaryItems(items: List<DispensaryEntity>)

    // --- Government Schemes ---
    @Query("SELECT * FROM government_schemes")
    fun getAllSchemes(): Flow<List<GovernmentSchemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchemes(schemes: List<GovernmentSchemeEntity>)

    // --- ASHA Features ---
    @Query("SELECT * FROM immunization_records")
    fun getAllImmunizationRecords(): Flow<List<ImmunizationRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImmunizationRecord(record: ImmunizationRecordEntity)

    @Query("SELECT * FROM daily_rounds")
    fun getAllDailyRounds(): Flow<List<DailyRoundEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyRound(round: DailyRoundEntity)

    @Query("SELECT * FROM asha_medicines")
    fun getAllAshaMedicines(): Flow<List<AshaMedicineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAshaMedicine(medicine: AshaMedicineEntity)

    // --- Outbox Queue (Offline-First Sync) ---
    @Query("SELECT * FROM outbox_records WHERE syncStatus != 'SYNCED' ORDER BY timestamp ASC")
    suspend fun getPendingOutboxRecords(): List<OutboxEntity>

    @Query("SELECT COUNT(*) FROM outbox_records WHERE syncStatus != 'SYNCED'")
    fun getPendingOutboxCount(): Flow<Int>

    @Query("SELECT * FROM outbox_records WHERE id = :id LIMIT 1")
    suspend fun getOutboxRecordById(id: String): OutboxEntity?

    @Query("SELECT * FROM outbox_records ORDER BY timestamp ASC")
    fun getAllOutboxRecords(): Flow<List<OutboxEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutboxRecord(outbox: OutboxEntity)

    @Query("UPDATE outbox_records SET syncStatus = :status, lastAttemptAt = :attemptAt, errorMessage = :error, retryCount = retryCount + 1 WHERE id = :id")
    suspend fun updateOutboxStatus(id: String, status: String, attemptAt: Long, error: String?)

    @Query("DELETE FROM outbox_records WHERE id = :id")
    suspend fun deleteOutboxRecord(id: String)

    @Query("DELETE FROM outbox_records WHERE entityId = :entityId")
    suspend fun deleteOutboxRecordsForEntity(entityId: String)

    // --- Disease Trend Records ---
    @Query("SELECT * FROM disease_trend_records ORDER BY dateFormatted ASC")
    fun getAllDiseaseTrendRecords(): Flow<List<DiseaseTrendRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseTrendRecord(record: DiseaseTrendRecordEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseTrendRecords(records: List<DiseaseTrendRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDispensaryItem(item: DispensaryEntity)

    // --- Lab Reports ---
    @Query("SELECT * FROM lab_reports ORDER BY dateFormatted DESC")
    fun getAllLabReports(): Flow<List<LabReportEntity>>

    @Query("SELECT * FROM lab_reports WHERE patientId = :patientId ORDER BY dateFormatted DESC")
    fun getLabReportsForPatient(patientId: String): Flow<List<LabReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabReport(report: LabReportEntity)

    // --- OPD Queue Tokens ---
    @Query("SELECT * FROM opd_tokens ORDER BY dateFormatted DESC")
    fun getAllOpdTokens(): Flow<List<OpdTokenEntity>>

    @Query("SELECT * FROM opd_tokens WHERE patientId = :patientId ORDER BY dateFormatted DESC")
    fun getOpdTokensForPatient(patientId: String): Flow<List<OpdTokenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpdToken(token: OpdTokenEntity)

    // --- Medical Certificates ---
    @Query("SELECT * FROM medical_certificates ORDER BY issuedDateFormatted DESC")
    fun getAllMedicalCertificates(): Flow<List<MedicalCertificateEntity>>

    @Query("SELECT * FROM medical_certificates WHERE patientId = :patientId ORDER BY issuedDateFormatted DESC")
    fun getMedicalCertificatesForPatient(patientId: String): Flow<List<MedicalCertificateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalCertificate(certificate: MedicalCertificateEntity)

    // --- Blood Stock ---
    @Query("SELECT * FROM blood_stock ORDER BY bloodGroup ASC")
    fun getAllBloodStock(): Flow<List<BloodStockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBloodStockItem(item: BloodStockEntity)

    // --- IPD Beds ---
    @Query("SELECT * FROM ipd_beds ORDER BY wardName ASC, bedNumber ASC")
    fun getAllIpdBeds(): Flow<List<IpdBedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIpdBed(bed: IpdBedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllIpdBeds(beds: List<IpdBedEntity>)

    // --- OT Surgery Bookings ---
    @Query("SELECT * FROM ot_surgery_bookings ORDER BY scheduledDate DESC, scheduledTimeSlot ASC")
    fun getAllOtSurgeryBookings(): Flow<List<OtSurgeryBookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtSurgeryBooking(booking: OtSurgeryBookingEntity)

    // --- External Referrals ---
    @Query("SELECT * FROM external_referrals ORDER BY issuedDate DESC")
    fun getAllExternalReferrals(): Flow<List<ExternalReferralEntity>>

    @Query("SELECT * FROM external_referrals WHERE patientId = :patientId ORDER BY issuedDate DESC")
    fun getExternalReferralsForPatient(patientId: String): Flow<List<ExternalReferralEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExternalReferral(referral: ExternalReferralEntity)

    // --- Bio-Medical Equipment ---
    @Query("SELECT * FROM biomedical_equipment ORDER BY department ASC, name ASC")
    fun getAllBioMedicalEquipment(): Flow<List<BioMedicalEquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBioMedicalEquipment(equipment: BioMedicalEquipmentEntity)

    // --- Live Clinic Queue & Day Slots ---
    @Query("SELECT * FROM queue_entries WHERE doctorId = :doctorId AND dateFormatted = :date ORDER BY checkedInAt ASC")
    fun observeDoctorQueue(doctorId: String, date: String): Flow<List<QueueEntryEntity>>

    @Query("SELECT * FROM queue_entries WHERE patientId = :patientId AND dateFormatted = :date LIMIT 1")
    fun observePatientQueueEntry(patientId: String, date: String): Flow<QueueEntryEntity?>

    @Query("SELECT * FROM queue_entries WHERE doctorId = :doctorId AND dateFormatted = :date AND status = 'COMPLETED' ORDER BY completedAt DESC LIMIT :limit")
    suspend fun getRecentCompletedEntries(doctorId: String, date: String, limit: Int): List<QueueEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQueueEntry(entry: QueueEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQueueEntries(entries: List<QueueEntryEntity>)

    @Query("SELECT * FROM doctor_day_slots WHERE doctorId = :doctorId AND dateFormatted = :date")
    fun observeDoctorSlots(doctorId: String, date: String): Flow<List<DoctorDaySlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDoctorSlot(slot: DoctorDaySlotEntity)

    // --- Patient Medical History ---
    @Query("SELECT * FROM medical_history WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getMedicalHistoryForPatient(patientId: String): Flow<List<MedicalHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalHistoryEntry(entry: MedicalHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalHistoryEntries(entries: List<MedicalHistoryEntity>)

    // --- Nearby Pharmacy Cache ---
    @Query("SELECT * FROM nearby_pharmacy_cache")
    suspend fun getAllCachedPharmacies(): List<NearbyPharmacyCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedPharmacies(pharmacies: List<NearbyPharmacyCacheEntity>)

    // --- Call Logs ---
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntity>>

    @Query("SELECT * FROM call_logs WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getCallLogsForPatient(patientId: String): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogEntity)

    @Query("UPDATE appointments SET status = :status WHERE id = :appointmentId")
    suspend fun updateAppointmentStatus(appointmentId: String, status: String)

    // --- Doctor-to-Doctor Referrals ---
    @Query("SELECT * FROM referrals ORDER BY createdAt DESC")
    fun getAllReferrals(): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE patientId = :patientId ORDER BY createdAt DESC")
    fun getReferralsForPatient(patientId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE referringUserId = :doctorId ORDER BY createdAt DESC")
    fun getReferralsByReferringDoctor(doctorId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE targetDoctorId = :doctorId OR (targetDoctorId IS NULL AND targetSpecialty = :specialty) ORDER BY CASE urgency WHEN 'EMERGENCY' THEN 1 WHEN 'URGENT' THEN 2 ELSE 3 END, createdAt DESC")
    fun getReferralsForDoctorOrSpecialty(doctorId: String, specialty: String): Flow<List<ReferralEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: ReferralEntity)

    @Update
    suspend fun updateReferral(referral: ReferralEntity)

    // --- Audit Logs ---
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Query("SELECT * FROM audit_logs WHERE resourceId = :patientId ORDER BY timestamp DESC")
    fun getAuditLogsForPatient(patientId: String): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}



