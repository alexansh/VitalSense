package com.vitalsense.app.core.data.local.entity

import androidx.room.Entity
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
    val highRiskCount: Int,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val profilePhotoUrl: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)

@Entity(tableName = "asha_workers")
data class AshaWorkerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ashaUniqueId: String,
    val phone: String,
    val assignedVillagesJson: String,
    val activePatientCount: Int,
    val alertCount: Int,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val availableDays: String,
    val departmentId: String = "dept_general_medicine",
    val departmentName: String = "General Medicine",
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val isOcrExtracted: Boolean = false,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
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
    val isUrgent: Boolean = false,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)

@Entity(tableName = "dispensary_stock")
data class DispensaryEntity(
    @PrimaryKey val id: String,
    val medicineName: String,
    val category: String,
    val availableQuantity: Int,
    val unit: String,
    val reorderThreshold: Int
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

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val emoji: String,
    val type: DepartmentType,
    val colorHex: Long,
    val headDoctorId: String? = null,
    val headDoctorName: String? = null,
    val isActive: Boolean = true,
    val availableDoctorCount: Int = 0,
    val pendingReferralCount: Int = 0,
    val description: String = "",
    val operatingHours: String = "24x7",
    val location: String = "",
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)

@Entity(tableName = "referrals")
data class ReferralEntity(
    @PrimaryKey val id: String,
    val caseId: String,
    val patientId: String,
    val patientName: String,
    val fromDoctorId: String,
    val fromDoctorName: String,
    val fromDepartmentId: String,
    val fromDepartmentName: String,
    val toDepartmentId: String,
    val toDepartmentName: String,
    val toDoctorId: String? = null,
    val toDoctorName: String? = null,
    val referralType: ReferralType,
    val urgency: ReferralUrgency,
    val reason: String,
    val clinicalNotes: String = "",
    val clinicalHistory: String = "",
    val status: ReferralStatus = ReferralStatus.PENDING,
    val acceptedByDoctorId: String? = null,
    val acceptedByDoctorName: String? = null,
    val acceptedAt: Long? = null,
    val serviceReportText: String? = null,
    val serviceReportAttachmentPath: String? = null,
    val serviceReportAttachmentUrl: String? = null,
    val serviceReportTimestamp: Long? = null,
    val parentReferralId: String? = null,
    val referralChainIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)
