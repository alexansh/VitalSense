package com.vitalsense.app.feature.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import com.vitalsense.app.core.state.AppStateHolder
import com.vitalsense.app.core.ui.components.AppUpdateBanner
import com.vitalsense.app.core.util.AppUpdateChecker
import com.vitalsense.app.core.util.AppUpdateInfo
import com.vitalsense.app.core.ui.components.TopRoleSwitcherBar
import com.vitalsense.app.core.ui.util.AdaptiveScreenContainer
import com.vitalsense.app.feature.admin.AdminHomeScreen
import com.vitalsense.app.feature.admin.AdminDiagnosticsScreen
import com.vitalsense.app.feature.admin.AdminFacilityQualityScreen
import com.vitalsense.app.feature.admin.AdminViewModel
import com.vitalsense.app.feature.admin.AdminDispensaryRestockScreen
import com.vitalsense.app.feature.admin.AdminDiseaseTrendsScreen
import com.vitalsense.app.feature.admin.AdminQueueOversightViewModel
import com.vitalsense.app.feature.admin.QueueOversightScreen
import com.vitalsense.app.feature.asha.AshaHomeScreen
import com.vitalsense.app.feature.auth.LoginScreen
import com.vitalsense.app.feature.biomedical.BioMedicalScreen
import com.vitalsense.app.feature.bloodbank.BloodBankScreen
import com.vitalsense.app.feature.doctor.CaseDetailScreen
import com.vitalsense.app.feature.doctor.DoctorHomeScreen
import com.vitalsense.app.feature.doctor.DoctorViewModel
import com.vitalsense.app.feature.doctor.DoctorQueueScreen
import com.vitalsense.app.feature.doctor.SpecialistReferralsScreen
import com.vitalsense.app.feature.ipd.IpdBedTrackerScreen
import com.vitalsense.app.feature.lab.LabReportsScreen
import com.vitalsense.app.feature.opd.OpdQueueScreen
import com.vitalsense.app.feature.ot.OtSchedulerScreen
import com.vitalsense.app.feature.patient.AppointmentsScreen
import com.vitalsense.app.feature.patient.PatientHomeScreen
import com.vitalsense.app.feature.patient.PatientViewModel
import com.vitalsense.app.feature.patient.PatientQueueViewModel
import com.vitalsense.app.feature.patient.QueueStatusScreen
import com.vitalsense.app.feature.referrals.ExternalReferralScreen
import com.vitalsense.app.feature.splash.SplashScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VitalSenseNavGraph(
    appStateHolder: AppStateHolder,
    repository: VitalSenseRepository,
    patientViewModel: PatientViewModel = hiltViewModel(),
    doctorViewModel: DoctorViewModel = hiltViewModel(),
    adminViewModel: AdminViewModel = hiltViewModel(),
    patientQueueViewModel: PatientQueueViewModel = hiltViewModel(),
    adminQueueOversightViewModel: AdminQueueOversightViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // Background In-App Update Check
    var updateInfo by remember { mutableStateOf<AppUpdateInfo?>(null) }
    var isUpdateDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val info = AppUpdateChecker.checkForUpdates()
        updateInfo = info
    }

    // Core global state
    val isLoggedIn by appStateHolder.isLoggedIn.collectAsStateWithLifecycle()
    val currentRole by appStateHolder.currentRole.collectAsStateWithLifecycle()
    val activePatient by appStateHolder.activePatient.collectAsStateWithLifecycle()
    val activeAsha by appStateHolder.activeAsha.collectAsStateWithLifecycle()
    val activeDoctor by doctorViewModel.activeDoctor.collectAsStateWithLifecycle()
    val activeProxyPatient by appStateHolder.activeProxyPatient.collectAsStateWithLifecycle()
    val isOffline by appStateHolder.isOffline.collectAsStateWithLifecycle()
    val connectivityState by appStateHolder.connectivityState.collectAsStateWithLifecycle()
    val isSyncing by appStateHolder.isSyncing.collectAsStateWithLifecycle()
    val pendingOutboxCount by appStateHolder.pendingOutboxCount.collectAsStateWithLifecycle(initialValue = 0)

    // Doctor specific scoped streams
    val doctorCases by doctorViewModel.scopedCases.collectAsStateWithLifecycle()
    val doctorAppointments by doctorViewModel.appointments.collectAsStateWithLifecycle()
    val doctorDispensaryStock by doctorViewModel.dispensaryStock.collectAsStateWithLifecycle()
    val selectedDoctorCase by doctorViewModel.selectedCase.collectAsStateWithLifecycle()
    val patientPrescriptions by doctorViewModel.patientPrescriptions.collectAsStateWithLifecycle()
    val patientProfile by doctorViewModel.patientProfile.collectAsStateWithLifecycle()
    val doctorCaseAnalytics by doctorViewModel.caseAnalytics.collectAsStateWithLifecycle()
    val patientMedicalHistory by doctorViewModel.patientMedicalHistory.collectAsStateWithLifecycle()

    // Data streams from repository for general components
    val villages by repository.getVillages().collectAsStateWithLifecycle(initialValue = emptyList())
    val patients by repository.getPatients().collectAsStateWithLifecycle(initialValue = emptyList())
    val notices by repository.getNotices().collectAsStateWithLifecycle(initialValue = emptyList())
    val allPrescriptions by repository.getPrescriptions().collectAsStateWithLifecycle(initialValue = emptyList())
    val allConditions by repository.getConditionRecords().collectAsStateWithLifecycle(initialValue = emptyList())
    val allAppointments by repository.getAppointments().collectAsStateWithLifecycle(initialValue = emptyList())
    val schemes by repository.getGovernmentSchemes().collectAsStateWithLifecycle(initialValue = emptyList())
    val allLabReports by repository.getLabReports().collectAsStateWithLifecycle(initialValue = emptyList())
    val allOpdTokens by repository.getOpdTokens().collectAsStateWithLifecycle(initialValue = emptyList())
    val allBloodStock by repository.getBloodStock().collectAsStateWithLifecycle(initialValue = emptyList())
    val allIpdBeds by repository.getIpdBeds().collectAsStateWithLifecycle(initialValue = emptyList())
    val allOtBookings by repository.getOtSurgeryBookings().collectAsStateWithLifecycle(initialValue = emptyList())
    val allExternalReferrals by repository.getExternalReferrals().collectAsStateWithLifecycle(initialValue = emptyList())
    val allBioMedicalEquipment by repository.getBioMedicalEquipment().collectAsStateWithLifecycle(initialValue = emptyList())

    val currentLanguage by appStateHolder.currentLanguage.collectAsStateWithLifecycle()

    // The effective patient (either direct or proxy managed by ASHA)
    val effectivePatient = activeProxyPatient ?: activePatient

    val activeUserName = when (currentRole) {
        UserRole.PATIENT -> effectivePatient.name
        UserRole.ASHA -> activeAsha.name
        UserRole.DOCTOR -> activeDoctor.name
        UserRole.ADMIN -> "District CMO (Rampur)"
    }

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        com.vitalsense.app.core.call.TeleCallingManager.onCallCompletedListener = { callLog ->
            coroutineScope.launch {
                repository.saveCallLog(callLog)
            }
        }
    }

    AnimatedContent(
        targetState = showSplash,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "SplashTransition"
    ) { inSplash ->
        if (inSplash) {
            SplashScreen(
                onSplashFinished = { showSplash = false }
            )
        } else {
            AnimatedContent(
                targetState = isLoggedIn,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "AuthTransition"
            ) { loggedIn ->
                if (!loggedIn) {
                    LoginScreen(
                        currentLanguage = currentLanguage,
                        onToggleLanguage = { appStateHolder.toggleLanguage() },
                        onSelectLanguage = { appStateHolder.setLanguage(it) },
                        onPatientLogin = { selectedPatient ->
                            appStateHolder.loginAsPatient(selectedPatient)
                        },
                        onAshaLogin = { selectedAsha ->
                            appStateHolder.loginAsAsha(selectedAsha)
                        },
                        onDoctorLogin = { selectedDoctor ->
                            appStateHolder.loginAsDoctor(selectedDoctor)
                        },
                        onAdminLogin = {
                            appStateHolder.loginAsAdmin()
                        },
                        modifier = modifier
                    )
                } else {
                    Scaffold(
                        topBar = {
                            TopRoleSwitcherBar(
                                currentRole = currentRole,
                                activeUserName = activeUserName,
                                activeProxyPatient = activeProxyPatient,
                                onExitProxy = {
                                    appStateHolder.clearProxy()
                                    appStateHolder.switchRole(UserRole.ASHA)
                                },
                                isOffline = isOffline,
                                connectivityState = connectivityState,
                                isSyncing = isSyncing,
                                pendingOutboxCount = pendingOutboxCount,
                                onManualSync = {
                                    appStateHolder.triggerSync()
                                },
                                onToggleOffline = {
                                    appStateHolder.toggleOffline()
                                },
                                currentLanguage = currentLanguage,
                                onToggleLanguage = {
                                    appStateHolder.toggleLanguage()
                                },
                                onSelectLanguage = { lang ->
                                    appStateHolder.setLanguage(lang)
                                },
                                onLogout = {
                                    doctorViewModel.clearSelectedCase()
                                    appStateHolder.logout()
                                }
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.background,
                        modifier = modifier.fillMaxSize()
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (!isUpdateDismissed && updateInfo?.isUpdateAvailable == true) {
                                AppUpdateBanner(
                                    updateInfo = updateInfo,
                                    onDismiss = { isUpdateDismissed = true }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                AnimatedContent(
                                    targetState = currentRole,
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                                    },
                                    label = "RoleTransition"
                                ) { role ->
                                    when (role) {
                                        UserRole.PATIENT -> {
                                            var currentPatientScreen by remember { mutableStateOf("home") }
                                            val patientHomeScrollState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                                            val familyMembers by repository.getFamilyMembers(effectivePatient.id).collectAsStateWithLifecycle(initialValue = emptyList())
                                            val patientReferrals by repository.getReferralsForPatient(effectivePatient.id).collectAsStateWithLifecycle(initialValue = emptyList())

                                            AnimatedContent(
                                                targetState = currentPatientScreen,
                                                transitionSpec = {
                                                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                                                },
                                                label = "PatientScreenTransition"
                                            ) { screen ->
                                                when (screen) {
                                                    "mental_wellness" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        com.vitalsense.app.feature.patient.mentalhealth.MentalWellnessScreen(
                                                            patient = effectivePatient,
                                                            onLogMood = { notes, severity ->
                                                                patientViewModel.logMentalWellness(
                                                                    patient = effectivePatient,
                                                                    moodNotes = notes,
                                                                    severityLevel = severity,
                                                                    isProxy = activeProxyPatient != null
                                                                )
                                                            },
                                                            onBack = { currentPatientScreen = "home" }
                                                        )
                                                    }
                                                    "lab_reports" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        LabReportsScreen(
                                                            patient = effectivePatient,
                                                            labReports = allLabReports.filter { it.patientId == effectivePatient.id },
                                                            onBackClick = { currentPatientScreen = "home" },
                                                            onOrderNewTest = { report ->
                                                                coroutineScope.launch {
                                                                    repository.saveLabReport(report)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "opd_queue" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        OpdQueueScreen(
                                                            patient = effectivePatient,
                                                            opdTokens = allOpdTokens.filter { it.patientId == effectivePatient.id },
                                                            onBackClick = { currentPatientScreen = "home" },
                                                            onBookToken = { token ->
                                                                coroutineScope.launch {
                                                                    repository.bookOpdToken(token)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "blood_bank" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        BloodBankScreen(
                                                            bloodStock = allBloodStock,
                                                            onBackClick = { currentPatientScreen = "home" }
                                                        )
                                                    }
                                                    "appointments" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        AppointmentsScreen(
                                                            appointments = allAppointments.filter { it.patientId == effectivePatient.id },
                                                            onRequestNew = { currentPatientScreen = "home" },
                                                            onBackClick = { currentPatientScreen = "home" },
                                                            onCheckIn = { apptId ->
                                                                patientQueueViewModel.checkIn(apptId)
                                                                currentPatientScreen = "queue_status"
                                                            },
                                                            onViewLiveQueue = { currentPatientScreen = "queue_status" },
                                                            onBookAppointment = { appt ->
                                                                coroutineScope.launch {
                                                                    repository.scheduleAppointment(appt)
                                                                }
                                                            },
                                                            language = currentLanguage
                                                        )
                                                    }
                                                    "queue_status" -> {
                                                        BackHandler { currentPatientScreen = "home" }
                                                        val patientQueueEntry by patientQueueViewModel.queueEntry.collectAsStateWithLifecycle()
                                                        val patientPos by patientQueueViewModel.position.collectAsStateWithLifecycle()
                                                        val patientWaitMin by patientQueueViewModel.estimatedWaitMinutes.collectAsStateWithLifecycle()
                                                        QueueStatusScreen(
                                                            entry = patientQueueEntry,
                                                            position = patientPos,
                                                            estimatedWaitMinutes = patientWaitMin,
                                                            onBackClick = { currentPatientScreen = "home" },
                                                            onCancelEntry = { entryId ->
                                                                patientQueueViewModel.cancelQueueEntry(entryId)
                                                            },
                                                            onJoinWalkIn = {
                                                                patientQueueViewModel.joinWalkIn("doc_rajesh", "Dr. Rajesh Varma")
                                                            }
                                                        )
                                                    }
                                                    else -> {
                                                        if (activeProxyPatient != null) {
                                                            BackHandler {
                                                                appStateHolder.clearProxy()
                                                                appStateHolder.switchRole(UserRole.ASHA)
                                                            }
                                                        } else {
                                                            BackHandler {
                                                                appStateHolder.logout()
                                                            }
                                                        }

                                                        PatientHomeScreen(
                                                            patient = effectivePatient,
                                                            scrollState = patientHomeScrollState,
                                                            notices = notices,
                                                            prescriptions = allPrescriptions.filter { it.patientId == effectivePatient.id },
                                                            schemes = schemes,
                                                            familyMembers = familyMembers,
                                                            referrals = patientReferrals,
                                                            isOffline = isOffline,
                                                            language = currentLanguage,
                                                            onCategoryClick = { category ->
                                                                if (category == ConditionCategory.MENTAL_HEALTH) {
                                                                    currentPatientScreen = "mental_wellness"
                                                                }
                                                            },
                                                            onLogCondition = { record ->
                                                                coroutineScope.launch {
                                                                    repository.logCondition(record)
                                                                }
                                                            },
                                                            onTriggerSos = {
                                                                coroutineScope.launch {
                                                                    repository.triggerEmergencySos(effectivePatient, null, null)
                                                                }
                                                            },
                                                            onSavePrescription = { rx ->
                                                                coroutineScope.launch {
                                                                    repository.savePrescription(rx)
                                                                }
                                                            },
                                                            onNavigateToLabReports = { currentPatientScreen = "lab_reports" },
                                                            onNavigateToOpdQueue = { currentPatientScreen = "opd_queue" },
                                                            onNavigateToBloodBank = { currentPatientScreen = "blood_bank" },
                                                            onNavigateToAppointments = { currentPatientScreen = "appointments" },
                                                            onNavigateToLiveQueue = { currentPatientScreen = "queue_status" }
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        UserRole.ASHA -> {
                                            var currentAshaScreen by remember { mutableStateOf("home") }
                                            val ashaHomeScrollState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

                                            val immunizations by repository.getImmunizationRecords().collectAsStateWithLifecycle(initialValue = emptyList())
                                            val dailyRounds by repository.getDailyRounds().collectAsStateWithLifecycle(initialValue = emptyList())
                                            val ashaMedicines by repository.getAshaMedicines().collectAsStateWithLifecycle(initialValue = emptyList())
                                            val ashaReferrals by repository.getAllReferrals().collectAsStateWithLifecycle(initialValue = emptyList())

                                            AnimatedContent(
                                                targetState = currentAshaScreen,
                                                transitionSpec = {
                                                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                                                },
                                                label = "AshaScreenTransition"
                                            ) { screen ->
                                                when (screen) {
                                                    "immunization" -> {
                                                        BackHandler { currentAshaScreen = "home" }
                                                        com.vitalsense.app.feature.asha.ImmunizationTrackerScreen(
                                                            records = immunizations,
                                                            onBackClick = { currentAshaScreen = "home" }
                                                        )
                                                    }
                                                    "daily_rounds" -> {
                                                        BackHandler { currentAshaScreen = "home" }
                                                        com.vitalsense.app.feature.asha.DailyRoundsScreen(
                                                            rounds = dailyRounds,
                                                            onBackClick = { currentAshaScreen = "home" },
                                                            onSaveRound = { round ->
                                                                coroutineScope.launch {
                                                                    repository.saveDailyRound(round)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "medicine_restock" -> {
                                                        BackHandler { currentAshaScreen = "home" }
                                                        com.vitalsense.app.feature.asha.MedicineRestockScreen(
                                                            medicines = ashaMedicines,
                                                            onBackClick = { currentAshaScreen = "home" },
                                                            onRequestRestock = { updatedMedicine ->
                                                                coroutineScope.launch {
                                                                    repository.saveAshaMedicine(updatedMedicine)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    else -> {
                                                        BackHandler {
                                                            appStateHolder.logout()
                                                        }

                                                        AshaHomeScreen(
                                                            asha = activeAsha,
                                                            patients = patients.filter { it.ashaWorkerId == activeAsha.id },
                                                            scrollState = ashaHomeScrollState,
                                                            notices = notices,
                                                            onSelectProxyPatient = { selectedPatient ->
                                                                appStateHolder.setProxyPatient(selectedPatient)
                                                                appStateHolder.switchRole(UserRole.PATIENT)
                                                            },
                                                            onSavePatient = { newPatient ->
                                                                coroutineScope.launch {
                                                                    repository.savePatient(newPatient)
                                                                }
                                                            },
                                                            onSendNotice = { notice ->
                                                                coroutineScope.launch {
                                                                    repository.sendNotice(notice)
                                                                }
                                                            },
                                                            onSavePrescription = { rx ->
                                                                coroutineScope.launch {
                                                                    repository.savePrescription(rx)
                                                                }
                                                            },
                                                            onTriggerSosForPatient = { targetPatient ->
                                                                repository.triggerEmergencySos(targetPatient, null, null)
                                                            },
                                                            onImmunizationClick = { currentAshaScreen = "immunization" },
                                                            onDailyRoundsClick = { currentAshaScreen = "daily_rounds" },
                                                            onMedicineRestockClick = { currentAshaScreen = "medicine_restock" },
                                                            referrals = ashaReferrals,
                                                            onCompleteReferral = { ref ->
                                                                coroutineScope.launch {
                                                                    val updatedStatusHistory = ref.statusHistory + com.vitalsense.app.core.data.model.ReferralStatusHistory(
                                                                        status = com.vitalsense.app.core.data.model.ReferralStatus.COMPLETED,
                                                                        changedByUserId = activeAsha.id,
                                                                        note = "Follow-up marked done by ASHA"
                                                                    )
                                                                    repository.updateReferral(ref.copy(
                                                                        status = com.vitalsense.app.core.data.model.ReferralStatus.COMPLETED,
                                                                        statusHistory = updatedStatusHistory,
                                                                        updatedAt = System.currentTimeMillis()
                                                                    ))
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        UserRole.DOCTOR -> {
                                            var currentDoctorScreen by remember { mutableStateOf("home") }
                                            val doctorHomeScrollState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                                            val doctorReferrals by doctorViewModel.doctorReferrals.collectAsStateWithLifecycle()
                                            val allReferrals by doctorViewModel.allReferrals.collectAsStateWithLifecycle()

                                            AnimatedContent(
                                                targetState = selectedDoctorCase,
                                                transitionSpec = {
                                                    if (targetState != null) {
                                                        slideInHorizontally(tween(240)) { it / 4 } + fadeIn(tween(220)) togetherWith
                                                                slideOutHorizontally(tween(200)) { -it / 4 } + fadeOut(tween(180))
                                                    } else {
                                                        slideInHorizontally(tween(240)) { -it / 4 } + fadeIn(tween(220)) togetherWith
                                                                slideOutHorizontally(tween(200)) { it / 4 } + fadeOut(tween(180))
                                                    }
                                                },
                                                label = "DoctorDetailTransition"
                                            ) { currentDoctorCase ->
                                                if (currentDoctorCase != null) {
                                                    BackHandler {
                                                        doctorViewModel.clearSelectedCase()
                                                    }

                                                    CaseDetailScreen(
                                                        record = currentDoctorCase,
                                                        patient = patientProfile,
                                                        priorPrescriptions = patientPrescriptions,
                                                        dispensaryStock = doctorDispensaryStock,
                                                        currentDoctor = activeDoctor,
                                                        medicalHistory = patientMedicalHistory,
                                                        allConditions = allConditions.filter { it.patientId == currentDoctorCase.patientId },
                                                        allAppointments = allAppointments.filter { it.patientId == currentDoctorCase.patientId },
                                                        onBack = { doctorViewModel.clearSelectedCase() },
                                                        onSubmitResponse = { responseText, privateNotes ->
                                                            doctorViewModel.submitMedicalResponse(
                                                                caseId = currentDoctorCase.id,
                                                                responseText = responseText,
                                                                privateNotes = privateNotes
                                                            )
                                                        },
                                                        onIssuePrescription = { medicines, instructions ->
                                                            doctorViewModel.issuePrescription(
                                                                caseId = currentDoctorCase.id,
                                                                patientId = currentDoctorCase.patientId,
                                                                patientName = currentDoctorCase.patientName,
                                                                medicines = medicines,
                                                                instructions = instructions
                                                            )
                                                        },
                                                        onProposeAppointment = { date, timeSlot ->
                                                            doctorViewModel.proposeAppointment(
                                                                patientId = currentDoctorCase.patientId,
                                                                patientName = currentDoctorCase.patientName,
                                                                dateFormatted = date,
                                                                timeSlot = timeSlot
                                                            )
                                                        },
                                                        onReferCase = { targetSpecialty, referralNotes ->
                                                            doctorViewModel.referCase(
                                                                caseId = currentDoctorCase.id,
                                                                targetSpecialty = targetSpecialty,
                                                                referralNotes = referralNotes
                                                            )
                                                        },
                                                        onOrderLabTest = { report ->
                                                            coroutineScope.launch {
                                                                repository.saveLabReport(report)
                                                            }
                                                        },
                                                        onIssueMedicalCertificate = { cert ->
                                                            coroutineScope.launch {
                                                                repository.saveMedicalCertificate(cert)
                                                            }
                                                        },
                                                        referrals = allReferrals,
                                                        onSendStructuredReferral = { doctorViewModel.createReferral(it) }
                                                    )
                                                } else {
                                                    val doctorQueue by doctorViewModel.todaysQueue.collectAsStateWithLifecycle()
                                                    val doctorSlotConfig by doctorViewModel.todaySlotConfig.collectAsStateWithLifecycle()

                                                    when (currentDoctorScreen) {
                                                        "ot_scheduler" -> {
                                                            BackHandler { currentDoctorScreen = "home" }
                                                            OtSchedulerScreen(
                                                                bookings = allOtBookings,
                                                                onBackClick = { currentDoctorScreen = "home" },
                                                                onBookSurgery = { booking ->
                                                                    coroutineScope.launch {
                                                                        repository.saveOtSurgeryBooking(booking)
                                                                    }
                                                                }
                                                            )
                                                        }
                                                        "ipd_beds" -> {
                                                            BackHandler { currentDoctorScreen = "home" }
                                                            IpdBedTrackerScreen(
                                                                beds = allIpdBeds,
                                                                patients = patients,
                                                                onBackClick = { currentDoctorScreen = "home" },
                                                                onSaveBed = { bed ->
                                                                    coroutineScope.launch {
                                                                        repository.saveIpdBed(bed)
                                                                    }
                                                                }
                                                            )
                                                        }
                                                        "referrals" -> {
                                                            BackHandler { currentDoctorScreen = "home" }
                                                            ExternalReferralScreen(
                                                                referrals = allExternalReferrals,
                                                                patients = patients,
                                                                onBackClick = { currentDoctorScreen = "home" },
                                                                onIssueReferral = { ref ->
                                                                    coroutineScope.launch {
                                                                        repository.saveExternalReferral(ref)
                                                                    }
                                                                }
                                                            )
                                                        }
                                                        "live_queue" -> {
                                                            BackHandler { currentDoctorScreen = "home" }
                                                            DoctorQueueScreen(
                                                                doctor = activeDoctor,
                                                                todaysQueue = doctorQueue,
                                                                slotConfig = doctorSlotConfig,
                                                                patients = patients,
                                                                onBackClick = { currentDoctorScreen = "home" },
                                                                onCallNext = { doctorViewModel.callNext() },
                                                                onStartConsultation = { doctorViewModel.startConsultation(it) },
                                                                onCompleteConsultation = { id, notes -> doctorViewModel.completeConsultation(id, notes) },
                                                                onMarkNoShow = { doctorViewModel.markNoShow(it) },
                                                                onSkip = { doctorViewModel.skipEntry(it) },
                                                                onPrioritize = { doctorViewModel.prioritizeEntry(it) },
                                                                onAddWalkIn = { id, name -> doctorViewModel.addWalkInPatient(id, name) },
                                                                onUpdateSlotConfig = { cap, open, start, end -> doctorViewModel.updateSlotConfig(cap, open, start, end) }
                                                            )
                                                        }
                                                        "specialist_referrals" -> {
                                                            BackHandler { currentDoctorScreen = "home" }
                                                            SpecialistReferralsScreen(
                                                                doctor = activeDoctor,
                                                                referrals = doctorReferrals,
                                                                onBack = { currentDoctorScreen = "home" },
                                                                onAcceptReferral = { doctorViewModel.acceptReferral(it) },
                                                                onDeclineReferral = { refId, reason, sugg -> doctorViewModel.declineReferral(refId, reason, sugg) },
                                                                onRequestMoreInfo = { refId, note -> doctorViewModel.requestMoreInfo(refId, note) },
                                                                onSubmitFindings = { refId, findings, recs, followUp -> doctorViewModel.submitSpecialistFindings(refId, findings, recs, followUp) },
                                                                onStartConsultCall = { ref ->
                                                                    val dummyAppt = Appointment(
                                                                        id = "appt_ref_${ref.id}",
                                                                        patientId = ref.patientId,
                                                                        patientName = ref.patientName,
                                                                        doctorId = activeDoctor.id,
                                                                        doctorName = activeDoctor.name,
                                                                        doctorSpecialty = activeDoctor.specialty.displayName,
                                                                        dateFormatted = "Today",
                                                                        timeSlot = "Now",
                                                                        status = "Confirmed",
                                                                        proposedBy = UserRole.DOCTOR,
                                                                        callType = CallType.VIDEO
                                                                    )
                                                                    com.vitalsense.app.core.call.TeleCallingManager.startAppointmentCall(dummyAppt, isDoctor = true)
                                                                }
                                                            )
                                                        }
                                                        else -> {
                                                            BackHandler {
                                                                appStateHolder.logout()
                                                            }

                                                            DoctorHomeScreen(
                                                                doctor = activeDoctor,
                                                                scrollState = doctorHomeScrollState,
                                                                cases = doctorCases,
                                                                caseAnalytics = doctorCaseAnalytics,
                                                                appointments = doctorAppointments,
                                                                dispensaryStock = doctorDispensaryStock,
                                                                patients = patients,
                                                                notices = notices,
                                                                allConditions = allConditions,
                                                                todaysQueue = doctorQueue,
                                                                onSelectCase = { record ->
                                                                    doctorViewModel.selectCase(record)
                                                                },
                                                                onAcceptAppointment = { apptId ->
                                                                    doctorViewModel.acceptAppointment(apptId)
                                                                },
                                                                onDeclineAppointment = { apptId ->
                                                                    doctorViewModel.declineAppointment(apptId)
                                                                },
                                                                onProposeAppointment = { patId, patName, date, slot ->
                                                                    doctorViewModel.proposeAppointment(
                                                                        patientId = patId,
                                                                        patientName = patName,
                                                                        dateFormatted = date,
                                                                        timeSlot = slot
                                                                    )
                                                                },
                                                                onNavigateToOtScheduler = { currentDoctorScreen = "ot_scheduler" },
                                                                onNavigateToIpdBeds = { currentDoctorScreen = "ipd_beds" },
                                                                onNavigateToExternalReferrals = { currentDoctorScreen = "referrals" },
                                                                onNavigateToLiveQueue = { currentDoctorScreen = "live_queue" },
                                                                onNavigateToSpecialistReferrals = { currentDoctorScreen = "specialist_referrals" },
                                                                referrals = doctorReferrals,
                                                                onRemindAdminRestock = { item ->
                                                                    doctorViewModel.sendNotice(
                                                                        BroadcastNotice(
                                                                            id = "restock_${item.id}_${System.currentTimeMillis()}",
                                                                            senderRole = UserRole.DOCTOR,
                                                                            senderName = activeDoctor.name,
                                                                            targetRole = "ADMIN",
                                                                            targetVillage = "Dispensary",
                                                                            title = "⚠️ Restock Reminder: ${item.medicineName}",
                                                                            message = "Dr. ${activeDoctor.name} has flagged ${item.medicineName} (${item.category}) as low on stock (${item.availableQuantity} ${item.unit} remaining). Please perform inventory restock.",
                                                                            timestamp = System.currentTimeMillis(),
                                                                            isUrgent = true
                                                                        )
                                                                    )
                                                                },
                                                                onSendReferral = { referral ->
                                                                    doctorViewModel.createReferral(referral)
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        UserRole.ADMIN -> {
                                            var currentAdminScreen by remember { mutableStateOf("home") }
                                            
                                            val adminDispensaryStock by adminViewModel.dispensaryStock.collectAsStateWithLifecycle()
                                            val adminDiseaseTrends by adminViewModel.diseaseTrends.collectAsStateWithLifecycle()

                                            AnimatedContent(
                                                targetState = currentAdminScreen,
                                                transitionSpec = {
                                                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                                                },
                                                label = "AdminScreenTransition"
                                            ) { screen ->
                                                when (screen) {
                                                    "dispensary_restock" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        AdminDispensaryRestockScreen(
                                                            dispensaryStock = adminDispensaryStock,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onSaveItem = { item ->
                                                                adminViewModel.saveDispensaryItem(item)
                                                            }
                                                        )
                                                    }
                                                    "disease_trends" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        AdminDiseaseTrendsScreen(
                                                            villages = villages,
                                                            trendRecords = adminDiseaseTrends,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onSaveRecord = { record ->
                                                                adminViewModel.saveDiseaseTrendRecord(record)
                                                            }
                                                        )
                                                    }
                                                    "ipd_beds" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        IpdBedTrackerScreen(
                                                            beds = allIpdBeds,
                                                            patients = patients,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onSaveBed = { bed ->
                                                                coroutineScope.launch {
                                                                    repository.saveIpdBed(bed)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "ot_scheduler" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        OtSchedulerScreen(
                                                            bookings = allOtBookings,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onBookSurgery = { booking ->
                                                                coroutineScope.launch {
                                                                    repository.saveOtSurgeryBooking(booking)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "referrals" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        ExternalReferralScreen(
                                                            referrals = allExternalReferrals,
                                                            patients = patients,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onIssueReferral = { ref ->
                                                                coroutineScope.launch {
                                                                    repository.saveExternalReferral(ref)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "biomedical" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        BioMedicalScreen(
                                                            equipmentList = allBioMedicalEquipment,
                                                            onBackClick = { currentAdminScreen = "home" },
                                                            onUpdateEquipment = { eq ->
                                                                coroutineScope.launch {
                                                                    repository.saveBioMedicalEquipment(eq)
                                                                }
                                                            }
                                                        )
                                                    }
                                                    "queue_oversight" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        val adminSummaries by adminQueueOversightViewModel.allDoctorSummaries.collectAsStateWithLifecycle()
                                                        val selectedDocId by adminQueueOversightViewModel.selectedDoctorId.collectAsStateWithLifecycle()
                                                        val selectedDocQueue by adminQueueOversightViewModel.selectedDoctorQueue.collectAsStateWithLifecycle()
                                                        QueueOversightScreen(
                                                            summaries = adminSummaries,
                                                            selectedDoctorId = selectedDocId,
                                                            selectedDoctorQueue = selectedDocQueue,
                                                            onSelectDoctor = { adminQueueOversightViewModel.selectDoctor(it) },
                                                            onClearSelectedDoctor = { adminQueueOversightViewModel.clearSelectedDoctor() },
                                                            onBackClick = { currentAdminScreen = "home" }
                                                        )
                                                    }
                                                    "facility_quality" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        AdminFacilityQualityScreen(
                                                            onNavigateBack = { currentAdminScreen = "home" }
                                                        )
                                                    }
                                                    "diagnostics" -> {
                                                        BackHandler { currentAdminScreen = "home" }
                                                        AdminDiagnosticsScreen(
                                                            onBackClick = { currentAdminScreen = "home" }
                                                        )
                                                    }
                                                    else -> {
                                                        BackHandler {
                                                            appStateHolder.logout()
                                                        }

                                                        AdminHomeScreen(
                                                            villages = villages,
                                                            notices = notices,
                                                            dispensaryStock = adminDispensaryStock,
                                                            onSendBroadcast = { title, message, village ->
                                                                adminViewModel.sendBroadcast(title, message, village)
                                                            },
                                                            onNavigateToDispensary = { currentAdminScreen = "dispensary_restock" },
                                                            onNavigateToDiseaseTrends = { currentAdminScreen = "disease_trends" },
                                                            onNavigateToIpdBeds = { currentAdminScreen = "ipd_beds" },
                                                            onNavigateToOtScheduler = { currentAdminScreen = "ot_scheduler" },
                                                            onNavigateToExternalReferrals = { currentAdminScreen = "referrals" },
                                                            onNavigateToBioMedical = { currentAdminScreen = "biomedical" },
                                                            onNavigateToQueueOversight = { currentAdminScreen = "queue_oversight" },
                                                            onNavigateToFacilityQuality = { currentAdminScreen = "facility_quality" },
                                                            onNavigateToDiagnostics = { currentAdminScreen = "diagnostics" }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
