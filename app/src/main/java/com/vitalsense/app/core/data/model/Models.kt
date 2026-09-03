package com.vitalsense.app.core.data.model

data class Village(
    val id: String,
    val name: String,
    val district: String,
    val state: String,
    val population: Int,
    val latitude: Double,
    val longitude: Double,
    val activeCases: Int,
    val highRiskCount: Int
)

data class Patient(
    val id: String,
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

data class AshaWorker(
    val id: String,
    val name: String,
    val ashaUniqueId: String,
    val phone: String,
    val assignedVillages: List<String>,
    val activePatientCount: Int,
    val alertCount: Int
)

data class Doctor(
    val id: String,
    val name: String,
    val specialty: DoctorSpecialty,
    val qualification: String,
    val hospitalName: String,
    val distanceKm: Double,
    val phone: String,
    val availableDays: String,
    val departmentId: String = "dept_general_medicine",
    val departmentName: String = "General Medicine"
)

data class ConditionRecord(
    val id: String,
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

data class PrescribedMedicine(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val quantity: Int
)

data class Prescription(
    val id: String,
    val caseId: String? = null,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val timestamp: Long,
    val dateFormatted: String,
    val medicines: List<PrescribedMedicine>,
    val instructions: String,
    val isOcrExtracted: Boolean = false,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)

data class Appointment(
    val id: String,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val dateFormatted: String,
    val timeSlot: String,
    val status: String, // "Confirmed", "Pending", "Declined", "Completed"
    val proposedBy: UserRole,
    val outcomeNotes: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
    val serverVersion: Long = 0L
)

data class BroadcastNotice(
    val id: String,
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

data class DispensaryItem(
    val id: String,
    val medicineName: String,
    val category: String,
    val availableQuantity: Int,
    val unit: String,
    val reorderThreshold: Int
) {
    val isLowStock: Boolean
        get() = availableQuantity <= reorderThreshold
}

data class GovernmentScheme(
    val id: String,
    val title: String,
    val category: String,
    val targetBeneficiary: String,
    val benefitsSummary: String,
    val eligibility: String,
    val applicationUrl: String = ""
)

/**
 * Hospital department — database-driven, admin-configurable.
 */
data class Department(
    val id: String,
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

/**
 * Inter-department referral with full chain tracking.
 */
data class Referral(
    val id: String,
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

/**
 * Bundled patient history for auto-sharing when a doctor accepts a referral.
 */
data class PatientHistory(
    val patient: Patient,
    val conditions: List<ConditionRecord> = emptyList(),
    val prescriptions: List<Prescription> = emptyList(),
    val appointments: List<Appointment> = emptyList(),
    val referrals: List<Referral> = emptyList()
)
