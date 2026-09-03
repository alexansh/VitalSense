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

    @Query("SELECT * FROM doctors WHERE departmentId = :deptId")
    fun getDoctorsByDepartment(deptId: String): Flow<List<DoctorEntity>>

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

    // --- Sync Worker Queries ---
    @Query("SELECT * FROM patients WHERE syncState = 'PENDING'")
    suspend fun getPendingPatients(): List<PatientEntity>

    @Query("SELECT * FROM condition_records WHERE syncState = 'PENDING'")
    suspend fun getPendingConditionRecords(): List<ConditionRecordEntity>

    @Query("SELECT * FROM prescriptions WHERE syncState = 'PENDING'")
    suspend fun getPendingPrescriptions(): List<PrescriptionEntity>

    @Query("SELECT * FROM appointments WHERE syncState = 'PENDING'")
    suspend fun getPendingAppointments(): List<AppointmentEntity>

    @Query("SELECT * FROM broadcast_notices WHERE syncState = 'PENDING'")
    suspend fun getPendingNotices(): List<BroadcastNoticeEntity>

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

    // --- Departments ---
    @Query("SELECT * FROM departments WHERE isActive = 1 ORDER BY type ASC, name ASC")
    fun getActiveDepartments(): Flow<List<DepartmentEntity>>

    @Query("SELECT * FROM departments ORDER BY type ASC, name ASC")
    fun getAllDepartments(): Flow<List<DepartmentEntity>>

    @Query("SELECT * FROM departments WHERE id = :id")
    fun getDepartmentById(id: String): Flow<DepartmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<DepartmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    // --- Referrals ---
    @Query("SELECT * FROM referrals ORDER BY createdAt DESC")
    fun getAllReferrals(): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE patientId = :patientId ORDER BY createdAt DESC")
    fun getReferralsForPatient(patientId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE caseId = :caseId ORDER BY referralChainIndex ASC")
    fun getReferralChainForCase(caseId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE toDepartmentId = :deptId AND status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS') ORDER BY CASE urgency WHEN 'EMERGENCY' THEN 0 WHEN 'URGENT' THEN 1 WHEN 'PRIORITY' THEN 2 ELSE 3 END, createdAt ASC")
    fun getIncomingReferralsForDepartment(deptId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE (toDoctorId = :doctorId OR (toDepartmentId = :deptId AND toDoctorId IS NULL)) AND status = 'PENDING' ORDER BY CASE urgency WHEN 'EMERGENCY' THEN 0 WHEN 'URGENT' THEN 1 WHEN 'PRIORITY' THEN 2 ELSE 3 END, createdAt ASC")
    fun getPendingReferralsForDoctor(doctorId: String, deptId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE fromDoctorId = :doctorId ORDER BY createdAt DESC")
    fun getSentReferralsByDoctor(doctorId: String): Flow<List<ReferralEntity>>

    @Query("SELECT * FROM referrals WHERE id = :id")
    fun getReferralById(id: String): Flow<ReferralEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: ReferralEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferrals(referrals: List<ReferralEntity>)

    @Query("SELECT * FROM referrals WHERE syncState = 'PENDING'")
    suspend fun getPendingReferrals(): List<ReferralEntity>
}
