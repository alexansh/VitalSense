package com.vitalsense.app.core.data.model

/**
 * The four distinct user roles in VitalSense.
 */
enum class UserRole(val label: String, val subtitle: String) {
    PATIENT("Patient", "Rural End-User Health Tracking"),
    ASHA("ASHA Worker", "Community Health & Proxy Care"),
    DOCTOR("Doctor", "Clinical Review & Prescriptions"),
    ADMIN("Admin", "District Disease Outbreak Surveillance")
}

/**
 * Clinical severity levels used throughout the app and disease heat map.
 */
enum class SeverityLevel(val displayName: String, val badgeColorHex: Long) {
    LOW("Low Risk", 0xFFC8F5D4),        // Soft Mint
    MODERATE("Moderate Risk", 0xFFFFD166), // Amber Warning
    HIGH("High Risk", 0xFFFF9F43),      // Orange
    SEVERE("Severe Alert", 0xFFFF6B6B)  // Coral Alert
}

/**
 * Health condition and topic categories.
 */
enum class ConditionCategory(val displayName: String, val emoji: String, val colorHex: Long) {
    GENERAL_MEDICINE("General Medicine", "💊", 0xFFE8EB7D),
    NUTRITION("Nutrition & Diet", "🍳", 0xFFFFB8F0),
    FITNESS("Fitness & Physical", "🏃", 0xFFC8F5D4),
    MENTAL_HEALTH("Mental Wellness", "🧠", 0xFFA3AEFE),
    MATERNAL_HEALTH("Maternal Health", "🤱", 0xFFFFD166),
    EMERGENCY("Emergency Help", "🚨", 0xFFFF6B6B)
}

/**
 * Doctor specialties.
 */
enum class DoctorSpecialty(val displayName: String) {
    GENERAL_PHYSICIAN("General Physician"),
    PSYCHOLOGIST("Psychologist & Mental Health"),
    PEDIATRICIAN("Pediatrician"),
    GYNECOLOGIST("Gynecologist & Maternal Care"),
    NEUROLOGIST("Neurologist"),
    CARDIOLOGIST("Cardiologist")
}

/**
 * Lifecycle states for clinical cases (§4.2).
 */
enum class CaseStatus(val displayName: String, val colorHex: Long) {
    PENDING_REVIEW("Pending Review", 0xFFFFD166), // Amber
    IN_PROGRESS("In Progress", 0xFFA3AEFE),       // Lavender
    RESPONDED("Responded", 0xFFC8F5D4),           // Mint Green
    REFERRED("Referred to Specialist", 0xFFFFB8F0),// Blush Pink
}

/**
 * Offline-first synchronization state.
 */
enum class SyncState {
    PENDING,
    SYNCED,
    CONFLICT,
    FAILED
}

/**
 * Department types in a hospital — clinical departments treat patients,
 * service departments provide diagnostic reports back to the referring doctor.
 */
enum class DepartmentType(val displayName: String) {
    CLINICAL("Clinical"),
    SERVICE("Diagnostic / Service")
}

/**
 * Referral types following real hospital inter-department referral patterns.
 */
enum class ReferralType(val displayName: String, val emoji: String) {
    CLINICAL("Clinical Referral", "🔄"),
    CO_MANAGEMENT("Co-Management", "🤝"),
    SERVICE("Diagnostic / Service Request", "🧪"),
    EMERGENCY("Emergency Transfer", "🚨")
}

/**
 * Urgency levels for referrals — maps to real hospital triage timelines.
 */
enum class ReferralUrgency(val displayName: String, val colorHex: Long) {
    ROUTINE("Routine", 0xFFC8F5D4),
    PRIORITY("Priority", 0xFFFFD166),
    URGENT("Urgent", 0xFFFF9F43),
    EMERGENCY("Emergency", 0xFFFF6B6B)
}

/**
 * Lifecycle status of a referral.
 */
enum class ReferralStatus(val displayName: String, val colorHex: Long) {
    PENDING("Pending Acceptance", 0xFFFFD166),
    ACCEPTED("Accepted", 0xFFA3AEFE),
    IN_PROGRESS("In Progress", 0xFF90CAF9),
    REPORT_SUBMITTED("Report Submitted", 0xFFC8F5D4),
    COMPLETED("Completed", 0xFF81C784),
    CANCELLED("Cancelled", 0xFFB0BEC5)
}
