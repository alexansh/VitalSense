package com.vitalsense.app.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Supported application languages.
 * Display name and native script name are provided for high-accessibility selection.
 */
enum class AppLanguage(val code: String, val displayName: String, val nativeName: String, val buttonLabel: String) {
    ENGLISH("en", "English", "English", "EN"),
    HINDI("hi", "Hindi", "हिन्दी", "हिन्दी"),
    TAMIL("ta", "Tamil", "தமிழ்", "தமிழ்"),
    MARATHI("mr", "Marathi", "मराठी", "मराठी")
}

interface AppStrings {

    val appName: String
    val tagline: String
    val online: String
    val offline: String
    val exit: String
    val cancel: String
    val done: String
    val save: String
    val submit: String
    val urgent: String
    val active: String
    val highPriority: String
    val highRisk: String
    val moderateRisk: String
    val lowRisk: String

    // Roles
    val rolePatient: String
    val rolePatientDesc: String
    val roleAsha: String
    val roleAshaDesc: String
    val roleDoctor: String
    val roleDoctorDesc: String
    val roleAdmin: String
    val roleAdminDesc: String

    // Login Screen
    val whoIsUsing: String
    val selectRoleDesc: String
    val patientSignIn: String
    val ashaSignIn: String
    val doctorSignIn: String
    val adminSignIn: String
    val mobileNumber: String
    val ashaHelperIdOptional: String
    val uniqueAshaId: String
    val securityPin: String
    val doctorEmail: String
    val password: String
    val adminPasscode: String
    val logInAsPatient: String
    val logInAsAsha: String
    val logInAsDoctor: String
    val logInAsAdmin: String
    val quickDemoLogin: String
    val offlineBanner: String

    // Top Bar
    val patientPortal: String
    val ashaPortal: String
    val doctorPortal: String
    val adminPortal: String
    val actingAsProxy: String
    val exitProxy: String

    // Patient Home
    val namaste: String
    val village: String
    val ashaAssigned: String
    val patientGuideTitle: String
    val patientGuideMsg: String
    val offlineHealthCard: String
    val viewCard: String
    val activeCondition: String
    val nextCheckup: String
    val noneScheduled: String
    val cachedOffline: String
    val howCanWeHelp: String
    val tapServiceDesc: String
    val myPrescriptions: String
    val uploadRx: String
    val noPrescriptions: String
    val scanOrWrite: String
    val districtAdvisories: String
    val issuedBy: String
    val emergencySos: String
    val emergencySosDesc: String
    val trigger: String
    val confirmSosTitle: String
    val confirmSosMsg: String
    val yesSendAlert: String
    val sosDispatchedTitle: String
    val sosDispatchedMsg: String
    val zeroInternetFallbacks: String
    val smsAsha: String
    val call108: String

    // Categories
    val catGeneralMedicine: String
    val catMaternalHealth: String
    val catFitness: String
    val catNutrition: String
    val catMentalHealth: String
    val catEmergency: String

    // ASHA Home
    val assignedVillages: String
    val uniqueAshaCardTitle: String
    val shareAshaIdDesc: String
    val newPatient: String
    val sendNotice: String
    val villageCaseload: String
    val noPatientsYet: String
    val scanRx: String
    val proxyMode: String
    val emergencyPatientAlerts: String
    val sosAlertForPatient: String
    val confirmSosPatientMsg: String
    val sosDispatchedForPatient: String
    val sosFailedForPatient: String
    val retry: String

    // Doctor Home & Case Detail
    val pendingCases: String
    val criticalCases: String
    val scheduledAppts: String
    val activeInQueue: String
    val specialistQueue: String
    val noPendingCases: String
    val symptoms: String
    val review: String
    val history: String
    val upcomingConsultations: String
    val proposeAppt: String
    val dispensaryStock: String
    val lowStock: String
    val patientDirectory: String
    val searchPatient: String
    val searchPlaceholder: String
    val caseQueue: String
    val reportedSymptoms: String
    val doctorAdviceTitle: String
    val quickTemplates: String
    val issueRx: String
    val refer: String
    val submitAdvice: String
    val updateAdvice: String

    // Admin Home
    val districtCommand: String
    val surveillanceRegion: String
    val totalActiveCases: String
    val monitoredVillages: String
    val outbreakSurveillance: String
    val liveTelemetry: String
    val broadcastAlertBtn: String
    val dispatchedDirectives: String
    val dispensaryInventory: String

    // Prescription & OCR
    val uploadPrescriptionTitle: String
    val cameraAiScan: String
    val writeDown: String
    val onDeviceAiBadge: String
    val selectDocSample: String
    val extractedRawText: String
    val parsedMedicines: String
    val saveDigitizedRx: String
    val addMedicine: String
    val medicineName: String
    val dosage: String
    val duration: String
    val frequency: String
    val savePrescriptionRecord: String

    // Calling & Teleconsultation
    val callRinging: String
    val callConnecting: String
    val callOngoing: String
    val callEnded: String
    val endCall: String
    val mute: String
    val unmute: String
    val speaker: String
    val videoCall: String
    val voiceCall: String
    val emergencyDoctorCall: String
    val routineConsultCall: String
    val autoEscalatingInSeconds: String
    val emergencyDoctorOnCall: String

    // Medicine Availability & Nearby Stores
    val medicineAvailabilityTitle: String
    val nearbyPharmaciesTitle: String
    val inStock: String
    val outOfStock: String
    val limitedStock: String
    val alternativesAvailable: String
    val findNearbyStores: String
    val openInMaps: String

    // Specialist Referrals
    val doctorReferralsTitle: String
    val referToSpecialistTitle: String
    val chooseSpecialty: String
    val urgencyLevel: String
    val clinicalQuestionTitle: String
    val reasonForReferral: String
    val attachRecords: String
    val acceptReferral: String
    val declineReferral: String
    val requestMoreInfo: String
    val specialistFindingsTitle: String
    val specialistRecommendationsTitle: String
    val submitSpecialistFindings: String
    val referralSentSuccess: String
    val referralCompleted: String
    val awaitingSpecialistReview: String
    val specialistAccepted: String
    val specialistDeclined: String

    // Language Dialog
    val selectLanguageTitle: String
    val selectLanguageSubtitle: String
    val currentLanguageBadge: String
    val applyLanguage: String

    // Hospital Queue
    val liveQueueTitle: String
    val tokenNumber: String
    val callNextPatient: String
    val startConsultation: String
    val completeConsultation: String
    val noShow: String
    val skipPatient: String
    val prioritizePatient: String

    // Doctor Analytics & Medical History
    val triageBreakdownTitle: String
    val statTotal: String
    val statPending: String
    val statResolved: String
    val statReferred: String
    val medicalHistoryTitle: String
    val noMedicalHistory: String
    val medicalHistoryTab: String

    // Extended Hospital Hub & Complete Language Pack Tokens
    val liveQueueAndAppointments: String
    val liveQueueDesc: String
    val hud: String
    val book: String
    val hospitalClinicalServices: String
    val hospitalServicesDesc: String
    val labReports: String
    val labReportsSub: String
    val opdQueue: String
    val opdQueueSub: String
    val bloodBank: String
    val bloodBankSub: String
    val consultationCardTitle: String
    val routineBadge: String
    val consultationCardDesc: String
    val routineCallButton: String
    val liveTokenTracker: String
    val viewLabDiagnostics: String
    val findDonors: String
    val opdLiveQueueAndTokens: String
    val opdSubtitle: String
    val bookOpdToken: String
    val bookHospitalOpdToken: String
    val yourActiveTokens: String
    val noActiveTokens: String
    val estimatedWait: String
    val currentServing: String
    val cabin: String
    val selectDepartment: String
    val selectDoctor: String
    val patientFullName: String
    val patientPhone: String
    val reasonForVisit: String
    val confirmBooking: String
    val tokenGeneratedSuccess: String
    val bloodBankRegistry: String
    val bloodBankSubtitle: String
    val bloodUnitsAvailable: String
    val callBloodBank: String
    val requestBloodUnits: String
    val filterBloodGroup: String
    val allGroups: String
    val donorDirectory: String
    val units: String
    val lastUpdated: String
    val diagnosticLabReports: String
    val labReportsSubtitle: String
    val downloadReport: String
    val normalRange: String
    val sampleCollected: String
    val reportDelivered: String
    val noLabReportsFound: String
    val testParameters: String
    val interpretation: String
    val ipdBedTracker: String
    val ipdSubtitle: String
    val icuBeds: String
    val oxygenBeds: String
    val generalWard: String
    val occupied: String
    val available: String
    val totalBeds: String
    val admitPatient: String
    val dischargePatient: String
    val otScheduler: String
    val otSubtitle: String
    val emergencyOt: String
    val bookOtSlot: String
    val surgeonInCharge: String
    val procedure: String
    val scheduledTime: String
    val otStatus: String
    val bioMedicalTracker: String
    val bioMedicalSubtitle: String
    val ventilators: String
    val defibrillators: String
    val dialysisUnits: String
    val operational: String
    val underMaintenance: String
    val reportFault: String
    val openHud: String
    val clinicalWorkstation: String
    val activeOpdQueue: String
    val inCallHud: String
    val teleConsultInProgress: String
    val muteMic: String
    val unmuteMic: String
    val turnVideoOff: String
    val turnVideoOn: String
    val switchCamera: String
    val liveTeleVitals: String
    val networkQuality: String
    val goodConnection: String
    val poorConnection: String
    val prescribeDuringCall: String
    val liveQueueHud: String
    val bookACall: String
    val bookTeleConsultation: String
    val bookConsultationSubtitle: String
    val scheduledAppointments: String
    val noUpcomingAppointments: String
    val joinCall: String
    val selectDate: String
    val selectTimeSlot: String
    val consultationType: String
    val navHome: String
    val navAppointments: String
    val navPrescriptions: String
    val navSettings: String
    val hudStatusSafe: String
    val hudStatusAttention: String
    val hudStatusDanger: String
    val hudStatusCritical: String
    val hudMonitoring: String
    val appointmentReminderTitle: String
    val appointmentReminderBody: String

    // Low Connectivity & Sync
    val slowNetwork: String
    val syncing: String
    val pendingChanges: String
    val lastSynchronized: String
    val offlineSosWarning: String
    val cachedDataFreshness: String
    val syncComplete: String
    val manualSync: String

}

class EnglishAppStrings : AppStrings {

    override val appName: String = "VitalSense"
    override val tagline: String = "SehatSetu — Rural Health Bridge"
    override val online: String = "Online"
    override val offline: String = "Offline"
    override val exit: String = "Exit"
    override val cancel: String = "Cancel"
    override val done: String = "Done"
    override val save: String = "Save"
    override val submit: String = "Submit"
    override val urgent: String = "URGENT"
    override val active: String = "ACTIVE"
    override val highPriority: String = "HIGH PRIORITY"
    override val highRisk: String = "High Risk"
    override val moderateRisk: String = "Moderate Risk"
    override val lowRisk: String = "Low Risk"

    override val rolePatient: String = "Patient"
    override val rolePatientDesc: String = "Health card & SOS"
    override val roleAsha: String = "ASHA Worker"
    override val roleAshaDesc: String = "Caseload & Proxy"
    override val roleDoctor: String = "Doctor"
    override val roleDoctorDesc: String = "Review & Prescribe"
    override val roleAdmin: String = "Admin"
    override val roleAdminDesc: String = "Outbreak Trends"

    override val whoIsUsing: String = "Who is using the app?"
    override val selectRoleDesc: String = "Select your role to access your dedicated healthcare portal:"
    override val patientSignIn: String = "👤 Patient Sign-In"
    override val ashaSignIn: String = "🤝 ASHA Worker Sign-In"
    override val doctorSignIn: String = "🩺 Doctor Clinical Portal"
    override val adminSignIn: String = "🛡️ District Health Admin"
    override val mobileNumber: String = "Mobile Number"
    override val ashaHelperIdOptional: String = "ASHA Helper ID (Optional)"
    override val uniqueAshaId: String = "Unique ASHA ID"
    override val securityPin: String = "4-Digit Security PIN"
    override val doctorEmail: String = "Medical Registration / Email"
    override val password: String = "Password"
    override val adminPasscode: String = "District Admin Passcode"
    override val logInAsPatient: String = "Log In as Patient →"
    override val logInAsAsha: String = "Log In to ASHA Caseload →"
    override val logInAsDoctor: String = "Log In to Clinical Portal →"
    override val logInAsAdmin: String = "Enter District Health Command →"
    override val quickDemoLogin: String = "⚡ Quick 1-Tap Demo Login:"
    override val offlineBanner: String = "📶 Offline-First: Health Card & Core Tools Work With Zero Internet"

    override val patientPortal: String = "Patient Portal"
    override val ashaPortal: String = "ASHA Worker Caseload"
    override val doctorPortal: String = "Clinical Review Portal"
    override val adminPortal: String = "District Outbreak Command"
    override val actingAsProxy: String = "Acting as Proxy for Patient:"
    override val exitProxy: String = "Exit Proxy"

    override val namaste: String = "Namaste"
    override val village: String = "Village"
    override val ashaAssigned: String = "ASHA"
    override val patientGuideTitle: String = "Your Rural Health Portal"
    override val patientGuideMsg: String = "Tap any health category below to log symptoms, check prescriptions, or connect with your ASHA helper."
    override val offlineHealthCard: String = "OFFLINE HEALTH CARD"
    override val viewCard: String = "View Card →"
    override val activeCondition: String = "Active Condition:"
    override val nextCheckup: String = "Next Checkup:"
    override val noneScheduled: String = "None Scheduled"
    override val cachedOffline: String = "Cached Offline ✓"
    override val howCanWeHelp: String = "How can we help you today?"
    override val tapServiceDesc: String = "Tap a service to report health condition or consult a doctor:"
    override val myPrescriptions: String = "💊 My Prescriptions"
    override val uploadRx: String = "➕ Upload Rx"
    override val noPrescriptions: String = "No prescriptions recorded yet"
    override val scanOrWrite: String = "Scan paper slip or write down medicines"
    override val districtAdvisories: String = "📢 District Health Advisories"
    override val issuedBy: String = "Issued by:"
    override val emergencySos: String = "EMERGENCY SOS"
    override val emergencySosDesc: String = "Instant dispatch to ASHA & 108 Emergency Ambulance"
    override val trigger: String = "TRIGGER"
    override val confirmSosTitle: String = "Confirm Emergency SOS?"
    override val confirmSosMsg: String = "This will immediately notify your assigned ASHA worker and local primary health center with your emergency profile."
    override val yesSendAlert: String = "🚨 YES, SEND EMERGENCY ALERT"
    override val sosDispatchedTitle: String = "Emergency Alert Dispatched!"
    override val sosDispatchedMsg: String = "ASHA worker and Emergency Response have been alerted. Stay calm and keep phone active."
    override val zeroInternetFallbacks: String = "Zero-Internet Fallbacks (Always Available):"
    override val smsAsha: String = "📱 SMS ASHA Worker Directly"
    override val call108: String = "📞 Call 108 Emergency Ambulance"

    override val catGeneralMedicine: String = "General Medicine"
    override val catMaternalHealth: String = "Maternal Health"
    override val catFitness: String = "Fitness & Physical"
    override val catNutrition: String = "Nutrition & Diet"
    override val catMentalHealth: String = "Mental Wellness"
    override val catEmergency: String = "Emergency Help"

    override val assignedVillages: String = "Assigned Villages:"
    override val uniqueAshaCardTitle: String = "Your Unique ASHA ID Card"
    override val shareAshaIdDesc: String = "Share this 8-character ID or QR with patients in your village to link them directly to your proxy care network."
    override val newPatient: String = "➕ New Patient"
    override val sendNotice: String = "📢 Send Notice"
    override val villageCaseload: String = "Village Health Caseload"
    override val noPatientsYet: String = "No patients registered in your caseload yet."
    override val scanRx: String = "Scan Rx"
    override val proxyMode: String = "Proxy Mode"
    override val emergencyPatientAlerts: String = "🚨 Emergency Patient Alerts"
    override val sosAlertForPatient: String = "Trigger Emergency SOS for Patient?"
    override val confirmSosPatientMsg: String = "This will flag the patient as CRITICAL in the district emergency queue and prepare an emergency call."
    override val sosDispatchedForPatient: String = "🚨 Emergency SOS dispatched for patient."
    override val sosFailedForPatient: String = "Failed to dispatch SOS. Please call 108 directly."
    override val retry: String = "Retry"

    override val pendingCases: String = "Pending Clinical Cases"
    override val criticalCases: String = "Critical / Urgent Cases"
    override val scheduledAppts: String = "Today's Appointments"
    override val activeInQueue: String = "Active In Queue"
    override val specialistQueue: String = "Specialist Queue"
    override val noPendingCases: String = "No pending patient cases. All caught up!"
    override val symptoms: String = "Symptoms"
    override val review: String = "Review Case →"
    override val history: String = "Clinical History"
    override val upcomingConsultations: String = "Upcoming Consultations"
    override val proposeAppt: String = "Propose Appointment"
    override val dispensaryStock: String = "Dispensary Stock"
    override val lowStock: String = "Low Stock Alerts"
    override val patientDirectory: String = "Patient Directory"
    override val searchPatient: String = "Search Patients"
    override val searchPlaceholder: String = "Type patient name or ID..."
    override val caseQueue: String = "Patient Case Queue"
    override val reportedSymptoms: String = "Reported Symptoms & Notes"
    override val doctorAdviceTitle: String = "Doctor Advice & Response"
    override val quickTemplates: String = "Quick Clinical Advice Templates"
    override val issueRx: String = "💊 Issue Prescription"
    override val refer: String = "🩺 Refer to Specialist"
    override val submitAdvice: String = "Submit Clinical Advice"
    override val updateAdvice: String = "Update Clinical Advice"

    override val districtCommand: String = "District Health Command"
    override val surveillanceRegion: String = "Monitoring Region: Bageshwar District"
    override val totalActiveCases: String = "Total Active Cases"
    override val monitoredVillages: String = "Monitored Villages"
    override val outbreakSurveillance: String = "Disease Outbreak Heatmap"
    override val liveTelemetry: String = "Live Public Health Telemetry"
    override val broadcastAlertBtn: String = "📢 Broadcast Health Advisory"
    override val dispatchedDirectives: String = "Active Advisories & Directives"
    override val dispensaryInventory: String = "Dispensary Stock & Restock Requests"

    override val uploadPrescriptionTitle: String = "Digitize Doctor Prescription"
    override val cameraAiScan: String = "📷 Camera / AI Scan"
    override val writeDown: String = "✍️ Write Down"
    override val onDeviceAiBadge: String = "⚡ On-Device AI: Scans paper prescriptions with zero internet."
    override val selectDocSample: String = "Select a document sample:"
    override val extractedRawText: String = "Raw Extracted OCR Text:"
    override val parsedMedicines: String = "Identified Medicines"
    override val saveDigitizedRx: String = "Save Digitized Prescription ✓"
    override val addMedicine: String = "➕ Add Prescribed Medicine:"
    override val medicineName: String = "Medicine Name"
    override val dosage: String = "Dosage (e.g., 500mg)"
    override val duration: String = "Duration (e.g., 5 days)"
    override val frequency: String = "Frequency (e.g., Twice daily)"
    override val savePrescriptionRecord: String = "Save Prescription Record ✓"

    override val callRinging: String = "Ringing..."
    override val callConnecting: String = "Connecting..."
    override val callOngoing: String = "Call Ongoing"
    override val callEnded: String = "Call Ended"
    override val endCall: String = "End Call"
    override val mute: String = "Mute"
    override val unmute: String = "Unmute"
    override val speaker: String = "Speaker"
    override val videoCall: String = "Video Call"
    override val voiceCall: String = "Voice Call"
    override val emergencyDoctorCall: String = "🚨 Emergency Doctor Call"
    override val routineConsultCall: String = "🩺 Consultation Call"
    override val autoEscalatingInSeconds: String = "Auto-escalating to next doctor in %d seconds"
    override val emergencyDoctorOnCall: String = "On-Call Emergency Physician"

    override val medicineAvailabilityTitle: String = "Medicine Availability & Alternatives"
    override val nearbyPharmaciesTitle: String = "Nearby Pharmacies & Medical Stores"
    override val inStock: String = "In Stock"
    override val outOfStock: String = "Out of Stock"
    override val limitedStock: String = "Limited Stock"
    override val alternativesAvailable: String = "Alternative Available Nearby"
    override val findNearbyStores: String = "Find Nearby Stores"
    override val openInMaps: String = "Open in Maps"

    override val doctorReferralsTitle: String = "Doctor-to-Doctor Referrals"
    override val referToSpecialistTitle: String = "Refer to Specialist"
    override val chooseSpecialty: String = "Select Target Specialty"
    override val urgencyLevel: String = "Clinical Urgency"
    override val clinicalQuestionTitle: String = "Specific Clinical Question / Ask"
    override val reasonForReferral: String = "Reason for Referral"
    override val attachRecords: String = "Attach Relevant Records"
    override val acceptReferral: String = "Accept Referral"
    override val declineReferral: String = "Decline Referral"
    override val requestMoreInfo: String = "Request More Info"
    override val specialistFindingsTitle: String = "Diagnostic Findings"
    override val specialistRecommendationsTitle: String = "Recommendations for Referring Doctor"
    override val submitSpecialistFindings: String = "Submit Closed-Loop Findings"
    override val referralSentSuccess: String = "Referral sent to specialist queue."
    override val referralCompleted: String = "Specialist consultation completed."
    override val awaitingSpecialistReview: String = "Awaiting Specialist Review"
    override val specialistAccepted: String = "Specialist Consultation Accepted"
    override val specialistDeclined: String = "Referral Declined"

    override val selectLanguageTitle: String = "Choose Language / भाषा चुनें"
    override val selectLanguageSubtitle: String = "Select your preferred language across the entire application:"
    override val currentLanguageBadge: String = "Active Language"
    override val applyLanguage: String = "Confirm & Apply"

    override val liveQueueTitle: String = "Live Clinic Visit Queue"
    override val tokenNumber: String = "Token #%d"
    override val callNextPatient: String = "Call Next Patient"
    override val startConsultation: String = "Start Consultation"
    override val completeConsultation: String = "Complete Consultation"
    override val noShow: String = "Mark No-Show"
    override val skipPatient: String = "Skip"
    override val prioritizePatient: String = "Prioritize"

    override val triageBreakdownTitle: String = "Triage Severity Breakdown"
    override val statTotal: String = "Total"
    override val statPending: String = "Pending"
    override val statResolved: String = "Resolved"
    override val statReferred: String = "Referred"
    override val medicalHistoryTitle: String = "Longitudinal Medical History"
    override val noMedicalHistory: String = "No medical history recorded."
    override val medicalHistoryTab: String = "Medical History"

    override val liveQueueAndAppointments: String = "Live Queue & Appointments"
    override val liveQueueDesc: String = "Check in today, view token # and wait time"
    override val hud: String = "HUD"
    override val book: String = "Book"
    override val hospitalClinicalServices: String = "Hospital & Clinical Services"
    override val hospitalServicesDesc: String = "Access pathology investigations, digital OPD token slips, and district blood registry."
    override val labReports: String = "Lab Reports"
    override val labReportsSub: String = "CBC, Sugar, Serology"
    override val opdQueue: String = "OPD Queue"
    override val opdQueueSub: String = "Live Tokens & Cabins"
    override val bloodBank: String = "Blood Bank"
    override val bloodBankSub: String = "Emergency Units"
    override val consultationCardTitle: String = "Doctor Tele-Consultation"
    override val routineBadge: String = "Routine"
    override val consultationCardDesc: String = "Book or join routine check-ins with your assigned physician (no emergency alarm)"
    override val routineCallButton: String = "Call / Book"
    override val liveTokenTracker: String = "Live Token Tracker"
    override val viewLabDiagnostics: String = "View Lab Diagnostics"
    override val findDonors: String = "Find Donors"
    override val opdLiveQueueAndTokens: String = "OPD Live Queue & Tokens"
    override val opdSubtitle: String = "Real-time clinic visit tokens, active doctor cabins, and estimated wait"
    override val bookOpdToken: String = "Book OPD Token"
    override val bookHospitalOpdToken: String = "Book Hospital OPD Token"
    override val yourActiveTokens: String = "Your Active Tokens"
    override val noActiveTokens: String = "No active OPD tokens for today"
    override val estimatedWait: String = "Estimated Wait"
    override val currentServing: String = "Currently Serving"
    override val cabin: String = "Cabin"
    override val selectDepartment: String = "Select Department"
    override val selectDoctor: String = "Select Doctor (Optional)"
    override val patientFullName: String = "Patient Full Name"
    override val patientPhone: String = "Contact Number"
    override val reasonForVisit: String = "Reason for Visit"
    override val confirmBooking: String = "Confirm & Generate Token"
    override val tokenGeneratedSuccess: String = "OPD Token Generated Successfully!"
    override val bloodBankRegistry: String = "District Blood Bank Registry"
    override val bloodBankSubtitle: String = "Live blood component units, donor availability, and emergency contact"
    override val bloodUnitsAvailable: String = "Blood Units Available"
    override val callBloodBank: String = "Call Blood Bank"
    override val requestBloodUnits: String = "Request Emergency Blood"
    override val filterBloodGroup: String = "Filter Blood Group"
    override val allGroups: String = "All Blood Groups"
    override val donorDirectory: String = "Verified Volunteer Donors"
    override val units: String = "Units"
    override val lastUpdated: String = "Last Updated"
    override val diagnosticLabReports: String = "Diagnostic Lab Reports"
    override val labReportsSubtitle: String = "Pathology, biochemistry, and radiology investigation records"
    override val downloadReport: String = "Download PDF"
    override val normalRange: String = "Normal Range"
    override val sampleCollected: String = "Sample Collected"
    override val reportDelivered: String = "Report Ready"
    override val noLabReportsFound: String = "No lab reports found on record"
    override val testParameters: String = "Test Parameters"
    override val interpretation: String = "Doctor's Interpretation"
    override val ipdBedTracker: String = "IPD Bed Occupancy Tracker"
    override val ipdSubtitle: String = "Real-time bed availability across ICU, Oxygen, and General wards"
    override val icuBeds: String = "ICU Beds"
    override val oxygenBeds: String = "Oxygen Beds"
    override val generalWard: String = "General Ward"
    override val occupied: String = "Occupied"
    override val available: String = "Available"
    override val totalBeds: String = "Total Beds"
    override val admitPatient: String = "Admit Patient"
    override val dischargePatient: String = "Discharge Patient"
    override val otScheduler: String = "Operation Theatre Scheduler"
    override val otSubtitle: String = "Surgical suites, elective schedules, and emergency trauma slots"
    override val emergencyOt: String = "Emergency OT"
    override val bookOtSlot: String = "Book OT Slot"
    override val surgeonInCharge: String = "Surgeon in Charge"
    override val procedure: String = "Procedure"
    override val scheduledTime: String = "Scheduled Time"
    override val otStatus: String = "OT Status"
    override val bioMedicalTracker: String = "Bio-Medical Equipment Tracker"
    override val bioMedicalSubtitle: String = "Critical life support systems, calibration logs, and uptime"
    override val ventilators: String = "Ventilators"
    override val defibrillators: String = "Defibrillators"
    override val dialysisUnits: String = "Dialysis Units"
    override val operational: String = "Operational"
    override val underMaintenance: String = "Under Maintenance"
    override val reportFault: String = "Report Fault / Issue"
    override val openHud: String = "Open HUD"
    override val clinicalWorkstation: String = "Clinical Workstation"
    override val activeOpdQueue: String = "Active OPD Queue"
    override val inCallHud: String = "In Consultation HUD"
    override val teleConsultInProgress: String = "Tele-Consultation in Progress"
    override val muteMic: String = "Mute"
    override val unmuteMic: String = "Unmute"
    override val turnVideoOff: String = "Stop Video"
    override val turnVideoOn: String = "Start Video"
    override val switchCamera: String = "Switch Camera"
    override val liveTeleVitals: String = "Live Tele-Vitals"
    override val networkQuality: String = "Network Quality"
    override val goodConnection: String = "Strong Connection"
    override val poorConnection: String = "Weak Connection"
    override val prescribeDuringCall: String = "Prescribe During Call"
    override val liveQueueHud: String = "Live Queue HUD"
    override val bookACall: String = "Book a Call"
    override val bookTeleConsultation: String = "Book Tele-Consultation"
    override val bookConsultationSubtitle: String = "Book a scheduled video or voice consultation with a verified specialist."
    override val scheduledAppointments: String = "Scheduled Consultations"
    override val noUpcomingAppointments: String = "No upcoming appointments scheduled"
    override val joinCall: String = "Join Call"
    override val selectDate: String = "Select Consultation Date"
    override val selectTimeSlot: String = "Select Time Slot"
    override val consultationType: String = "Consultation Type"
    override val navHome: String = "Home"
    override val navAppointments: String = "Appointments"
    override val navPrescriptions: String = "Prescriptions"
    override val navSettings: String = "Settings"
    override val hudStatusSafe: String = "Safe"
    override val hudStatusAttention: String = "Attention"
    override val hudStatusDanger: String = "Danger"
    override val hudStatusCritical: String = "Critical"
    override val hudMonitoring: String = "Continuous Monitoring"
    override val appointmentReminderTitle: String = "Upcoming Doctor Consultation"
    override val appointmentReminderBody: String = "Your appointment with %1\$s is scheduled in 15 minutes."

    // Low Connectivity & Sync
    override val slowNetwork: String = "Slow Connection"
    override val syncing: String = "Syncing changes…"
    override val pendingChanges: String = "%d pending changes"
    override val lastSynchronized: String = "Last synchronized: %s"
    override val offlineSosWarning: String = "⚠️ Device Offline: Direct server dispatch unavailable. Use 1-Tap Fallbacks (108 Call / SMS to ASHA)."
    override val cachedDataFreshness: String = "Cached offline data (%s)"
    override val syncComplete: String = "All data synced"
    override val manualSync: String = "Sync Now"

}
val EnglishStrings: AppStrings = EnglishAppStrings()

class HindiAppStrings : AppStrings {

    override val appName: String = "VitalSense"
    override val tagline: String = "सेहतसेतु — ग्रामीण स्वास्थ्य नेटवर्क"
    override val online: String = "ऑनलाइन"
    override val offline: String = "ऑफलाइन"
    override val exit: String = "बाहर निकलें"
    override val cancel: String = "रद्द करें"
    override val done: String = "पूर्ण"
    override val save: String = "सहेजें"
    override val submit: String = "जमा करें"
    override val urgent: String = "अति आवश्यक"
    override val active: String = "सक्रिय"
    override val highPriority: String = "उच्च प्राथमिकता"
    override val highRisk: String = "उच्च जोखिम"
    override val moderateRisk: String = "मध्यम जोखिम"
    override val lowRisk: String = "सामान्य / कम जोखिम"

    override val rolePatient: String = "मरीज़"
    override val rolePatientDesc: String = "स्वास्थ्य कार्ड व आपातकालीन SOS"
    override val roleAsha: String = "आशा कार्यकर्ता"
    override val roleAshaDesc: String = "गाँव स्वास्थ्य रिकॉर्ड व सहायता"
    override val roleDoctor: String = "डॉक्टर"
    override val roleDoctorDesc: String = "मरीज़ समीक्षा व ई-पर्चे"
    override val roleAdmin: String = "प्रशासक"
    override val roleAdminDesc: String = "ज़िला बीमारी निगरानी"

    override val whoIsUsing: String = "ऐप का उपयोग कौन कर रहा है?"
    override val selectRoleDesc: String = "अपने स्वास्थ्य पोर्टल में प्रवेश करने के लिए अपनी भूमिका चुनें:"
    override val patientSignIn: String = "👤 मरीज़ लॉगिन"
    override val ashaSignIn: String = "🤝 आशा कार्यकर्ता लॉगिन"
    override val doctorSignIn: String = "🩺 डॉक्टर क्लिनिकल पोर्टल"
    override val adminSignIn: String = "🛡️ ज़िला स्वास्थ्य प्रशासक"
    override val mobileNumber: String = "मोबाइल नंबर"
    override val ashaHelperIdOptional: String = "आशा कार्यकर्ता आईडी (वैकल्पिक)"
    override val uniqueAshaId: String = "विशिष्ट आशा आईडी"
    override val securityPin: String = "4-अंकीय सुरक्षा पिन"
    override val doctorEmail: String = "पंजीकरण नंबर / ईमेल"
    override val password: String = "पासवर्ड"
    override val adminPasscode: String = "प्रशासक सुरक्षा पासकोड"
    override val logInAsPatient: String = "मरीज़ के रूप में लॉगिन करें →"
    override val logInAsAsha: String = "आशा पोर्टल में लॉगिन करें →"
    override val logInAsDoctor: String = "डॉक्टर पोर्टल में लॉगिन करें →"
    override val logInAsAdmin: String = "ज़िला स्वास्थ्य कमांड में प्रवेश करें →"
    override val quickDemoLogin: String = "⚡ त्वरित 1-टैप डेमो लॉगिन:"
    override val offlineBanner: String = "📶 ऑफलाइन-फर्स्ट: स्वास्थ्य कार्ड बिना इंटरनेट के भी काम करता है"

    override val patientPortal: String = "मरीज़ स्वास्थ्य पोर्टल"
    override val ashaPortal: String = "आशा कार्यकर्ता कार्यक्षेत्र"
    override val doctorPortal: String = "डॉक्टर क्लिनिकल पोर्टल"
    override val adminPortal: String = "ज़िला बीमारी निगरानी कमांड"
    override val actingAsProxy: String = "मरीज़ के प्रतिनिधि के रूप में कार्यरत:"
    override val exitProxy: String = "प्रतिनिधि मोड से बाहर निकलें"

    override val namaste: String = "नमस्ते"
    override val village: String = "गाँव"
    override val ashaAssigned: String = "आशा"
    override val patientGuideTitle: String = "आपका ग्रामीण स्वास्थ्य पोर्टल"
    override val patientGuideMsg: String = "लक्षण दर्ज करने, दवा के पर्चे देखने या अपनी आशा कार्यकर्ता से जुड़ने के लिए नीचे किसी भी श्रेणी पर टैप करें।"
    override val offlineHealthCard: String = "ऑफलाइन स्वास्थ्य कार्ड"
    override val viewCard: String = "कार्ड देखें →"
    override val activeCondition: String = "सक्रिय स्वास्थ्य स्थिति:"
    override val nextCheckup: String = "अगली जांच:"
    override val noneScheduled: String = "कोई निर्धारित नहीं"
    override val cachedOffline: String = "ऑफलाइन सुरक्षित ✓"
    override val howCanWeHelp: String = "आज हम आपकी क्या मदद कर सकते हैं?"
    override val tapServiceDesc: String = "समस्या बताने या डॉक्टर से सलाह लेने के लिए सेवा चुनें:"
    override val myPrescriptions: String = "💊 मेरे डॉक्टर के पर्चे"
    override val uploadRx: String = "➕ पर्चा जोड़ें"
    override val noPrescriptions: String = "अभी कोई पर्चा दर्ज नहीं है"
    override val scanOrWrite: String = "पर्ची स्कैन करें या हाथ से लिखें"
    override val districtAdvisories: String = "📢 ज़िला स्वास्थ्य परामर्श"
    override val issuedBy: String = "द्वारा जारी:"
    override val emergencySos: String = "आपातकालीन SOS"
    override val emergencySosDesc: String = "आशा कार्यकर्ता व 108 एम्बुलेंस को तत्काल अलर्ट"
    override val trigger: String = "अलर्ट भेजें"
    override val confirmSosTitle: String = "क्या आपातकालीन SOS भेजना है?"
    override val confirmSosMsg: String = "यह तुरंत आपकी आशा कार्यकर्ता और प्राथमिक स्वास्थ्य केंद्र को आपकी स्थिति के साथ अलर्ट भेजेगा।"
    override val yesSendAlert: String = "🚨 हाँ, आपातकालीन अलर्ट भेजें"
    override val sosDispatchedTitle: String = "आपातकालीन अलर्ट भेज दिया गया!"
    override val sosDispatchedMsg: String = "आशा कार्यकर्ता और आपातकालीन दल को सूचित कर दिया गया है। कृपया शांत रहें।"
    override val zeroInternetFallbacks: String = "बिना इंटरनेट के आपातकालीन विकल्प:"
    override val smsAsha: String = "📱 आशा कार्यकर्ता को सीधे SMS भेजें"
    override val call108: String = "📞 108 एम्बुलेंस को कॉल करें"

    override val catGeneralMedicine: String = "सामान्य चिकित्सा"
    override val catMaternalHealth: String = "मातृ स्वास्थ्य"
    override val catFitness: String = "शारीरिक स्वास्थ्य"
    override val catNutrition: String = "पोषण व आहार"
    override val catMentalHealth: String = "मानसिक स्वास्थ्य"
    override val catEmergency: String = "आपातकालीन सहायता"

    override val assignedVillages: String = "आवंटित गाँव:"
    override val uniqueAshaCardTitle: String = "आपका विशिष्ट आशा पहचान पत्र"
    override val shareAshaIdDesc: String = "गाँव के मरीज़ों को सीधे अपने नेटवर्क से जोड़ने के लिए यह 8-अक्षरों का कोड या QR साझा करें।"
    override val newPatient: String = "➕ नया मरीज़"
    override val sendNotice: String = "📢 सूचना भेजें"
    override val villageCaseload: String = "गाँव स्वास्थ्य रिकॉर्ड सूची"
    override val noPatientsYet: String = "अभी आपकी सूची में कोई मरीज़ पंजीकृत नहीं है।"
    override val scanRx: String = "पर्चा स्कैन"
    override val proxyMode: String = "प्रतिनिधि मोड"
    override val emergencyPatientAlerts: String = "🚨 आपातकालीन मरीज़ अलर्ट"
    override val sosAlertForPatient: String = "क्या मरीज़ के लिए आपातकालीन SOS भेजना है?"
    override val confirmSosPatientMsg: String = "यह ज़िला आपातकालीन कतार में मरीज़ को गंभीर श्रेणी में दर्ज करेगा।"
    override val sosDispatchedForPatient: String = "🚨 मरीज़ के लिए आपातकालीन SOS भेज दिया गया।"
    override val sosFailedForPatient: String = "SOS नहीं भेजा जा सका। कृपया सीधे 108 पर कॉल करें।"
    override val retry: String = "पुनः प्रयास करें"

    override val pendingCases: String = "समीक्षा के लिए लंबित मामले"
    override val criticalCases: String = "गंभीर / अति आवश्यक मामले"
    override val scheduledAppts: String = "आज के परामर्श"
    override val activeInQueue: String = "कतार में सक्रिय मरीज़"
    override val specialistQueue: String = "विशेषज्ञ कतार"
    override val noPendingCases: String = "कोई लंबित मामला नहीं है। सब पूर्ण!"
    override val symptoms: String = "लक्षण"
    override val review: String = "मामला देखें →"
    override val history: String = "मरीज़ का इतिहास"
    override val upcomingConsultations: String = "आगामी परामर्श"
    override val proposeAppt: String = "परामर्श समय दें"
    override val dispensaryStock: String = "दवाखाना स्टॉक"
    override val lowStock: String = "दवा की कमी की चेतावनी"
    override val patientDirectory: String = "मरीज़ निर्देशिका"
    override val searchPatient: String = "मरीज़ खोजें"
    override val searchPlaceholder: String = "मरीज़ का नाम या आईडी लिखें..."
    override val caseQueue: String = "मरीज़ केस कतार"
    override val reportedSymptoms: String = "दर्ज लक्षण व विवरण"
    override val doctorAdviceTitle: String = "डॉक्टर की सलाह"
    override val quickTemplates: String = "त्वरित चिकित्सकीय सलाह"
    override val issueRx: String = "💊 पर्चा लिखें"
    override val refer: String = "🩺 विशेषज्ञ को रेफर करें"
    override val submitAdvice: String = "सलाह दर्ज करें"
    override val updateAdvice: String = "सलाह अपडेट करें"

    override val districtCommand: String = "ज़िला स्वास्थ्य कमांड"
    override val surveillanceRegion: String = "निगरानी क्षेत्र: बागेश्वर ज़िला"
    override val totalActiveCases: String = "कुल सक्रिय मामले"
    override val monitoredVillages: String = "निगरानीधीन गाँव"
    override val outbreakSurveillance: String = "संक्रामक बीमारी मानचित्र"
    override val liveTelemetry: String = "लाइव स्वास्थ्य डेटा"
    override val broadcastAlertBtn: String = "📢 स्वास्थ्य परामर्श जारी करें"
    override val dispatchedDirectives: String = "जारी किए गए निर्देश व परामर्श"
    override val dispensaryInventory: String = "दवाखाना स्टॉक व आपूर्ति अनुरोध"

    override val uploadPrescriptionTitle: String = "डॉक्टर के पर्चे को डिजिटल बनाएं"
    override val cameraAiScan: String = "📷 कैमरा / AI स्कैन"
    override val writeDown: String = "✍️ हाथ से लिखें"
    override val onDeviceAiBadge: String = "⚡ ऑन-डिवाइस AI: बिना इंटरनेट के डॉक्टर की पर्ची स्कैन करता है।"
    override val selectDocSample: String = "दस्तावेज़ का नमूना चुनें:"
    override val extractedRawText: String = "स्कैन किया गया टेक्स्ट:"
    override val parsedMedicines: String = "पहचानी गई दवाइयाँ"
    override val saveDigitizedRx: String = "डिजिटल नुस्खा सहेजें ✓"
    override val addMedicine: String = "➕ निर्धारित दवा जोड़ें:"
    override val medicineName: String = "दवा का नाम"
    override val dosage: String = "खुराक"
    override val duration: String = "अवधि"
    override val frequency: String = "कब लें"
    override val savePrescriptionRecord: String = "नुस्खा रिकॉर्ड सहेजें ✓"

    override val callRinging: String = "घंटी बज रही है..."
    override val callConnecting: String = "कॉल जुड़ रही है..."
    override val callOngoing: String = "कॉल जारी है"
    override val callEnded: String = "कॉल समाप्त"
    override val endCall: String = "कॉल काटें"
    override val mute: String = "माइक बंद"
    override val unmute: String = "माइक चालू"
    override val speaker: String = "स्पीकर"
    override val videoCall: String = "वीडियो कॉल"
    override val voiceCall: String = "ऑडियो कॉल"
    override val emergencyDoctorCall: String = "🚨 आपातकालीन डॉक्टर कॉल"
    override val routineConsultCall: String = "🩺 नियमित परामर्श कॉल"
    override val autoEscalatingInSeconds: String = "%d सेकंड में अगले डॉक्टर को कॉल जाएगी"
    override val emergencyDoctorOnCall: String = "ड्यूटी पर आपातकालीन डॉक्टर"

    override val medicineAvailabilityTitle: String = "दवा की उपलब्धता व विकल्प"
    override val nearbyPharmaciesTitle: String = "नज़दीकी मेडिकल स्टोर व फार्मेसी"
    override val inStock: String = "उपलब्ध है"
    override val outOfStock: String = "स्टॉक में नहीं"
    override val limitedStock: String = "सीमित स्टॉक"
    override val alternativesAvailable: String = "समान असर वाली वैकल्पिक दवा पास में उपलब्ध"
    override val findNearbyStores: String = "नज़दीकी स्टोर देखें"
    override val openInMaps: String = "मानचित्र में देखें"

    override val doctorReferralsTitle: String = "डॉक्टर-टू-डॉक्टर विशेषज्ञ परामर्श"
    override val referToSpecialistTitle: String = "विशेषज्ञ को रेफर करें"
    override val chooseSpecialty: String = "विशेषज्ञ विभाग चुनें"
    override val urgencyLevel: String = "आवश्यकता स्तर"
    override val clinicalQuestionTitle: String = "विशिष्ट प्रश्न / नैदानिक पूछ"
    override val reasonForReferral: String = "रेफर करने का मुख्य कारण"
    override val attachRecords: String = "संबंधित रिपोर्ट व पर्चे जोड़ें"
    override val acceptReferral: String = "रेफरल स्वीकार करें"
    override val declineReferral: String = "रेफरल अस्वीकार करें"
    override val requestMoreInfo: String = "और जानकारी मांगें"
    override val specialistFindingsTitle: String = "विशेषज्ञ की नैदानिक जांच रिपोर्ट"
    override val specialistRecommendationsTitle: String = "प्रारंभिक डॉक्टर के लिए सलाह"
    override val submitSpecialistFindings: String = "विशेषज्ञ रिपोर्ट जमा करें"
    override val referralSentSuccess: String = "विशेषज्ञ को रेफरल भेज दिया गया।"
    override val referralCompleted: String = "विशेषज्ञ परामर्श पूर्ण हुआ।"
    override val awaitingSpecialistReview: String = "विशेषज्ञ डॉक्टर की समीक्षा की प्रतीक्षा है"
    override val specialistAccepted: String = "विशेषज्ञ डॉक्टर ने परामर्श स्वीकार किया"
    override val specialistDeclined: String = "रेफरल अस्वीकार हुआ"

    override val selectLanguageTitle: String = "भाषा चुनें / Select Language"
    override val selectLanguageSubtitle: String = "पूरी ऐप के लिए अपनी पसंदीदा भाषा चुनें:"
    override val currentLanguageBadge: String = "सक्रिय भाषा"
    override val applyLanguage: String = "लागू करें ✓"

    override val liveQueueTitle: String = "अस्पताल कतार स्थिति"
    override val tokenNumber: String = "टोकन #%d"
    override val callNextPatient: String = "अगले मरीज़ को बुलाएं"
    override val startConsultation: String = "परामर्श शुरू करें"
    override val completeConsultation: String = "परामर्श पूरा करें"
    override val noShow: String = "अनुपस्थित दर्ज करें"
    override val skipPatient: String = "आगे बढ़ें"
    override val prioritizePatient: String = "प्राथमिकता दें"

    override val triageBreakdownTitle: String = "ट्राइएज गंभीरता विश्लेषण"
    override val statTotal: String = "कुल"
    override val statPending: String = "लंबित"
    override val statResolved: String = "समाधानित"
    override val statReferred: String = "रेफर किया"
    override val medicalHistoryTitle: String = "रोगी का संपूर्ण चिकित्सा इतिहास"
    override val noMedicalHistory: String = "कोई चिकित्सा इतिहास दर्ज नहीं है।"
    override val medicalHistoryTab: String = "चिकित्सा इतिहास"

    override val liveQueueAndAppointments: String = "लाइव कतार और अपॉइंटमेंट"
    override val liveQueueDesc: String = "आज ही चेक इन करें, टोकन नंबर और प्रतीक्षा समय देखें"
    override val hud: String = "एचयूडी"
    override val book: String = "बुक करें"
    override val hospitalClinicalServices: String = "अस्पताल एवं नैदानिक सेवाएं"
    override val hospitalServicesDesc: String = "पैथोलॉजी जांच, डिजिटल ओपीडी टोकन और जिला रक्त बैंक तक पहुंचें।"
    override val labReports: String = "लैब रिपोर्ट्स"
    override val labReportsSub: String = "सीबीसी, शुगर, सीरोलॉजी"
    override val opdQueue: String = "ओपीडी कतार"
    override val opdQueueSub: String = "लाइव टोकन और केबिन"
    override val bloodBank: String = "ब्लड बैंक"
    override val bloodBankSub: String = "आपातकालीन यूनिट्स"
    override val consultationCardTitle: String = "डॉक्टर टेली-परामर्श"
    override val routineBadge: String = "नियमित"
    override val consultationCardDesc: String = "अपने चिकित्सक के साथ नियमित परामर्श बुक करें या जुड़ें (कोई आपातकालीन अलार्म नहीं)"
    override val routineCallButton: String = "कॉल / बुक"
    override val liveTokenTracker: String = "लाइव टोकन ट्रैकर"
    override val viewLabDiagnostics: String = "लैब जांच देखें"
    override val findDonors: String = "रक्तदाता खोजें"
    override val opdLiveQueueAndTokens: String = "ओपीडी लाइव कतार और टोकन"
    override val opdSubtitle: String = "रीयल-टाइम क्लिनिक टोकन, सक्रिय डॉक्टर केबिन और अनुमानित प्रतीक्षा समय"
    override val bookOpdToken: String = "ओपीडी टोकन बुक करें"
    override val bookHospitalOpdToken: String = "अस्पताल ओपीडी टोकन बुक करें"
    override val yourActiveTokens: String = "आपके सक्रिय टोकन"
    override val noActiveTokens: String = "आज के लिए कोई सक्रिय ओपीडी टोकन नहीं है"
    override val estimatedWait: String = "अनुमानित प्रतीक्षा"
    override val currentServing: String = "वर्तमान में देखा जा रहा है"
    override val cabin: String = "केबिन"
    override val selectDepartment: String = "विभाग चुनें"
    override val selectDoctor: String = "डॉक्टर चुनें (वैकल्पिक)"
    override val patientFullName: String = "रोगी का पूरा नाम"
    override val patientPhone: String = "संपर्क नंबर"
    override val reasonForVisit: String = "आने का कारण"
    override val confirmBooking: String = "पुष्टि करें और टोकन प्राप्त करें"
    override val tokenGeneratedSuccess: String = "ओपीडी टोकन सफलतापूर्वक उत्पन्न हुआ!"
    override val bloodBankRegistry: String = "जिला रक्त बैंक रजिस्ट्री"
    override val bloodBankSubtitle: String = "लाइव रक्त घटक, रक्तदाताओं की उपलब्धता और आपातकालीन संपर्क"
    override val bloodUnitsAvailable: String = "उपलब्ध रक्त इकाइयां"
    override val callBloodBank: String = "ब्लड बैंक को कॉल करें"
    override val requestBloodUnits: String = "आपातकालीन रक्त का अनुरोध करें"
    override val filterBloodGroup: String = "रक्त समूह चुनें"
    override val allGroups: String = "सभी रक्त समूह"
    override val donorDirectory: String = "सत्यापित रक्तदाता"
    override val units: String = "यूनिट"
    override val lastUpdated: String = "अंतिम अद्यतन"
    override val diagnosticLabReports: String = "नैदानिक लैब रिपोर्ट"
    override val labReportsSubtitle: String = "पैथोलॉजी, बायोकेमिस्ट्री और रेडियोलॉजी जांच रिकॉर्ड"
    override val downloadReport: String = "पीडीएफ डाउनलोड करें"
    override val normalRange: String = "सामान्य सीमा"
    override val sampleCollected: String = "सैंपल लिया गया"
    override val reportDelivered: String = "रिपोर्ट तैयार है"
    override val noLabReportsFound: String = "कोई लैब रिपोर्ट नहीं मिली"
    override val testParameters: String = "जांच विवरण"
    override val interpretation: String = "डॉक्टर की टिप्पणी"
    override val ipdBedTracker: String = "आईपीडी बेड उपलब्धता ट्रैकर"
    override val ipdSubtitle: String = "आईसीयू, ऑक्सीजन और सामान्य वार्ड में रियल-टाइम बेड उपलब्धता"
    override val icuBeds: String = "आईसीयू बेड"
    override val oxygenBeds: String = "ऑक्सीजन बेड"
    override val generalWard: String = "सामान्य वार्ड"
    override val occupied: String = "भरे हुए"
    override val available: String = "उपलब्ध"
    override val totalBeds: String = "कुल बेड"
    override val admitPatient: String = "रोगी को भर्ती करें"
    override val dischargePatient: String = "रोगी को डिस्चार्ज करें"
    override val otScheduler: String = "ऑपरेशन थिएटर शेड्यूलर"
    override val otSubtitle: String = "सर्जिकल सुइट्स, सर्जरी शेड्यूल और आपातकालीन स्लॉट"
    override val emergencyOt: String = "आपातकालीन ओटी"
    override val bookOtSlot: String = "ओटी स्लॉट बुक करें"
    override val surgeonInCharge: String = "प्रभारी सर्जन"
    override val procedure: String = "प्रक्रिया / सर्जरी"
    override val scheduledTime: String = "निर्धारित समय"
    override val otStatus: String = "ओटी स्थिति"
    override val bioMedicalTracker: String = "बायो-मेडिकल उपकरण ट्रैकर"
    override val bioMedicalSubtitle: String = "जीवन रक्षक उपकरण, कैलिब्रेशन लॉग और कार्य स्थिति"
    override val ventilators: String = "वेंटिलेटर"
    override val defibrillators: String = "डिफाइब्रिलेटर"
    override val dialysisUnits: String = "डायलिसिस यूनिट्स"
    override val operational: String = "कार्यरत"
    override val underMaintenance: String = "रखरखाव में"
    override val reportFault: String = "खराबी की सूचना दें"
    override val openHud: String = "एचयूडी खोलें"
    override val clinicalWorkstation: String = "क्लिनिकल कार्यस्थान"
    override val activeOpdQueue: String = "सक्रिय ओपीडी कतार"
    override val inCallHud: String = "परामर्श के दौरान एचयूडी"
    override val teleConsultInProgress: String = "टेली-परामर्श जारी है"
    override val muteMic: String = "माइक बंद"
    override val unmuteMic: String = "माइक चालू"
    override val turnVideoOff: String = "वीडियो बंद"
    override val turnVideoOn: String = "वीडियो चालू"
    override val switchCamera: String = "कैमरा बदलें"
    override val liveTeleVitals: String = "लाइव टेली-वाइटल्स"
    override val networkQuality: String = "नेटवर्क गुणवत्ता"
    override val goodConnection: String = "मजबूत कनेक्शन"
    override val poorConnection: String = "कमजोर कनेक्शन"
    override val prescribeDuringCall: String = "कॉल के दौरान पर्चा लिखें"
    override val liveQueueHud: String = "लाइव कतार एचयूडी"
    override val bookACall: String = "कॉल बुक करें"
    override val bookTeleConsultation: String = "टेली-परामर्श बुक करें"
    override val bookConsultationSubtitle: String = "सत्यापित विशेषज्ञ के साथ वीडियो या वॉयस परामर्श बुक करें।"
    override val scheduledAppointments: String = "शेड्यूल किए गए परामर्श"
    override val noUpcomingAppointments: String = "कोई आगामी अपॉइंटमेंट नहीं है"
    override val joinCall: String = "कॉल में जुड़ें"
    override val selectDate: String = "परामर्श की तिथि चुनें"
    override val selectTimeSlot: String = "समय स्लॉट चुनें"
    override val consultationType: String = "परामर्श का प्रकार"
    override val navHome: String = "होम"
    override val navAppointments: String = "अपॉइंटमेंट्स"
    override val navPrescriptions: String = "दवाइयां"
    override val navSettings: String = "सेटिंग्स"
    override val hudStatusSafe: String = "सुरक्षित"
    override val hudStatusAttention: String = "सावधानी"
    override val hudStatusDanger: String = "खतरा"
    override val hudStatusCritical: String = "गंभीर"
    override val hudMonitoring: String = "निरंतर निगरानी"
    override val appointmentReminderTitle: String = "आगामी डॉक्टर परामर्श"
    override val appointmentReminderBody: String = "%1\$s के साथ आपका परामर्श 15 मिनट में शुरू होगा।"

    // Low Connectivity & Sync
    override val slowNetwork: String = "धीमा नेटवर्क"
    override val syncing: String = "डेटा सिंक हो रहा है…"
    override val pendingChanges: String = "%d बदलाव सिंक होने बाकी"
    override val lastSynchronized: String = "अंतिम सिंक: %s"
    override val offlineSosWarning: String = "⚠️ डिवाइस ऑफलाइन है: सर्वर अलर्ट उपलब्ध नहीं है। तुरंत 108 कॉल या आशा को SMS भेजें।"
    override val cachedDataFreshness: String = "कैश किया गया ऑफलाइन डेटा (%s)"
    override val syncComplete: String = "सभी डेटा सिंक हो गया"
    override val manualSync: String = "अभी सिंक करें"

}
val HindiStrings: AppStrings = HindiAppStrings()

class TamilAppStrings : AppStrings {

    override val appName: String = "VitalSense"
    override val tagline: String = "SehatSetu — கிராமப்புற சுகாதார பாலம்"
    override val online: String = "ஆன்லைன்"
    override val offline: String = "ஆஃப்லைன்"
    override val exit: String = "வெளியேறு"
    override val cancel: String = "ரத்துசெய்"
    override val done: String = "முடிந்தது"
    override val save: String = "சேமி"
    override val submit: String = "சமர்ப்பி"
    override val urgent: String = "அவசரம்"
    override val active: String = "செயலில்"
    override val highPriority: String = "முக்கிய முன்னுரிமை"
    override val highRisk: String = "அதிக ஆபத்து"
    override val moderateRisk: String = "நடுத்தர ஆபத்து"
    override val lowRisk: String = "குறைந்த ஆபத்து / இயல்பு"

    override val rolePatient: String = "நோயாளி"
    override val rolePatientDesc: String = "சுகாதார அட்டை மற்றும் அவசர SOS"
    override val roleAsha: String = "ஆஷா பணியாளர்"
    override val roleAshaDesc: String = "சமூக பராமரிப்பு மற்றும் உதவி"
    override val roleDoctor: String = "மருத்துவர்"
    override val roleDoctorDesc: String = "மருத்துவ ஆய்வு மற்றும் மருந்துகள்"
    override val roleAdmin: String = "நிர்வாகி"
    override val roleAdminDesc: String = "மாவட்ட நோய் கண்காணிப்பு"

    override val whoIsUsing: String = "பயன்படுத்துபவர் யார்?"
    override val selectRoleDesc: String = "உங்கள் சுகாதார போர்ட்டலை அணுக உங்கள் பங்கைத் தேர்ந்தெடுக்கவும்:"
    override val patientSignIn: String = "👤 நோயாளி உள்நுழைவு"
    override val ashaSignIn: String = "🤝 ஆஷா பணியாளர் உள்நுழைவு"
    override val doctorSignIn: String = "🩺 மருத்துவர் போர்ட்டல்"
    override val adminSignIn: String = "🛡️ மாவட்ட சுகாதார நிர்வாகம்"
    override val mobileNumber: String = "மொபைல் எண்"
    override val ashaHelperIdOptional: String = "ஆஷா உதவியாளர் ஐடி (விருப்பத்தேர்வு)"
    override val uniqueAshaId: String = "தனித்துவமான ஆஷா ஐடி"
    override val securityPin: String = "4-இலக்க பாதுகாப்பு பின்"
    override val doctorEmail: String = "மருத்துவப் பதிவு / மின்னஞ்சல்"
    override val password: String = "கடவுச்சொல்"
    override val adminPasscode: String = "மாவட்ட நிர்வாக கடவுச்சொல்"
    override val logInAsPatient: String = "நோயாளியாக உள்நுழைக →"
    override val logInAsAsha: String = "ஆஷா போர்ட்டலில் உள்நுழைக →"
    override val logInAsDoctor: String = "மருத்துவர் போர்ட்டலில் உள்நுழைக →"
    override val logInAsAdmin: String = "மாவட்ட சுகாதார கட்டளைக்கு செல்க →"
    override val quickDemoLogin: String = "⚡ 1-தட்டு டெமோ உள்நுழைவு:"
    override val offlineBanner: String = "📶 ஆஃப்லைன்: இணையம் இல்லாமலும் சுகாதார அட்டை இயங்கும்"

    override val patientPortal: String = "நோயாளி சுகாதார போர்டல்"
    override val ashaPortal: String = "ஆஷா பணியாளர் போர்டல்"
    override val doctorPortal: String = "மருத்துவ ஆய்வு போர்டல்"
    override val adminPortal: String = "மாவட்ட நோய் கண்காணிப்பு"
    override val actingAsProxy: String = "நோயாளியின் பிரதிநிதியாக:"
    override val exitProxy: String = "பிரதிநிதித்துவத்தை முடிக்கவும்"

    override val namaste: String = "வணக்கம்"
    override val village: String = "கிராமம்"
    override val ashaAssigned: String = "ஆஷா பணியாளர்"
    override val patientGuideTitle: String = "உங்கள் கிராமப்புற சுகாதார போர்டல்"
    override val patientGuideMsg: String = "அறிகுறிகளை பதிவு செய்ய, மருந்துகளைப் பார்க்க அல்லது உங்கள் ஆஷா பணியாளரைத் தொடர்பு கொள்ள கீழே தொடவும்."
    override val offlineHealthCard: String = "ஆஃப்லைன் சுகாதார அட்டை"
    override val viewCard: String = "அட்டையைக் காண்க →"
    override val activeCondition: String = "தற்போதைய நிலை:"
    override val nextCheckup: String = "அடுத்த பரிசோதனை:"
    override val noneScheduled: String = "திட்டமிடப்படவில்லை"
    override val cachedOffline: String = "ஆஃப்லைனில் பாதுகாக்கப்பட்டது ✓"
    override val howCanWeHelp: String = "இன்று நாங்கள் உங்களுக்கு எவ்வாறு உதவலாம்?"
    override val tapServiceDesc: String = "மருத்துவ ஆலோசனை பெற சேவையைத் தேர்ந்தெடுக்கவும்:"
    override val myPrescriptions: String = "💊 எனது மருந்துச் சீட்டுகள்"
    override val uploadRx: String = "➕ மருந்துச் சீட்டு சேர்"
    override val noPrescriptions: String = "மருந்துச் சீட்டுகள் எதுவும் பதிவு செய்யப்படவில்லை"
    override val scanOrWrite: String = "சீட்டை ஸ்கேன் செய்யவும் அல்லது எழுதவும்"
    override val districtAdvisories: String = "📢 மாவட்ட சுகாதார ஆலோசனைகள்"
    override val issuedBy: String = "வெளியிட்டவர்:"
    override val emergencySos: String = "அவசர SOS"
    override val emergencySosDesc: String = "ஆஷா மற்றும் 108 ஆம்புலன்ஸுக்கு உடனடி உதவி அழைப்பு"
    override val trigger: String = "அழை"
    override val confirmSosTitle: String = "அவசர SOS அனுப்ப வேண்டுமா?"
    override val confirmSosMsg: String = "இது உடனடியாக உங்கள் ஆஷா பணியாளர் மற்றும் ஆரம்ப சுகாதார நிலையத்திற்கு உங்கள் விவரங்களுடன் அவசர எச்சரிக்கையை அனுப்பும்."
    override val yesSendAlert: String = "🚨 ஆம், அவசர எச்சரிக்கை அனுப்பு"
    override val sosDispatchedTitle: String = "அவசர எச்சரிக்கை அனுப்பப்பட்டது!"
    override val sosDispatchedMsg: String = "ஆஷா பணியாளர் மற்றும் அவசர குழுவிற்கு தகவல் தெரிவிக்கப்பட்டுள்ளது. அமைதியாக இருங்கள்."
    override val zeroInternetFallbacks: String = "இணையமற்ற அவசர வழிகள்:"
    override val smsAsha: String = "📱 ஆஷா பணியாளருக்கு நேரடியாக SMS செய்க"
    override val call108: String = "📞 108 ஆம்புலன்ஸை அழைக்கவும்"

    override val catGeneralMedicine: String = "பொது மருத்துவம்"
    override val catMaternalHealth: String = "தாய்மை நலம்"
    override val catFitness: String = "உடற்பயிற்சி மற்றும் நலம்"
    override val catNutrition: String = "ஊட்டச்சத்து மற்றும் உணவு"
    override val catMentalHealth: String = "மன நலம்"
    override val catEmergency: String = "அவசர உதவி"

    override val assignedVillages: String = "ஒதுக்கப்பட்ட கிராமங்கள்:"
    override val uniqueAshaCardTitle: String = "உங்கள் தனித்துவமான ஆஷா அட்டை"
    override val shareAshaIdDesc: String = "கிராமப்புற நோயாளிகளை உங்கள் பராமரிப்பு நெட்வொர்க்கில் இணைக்க இந்த 8-இலக்க குறியீட்டைப் பகிரவும்."
    override val newPatient: String = "➕ புதிய நோயாளி"
    override val sendNotice: String = "📢 அறிவிப்பு அனுப்பு"
    override val villageCaseload: String = "கிராம சுகாதார பட்டியல்"
    override val noPatientsYet: String = "இன்னும் நோயாளிகள் பதிவு செய்யப்படவில்லை."
    override val scanRx: String = "மருந்து ஸ்கேன்"
    override val proxyMode: String = "பிரதிநிதி முறை"
    override val emergencyPatientAlerts: String = "🚨 அவசர நோயாளி எச்சரிக்கைகள்"
    override val sosAlertForPatient: String = "நோயாளிக்கு அவசர SOS அனுப்பவா?"
    override val confirmSosPatientMsg: String = "இது மாவட்ட அவசர வரிசையில் நோயாளியை தீவிர நிலையில் பதிவு செய்யும்."
    override val sosDispatchedForPatient: String = "🚨 நோயாளிக்கு அவசர எச்சரிக்கை அனுப்பப்பட்டது."
    override val sosFailedForPatient: String = "எச்சரிக்கை அனுப்ப முடியவில்லை. 108-ஐ நேரடியாக அழைக்கவும்."
    override val retry: String = "மீண்டும் முயற்சி"

    override val pendingCases: String = "நிலுவையில் உள்ள வழக்குகள்"
    override val criticalCases: String = "அவசர மற்றும் தீவிர வழக்குகள்"
    override val scheduledAppts: String = "இன்றைய ஆலோசனைகள்"
    override val activeInQueue: String = "வரிசையில் உள்ளவர்கள்"
    override val specialistQueue: String = "நிபுணர் வரிசை"
    override val noPendingCases: String = "நிலுவை வழக்குகள் ஏதுமில்லை!"
    override val symptoms: String = "அறிகுறிகள்"
    override val review: String = "ஆய்வு செய் →"
    override val history: String = "மருத்துவ வரலாறு"
    override val upcomingConsultations: String = "வரவிருக்கும் ஆலோசனைகள்"
    override val proposeAppt: String = "நேரம் ஒதுக்கு"
    override val dispensaryStock: String = "மருந்தக இருப்பு"
    override val lowStock: String = "குறைந்த இருப்பு எச்சரிக்கை"
    override val patientDirectory: String = "நோயாளி விபரம்"
    override val searchPatient: String = "நோயாளியைத் தேடு"
    override val searchPlaceholder: String = "பெயர் அல்லது ஐடியை உள்ளிடவும்..."
    override val caseQueue: String = "நோயாளி வரிசை"
    override val reportedSymptoms: String = "அறிவிக்கப்பட்ட அறிகுறிகள்"
    override val doctorAdviceTitle: String = "மருத்துவர் ஆலோசனை"
    override val quickTemplates: String = "விரைவு மருத்துவ ஆலோசனைகள்"
    override val issueRx: String = "💊 மருந்துச் சீட்டு வழங்கு"
    override val refer: String = "🩺 நிபுணரிடம் பரிந்துரை செய்"
    override val submitAdvice: String = "ஆலோசனையை பதிவு செய்"
    override val updateAdvice: String = "ஆலோசனையை புதுப்பி"

    override val districtCommand: String = "மாவட்ட சுகாதார கட்டளை மையம்"
    override val surveillanceRegion: String = "கண்காணிப்பு பகுதி: பாகேஷ்வர் மாவட்டம்"
    override val totalActiveCases: String = "மொத்த தீவிர வழக்குகள்"
    override val monitoredVillages: String = "கண்காணிக்கப்படும் கிராமங்கள்"
    override val outbreakSurveillance: String = "நோய் பரவல் வரைபடம்"
    override val liveTelemetry: String = "நேரலை சுகாதார தரவு"
    override val broadcastAlertBtn: String = "📢 சுகாதார எச்சரிக்கை வெளியிடு"
    override val dispatchedDirectives: String = "செயலில் உள்ள வழிகாட்டுதல்கள்"
    override val dispensaryInventory: String = "மருந்தக இருப்பு மற்றும் கோரிக்கைகள்"

    override val uploadPrescriptionTitle: String = "மருந்துச் சீட்டை டிஜிட்டல் மயமாக்கு"
    override val cameraAiScan: String = "📷 கேமரா / AI ஸ்கேன்"
    override val writeDown: String = "✍️ கையேடு மூலம் எழுது"
    override val onDeviceAiBadge: String = "⚡ AI ஸ்கேனர்: இணையம் இல்லாமல் காகித மருந்துச் சீட்டைப் படிக்கிறது."
    override val selectDocSample: String = "மாதிரி ஆவணத்தைத் தேர்ந்தெடுக்கவும்:"
    override val extractedRawText: String = "ஸ்கேன் செய்யப்பட்ட உரை:"
    override val parsedMedicines: String = "கண்டறியப்பட்ட மருந்துகள்"
    override val saveDigitizedRx: String = "டிஜிட்டல் மருந்துச் சீட்டைச் சேமி ✓"
    override val addMedicine: String = "➕ பரிந்துரைக்கப்பட்ட மருந்தைச் சேர்:"
    override val medicineName: String = "மருந்தின் பெயர்"
    override val dosage: String = "அளவு (Dosage)"
    override val duration: String = "கால அளவு"
    override val frequency: String = "எப்போது உட்கொள்ள வேண்டும்"
    override val savePrescriptionRecord: String = "மருந்துப் பதிவைச் சேமி ✓"

    override val callRinging: String = "ஒலிக்கிறது..."
    override val callConnecting: String = "இணைக்கிறது..."
    override val callOngoing: String = "அழைப்பு தொடர்கிறது"
    override val callEnded: String = "அழைப்பு முடிந்தது"
    override val endCall: String = "அழைப்பை முடி"
    override val mute: String = "ஒலியை நிறுத்து"
    override val unmute: String = "ஒலியை இயக்கு"
    override val speaker: String = "ஸ்பீக்கர்"
    override val videoCall: String = "வீடியோ அழைப்பு"
    override val voiceCall: String = "குரல் அழைப்பு"
    override val emergencyDoctorCall: String = "🚨 அவசர மருத்துவர் அழைப்பு"
    override val routineConsultCall: String = "🩺 வழக்கமான ஆலோசனை அழைப்பு"
    override val autoEscalatingInSeconds: String = "%d வினாடிகளில் அடுத்த மருத்துவருக்கு மாற்றப்படும்"
    override val emergencyDoctorOnCall: String = "பணியில் உள்ள அவசர மருத்துவர்"

    override val medicineAvailabilityTitle: String = "மருந்து இருப்பு மற்றும் மாற்றுகள்"
    override val nearbyPharmaciesTitle: String = "அருகிலுள்ள மருந்தகங்கள்"
    override val inStock: String = "இருப்பில் உள்ளது"
    override val outOfStock: String = "இருப்பில் இல்லை"
    override val limitedStock: String = "குறைந்த அளவு இருப்பு"
    override val alternativesAvailable: String = "மாற்று மருந்து அருகில் கிடைக்கிறது"
    override val findNearbyStores: String = "அருகிலுள்ள கடைகளைத் தேடு"
    override val openInMaps: String = "வரைபடத்தில் காண்க"

    override val doctorReferralsTitle: String = "மருத்துவர் பரிந்துரை அமைப்பு"
    override val referToSpecialistTitle: String = "சிறப்பு மருத்துவருக்குப் பரிந்துரை"
    override val chooseSpecialty: String = "சிறப்புத் துறையைத் தேர்ந்தெடுக்கவும்"
    override val urgencyLevel: String = "அவசர நிலை"
    override val clinicalQuestionTitle: String = "குறிப்பிட்ட மருத்துவக் கேள்வி"
    override val reasonForReferral: String = "பரிந்துரைக்கான காரணம்"
    override val attachRecords: String = "முந்தைய அறிக்கைகளை இணைக்கவும்"
    override val acceptReferral: String = "பரிந்துரையை ஏற்றுக்கொள்"
    override val declineReferral: String = "பரிந்துரையை நிராகரி"
    override val requestMoreInfo: String = "கூடுதல் தகவல் கோரு"
    override val specialistFindingsTitle: String = "சிறப்பு மருத்துவர் பரிசோதனை முடிவுகள்"
    override val specialistRecommendationsTitle: String = "பரிந்துரைத்த மருத்துவருக்கு ஆலோசனை"
    override val submitSpecialistFindings: String = "முடிவுகளைச் சமர்ப்பி"
    override val referralSentSuccess: String = "பரிந்துரை அனுப்பப்பட்டது."
    override val referralCompleted: String = "சிறப்பு ஆலோசனை நிறைவடைந்தது."
    override val awaitingSpecialistReview: String = "சிறப்பு மருத்துவரின் ஆய்வுக்கு காத்திருக்கிறது"
    override val specialistAccepted: String = "சிறப்பு மருத்துவர் ஏற்றுக்கொண்டார்"
    override val specialistDeclined: String = "பரிந்துரை நிராகரிக்கப்பட்டது"

    override val selectLanguageTitle: String = "மொழியைத் தேர்ந்தெடுக்கவும் / Select Language"
    override val selectLanguageSubtitle: String = "பயன்பாட்டிற்கான உங்கள் மொழியைத் தேர்ந்தெடுக்கவும்:"
    override val currentLanguageBadge: String = "தற்போதைய மொழி"
    override val applyLanguage: String = "பயன்படுத்து ✓"

    override val liveQueueTitle: String = "நேரலை மருத்துவமனை வரிசை"
    override val tokenNumber: String = "டோக்கன் #%d"
    override val callNextPatient: String = "அடுத்த நோயாளியை அழைக்கவும்"
    override val startConsultation: String = "ஆலோசனையைத் தொடங்கு"
    override val completeConsultation: String = "ஆலோசனையை முடி"
    override val noShow: String = "வராதவராகப் பதிவு செய்"
    override val skipPatient: String = "தவிர்"
    override val prioritizePatient: String = "முன்னுரிமை அளி"

    override val triageBreakdownTitle: String = "சிகிச்சை முன்னுரிமை பிரிவு"
    override val statTotal: String = "மொத்தம்"
    override val statPending: String = "நிலுவையில்"
    override val statResolved: String = "தீர்க்கப்பட்டது"
    override val statReferred: String = "பரிந்துரைக்கப்பட்டது"
    override val medicalHistoryTitle: String = "நோயாளியின் மருத்துவ வரலாறு"
    override val noMedicalHistory: String = "மருத்துவ வரலாறு எதுவும் பதிவு செய்யப்படவில்லை."
    override val medicalHistoryTab: String = "மருத்துவ வரலாறு"

    override val liveQueueAndAppointments: String = "நேரலை வரிசை & சந்திப்புகள்"
    override val liveQueueDesc: String = "இன்றே பதிவு செய்து, டோக்கன் எண் மற்றும் காத்திருப்பு நேரத்தைப் பார்க்கவும்"
    override val hud: String = "எச்யுடி"
    override val book: String = "முன்பதிவு"
    override val hospitalClinicalServices: String = "மருத்துவமனை & மருத்துவ சேவைகள்"
    override val hospitalServicesDesc: String = "ஆய்வக சோதனைகள், ஓபிடி டோக்கன் மற்றும் இரத்த வங்கி விபரங்களை அணுகவும்."
    override val labReports: String = "ஆய்வக அறிக்கைகள்"
    override val labReportsSub: String = "சிபிசி, சர்க்கரை, செராலஜி"
    override val opdQueue: String = "ஓபிடி வரிசை"
    override val opdQueueSub: String = "நேரலை டோக்கன்கள் & அறைகள்"
    override val bloodBank: String = "இரத்த வங்கி"
    override val bloodBankSub: String = "அவசர அலகுகள்"
    override val consultationCardTitle: String = "மருத்துவர் தொலை ஆலோசனைகள்"
    override val routineBadge: String = "வழக்கமான"
    override val consultationCardDesc: String = "மருத்துவருடன் வழக்கமான ஆலோசனையைத் தொடங்குங்கள் (அவசர அழைப்பு இல்லை)"
    override val routineCallButton: String = "அழை / முன்பதிவு"
    override val liveTokenTracker: String = "நேரலை டோக்கன் கண்காணிப்பு"
    override val viewLabDiagnostics: String = "ஆய்வக முடிவுகளைக் காண்க"
    override val findDonors: String = "தானியாளர்களைக் கண்டறியவும்"
    override val opdLiveQueueAndTokens: String = "ஓபிடி நேரலை வரிசை மற்றும் டோக்கன்கள்"
    override val opdSubtitle: String = "நேரடி மருத்துவமனை டோக்கன்கள், செயலில் உள்ள மருத்துவர் அறைகள்"
    override val bookOpdToken: String = "ஓபிடி டோக்கனை முன்பதிவு செய்"
    override val bookHospitalOpdToken: String = "மருத்துவமனை ஓபிடி டோக்கன் பதிவு"
    override val yourActiveTokens: String = "உங்கள் செயலில் உள்ள டோக்கன்கள்"
    override val noActiveTokens: String = "இன்றைக்கு செயலில் உள்ள டோக்கன்கள் எதுவும் இல்லை"
    override val estimatedWait: String = "மதிப்பிடப்பட்ட காத்திருப்பு"
    override val currentServing: String = "தற்போது பார்க்கப்படுவது"
    override val cabin: String = "அறை"
    override val selectDepartment: String = "துறையைத் தேர்ந்தெடுக்கவும்"
    override val selectDoctor: String = "மருத்துவரைத் தேர்ந்தெடு (விருப்பத்தேர்வு)"
    override val patientFullName: String = "நோயாளியின் முழுப் பெயர்"
    override val patientPhone: String = "தொடர்பு எண்"
    override val reasonForVisit: String = "வருவதற்கான காரணம்"
    override val confirmBooking: String = "உறுதிசெய்து டோக்கனைப் பெறு"
    override val tokenGeneratedSuccess: String = "ஓபிடி டோக்கன் வெற்றிகரமாக உருவாக்கப்பட்டது!"
    override val bloodBankRegistry: String = "மாவட்ட இரத்த வங்கிப் பதிவேடு"
    override val bloodBankSubtitle: String = "நேரடி இரத்த அலகுகள் மற்றும் அவசரத் தொடர்பு"
    override val bloodUnitsAvailable: String = "கிடைக்கும் இரத்த அலகுகள்"
    override val callBloodBank: String = "இரத்த வங்கிக்கு அழைக்கவும்"
    override val requestBloodUnits: String = "அவசர இரத்தக் கோரிக்கை"
    override val filterBloodGroup: String = "இரத்த வகையைத் தேர்ந்தெடுக்கவும்"
    override val allGroups: String = "அனைத்து இரத்த வகைகள்"
    override val donorDirectory: String = "சரிபார்க்கப்பட்ட இரத்த தானியாளர்கள்"
    override val units: String = "அலகுகள்"
    override val lastUpdated: String = "கடைசியாக புதுப்பிக்கப்பட்டது"
    override val diagnosticLabReports: String = "நோய் கண்டறிதல் ஆய்வக அறிக்கைகள்"
    override val labReportsSubtitle: String = "நோயியல் மற்றும் உயிர்வேதியியல் சோதனை பதிவுகள்"
    override val downloadReport: String = "பிடிஎஃப் பதிவிறக்கு"
    override val normalRange: String = "இயல்பான வரம்பு"
    override val sampleCollected: String = "மாதிரி சேகரிக்கப்பட்டது"
    override val reportDelivered: String = "அறிக்கை தயார்"
    override val noLabReportsFound: String = "ஆய்வக அறிக்கைகள் எதுவும் இல்லை"
    override val testParameters: String = "சோதனை அளவுருக்கள்"
    override val interpretation: String = "மருத்துவரின் விளக்கம்"
    override val ipdBedTracker: String = "உள்நோயாளி படுக்கை கண்காணிப்பு"
    override val ipdSubtitle: String = "தீவிர சிகிச்சை மற்றும் பொது படுக்கைகள் விபரம்"
    override val icuBeds: String = "தீவிர சிகிச்சை படுக்கைகள்"
    override val oxygenBeds: String = "ஆக்சிஜன் படுக்கைகள்"
    override val generalWard: String = "பொது வார்டு"
    override val occupied: String = "நிரம்பியது"
    override val available: String = "கிடைக்கும்"
    override val totalBeds: String = "மொத்த படுக்கைகள்"
    override val admitPatient: String = "நோயாளியை அனுமதிக்கவும்"
    override val dischargePatient: String = "நோயாளியை விடுவிக்கவும்"
    override val otScheduler: String = "அறுவை சிகிச்சை அரங்கு அட்டவணை"
    override val otSubtitle: String = "அறுவை சிகிச்சை நேரங்கள் மற்றும் அவசர இடங்கள்"
    override val emergencyOt: String = "அவசர அறுவை அரங்கு"
    override val bookOtSlot: String = "அறுவை சிகிச்சை முன்பதிவு செய்"
    override val surgeonInCharge: String = "பொறுப்பு அறுவை சிகிச்சை நிபுணர்"
    override val procedure: String = "அறுவை சிகிச்சை முறை"
    override val scheduledTime: String = "திட்டமிடப்பட்ட நேரம்"
    override val otStatus: String = "அரங்கு நிலை"
    override val bioMedicalTracker: String = "உயிர்-மருத்துவ உபகரண கண்காணிப்பு"
    override val bioMedicalSubtitle: String = "முக்கிய மருத்துவ உபகரணங்கள் மற்றும் பராமரிப்பு நிலை"
    override val ventilators: String = "செயற்கை சுவாசக் கருவிகள்"
    override val defibrillators: String = "இதய மீட்புக் கருவிகள்"
    override val dialysisUnits: String = "டயாலிசிஸ் அலகுகள்"
    override val operational: String = "செயல்படுகிறது"
    override val underMaintenance: String = "பராமரிப்பில்"
    override val reportFault: String = "கோளாறைப் புகாரளிக்கவும்"
    override val openHud: String = "எச்யுடி திறக்கவும்"
    override val clinicalWorkstation: String = "மருத்துவ பணிநிலையம்"
    override val activeOpdQueue: String = "செயலில் உள்ள ஓபிடி வரிசை"
    override val inCallHud: String = "ஆலோசனையில் எச்யுடி"
    override val teleConsultInProgress: String = "தொலை ஆலோசனை நடைபெறுகிறது"
    override val muteMic: String = "ஒலி நிறுத்து"
    override val unmuteMic: String = "ஒலி இயக்கு"
    override val turnVideoOff: String = "வீடியோ நிறுத்து"
    override val turnVideoOn: String = "வீடியோ தொடங்கு"
    override val switchCamera: String = "கேமராவை மாற்று"
    override val liveTeleVitals: String = "நேரடி உடலியல் அளவுகள்"
    override val networkQuality: String = "நெட்வொர்க் தரம்"
    override val goodConnection: String = "நல்ல இணைப்பு"
    override val poorConnection: String = "பலவீனமான இணைப்பு"
    override val prescribeDuringCall: String = "அழைப்பின் போதே மருந்து எழுது"
    override val liveQueueHud: String = "நேரலை வரிசை எச்யுடி"
    override val bookACall: String = "அழைப்பை முன்பதிவு செய்"
    override val bookTeleConsultation: String = "தொலை ஆலோசனையை பதிவு செய்"
    override val bookConsultationSubtitle: String = "சரிபார்க்கப்பட்ட சிறப்பு மருத்துவருடன் ஆலோசனைக்கு முன்பதிவு செய்யுங்கள்."
    override val scheduledAppointments: String = "திட்டமிடப்பட்ட ஆலோசனைகள்"
    override val noUpcomingAppointments: String = "வரவிருக்கும் சந்திப்புகள் எதுவும் இல்லை"
    override val joinCall: String = "அழைப்பில் சேரவும்"
    override val selectDate: String = "ஆலோசனை தேதியைத் தேர்ந்தெடு"
    override val selectTimeSlot: String = "நேர இடைவெளியைத் தேர்ந்தெடு"
    override val consultationType: String = "ஆலோசனை வகை"
    override val navHome: String = "முகப்பு"
    override val navAppointments: String = "சந்திப்புகள்"
    override val navPrescriptions: String = "மருந்துச்சீட்டுகள்"
    override val navSettings: String = "அமைப்புகள்"
    override val hudStatusSafe: String = "பாதுகாப்பானது"
    override val hudStatusAttention: String = "கவனம்"
    override val hudStatusDanger: String = "ஆபத்து"
    override val hudStatusCritical: String = "மிகவும் ஆபத்தானது"
    override val hudMonitoring: String = "தொடர் கண்காணிப்பு"
    override val appointmentReminderTitle: String = "வரவிருக்கும் மருத்துவர் ஆலோசனை"
    override val appointmentReminderBody: String = "%1\$s உடனான உங்கள் சந்திப்பு 15 நிமிடங்களில் தொடங்குகிறது."

    // Low Connectivity & Sync
    override val slowNetwork: String = "மெதுவான இணைப்பு"
    override val syncing: String = "ஒத்திசைக்கப்படுகிறது…"
    override val pendingChanges: String = "%d மாற்றங்கள் நிலுவையில் உள்ளன"
    override val lastSynchronized: String = "கடைசி ஒத்திசைவு: %s"
    override val offlineSosWarning: String = "⚠️ சாதனம் ஆஃப்லைனில் உள்ளது: நேரடி சர்வர் எச்சரிக்கை கிடைக்கவில்லை. 108 அழைப்பு அல்லது SMS பயன்படுத்தவும்."
    override val cachedDataFreshness: String = "கேச் செய்யப்பட்ட ஆஃப்லைன் தரவு (%s)"
    override val syncComplete: String = "அனைத்து தரவும் ஒத்திசைக்கப்பட்டது"
    override val manualSync: String = "இப்போது ஒத்திசைக்கவும்"

}
val TamilStrings: AppStrings = TamilAppStrings()

class MarathiAppStrings : AppStrings {

    override val appName: String = "VitalSense"
    override val tagline: String = "SehatSetu — ग्रामीण आरोग्य नेटवर्क"
    override val online: String = "ऑनलाइन"
    override val offline: String = "ऑफलाइन"
    override val exit: String = "बाहेर पडा"
    override val cancel: String = "रद्द करा"
    override val done: String = "पूर्ण"
    override val save: String = "जतन करा"
    override val submit: String = "सादर करा"
    override val urgent: String = "तातडीचे"
    override val active: String = "सक्रिय"
    override val highPriority: String = "उच्च प्राधान्य"
    override val highRisk: String = "अति धोका"
    override val moderateRisk: String = "मध्यम धोका"
    override val lowRisk: String = "कमी धोका / सामान्य"

    override val rolePatient: String = "रुग्ण"
    override val rolePatientDesc: String = "आरोग्य कार्ड व आपत्कालीन SOS"
    override val roleAsha: String = "आशा सेविका"
    override val roleAshaDesc: String = "गावातील रुग्ण नोंदी व मदत"
    override val roleDoctor: String = "डॉक्टर"
    override val roleDoctorDesc: String = "तपासणी व औषधोपचार"
    override val roleAdmin: String = "प्रशासक"
    override val roleAdminDesc: String = "जिल्हा आजार नियंत्रण"

    override val whoIsUsing: String = "अॅप कोण वापरत आहे?"
    override val selectRoleDesc: String = "आपल्या आरोग्य पोर्टलमध्ये जाण्यासाठी आपली भूमिका निवडा:"
    override val patientSignIn: String = "👤 रुग्ण लॉगिन"
    override val ashaSignIn: String = "🤝 आशा सेविका लॉगिन"
    override val doctorSignIn: String = "🩺 डॉक्टर क्लिनिकल पोर्टल"
    override val adminSignIn: String = "🛡️ जिल्हा आरोग्य प्रशासन"
    override val mobileNumber: String = "मोबाईल नंबर"
    override val ashaHelperIdOptional: String = "आशा मदतनीस आयडी (पर्यायी)"
    override val uniqueAshaId: String = "विशिष्ट आशा आयडी"
    override val securityPin: String = "४-अंकी सुरक्षा पिन"
    override val doctorEmail: String = "वैद्यकीय नोंदणी / ईमेल"
    override val password: String = "पासवर्ड"
    override val adminPasscode: String = "जिल्हा प्रशासन पासकोड"
    override val logInAsPatient: String = "रुग्ण म्हणून लॉगिन करा →"
    override val logInAsAsha: String = "आशा पोर्टलवर लॉगिन करा →"
    override val logInAsDoctor: String = "डॉक्टर पोर्टलवर लॉगिन करा →"
    override val logInAsAdmin: String = "जिल्हा आरोग्य नियंत्रण कक्षात जा →"
    override val quickDemoLogin: String = "⚡ झटपट १-टॅप डेमो लॉगिन:"
    override val offlineBanner: String = "📶 ऑफलाइन-फर्स्ट: इंटरनेट नसतानाही आरोग्य कार्ड सुरक्षितपणे काम करते"

    override val patientPortal: String = "रुग्ण आरोग्य पोर्टल"
    override val ashaPortal: String = "आशा सेविका कार्यक्षेत्र"
    override val doctorPortal: String = "क्लिनिकल तपासणी पोर्टल"
    override val adminPortal: String = "जिल्हा साथरोग नियंत्रण"
    override val actingAsProxy: String = "रुग्णाचे प्रतिनिधी म्हणून कार्यरत:"
    override val exitProxy: String = "प्रतिनिधी मोडमधून बाहेर पडा"

    override val namaste: String = "नमस्ते"
    override val village: String = "गाव"
    override val ashaAssigned: String = "आशा सेविका"
    override val patientGuideTitle: String = "आपले ग्रामीण आरोग्य पोर्टल"
    override val patientGuideMsg: String = "लक्षणे नोंदवण्यासाठी, औषधांची चिठ्ठी पाहण्यासाठी किंवा आपल्या आशा सेविकेशी संपर्क साधण्यासाठी खाली टॅप करा."
    override val offlineHealthCard: String = "ऑफलाइन आरोग्य कार्ड"
    override val viewCard: String = "कार्ड पहा →"
    override val activeCondition: String = "सध्याची आरोग्य स्थिती:"
    override val nextCheckup: String = "पुढील तपासणी:"
    override val noneScheduled: String = "नियोजित नाही"
    override val cachedOffline: String = "ऑफलाइन सुरक्षित ✓"
    override val howCanWeHelp: String = "आम्ही आज आपल्याला कशी मदत करू शकतो?"
    override val tapServiceDesc: String = "तपासणी किंवा सल्ला मिळवण्यासाठी सेवा निवडा:"
    override val myPrescriptions: String = "💊 माझी औषध प्रिस्क्रिप्शन"
    override val uploadRx: String = "➕ नवीन प्रिस्क्रिप्शन"
    override val noPrescriptions: String = "अद्याप कोणतीही औषध नोंद नाही"
    override val scanOrWrite: String = "चिठ्ठी स्कॅन करा किंवा लिहून ठेवा"
    override val districtAdvisories: String = "📢 जिल्हा आरोग्य सूचना"
    override val issuedBy: String = "जारीकर्ते:"
    override val emergencySos: String = "आपत्कालीन SOS"
    override val emergencySosDesc: String = "आशा सेविका व १०८ रुग्णवाहिकेला तात्काळ अलर्ट"
    override val trigger: String = "अलर्ट पाठवा"
    override val confirmSosTitle: String = "आपत्कालीन SOS पाठवायचा आहे का?"
    override val confirmSosMsg: String = "हे तात्काळ आपल्या आशा सेविकेला आणि प्राथमिक आरोग्य केंद्राला आपल्या माहितीसह अलर्ट पाठवेल."
    override val yesSendAlert: String = "🚨 होय, आपत्कालीन अलर्ट पाठवा"
    override val sosDispatchedTitle: String = "आपत्कालीन अलर्ट पाठवला गेला!"
    override val sosDispatchedMsg: String = "आशा सेविका व आपत्कालीन पथकाला माहिती दिली आहे. कृपया शांत रहा."
    override val zeroInternetFallbacks: String = "इंटरनेट नसतानाचे तात्काळ पर्याय:"
    override val smsAsha: String = "📱 आशा सेविकेला थेट SMS पाठवा"
    override val call108: String = "📞 १०८ रुग्णवाहिकेला कॉल करा"

    override val catGeneralMedicine: String = "सामान्य औषधोपचार"
    override val catMaternalHealth: String = "मातृ व बाळ आरोग्य"
    override val catFitness: String = "शारीरिक व्यायाम"
    override val catNutrition: String = "आहार व पोषण"
    override val catMentalHealth: String = "मानसिक आरोग्य"
    override val catEmergency: String = "आपत्कालीन मदत"

    override val assignedVillages: String = "नेमून दिलेली गावे:"
    override val uniqueAshaCardTitle: String = "आपले विशिष्ट आशा ओळखपत्र"
    override val shareAshaIdDesc: String = "गावातील रुग्णांना थेट जोडण्यासाठी हा ८-अक्षरी कोड किंवा QR शेअर करा."
    override val newPatient: String = "➕ नवीन रुग्ण"
    override val sendNotice: String = "📢 नोटीस पाठवा"
    override val villageCaseload: String = "गावातील रुग्ण नोंदणी"
    override val noPatientsYet: String = "अद्याप कोणतेही रुग्ण नोंदवलेले नाहीत."
    override val scanRx: String = "प्रिस्क्रिप्शन स्कॅन"
    override val proxyMode: String = "प्रतिनिधी मोड"
    override val emergencyPatientAlerts: String = "🚨 आपत्कालीन रुग्ण अलर्ट"
    override val sosAlertForPatient: String = "रुग्णासाठी आपत्कालीन SOS पाठवायचा का?"
    override val confirmSosPatientMsg: String = "हे जिल्हा आपत्कालीन रांगेत रुग्णाला अतिगंभीर श्रेणीत नोंदवेल."
    override val sosDispatchedForPatient: String = "🚨 रुग्णासाठी आपत्कालीन अलर्ट पाठवला गेला."
    override val sosFailedForPatient: String = "अलर्ट पाठवता आला नाही. कृपया थेट १०८ वर कॉल करा."
    override val retry: String = "पुन्हा प्रयत्न करा"

    override val pendingCases: String = "तपासणीसाठी प्रलंबित रुग्ण"
    override val criticalCases: String = "अतिगंभीर / तातडीचे रुग्ण"
    override val scheduledAppts: String = "आजच्या भेटी"
    override val activeInQueue: String = "रांगेतील सक्रिय रुग्ण"
    override val specialistQueue: String = "तज्ज्ञ डॉक्टर रांग"
    override val noPendingCases: String = "सध्या कोणतेही प्रलंबित रुग्ण नाहीत!"
    override val symptoms: String = "लक्षणे"
    override val review: String = "रुग्ण तपासा →"
    override val history: String = "वैद्यकीय इतिहास"
    override val upcomingConsultations: String = "आगामी सल्लामसलत"
    override val proposeAppt: String = "वेळ निश्चित करा"
    override val dispensaryStock: String = "औषध साठा"
    override val lowStock: String = "कमी साठा अलर्ट"
    override val patientDirectory: String = "रुग्ण यादी"
    override val searchPatient: String = "रुग्ण शोधा"
    override val searchPlaceholder: String = "नाव किंवा आयडी टाका..."
    override val caseQueue: String = "रुग्ण रांग"
    override val reportedSymptoms: String = "नोंदवलेली लक्षणे व माहिती"
    override val doctorAdviceTitle: String = "डॉक्टरांचा सल्ला"
    override val quickTemplates: String = "झटपट वैद्यकीय सल्ले"
    override val issueRx: String = "💊 औषध चिठ्ठी द्या"
    override val refer: String = "🩺 तज्ज्ञांकडे पाठवा"
    override val submitAdvice: String = "सल्ला नोंदवा"
    override val updateAdvice: String = "सल्ला अद्यतनित करा"

    override val districtCommand: String = "जिल्हा आरोग्य नियंत्रण कक्ष"
    override val surveillanceRegion: String = "निगरानी क्षेत्र: बागेश्वर जिल्हा"
    override val totalActiveCases: String = "एकूण सक्रिय रुग्ण"
    override val monitoredVillages: String = "निगराणीखालील गावे"
    override val outbreakSurveillance: String = "साथरोग नकाशा"
    override val liveTelemetry: String = "थेट आरोग्य आकडेवारी"
    override val broadcastAlertBtn: String = "📢 आरोग्य सूचना जारी करा"
    override val dispatchedDirectives: String = "जारी केलेल्या मार्गदर्शक सूचना"
    override val dispensaryInventory: String = "औषध साठा व मागणी"

    override val uploadPrescriptionTitle: String = "डॉक्टरांची चिठ्ठी डिजिटल करा"
    override val cameraAiScan: String = "📷 कॅमेरा / AI स्कॅन"
    override val writeDown: String = "✍️ लिहून काढा"
    override val onDeviceAiBadge: String = "⚡ AI स्कॅनर: इंटरनेट नसतानाही कागदी चिठ्ठी वाचतो."
    override val selectDocSample: String = "कागदपत्राचा नमुना निवडा:"
    override val extractedRawText: String = "स्कॅन केलेला मजकूर:"
    override val parsedMedicines: String = "ओळखलेली औषधे"
    override val saveDigitizedRx: String = "डिजिटल चिठ्ठी जतन करा ✓"
    override val addMedicine: String = "➕ औषध जोडा:"
    override val medicineName: String = "औषधाचे नाव"
    override val dosage: String = "मात्रा (Dosage)"
    override val duration: String = "कालावधी"
    override val frequency: String = "कधी घ्यावे"
    override val savePrescriptionRecord: String = "औषध नोंद जतन करा ✓"

    override val callRinging: String = "घंटी वाजत आहे..."
    override val callConnecting: String = "कॉल जोडला जात आहे..."
    override val callOngoing: String = "कॉल सुरू आहे"
    override val callEnded: String = "कॉल समाप्त"
    override val endCall: String = "कॉल संपवा"
    override val mute: String = "माइक बंद"
    override val unmute: String = "माइक चालू"
    override val speaker: String = "स्पीकर"
    override val videoCall: String = "व्हिडिओ कॉल"
    override val voiceCall: String = "ऑडिओ कॉल"
    override val emergencyDoctorCall: String = "🚨 आपत्कालीन डॉक्टर कॉल"
    override val routineConsultCall: String = "🩺 नियमित सल्लामसलत कॉल"
    override val autoEscalatingInSeconds: String = "%d सेकंदात पुढील डॉक्टरांना कॉल जोडला जाईल"
    override val emergencyDoctorOnCall: String = "ड्युटीवरील आपत्कालीन डॉक्टर"

    override val medicineAvailabilityTitle: String = "औषधांची उपलब्धता व पर्याय"
    override val nearbyPharmaciesTitle: String = "जवळची औषध दुकाने व फार्मसी"
    override val inStock: String = "उपलब्ध आहे"
    override val outOfStock: String = "साठ्यात नाही"
    override val limitedStock: String = "मर्यादित साठा"
    override val alternativesAvailable: String = "समान घटकांचे पर्यायी औषध जवळ उपलब्ध"
    override val findNearbyStores: String = "जवळची दुकाने शोधा"
    override val openInMaps: String = "नकाशात पहा"

    override val doctorReferralsTitle: String = "डॉक्टर-टू-डॉक्टर तज्ज्ञ रेफरल"
    override val referToSpecialistTitle: String = "तज्ज्ञ डॉक्टरांकडे पाठवा"
    override val chooseSpecialty: String = "तज्ज्ञ विभाग निवडा"
    override val urgencyLevel: String = "तातडीची पातळी"
    override val clinicalQuestionTitle: String = "वैद्यकीय विचारणा / प्रश्न"
    override val reasonForReferral: String = "रेफर करण्याचे कारण"
    override val attachRecords: String = "मागील तपासण्या व अहवाल जोडा"
    override val acceptReferral: String = "रेफरल स्वीकारा"
    override val declineReferral: String = "रेफरल नाकारा"
    override val requestMoreInfo: String = "अधिक माहिती मागा"
    override val specialistFindingsTitle: String = "तज्ज्ञ तपासणी निष्कर्ष"
    override val specialistRecommendationsTitle: String = "रेफर करणाऱ्या डॉक्टरांसाठी सल्ला"
    override val submitSpecialistFindings: String = "अंतिम निष्कर्ष सादर करा"
    override val referralSentSuccess: String = "रेफरल तज्ज्ञांकडे पाठवले गेले."
    override val referralCompleted: String = "तज्ज्ञ तपासणी पूर्ण झाली."
    override val awaitingSpecialistReview: String = "तज्ज्ञ डॉक्टरांच्या अभिप्रायाची प्रतीक्षा आहे"
    override val specialistAccepted: String = "तज्ज्ञ डॉक्टरांनी तपासणी स्वीकारली"
    override val specialistDeclined: String = "रेफरल नाकारले गेले"

    override val selectLanguageTitle: String = "भाषा निवडा / Select Language"
    override val selectLanguageSubtitle: String = "संपूर्ण अॅपसाठी आपली पसंतीची भाषा निवडा:"
    override val currentLanguageBadge: String = "सक्रिय भाषा"
    override val applyLanguage: String = "लागू करा ✓"

    override val liveQueueTitle: String = "थेट रुग्णालय रांग"
    override val tokenNumber: String = "टोकन #%d"
    override val callNextPatient: String = "पुढील रुग्णाला बोलवा"
    override val startConsultation: String = "तपासणी सुरू करा"
    override val completeConsultation: String = "तपासणी पूर्ण करा"
    override val noShow: String = "गैरहजर नोंदवा"
    override val skipPatient: String = "पुढे जा"
    override val prioritizePatient: String = "प्राधान्य द्या"

    override val triageBreakdownTitle: String = "ट्रायज तीव्रता विश्लेषण"
    override val statTotal: String = "एकूण"
    override val statPending: String = "प्रलंबित"
    override val statResolved: String = "निराकरण"
    override val statReferred: String = "रेफर केले"
    override val medicalHistoryTitle: String = "रुग्णाचा संपूर्ण वैद्यकीय इतिहास"
    override val noMedicalHistory: String = "कोणताही वैद्यकीय इतिहास नोंदवलेला नाही."
    override val medicalHistoryTab: String = "वैद्यकीय इतिहास"

    override val liveQueueAndAppointments: String = "थेट रांग आणि भेटी"
    override val liveQueueDesc: String = "आजच चेक इन करा, टोकन क्रमांक आणि प्रतीक्षा वेळ पहा"
    override val hud: String = "एचयूडी"
    override val book: String = "नोंदवा"
    override val hospitalClinicalServices: String = "रुग्णालय आणि वैद्यकीय सेवा"
    override val hospitalServicesDesc: String = "पॅथॉलॉजी चाचण्या, डिजिटल ओपीडी टोकन आणि जिल्हा रक्त नोंदणी पहा."
    override val labReports: String = "लॅब अहवाल"
    override val labReportsSub: String = "सीबीसी, शुगर, सेरोलॉजी"
    override val opdQueue: String = "ओपीडी रांग"
    override val opdQueueSub: String = "थेट टोकन आणि केबिन"
    override val bloodBank: String = "रक्तपेढी"
    override val bloodBankSub: String = "तातडीचे युनिट्स"
    override val consultationCardTitle: String = "डॉक्टर टेलि-सल्ला"
    override val routineBadge: String = "नियमित"
    override val consultationCardDesc: String = "आपल्या डॉक्टरांशी नियमित सल्लामसलत बुक करा किंवा सामील व्हा (आणीबाणी गजर नाही)"
    override val routineCallButton: String = "कॉल / बुक"
    override val liveTokenTracker: String = "थेट टोकन ट्रॅकर"
    override val viewLabDiagnostics: String = "लॅब चाचण्या पहा"
    override val findDonors: String = "रक्तदाते शोधा"
    override val opdLiveQueueAndTokens: String = "ओपीडी थेट रांग आणि टोकन"
    override val opdSubtitle: String = "रिअल-टाइम क्लिनिक भेट टोकन आणि अंदाजे प्रतीक्षा वेळ"
    override val bookOpdToken: String = "ओपीडी टोकन बुक करा"
    override val bookHospitalOpdToken: String = "रुग्णालय ओपीडी टोकन बुक करा"
    override val yourActiveTokens: String = "आपले सक्रिय टोकन"
    override val noActiveTokens: String = "आजसाठी कोणतेही सक्रिय ओपीडी टोकन नाही"
    override val estimatedWait: String = "अंदाजे प्रतीक्षा"
    override val currentServing: String = "सध्या तपासले जात आहे"
    override val cabin: String = "केबिन"
    override val selectDepartment: String = "विभाग निवडा"
    override val selectDoctor: String = "डॉक्टर निवडा (पर्यायी)"
    override val patientFullName: String = "रुग्णाचे पूर्ण नाव"
    override val patientPhone: String = "संपर्क क्रमांक"
    override val reasonForVisit: String = "भेटीचे कारण"
    override val confirmBooking: String = "पुष्टी करा आणि टोकन मिळवा"
    override val tokenGeneratedSuccess: String = "ओपीडी टोकन यशस्वीरित्या तयार केले!"
    override val bloodBankRegistry: String = "जिल्हा रक्तपेढी नोंदणी"
    override val bloodBankSubtitle: String = "थेट रक्त घटक, रक्तदाते उपलब्धता आणि तातडीचा संपर्क"
    override val bloodUnitsAvailable: String = "उपलब्ध रक्त युनिट्स"
    override val callBloodBank: String = "रक्तपेढीला कॉल करा"
    override val requestBloodUnits: String = "तातडीच्या रक्ताची विनंती करा"
    override val filterBloodGroup: String = "रक्तगट निवडा"
    override val allGroups: String = "सर्व रक्तगट"
    override val donorDirectory: String = "सत्यापित रक्तदाते"
    override val units: String = "युनिट्स"
    override val lastUpdated: String = "शेवटचे अपडेट"
    override val diagnosticLabReports: String = "वैद्यकीय लॅब अहवाल"
    override val labReportsSubtitle: String = "पॅथॉलॉजी आणि बायोकेमिस्ट्री तपासणी नोंदी"
    override val downloadReport: String = "पीडीएफ डाउनलोड करा"
    override val normalRange: String = "सामान्य श्रेणी"
    override val sampleCollected: String = "नमुना गोळा केला"
    override val reportDelivered: String = "अहवाल तयार आहे"
    override val noLabReportsFound: String = "कोणतेही लॅब अहवाल सापडले नाहीत"
    override val testParameters: String = "चाचणी पॅरामीटर्स"
    override val interpretation: String = "डॉक्टरांचा निष्कर्ष"
    override val ipdBedTracker: String = "आयपीडी खाटा उपलब्धता ट्रॅकर"
    override val ipdSubtitle: String = "आयसीयू, ऑक्सिजन आणि जनरल वॉर्डमधील थेट खाटा उपलब्धता"
    override val icuBeds: String = "आयसीयू खाटा"
    override val oxygenBeds: String = "ऑक्सिजन खाटा"
    override val generalWard: String = "जनरल वॉर्ड"
    override val occupied: String = "भरलेले"
    override val available: String = "उपलब्ध"
    override val totalBeds: String = "एकूण खाटा"
    override val admitPatient: String = "रुग्णाला दाखल करा"
    override val dischargePatient: String = "रुग्णाला डिस्चार्ज करा"
    override val otScheduler: String = "शस्त्रक्रिया थिएटर वेळापत्रक"
    override val otSubtitle: String = "शस्त्रक्रिया वेळ आणि तातडीचे स्लॉट्स"
    override val emergencyOt: String = "तातडीचे ओटी"
    override val bookOtSlot: String = "ओटी स्लॉट बुक करा"
    override val surgeonInCharge: String = "प्रभारी शल्यचिकित्सक"
    override val procedure: String = "प्रक्रिया / शस्त्रक्रिया"
    override val scheduledTime: String = "नियोजित वेळ"
    override val otStatus: String = "ओटी स्थिती"
    override val bioMedicalTracker: String = "बायो-मेडिकल उपकरण ट्रॅकर"
    override val bioMedicalSubtitle: String = "जीवनरक्षक उपकरणे, कॅलिब्रेशन लॉग आणि कार्यस्थिती"
    override val ventilators: String = "व्हेंटिलेटर"
    override val defibrillators: String = "डिफिब्रिलेटर"
    override val dialysisUnits: String = "डायलिसीस युनिट्स"
    override val operational: String = "कार्यरत"
    override val underMaintenance: String = "देखभालीखाली"
    override val reportFault: String = "दोष नोंदवा"
    override val openHud: String = "एचयूडी उघडा"
    override val clinicalWorkstation: String = "वैद्यकीय कार्यस्थान"
    override val activeOpdQueue: String = "सक्रिय ओपीडी रांग"
    override val inCallHud: String = "सल्लामसलत दरम्यान एचयूडी"
    override val teleConsultInProgress: String = "टेलि-सल्ला सुरू आहे"
    override val muteMic: String = "माइक बंद"
    override val unmuteMic: String = "माइक चालू"
    override val turnVideoOff: String = "व्हिडिओ बंद"
    override val turnVideoOn: String = "व्हिडिओ सुरू"
    override val switchCamera: String = "कॅमेरा बदला"
    override val liveTeleVitals: String = "थेट टेलि-व्हायटल्स"
    override val networkQuality: String = "नेटवर्क गुणवत्ता"
    override val goodConnection: String = "मजबूत कनेक्शन"
    override val poorConnection: String = "कमकुवत कनेक्शन"
    override val prescribeDuringCall: String = "कॉल दरम्यान औषधे लिहा"
    override val liveQueueHud: String = "थेट रांग एचयूडी"
    override val bookACall: String = "कॉल बुक करा"
    override val bookTeleConsultation: String = "टेलि-सल्ला बुक करा"
    override val bookConsultationSubtitle: String = "सत्यापित तज्ज्ञांसोबत व्हिडिओ किंवा व्हॉइस सल्लामसलत बुक करा."
    override val scheduledAppointments: String = "नियोजित सल्लामसलत"
    override val noUpcomingAppointments: String = "कोणत्याही आगामी भेटी नियोजित नाहीत"
    override val joinCall: String = "कॉलमध्ये सामील व्हा"
    override val selectDate: String = "सल्ल्याची तारीख निवडा"
    override val selectTimeSlot: String = "वेळ स्लॉट निवडा"
    override val consultationType: String = "सल्ल्याचा प्रकार"
    override val navHome: String = "मुख्यपृष्ठ"
    override val navAppointments: String = "भेटी"
    override val navPrescriptions: String = "औषधपत्रिका"
    override val navSettings: String = "सेटिंग्ज"
    override val hudStatusSafe: String = "सुरक्षित"
    override val hudStatusAttention: String = "लक्ष द्या"
    override val hudStatusDanger: String = "धोका"
    override val hudStatusCritical: String = "अत्यंत गंभीर"
    override val hudMonitoring: String = "सतत देखरेख"
    override val appointmentReminderTitle: String = "आगामी डॉक्टर सल्लामसलत"
    override val appointmentReminderBody: String = "%1\$s सोबत तुमची भेट 15 मिनिटांत सुरू होईल."

    // Low Connectivity & Sync
    override val slowNetwork: String = "मंद इंटरनेट कनेक्शन"
    override val syncing: String = "डेटा सिंक होत आहे…"
    override val pendingChanges: String = "%d बदल सिंक होणे बाकी"
    override val lastSynchronized: String = "शेवटचे सिंक: %s"
    override val offlineSosWarning: String = "⚠️ डिव्हाइस ऑफलाइन आहे: थेट सर्व्हर अलर्ट उपलब्ध नाही. त्वरित १०८ कॉल किंवा आशा सेविकेला SMS पाठवा."
    override val cachedDataFreshness: String = "कॅश केलेला ऑफलाइन डेटा (%s)"
    override val syncComplete: String = "सर्व डेटा सिंक झाला आहे"
    override val manualSync: String = "आत्ता सिंक करा"

}
val MarathiStrings: AppStrings = MarathiAppStrings()

val LocalAppStrings = staticCompositionLocalOf { EnglishStrings }

object AppLanguageManager {
    fun getStrings(language: AppLanguage): AppStrings {
        return when (language) {
            AppLanguage.ENGLISH -> EnglishStrings
            AppLanguage.HINDI -> HindiStrings
            AppLanguage.TAMIL -> TamilStrings
            AppLanguage.MARATHI -> MarathiStrings
        }
    }
}
