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


    // New Multilingual AppStrings Additions
    val loginEnterBtn: String
    val smartHealthId: String
    val secureVerifiedBadge: String
    val signInWithGoogle: String
    val instantDemoSignIn: String
    val scanAshaCardQr: String
    val doctorConsultationDesk: String
    val uniqueDoctorId: String
    val egDoctorId: String
    val signInWithDoctorId: String
    val ashaFieldWorkerDesk: String
    val egAshaId: String
    val pinPasscode: String
    val signInWithAshaId: String
    val officialGovEmail: String
    val passcodeLabel: String
    val scanningAshaQr: String
    val villageAgeLabel: String
    val ashaWorkerLabel: String
    val sehatSetuBrand: String
    val ambulance108: String
    val adminEmailPlaceholder: String
    val systemBroadcast: String
    val broadcastTitle: String
    val broadcastMessage: String
    val diagnosticsAvailability: String
    val liveMachineLabStatus: String
    val monitorRealTimeStatus: String
    val diseaseTrendsTitle: String
    val villageSelection: String
    val outbreakTrendsCases: String
    val noTrendDataVillage: String
    val recordNewData: String
    val diseaseLabel: String
    val totalCasesLabel: String
    val dispensaryRestockTitle: String
    val manageInventory: String
    val lowStockTag: String
    val addQuantityLabel: String
    val facilityQualityMetrics: String
    val backAction: String
    val overallHealthSystemQuality: String
    val doctorsFlaggedLowMeds: String
    val restockAction: String
    val restockNowBtn: String
    val dismissReminder: String
    val pinnedOnMap: String
    val hospitalOpsCareDesk: String
    val hospitalOpsCareDesc: String
    val ipdWardsBeds: String
    val occupancyAdmission: String
    val otSurgeryDesk: String
    val pacSurgeonRoster: String
    val externalReferralsDesk: String
    val aiimsCashlessDesk: String
    val bioMedicalRegistry: String
    val oxygenEquipment: String
    val liveClinicQueueOversight: String
    val monitorDoctorQueues: String
    val monitorBtn: String
    val monitorPhcInfrastructure: String
    val viewBtn: String
    val dispatchedStatus: String
    val dismissBtn: String
    val dispensaryLowStockAlerts: String
    val allStockAboveThresholds: String
    val broadcastNowBtn: String
    val targetVillageAudience: String
    val currentServingToken: String
    val waitingInLine: String
    val noPatientsInQueueToday: String
    val tapDoctorToInspect: String
    val nowServingLabel: String
    val inWaitingLabel: String
    val avgWaitLabel: String
    val reviewAccountsTitle: String
    val doctorsCategory: String
    val ashasCategory: String
    val villagesCategory: String
    val villageOutbreakHeatmap: String
    val mapsLabel: String
    val kmDragPan: String
    val interactiveMapsEnhance: String
    val updateAction: String
    val hospitalCareBme: String
    val maintenanceDue: String
    val bmeEngineering: String
    val twentyFourSevenOnCall: String
    val lastServiced: String
    val nextDueDate: String
    val updateStatusBtn: String
    val selectOperationalStatus: String
    val saveStatusBtn: String
    val criticalShortages: String
    val emergencyTransfusionProtocol: String
    val emergencyTransfusionDesc: String
    val hospitalCareIpd: String
    val totalCapacity: String
    val admittedPatients: String
    val availableVacant: String
    val clearDischargeBed: String
    val confirmAdmission: String
    val abnormalFindings: String
    val noLabInvestigationsCategory: String
    val viewFullEReport: String
    val certifiedLabReport: String
    val investigationFindings: String
    val pathologistClinicalNotes: String
    val orderDiagnosticLabTest: String
    val selectInvestigationPanel: String
    val hospitalDeptsLiveBoard: String
    val liveOpdQueueTitle: String
    val yourTokenNumber: String
    val departmentLabel: String
    val roomCabinLabel: String
    val estWaitTime: String
    val noActiveOpdToken: String
    val opdDigitalSlipDesc: String
    val servingTokenPrefix: String
    val surgicalCareOtModule: String
    val leadSurgeonLabel: String
    val surgeonSpecialtyLabel: String
    val pacValidatedBadge: String
    val noSurgicalProceduresScheduled: String
    val timeSlotLabel: String
    val operatingSurgeon: String
    val anesthetistLabel: String
    val pacClearedCheck: String
    val confirmOtSlotBtn: String
    val hospitalDeskLabel: String
    val hospitalNetworkExternal: String
    val superSpecialtyReferrals: String
    val empanelledHospitalsDesk: String
    val issueVoucherBtn: String
    val activeReferralPasses: String
    val tieUpNetwork: String
    val networkHospitalsSample: String
    val cashlessApprovedBadge: String
    val beneficiaryPatient: String
    val ambulanceRequisitioned: String
    val issueSuperSpecialtyVoucher: String
    val requisitionEmergencyAmbulance: String
    val issueSignVoucherBtn: String
    val sehatSetuSplashTitle: String
    val bridgingRuralHealthZeroNet: String
    val encryptedOfflineAbha: String
    val todaysWorklist: String
    val routineFollowUp: String
    val highRiskRegistry: String
    val allPatientsHighRisk: String
    val markEmergencyClear: String
    val dispatchEmergencySosDesc: String
    val confirmEmergencyResolved: String
    val yesMarkClearDismiss: String
    val chatWithPatient: String
    val messagesPersistLocally: String
    val sendNoticeToCaseload: String
    val dailyVillageRounds: String
    val logVisitBtn: String
    val villageRoundsDoorToDoor: String
    val noVillageRoundsLogged: String
    val maternalCategory: String
    val childCategory: String
    val vaccineCategory: String
    val immunizationTrackerTitle: String
    val maternalChildRecords: String
    val noRecordsFound: String
    val vaccinationSchedule: String
    val medicineRestockTracker: String
    val ashaFieldKitStock: String
    val noMedicinesInKit: String
    val kitRefillNeededPhc: String
    val requestRefill50: String
    val registerNewPatientTitle: String
    val nameFieldLabel: String
    val ageFieldLabel: String
    val logVillageRoundVisitTitle: String
    val doorToDoorHealthRecord: String
    val servicesProvidedVisit: String
    val maternalAncService: String
    val childHealthService: String
    val immunizationService: String
    val medicineIfaService: String
    val saveVillageRoundVisit: String
    val registerNewVillagerTitle: String
    val genderLabel: String
    val assignedVillageLabel: String
    val initialRiskLevelLabel: String
    val registerVillagerCaseload: String
    val broadcastVillageAdvisory: String
    val quickAdvisoryTemplates: String
    val broadcastTargetVillage: String
    val broadcastToVillageDashboard: String
    val pendingAppointmentsTitle: String
    val submittedViaAshaHelper: String
    val directPatientSubmission: String
    val historyAndRx: String
    val healthCardTab: String
    val mentalHealthCaseFlag: String
    val mentalHealthApproachNotice: String
    val confidentialDoctorNotes: String
    val clinicalActionsTitle: String
    val ocrDigitizedBadge: String
    val lowStockAlertBadge: String
    val clinicalTriageToday: String
    val specialistReferralsQueue: String
    val triageIncomingConsults: String
    val otDeskTab: String
    val surgeriesAndPac: String
    val ipdBedsTab: String
    val wardOccupancy: String
    val referralsTab: String
    val aiimsTieUp: String
    val noActiveSosAlerts: String
    val mentalHealthReferral: String
    val noAppointmentsScheduled: String
    val declineAction: String
    val acceptCheckAction: String
    val roomOpenStatus: String
    val rescheduleAction: String
    val patientDidntJoinWindow: String
    val adminRemindedBadge: String
    val remindAdminBtn: String
    val callActionBtn: String
    val directiveLabel: String
    val liveVitalsStatusHalo: String
    val transferToNextOnCall: String
    val nowServingTokenCaps: String
    val walkInLabel: String
    val activeConsultationLabel: String
    val orderedByCheckIn: String
    val queueAllCaughtUp: String
    val noPatientsWaitingNow: String
    val selectWalkInPatient: String
    val selectArrowBtn: String
    val pendingCasesTitle: String
    val dosageLabel: String
    val noReferralsInQueue: String
    val specificClinicalQuestionAsk: String
    val attachedRecordsLabel: String
    val closedLoopFindingsRecorded: String
    val askInfoBtn: String
    val declineReferralBtn: String
    val callPatientConsultBtn: String
    val sendFindingsBackBtn: String
    val provideDeclineRationale: String
    val declineRationalePlaceholder: String
    val suggestedSpecialistDept: String
    val suggestedSpecialistPlaceholder: String
    val declineAndNotifyBtn: String
    val requestMoreInfoTitle: String
    val specifyDetailsNeedBeforeAccepting: String
    val requestInfoPlaceholder: String
    val sendRequestBtn: String
    val doctorToDoctorReferral: String
    val selectTargetSpecialty: String
    val routingTriageAssignment: String
    val specialtyQueueOption: String
    val namedSpecialistOption: String
    val directPhysicianHandoff: String
    val noNamedSpecialistFallback: String
    val urgencyLevelRequired: String
    val emergencyWarningQueueDelay: String
    val referralQueueNotAcuteResponse: String
    val launchEmergencySosNow: String
    val clinicalReasonForReferral: String
    val describeClinicalFindingsPrompt: String
    val specificClinicalQuestionHeading: String
    val clearlySpecifyQuestionInstruction: String
    val clinicalQuestionPlaceholder: String
    val sendReferralToSpecialist: String
    val configureClinicQueueSlots: String
    val manageCapacityWalkInRules: String
    val acceptWalkInQueue: String
    val allowDirectCheckinNoBooking: String
    val issueMedicalCertificateTitle: String
    val certifiedClinicalLeaveFitness: String
    val certificateTypeLabel: String
    val certificateSealedStampNotice: String
    val patientHealthCardTitle: String
    val viewOnlyAccessRule: String
    val latestReportedCondition: String
    val medicalHistoryAndRecords: String
    val recordsHeading: String
    val noConditionRecordsLogged: String
    val noPriorPrescriptionsUploaded: String
    val aiDigitizedBadge: String
    val outOfStockNearPatientWarning: String
    val likelyAvailableNearPatient: String
    val addAnotherMedicineBtn: String
    val medicineNamePlaceholder: String
    val notFoundNearPatientLocation: String
    val swapMedicineBtn: String
    val medicineSuggestionDisclaimer: String
    val quantityShort: String
    val frequencyAndTiming: String
    val durationLabel: String
    val addToPrescriptionBtn: String
    val dietaryFollowUpInstructions: String
    val instructionsPatientAsha: String
    val selectProposedDate: String
    val selectTimeSlotDialog: String
    val sendProposalBtn: String
    val startConsultBtn: String
    val noShowBtn: String
    val referCaseToSpecialist: String
    val selectTargetSpecialtyColon: String
    val clinicalReferralNotesColon: String
    val transferCaseArrow: String
    val scheduleNewAppointmentTitle: String
    val proposeConsultationTime: String
    val selectPatientColon: String
    val selectDateColon: String
    val availableTimeSlotColon: String
    val sendAppointmentProposalCheck: String
    val specialistLoopClosure: String
    val referringAskClinicalQuestion: String
    val clinicalFindingsDiagnosticAssessment: String
    val documentEvaluationFindingsPrompt: String
    val ongoingCarePlanRecommendations: String
    val adviseTreatmentAdjustmentsPrompt: String
    val specialistFollowUpRequired: String
    val sendFindingsCloseLoop: String
    val ultraLowBandwidthMode: String
    val connectedPhcTeleKiosk: String
    val pulseLabel: String
    val bpLabel: String
    val spo2VitalsLabel: String
    val tempLabel: String
    val tapToExpand: String
    val patientHealthVitals: String
    val bpNormalSample: String
    val heartRateSample: String
    val bloodOxygenSample: String
    val temperatureSample: String
    val chronicConditionNone: String
    val lastVisitSample: String
    val camOffLabel: String
    val tapToEnableCam: String
    val switchToVoiceCallWeakSignal: String
    val doctorDidntJoinRebook: String
    val rebookCallBtn: String
    val waitingForDoctorToJoin: String
    val statusNextInQueue: String
    val doctorWrappingUpMsg: String
    val enterConsultationRoom: String
    val cancelLeaveBtn: String
    val selectConsultationModeNetwork: String
    val videoCallHd: String
    val requires4gWifi: String
    val voiceCallLowBandwidth: String
    val recommended2gSignal: String
    val confirmBookingCheck: String
    val selectSeverityLevel: String
    val nearestDoctorsListView: String
    val distanceMocked: String
    val findMedicineNearby: String
    val notFoundNearbyAlternative: String
    val likelyInStock: String
    val outOfStockTag: String
    val callPharmacyBtn: String
    val docSuggestedAlternative: String
    val docSuggestedAlternativePlain: String
    val pharmacyStockNotice: String
    val helpManualTitle: String
    val bloodGroupLabel: String
    val oPositiveSample: String
    val allergiesLabel: String
    val noneReported: String
    val emergencyLabel: String
    val permanentOfflineQrIdentity: String
    val permanentQrOfflineRecord: String
    val symptomsSubmittedTriage: String
    val aiScannedBadge: String
    val findNearbyLink: String
    val ruralHealthSchemesPmjay: String
    val freeTreatment5Lakh: String
    val viewSchemesBtn: String
    val uploadPrescriptionOcr: String
    val extractedTextLabel: String
    val noPrescriptionsFound: String
    val prescribedMedicinesLabel: String
    val liveVisitQueue: String
    val noActiveQueueTicket: String
    val checkInScheduledDesc: String
    val getInstantTokenToday: String
    val yourTokenNumberCaps: String
    val confirmingPosition: String
    val queuePositionLabel: String
    val attendingPhysician: String
    val cancelTokenBtn: String
    val govtSchemesTitle: String
    val governmentHealthSchemes: String
    val ruralWelfarePrograms: String
    val eligibleBadge: String
    val closeSchemesView: String
    val digitalHealthCardUmid: String
    val vitalSenseIdentity: String
    val linkedBeneficiariesFamily: String
    val primarySelf: String
    val scanAtClinicDispensary: String
    val emergencyContactLabel: String
    val assignedAshaLabel: String
    val activeClinicalConditionLabel: String
    val linkAbhaBtn: String
    val offlineSqliteEncrypted: String
    val logHealthSymptomsTitle: String
    val categoryCaps: String
    val selectCommonSymptoms: String
    val severityLevelCaps: String
    val submitToDoctorTriage: String
    val careJourneyTitle: String
    val spo2Label: String
    val backArrowBtn: String
    val howAreYouFeelingToday: String
    val checkInSavedNotice: String
    val guidedBreathingTitle: String
    val breathe4SecondsMsg: String
    val tapToStart: String
    val digitizePaperPrescription: String
    val addPrescribedMedicines: String
    val addMedicineBtn: String
    val positionPrescriptionFrame: String
    val googleAutoCropScanner: String
    val cantScanEnterManually: String
    val cameraPermissionNeeded: String
    val cameraPermissionReason: String
    val cameraAccessDeclinedMsg: String
    val openAppSettingsBtn: String
    val allowCameraAccessBtn: String
    val enterDetailsManuallyBtn: String
    val aiPrescriptionDigitizer: String
    val zeroCloudOfflineInference: String
    val selectPrescriptionPhotoDesc: String
    val simulateCaptureScan: String
    val feverRxSample: String
    val infectionSample: String
    val extractedClinicalEntities: String
    val rawOcrTextStream: String
    val clinicalInstructionsNotes: String
    val saveToMedicalRecord: String
    val readingPrescriptionOnDevice: String
    val runningLocalMlKitOcr: String
    val reviewConfirmOcrScan: String
    val extractedTextTapToEdit: String
    val onDeviceOcrBadge: String
    val noMedicineNamesMatchedFallback: String
    val prescribingDoctorHealthPost: String
    val instructionsDosageDirections: String
    val retakePhotoBtn: String
    val couldntReadAnyText: String
    val photoQualityHint: String
    val enterPrescriptionManually: String
    val reviewPrescriptionPhoto: String
    val ensureHandwritingReadable: String
    val useThisPhotoScanText: String

    // New Button AppStrings Additions

    // New Button AppStrings Additions
    val saveRecord: String
    val restockItem: String
    val broadcastDistrictDirective: String
    val manageDispensary: String
    val diagnosticsLabs: String
    val visitAction: String
    val logVitalsAction: String
    val viewProfile: String
    val startTeleConsultCall: String
    val scanExternalRxOcr: String
    val saveConfiguration: String
    val digitallySignIssue: String
    val closeHealthCard: String
    val closeMedicalHistory: String
    val closeEReport: String
    val issueOrder: String
    val bookOpdTokenNow: String
    val submitToDoctorQueueCheck: String
    val viewCareJourneyTimeline: String
    val saveCheckIn: String
    val savePrescriptionRecord: String
    val saveDigitizedPrescription: String
    val manualHelpOverview: String
    val clinicalAskPrefix: String

    // Final Polish AppStrings Additions
    val scanPhysicalCardZeroPwdDesc: String
    val patientIdentityVerified: String
    val referredByDoctor: String
    val specialistFindingsDiagnosticAssessment: String
    val specialistRecommendationsCarePlan: String
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


    // New Multilingual EnglishAppStrings Additions
    override val loginEnterBtn: String = "Enter →"
    override val smartHealthId: String = "SMART HEALTH ID"
    override val secureVerifiedBadge: String = "SECURE VERIFIED"
    override val signInWithGoogle: String = "Sign in with Google"
    override val instantDemoSignIn: String = "⚡ Instant Demo Sign In"
    override val scanAshaCardQr: String = "🪪 Scan ASHA Card (QR Claim)"
    override val doctorConsultationDesk: String = "Doctor Consultation Desk"
    override val uniqueDoctorId: String = "Unique Doctor ID"
    override val egDoctorId: String = "e.g. DOC-101"
    override val signInWithDoctorId: String = "Sign In with Doctor ID"
    override val ashaFieldWorkerDesk: String = "ASHA Field Worker Desk"
    override val egAshaId: String = "e.g. ASHA-401"
    override val pinPasscode: String = "PIN / Passcode"
    override val signInWithAshaId: String = "Sign In with ASHA ID"
    override val officialGovEmail: String = "Official Gov Email"
    override val passcodeLabel: String = "Passcode"
    override val scanningAshaQr: String = "Scanning ASHA QR..."
    override val villageAgeLabel: String = "Village:  · Age:  ()"
    override val ashaWorkerLabel: String = "ASHA Worker:"
    override val sehatSetuBrand: String = "SehatSetu"
    override val ambulance108: String = "108 Ambulance"
    override val adminEmailPlaceholder: String = "admin@vitalsense.gov.in"
    override val systemBroadcast: String = "System Broadcast"
    override val broadcastTitle: String = "Title"
    override val broadcastMessage: String = "Message"
    override val diagnosticsAvailability: String = "Diagnostics Availability"
    override val liveMachineLabStatus: String = "Live Machine & Lab Status"
    override val monitorRealTimeStatus: String = "Monitor the real-time operational status of all facility diagnostic machines and laboratories."
    override val diseaseTrendsTitle: String = "Disease Trends"
    override val villageSelection: String = "Village Selection"
    override val outbreakTrendsCases: String = "Outbreak Trends (Total Cases)"
    override val noTrendDataVillage: String = "No trend data available for this village."
    override val recordNewData: String = "Record New Data"
    override val diseaseLabel: String = "Disease"
    override val totalCasesLabel: String = "Total Cases"
    override val dispensaryRestockTitle: String = "Dispensary Restock"
    override val manageInventory: String = "Manage Inventory"
    override val lowStockTag: String = "LOW STOCK"
    override val addQuantityLabel: String = "Add quantity"
    override val facilityQualityMetrics: String = "Facility Quality Metrics"
    override val backAction: String = "Back"
    override val overallHealthSystemQuality: String = "Overall Health System Quality"
    override val doctorsFlaggedLowMeds: String = "Doctors have flagged low dispensary medicines"
    override val restockAction: String = "RESTOCK"
    override val restockNowBtn: String = "📦 Restock Now"
    override val dismissReminder: String = "✕ Dismiss Reminder"
    override val pinnedOnMap: String = "PINNED ON MAP 📍"
    override val hospitalOpsCareDesk: String = "Hospital Operations & Care Desk"
    override val hospitalOpsCareDesc: String = "Real-time in-patient wards, surgical suites, tertiary referrals, and critical biomedical assets."
    override val ipdWardsBeds: String = "IPD Wards & Beds"
    override val occupancyAdmission: String = "Occupancy & Admission"
    override val otSurgeryDesk: String = "OT Surgery Desk"
    override val pacSurgeonRoster: String = "PAC & Surgeon Roster"
    override val externalReferralsDesk: String = "External Referrals"
    override val aiimsCashlessDesk: String = "AIIMS & Cashless Desk"
    override val bioMedicalRegistry: String = "Bio-Medical Registry"
    override val oxygenEquipment: String = "Oxygen & Equipment"
    override val liveClinicQueueOversight: String = "Live Clinic Queue Oversight"
    override val monitorDoctorQueues: String = "Monitor doctor queues, wait times and clinic load"
    override val monitorBtn: String = "Monitor"
    override val monitorPhcInfrastructure: String = "Monitor PHC/CHC infrastructure and feedback"
    override val viewBtn: String = "View"
    override val dispatchedStatus: String = "DISPATCHED"
    override val dismissBtn: String = "✕ Dismiss"
    override val dispensaryLowStockAlerts: String = "Dispensary Low Stock Alerts"
    override val allStockAboveThresholds: String = "All stock is above reorder thresholds."
    override val broadcastNowBtn: String = "Broadcast Now"
    override val targetVillageAudience: String = "Target Village / Audience"
    override val currentServingToken: String = "Current Serving Token"
    override val waitingInLine: String = "Waiting in Line"
    override val noPatientsInQueueToday: String = "No patients in queue for this doctor today."
    override val tapDoctorToInspect: String = "Tap doctor to inspect queue"
    override val nowServingLabel: String = "Now Serving"
    override val inWaitingLabel: String = "In Waiting"
    override val avgWaitLabel: String = "Avg Wait"
    override val reviewAccountsTitle: String = "Review Accounts"
    override val doctorsCategory: String = "Doctors"
    override val ashasCategory: String = "ASHAs"
    override val villagesCategory: String = "Villages"
    override val villageOutbreakHeatmap: String = "Village Outbreak Heatmap"
    override val mapsLabel: String = "Maps"
    override val kmDragPan: String = "2 km ───┤ (Drag to pan freely)"
    override val interactiveMapsEnhance: String = "Interactive Google Maps & enhancements"
    override val updateAction: String = "Update"
    override val hospitalCareBme: String = "Hospital Care · BME"
    override val maintenanceDue: String = "Maintenance / Due"
    override val bmeEngineering: String = "BME Engineering"
    override val twentyFourSevenOnCall: String = "24x7 On-Call"
    override val lastServiced: String = "Last Serviced"
    override val nextDueDate: String = "Next Due Date"
    override val updateStatusBtn: String = "Update Status"
    override val selectOperationalStatus: String = "Select Operational Status:"
    override val saveStatusBtn: String = "Save Status"
    override val criticalShortages: String = "Critical Shortages"
    override val emergencyTransfusionProtocol: String = "Emergency Transfusion Protocol"
    override val emergencyTransfusionDesc: String = "Universal Donor: O Negative (O-) · Universal Recipient: AB Positive (AB+). For maternal hemorrhages or road trauma, cross-matching is fast-tracked at District Hospital Rampur."
    override val hospitalCareIpd: String = "Hospital Care · IPD"
    override val totalCapacity: String = "Total Capacity"
    override val admittedPatients: String = "Admitted Patients"
    override val availableVacant: String = "Available Vacant"
    override val clearDischargeBed: String = "Clear & Discharge Bed"
    override val confirmAdmission: String = "Confirm Admission"
    override val abnormalFindings: String = "Abnormal Findings"
    override val noLabInvestigationsCategory: String = "No lab investigations in this category"
    override val viewFullEReport: String = "View Full E-Report ➔"
    override val certifiedLabReport: String = "Certified Laboratory Report"
    override val investigationFindings: String = "Investigation Findings"
    override val pathologistClinicalNotes: String = "Pathologist Clinical Notes"
    override val orderDiagnosticLabTest: String = "Order Diagnostic Lab Test"
    override val selectInvestigationPanel: String = "Select Investigation Panel:"
    override val hospitalDeptsLiveBoard: String = "Hospital Departments Live Board"
    override val liveOpdQueueTitle: String = "LIVE OPD QUEUE"
    override val yourTokenNumber: String = "Your Token Number"
    override val departmentLabel: String = "Department"
    override val roomCabinLabel: String = "Room / Cabin"
    override val estWaitTime: String = "Est. Wait Time"
    override val noActiveOpdToken: String = "No Active OPD Token"
    override val opdDigitalSlipDesc: String = "Self check-in or generate a digital queue slip to visit PHC / District Hospital doctors without physical lines."
    override val servingTokenPrefix: String = "Serving:"
    override val surgicalCareOtModule: String = "Surgical Care · OT Module"
    override val leadSurgeonLabel: String = "Lead Surgeon: Dr. Ayushman Dev Singh"
    override val surgeonSpecialtyLabel: String = "MDS, Maxillofacial Trauma & Reconstructive Surgery"
    override val pacValidatedBadge: String = "PAC Validated"
    override val noSurgicalProceduresScheduled: String = "No surgical procedures currently scheduled in OT."
    override val timeSlotLabel: String = "Time Slot"
    override val operatingSurgeon: String = "Operating Surgeon"
    override val anesthetistLabel: String = "Anesthetist"
    override val pacClearedCheck: String = "Pre-Anesthesia Checkup (PAC) Cleared"
    override val confirmOtSlotBtn: String = "Confirm OT Slot"
    override val hospitalDeskLabel: String = "Hospital Desk"
    override val hospitalNetworkExternal: String = "Hospital Network · External Referrals"
    override val superSpecialtyReferrals: String = "🏛️ Super-Specialty External Referrals"
    override val empanelledHospitalsDesk: String = "Empanelled Apex Hospitals & Cashless Requisition Desk"
    override val issueVoucherBtn: String = "+ Issue Voucher"
    override val activeReferralPasses: String = "Active Referral Passes"
    override val tieUpNetwork: String = "Tie-up Network"
    override val networkHospitalsSample: String = "AIIMS, Central Rly, KGMU"
    override val cashlessApprovedBadge: String = "✓ CASHLESS APPROVED"
    override val beneficiaryPatient: String = "Beneficiary Patient"
    override val ambulanceRequisitioned: String = "🚑 Ambulance Requisitioned"
    override val issueSuperSpecialtyVoucher: String = "Issue Super-Specialty Referral Voucher"
    override val requisitionEmergencyAmbulance: String = "Requisition Emergency Transport / Ambulance"
    override val issueSignVoucherBtn: String = "Issue & Sign Voucher"
    override val sehatSetuSplashTitle: String = "SEHAT SETU · सेहत सेतु"
    override val bridgingRuralHealthZeroNet: String = "Bridging Rural Healthcare · Zero-Internet Ready"
    override val encryptedOfflineAbha: String = "Encrypted Offline SQLite · ABHA Ready"
    override val todaysWorklist: String = "📅 Today's Worklist"
    override val routineFollowUp: String = "Routine Follow-up"
    override val highRiskRegistry: String = "🚨 High-Risk Registry"
    override val allPatientsHighRisk: String = "All patients are in the High-Risk Registry."
    override val markEmergencyClear: String = "Mark Emergency Clear"
    override val dispatchEmergencySosDesc: String = "This will immediately dispatch a high-priority SOS alert to doctors and emergency response."
    override val confirmEmergencyResolved: String = "Confirm Emergency Resolved"
    override val yesMarkClearDismiss: String = "Yes, Mark Clear & Dismiss"
    override val chatWithPatient: String = "Chat with Patient"
    override val messagesPersistLocally: String = "Messages persist locally (mocked thread)"
    override val sendNoticeToCaseload: String = "Send Notice to Caseload"
    override val dailyVillageRounds: String = "Daily Village Rounds"
    override val logVisitBtn: String = "Log Visit"
    override val villageRoundsDoorToDoor: String = "Village Rounds & Door-to-Door Visits"
    override val noVillageRoundsLogged: String = "No village rounds logged yet. Tap '+ Log Visit' to record door-to-door checkups."
    override val maternalCategory: String = "🤰 Maternal"
    override val childCategory: String = "👶 Child"
    override val vaccineCategory: String = "💉 Vaccine"
    override val immunizationTrackerTitle: String = "Immunization Tracker"
    override val maternalChildRecords: String = "Maternal & Child Records"
    override val noRecordsFound: String = "No records found."
    override val vaccinationSchedule: String = "Vaccination Schedule"
    override val medicineRestockTracker: String = "Medicine Restock Tracker"
    override val ashaFieldKitStock: String = "ASHA Field Kit Stock & Indent"
    override val noMedicinesInKit: String = "No medicines found in kit."
    override val kitRefillNeededPhc: String = "Kit refill needed from PHC dispensary"
    override val requestRefill50: String = "Request Refill (+50)"
    override val registerNewPatientTitle: String = "Register New Patient"
    override val nameFieldLabel: String = "Name"
    override val ageFieldLabel: String = "Age"
    override val logVillageRoundVisitTitle: String = "Log Village Round Visit"
    override val doorToDoorHealthRecord: String = "Door-to-Door Health Record"
    override val servicesProvidedVisit: String = "SERVICES PROVIDED DURING VISIT"
    override val maternalAncService: String = "🤰 Maternal / ANC"
    override val childHealthService: String = "👶 Child Health"
    override val immunizationService: String = "💉 Immunization"
    override val medicineIfaService: String = "💊 Medicine / IFA"
    override val saveVillageRoundVisit: String = "✓ Save Village Round Visit"
    override val registerNewVillagerTitle: String = "Register New Villager"
    override val genderLabel: String = "GENDER"
    override val assignedVillageLabel: String = "ASSIGNED VILLAGE"
    override val initialRiskLevelLabel: String = "INITIAL RISK LEVEL"
    override val registerVillagerCaseload: String = "✓ Register Villager into Caseload"
    override val broadcastVillageAdvisory: String = "Broadcast Village Advisory"
    override val quickAdvisoryTemplates: String = "QUICK ADVISORY TEMPLATES"
    override val broadcastTargetVillage: String = "BROADCAST TARGET VILLAGE"
    override val broadcastToVillageDashboard: String = "📢 Broadcast to Village Dashboard"
    override val pendingAppointmentsTitle: String = "Pending Appointments"
    override val submittedViaAshaHelper: String = "🤝 Submitted via ASHA Helper"
    override val directPatientSubmission: String = "Direct Patient Submission"
    override val historyAndRx: String = "📋 History & Rx"
    override val healthCardTab: String = "🪪 Health Card"
    override val mentalHealthCaseFlag: String = "Mental Health Case Flag"
    override val mentalHealthApproachNotice: String = "Patient logged psychological stress/anxiety symptoms. Approached with empathy and holistic care."
    override val confidentialDoctorNotes: String = "🔒 Confidential Clinical Notes (Doctor-Only)"
    override val clinicalActionsTitle: String = "Clinical Actions"
    override val ocrDigitizedBadge: String = "OCR Digitized"
    override val lowStockAlertBadge: String = "LOW STOCK ALERT"
    override val clinicalTriageToday: String = "CLINICAL TRIAGE TODAY"
    override val specialistReferralsQueue: String = "Specialist Referrals Queue"
    override val triageIncomingConsults: String = "Triage incoming consults & closed-loop specialist evaluations"
    override val otDeskTab: String = "OT Desk"
    override val surgeriesAndPac: String = "Surgeries & PAC"
    override val ipdBedsTab: String = "IPD Beds"
    override val wardOccupancy: String = "Ward Occupancy"
    override val referralsTab: String = "Referrals"
    override val aiimsTieUp: String = "AIIMS / Tie-Up"
    override val noActiveSosAlerts: String = "No active SOS alerts."
    override val mentalHealthReferral: String = "Mental Health Referral"
    override val noAppointmentsScheduled: String = "No appointments scheduled."
    override val declineAction: String = "Decline"
    override val acceptCheckAction: String = "Accept ✓"
    override val roomOpenStatus: String = "● Room Open"
    override val rescheduleAction: String = "Reschedule"
    override val patientDidntJoinWindow: String = "Patient didn't join within window"
    override val adminRemindedBadge: String = "✓ Admin Reminded"
    override val remindAdminBtn: String = "🔔 Remind Admin"
    override val callActionBtn: String = "📹 Call"
    override val directiveLabel: String = "DIRECTIVE"
    override val liveVitalsStatusHalo: String = "LIVE VITALS STATUS HALO"
    override val transferToNextOnCall: String = "Transfer to Next On-Call"
    override val nowServingTokenCaps: String = "NOW SERVING TOKEN"
    override val walkInLabel: String = "Walk-In"
    override val activeConsultationLabel: String = "Active Consultation"
    override val orderedByCheckIn: String = "Ordered by Check-In"
    override val queueAllCaughtUp: String = "Queue is all caught up!"
    override val noPatientsWaitingNow: String = "No patients are currently waiting."
    override val selectWalkInPatient: String = "Select Walk-In Patient"
    override val selectArrowBtn: String = "Select →"
    override val pendingCasesTitle: String = "Pending Cases"
    override val dosageLabel: String = "Dosage"
    override val noReferralsInQueue: String = "No referrals in this queue view."
    override val specificClinicalQuestionAsk: String = "🎯 SPECIFIC CLINICAL QUESTION / ASK:"
    override val attachedRecordsLabel: String = "📎 Attached Records:"
    override val closedLoopFindingsRecorded: String = "CLOSED LOOP: SPECIALIST FINDINGS RECORDED"
    override val askInfoBtn: String = "❓ Ask Info"
    override val declineReferralBtn: String = "✕ Decline"
    override val callPatientConsultBtn: String = "📹 Call Patient (Consult)"
    override val sendFindingsBackBtn: String = "📝 Send Findings Back"
    override val provideDeclineRationale: String = "Please provide the clinical rationale for declining this referral handoff:"
    override val declineRationalePlaceholder: String = "e.g. Beyond department scope, bed capacity reached, refer to Oncology instead..."
    override val suggestedSpecialistDept: String = "Suggested Specialist / Department (Optional):"
    override val suggestedSpecialistPlaceholder: String = "e.g. Dr. Meera Nambiar / Psychiatry"
    override val declineAndNotifyBtn: String = "Decline & Notify"
    override val requestMoreInfoTitle: String = "Request More Information"
    override val specifyDetailsNeedBeforeAccepting: String = "Specify the clinical details or diagnostic tests you need before accepting:"
    override val requestInfoPlaceholder: String = "e.g. Please provide recent serum creatinine and 12-lead ECG strip..."
    override val sendRequestBtn: String = "Send Request"
    override val doctorToDoctorReferral: String = "Doctor-to-Doctor Referral"
    override val selectTargetSpecialty: String = "1. Select Target Medical Specialty *"
    override val routingTriageAssignment: String = "2. Routing & Triage Assignment"
    override val specialtyQueueOption: String = "🏢 Specialty Queue"
    override val namedSpecialistOption: String = "👨‍⚕️ Named Specialist"
    override val directPhysicianHandoff: String = "Direct specific physician handoff"
    override val noNamedSpecialistFallback: String = "No specific named specialist registered for this specialty. Will fallback to department queue."
    override val urgencyLevelRequired: String = "3. Urgency Level *"
    override val emergencyWarningQueueDelay: String = "Emergency Warning: Queue Delay Risk"
    override val referralQueueNotAcuteResponse: String = "A referral queue is an asynchronous clinical handoff, NOT an acute response mechanism. If this patient has unstable vitals or life-threatening symptoms, please launch an immediate Emergency SOS call in addition to this record."
    override val launchEmergencySosNow: String = "🚨 Launch Emergency Video/Voice SOS Now"
    override val clinicalReasonForReferral: String = "4. Clinical Reason for Referral *"
    override val describeClinicalFindingsPrompt: String = "Describe clinical findings, progression, and why specialist input is required..."
    override val specificClinicalQuestionHeading: String = "5. Specific Clinical Question / Ask *"
    override val clearlySpecifyQuestionInstruction: String = "Clearly specify what you need from the specialist (e.g. 'Confirm diagnosis of stage 2 HTN and advise titration')"
    override val clinicalQuestionPlaceholder: String = "e.g. Confirm diagnosis of X, evaluate for surgical intervention, or advise on drug titration..."
    override val sendReferralToSpecialist: String = "Send Referral to Specialist"
    override val configureClinicQueueSlots: String = "Configure Clinic & Queue Slots"
    override val manageCapacityWalkInRules: String = "Manage patient capacity and walk-in entry rules for today."
    override val acceptWalkInQueue: String = "Accept Walk-In Queue"
    override val allowDirectCheckinNoBooking: String = "Allow patients without prior booking to check-in directly."
    override val issueMedicalCertificateTitle: String = "Issue Medical Certificate"
    override val certifiedClinicalLeaveFitness: String = "Certified Clinical Leave & Fitness"
    override val certificateTypeLabel: String = "Certificate Type:"
    override val certificateSealedStampNotice: String = "Certificate will be cryptographically stamped with digital verification seal."
    override val patientHealthCardTitle: String = "🪪 Patient Health Card"
    override val viewOnlyAccessRule: String = "🔒 VIEW-ONLY ACCESS (§3 PRD Rule)"
    override val latestReportedCondition: String = "📋 Latest Reported Condition"
    override val medicalHistoryAndRecords: String = "📋 Medical History & Records"
    override val recordsHeading: String = "Records"
    override val noConditionRecordsLogged: String = "No condition records logged for this patient yet."
    override val noPriorPrescriptionsUploaded: String = "No prior prescriptions uploaded or issued."
    override val aiDigitizedBadge: String = "AI Digitized"
    override val outOfStockNearPatientWarning: String = "⚠️ Out of stock near patient · Clinical override noted"
    override val likelyAvailableNearPatient: String = "✅ Likely available near patient"
    override val addAnotherMedicineBtn: String = "+ Add Another Medicine"
    override val medicineNamePlaceholder: String = "e.g. Paracetamol 650mg or Amoxicillin"
    override val notFoundNearPatientLocation: String = "⚠️ Not found near patient's location"
    override val swapMedicineBtn: String = "Swap ✓"
    override val medicineSuggestionDisclaimer: String = "⚠️ Disclaimer: Suggestions are based on medicine category only — confirm clinical appropriateness before prescribing."
    override val quantityShort: String = "Qty"
    override val frequencyAndTiming: String = "Frequency & Timing"
    override val durationLabel: String = "Duration"
    override val addToPrescriptionBtn: String = "+ Add to Prescription"
    override val dietaryFollowUpInstructions: String = "Dietary & Follow-Up Instructions"
    override val instructionsPatientAsha: String = "Instructions for Patient & ASHA"
    override val selectProposedDate: String = "Select Proposed Date:"
    override val selectTimeSlotDialog: String = "Select Time Slot:"
    override val sendProposalBtn: String = "Send Proposal"
    override val startConsultBtn: String = "Start Consult"
    override val noShowBtn: String = "No-Show"
    override val referCaseToSpecialist: String = "🔄 Refer Case to Specialist"
    override val selectTargetSpecialtyColon: String = "Select Target Medical Specialty:"
    override val clinicalReferralNotesColon: String = "Clinical Referral Notes:"
    override val transferCaseArrow: String = "Transfer Case →"
    override val scheduleNewAppointmentTitle: String = "📅 Schedule New Appointment"
    override val proposeConsultationTime: String = "Propose consultation time to patient"
    override val selectPatientColon: String = "Select Patient:"
    override val selectDateColon: String = "Select Date:"
    override val availableTimeSlotColon: String = "Available Time Slot:"
    override val sendAppointmentProposalCheck: String = "Send Appointment Proposal ✓"
    override val specialistLoopClosure: String = "Specialist Loop Closure"
    override val referringAskClinicalQuestion: String = "Referring Ask / Clinical Question:"
    override val clinicalFindingsDiagnosticAssessment: String = "1. Clinical Findings & Diagnostic Assessment *"
    override val documentEvaluationFindingsPrompt: String = "Document your clinical evaluation, exam results, diagnostic conclusions..."
    override val ongoingCarePlanRecommendations: String = "2. Ongoing Care Plan & Recommendations *"
    override val adviseTreatmentAdjustmentsPrompt: String = "Advise treatment adjustments, medication doses, lifestyle advice, or monitoring frequency..."
    override val specialistFollowUpRequired: String = "Specialist Follow-Up Required"
    override val sendFindingsCloseLoop: String = "Send Findings & Close Loop"
    override val ultraLowBandwidthMode: String = "📡 Ultra-Low Bandwidth Mode (2G Audio Only)"
    override val connectedPhcTeleKiosk: String = "Connected from Sundarpura PHC Tele-Kiosk"
    override val pulseLabel: String = "❤️ Pulse"
    override val bpLabel: String = "🩸 BP"
    override val spo2VitalsLabel: String = "🫁 SpO2"
    override val tempLabel: String = "🌡️ Temp"
    override val tapToExpand: String = "Tap to expand"
    override val patientHealthVitals: String = "Patient Health Vitals"
    override val bpNormalSample: String = "• Blood Pressure: 118/78 mmHg (Normal)"
    override val heartRateSample: String = "• Heart Rate: 74 bpm (Stable)"
    override val bloodOxygenSample: String = "• Blood Oxygen: 98% SpO2 (Healthy)"
    override val temperatureSample: String = "• Temperature: 98.4°F"
    override val chronicConditionNone: String = "• Chronic Condition: None"
    override val lastVisitSample: String = "• Last Visit: 12 days ago (PHC OPD)"
    override val camOffLabel: String = "📷 Off"
    override val tapToEnableCam: String = "Tap to Enable Cam"
    override val switchToVoiceCallWeakSignal: String = "Switch to Voice Call (Save Bandwidth / Weak Signal)"
    override val doctorDidntJoinRebook: String = "Doctor didn't join · Rebook slot?"
    override val rebookCallBtn: String = "Rebook Call"
    override val waitingForDoctorToJoin: String = "Waiting for Doctor to Join…"
    override val statusNextInQueue: String = "STATUS: Next in Queue"
    override val doctorWrappingUpMsg: String = "The doctor is wrapping up their previous patient note and will join momentarily. Please do not close the app."
    override val enterConsultationRoom: String = "Enter Consultation Room →"
    override val cancelLeaveBtn: String = "Cancel / Leave"
    override val selectConsultationModeNetwork: String = "Select consultation mode based on your internet connection:"
    override val videoCallHd: String = "Video Call (HD)"
    override val requires4gWifi: String = "Requires 4G / Wi-Fi signal"
    override val voiceCallLowBandwidth: String = "Voice Call (Low Bandwidth)"
    override val recommended2gSignal: String = "Recommended for 2G / weak village signal"
    override val confirmBookingCheck: String = "Confirm Booking ✓"
    override val selectSeverityLevel: String = "Select Severity Level:"
    override val nearestDoctorsListView: String = "Nearest Doctors (List View)"
    override val distanceMocked: String = "Distance: 2.5 km (mocked)"
    override val findMedicineNearby: String = "📍 Find Medicine Nearby"
    override val notFoundNearbyAlternative: String = "Not found in stock nearby — Doctor's alternative suggested"
    override val likelyInStock: String = "🟢 Likely In-Stock"
    override val outOfStockTag: String = "🔴 Out"
    override val callPharmacyBtn: String = "📞 Call"
    override val docSuggestedAlternative: String = "💡 Doctor's Suggested Alternative Available"
    override val docSuggestedAlternativePlain: String = "💡 Doctor suggested alternative available"
    override val pharmacyStockNotice: String = "Notice: Pharmacy stock is estimated from chain data and deterministic modeling. Please call to confirm before traveling."
    override val helpManualTitle: String = "Help Manual"
    override val bloodGroupLabel: String = "Blood Group"
    override val oPositiveSample: String = "O+ Positive"
    override val allergiesLabel: String = "Allergies"
    override val noneReported: String = "None Reported"
    override val emergencyLabel: String = "Emergency"
    override val permanentOfflineQrIdentity: String = "Permanent Offline QR Identity"
    override val permanentQrOfflineRecord: String = "Permanent QR & Offline Record"
    override val symptomsSubmittedTriage: String = "Symptoms submitted to PHC Doctor triage queue!"
    override val aiScannedBadge: String = "AI Scanned"
    override val findNearbyLink: String = "📍 Find nearby"
    override val ruralHealthSchemesPmjay: String = "Rural Health Schemes (PM-JAY)"
    override val freeTreatment5Lakh: String = "Free treatment up to ₹5 Lakh & Maternal Subsidies"
    override val viewSchemesBtn: String = "View Schemes"
    override val uploadPrescriptionOcr: String = "Upload Prescription (OCR)"
    override val extractedTextLabel: String = "Extracted Text"
    override val noPrescriptionsFound: String = "No prescriptions found."
    override val prescribedMedicinesLabel: String = "Prescribed Medicines:"
    override val liveVisitQueue: String = "Live Visit Queue"
    override val noActiveQueueTicket: String = "No Active Queue Ticket"
    override val checkInScheduledDesc: String = "Check in to a scheduled appointment or join a doctor's walk-in queue to receive your token."
    override val getInstantTokenToday: String = "Get Instant Token for Today"
    override val yourTokenNumberCaps: String = "YOUR TOKEN NUMBER"
    override val confirmingPosition: String = "Confirming your position…"
    override val queuePositionLabel: String = "Queue Position"
    override val attendingPhysician: String = "Attending Physician"
    override val cancelTokenBtn: String = "Cancel Token"
    override val govtSchemesTitle: String = "Govt Schemes"
    override val governmentHealthSchemes: String = "Government Health Schemes"
    override val ruralWelfarePrograms: String = "Rural Welfare & Subsidy Programs"
    override val eligibleBadge: String = "ELIGIBLE"
    override val closeSchemesView: String = "Close Schemes View"
    override val digitalHealthCardUmid: String = "DIGITAL HEALTH CARD (UMID)"
    override val vitalSenseIdentity: String = "VitalSense / SehatSetu Identity"
    override val linkedBeneficiariesFamily: String = "Linked Beneficiaries (Family):"
    override val primarySelf: String = "👤 Primary (Self)"
    override val scanAtClinicDispensary: String = "SCAN AT PHC CLINIC / DISPENSARY"
    override val emergencyContactLabel: String = "Emergency Contact"
    override val assignedAshaLabel: String = "Assigned ASHA"
    override val activeClinicalConditionLabel: String = "Active Clinical Condition"
    override val linkAbhaBtn: String = "Link ABHA"
    override val offlineSqliteEncrypted: String = "Offline SQLite Encrypted"
    override val logHealthSymptomsTitle: String = "Log Health Symptoms"
    override val categoryCaps: String = "CATEGORY"
    override val selectCommonSymptoms: String = "SELECT COMMON SYMPTOMS"
    override val severityLevelCaps: String = "SEVERITY LEVEL"
    override val submitToDoctorTriage: String = "🚀 Submit to PHC Doctor Triage"
    override val careJourneyTitle: String = "Care Journey"
    override val spo2Label: String = "SpO2"
    override val backArrowBtn: String = "← Back"
    override val howAreYouFeelingToday: String = "How are you feeling today?"
    override val checkInSavedNotice: String = "✅ Check-in saved. A doctor or ASHA worker will check on you if needed."
    override val guidedBreathingTitle: String = "🌬️ Guided Breathing"
    override val breathe4SecondsMsg: String = "Breathe in for 4 seconds, hold for 4, exhale for 4."
    override val tapToStart: String = "Tap to Start"
    override val digitizePaperPrescription: String = "Digitize paper prescription via camera OCR or manual entry"
    override val addPrescribedMedicines: String = "Add Prescribed Medicines"
    override val addMedicineBtn: String = "+ Add Medicine"
    override val positionPrescriptionFrame: String = "📄 Position prescription inside the frame"
    override val googleAutoCropScanner: String = "✨ Google Auto-Crop & Clean Scanner"
    override val cantScanEnterManually: String = "✍️ Can't scan? Enter details manually"
    override val cameraPermissionNeeded: String = "Camera Permission Needed"
    override val cameraPermissionReason: String = "VitalSense uses your camera to capture prescription documents and extract medicines offline on your device."
    override val cameraAccessDeclinedMsg: String = "Camera access was declined. Please open device settings to enable camera permissions for VitalSense."
    override val openAppSettingsBtn: String = "⚙️ Open App Settings"
    override val allowCameraAccessBtn: String = "Allow Camera Access"
    override val enterDetailsManuallyBtn: String = "✍️ Enter Details Manually"
    override val aiPrescriptionDigitizer: String = "📷 AI Prescription Digitizer"
    override val zeroCloudOfflineInference: String = "⚡ Zero-Cloud Offline Inference"
    override val selectPrescriptionPhotoDesc: String = "Select a prescription photo to extract clinical entities locally on device without network latency."
    override val simulateCaptureScan: String = "Simulate Camera Capture / Rx Scan:"
    override val feverRxSample: String = "🌡️ Fever Rx"
    override val infectionSample: String = "💊 Infection"
    override val extractedClinicalEntities: String = "Extracted Clinical Entities:"
    override val rawOcrTextStream: String = "RAW OCR TEXT STREAM"
    override val clinicalInstructionsNotes: String = "Clinical Instructions & Notes"
    override val saveToMedicalRecord: String = "Save to Patient's Medical Record ✓"
    override val readingPrescriptionOnDevice: String = "🔍 Reading prescription on-device..."
    override val runningLocalMlKitOcr: String = "Running local ML Kit OCR without network"
    override val reviewConfirmOcrScan: String = "📋 Review & Confirm OCR Scan"
    override val extractedTextTapToEdit: String = "Extracted Text (Tap to Edit):"
    override val onDeviceOcrBadge: String = "ON-DEVICE OCR"
    override val noMedicineNamesMatchedFallback: String = "No standard medicine names matched automatically. The raw text above will be saved as a Digitized Prescription note."
    override val prescribingDoctorHealthPost: String = "Prescribing Doctor / Health Post:"
    override val instructionsDosageDirections: String = "Instructions / Dosage Directions:"
    override val retakePhotoBtn: String = "🔁 Retake Photo"
    override val couldntReadAnyText: String = "We couldn't read any text"
    override val photoQualityHint: String = "The photo might be too blurry, too dark, or taken at an angle. Please try again with better lighting and hold the camera steady."
    override val enterPrescriptionManually: String = "✍️ Enter Prescription Manually"
    override val reviewPrescriptionPhoto: String = "📸 Review Prescription Photo"
    override val ensureHandwritingReadable: String = "Make sure the doctor's writing and medicine names are clear and readable."
    override val useThisPhotoScanText: String = "✅ Use this photo (Scan Text)"

    // New Button EnglishAppStrings Additions

    // New Button EnglishAppStrings Additions
    override val saveRecord: String = "Save Record"
    override val restockItem: String = "Restock Item"
    override val broadcastDistrictDirective: String = "📢 Broadcast District-Wide Health Directive"
    override val manageDispensary: String = "Manage Dispensary"
    override val diagnosticsLabs: String = "Diagnostics & Labs"
    override val visitAction: String = "Visit"
    override val logVitalsAction: String = "Log Vitals"
    override val viewProfile: String = "View Profile"
    override val startTeleConsultCall: String = "📹 Start Tele-Consultation Call"
    override val scanExternalRxOcr: String = "📷 Scan External Rx (OCR)"
    override val saveConfiguration: String = "Save Configuration"
    override val digitallySignIssue: String = "Digitally Sign & Issue"
    override val closeHealthCard: String = "Close Health Card"
    override val closeMedicalHistory: String = "Close Medical History"
    override val closeEReport: String = "Close E-Report"
    override val issueOrder: String = "Issue Order"
    override val bookOpdTokenNow: String = "🎟️ Book OPD Token Now"
    override val submitToDoctorQueueCheck: String = "Submit to Doctor Queue ✓"
    override val viewCareJourneyTimeline: String = "View Full Care Journey (Timeline)"
    override val saveCheckIn: String = "Save Check-in"
    override val savePrescriptionRecord: String = "Save Prescription Record"
    override val saveDigitizedPrescription: String = "💾 Save Digitized Prescription"
    override val manualHelpOverview: String = "1. Health Card: View your details offline.\\n2. SOS: Send emergency alerts.\\n3. OCR: Scan physical prescriptions."
    override val clinicalAskPrefix: String = "Clinical Ask: "

    // Final Polish EnglishAppStrings Additions
    override val scanPhysicalCardZeroPwdDesc: String = "Scan the physical health card issued by your village ASHA worker. Zero passwords required."
    override val patientIdentityVerified: String = "Patient Identity Verified!"
    override val referredByDoctor: String = "Referred by Doctor"
    override val specialistFindingsDiagnosticAssessment: String = "Specialist Diagnostic Findings"
    override val specialistRecommendationsCarePlan: String = "Specialist Recommendations"
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


    // New Multilingual HindiAppStrings Additions
    override val loginEnterBtn: String = "प्रवेश करें →"
    override val smartHealthId: String = "स्मार्ट हेल्थ आईडी"
    override val secureVerifiedBadge: String = "सुरक्षित व सत्यापित"
    override val signInWithGoogle: String = "Google से साइन इन करें"
    override val instantDemoSignIn: String = "⚡ तुरंत डेमो साइन इन"
    override val scanAshaCardQr: String = "🪪 आशा कार्ड स्कैन करें (QR क्लेम)"
    override val doctorConsultationDesk: String = "डॉक्टर क्लिनिकल डेस्क"
    override val uniqueDoctorId: String = "विशिष्ट डॉक्टर आईडी"
    override val egDoctorId: String = "उदा. DOC-101"
    override val signInWithDoctorId: String = "डॉक्टर आईडी से साइन इन करें"
    override val ashaFieldWorkerDesk: String = "आशा कार्यकर्ता फील्ड डेस्क"
    override val egAshaId: String = "उदा. ASHA-401"
    override val pinPasscode: String = "पिन / पासकोड"
    override val signInWithAshaId: String = "आशा आईडी से साइन इन करें"
    override val officialGovEmail: String = "आधिकारिक सरकारी ईमेल"
    override val passcodeLabel: String = "पासकोड"
    override val scanningAshaQr: String = "आशा QR स्कैन हो रहा है..."
    override val villageAgeLabel: String = "गाँव:  · आयु:  ()"
    override val ashaWorkerLabel: String = "आशा कार्यकर्ता:"
    override val sehatSetuBrand: String = "सेहतसेतु"
    override val ambulance108: String = "108 एम्बुलेंस"
    override val adminEmailPlaceholder: String = "admin@vitalsense.gov.in"
    override val systemBroadcast: String = "सिस्टम प्रसारण"
    override val broadcastTitle: String = "शीर्षक"
    override val broadcastMessage: String = "संदेश"
    override val diagnosticsAvailability: String = "जाँच व प्रयोगशाला उपलब्धता"
    override val liveMachineLabStatus: String = "मशीन व लैब की लाइव स्थिति"
    override val monitorRealTimeStatus: String = "सभी नैदानिक मशीनों और प्रयोगशालाओं की वास्तविक परिचालन स्थिति देखें।"
    override val diseaseTrendsTitle: String = "बीमारी के रुझान"
    override val villageSelection: String = "गाँव चयन"
    override val outbreakTrendsCases: String = "प्रकोप रुझान (कुल मामले)"
    override val noTrendDataVillage: String = "इस गाँव के लिए कोई रुझान डेटा उपलब्ध नहीं है।"
    override val recordNewData: String = "नया डेटा दर्ज करें"
    override val diseaseLabel: String = "बीमारी"
    override val totalCasesLabel: String = "कुल मामले"
    override val dispensaryRestockTitle: String = "दवाखाना पुनःपूर्ति"
    override val manageInventory: String = "इन्वेंट्री प्रबंधन"
    override val lowStockTag: String = "कम स्टॉक"
    override val addQuantityLabel: String = "मात्रा जोड़ें"
    override val facilityQualityMetrics: String = "स्वास्थ्य केंद्र गुणवत्ता मानक"
    override val backAction: String = "पीछे जाएं"
    override val overallHealthSystemQuality: String = "समग्र स्वास्थ्य प्रणाली गुणवत्ता"
    override val doctorsFlaggedLowMeds: String = "डॉक्टरों ने कम दवाओं की सूचना दी है"
    override val restockAction: String = "पुनःपूर्ति करें"
    override val restockNowBtn: String = "📦 अभी स्टॉक भरें"
    override val dismissReminder: String = "✕ स्मरणपत्र हटाएं"
    override val pinnedOnMap: String = "नक्शे पर चिह्नित 📍"
    override val hospitalOpsCareDesk: String = "अस्पताल संचालन व सेवा डेस्क"
    override val hospitalOpsCareDesc: String = "आईपीडी वार्ड, ऑपरेशन थिएटर, विशेषज्ञ रेफरल और बायोमेडिकल उपकरण की लाइव जानकारी।"
    override val ipdWardsBeds: String = "आईपीडी वार्ड व बिस्तर"
    override val occupancyAdmission: String = "भर्ती व उपलब्धता"
    override val otSurgeryDesk: String = "ऑपरेशन थिएटर डेस्क"
    override val pacSurgeonRoster: String = "पीएसी व सर्जन ड्यूटी चार्ट"
    override val externalReferralsDesk: String = "बाहरी अस्पताल रेफरल"
    override val aiimsCashlessDesk: String = "एम्स व कैशलेस डेस्क"
    override val bioMedicalRegistry: String = "बायोमेडिकल उपकरण"
    override val oxygenEquipment: String = "ऑक्सीजन व मशीनरी"
    override val liveClinicQueueOversight: String = "लाइव क्लिनिक कतार निगरानी"
    override val monitorDoctorQueues: String = "डॉक्टर कतार, प्रतीक्षा समय और क्लिनिक भार देखें"
    override val monitorBtn: String = "निगरानी करें"
    override val monitorPhcInfrastructure: String = "पीएचसी/सीएचसी बुनियादी ढांचे व फीडबैक की निगरानी करें"
    override val viewBtn: String = "देखें"
    override val dispatchedStatus: String = "भेज दिया गया"
    override val dismissBtn: String = "✕ हटाएं"
    override val dispensaryLowStockAlerts: String = "दवाखाना कम स्टॉक अलर्ट"
    override val allStockAboveThresholds: String = "सभी दवाएं पर्याप्त मात्रा में उपलब्ध हैं।"
    override val broadcastNowBtn: String = "अभी प्रसारित करें"
    override val targetVillageAudience: String = "लक्षित गाँव / नागरिक"
    override val currentServingToken: String = "वर्तमान सेवा टोकन"
    override val waitingInLine: String = "प्रतीक्षारत मरीज़"
    override val noPatientsInQueueToday: String = "आज इस डॉक्टर की कतार में कोई मरीज़ नहीं है।"
    override val tapDoctorToInspect: String = "कतार देखने के लिए डॉक्टर पर टैप करें"
    override val nowServingLabel: String = "वर्तमान टोकन"
    override val inWaitingLabel: String = "प्रतीक्षारत"
    override val avgWaitLabel: String = "औसत प्रतीक्षा"
    override val reviewAccountsTitle: String = "खाते समीक्षा"
    override val doctorsCategory: String = "डॉक्टर"
    override val ashasCategory: String = "आशा कार्यकर्ता"
    override val villagesCategory: String = "गाँव"
    override val villageOutbreakHeatmap: String = "गाँव बीमारी प्रकोप हीटमैप"
    override val mapsLabel: String = "नक्शा"
    override val kmDragPan: String = "2 किमी ───┤ (आगे-पीछे स्क्रॉल करें)"
    override val interactiveMapsEnhance: String = "इंटरैक्टिव गूगल मैप्स और नई सुविधाएं"
    override val updateAction: String = "अपडेट करें"
    override val hospitalCareBme: String = "अस्पताल सेवा · बायोमेडिकल"
    override val maintenanceDue: String = "रखरखाव देय"
    override val bmeEngineering: String = "बायोमेडिकल इंजीनियरिंग"
    override val twentyFourSevenOnCall: String = "24x7 उपलब्ध"
    override val lastServiced: String = "अंतिम सर्विस"
    override val nextDueDate: String = "अगली सर्विस तिथि"
    override val updateStatusBtn: String = "स्थिति अपडेट करें"
    override val selectOperationalStatus: String = "परिचालन स्थिति चुनें:"
    override val saveStatusBtn: String = "स्थिति सहेजें"
    override val criticalShortages: String = "अति आवश्यक कमी"
    override val emergencyTransfusionProtocol: String = "आपातकालीन रक्ताधान प्रोटोकॉल"
    override val emergencyTransfusionDesc: String = "सर्वदाता: O नेगेटिव (O-) · सर्वग्राही: AB पॉजिटिव (AB+)। आपातकालीन मामलों में ज़िला अस्पताल में क्रॉस-मैचिंग प्राथमिकता पर की जाती है।"
    override val hospitalCareIpd: String = "अस्पताल सेवा · आईपीडी"
    override val totalCapacity: String = "कुल क्षमता"
    override val admittedPatients: String = "भर्ती मरीज़"
    override val availableVacant: String = "उपलब्ध खाली बिस्तर"
    override val clearDischargeBed: String = "डिस्चार्ज करें व बिस्तर खाली करें"
    override val confirmAdmission: String = "भर्ती की पुष्टि करें"
    override val abnormalFindings: String = "असामान्य रिपोर्ट परिणाम"
    override val noLabInvestigationsCategory: String = "इस श्रेणी में कोई प्रयोगशाला जांच नहीं है"
    override val viewFullEReport: String = "पूर्ण ई-रिपोर्ट देखें ➔"
    override val certifiedLabReport: String = "प्रमाणित प्रयोगशाला रिपोर्ट"
    override val investigationFindings: String = "जांच निष्कर्ष"
    override val pathologistClinicalNotes: String = "पैथोलॉजिस्ट क्लिनिकल नोट्स"
    override val orderDiagnosticLabTest: String = "नई लैब जांच लिखें"
    override val selectInvestigationPanel: String = "जांच पैनल चुनें:"
    override val hospitalDeptsLiveBoard: String = "अस्पताल विभाग लाइव बोर्ड"
    override val liveOpdQueueTitle: String = "लाइव ओपीडी कतार"
    override val yourTokenNumber: String = "आपका टोकन नंबर"
    override val departmentLabel: String = "विभाग"
    override val roomCabinLabel: String = "कक्ष / केबिन"
    override val estWaitTime: String = "अनुमानित प्रतीक्षा समय"
    override val noActiveOpdToken: String = "कोई सक्रिय ओपीडी टोकन नहीं"
    override val opdDigitalSlipDesc: String = "बिना लाइन में लगे पीएचसी / ज़िला अस्पताल डॉक्टरों से मिलने के लिए डिजिटल टोकन प्राप्त करें।"
    override val servingTokenPrefix: String = "वर्तमान सेवा:"
    override val surgicalCareOtModule: String = "शल्य चिकित्सा · ऑपरेशन थिएटर"
    override val leadSurgeonLabel: String = "मुख्य सर्जन: डॉ. आयुष्मान देव सिंह"
    override val surgeonSpecialtyLabel: String = "एमडीएस, मैक्सिलोफेशियल ट्रॉमा विशेषज्ञ"
    override val pacValidatedBadge: String = "पीएसी स्वीकृत"
    override val noSurgicalProceduresScheduled: String = "वर्तमान में ऑपरेशन थिएटर में कोई सर्जरी निर्धारित नहीं है।"
    override val timeSlotLabel: String = "समय स्लॉट"
    override val operatingSurgeon: String = "ऑपरेटिंग सर्जन"
    override val anesthetistLabel: String = "एनेस्थेटिस्ट (निश्चेतक)"
    override val pacClearedCheck: String = "प्री-एनेस्थीसिया चेकअप (PAC) पूर्ण"
    override val confirmOtSlotBtn: String = "ओटी स्लॉट की पुष्टि करें"
    override val hospitalDeskLabel: String = "अस्पताल डेस्क"
    override val hospitalNetworkExternal: String = "अस्पताल नेटवर्क · बाहरी रेफरल"
    override val superSpecialtyReferrals: String = "🏛️ सुपर-स्पेशियलिटी बाहरी रेफरल"
    override val empanelledHospitalsDesk: String = "सूचीबद्ध शीर्ष अस्पताल व कैशलेस मांग डेस्क"
    override val issueVoucherBtn: String = "+ वाउचर जारी करें"
    override val activeReferralPasses: String = "सक्रिय रेफरल पास"
    override val tieUpNetwork: String = "अनुबंधित अस्पताल नेटवर्क"
    override val networkHospitalsSample: String = "एम्स, सेंट्रल रेलवे, केजीएमयू"
    override val cashlessApprovedBadge: String = "✓ कैशलेस स्वीकृत"
    override val beneficiaryPatient: String = "लाभार्थी मरीज़"
    override val ambulanceRequisitioned: String = "🚑 एम्बुलेंस बुलाई गई"
    override val issueSuperSpecialtyVoucher: String = "सुपर-स्पेशियलिटी रेफरल वाउचर बनाएं"
    override val requisitionEmergencyAmbulance: String = "आपातकालीन एम्बुलेंस वाहन की मांग करें"
    override val issueSignVoucherBtn: String = "वाउचर जारी व हस्ताक्षरित करें"
    override val sehatSetuSplashTitle: String = "सेहत सेतु · SEHAT SETU"
    override val bridgingRuralHealthZeroNet: String = "ग्रामीण स्वास्थ्य सेतु · शून्य इंटरनेट पर भी कार्यरत"
    override val encryptedOfflineAbha: String = "एन्क्रिप्टेड ऑफलाइन डेटा · आभा (ABHA) सक्षम"
    override val todaysWorklist: String = "📅 आज की कार्यसूची"
    override val routineFollowUp: String = "नियमित फॉलो-अप"
    override val highRiskRegistry: String = "🚨 उच्च-जोखिम रजिस्टर"
    override val allPatientsHighRisk: String = "सभी मरीज़ उच्च-जोखिम रजिस्टर में दर्ज हैं।"
    override val markEmergencyClear: String = "आपातकाल समाप्त चिह्नित करें"
    override val dispatchEmergencySosDesc: String = "यह डॉक्टरों और आपातकालीन टीम को तत्काल उच्च-प्राथमिकता SOS अलर्ट भेजेगा।"
    override val confirmEmergencyResolved: String = "आपातकाल समाधान की पुष्टि करें"
    override val yesMarkClearDismiss: String = "हाँ, समाप्त करें व हटाएं"
    override val chatWithPatient: String = "मरीज़ से संदेश चैट"
    override val messagesPersistLocally: String = "संदेश डिवाइस पर सुरक्षित हैं"
    override val sendNoticeToCaseload: String = "गाँव वासियों को सूचना भेजें"
    override val dailyVillageRounds: String = "दैनिक ग्राम भ्रमण"
    override val logVisitBtn: String = "भ्रमण दर्ज करें"
    override val villageRoundsDoorToDoor: String = "गाँव भ्रमण व घर-घर स्वास्थ्य जांच"
    override val noVillageRoundsLogged: String = "अभी तक कोई भ्रमण दर्ज नहीं है। घर-घर जांच दर्ज करने के लिए '+ भ्रमण दर्ज करें' दबाएं।"
    override val maternalCategory: String = "🤰 मातृ स्वास्थ्य"
    override val childCategory: String = "👶 बाल स्वास्थ्य"
    override val vaccineCategory: String = "💉 टीकाकरण"
    override val immunizationTrackerTitle: String = "टीकाकरण ट्रैकर"
    override val maternalChildRecords: String = "मातृ एवं शिशु स्वास्थ्य रिकॉर्ड"
    override val noRecordsFound: String = "कोई रिकॉर्ड नहीं मिला।"
    override val vaccinationSchedule: String = "टीकाकरण समय सारणी"
    override val medicineRestockTracker: String = "दवा पुनःपूर्ति ट्रैकर"
    override val ashaFieldKitStock: String = "आशा किट दवा सूची व मांग"
    override val noMedicinesInKit: String = "किट में कोई दवा उपलब्ध नहीं है।"
    override val kitRefillNeededPhc: String = "पीएचसी दवाखाने से किट पुनःपूर्ति आवश्यक है"
    override val requestRefill50: String = "पुनःपूर्ति मांग (+50)"
    override val registerNewPatientTitle: String = "नया मरीज़ पंजीकृत करें"
    override val nameFieldLabel: String = "नाम"
    override val ageFieldLabel: String = "आयु"
    override val logVillageRoundVisitTitle: String = "गाँव भ्रमण जांच दर्ज करें"
    override val doorToDoorHealthRecord: String = "घर-घर स्वास्थ्य रिकॉर्ड"
    override val servicesProvidedVisit: String = "भ्रमण के दौरान दी गई सेवाएं"
    override val maternalAncService: String = "🤰 मातृ / प्रसव पूर्व जांच (ANC)"
    override val childHealthService: String = "👶 बाल स्वास्थ्य"
    override val immunizationService: String = "💉 टीकाकरण"
    override val medicineIfaService: String = "💊 दवा / आयरन फोलिक एसिड"
    override val saveVillageRoundVisit: String = "✓ भ्रमण रिकॉर्ड सहेजें"
    override val registerNewVillagerTitle: String = "गाँव के नए निवासी का पंजीकरण"
    override val genderLabel: String = "लिंग"
    override val assignedVillageLabel: String = "आवंटित गाँव"
    override val initialRiskLevelLabel: String = "प्रारंभिक जोखिम स्तर"
    override val registerVillagerCaseload: String = "✓ निवासी को सूची में पंजीकृत करें"
    override val broadcastVillageAdvisory: String = "गाँव स्वास्थ्य परामर्श प्रसारित करें"
    override val quickAdvisoryTemplates: String = "त्वरित परामर्श संदेश प्रारूप"
    override val broadcastTargetVillage: String = "प्रसारण हेतु लक्षित गाँव"
    override val broadcastToVillageDashboard: String = "📢 गाँव डैशबोर्ड पर प्रसारित करें"
    override val pendingAppointmentsTitle: String = "प्रतीक्षारत नियुक्तियां"
    override val submittedViaAshaHelper: String = "🤝 आशा कार्यकर्ता के माध्यम से भेजा गया"
    override val directPatientSubmission: String = "मरीज़ द्वारा स्वयं भेजा गया"
    override val historyAndRx: String = "📋 इतिहास व पर्चे"
    override val healthCardTab: String = "🪪 स्वास्थ्य कार्ड"
    override val mentalHealthCaseFlag: String = "मानसिक स्वास्थ्य परामर्श फ्लैग"
    override val mentalHealthApproachNotice: String = "मरीज़ ने तनाव/चिंता के लक्षण बताए हैं। सहानुभूति व समग्र दृष्टिकोण से परामर्श दें।"
    override val confidentialDoctorNotes: String = "🔒 गोपनीय क्लिनिकल नोट्स (केवल डॉक्टर के लिए)"
    override val clinicalActionsTitle: String = "क्लिनिकल कार्रवाइयां"
    override val ocrDigitizedBadge: String = "ओसीआर द्वारा डिजिटल"
    override val lowStockAlertBadge: String = "कम स्टॉक चेतावनी"
    override val clinicalTriageToday: String = "आज की प्राथमिकता जांच (ट्राइएज)"
    override val specialistReferralsQueue: String = "विशेषज्ञ रेफरल कतार"
    override val triageIncomingConsults: String = "आने वाले रेफरल और विशेषज्ञ रिपोर्ट की समीक्षा करें"
    override val otDeskTab: String = "ऑपरेशन थिएटर"
    override val surgeriesAndPac: String = "सर्जरी व प्री-एनेस्थीसिया"
    override val ipdBedsTab: String = "आईपीडी बिस्तर"
    override val wardOccupancy: String = "वार्ड में भर्ती"
    override val referralsTab: String = "रेफरल"
    override val aiimsTieUp: String = "एम्स / अनुबंधित अस्पताल"
    override val noActiveSosAlerts: String = "कोई सक्रिय SOS अलर्ट नहीं है।"
    override val mentalHealthReferral: String = "मानसिक स्वास्थ्य रेफरल"
    override val noAppointmentsScheduled: String = "कोई निर्धारित नियुक्ति नहीं है।"
    override val declineAction: String = "अस्वीकार करें"
    override val acceptCheckAction: String = "स्वीकार करें ✓"
    override val roomOpenStatus: String = "● परामर्श कक्ष खुला है"
    override val rescheduleAction: String = "समय बदलें"
    override val patientDidntJoinWindow: String = "मरीज़ निर्धारित समय में नहीं जुड़े"
    override val adminRemindedBadge: String = "✓ प्रशासन को सूचित किया"
    override val remindAdminBtn: String = "🔔 प्रशासन को याद दिलाएं"
    override val callActionBtn: String = "📹 कॉल करें"
    override val directiveLabel: String = "क्लिनिकल निर्देश"
    override val liveVitalsStatusHalo: String = "लाइव वाइटल स्थिति संकेतक"
    override val transferToNextOnCall: String = "ड्यूटी डॉक्टर को स्थानांतरित करें"
    override val nowServingTokenCaps: String = "वर्तमान सेवा टोकन"
    override val walkInLabel: String = "सीधे आए (वॉक-इन)"
    override val activeConsultationLabel: String = "सक्रिय परामर्श"
    override val orderedByCheckIn: String = "चेक-इन समय के अनुसार"
    override val queueAllCaughtUp: String = "कतार समाप्त हो चुकी है!"
    override val noPatientsWaitingNow: String = "वर्तमान में कोई मरीज़ प्रतीक्षारत नहीं है।"
    override val selectWalkInPatient: String = "वॉक-इन मरीज़ चुनें"
    override val selectArrowBtn: String = "चुनें →"
    override val pendingCasesTitle: String = "लंबित मामले"
    override val dosageLabel: String = "खुराक"
    override val noReferralsInQueue: String = "इस कतार में कोई रेफरल नहीं है।"
    override val specificClinicalQuestionAsk: String = "🎯 विशेषज्ञ से मुख्य प्रश्न / अपेक्षा:"
    override val attachedRecordsLabel: String = "📎 संलग्न स्वास्थ्य रिकॉर्ड:"
    override val closedLoopFindingsRecorded: String = "विशेषज्ञ निष्कर्ष दर्ज व क्लोज्ड लूप पूर्ण"
    override val askInfoBtn: String = "❓ जानकारी मांगें"
    override val declineReferralBtn: String = "✕ अस्वीकार करें"
    override val callPatientConsultBtn: String = "📹 मरीज़ को कॉल करें (परामर्श)"
    override val sendFindingsBackBtn: String = "📝 अपने निष्कर्ष वापस भेजें"
    override val provideDeclineRationale: String = "रेफरल अस्वीकार करने का चिकित्सकीय कारण बताएं:"
    override val declineRationalePlaceholder: String = "उदा. विभाग की सीमा से बाहर, बिस्तर अनुपलब्ध, कैंसर विभाग को भेजें..."
    override val suggestedSpecialistDept: String = "सुझाया गया विशेषज्ञ / विभाग (वैकल्पिक):"
    override val suggestedSpecialistPlaceholder: String = "उदा. डॉ. मीरा नंबियार / मनोरोग विभाग"
    override val declineAndNotifyBtn: String = "अस्वीकार करें व सूचित करें"
    override val requestMoreInfoTitle: String = "अतिरिक्त जानकारी मांगें"
    override val specifyDetailsNeedBeforeAccepting: String = "रेफरल स्वीकार करने से पहले आवश्यक जांच व विवरण बताएं:"
    override val requestInfoPlaceholder: String = "उदा. कृपया हालिया सीरम क्रिएटिनिन और 12-लीड ईसीजी स्ट्रिप उपलब्ध कराएं..."
    override val sendRequestBtn: String = "अनुरोध भेजें"
    override val doctorToDoctorReferral: String = "डॉक्टर-से-डॉक्टर रेफरल"
    override val selectTargetSpecialty: String = "1. विशेषज्ञता क्षेत्र चुनें *"
    override val routingTriageAssignment: String = "2. रेफरल दिशा व प्राथमिकता"
    override val specialtyQueueOption: String = "🏢 विशेषज्ञ विभाग कतार"
    override val namedSpecialistOption: String = "👨‍⚕️ विशिष्ट नामित डॉक्टर"
    override val directPhysicianHandoff: String = "सीधे किसी डॉक्टर को मामला सौंपें"
    override val noNamedSpecialistFallback: String = "इस विभाग में कोई विशिष्ट डॉक्टर पंजीकृत नहीं है। सामान्य विभाग कतार में भेजा जाएगा।"
    override val urgencyLevelRequired: String = "3. आपात स्थिति स्तर *"
    override val emergencyWarningQueueDelay: String = "आपातकालीन चेतावनी: कतार में देरी का जोखिम"
    override val referralQueueNotAcuteResponse: String = "रेफरल कतार सामान्य प्रक्रिया है, आपातकालीन चिकित्सा नहीं। यदि मरीज़ की हालत गंभीर है, तो कृपया तुरंत इमरजेंसी SOS कॉल शुरू करें।"
    override val launchEmergencySosNow: String = "🚨 तुरंत इमरजेंसी वीडियो/वॉयस SOS कॉल करें"
    override val clinicalReasonForReferral: String = "4. रेफर करने का क्लिनिकल कारण *"
    override val describeClinicalFindingsPrompt: String = "मरीज़ के लक्षण, रोग की स्थिति और विशेषज्ञ की आवश्यकता का कारण लिखें..."
    override val specificClinicalQuestionHeading: String = "5. विशेषज्ञ से मुख्य सलाह / प्रश्न *"
    override val clearlySpecifyQuestionInstruction: String = "स्पष्ट रूप से बताएं कि विशेषज्ञ से क्या मार्गदर्शन चाहिए (उदा. 'रक्तचाप की जांच व दवा की खुराक का निर्धारण')"
    override val clinicalQuestionPlaceholder: String = "उदा. रोग की पुष्टि, सर्जरी की आवश्यकता, या दवाओं की खुराक में बदलाव..."
    override val sendReferralToSpecialist: String = "विशेषज्ञ को रेफरल भेजें"
    override val configureClinicQueueSlots: String = "क्लिनिक व कतार स्लॉट प्रबंधित करें"
    override val manageCapacityWalkInRules: String = "आज के लिए मरीज़ क्षमता और वॉक-इन नियम निर्धारित करें।"
    override val acceptWalkInQueue: String = "सीधे आने वाले मरीज़ों को स्वीकार करें"
    override val allowDirectCheckinNoBooking: String = "बिना पूर्व बुकिंग वाले मरीज़ों को सीधे लाइन में जुड़ने की अनुमति दें।"
    override val issueMedicalCertificateTitle: String = "मेडिकल प्रमाणपत्र जारी करें"
    override val certifiedClinicalLeaveFitness: String = "प्रमाणित बीमारी अवकाश अथवा फिटनेस प्रमाणपत्र"
    override val certificateTypeLabel: String = "प्रमाणपत्र का प्रकार:"
    override val certificateSealedStampNotice: String = "प्रमाणपत्र पर डिजिटल सत्यापन सील व हस्ताक्षर अंकित होंगे।"
    override val patientHealthCardTitle: String = "🪪 मरीज़ स्वास्थ्य कार्ड"
    override val viewOnlyAccessRule: String = "🔒 केवल देखने की अनुमति (सुरक्षा नियम)"
    override val latestReportedCondition: String = "📋 हाल ही में दर्ज लक्षण व स्थिति"
    override val medicalHistoryAndRecords: String = "📋 पूर्व स्वास्थ्य इतिहास व रिकॉर्ड"
    override val recordsHeading: String = "स्वास्थ्य रिकॉर्ड"
    override val noConditionRecordsLogged: String = "इस मरीज़ के लिए अभी तक कोई लक्षण दर्ज नहीं है।"
    override val noPriorPrescriptionsUploaded: String = "पहले का कोई पर्चा उपलब्ध नहीं है।"
    override val aiDigitizedBadge: String = "एआई द्वारा डिजिटल"
    override val outOfStockNearPatientWarning: String = "⚠️ मरीज़ के पास दवा उपलब्ध नहीं है · डॉक्टर की सहमति दर्ज"
    override val likelyAvailableNearPatient: String = "✅ मरीज़ के पास उपलब्ध होने की संभावना"
    override val addAnotherMedicineBtn: String = "+ अन्य दवा जोड़ें"
    override val medicineNamePlaceholder: String = "उदा. पैरासिटामोल 650mg या एमोक्सिसिलिन"
    override val notFoundNearPatientLocation: String = "⚠️ मरीज़ के स्थान के पास उपलब्ध नहीं"
    override val swapMedicineBtn: String = "बदलें ✓"
    override val medicineSuggestionDisclaimer: String = "⚠️ अस्वीकरण: सुझाव केवल दवा की श्रेणी पर आधारित हैं — लिखने से पहले जांच करें।"
    override val quantityShort: String = "मात्रा"
    override val frequencyAndTiming: String = "खुराक और समय"
    override val durationLabel: String = "अवधि (दिन)"
    override val addToPrescriptionBtn: String = "+ पर्चे में जोड़ें"
    override val dietaryFollowUpInstructions: String = "खान-पान व फॉलो-अप निर्देश"
    override val instructionsPatientAsha: String = "मरीज़ और आशा कार्यकर्ता के लिए निर्देश"
    override val selectProposedDate: String = "प्रस्तावित तारीख चुनें:"
    override val selectTimeSlotDialog: String = "समय स्लॉट चुनें:"
    override val sendProposalBtn: String = "प्रस्ताव भेजें"
    override val startConsultBtn: String = "परामर्श शुरू करें"
    override val noShowBtn: String = "मरीज़ उपस्थित नहीं"
    override val referCaseToSpecialist: String = "🔄 विशेषज्ञ को रेफर करें"
    override val selectTargetSpecialtyColon: String = "विशेषज्ञता क्षेत्र चुनें:"
    override val clinicalReferralNotesColon: String = "रेफरल क्लिनिकल नोट्स:"
    override val transferCaseArrow: String = "केस स्थानांतरित करें →"
    override val scheduleNewAppointmentTitle: String = "📅 नई परामर्श भेंट निर्धारित करें"
    override val proposeConsultationTime: String = "मरीज़ को परामर्श का समय प्रस्तावित करें"
    override val selectPatientColon: String = "मरीज़ चुनें:"
    override val selectDateColon: String = "तारीख चुनें:"
    override val availableTimeSlotColon: String = "उपलब्ध समय स्लॉट:"
    override val sendAppointmentProposalCheck: String = "भेंट का प्रस्ताव भेजें ✓"
    override val specialistLoopClosure: String = "विशेषज्ञ जांच रिपोर्ट व लूप पूर्ण"
    override val referringAskClinicalQuestion: String = "रेफर करने वाले डॉक्टर का प्रश्न:"
    override val clinicalFindingsDiagnosticAssessment: String = "1. क्लिनिकल निष्कर्ष और रोग निदान *"
    override val documentEvaluationFindingsPrompt: String = "अपनी जांच रिपोर्ट, परीक्षण परिणाम और निदान निष्कर्ष दर्ज करें..."
    override val ongoingCarePlanRecommendations: String = "2. उपचार योजना व सलाह *"
    override val adviseTreatmentAdjustmentsPrompt: String = "दवा की खुराक, खान-पान की सलाह, या फॉलो-अप की आवृत्ति बताएं..."
    override val specialistFollowUpRequired: String = "विशेषज्ञ द्वारा पुनः फॉलो-अप आवश्यक है"
    override val sendFindingsCloseLoop: String = "रिपोर्ट भेजें व निष्कर्ष दर्ज करें"
    override val ultraLowBandwidthMode: String = "📡 कम इंटरनेट मोड (केवल 2G ऑडियो)"
    override val connectedPhcTeleKiosk: String = "सुंदरपुरा प्राथमिक स्वास्थ्य केंद्र कियोस्क से जुड़े"
    override val pulseLabel: String = "❤️ नाड़ी (Pulse)"
    override val bpLabel: String = "🩸 रक्तचाप (BP)"
    override val spo2VitalsLabel: String = "🫁 ऑक्सीजन (SpO2)"
    override val tempLabel: String = "🌡️ तापमान"
    override val tapToExpand: String = "विस्तार के लिए टैप करें"
    override val patientHealthVitals: String = "मरीज़ के वाइटल्स (शारीरिक संकेत)"
    override val bpNormalSample: String = "• रक्तचाप: 118/78 mmHg (सामान्य)"
    override val heartRateSample: String = "• हृदय गति: 74 bpm (स्थिर)"
    override val bloodOxygenSample: String = "• ऑक्सीजन: 98% SpO2 (स्वस्थ)"
    override val temperatureSample: String = "• तापमान: 98.4°F"
    override val chronicConditionNone: String = "• पुरानी बीमारी: कोई नहीं"
    override val lastVisitSample: String = "• पिछली जांच: 12 दिन पहले (पीएचसी ओपीडी)"
    override val camOffLabel: String = "📷 कैमरा बंद"
    override val tapToEnableCam: String = "कैमरा चालू करने के लिए टैप करें"
    override val switchToVoiceCallWeakSignal: String = "ऑडियो कॉल पर बदलें (कमजोर सिग्नल के लिए)"
    override val doctorDidntJoinRebook: String = "डॉक्टर नहीं जुड़े · नया समय चुनें?"
    override val rebookCallBtn: String = "पुनः बुक करें"
    override val waitingForDoctorToJoin: String = "डॉक्टर के जुड़ने की प्रतीक्षा है…"
    override val statusNextInQueue: String = "स्थिति: कतार में अगला नंबर आपका है"
    override val doctorWrappingUpMsg: String = "डॉक्टर पिछले मरीज़ की जांच पूरी कर रहे हैं और जल्द ही जुड़ेंगे। कृपया ऐप बंद न करें।"
    override val enterConsultationRoom: String = "परामर्श कक्ष में प्रवेश करें →"
    override val cancelLeaveBtn: String = "रद्द करें / बाहर आएं"
    override val selectConsultationModeNetwork: String = "अपने इंटरनेट कनेक्शन के आधार पर परामर्श मोड चुनें:"
    override val videoCallHd: String = "वीडियो कॉल (HD)"
    override val requires4gWifi: String = "4G या वाई-फाई आवश्यक"
    override val voiceCallLowBandwidth: String = "वॉयस कॉल (कम इंटरनेट)"
    override val recommended2gSignal: String = "2G या कमजोर सिग्नल के लिए उपयुक्त"
    override val confirmBookingCheck: String = "बुकिंग की पुष्टि करें ✓"
    override val selectSeverityLevel: String = "गंभीरता स्तर चुनें:"
    override val nearestDoctorsListView: String = "निकटतम डॉक्टर (सूची दृश्य)"
    override val distanceMocked: String = "दूरी: 2.5 किमी"
    override val findMedicineNearby: String = "📍 नज़दीकी दवा खोजें"
    override val notFoundNearbyAlternative: String = "आसपास उपलब्ध नहीं — डॉक्टर द्वारा वैकल्पिक दवा सुझाई गई"
    override val likelyInStock: String = "🟢 उपलब्ध होने की संभावना"
    override val outOfStockTag: String = "🔴 अनुपलब्ध"
    override val callPharmacyBtn: String = "📞 कॉल करें"
    override val docSuggestedAlternative: String = "💡 डॉक्टर द्वारा सुझाई गई वैकल्पिक दवा उपलब्ध"
    override val docSuggestedAlternativePlain: String = "💡 डॉक्टर द्वारा सुझाई गई वैकल्पिक दवा उपलब्ध"
    override val pharmacyStockNotice: String = "सूचना: मेडिकल स्टोर पर दवा की उपलब्धता अनुमानित है। कृपया जाने से पहले फोन करके पुष्टि करें।"
    override val helpManualTitle: String = "सहायता मार्गदर्शिका"
    override val bloodGroupLabel: String = "रक्त समूह (ब्लड ग्रुप)"
    override val oPositiveSample: String = "O+ पॉजिटिव"
    override val allergiesLabel: String = "एलर्जी"
    override val noneReported: String = "कोई नहीं"
    override val emergencyLabel: String = "आपातकालीन संपर्क"
    override val permanentOfflineQrIdentity: String = "स्थायी ऑफलाइन क्यूआर पहचान"
    override val permanentQrOfflineRecord: String = "स्थायी क्यूआर व ऑफलाइन रिकॉर्ड"
    override val symptomsSubmittedTriage: String = "लक्षण पीएचसी डॉक्टर जांच कतार में भेज दिए गए हैं!"
    override val aiScannedBadge: String = "एआई द्वारा स्कैन"
    override val findNearbyLink: String = "📍 नज़दीक खोजें"
    override val ruralHealthSchemesPmjay: String = "सरकारी स्वास्थ्य योजनाएं (आयुष्मान भारत)"
    override val freeTreatment5Lakh: String = "₹5 लाख तक मुफ्त इलाज व मातृत्व सहायता"
    override val viewSchemesBtn: String = "योजनाएं देखें"
    override val uploadPrescriptionOcr: String = "पर्चा अपलोड करें (OCR)"
    override val extractedTextLabel: String = "निकाला गया टेक्स्ट"
    override val noPrescriptionsFound: String = "कोई पर्चा नहीं मिला।"
    override val prescribedMedicinesLabel: String = "निर्धारित दवाएं:"
    override val liveVisitQueue: String = "लाइव क्लिनिक कतार"
    override val noActiveQueueTicket: String = "कोई सक्रिय टोकन नहीं"
    override val checkInScheduledDesc: String = "टोकन प्राप्त करने के लिए अपनी निर्धारित भेंट में चेक-इन करें या वॉक-इन कतार में शामिल हों।"
    override val getInstantTokenToday: String = "आज के लिए तुरंत टोकन लें"
    override val yourTokenNumberCaps: String = "आपका टोकन नंबर"
    override val confirmingPosition: String = "कतार में आपका स्थान जांचा जा रहा है…"
    override val queuePositionLabel: String = "कतार में स्थान"
    override val attendingPhysician: String = "परीक्षक डॉक्टर"
    override val cancelTokenBtn: String = "टोकन रद्द करें"
    override val govtSchemesTitle: String = "सरकारी योजनाएं"
    override val governmentHealthSchemes: String = "सरकारी स्वास्थ्य योजनाएं"
    override val ruralWelfarePrograms: String = "ग्रामीण कल्याण व सब्सिडी कार्यक्रम"
    override val eligibleBadge: String = "पात्र"
    override val closeSchemesView: String = "योजना विवरण बंद करें"
    override val digitalHealthCardUmid: String = "डिजिटल स्वास्थ्य कार्ड (UMID)"
    override val vitalSenseIdentity: String = "वाइटलसेंस / सेहतसेतु पहचान"
    override val linkedBeneficiariesFamily: String = "जुड़े हुए लाभार्थी (परिवार):"
    override val primarySelf: String = "👤 स्वयं (मुख्य)"
    override val scanAtClinicDispensary: String = "पीएचसी क्लिनिक या दवाखाने पर स्कैन कराएं"
    override val emergencyContactLabel: String = "आपातकालीन संपर्क"
    override val assignedAshaLabel: String = "आवंटित आशा कार्यकर्ता"
    override val activeClinicalConditionLabel: String = "वर्तमान स्वास्थ्य स्थिति"
    override val linkAbhaBtn: String = "आभा (ABHA) लिंक करें"
    override val offlineSqliteEncrypted: String = "ऑफलाइन सुरक्षित एन्क्रिप्टेड"
    override val logHealthSymptomsTitle: String = "स्वास्थ्य लक्षण दर्ज करें"
    override val categoryCaps: String = "श्रेणी"
    override val selectCommonSymptoms: String = "सामान्य लक्षण चुनें"
    override val severityLevelCaps: String = "गंभीरता स्तर"
    override val submitToDoctorTriage: String = "🚀 डॉक्टर जांच कतार में भेजें"
    override val careJourneyTitle: String = "स्वास्थ्य यात्रा विवरण"
    override val spo2Label: String = "ऑक्सीजन (SpO2)"
    override val backArrowBtn: String = "← पीछे जाएं"
    override val howAreYouFeelingToday: String = "आज आप कैसा महसूस कर रहे हैं?"
    override val checkInSavedNotice: String = "✅ जानकारी सहेजी गई। आवश्यकता होने पर डॉक्टर या आशा कार्यकर्ता आपसे संपर्क करेंगे।"
    override val guidedBreathingTitle: String = "🌬️ निर्देशित श्वास व्यायाम"
    override val breathe4SecondsMsg: String = "4 सेकंड सांस अंदर लें, 4 सेकंड रोकें, 4 सेकंड में छोड़ें।"
    override val tapToStart: String = "शुरू करने के लिए टैप करें"
    override val digitizePaperPrescription: String = "कैमरा से स्कैन करके या लिखकर पर्चा डिजिटल करें"
    override val addPrescribedMedicines: String = "निर्धारित दवाएं जोड़ें"
    override val addMedicineBtn: String = "+ दवा जोड़ें"
    override val positionPrescriptionFrame: String = "📄 पर्चे को फ्रेम के अंदर रखें"
    override val googleAutoCropScanner: String = "✨ ऑटो-क्रॉप व ऑटो-क्लीन स्कैनर"
    override val cantScanEnterManually: String = "✍️ स्कैन नहीं हो रहा? हाथ से दर्ज करें"
    override val cameraPermissionNeeded: String = "कैमरा अनुमति आवश्यक है"
    override val cameraPermissionReason: String = "दवाइयों के पर्चे को ऑफलाइन स्कैन करने के लिए वाइटलसेंस को कैमरा अनुमति की आवश्यकता है।"
    override val cameraAccessDeclinedMsg: String = "कैमरा अनुमति अस्वीकृत कर दी गई थी। कृपया फोन सेटिंग्स में जाकर अनुमति चालू करें।"
    override val openAppSettingsBtn: String = "⚙️ ऐप सेटिंग्स खोलें"
    override val allowCameraAccessBtn: String = "कैमरा अनुमति दें"
    override val enterDetailsManuallyBtn: String = "✍️ विवरण हाथ से लिखें"
    override val aiPrescriptionDigitizer: String = "📷 एआई पर्चा डिजिटाइज़र"
    override val zeroCloudOfflineInference: String = "⚡ इंटरनेट के बिना सुरक्षित ऑफलाइन जांच"
    override val selectPrescriptionPhotoDesc: String = "बिना इंटरनेट के अपने फोन पर ही पर्चे से दवाएं पहचानने के लिए फोटो चुनें।"
    override val simulateCaptureScan: String = "नमूना पर्चा स्कैन करें:"
    override val feverRxSample: String = "🌡️ बुखार का पर्चा"
    override val infectionSample: String = "💊 संक्रमण (इंफेक्शन)"
    override val extractedClinicalEntities: String = "पहचानी गई दवाएं व निर्देश:"
    override val rawOcrTextStream: String = "मूल स्कैन टेक्स्ट"
    override val clinicalInstructionsNotes: String = "डॉक्टर के निर्देश व खुराक"
    override val saveToMedicalRecord: String = "मरीज़ के मेडिकल रिकॉर्ड में सहेजें ✓"
    override val readingPrescriptionOnDevice: String = "🔍 फोन पर पर्चा पढ़ा जा रहा है..."
    override val runningLocalMlKitOcr: String = "इंटरनेट के बिना ऑफलाइन टेक्स्ट पहचान जारी है"
    override val reviewConfirmOcrScan: String = "📋 पर्चा स्कैन की समीक्षा व पुष्टि"
    override val extractedTextTapToEdit: String = "निकाला गया टेक्स्ट (बदलने के लिए टैप करें):"
    override val onDeviceOcrBadge: String = "डिवाइस पर OCR"
    override val noMedicineNamesMatchedFallback: String = "कोई मानक दवा नाम अपने आप नहीं मिला। उपरोक्त टेक्स्ट को डिजिटल पर्चे के रूप में सहेजा जाएगा।"
    override val prescribingDoctorHealthPost: String = "डॉक्टर का नाम / स्वास्थ्य केंद्र:"
    override val instructionsDosageDirections: String = "खुराक और सेवन के नियम:"
    override val retakePhotoBtn: String = "🔁 दोबारा फोटो लें"
    override val couldntReadAnyText: String = "हम पर्चा पढ़ नहीं सके"
    override val photoQualityHint: String = "फोटो धुंधली, अंधेरी या तिरछी हो सकती है। कृपया अच्छी रोशनी में कैमरा स्थिर रखकर दोबारा फोटो लें।"
    override val enterPrescriptionManually: String = "✍️ पर्चा हाथ से लिखें"
    override val reviewPrescriptionPhoto: String = "📸 पर्चे के फोटो की समीक्षा करें"
    override val ensureHandwritingReadable: String = "सुनिश्चित करें कि डॉक्टर की लिखावट और दवाओं के नाम स्पष्ट दिखाई दे रहे हैं।"
    override val useThisPhotoScanText: String = "✅ इस फोटो का उपयोग करें (टेक्स्ट स्कैन)"

    // New Button HindiAppStrings Additions

    // New Button HindiAppStrings Additions
    override val saveRecord: String = "रिकॉर्ड सहेजें"
    override val restockItem: String = "सामग्री स्टॉक करें"
    override val broadcastDistrictDirective: String = "📢 ज़िला-स्तरीय स्वास्थ्य निर्देश प्रसारित करें"
    override val manageDispensary: String = "दवाखाना प्रबंधित करें"
    override val diagnosticsLabs: String = "जांच व प्रयोगशालाएं"
    override val visitAction: String = "भ्रमण करें"
    override val logVitalsAction: String = "वाइटल्स दर्ज करें"
    override val viewProfile: String = "प्रोफ़ाइल देखें"
    override val startTeleConsultCall: String = "📹 टेली-परामर्श कॉल शुरू करें"
    override val scanExternalRxOcr: String = "📷 बाहरी पर्चा स्कैन करें (OCR)"
    override val saveConfiguration: String = "सेटिंग्स सहेजें"
    override val digitallySignIssue: String = "डिजिटल हस्ताक्षर करें व जारी करें"
    override val closeHealthCard: String = "स्वास्थ्य कार्ड बंद करें"
    override val closeMedicalHistory: String = "इतिहास विवरण बंद करें"
    override val closeEReport: String = "ई-रिपोर्ट बंद करें"
    override val issueOrder: String = "जांच आदेश जारी करें"
    override val bookOpdTokenNow: String = "🎟️ अभी ओपीडी टोकन बुक करें"
    override val submitToDoctorQueueCheck: String = "डॉक्टर कतार में भेजें ✓"
    override val viewCareJourneyTimeline: String = "स्वास्थ्य यात्रा टाइमलाइन देखें"
    override val saveCheckIn: String = "चेक-इन सहेजें"
    override val savePrescriptionRecord: String = "पर्चा रिकॉर्ड सहेजें"
    override val saveDigitizedPrescription: String = "💾 डिजिटल पर्चा सहेजें"
    override val manualHelpOverview: String = "1. स्वास्थ्य कार्ड: अपना विवरण ऑफलाइन देखें।\\n2. SOS: आपातकालीन अलर्ट भेजें।\\n3. OCR: पर्चा स्कैन करें।"
    override val clinicalAskPrefix: String = "विशेषज्ञ से मार्गदर्शन: "

    // Final Polish HindiAppStrings Additions
    override val scanPhysicalCardZeroPwdDesc: String = "आशा कार्यकर्ता द्वारा दिया गया स्वास्थ्य कार्ड स्कैन करें। किसी पासवर्ड की आवश्यकता नहीं है।"
    override val patientIdentityVerified: String = "मरीज़ पहचान सत्यापित!"
    override val referredByDoctor: String = "डॉक्टर द्वारा रेफर किया गया"
    override val specialistFindingsDiagnosticAssessment: String = "विशेषज्ञ नैदानिक निष्कर्ष"
    override val specialistRecommendationsCarePlan: String = "विशेषज्ञ उपचार सिफारिशें"
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


    // New Multilingual TamilAppStrings Additions
    override val loginEnterBtn: String = "உள்நுழைக →"
    override val smartHealthId: String = "ஸ்மார்ட் ஹெல்த் ஐடி"
    override val secureVerifiedBadge: String = "பாதுகாப்பான சரிபார்க்கப்பட்டது"
    override val signInWithGoogle: String = "Google உடன் உள்நுழைக"
    override val instantDemoSignIn: String = "⚡ உடனடி டெமோ உள்நுழைவு"
    override val scanAshaCardQr: String = "🪪 ஆஷா அட்டையை ஸ்கேன் செய் (QR)"
    override val doctorConsultationDesk: String = "மருத்துவர் ஆலோசனை மையம்"
    override val uniqueDoctorId: String = "தனித்துவமான மருத்துவர் ஐடி"
    override val egDoctorId: String = "எ.கா. DOC-101"
    override val signInWithDoctorId: String = "மருத்துவர் ஐடியுடன் உள்நுழைக"
    override val ashaFieldWorkerDesk: String = "ஆஷா களப் பணியாளர் மையம்"
    override val egAshaId: String = "எ.கா. ASHA-401"
    override val pinPasscode: String = "பின் / கடவுச்சொல்"
    override val signInWithAshaId: String = "ஆஷா ஐடியுடன் உள்நுழைக"
    override val officialGovEmail: String = "அதிகாரப்பூர்வ அரசு மின்னஞ்சல்"
    override val passcodeLabel: String = "கடவுக்குறியீடு"
    override val scanningAshaQr: String = "ஆஷா QR ஸ்கேன் செய்யப்படுகிறது..."
    override val villageAgeLabel: String = "கிராமம்:  · வயது:  ()"
    override val ashaWorkerLabel: String = "ஆஷா பணியாளர்:"
    override val sehatSetuBrand: String = "சேஹத்சேது"
    override val ambulance108: String = "108 ஆம்புலன்ஸ்"
    override val adminEmailPlaceholder: String = "admin@vitalsense.gov.in"
    override val systemBroadcast: String = "அமைப்பு ஒளிபரப்பு"
    override val broadcastTitle: String = "தலைப்பு"
    override val broadcastMessage: String = "செய்தி"
    override val diagnosticsAvailability: String = "பரிசோதனை வசதிகள் கிடைக்கும் நிலை"
    override val liveMachineLabStatus: String = "நேரடி இயந்திரம் & ஆய்வக நிலை"
    override val monitorRealTimeStatus: String = "அனைத்து சாதனங்கள் மற்றும் ஆய்வகங்களின் நேரடி நிலையை கண்காணிக்கவும்."
    override val diseaseTrendsTitle: String = "நோய் போக்குகள்"
    override val villageSelection: String = "கிராம தேர்வு"
    override val outbreakTrendsCases: String = "நோய் பரவல் போக்கு (மொத்த வழக்குகள்)"
    override val noTrendDataVillage: String = "இந்த கிராமத்திற்கு தரவு எதுவும் கிடைக்கவில்லை."
    override val recordNewData: String = "புதிய தரவை பதிவு செய்"
    override val diseaseLabel: String = "நோய்"
    override val totalCasesLabel: String = "மொத்த வழக்குகள்"
    override val dispensaryRestockTitle: String = "மருந்தக மறுதொகுப்பு"
    override val manageInventory: String = "சரக்கு மேலாண்மை"
    override val lowStockTag: String = "குறைந்த இருப்பு"
    override val addQuantityLabel: String = "அளவைச் சேர்"
    override val facilityQualityMetrics: String = "சுகாதார மைய தர அளவீடுகள்"
    override val backAction: String = "பின்செல்"
    override val overallHealthSystemQuality: String = "ஒட்டுமொத்த சுகாதார அமைப்பு தரம்"
    override val doctorsFlaggedLowMeds: String = "மருந்துகள் குறைந்துள்ளதாக மருத்துவர்கள் தெரிவித்துள்ளனர்"
    override val restockAction: String = "மறுதொகுப்பு செய்"
    override val restockNowBtn: String = "📦 இப்போது இருப்பு சேர்"
    override val dismissReminder: String = "✕ நினைவூட்டலை நிராகரி"
    override val pinnedOnMap: String = "வரைபடத்தில் குறிக்கப்பட்டது 📍"
    override val hospitalOpsCareDesk: String = "மருத்துவமனை செயல்பாடுகள் மற்றும் சேவை மையம்"
    override val hospitalOpsCareDesc: String = "உள்நோயாளி வார்டுகள், அறுவை சிகிச்சை அறைகள் மற்றும் உபகரணங்களின் நேரடி தகவல்."
    override val ipdWardsBeds: String = "உள்நோயாளி வார்டுகள் & படுக்கைகள்"
    override val occupancyAdmission: String = "படுக்கை இருப்பு & சேர்க்கை"
    override val otSurgeryDesk: String = "அறுவை சிகிச்சை மையம்"
    override val pacSurgeonRoster: String = "பிஏசி மற்றும் அறுவை சிகிச்சை நிபுணர்கள்"
    override val externalReferralsDesk: String = "வெளி மருத்துவமனை பரிந்துரைகள்"
    override val aiimsCashlessDesk: String = "எய்ம்ஸ் & பணமில்லா சேவை மையம்"
    override val bioMedicalRegistry: String = "உயிர் மருத்துவப் பதிவேடு"
    override val oxygenEquipment: String = "ஆக்ஸிஜன் மற்றும் உபகரணங்கள்"
    override val liveClinicQueueOversight: String = "நேரடி மருத்துவமனை வரிசை கண்காணிப்பு"
    override val monitorDoctorQueues: String = "மருத்துவர் வரிசை, காத்திருப்பு நேரம் மற்றும் நெரிசலைக் கண்காணிக்கவும்"
    override val monitorBtn: String = "கண்காணி"
    override val monitorPhcInfrastructure: String = "ஆரம்ப சுகாதார நிலைய கட்டமைப்பு மற்றும் கருத்துக்களைக் கண்காணிக்கவும்"
    override val viewBtn: String = "பார்"
    override val dispatchedStatus: String = "அனுப்பப்பட்டது"
    override val dismissBtn: String = "✕ நிராகரி"
    override val dispensaryLowStockAlerts: String = "மருந்தக குறைந்த இருப்பு எச்சரிக்கை"
    override val allStockAboveThresholds: String = "அனைத்து மருந்துகளும் போதுமான அளவில் உள்ளன."
    override val broadcastNowBtn: String = "இப்போது ஒளிபரப்பு செய்"
    override val targetVillageAudience: String = "இலக்கு கிராமம் / பொதுமக்கள்"
    override val currentServingToken: String = "தற்போது அழைக்கப்படும் டோக்கன்"
    override val waitingInLine: String = "வரிசையில் காத்திருப்போர்"
    override val noPatientsInQueueToday: String = "இன்று இந்த மருத்துவருக்கு வரிசையில் நோயாளிகள் இல்லை."
    override val tapDoctorToInspect: String = "வரிசையைப் பார்க்க மருத்துவரைத் தட்டவும்"
    override val nowServingLabel: String = "தற்போது அழைக்கப்படுபவர்"
    override val inWaitingLabel: String = "காத்திருப்போர்"
    override val avgWaitLabel: String = "சராசரி காத்திருப்பு"
    override val reviewAccountsTitle: String = "கணக்குகளை மதிப்பாய்வு செய்"
    override val doctorsCategory: String = "மருத்துவர்கள்"
    override val ashasCategory: String = "ஆஷா பணியாளர்கள்"
    override val villagesCategory: String = "கிராமங்கள்"
    override val villageOutbreakHeatmap: String = "கிராம நோய் பரவல் வரைபடம்"
    override val mapsLabel: String = "வரைபடங்கள்"
    override val kmDragPan: String = "2 கி.மீ ───┤ (நகர்த்த இழுக்கவும்)"
    override val interactiveMapsEnhance: String = "கூகிள் வரைபடம் மற்றும் புதிய வசதிகள்"
    override val updateAction: String = "புதுப்பி"
    override val hospitalCareBme: String = "மருத்துவமனை பராமரிப்பு · உயிரி மருத்துவம்"
    override val maintenanceDue: String = "பராமரிப்பு தேவை"
    override val bmeEngineering: String = "உயிரி மருத்துவப் பொறியியல்"
    override val twentyFourSevenOnCall: String = "24x7 தயார் நிலை"
    override val lastServiced: String = "கடைசியாக சர்வீஸ் செய்யப்பட்டது"
    override val nextDueDate: String = "அடுத்த பராமரிப்பு தேதி"
    override val updateStatusBtn: String = "நிலையைப் புதுப்பி"
    override val selectOperationalStatus: String = "செயல்பாட்டு நிலையைத் தேர்ந்தெடுக்கவும்:"
    override val saveStatusBtn: String = "நிலையைச் சேமி"
    override val criticalShortages: String = "தீவிர பற்றாக்குறை"
    override val emergencyTransfusionProtocol: String = "அவசர இரத்த மாற்று நெறிமுறை"
    override val emergencyTransfusionDesc: String = "பொது கொடையாளர்: O நெகட்டிவ் · பொது பெறுநர்: AB பாசிட்டிவ். அவசர காலங்களில் மாவட்ட மருத்துவமனையில் முன்னுரிமை வழங்கப்படும்."
    override val hospitalCareIpd: String = "மருத்துவமனை பராமரிப்பு · உள்நோயாளி"
    override val totalCapacity: String = "மொத்த கொள்ளளவு"
    override val admittedPatients: String = "அனுமதிக்கப்பட்ட நோயாளிகள்"
    override val availableVacant: String = "காலியாக உள்ள படுக்கைகள்"
    override val clearDischargeBed: String = "வெளியேற்றி படுக்கையை காலி செய்"
    override val confirmAdmission: String = "சேர்க்கையை உறுதிசெய்"
    override val abnormalFindings: String = "இயல்புக்கு மாறான முடிவுகள்"
    override val noLabInvestigationsCategory: String = "இந்த பிரிவில் சோதனைகள் எதுவும் இல்லை"
    override val viewFullEReport: String = "முழு அறிக்கையைப் பார் ➔"
    override val certifiedLabReport: String = "சான்றளிக்கப்பட்ட ஆய்வக அறிக்கை"
    override val investigationFindings: String = "பரிசோதனை முடிவுகள்"
    override val pathologistClinicalNotes: String = "நோயியல் நிபுணர் குறிப்புகள்"
    override val orderDiagnosticLabTest: String = "பரிசோதனைக்கு பரிந்துரை செய்"
    override val selectInvestigationPanel: String = "பரிசோதனை தொகுப்பைத் தேர்ந்தெடுக்கவும்:"
    override val hospitalDeptsLiveBoard: String = "மருத்துவமனை துறைகள் நேரடி பலகை"
    override val liveOpdQueueTitle: String = "நேரடி புறநோயாளி வரிசை"
    override val yourTokenNumber: String = "உங்கள் டோக்கன் எண்"
    override val departmentLabel: String = "துறை"
    override val roomCabinLabel: String = "அறை / கேபின்"
    override val estWaitTime: String = "எதிர்பார்க்கப்படும் நேரம்"
    override val noActiveOpdToken: String = "செயலில் உள்ள டோக்கன் இல்லை"
    override val opdDigitalSlipDesc: String = "வரிசையில் நிற்காமல் மருத்துவரை சந்திக்க டிஜிட்டல் டோக்கனைப் பெறுங்கள்."
    override val servingTokenPrefix: String = "அழைக்கப்படுபவர்:"
    override val surgicalCareOtModule: String = "அறுவை சிகிச்சை · ஓடி பிரிவு"
    override val leadSurgeonLabel: String = "தலைமை மருத்துவர்: டாக்டர் ஆயுஷ்மான் சிங்"
    override val surgeonSpecialtyLabel: String = "எம்.டி.எஸ், அறுவை சிகிச்சை நிபுணர்"
    override val pacValidatedBadge: String = "பிஏசி அங்கீகரிக்கப்பட்டது"
    override val noSurgicalProceduresScheduled: String = "தற்போது அறுவை சிகிச்சைகள் எதுவும் திட்டமிடப்படவில்லை."
    override val timeSlotLabel: String = "நேர இடைவெளி"
    override val operatingSurgeon: String = "அறுவை சிகிச்சை மருத்துவர்"
    override val anesthetistLabel: String = "மயக்க மருந்து நிபுணர்"
    override val pacClearedCheck: String = "மயக்க மருந்து பரிசோதனை (பிஏசி) முடிந்தது"
    override val confirmOtSlotBtn: String = "ஓடி நேரத்தை உறுதிசெய்"
    override val hospitalDeskLabel: String = "மருத்துவமனை மையம்"
    override val hospitalNetworkExternal: String = "மருத்துவமனை நெட்வொர்க் · வெளி பரிந்துரைகள்"
    override val superSpecialtyReferrals: String = "🏛️ சிறப்பு மருத்துவமனை வெளி பரிந்துரைகள்"
    override val empanelledHospitalsDesk: String = "பணமில்லா கோரிக்கை மையம் மற்றும் பெரிய மருத்துவமனைகள்"
    override val issueVoucherBtn: String = "+ வவுச்சர் வழங்கு"
    override val activeReferralPasses: String = "செயலில் உள்ள பரிந்துரை சீட்டுகள்"
    override val tieUpNetwork: String = "இணைப்பு நெட்வொர்க்"
    override val networkHospitalsSample: String = "எய்ம்ஸ், ரயில்வே, கேஜிஎம்யு"
    override val cashlessApprovedBadge: String = "✓ பணமில்லா சேவை அங்கீகரிக்கப்பட்டது"
    override val beneficiaryPatient: String = "பயனாளி நோயாளி"
    override val ambulanceRequisitioned: String = "🚑 ஆம்புலன்ஸ் கோரப்பட்டது"
    override val issueSuperSpecialtyVoucher: String = "சிறப்பு பரிந்துரை வவுச்சரை வழங்கு"
    override val requisitionEmergencyAmbulance: String = "அவசர ஆம்புலன்ஸ் வாகனத்தை கோரு"
    override val issueSignVoucherBtn: String = "வவுச்சரை வழங்கி கையொப்பமிடு"
    override val sehatSetuSplashTitle: String = "சேஹத் சேது · SEHAT SETU"
    override val bridgingRuralHealthZeroNet: String = "கிராமப்புற சுகாதார பாலம் · இணையம் இல்லாமலும் இயங்கும்"
    override val encryptedOfflineAbha: String = "பாதுகாப்பான உள்ளூர் தரவு · ஆபா (ABHA) தயார்"
    override val todaysWorklist: String = "📅 இன்றைய பணிப்பட்டியல்"
    override val routineFollowUp: String = "வழக்கமான பின்தொடர்தல்"
    override val highRiskRegistry: String = "🚨 அதிக ஆபத்து பதிவேடு"
    override val allPatientsHighRisk: String = "அனைத்து நோயாளிகளும் அதிக ஆபத்து பதிவேட்டில் உள்ளனர்."
    override val markEmergencyClear: String = "அவசரநிலையை முடிவுக்கு கொண்டுவா"
    override val dispatchEmergencySosDesc: String = "இது மருத்துவர்களுக்கு உடனடி அவசர எச்சரிக்கையை அனுப்பும்."
    override val confirmEmergencyResolved: String = "அவசரநிலை தீர்க்கப்பட்டதை உறுதிசெய்"
    override val yesMarkClearDismiss: String = "ஆம், முடிந்தது என பதிவு செய்"
    override val chatWithPatient: String = "நோயாளியுடன் உரையாடு"
    override val messagesPersistLocally: String = "செய்திகள் பாதுகாப்பாக சேமிக்கப்படுகின்றன"
    override val sendNoticeToCaseload: String = "பொதுமக்களுக்கு அறிவிப்பு அனுப்பு"
    override val dailyVillageRounds: String = "தினசரி கிராம களப்பணி"
    override val logVisitBtn: String = "பார்வையை பதிவு செய்"
    override val villageRoundsDoorToDoor: String = "கிராம களப்பணி & வீடு வீடான சந்திப்பு"
    override val noVillageRoundsLogged: String = "பதிவுகள் எதுவும் இல்லை. பதிவிட '+ பார்வையை பதிவு செய்' என்பதைத் தட்டவும்."
    override val maternalCategory: String = "🤰 தாய்மை நலம்"
    override val childCategory: String = "👶 குழந்தைகள் நலம்"
    override val vaccineCategory: String = "💉 தடுப்பூசி"
    override val immunizationTrackerTitle: String = "தடுப்பூசி கண்காணிப்பாளர்"
    override val maternalChildRecords: String = "தாய் மற்றும் சேய் பதிவேடுகள்"
    override val noRecordsFound: String = "பதிவுகள் எதுவும் கிடைக்கவில்லை."
    override val vaccinationSchedule: String = "தடுப்பூசி அட்டவணை"
    override val medicineRestockTracker: String = "மருந்து இருப்பு கண்காணிப்பாளர்"
    override val ashaFieldKitStock: String = "ஆஷா மருத்துவப் பெட்டி இருப்பு"
    override val noMedicinesInKit: String = "பெட்டியில் மருந்துகள் எதுவும் இல்லை."
    override val kitRefillNeededPhc: String = "ஆரம்ப சுகாதார நிலையத்திலிருந்து மருந்துகள் தேவை"
    override val requestRefill50: String = "மறுதொகுப்பு கோரு (+50)"
    override val registerNewPatientTitle: String = "புதிய நோயாளியைப் பதிவு செய்"
    override val nameFieldLabel: String = "பெயர்"
    override val ageFieldLabel: String = "வயது"
    override val logVillageRoundVisitTitle: String = "கிராம களப்பணி பதிவேடு"
    override val doorToDoorHealthRecord: String = "வீடு வீடான சுகாதாரப் பதிவு"
    override val servicesProvidedVisit: String = "வழங்கப்பட்ட சுகாதார சேவைகள்"
    override val maternalAncService: String = "🤰 தாய்மை நலம் / ஏஎன்சி"
    override val childHealthService: String = "👶 குழந்தைகள் நலம்"
    override val immunizationService: String = "💉 தடுப்பூசி"
    override val medicineIfaService: String = "💊 மருந்துகள் / இரும்புச்சத்து மாத்திரை"
    override val saveVillageRoundVisit: String = "✓ களப்பணிப் பதிவைச் சேமி"
    override val registerNewVillagerTitle: String = "புதிய கிராமவாசயைப் பதிவு செய்"
    override val genderLabel: String = "பாலினம்"
    override val assignedVillageLabel: String = "ஒதுக்கப்பட்ட கிராமம்"
    override val initialRiskLevelLabel: String = "ஆரம்ப ஆபத்து நிலை"
    override val registerVillagerCaseload: String = "✓ கிராமவாசியை பட்டியலில் பதிவு செய்"
    override val broadcastVillageAdvisory: String = "கிராம ஆலோசனை அறிவிப்பு"
    override val quickAdvisoryTemplates: String = "விரைவு அறிவிப்பு வார்ப்புருக்கள்"
    override val broadcastTargetVillage: String = "ஒளிபரப்பு இலக்கு கிராமம்"
    override val broadcastToVillageDashboard: String = "📢 கிராம பலகைக்கு ஒளிபரப்பு செய்"
    override val pendingAppointmentsTitle: String = "நிலுவையில் உள்ள சந்திப்புகள்"
    override val submittedViaAshaHelper: String = "🤝 ஆஷா உதவியாளர் மூலம் பெறப்பட்டது"
    override val directPatientSubmission: String = "நோயாளி நேரடியாக அனுப்பியது"
    override val historyAndRx: String = "📋 வரலாறு & மருந்துகள்"
    override val healthCardTab: String = "🪪 சுகாதார அட்டை"
    override val mentalHealthCaseFlag: String = "மனநல ஆலோசனை தேவை"
    override val mentalHealthApproachNotice: String = "நோயாளி மன அழுத்த அறிகுறிகளைப் பதிவு செய்துள்ளார். அக்கறையுடன் அணுகவும்."
    override val confidentialDoctorNotes: String = "🔒 ரகசிய மருத்துவக் குறிப்புகள் (மருத்துவர் மட்டும்)"
    override val clinicalActionsTitle: String = "மருத்துவ நடவடிக்கைகள்"
    override val ocrDigitizedBadge: String = "OCR மூலம் மாற்றப்பட்டது"
    override val lowStockAlertBadge: String = "குறைந்த இருப்பு எச்சரிக்கை"
    override val clinicalTriageToday: String = "இன்றைய நோயாளிகள் தரம் பிரித்தல்"
    override val specialistReferralsQueue: String = "சிறப்பு மருத்துவர் பரிந்துரை வரிசை"
    override val triageIncomingConsults: String = "பரிந்துரைகள் மற்றும் சிறப்பு மருத்துவர் அறிக்கைகளை மதிப்பாய்வு செய்"
    override val otDeskTab: String = "அறுவை சிகிச்சை பிரிவு"
    override val surgeriesAndPac: String = "அறுவை சிகிச்சைகள் & பிஏசி"
    override val ipdBedsTab: String = "உள்நோயாளி படுக்கைகள்"
    override val wardOccupancy: String = "வார்டு இருப்பு"
    override val referralsTab: String = "பரிந்துரைகள்"
    override val aiimsTieUp: String = "எய்ம்ஸ் / இணைப்பு"
    override val noActiveSosAlerts: String = "அவசர SOS எச்சரிக்கைகள் எதுவும் இல்லை."
    override val mentalHealthReferral: String = "மனநலப் பரிந்துரை"
    override val noAppointmentsScheduled: String = "திட்டமிடப்பட்ட சந்திப்புகள் எதுவும் இல்லை."
    override val declineAction: String = "நிராகரி"
    override val acceptCheckAction: String = "ஏற்றுக்கொள் ✓"
    override val roomOpenStatus: String = "● அறை திறக்கப்பட்டுள்ளது"
    override val rescheduleAction: String = "மறுநேரமிடு"
    override val patientDidntJoinWindow: String = "நோயாளி குறிப்பிட்ட நேரத்தில் இணையவில்லை"
    override val adminRemindedBadge: String = "✓ நிர்வாகத்திற்கு நினைவூட்டப்பட்டது"
    override val remindAdminBtn: String = "🔔 நிர்வாகத்திற்கு நினைவூட்டு"
    override val callActionBtn: String = "📹 அழை"
    override val directiveLabel: String = "மருத்துவ உத்தரவு"
    override val liveVitalsStatusHalo: String = "நேரடி உடல்நிலை காட்டி"
    override val transferToNextOnCall: String = "அடுத்த மருத்துவருக்கு மாற்று"
    override val nowServingTokenCaps: String = "தற்போது அழைக்கப்படும் டோக்கன்"
    override val walkInLabel: String = "நேரடி வருகை"
    override val activeConsultationLabel: String = "செயலில் உள்ள ஆலோசனை"
    override val orderedByCheckIn: String = "வருகை நேரத்தின்படி வரிசைப்படுத்தப்பட்டது"
    override val queueAllCaughtUp: String = "வரிசை முடிந்தது!"
    override val noPatientsWaitingNow: String = "தற்போது நோயாளிகள் யாரும் காத்திருக்கவில்லை."
    override val selectWalkInPatient: String = "நேரடி நோயாளியைத் தேர்ந்தெடு"
    override val selectArrowBtn: String = "தேர்ந்தெடு →"
    override val pendingCasesTitle: String = "நிலுவையில் உள்ள வழக்குகள்"
    override val dosageLabel: String = "மருந்தளவு"
    override val noReferralsInQueue: String = "இந்த வரிசையில் பரிந்துரைகள் இல்லை."
    override val specificClinicalQuestionAsk: String = "🎯 மருத்துவரிடம் கேட்கப்படும் கேள்வி:"
    override val attachedRecordsLabel: String = "📎 இணைக்கப்பட்ட பதிவுகள்:"
    override val closedLoopFindingsRecorded: String = "சிறப்பு மருத்துவரின் அறிக்கை பதிவு செய்யப்பட்டது"
    override val askInfoBtn: String = "❓ தகவல் கேள்"
    override val declineReferralBtn: String = "✕ நிராகரி"
    override val callPatientConsultBtn: String = "📹 நோயாளியை அழை (ஆலோசனை)"
    override val sendFindingsBackBtn: String = "📝 முடிவுகளை திருப்பி அனுப்பு"
    override val provideDeclineRationale: String = "பரிந்துரையை நிராகரிப்பதற்கான மருத்துவக் காரணத்தைக் குறிப்பிடவும்:"
    override val declineRationalePlaceholder: String = "எ.கா. துறை எல்லைக்கு அப்பாற்பட்டது, படுக்கை வசதி இல்லை..."
    override val suggestedSpecialistDept: String = "பரிந்துரைக்கப்படும் துறை / மருத்துவர் (விருப்பத்தேர்வு):"
    override val suggestedSpecialistPlaceholder: String = "எ.கா. டாக்டர் மீரா நம்பியார் / மனநலப் பிரிவு"
    override val declineAndNotifyBtn: String = "நிராகரித்து அறிவிக்கவும்"
    override val requestMoreInfoTitle: String = "கூடுதல் தகவலைக் கோரு"
    override val specifyDetailsNeedBeforeAccepting: String = "ஏற்றுக்கொள்வதற்கு முன் தேவைப்படும் விவரங்களைக் குறிப்பிடவும்:"
    override val requestInfoPlaceholder: String = "எ.கா. சமீபத்திய இரத்த பரிசோதனை அறிக்கை மற்றும் ஈசிஜி தேவை..."
    override val sendRequestBtn: String = "கோரிக்கையை அனுப்பு"
    override val doctorToDoctorReferral: String = "மருத்துவர் இடையேயான பரிந்துரை"
    override val selectTargetSpecialty: String = "1. மருத்துவத் துறையைத் தேர்ந்தெடுக்கவும் *"
    override val routingTriageAssignment: String = "2. வழிப்படுத்துதல் மற்றும் முன்னுரிமை"
    override val specialtyQueueOption: String = "🏢 துறை சார்ந்த பொது வரிசை"
    override val namedSpecialistOption: String = "👨‍⚕️ குறிப்பிட்ட மருத்துவர்"
    override val directPhysicianHandoff: String = "குறிப்பிட்ட மருத்துவருக்கு மாற்றுதல்"
    override val noNamedSpecialistFallback: String = "குறிப்பிட்ட மருத்துவர் இல்லை. பொது வரிசைக்கு அனுப்பப்படும்."
    override val urgencyLevelRequired: String = "3. அவசர நிலை *"
    override val emergencyWarningQueueDelay: String = "எச்சரிக்கை: வரிசை தாமத அபாயம்"
    override val referralQueueNotAcuteResponse: String = "இது உடனடி அவசர சிகிச்சை அல்ல. நோயாளிக்கு அவசர சிகிச்சை தேவைப்பட்டால் உடனே SOS அழைப்பைத் தொடங்கவும்."
    override val launchEmergencySosNow: String = "🚨 உடனே அவசர SOS அழைப்பைத் தொடங்கு"
    override val clinicalReasonForReferral: String = "4. பரிந்துரைக்கான மருத்துவக் காரணம் *"
    override val describeClinicalFindingsPrompt: String = "அறிகுறிகள் மற்றும் சிறப்பு மருத்துவர் தேவைப்படுவதற்கான காரணத்தை விவரிக்கவும்..."
    override val specificClinicalQuestionHeading: String = "5. மருத்துவரிடம் கேட்கப்படும் குறிப்பிட்ட கேள்வி *"
    override val clearlySpecifyQuestionInstruction: String = "மருத்துவரிடம் இருந்து உங்களுக்கு என்ன தேவை என்பதை தெளிவாகக் குறிப்பிடவும்"
    override val clinicalQuestionPlaceholder: String = "எ.கா. நோயை உறுதிப்படுத்துதல், அறுவை சிகிச்சை தேவையா என அறிதல்..."
    override val sendReferralToSpecialist: String = "சிறப்பு மருத்துவருக்கு பரிந்துரையை அனுப்பு"
    override val configureClinicQueueSlots: String = "மருத்துவமனை நேர இடைவெளிகளை அமை"
    override val manageCapacityWalkInRules: String = "இன்றைய நோயாளி எண்ணிக்கை மற்றும் விதிகளை நிர்வகிக்கவும்."
    override val acceptWalkInQueue: String = "நேரடி வருகை வரிசையை அனுமதி"
    override val allowDirectCheckinNoBooking: String = "முன்பதிவு இல்லாத நோயாளிகளை நேரடியாக அனுமதிக்கவும்."
    override val issueMedicalCertificateTitle: String = "மருத்துவச் சான்றிதழ் வழங்கு"
    override val certifiedClinicalLeaveFitness: String = "மருத்துவ விடுப்பு மற்றும் உடல் தகுதிச் சான்றிதழ்"
    override val certificateTypeLabel: String = "சான்றிதழ் வகை:"
    override val certificateSealedStampNotice: String = "சான்றிதழில் டிஜிட்டல் முத்திரை இடப்படும்."
    override val patientHealthCardTitle: String = "🪪 நோயாளி சுகாதார அட்டை"
    override val viewOnlyAccessRule: String = "🔒 பார்வைக்கு மட்டும் அனுமதி"
    override val latestReportedCondition: String = "📋 சமீபத்திய உடல்நிலை அறிக்கை"
    override val medicalHistoryAndRecords: String = "📋 மருத்துவ வரலாறு மற்றும் பதிவுகள்"
    override val recordsHeading: String = "பதிவுகள்"
    override val noConditionRecordsLogged: String = "இந்த நோயாளிக்கு பதிவுகள் எதுவும் இல்லை."
    override val noPriorPrescriptionsUploaded: String = "முந்தைய மருந்துச் சீட்டுகள் எதுவும் இல்லை."
    override val aiDigitizedBadge: String = "AI மூலம் மாற்றப்பட்டது"
    override val outOfStockNearPatientWarning: String = "⚠️ அருகில் மருந்து இல்லை · மருத்துவர் பரிந்துரைத்தது"
    override val likelyAvailableNearPatient: String = "✅ அருகில் கிடைக்க வாய்ப்புள்ளது"
    override val addAnotherMedicineBtn: String = "+ மற்றொரு மருந்தைச் சேர்"
    override val medicineNamePlaceholder: String = "எ.கா. பாராசிட்டமால் 650mg"
    override val notFoundNearPatientLocation: String = "⚠️ நோயாளியின் இருப்பிடத்திற்கு அருகில் இல்லை"
    override val swapMedicineBtn: String = "மாற்று ✓"
    override val medicineSuggestionDisclaimer: String = "⚠️ எச்சரிக்கை: பரிந்துரைகள் வகையை மட்டுமே அடிப்படையாகக் கொண்டவை."
    override val quantityShort: String = "அளவு"
    override val frequencyAndTiming: String = "அளவு மற்றும் நேரம்"
    override val durationLabel: String = "கால அளவு"
    override val addToPrescriptionBtn: String = "+ மருந்துச் சீட்டில் சேர்"
    override val dietaryFollowUpInstructions: String = "உணவு முறை மற்றும் ஆலோசனை"
    override val instructionsPatientAsha: String = "நோயாளி மற்றும் ஆஷாவுக்கான குறிப்புகள்"
    override val selectProposedDate: String = "தேதியைத் தேர்ந்தெடுக்கவும்:"
    override val selectTimeSlotDialog: String = "நேரத்தைத் தேர்ந்தெடுக்கவும்:"
    override val sendProposalBtn: String = "அனுப்பு"
    override val startConsultBtn: String = "தொடங்கு"
    override val noShowBtn: String = "வரவில்லை"
    override val referCaseToSpecialist: String = "🔄 சிறப்பு மருத்துவருக்கு மாற்று"
    override val selectTargetSpecialtyColon: String = "மருத்துவத் துறையைத் தேர்ந்தெடுக்கவும்:"
    override val clinicalReferralNotesColon: String = "பரிந்துரைக் குறிப்புகள்:"
    override val transferCaseArrow: String = "வழக்கை மாற்று →"
    override val scheduleNewAppointmentTitle: String = "📅 புதிய சந்திப்பை திட்டமிடு"
    override val proposeConsultationTime: String = "நோயாளிக்கு நேரத்தைப் பரிந்துரை செய்"
    override val selectPatientColon: String = "நோயாளியைத் தேர்ந்தெடு:"
    override val selectDateColon: String = "தேதியைத் தேர்ந்தெடு:"
    override val availableTimeSlotColon: String = "கிடைக்கும் நேரம்:"
    override val sendAppointmentProposalCheck: String = "திட்டத்தை அனுப்பு ✓"
    override val specialistLoopClosure: String = "சிறப்பு மருத்துவரின் முடிவு"
    override val referringAskClinicalQuestion: String = "கேட்கப்பட்ட மருத்துவக் கேள்வி:"
    override val clinicalFindingsDiagnosticAssessment: String = "1. மருத்துவ முடிவுகள் மற்றும் கண்டறிதல் *"
    override val documentEvaluationFindingsPrompt: String = "உங்கள் மருத்துவ மதிப்பீடு மற்றும் முடிவுகளைப் பதிவு செய்க..."
    override val ongoingCarePlanRecommendations: String = "2. சிகிச்சை முறை மற்றும் பரிந்துரைகள் *"
    override val adviseTreatmentAdjustmentsPrompt: String = "மருந்து அளவுகள் மற்றும் வாழ்க்கை முறை மாற்றங்களை பரிந்துரைக்கவும்..."
    override val specialistFollowUpRequired: String = "மீண்டும் ஆலோசனை தேவை"
    override val sendFindingsCloseLoop: String = "அறிக்கையை அனுப்பி முடிக்கவும்"
    override val ultraLowBandwidthMode: String = "📡 குறைந்த இணைய வசதி (2G ஆடியோ மட்டும்)"
    override val connectedPhcTeleKiosk: String = "சுந்தர்புரா ஆரம்ப சுகாதார நிலையத்திலிருந்து இணைக்கப்பட்டது"
    override val pulseLabel: String = "❤️ நாடித்துடிப்பு"
    override val bpLabel: String = "🩸 இரத்த அழுத்தம்"
    override val spo2VitalsLabel: String = "🫁 ஆக்ஸிஜன்"
    override val tempLabel: String = "🌡️ உடல் வெப்பநிலை"
    override val tapToExpand: String = "விரிவாக்க தட்டவும்"
    override val patientHealthVitals: String = "நோயாளி உடல்நிலை அளவீடுகள்"
    override val bpNormalSample: String = "• இரத்த அழுத்தம்: 118/78 mmHg (இயல்பு)"
    override val heartRateSample: String = "• இதயத் துடிப்பு: 74 bpm (சீரானது)"
    override val bloodOxygenSample: String = "• ஆக்ஸிஜன் அளவு: 98% SpO2 (ஆரோக்கியம்)"
    override val temperatureSample: String = "• வெப்பநிலை: 98.4°F"
    override val chronicConditionNone: String = "• நீண்டகால நோய்: எதுவுமில்லை"
    override val lastVisitSample: String = "• கடைசி வருகை: 12 நாட்களுக்கு முன்"
    override val camOffLabel: String = "📷 கேமரா ஆஃப்"
    override val tapToEnableCam: String = "கேமராவை இயக்க தட்டவும்"
    override val switchToVoiceCallWeakSignal: String = "குரல் அழைப்புக்கு மாறவும் (குறைந்த சமிக்ஞை)"
    override val doctorDidntJoinRebook: String = "மருத்துவர் இணையவில்லை · மறுபதிவு செய்யவா?"
    override val rebookCallBtn: String = "மறுபதிவு செய்"
    override val waitingForDoctorToJoin: String = "மருத்துவர் இணையும் வரை காத்திருக்கவும்…"
    override val statusNextInQueue: String = "நிலை: அடுத்த முறை உங்களுடையது"
    override val doctorWrappingUpMsg: String = "மருத்துவர் முந்தைய நோயாளியைப் பரிசீலித்து விரைவில் இணைவார். செயலியை மூட வேண்டாம்."
    override val enterConsultationRoom: String = "ஆலோசனை அறைக்குள் நுழைக →"
    override val cancelLeaveBtn: String = "ரத்துசெய் / வெளியேறு"
    override val selectConsultationModeNetwork: String = "உங்கள் இணைய இணைப்பின் அடிப்படையில் ஆலோசனை முறையைத் தேர்ந்தெடுக்கவும்:"
    override val videoCallHd: String = "வீடியோ அழைப்பு (HD)"
    override val requires4gWifi: String = "4G / Wi-Fi சிக்னல் தேவை"
    override val voiceCallLowBandwidth: String = "குரல் அழைப்பு (குறைந்த இணையம்)"
    override val recommended2gSignal: String = "2G / பலவீனமான சமிக்ஞைக்கு ஏற்றது"
    override val confirmBookingCheck: String = "பதிவை உறுதிசெய் ✓"
    override val selectSeverityLevel: String = "தீவிரத்தன்மையை தேர்ந்தெடுக்கவும்:"
    override val nearestDoctorsListView: String = "அருகிலுள்ள மருத்துவர்கள் (பட்டியல்)"
    override val distanceMocked: String = "தொலைவு: 2.5 கி.மீ"
    override val findMedicineNearby: String = "📍 அருகில் உள்ள மருந்துகளைக் கண்டுபிடி"
    override val notFoundNearbyAlternative: String = "அருகில் கிடைக்கவில்லை — மருத்துவர் பரிந்துரைத்த மாற்று மருந்து உள்ளது"
    override val likelyInStock: String = "🟢 இருப்பு இருக்க வாய்ப்புள்ளது"
    override val outOfStockTag: String = "🔴 இருப்பு இல்லை"
    override val callPharmacyBtn: String = "📞 அழை"
    override val docSuggestedAlternative: String = "💡 மருத்துவர் பரிந்துரைத்த மாற்று மருந்து உள்ளது"
    override val docSuggestedAlternativePlain: String = "💡 மருத்துவர் பரிந்துரைத்த மாற்று மருந்து உள்ளது"
    override val pharmacyStockNotice: String = "குறிப்பு: மருந்தின் இருப்பு உத்தேசமானது. புறப்படும் முன் தொலைபேசியில் உறுதிப்படுத்தவும்."
    override val helpManualTitle: String = "உதவி கையேடு"
    override val bloodGroupLabel: String = "இரத்த வகை"
    override val oPositiveSample: String = "O+ பாசிட்டிவ்"
    override val allergiesLabel: String = "ஒவ்வாமை"
    override val noneReported: String = "எதுவுமில்லை"
    override val emergencyLabel: String = "அவசரநிலை"
    override val permanentOfflineQrIdentity: String = "நிரந்தர உள்ளூர் QR அடையாளம்"
    override val permanentQrOfflineRecord: String = "நிரந்தர QR மற்றும் பதிவுகள்"
    override val symptomsSubmittedTriage: String = "அறிகுறிகள் மருத்துவருக்கு அனுப்பப்பட்டன!"
    override val aiScannedBadge: String = "AI மூலம் ஸ்கேன் செய்யப்பட்டது"
    override val findNearbyLink: String = "📍 அருகில் தேடு"
    override val ruralHealthSchemesPmjay: String = "அரசு சுகாதாரத் திட்டங்கள் (PM-JAY)"
    override val freeTreatment5Lakh: String = "₹5 லட்சம் வரை இலவச சிகிச்சை"
    override val viewSchemesBtn: String = "திட்டங்களைப் பார்"
    override val uploadPrescriptionOcr: String = "மருந்துச் சீட்டைப் பதிவேற்று (OCR)"
    override val extractedTextLabel: String = "பிரித்தெடுக்கப்பட்ட உரை"
    override val noPrescriptionsFound: String = "மருந்துச் சீட்டுகள் எதுவும் இல்லை."
    override val prescribedMedicinesLabel: String = "பரிந்துரைக்கப்பட்ட மருந்துகள்:"
    override val liveVisitQueue: String = "நேரடி மருத்துவ வரிசை"
    override val noActiveQueueTicket: String = "செயலில் உள்ள டோக்கன் இல்லை"
    override val checkInScheduledDesc: String = "டோக்கனைப் பெற உங்கள் முன்பதிவில் இணையவும்."
    override val getInstantTokenToday: String = "இன்றைய உடனடி டோக்கனைப் பெறு"
    override val yourTokenNumberCaps: String = "உங்கள் டோக்கன் எண்"
    override val confirmingPosition: String = "உங்கள் வரிசை எண் சரிபார்க்கப்படுகிறது…"
    override val queuePositionLabel: String = "வரிசை நிலை"
    override val attendingPhysician: String = "பரிசோதிக்கும் மருத்துவர்"
    override val cancelTokenBtn: String = "டோக்கனை ரத்துசெய்"
    override val govtSchemesTitle: String = "அரசுத் திட்டங்கள்"
    override val governmentHealthSchemes: String = "அரசு சுகாதார நலத் திட்டங்கள்"
    override val ruralWelfarePrograms: String = "கிராமப்புற நலத் திட்டங்கள்"
    override val eligibleBadge: String = "தகுதியுடையவர்"
    override val closeSchemesView: String = "மூடு"
    override val digitalHealthCardUmid: String = "டிஜிட்டல் சுகாதார அட்டை (UMID)"
    override val vitalSenseIdentity: String = "வைட்டல்சென்ஸ் / சேஹத்சேது அடையாளம்"
    override val linkedBeneficiariesFamily: String = "இணைக்கப்பட்ட குடும்ப உறுப்பினர்கள்:"
    override val primarySelf: String = "👤 முதன்மை (சுய)"
    override val scanAtClinicDispensary: String = "மருத்துவமனையில் ஸ்கேன் செய்யவும்"
    override val emergencyContactLabel: String = "அவசர தொடர்பு"
    override val assignedAshaLabel: String = "ஒதுக்கப்பட்ட ஆஷா"
    override val activeClinicalConditionLabel: String = "தற்போதைய உடல்நிலை"
    override val linkAbhaBtn: String = "ஆபா (ABHA) இணை"
    override val offlineSqliteEncrypted: String = "பாதுகாப்பான உள்ளூர் தரவு"
    override val logHealthSymptomsTitle: String = "அறிகுறிகளைப் பதிவு செய்"
    override val categoryCaps: String = "பிரிவு"
    override val selectCommonSymptoms: String = "பொதுவான அறிகுறிகளைத் தேர்ந்தெடுக்கவும்"
    override val severityLevelCaps: String = "தீவிர நிலை"
    override val submitToDoctorTriage: String = "🚀 மருத்துவருக்கு அனுப்பு"
    override val careJourneyTitle: String = "சுகாதாரப் பயணம்"
    override val spo2Label: String = "ஆக்ஸிஜன் (SpO2)"
    override val backArrowBtn: String = "← பின்செல்"
    override val howAreYouFeelingToday: String = "இன்று உங்கள் உடல்நிலை எப்படி உள்ளது?"
    override val checkInSavedNotice: String = "✅ பதிவு சேமிக்கப்பட்டது. தேவைப்பட்டால் மருத்துவர் உங்களைத் தொடர்புகொள்வார்."
    override val guidedBreathingTitle: String = "🌬️ மூச்சுப் பயிற்சி"
    override val breathe4SecondsMsg: String = "4 வினாடிகள் மூச்சை உள்ளிழுக்கவும், 4 வினாடிகள் வைத்திருக்கவும், 4 வினாடிகள் வெளிவிடவும்."
    override val tapToStart: String = "தொடங்க தட்டவும்"
    override val digitizePaperPrescription: String = "மருந்துச் சீட்டை கேமரா மூலம் டிஜிட்டல் மயமாக்குங்கள்"
    override val addPrescribedMedicines: String = "பரிந்துரைக்கப்பட்ட மருந்துகளைச் சேர்"
    override val addMedicineBtn: String = "+ மருந்தைச் சேர்"
    override val positionPrescriptionFrame: String = "📄 மருந்துச் சீட்டை கட்டத்திற்குள் வைக்கவும்"
    override val googleAutoCropScanner: String = "✨ தானியங்கி ஸ்கேனர்"
    override val cantScanEnterManually: String = "✍️ கைமுறையாக உள்ளிடவும்"
    override val cameraPermissionNeeded: String = "கேமரா அனுமதி தேவை"
    override val cameraPermissionReason: String = "மருந்துச் சீட்டை ஸ்கேன் செய்ய கேமரா அனுமதி தேவை."
    override val cameraAccessDeclinedMsg: String = "கேமரா அனுமதி மறுக்கப்பட்டது. அமைப்புகளில் சென்று அனுமதிக்கவும்."
    override val openAppSettingsBtn: String = "⚙️ அமைப்புகளைத் திற"
    override val allowCameraAccessBtn: String = "கேமராவுக்கு அனுமதி அளி"
    override val enterDetailsManuallyBtn: String = "✍️ கைமுறையாக உள்ளிடவும்"
    override val aiPrescriptionDigitizer: String = "📷 AI மருந்துச் சீட்டு ஸ்கேனர்"
    override val zeroCloudOfflineInference: String = "⚡ இணையம் இல்லாத பாதுகாப்பான ஸ்கேன்"
    override val selectPrescriptionPhotoDesc: String = "இணையம் இல்லாமல் சாதனத்திலேயே மருந்துகளைப் படிக்க புகைப்படத்தைத் தேர்ந்தெடுக்கவும்."
    override val simulateCaptureScan: String = "மாதிரி ஸ்கேன் செய்:"
    override val feverRxSample: String = "🌡️ காய்ச்சல் சீட்டு"
    override val infectionSample: String = "💊 தொற்று மருந்து"
    override val extractedClinicalEntities: String = "கண்டறியப்பட்ட மருந்துகள்:"
    override val rawOcrTextStream: String = "ஸ்கேன் செய்யப்பட்ட அசல் உரை"
    override val clinicalInstructionsNotes: String = "மருத்துவ அறிவுறுத்தல்கள்"
    override val saveToMedicalRecord: String = "மருத்துவப் பதிவேட்டில் சேமி ✓"
    override val readingPrescriptionOnDevice: String = "🔍 சாதனத்திலேயே படிக்கப்படுகிறது..."
    override val runningLocalMlKitOcr: String = "இணையம் இல்லாமல் எழுத்துக்கள் கண்டறியப்படுகின்றன"
    override val reviewConfirmOcrScan: String = "📋 ஸ்கேன் முடிவுகளைச் சரிபார்"
    override val extractedTextTapToEdit: String = "எடுக்கப்பட்ட உரை (திருத்த தட்டவும்):"
    override val onDeviceOcrBadge: String = "சாதன OCR"
    override val noMedicineNamesMatchedFallback: String = "மருந்து பெயர்கள் பொருந்தவில்லை. மேலே உள்ள உரை குறிப்புகளாக சேமிக்கப்படும்."
    override val prescribingDoctorHealthPost: String = "பரிந்துரைத்த மருத்துவர் / சுகாதார நிலையம்:"
    override val instructionsDosageDirections: String = "மருந்து உட்கொள்ளும் முறைகள்:"
    override val retakePhotoBtn: String = "🔁 மீண்டும் படம் எடு"
    override val couldntReadAnyText: String = "உரையைப் படிக்க முடியவில்லை"
    override val photoQualityHint: String = "படம் மங்கலாக இருக்கலாம். நல்ல வெளிச்சத்தில் மீண்டும் எடுக்கவும்."
    override val enterPrescriptionManually: String = "✍️ கைமுறையாக உள்ளிடவும்"
    override val reviewPrescriptionPhoto: String = "📸 புகைப்படத்தைச் சரிபார்க்கவும்"
    override val ensureHandwritingReadable: String = "மருத்துவரின் கையெழுத்து தெளிவாக இருப்பதை உறுதிசெய்க."
    override val useThisPhotoScanText: String = "✅ இந்தப் படத்தைப் பயன்படுத்து"

    // New Button TamilAppStrings Additions

    // New Button TamilAppStrings Additions
    override val saveRecord: String = "பதிவைச் சேமி"
    override val restockItem: String = "பொருளை மறுதொகுப்பு செய்"
    override val broadcastDistrictDirective: String = "📢 மாவட்ட சுகாதார உத்தரவை ஒளிபரப்பு செய்"
    override val manageDispensary: String = "மருந்தகத்தை நிர்வகி"
    override val diagnosticsLabs: String = "பரிசோதனைகள் மற்றும் ஆய்வகங்கள்"
    override val visitAction: String = "பார்வையிடு"
    override val logVitalsAction: String = "உடல்நிலையை பதிவு செய்"
    override val viewProfile: String = "சுயவிவரத்தைப் பார்"
    override val startTeleConsultCall: String = "📹 தொலைமருத்துவ அழைப்பைத் தொடங்கு"
    override val scanExternalRxOcr: String = "📷 மருந்துச் சீட்டை ஸ்கேன் செய்"
    override val saveConfiguration: String = "அமைப்புகளைச் சேமி"
    override val digitallySignIssue: String = "டிஜிட்டல் கையொப்பமிட்டு வழங்கு"
    override val closeHealthCard: String = "அட்டையை மூடு"
    override val closeMedicalHistory: String = "வரலாற்றை மூடு"
    override val closeEReport: String = "அறிக்கையை மூடு"
    override val issueOrder: String = "ஆணையை வெளியிடு"
    override val bookOpdTokenNow: String = "🎟️ இப்போது புறநோயாளி டோக்கன் எடு"
    override val submitToDoctorQueueCheck: String = "மருத்துவர் வரிசைக்கு அனுப்பு ✓"
    override val viewCareJourneyTimeline: String = "முழுப் பயணத்தையும் பார்"
    override val saveCheckIn: String = "பதிவைச் சேமி"
    override val savePrescriptionRecord: String = "மருந்துச் சீட்டைச் சேமி"
    override val saveDigitizedPrescription: String = "💾 மருந்துச் சீட்டைச் சேமி"
    override val manualHelpOverview: String = "1. சுகாதார அட்டை: விவரங்களை இணையமின்றி பார்க்கவும்.\\n2. SOS: அவசர எச்சரிக்கை அனுப்பவும்.\\n3. OCR: மருந்துச் சீட்டை ஸ்கேன் செய்யவும்."
    override val clinicalAskPrefix: String = "மருத்துவ ஆலோசனை: "

    // Final Polish TamilAppStrings Additions
    override val scanPhysicalCardZeroPwdDesc: String = "ஆஷா பணியாளர் வழங்கிய சுகாதார அட்டையை ஸ்கேன் செய்யவும். கடவுச்சொல் தேவையில்லை."
    override val patientIdentityVerified: String = "நோயாளி அடையாளம் சரிபார்க்கப்பட்டது!"
    override val referredByDoctor: String = "மருத்துவரால் பரிந்துரைக்கப்பட்டது"
    override val specialistFindingsDiagnosticAssessment: String = "சிறப்பு மருத்துவரின் பரிசோதனை முடிவுகள்"
    override val specialistRecommendationsCarePlan: String = "சிறப்பு மருத்துவரின் பரிந்துரைகள்"
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


    // New Multilingual MarathiAppStrings Additions
    override val loginEnterBtn: String = "प्रवेश करा →"
    override val smartHealthId: String = "स्मार्ट हेल्थ आयडी"
    override val secureVerifiedBadge: String = "सुरक्षित व पडताळलेले"
    override val signInWithGoogle: String = "Google सह साइन इन करा"
    override val instantDemoSignIn: String = "⚡ त्वरित डेमो साइन इन"
    override val scanAshaCardQr: String = "🪪 आशा कार्ड स्कॅन करा (QR क्लेम)"
    override val doctorConsultationDesk: String = "डॉक्टर सल्ला केंद्र"
    override val uniqueDoctorId: String = "विशिष्ट डॉक्टर आयडी"
    override val egDoctorId: String = "उदा. DOC-101"
    override val signInWithDoctorId: String = "डॉक्टर आयडीसह साइन इन करा"
    override val ashaFieldWorkerDesk: String = "आशा सेविका फील्ड डेस्क"
    override val egAshaId: String = "उदा. ASHA-401"
    override val pinPasscode: String = "पिन / पासकोड"
    override val signInWithAshaId: String = "आशा आयडीसह साइन इन करा"
    override val officialGovEmail: String = "अधिकृत शासकीय ईमेल"
    override val passcodeLabel: String = "पासकोड"
    override val scanningAshaQr: String = "आशा QR स्कॅन होत आहे..."
    override val villageAgeLabel: String = "गाव:  · वय:  ()"
    override val ashaWorkerLabel: String = "आशा सेविका:"
    override val sehatSetuBrand: String = "सेहतसेतू"
    override val ambulance108: String = "108 रुग्णवाहिका"
    override val adminEmailPlaceholder: String = "admin@vitalsense.gov.in"
    override val systemBroadcast: String = "प्रणाली प्रसारण"
    override val broadcastTitle: String = "शीर्षक"
    override val broadcastMessage: String = "संदेश"
    override val diagnosticsAvailability: String = "निदान चाचणी उपलब्धता"
    override val liveMachineLabStatus: String = "थेट मशीन व लॅब स्थिती"
    override val monitorRealTimeStatus: String = "सर्व निदान यंत्रे व प्रयोगशाळांची थेट कार्यस्थिती तपासा."
    override val diseaseTrendsTitle: String = "आजार कल व ट्रेंड्स"
    override val villageSelection: String = "गाव निवड"
    override val outbreakTrendsCases: String = "साथ रोग कल (एकूण रुग्ण)"
    override val noTrendDataVillage: String = "या गावासाठी कोणताही ट्रेंड डेटा उपलब्ध नाही."
    override val recordNewData: String = "नवीन डेटा नोंदवा"
    override val diseaseLabel: String = "आजार"
    override val totalCasesLabel: String = "एकूण केसेस"
    override val dispensaryRestockTitle: String = "औषधालय पुनर्भरणा"
    override val manageInventory: String = "इन्व्हेंटरी व्यवस्थापन"
    override val lowStockTag: String = "कमी साठा"
    override val addQuantityLabel: String = "प्रमाण जोडा"
    override val facilityQualityMetrics: String = "आरोग्य केंद्र गुणवत्ता मेट्रिक्स"
    override val backAction: String = "मागे जा"
    override val overallHealthSystemQuality: String = "एकूण आरोग्य प्रणाली गुणवत्ता"
    override val doctorsFlaggedLowMeds: String = "डॉक्टरांनी औषध साठा कमी असल्याचे कळवले आहे"
    override val restockAction: String = "पुनर्भरणा करा"
    override val restockNowBtn: String = "📦 आता स्टॉक भरा"
    override val dismissReminder: String = "✕ स्मरणपत्र काढा"
    override val pinnedOnMap: String = "नकाशावर पिन केले 📍"
    override val hospitalOpsCareDesk: String = "रुग्णालय ऑपरेशन्स व केअर डेस्क"
    override val hospitalOpsCareDesc: String = "थेट आयपीडी वॉर्ड, शस्त्रक्रिया कक्ष, विशेषज्ञ रेफरल्स आणि वैद्यकीय उपकरणे."
    override val ipdWardsBeds: String = "आयपीडी वॉर्ड व खाटा"
    override val occupancyAdmission: String = "दाखल रुग्ण व जागा"
    override val otSurgeryDesk: String = "शस्त्रक्रिया (OT) डेस्क"
    override val pacSurgeonRoster: String = "पीएसी व शल्यचिकित्सक यादी"
    override val externalReferralsDesk: String = "बाह्य रेफरल्स"
    override val aiimsCashlessDesk: String = "एम्स व कॅशलेस डेस्क"
    override val bioMedicalRegistry: String = "बायो-मेडिकल नोंदणी"
    override val oxygenEquipment: String = "ऑक्सिजन व उपकरणे"
    override val liveClinicQueueOversight: String = "थेट क्लिनिक रांग नियंत्रण"
    override val monitorDoctorQueues: String = "डॉक्टरांची रांग, प्रतीक्षा वेळ व गर्दी तपासा"
    override val monitorBtn: String = "निरीक्षण करा"
    override val monitorPhcInfrastructure: String = "पीएचसी/सीएचसी पायाभूत सुविधा व अभिप्राय तपासा"
    override val viewBtn: String = "पहा"
    override val dispatchedStatus: String = "पाठवले"
    override val dismissBtn: String = "✕ बंद करा"
    override val dispensaryLowStockAlerts: String = "औषधालय कमी साठा अलर्ट"
    override val allStockAboveThresholds: String = "सर्व औषध साठा पुरेशा प्रमाणात उपलब्ध आहे."
    override val broadcastNowBtn: String = "आता प्रसारित करा"
    override val targetVillageAudience: String = "लक्षित गाव / लोक"
    override val currentServingToken: String = "सध्या सुरू असलेले टोकन"
    override val waitingInLine: String = "रांगेत प्रतीक्षारत"
    override val noPatientsInQueueToday: String = "आज या डॉक्टरांच्या रांगेत कोणतेही रुग्ण नाहीत."
    override val tapDoctorToInspect: String = "रांग पाहण्यासाठी डॉक्टरांवर टॅप करा"
    override val nowServingLabel: String = "सध्या सुरू"
    override val inWaitingLabel: String = "प्रतीक्षेत"
    override val avgWaitLabel: String = "सरासरी वेळ"
    override val reviewAccountsTitle: String = "खाती पुनरावलोकन"
    override val doctorsCategory: String = "डॉक्टर्स"
    override val ashasCategory: String = "आशा सेविका"
    override val villagesCategory: String = "गावे"
    override val villageOutbreakHeatmap: String = "गाव आजार प्रादुर्भाव हीटमॅप"
    override val mapsLabel: String = "नकाशे"
    override val kmDragPan: String = "2 किमी ───┤ (हलवण्यासाठी ड्रॅग करा)"
    override val interactiveMapsEnhance: String = "परस्परसंवादी गुगल नकाशे व सुधारणा"
    override val updateAction: String = "अपडेट करा"
    override val hospitalCareBme: String = "रुग्णालय सेवा · बायोमेडिकल"
    override val maintenanceDue: String = "देखभाल बाकी"
    override val bmeEngineering: String = "बायोमेडिकल अभियांत्रिकी"
    override val twentyFourSevenOnCall: String = "24x7 सेवेत उपलब्ध"
    override val lastServiced: String = "शेवटची सर्व्हिस"
    override val nextDueDate: String = "पुढील सर्व्हिस तारीख"
    override val updateStatusBtn: String = "स्थिती अपडेट करा"
    override val selectOperationalStatus: String = "कार्यस्थिती निवडा:"
    override val saveStatusBtn: String = "स्थिती जतन करा"
    override val criticalShortages: String = "गंभीर टंचाई"
    override val emergencyTransfusionProtocol: String = "आपत्कालीन रक्त संक्रमण प्रोटोकॉल"
    override val emergencyTransfusionDesc: String = "सर्वयोग्य दाता: O निगेटिव्ह · सर्वयोग्य स्वीकारणारा: AB पॉझिटिव्ह. तातडीच्या प्रसंगी जिल्हा रुग्णालयात जलद क्रॉस-मॅचिंग केले जाते."
    override val hospitalCareIpd: String = "रुग्णालय सेवा · आयपीडी"
    override val totalCapacity: String = "एकूण क्षमता"
    override val admittedPatients: String = "दाखल रुग्ण"
    override val availableVacant: String = "उपलब्ध रिक्त खाटा"
    override val clearDischargeBed: String = "डिस्चार्ज करून खाट रिकामी करा"
    override val confirmAdmission: String = "प्रवेश निश्चित करा"
    override val abnormalFindings: String = "असामान्य निष्कर्ष"
    override val noLabInvestigationsCategory: String = "या श्रेणीत कोणतीही तपासणी उपलब्ध नाही"
    override val viewFullEReport: String = "संपूर्ण ई-रिपोर्ट पहा ➔"
    override val certifiedLabReport: String = "प्रमाणित प्रयोगशाळा अहवाल"
    override val investigationFindings: String = "तपासणी निष्कर्ष"
    override val pathologistClinicalNotes: String = "पॅथॉलॉजिस्ट क्लिनिकल टिप्पण्या"
    override val orderDiagnosticLabTest: String = "नवीन लॅब तपासणी नोंदवा"
    override val selectInvestigationPanel: String = "तपासणी पॅनेल निवडा:"
    override val hospitalDeptsLiveBoard: String = "रुग्णालय विभाग थेट फलक"
    override val liveOpdQueueTitle: String = "थेट ओपीडी रांग"
    override val yourTokenNumber: String = "तुमचा टोकन क्रमांक"
    override val departmentLabel: String = "विभाग"
    override val roomCabinLabel: String = "खोली / केबिन"
    override val estWaitTime: String = "अंदाजे वेळ"
    override val noActiveOpdToken: String = "सक्रिय ओपीडी टोकन नाही"
    override val opdDigitalSlipDesc: String = "रांगेत उभे न राहता डॉक्टरांना भेटण्यासाठी डिजिटल टोकन मिळवा."
    override val servingTokenPrefix: String = "सुरू असलेले:"
    override val surgicalCareOtModule: String = "शस्त्रक्रिया सेवा · ओटी विभाग"
    override val leadSurgeonLabel: String = "प्रमुख शल्यचिकित्सक: डॉ. आयुष्मान देव सिंह"
    override val surgeonSpecialtyLabel: String = "एमडीएस, ट्रॉमॅटॉलॉजी विशेषज्ञ"
    override val pacValidatedBadge: String = "पीएसी मंजूर"
    override val noSurgicalProceduresScheduled: String = "सध्या ओटीमध्ये कोणतीही शस्त्रक्रिया नियोजित नाही."
    override val timeSlotLabel: String = "वेळ स्लॉट"
    override val operatingSurgeon: String = "शस्त्रक्रिया डॉक्टर"
    override val anesthetistLabel: String = "भूलतज्ज्ञ (ॲनेस्थेटिस्ट)"
    override val pacClearedCheck: String = "भूलपूर्व तपासणी (पीएसी) पूर्ण"
    override val confirmOtSlotBtn: String = "ओटी स्लॉट निश्चित करा"
    override val hospitalDeskLabel: String = "रुग्णालय डेस्क"
    override val hospitalNetworkExternal: String = "रुग्णालय नेटवर्क · बाह्य रेफरल्स"
    override val superSpecialtyReferrals: String = "🏛️ सुपर-स्पेशालिटी बाह्य रेफरल्स"
    override val empanelledHospitalsDesk: String = "पॅनेलवरील प्रमुख रुग्णालये व कॅशलेस डेस्क"
    override val issueVoucherBtn: String = "+ व्हाउचर जारी करा"
    override val activeReferralPasses: String = "सक्रिय रेफरल पासेस"
    override val tieUpNetwork: String = "संलग्न रुग्णालय नेटवर्क"
    override val networkHospitalsSample: String = "एम्स, सेंट्रल रेल्वे, केजीएमयू"
    override val cashlessApprovedBadge: String = "✓ कॅशलेस मंजूर"
    override val beneficiaryPatient: String = "लाभार्थी रुग्ण"
    override val ambulanceRequisitioned: String = "🚑 रुग्णवाहिका मागवली"
    override val issueSuperSpecialtyVoucher: String = "सुपर-स्पेशालिटी रेफरल व्हाउचर काढा"
    override val requisitionEmergencyAmbulance: String = "आपत्कालीन रुग्णवाहिका मागणी करा"
    override val issueSignVoucherBtn: String = "व्हाउचर जारी व स्वाक्षरी करा"
    override val sehatSetuSplashTitle: String = "सेहत सेतू · SEHAT SETU"
    override val bridgingRuralHealthZeroNet: String = "ग्रामीण आरोग्य सेतू · इंटरनेट नसतानाही कार्यरत"
    override val encryptedOfflineAbha: String = "सुरक्षित ऑफलाइन डेटा · आभा (ABHA) सज्ज"
    override val todaysWorklist: String = "📅 आजची कार्यसूची"
    override val routineFollowUp: String = "नियमित फॉलो-अप"
    override val highRiskRegistry: String = "🚨 अति-धोका नोंदवही"
    override val allPatientsHighRisk: String = "सर्व रुग्ण अति-धोका नोंदवहीत समाविष्ट आहेत."
    override val markEmergencyClear: String = "आपत्कालीन स्थिती पूर्ण घोषित करा"
    override val dispatchEmergencySosDesc: String = "यामुळे डॉक्टरांना व आपत्कालीन पथकाला तातडीने SOS अलर्ट जाईल."
    override val confirmEmergencyResolved: String = "आपत्कालीन स्थिती निवारण निश्चित करा"
    override val yesMarkClearDismiss: String = "होय, समाप्त करा व बंद करा"
    override val chatWithPatient: String = "रुग्णाशी चॅट करा"
    override val messagesPersistLocally: String = "संदेश स्थानिकरित्या सुरक्षित आहेत"
    override val sendNoticeToCaseload: String = "गावकऱ्यांना सूचना पाठवा"
    override val dailyVillageRounds: String = "दैनिक गाव फेरी"
    override val logVisitBtn: String = "भेट नोंदवा"
    override val villageRoundsDoorToDoor: String = "गाव फेरी व घरोघरी आरोग्य तपासणी"
    override val noVillageRoundsLogged: String = "कोणतीही नोंद उपलब्ध नाही. घरोघरी तपासणी नोंदवण्यासाठी '+ भेट नोंदवा' टॅप करा."
    override val maternalCategory: String = "🤰 माता आरोग्य"
    override val childCategory: String = "👶 बाल आरोग्य"
    override val vaccineCategory: String = "💉 लसीकरण"
    override val immunizationTrackerTitle: String = "लसीकरण ट्रॅकर"
    override val maternalChildRecords: String = "माता व बाल आरोग्य नोंदी"
    override val noRecordsFound: String = "कोणतीही नोंद सापडली नाही."
    override val vaccinationSchedule: String = "लसीकरण वेळापत्रक"
    override val medicineRestockTracker: String = "औषध साठा ट्रॅकर"
    override val ashaFieldKitStock: String = "आशा किट औषध साठा व मागणी"
    override val noMedicinesInKit: String = "किटमध्ये कोणतीही औषधे नाहीत."
    override val kitRefillNeededPhc: String = "पीएचसी औषधालयातून किट भरणे आवश्यक"
    override val requestRefill50: String = "पुनर्भरणा मागणी (+50)"
    override val registerNewPatientTitle: String = "नवीन रुग्णाची नोंदणी करा"
    override val nameFieldLabel: String = "नाव"
    override val ageFieldLabel: String = "वय"
    override val logVillageRoundVisitTitle: String = "गाव भेट तपासणी नोंदवा"
    override val doorToDoorHealthRecord: String = "घरोघरी आरोग्य तपासणी नोंद"
    override val servicesProvidedVisit: String = "भेटीदरम्यान दिलेल्या सेवा"
    override val maternalAncService: String = "🤰 माता / प्रसूतीपूर्व तपासणी (ANC)"
    override val childHealthService: String = "👶 बाल आरोग्य"
    override val immunizationService: String = "💉 लसीकरण"
    override val medicineIfaService: String = "💊 औषध / आयर्न गोळ्या"
    override val saveVillageRoundVisit: String = "✓ भेट नोंद जतन करा"
    override val registerNewVillagerTitle: String = "गावातील नवीन रहिवाशाची नोंदणी"
    override val genderLabel: String = "लिंग"
    override val assignedVillageLabel: String = "नेमून दिलेले गाव"
    override val initialRiskLevelLabel: String = "प्रारंभिक धोका पातळी"
    override val registerVillagerCaseload: String = "✓ रहिवाशाची नोंदणी पूर्ण करा"
    override val broadcastVillageAdvisory: String = "गाव आरोग्य सल्ला प्रसारित करा"
    override val quickAdvisoryTemplates: String = "जलद सल्ला संदेश नमुने"
    override val broadcastTargetVillage: String = "प्रसारणासाठी लक्षित गाव"
    override val broadcastToVillageDashboard: String = "📢 गाव डॅशबोर्डवर प्रसारित करा"
    override val pendingAppointmentsTitle: String = "प्रलंबित भेटी"
    override val submittedViaAshaHelper: String = "🤝 आशा सेविकेमार्फत पाठवले"
    override val directPatientSubmission: String = "रुग्णाने स्वतः पाठवले"
    override val historyAndRx: String = "📋 इतिहास व औषधपत्रिका"
    override val healthCardTab: String = "🪪 आरोग्य कार्ड"
    override val mentalHealthCaseFlag: String = "मानसिक आरोग्य सल्ला फ्लॅग"
    override val mentalHealthApproachNotice: String = "रुग्णाने मानसिक ताण/चिंतेची लक्षणे नोंदवली आहेत. सहानुभूतीने सल्ला द्या."
    override val confidentialDoctorNotes: String = "🔒 गोपनीय क्लिनिकल नोट्स (केवळ डॉक्टरांसाठी)"
    override val clinicalActionsTitle: String = "वैद्यकीय कृती"
    override val ocrDigitizedBadge: String = "ओसीआर द्वारे डिजिटल"
    override val lowStockAlertBadge: String = "कमी साठा इशारा"
    override val clinicalTriageToday: String = "आजची रुग्ण तपासणी व प्राधान्यक्रम"
    override val specialistReferralsQueue: String = "तज्ज्ञ डॉक्टर रेफरल रांग"
    override val triageIncomingConsults: String = "आलेले रेफरल्स व तज्ज्ञ अहवालांचे परीक्षण करा"
    override val otDeskTab: String = "शस्त्रक्रिया कक्ष (OT)"
    override val surgeriesAndPac: String = "शस्त्रक्रिया व पीएसी"
    override val ipdBedsTab: String = "आयपीडी खाटा"
    override val wardOccupancy: String = "वॉर्डमधील रुग्णसंख्या"
    override val referralsTab: String = "रेफरल्स"
    override val aiimsTieUp: String = "एम्स / संलग्न रुग्णालय"
    override val noActiveSosAlerts: String = "कोणताही सक्रिय SOS इशारा नाही."
    override val mentalHealthReferral: String = "मानसिक आरोग्य रेफरल"
    override val noAppointmentsScheduled: String = "कोणतीही नियोजित भेट नाही."
    override val declineAction: String = "नाकारा"
    override val acceptCheckAction: String = "स्वीकारा ✓"
    override val roomOpenStatus: String = "● सल्ला कक्ष सुरू आहे"
    override val rescheduleAction: String = "वेळ बदला"
    override val patientDidntJoinWindow: String = "रुग्ण वेळेत उपस्थित राहिले नाहीत"
    override val adminRemindedBadge: String = "✓ प्रशासनास स्मरण दिले"
    override val remindAdminBtn: String = "🔔 प्रशासनास आठवण करा"
    override val callActionBtn: String = "📹 कॉल करा"
    override val directiveLabel: String = "वैद्यकीय आदेश"
    override val liveVitalsStatusHalo: String = "थेट आरोग्य स्थिती दर्शक"
    override val transferToNextOnCall: String = "पुढील ऑन-कॉल डॉक्टरांकडे वर्ग करा"
    override val nowServingTokenCaps: String = "सध्या सुरू असलेले टोकन"
    override val walkInLabel: String = "थेट भेट (वॉक-इन)"
    override val activeConsultationLabel: String = "सध्या सुरू असलेला सल्ला"
    override val orderedByCheckIn: String = "चेक-इन वेळेनुसार क्रम"
    override val queueAllCaughtUp: String = "रांगेतील सर्व रुग्ण तपासले गेले आहेत!"
    override val noPatientsWaitingNow: String = "सध्या कोणताही रुग्ण प्रतीक्षेत नाही."
    override val selectWalkInPatient: String = "वॉक-इन रुग्ण निवडा"
    override val selectArrowBtn: String = "निवडा →"
    override val pendingCasesTitle: String = "प्रलंबित प्रकरणे"
    override val dosageLabel: String = "डोस"
    override val noReferralsInQueue: String = "या रांगेत कोणतेही रेफरल्स नाहीत."
    override val specificClinicalQuestionAsk: String = "🎯 तज्ज्ञ डॉक्टरांसाठी मुख्य प्रश्न:"
    override val attachedRecordsLabel: String = "📎 जोडलेले आरोग्य रेकॉर्ड्स:"
    override val closedLoopFindingsRecorded: String = "तज्ज्ञ डॉक्टरांचा अहवाल नोंदवला गेला आहे"
    override val askInfoBtn: String = "❓ माहिती विचारा"
    override val declineReferralBtn: String = "✕ नाकारा"
    override val callPatientConsultBtn: String = "📹 रुग्णास कॉल करा (सल्ला)"
    override val sendFindingsBackBtn: String = "📝 आपले निष्कर्ष परत पाठवा"
    override val provideDeclineRationale: String = "रेफरल नाकारण्याचे वैद्यकीय कारण नमूद करा:"
    override val declineRationalePlaceholder: String = "उदा. विभागाच्या कार्यकक्षेत नाही, खाटा शिल्लक नाहीत..."
    override val suggestedSpecialistDept: String = "सुचवलेला विभाग / डॉक्टर (पर्यायी):"
    override val suggestedSpecialistPlaceholder: String = "उदा. डॉ. मीरा नंबियार / मानसोपचार विभाग"
    override val declineAndNotifyBtn: String = "नाकारा व कळवा"
    override val requestMoreInfoTitle: String = "अधिक माहितीची मागणी करा"
    override val specifyDetailsNeedBeforeAccepting: String = "स्वीकारण्यापूर्वी आवश्यक असलेल्या चाचण्या व तपशील सांगा:"
    override val requestInfoPlaceholder: String = "उदा. कृपया अलीकडील सिरम क्रिएटिनिन आणि ईसीजी अहवाल पाठवा..."
    override val sendRequestBtn: String = "विनंती पाठवा"
    override val doctorToDoctorReferral: String = "डॉक्टर-ते-डॉक्टर रेफरल"
    override val selectTargetSpecialty: String = "1. वैद्यकीय विशेषज्ञता निवडा *"
    override val routingTriageAssignment: String = "2. दिशा व प्राधान्य निश्चिती"
    override val specialtyQueueOption: String = "🏢 विशेष विभाग रांग"
    override val namedSpecialistOption: String = "👨‍⚕️ विशिष्ट नियुक्त डॉक्टर"
    override val directPhysicianHandoff: String = "थेट संबंधित डॉक्टरांकडे केस वर्ग करा"
    override val noNamedSpecialistFallback: String = "या विभागात विशिष्ट डॉक्टर नोंदणीकृत नाहीत. सर्वसाधारण विभाग रांगेत पाठवले जाईल."
    override val urgencyLevelRequired: String = "3. तातडीची पातळी *"
    override val emergencyWarningQueueDelay: String = "धोका इशारा: रांगेमुळे विलंबाची शक्यता"
    override val referralQueueNotAcuteResponse: String = "रेफरल रांग ही सर्वसाधारण प्रक्रिया आहे, तातडीची सेवा नाही. रुग्णाची प्रकृती गंभीर असल्यास त्वरित आपत्कालीन SOS कॉल सुरू करा."
    override val launchEmergencySosNow: String = "🚨 आताच आपत्कालीन व्हिडिओ/व्हॉइस SOS कॉल करा"
    override val clinicalReasonForReferral: String = "4. रेफर करण्याचे वैद्यकीय कारण *"
    override val describeClinicalFindingsPrompt: String = "रुग्णाची लक्षणे, आजाराची वाढ आणि तज्ज्ञ सल्ला का आवश्यक आहे ते लिहा..."
    override val specificClinicalQuestionHeading: String = "5. तज्ज्ञ डॉक्टरांकडून हवे असलेले मार्गदर्शन *"
    override val clearlySpecifyQuestionInstruction: String = "तज्ज्ञांकडून नक्की काय मार्गदर्शन हवे आहे ते स्पष्ट लिहा"
    override val clinicalQuestionPlaceholder: String = "उदा. आजाराचे निदान, शस्त्रक्रियेची गरज किंवा औषध मात्रेतील बदल..."
    override val sendReferralToSpecialist: String = "तज्ज्ञ डॉक्टरांकडे रेफरल पाठवा"
    override val configureClinicQueueSlots: String = "क्लिनिक व रांग स्लॉट्स व्यवस्थापित करा"
    override val manageCapacityWalkInRules: String = "आजची रुग्ण क्षमता व वॉक-इन नियम ठरवा."
    override val acceptWalkInQueue: String = "वॉक-इन रुग्णांना परवानगी द्या"
    override val allowDirectCheckinNoBooking: String = "पूर्व नोंदणी नसलेल्या रुग्णांना थेट रांगेत येण्याची मुभा द्या."
    override val issueMedicalCertificateTitle: String = "वैद्यकीय प्रमाणपत्र द्या"
    override val certifiedClinicalLeaveFitness: String = "वैद्यकीय रजा व तंदुरुस्ती प्रमाणपत्र"
    override val certificateTypeLabel: String = "प्रमाणपत्राचा प्रकार:"
    override val certificateSealedStampNotice: String = "प्रमाणपत्रावर डिजिटल पडताळणी मोहर व स्वाक्षरी असेल."
    override val patientHealthCardTitle: String = "🪪 रुग्ण आरोग्य कार्ड"
    override val viewOnlyAccessRule: String = "🔒 केवळ पाहण्याची परवानगी (सुरक्षा नियम)"
    override val latestReportedCondition: String = "📋 नुकतीच नोंदवलेली लक्षणे"
    override val medicalHistoryAndRecords: String = "📋 पूर्वीचा आरोग्य इतिहास व नोंदी"
    override val recordsHeading: String = "आरोग्य नोंदी"
    override val noConditionRecordsLogged: String = "या रुग्णासाठी अद्याप कोणतीही लक्षण नोंद उपलब्ध नाही."
    override val noPriorPrescriptionsUploaded: String = "पूर्वीचे कोणतेही प्रिस्क्रिप्शन उपलब्ध नाही."
    override val aiDigitizedBadge: String = "एआय द्वारे डिजिटल"
    override val outOfStockNearPatientWarning: String = "⚠️ रुग्णाजवळ औषध उपलब्ध नाही · डॉक्टरांची संमती नोंदवली"
    override val likelyAvailableNearPatient: String = "✅ रुग्णाजवळ मिळण्याची शक्यता"
    override val addAnotherMedicineBtn: String = "+ आणखी एक औषध जोडा"
    override val medicineNamePlaceholder: String = "उदा. पॅरासिटामॉल 650mg किंवा ॲमॉक्सिसिलिन"
    override val notFoundNearPatientLocation: String = "⚠️ रुग्णाच्या परिसराजवळ उपलब्ध नाही"
    override val swapMedicineBtn: String = "बदला ✓"
    override val medicineSuggestionDisclaimer: String = "⚠️ अस्वीकरण: सुचवलेली औषधे केवळ वर्गीकरणावर आधारित आहेत — तपासणी करूनच द्या."
    override val quantityShort: String = "प्रमाण"
    override val frequencyAndTiming: String = "डोस व वेळ"
    override val durationLabel: String = "कालावधी (दिवस)"
    override val addToPrescriptionBtn: String = "+ प्रिस्क्रिप्शनमध्ये जोडा"
    override val dietaryFollowUpInstructions: String = "आहार व पुढील तपासणी सूचना"
    override val instructionsPatientAsha: String = "रुग्ण व आशा सेविकेसाठी सूचना"
    override val selectProposedDate: String = "तारीख निवडा:"
    override val selectTimeSlotDialog: String = "वेळ स्लॉट निवडा:"
    override val sendProposalBtn: String = "प्रस्ताव पाठवा"
    override val startConsultBtn: String = "सल्ला सुरू करा"
    override val noShowBtn: String = "रुग्ण अनुपस्थित"
    override val referCaseToSpecialist: String = "🔄 तज्ज्ञ डॉक्टरांकडे वर्ग करा"
    override val selectTargetSpecialtyColon: String = "वैद्यकीय विशेषज्ञता निवडा:"
    override val clinicalReferralNotesColon: String = "रेफरल क्लिनिकल नोंदी:"
    override val transferCaseArrow: String = "केस वर्ग करा →"
    override val scheduleNewAppointmentTitle: String = "📅 नवीन भेटीची वेळ ठरवा"
    override val proposeConsultationTime: String = "रुग्णाला सल्लामसलतीची वेळ सुचवा"
    override val selectPatientColon: String = "रुग्ण निवडा:"
    override val selectDateColon: String = "तारीख निवडा:"
    override val availableTimeSlotColon: String = "उपलब्ध वेळ स्लॉट:"
    override val sendAppointmentProposalCheck: String = "भेटीचा प्रस्ताव पाठवा ✓"
    override val specialistLoopClosure: String = "तज्ज्ञ डॉक्टरांचा अहवाल व सांगता"
    override val referringAskClinicalQuestion: String = "रेफर करणाऱ्या डॉक्टरांचा प्रश्न:"
    override val clinicalFindingsDiagnosticAssessment: String = "1. क्लिनिकल निष्कर्ष व रोग निदान *"
    override val documentEvaluationFindingsPrompt: String = "आपले तपासणी निष्कर्ष व निदान तपशील नोंदवा..."
    override val ongoingCarePlanRecommendations: String = "2. पुढील उपचार योजना व सल्ला *"
    override val adviseTreatmentAdjustmentsPrompt: String = "औषधांचा डोस, आहार सल्ला किंवा तपासणीची वारंवारता सुचवा..."
    override val specialistFollowUpRequired: String = "तज्ज्ञांकडून पुन्हा फॉलो-अप आवश्यक आहे"
    override val sendFindingsCloseLoop: String = "अहवाल पाठवून सांगता करा"
    override val ultraLowBandwidthMode: String = "📡 कमी इंटरनेट मोड (केवळ 2G ऑडिओ)"
    override val connectedPhcTeleKiosk: String = "सुंदरपुरा प्राथमिक आरोग्य केंद्र किऑस्कवरून जोडले"
    override val pulseLabel: String = "❤️ नाडी (Pulse)"
    override val bpLabel: String = "🩸 रक्तदाब (BP)"
    override val spo2VitalsLabel: String = "🫁 ऑक्सिजन (SpO2)"
    override val tempLabel: String = "🌡️ तापमान"
    override val tapToExpand: String = "विस्तारण्यासाठी टॅप करा"
    override val patientHealthVitals: String = "रुग्णाचे शारीरिक मापदंड"
    override val bpNormalSample: String = "• रक्तदाब: 118/78 mmHg (सामान्य)"
    override val heartRateSample: String = "• हृदय गती: 74 bpm (स्थिर)"
    override val bloodOxygenSample: String = "• ऑक्सिजन: 98% SpO2 (उत्तम)"
    override val temperatureSample: String = "• तापमान: 98.4°F"
    override val chronicConditionNone: String = "• जुनाट आजार: काहीही नाही"
    override val lastVisitSample: String = "• मागील भेट: १२ दिवसांपूर्वी (पीएचसी ओपीडी)"
    override val camOffLabel: String = "📷 कॅमेरा बंद"
    override val tapToEnableCam: String = "कॅमेरा सुरू करण्यासाठी टॅप करा"
    override val switchToVoiceCallWeakSignal: String = "ऑडिओ कॉलवर बदला (कमकुवत नेटवर्कसाठी)"
    override val doctorDidntJoinRebook: String = "डॉक्टर उपस्थित झाले नाहीत · नवीन वेळ निवडावी?"
    override val rebookCallBtn: String = "पुन्हा बुक करा"
    override val waitingForDoctorToJoin: String = "डॉक्टर जोडले जाण्याची प्रतीक्षा आहे…"
    override val statusNextInQueue: String = "स्थिती: रांगेत पुढील नंबर आपला आहे"
    override val doctorWrappingUpMsg: String = "डॉक्टर मागील रुग्णाची तपासणी संपवून लवकरच उपस्थित राहतील. कृपया अॅप बंद करू नका."
    override val enterConsultationRoom: String = "सल्ला कक्षात प्रवेश करा →"
    override val cancelLeaveBtn: String = "रद्द करा / बाहेर पडा"
    override val selectConsultationModeNetwork: String = "आपल्या इंटरनेट वेगाच्या आधारे सल्ला प्रकार निवडा:"
    override val videoCallHd: String = "व्हिडिओ कॉल (HD)"
    override val requires4gWifi: String = "4G किंवा वाय-फाय आवश्यक"
    override val voiceCallLowBandwidth: String = "व्हॉइस कॉल (कमी इंटरनेट)"
    override val recommended2gSignal: String = "2G किंवा कमकुवत नेटवर्कसाठी योग्य"
    override val confirmBookingCheck: String = "बुकिंग निश्चित करा ✓"
    override val selectSeverityLevel: String = "गंभीरता पातळी निवडा:"
    override val nearestDoctorsListView: String = "जवळचे डॉक्टर्स (यादी)"
    override val distanceMocked: String = "अंतर: 2.5 किमी"
    override val findMedicineNearby: String = "📍 जवळचे औषध शोधा"
    override val notFoundNearbyAlternative: String = "जवळ उपलब्ध नाही — डॉक्टरांनी सुचवलेले पर्यायी औषध उपलब्ध"
    override val likelyInStock: String = "🟢 उपलब्ध असण्याची शक्यता"
    override val outOfStockTag: String = "🔴 संपले"
    override val callPharmacyBtn: String = "📞 कॉल करा"
    override val docSuggestedAlternative: String = "💡 डॉक्टरांनी सुचवलेले पर्यायी औषध उपलब्ध"
    override val docSuggestedAlternativePlain: String = "💡 डॉक्टरांनी सुचवलेले पर्यायी औषध उपलब्ध"
    override val pharmacyStockNotice: String = "सूचना: मेडिकल स्टोअरमधील औषध साठा अंदाजित आहे. कृपया जाण्यापूर्वी फोन करून खात्री करा."
    override val helpManualTitle: String = "मदत पुस्तिका"
    override val bloodGroupLabel: String = "रक्तगट"
    override val oPositiveSample: String = "O+ पॉझिटिव्ह"
    override val allergiesLabel: String = "अॅलर्जी"
    override val noneReported: String = "काहीही नाही"
    override val emergencyLabel: String = "आपत्कालीन"
    override val permanentOfflineQrIdentity: String = "कायमस्वरूपी ऑफलाइन QR ओळख"
    override val permanentQrOfflineRecord: String = "कायमस्वरूपी QR व ऑफलाइन नोंद"
    override val symptomsSubmittedTriage: String = "लक्षणे पीएचसी डॉक्टरांच्या तपासणी रांगेत पाठवली आहेत!"
    override val aiScannedBadge: String = "एआय द्वारे स्कॅन"
    override val findNearbyLink: String = "📍 जवळ शोधा"
    override val ruralHealthSchemesPmjay: String = "शासकीय आरोग्य योजना (आयुष्मान भारत)"
    override val freeTreatment5Lakh: String = "₹५ लाखांपर्यंत मोफत उपचार व मातृत्व सहाय्य"
    override val viewSchemesBtn: String = "योजना पहा"
    override val uploadPrescriptionOcr: String = "प्रिस्क्रिप्शन अपलोड करा (OCR)"
    override val extractedTextLabel: String = "काढलेला मजकूर"
    override val noPrescriptionsFound: String = "कोणतेही प्रिस्क्रिप्शन सापडले नाही."
    override val prescribedMedicinesLabel: String = "दिलेली औषधे:"
    override val liveVisitQueue: String = "थेट क्लिनिक रांग"
    override val noActiveQueueTicket: String = "सक्रिय टोकन नाही"
    override val checkInScheduledDesc: String = "टोकन मिळवण्यासाठी आपल्या नियोजित भेटीत चेक-इन करा किंवा वॉक-इन रांगेत सामील व्हा."
    override val getInstantTokenToday: String = "आजसाठी त्वरित टोकन मिळवा"
    override val yourTokenNumberCaps: String = "तुमचा टोकन क्रमांक"
    override val confirmingPosition: String = "रांगेतील जागा तपासली जात आहे…"
    override val queuePositionLabel: String = "रांगेतील जागा"
    override val attendingPhysician: String = "तपासणारे डॉक्टर"
    override val cancelTokenBtn: String = "टोकन रद्द करा"
    override val govtSchemesTitle: String = "शासकीय योजना"
    override val governmentHealthSchemes: String = "शासकीय आरोग्य योजना"
    override val ruralWelfarePrograms: String = "ग्रामीण कल्याण व अनुदान योजना"
    override val eligibleBadge: String = "पात्र"
    override val closeSchemesView: String = "योजना तपशील बंद करा"
    override val digitalHealthCardUmid: String = "डिजिटल आरोग्य कार्ड (UMID)"
    override val vitalSenseIdentity: String = "व्हाइटलसेन्स / सेहतसेतू ओळख"
    override val linkedBeneficiariesFamily: String = "जोडलेले लाभार्थी (कुटुंब):"
    override val primarySelf: String = "👤 स्वतः (प्रमुख)"
    override val scanAtClinicDispensary: String = "पीएचसी क्लिनिक किंवा औषधालयात स्कॅन करा"
    override val emergencyContactLabel: String = "आपत्कालीन संपर्क"
    override val assignedAshaLabel: String = "नेमून दिलेली आशा सेविका"
    override val activeClinicalConditionLabel: String = "सध्याची आरोग्य स्थिती"
    override val linkAbhaBtn: String = "आभा (ABHA) जोडा"
    override val offlineSqliteEncrypted: String = "सुरक्षित ऑफलाइन डेटा"
    override val logHealthSymptomsTitle: String = "आरोग्य लक्षणे नोंदवा"
    override val categoryCaps: String = "श्रेणी"
    override val selectCommonSymptoms: String = "सामान्य लक्षणे निवडा"
    override val severityLevelCaps: String = "गंभीरता पातळी"
    override val submitToDoctorTriage: String = "🚀 डॉक्टरांकडे पाठवा"
    override val careJourneyTitle: String = "आरोग्य प्रवास"
    override val spo2Label: String = "ऑक्सिजन (SpO2)"
    override val backArrowBtn: String = "← मागे जा"
    override val howAreYouFeelingToday: String = "आज आपल्याला कसे वाटत आहे?"
    override val checkInSavedNotice: String = "✅ नोंद जतन झाली. आवश्यक असल्यास डॉक्टर किंवा आशा सेविका आपल्याशी संपर्क करतील."
    override val guidedBreathingTitle: String = "🌬️ श्वसन व्यायाम"
    override val breathe4SecondsMsg: String = "४ सेकंद श्वास आत घ्या, ४ सेकंद रोखा, ४ सेकंदात सोडा."
    override val tapToStart: String = "सुरू करण्यासाठी टॅप करा"
    override val digitizePaperPrescription: String = "कॅमेऱ्याने स्कॅन करून किंवा लिहून प्रिस्क्रिप्शन डिजिटल करा"
    override val addPrescribedMedicines: String = "दिलेली औषधे जोडा"
    override val addMedicineBtn: String = "+ औषध जोडा"
    override val positionPrescriptionFrame: String = "📄 प्रिस्क्रिप्शन फ्रेमच्या आत ठेवा"
    override val googleAutoCropScanner: String = "✨ ऑटो-क्रॉप व ऑटो-क्लीन स्कॅनर"
    override val cantScanEnterManually: String = "✍️ स्कॅन होत नाही? हाताने नोंदवा"
    override val cameraPermissionNeeded: String = "कॅमेरा परवानगी आवश्यक"
    override val cameraPermissionReason: String = "प्रिस्क्रिप्शन ऑफलाइन स्कॅन करण्यासाठी कॅमेरा परवानगी आवश्यक आहे."
    override val cameraAccessDeclinedMsg: String = "कॅमेरा परवानगी नाकारली गेली. कृपया सेटिंग्जमध्ये जाऊन परवानगी सुरू करा."
    override val openAppSettingsBtn: String = "⚙️ अॅप सेटिंग्ज उघडा"
    override val allowCameraAccessBtn: String = "कॅमेरा परवानगी द्या"
    override val enterDetailsManuallyBtn: String = "✍️ हाताने नोंद करा"
    override val aiPrescriptionDigitizer: String = "📷 एआय प्रिस्क्रिप्शन डिजिटायझर"
    override val zeroCloudOfflineInference: String = "⚡ इंटरनेटशिवाय सुरक्षित ऑफलाइन तपासणी"
    override val selectPrescriptionPhotoDesc: String = "इंटरनेटशिवाय फोनवरच प्रिस्क्रिप्शनमधून औषधे वाचण्यासाठी फोटो निवडा."
    override val simulateCaptureScan: String = "नमुना स्कॅन करा:"
    override val feverRxSample: String = "🌡️ तापाचे प्रिस्क्रिप्शन"
    override val infectionSample: String = "💊 संसर्ग औषध"
    override val extractedClinicalEntities: String = "ओळखलेली औषधे व सूचना:"
    override val rawOcrTextStream: String = "मूळ स्कॅन केलेला मजकूर"
    override val clinicalInstructionsNotes: String = "डॉक्टरांच्या सूचना व डोस"
    override val saveToMedicalRecord: String = "रुग्णाच्या मेडिकल रेकॉर्डमध्ये जतन करा ✓"
    override val readingPrescriptionOnDevice: String = "🔍 फोनवर प्रिस्क्रिप्शन वाचले जात आहे..."
    override val runningLocalMlKitOcr: String = "इंटरनेटशिवाय ऑफलाइन मजकूर ओळख सुरू आहे"
    override val reviewConfirmOcrScan: String = "📋 स्कॅन तपासणी व पुष्टी"
    override val extractedTextTapToEdit: String = "काढलेला मजकूर (बदलण्यासाठी टॅप करा):"
    override val onDeviceOcrBadge: String = "डिव्हाइस OCR"
    override val noMedicineNamesMatchedFallback: String = "कोणतेही औषध आपोआप जुळले नाही. वरील मजकूर डिजिटल टीप म्हणून जतन केला जाईल."
    override val prescribingDoctorHealthPost: String = "डॉक्टरांचे नाव / प्राथमिक आरोग्य केंद्र:"
    override val instructionsDosageDirections: String = "औषध डोस व सूचना:"
    override val retakePhotoBtn: String = "🔁 पुन्हा फोटो घ्या"
    override val couldntReadAnyText: String = "आम्हाला मजकूर वाचता आला नाही"
    override val photoQualityHint: String = "फोटो अंधुक किंवा तिरपा असू शकतो. कृपया चांगल्या प्रकाशात कॅमेरा स्थिर धरून पुन्हा फोटो घ्या."
    override val enterPrescriptionManually: String = "✍️ हाताने प्रिस्क्रिप्शन लिहा"
    override val reviewPrescriptionPhoto: String = "📸 फोटो तपासा"
    override val ensureHandwritingReadable: String = "डॉक्टरांचे हस्ताक्षर व औषधांची नावे स्पष्ट दिसत असल्याची खात्री करा."
    override val useThisPhotoScanText: String = "✅ हा फोटो वापरा (मजकूर स्कॅन)"

    // New Button MarathiAppStrings Additions

    // New Button MarathiAppStrings Additions
    override val saveRecord: String = "नोंद जतन करा"
    override val restockItem: String = "वस्तू स्टॉक करा"
    override val broadcastDistrictDirective: String = "📢 जिल्हास्तरीय आरोग्य सूचना प्रसारित करा"
    override val manageDispensary: String = "औषधालय व्यवस्थापन करा"
    override val diagnosticsLabs: String = "निदान चाचण्या व लॅब"
    override val visitAction: String = "भेट द्या"
    override val logVitalsAction: String = "शारीरिक मापदंड नोंदवा"
    override val viewProfile: String = "प्रोफाइल पहा"
    override val startTeleConsultCall: String = "📹 टेलि-सल्ला कॉल सुरू करा"
    override val scanExternalRxOcr: String = "📷 बाहेरील प्रिस्क्रिप्शन स्कॅन करा (OCR)"
    override val saveConfiguration: String = "रचना जतन करा"
    override val digitallySignIssue: String = "डिजिटल स्वाक्षरी करून जारी करा"
    override val closeHealthCard: String = "आरोग्य कार्ड बंद करा"
    override val closeMedicalHistory: String = "वैद्यकीय इतिहास बंद करा"
    override val closeEReport: String = "ई-रिपोर्ट बंद करा"
    override val issueOrder: String = "चाचणी आदेश जारी करा"
    override val bookOpdTokenNow: String = "🎟️ आता ओपीडी टोकन बुक करा"
    override val submitToDoctorQueueCheck: String = "डॉक्टरांच्या रांगेत पाठवा ✓"
    override val viewCareJourneyTimeline: String = "आरोग्य प्रवास टाइमलाइन पहा"
    override val saveCheckIn: String = "चेक-इन जतन करा"
    override val savePrescriptionRecord: String = "प्रिस्क्रिप्शन नोंद जतन करा"
    override val saveDigitizedPrescription: String = "💾 डिजिटल प्रिस्क्रिप्शन जतन करा"
    override val manualHelpOverview: String = "1. आरोग्य कार्ड: तपशील ऑफलाइन पहा.\\n2. SOS: आपत्कालीन अलर्ट पाठवा.\\n3. OCR: प्रिस्क्रिप्शन स्कॅन करा."
    override val clinicalAskPrefix: String = "वैद्यकीय विचारणा: "

    // Final Polish MarathiAppStrings Additions
    override val scanPhysicalCardZeroPwdDesc: String = "आशा सेविकेने दिलेले आरोग्य कार्ड स्कॅन करा. कोणत्याही पासवर्डची आवश्यकता नाही."
    override val patientIdentityVerified: String = "रुग्ण ओळख पडताळली!"
    override val referredByDoctor: String = "डॉक्टरांनी रेफर केले"
    override val specialistFindingsDiagnosticAssessment: String = "तज्ज्ञ डॉक्टरांचे निदान निष्कर्ष"
    override val specialistRecommendationsCarePlan: String = "तज्ज्ञ डॉक्टरांच्या शिफारसी"
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
