package com.vitalsense.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vitalsense.app.core.data.model.*

@Entity(tableName = "villages")
data class VillageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val district: String,
    val state: String,
    val population: Int,
    val latitude: Double,
    val longitude: Double,
    val activeCases: Int,
    val highRiskCount: Int
)

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val phone: String,
    val villageId: String,
    val villageName: String,
    val ashaWorkerId: String,
    val ashaWorkerName: String,
    val currentRiskLevel: SeverityLevel,
    val lastCondition: String,
    val lastVisitDate: String,
    val nextAppointmentDate: String?,
    val emergencyContact: String,
    val profilePhotoUrl: String? = null
)

@Entity(tableName = "asha_workers")
data class AshaWorkerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ashaUniqueId: String,
    val phone: String,
    val assignedVillagesJson: String,
    val activePatientCount: Int,
    val alertCount: Int
)

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: DoctorSpecialty,
    val qualification: String,
    val hospitalName: String,
    val distanceKm: Double,
    val phone: String,
    val availableDays: String
)

@Entity(tableName = "condition_records")
data class ConditionRecordEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val villageId: String,
    val villageName: String,
    val category: ConditionCategory,
    val severity: SeverityLevel,
    val requestedDoctorType: DoctorSpecialty,
    val notes: String,
    val timestamp: Long,
    val ashaProxyLogged: Boolean = false,
    val status: CaseStatus = CaseStatus.PENDING_REVIEW,
    val assignedDoctorId: String? = null,
    val assignedDoctorName: String? = null,
    val doctorResponse: String? = null,
    val doctorResponseTimestamp: Long? = null,
    val doctorResponseDoctorName: String? = null,
    val privateDoctorNotes: String? = null,
    val referredByDoctorId: String? = null,
    val referredByDoctorName: String? = null,
    val referralNotes: String? = null,
    val isPendingSync: Boolean = false
)

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey val id: String,
    val caseId: String? = null,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val timestamp: Long,
    val dateFormatted: String,
    val medicinesJson: String,
    val instructions: String,
    val isOcrExtracted: Boolean = false
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val dateFormatted: String,
    val timeSlot: String,
    val status: String,
    val proposedBy: UserRole,
    val outcomeNotes: String? = null,
    val callType: String = "VIDEO",
    val scheduledTimestamp: Long = 0L
)

@Entity(tableName = "broadcast_notices")
data class BroadcastNoticeEntity(
    @PrimaryKey val id: String,
    val senderRole: UserRole,
    val senderName: String,
    val targetRole: String,
    val targetVillage: String?,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isUrgent: Boolean = false
)

@Entity(tableName = "dispensary_stock")
data class DispensaryEntity(
    @PrimaryKey val id: String,
    val medicineName: String,
    val category: String,
    val availableQuantity: Int,
    val unit: String,
    val reorderThreshold: Int,
    val lastRestockDateFormatted: String? = null
)

@Entity(tableName = "government_schemes")
data class GovernmentSchemeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val targetBeneficiary: String,
    val benefitsSummary: String,
    val eligibility: String,
    val applicationUrl: String = ""
)

@Entity(tableName = "immunization_records")
data class ImmunizationRecordEntity(
    @PrimaryKey val id: String,
    val childName: String,
    val motherName: String,
    val dobFormatted: String,
    val gender: String,
    val villageName: String,
    val ashaWorkerId: String,
    val vaccinesJson: String // List<VaccineInfo>
)

@Entity(tableName = "daily_rounds")
data class DailyRoundEntity(
    @PrimaryKey val id: String,
    val dateFormatted: String,
    val villageName: String,
    val householdName: String,
    val personName: String,
    val ashaWorkerId: String,
    val purpose: String,
    val isPregnancyChecked: Boolean,
    val isChildHealthChecked: Boolean,
    val isImmunizationChecked: Boolean,
    val isMedicineGiven: Boolean,
    val isCounsellingDone: Boolean,
    val notes: String,
    val status: String // "Pending", "Completed"
)

@Entity(tableName = "asha_medicines")
data class AshaMedicineEntity(
    @PrimaryKey val id: String,
    val ashaWorkerId: String,
    val medicineName: String,
    val availableQuantity: Int,
    val unit: String,
    val minStockQuantity: Int,
    val expiryDateFormatted: String,
    val lastRestockDateFormatted: String?
)

@Entity(tableName = "disease_trend_records")
data class DiseaseTrendRecordEntity(
    @PrimaryKey val id: String,
    val villageName: String,
    val diseaseName: String,
    val caseCount: Int,
    val dateFormatted: String,
    val severity: String?
)

@Entity(tableName = "lab_reports")
data class LabReportEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val testCategory: String,
    val doctorName: String,
    val dateFormatted: String,
    val items: List<com.vitalsense.app.core.data.model.LabTestItem>,
    val notes: String,
    val status: String
)

@Entity(tableName = "opd_tokens")
data class OpdTokenEntity(
    @PrimaryKey val id: String,
    val tokenNumber: String,
    val patientId: String,
    val patientName: String,
    val doctorName: String,
    val department: String,
    val cabinNumber: String,
    val currentServingToken: String,
    val estimatedWaitMinutes: Int,
    val status: String,
    val dateFormatted: String
)

@Entity(tableName = "medical_certificates")
data class MedicalCertificateEntity(
    @PrimaryKey val id: String,
    val certificateNumber: String,
    val patientId: String,
    val patientName: String,
    val patientAge: Int,
    val patientGender: String,
    val doctorName: String,
    val doctorRegistrationNumber: String,
    val diagnosis: String,
    val restStartDate: String,
    val restEndDate: String,
    val fitDate: String,
    val certificateType: String,
    val issuedDateFormatted: String
)

@Entity(tableName = "blood_stock")
data class BloodStockEntity(
    @PrimaryKey val id: String,
    val bloodGroup: String,
    val unitsAvailable: Int,
    val hospitalName: String,
    val contactPhone: String,
    val status: String
)

@Entity(tableName = "ipd_beds")
data class IpdBedEntity(
    @PrimaryKey val id: String,
    val wardName: String,
    val bedNumber: String,
    val isOccupied: Boolean,
    val patientId: String?,
    val patientName: String?,
    val admissionDate: String?,
    val attendingDoctorName: String?,
    val diagnosis: String?,
    val nurseInCharge: String?
)

@Entity(tableName = "ot_surgery_bookings")
data class OtSurgeryBookingEntity(
    @PrimaryKey val id: String,
    val otRoomName: String,
    val patientId: String,
    val patientName: String,
    val surgeryName: String,
    val surgeonName: String,
    val anesthetistName: String,
    val scheduledDate: String,
    val scheduledTimeSlot: String,
    val pacCleared: Boolean,
    val status: String
)

@Entity(tableName = "external_referrals")
data class ExternalReferralEntity(
    @PrimaryKey val id: String,
    val referralLetterId: String,
    val patientId: String,
    val patientName: String,
    val referringDoctorName: String,
    val empanelledHospitalName: String,
    val specialtyRequired: String,
    val clinicalSummary: String,
    val isCashlessApproved: Boolean,
    val ambulanceRequisitioned: Boolean,
    val issuedDate: String,
    val status: String
)

@Entity(tableName = "biomedical_equipment")
data class BioMedicalEquipmentEntity(
    @PrimaryKey val id: String,
    val assetCode: String,
    val name: String,
    val department: String,
    val status: String,
    val lastServiceDate: String,
    val nextServiceDue: String,
    val location: String,
    val inChargeContact: String
)

@Entity(tableName = "doctor_day_slots")
data class DoctorDaySlotEntity(
    @PrimaryKey val id: String,
    val doctorId: String,
    val dateFormatted: String,
    val startTime: String,
    val endTime: String,
    val capacity: Int,
    val isWalkInOpen: Boolean
)

@Entity(tableName = "queue_entries")
data class QueueEntryEntity(
    @PrimaryKey val id: String,
    val doctorId: String,
    val doctorName: String,
    val dateFormatted: String,
    val tokenNumber: Int,
    val provisionalToken: Boolean,
    val appointmentId: String?,
    val patientId: String,
    val patientName: String,
    val source: com.vitalsense.app.core.data.model.QueueEntrySource,
    val status: com.vitalsense.app.core.data.model.QueueEntryStatus,
    val priorityFlag: Boolean,
    val checkedInAt: Long,
    val calledAt: Long?,
    val consultationStartedAt: Long?,
    val completedAt: Long?,
    val outcomeNotes: String?,
    val isPendingSync: Boolean
)

@Entity(tableName = "medical_history")
data class MedicalHistoryEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val type: String, // "CONDITION" or "MEDICATION"
    val title: String,
    val details: String,
    val severity: String?, // SeverityLevel name or null
    val doctorId: String,
    val doctorName: String,
    val caseId: String?,
    val prescriptionId: String?,
    val timestamp: Long,
    val dateFormatted: String
)

@Entity(tableName = "nearby_pharmacy_cache")
data class NearbyPharmacyCacheEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String?,
    val cachedAt: Long
)

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey val id: String,
    val callType: String,
    val callMode: String,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val outcome: String,
    val outcomeNotes: String? = null
)

@Entity(
    tableName = "referrals",
    indices = [
        Index("patientId"),
        Index("referringUserId"),
        Index("targetDoctorId"),
        Index("targetSpecialty")
    ]
)
data class ReferralEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val referringUserId: String,
    val referringUserName: String,
    val referringUserSpecialty: String,
    val targetDoctorId: String?,
    val targetDoctorName: String?,
    val targetSpecialty: String,
    val reason: String,
    val clinicalQuestion: String,
    val urgency: ReferralUrgency,
    val attachedRecordIds: List<String>,
    val status: ReferralStatus,
    val statusHistory: List<ReferralStatusHistory>,
    val declineReason: String?,
    val suggestedSpecialtyOrDoctor: String?,
    val infoRequestNote: String?,
    val specialistFindings: String?,
    val specialistRecommendations: String?,
    val specialistFollowUpNeeded: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val respondedAt: Long?,
    val completedAt: Long?
) {
    fun toDomainModel(): Referral = Referral(
        id = id,
        patientId = patientId,
        patientName = patientName,
        referringUserId = referringUserId,
        referringUserName = referringUserName,
        referringUserSpecialty = referringUserSpecialty,
        targetDoctorId = targetDoctorId,
        targetDoctorName = targetDoctorName,
        targetSpecialty = targetSpecialty,
        reason = reason,
        clinicalQuestion = clinicalQuestion,
        urgency = urgency,
        attachedRecordIds = attachedRecordIds,
        status = status,
        statusHistory = statusHistory,
        declineReason = declineReason,
        suggestedSpecialtyOrDoctor = suggestedSpecialtyOrDoctor,
        infoRequestNote = infoRequestNote,
        specialistFindings = specialistFindings,
        specialistRecommendations = specialistRecommendations,
        specialistFollowUpNeeded = specialistFollowUpNeeded,
        createdAt = createdAt,
        updatedAt = updatedAt,
        respondedAt = respondedAt,
        completedAt = completedAt
    )
    fun toModel(): Referral = toDomainModel()
}

fun Referral.toEntity(): ReferralEntity = ReferralEntity(
    id = id,
    patientId = patientId,
    patientName = patientName,
    referringUserId = referringUserId,
    referringUserName = referringUserName,
    referringUserSpecialty = referringUserSpecialty,
    targetDoctorId = targetDoctorId,
    targetDoctorName = targetDoctorName,
    targetSpecialty = targetSpecialty,
    reason = reason,
    clinicalQuestion = clinicalQuestion,
    urgency = urgency,
    attachedRecordIds = attachedRecordIds,
    status = status,
    statusHistory = statusHistory,
    declineReason = declineReason,
    suggestedSpecialtyOrDoctor = suggestedSpecialtyOrDoctor,
    infoRequestNote = infoRequestNote,
    specialistFindings = specialistFindings,
    specialistRecommendations = specialistRecommendations,
    specialistFollowUpNeeded = specialistFollowUpNeeded,
    createdAt = createdAt,
    updatedAt = updatedAt,
    respondedAt = respondedAt,
    completedAt = completedAt
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val actorId: String,
    val actorRole: String,
    val action: String,
    val resourceId: String?,
    val resourceType: String?,
    val details: String?,
    val isSynced: Boolean
) {
    fun toModel(): com.vitalsense.app.core.data.model.AuditLog = com.vitalsense.app.core.data.model.AuditLog(
        id = id,
        timestamp = timestamp,
        actorId = actorId,
        actorRole = actorRole,
        action = action,
        resourceId = resourceId,
        resourceType = resourceType,
        details = details,
        isSynced = isSynced
    )
}

fun com.vitalsense.app.core.data.model.AuditLog.toEntity(): AuditLogEntity = AuditLogEntity(
    id = id,
    timestamp = timestamp,
    actorId = actorId,
    actorRole = actorRole,
    action = action,
    resourceId = resourceId,
    resourceType = resourceType,
    details = details,
    isSynced = isSynced
)
