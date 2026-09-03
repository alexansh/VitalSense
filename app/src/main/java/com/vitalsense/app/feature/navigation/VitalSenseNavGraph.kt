package com.vitalsense.app.feature.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import com.vitalsense.app.core.state.AppStateHolder
import com.vitalsense.app.core.ui.components.TopRoleSwitcherBar
import com.vitalsense.app.feature.admin.*
import com.vitalsense.app.feature.asha.*
import com.vitalsense.app.feature.auth.LoginScreen
import com.vitalsense.app.feature.doctor.*
import com.vitalsense.app.feature.patient.*
import com.vitalsense.app.feature.patient.mentalhealth.MentalWellnessScreen
import kotlinx.coroutines.launch

private enum class PatientSubScreen {
    HOME, HEALTH_CARD, CONDITION_ENTRY, PRESCRIPTIONS, APPOINTMENTS, DOCTOR_MAP, SCHEMES, MENTAL_WELLNESS, OCR, MANUAL, DEPARTMENTS
}

private enum class AshaSubScreen {
    HOME, REGISTRATION, CHAT, BROADCAST
}

private enum class DoctorSubScreen {
    HOME, CASE_DETAIL, PENDING_CASES, PRESCRIPTION_CREATOR, DISPENSARY_STOCK, APPOINTMENTS,
    INCOMING_REFERRALS, REFERRAL_HISTORY, CREATE_REFERRAL, SERVICE_REPORT, PATIENT_HISTORY
}

private enum class AdminSubScreen {
    HOME, VILLAGE_LIST, OUTBREAK_GRID, BROADCAST, ACCOUNTS, DEPARTMENTS
}

@Composable
fun VitalSenseNavGraph(
    appStateHolder: AppStateHolder,
    repository: VitalSenseRepository,
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel = hiltViewModel(),
    patientViewModel: PatientViewModel = hiltViewModel(),
    doctorViewModel: DoctorViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val isLoggedIn by appStateHolder.isLoggedIn.collectAsStateWithLifecycle()
    val currentRole by appStateHolder.currentRole.collectAsStateWithLifecycle()
    val activePatient by appStateHolder.activePatient.collectAsStateWithLifecycle()
    val activeAsha by appStateHolder.activeAsha.collectAsStateWithLifecycle()
    val activeDoctor by appStateHolder.activeDoctor.collectAsStateWithLifecycle()
    val activeProxyPatient by appStateHolder.activeProxyPatient.collectAsStateWithLifecycle()
    val isOffline by appStateHolder.isOffline.collectAsStateWithLifecycle()

    // Sub-screen navigation states
    var patientSubScreen by remember { mutableStateOf(PatientSubScreen.HOME) }
    var selectedConditionCategory by remember { mutableStateOf(ConditionCategory.GENERAL_MEDICINE) }

    var ashaSubScreen by remember { mutableStateOf(AshaSubScreen.HOME) }
    var selectedChatPatient by remember { mutableStateOf<Patient?>(null) }

    var doctorSubScreen by remember { mutableStateOf(DoctorSubScreen.HOME) }
    var adminSubScreen by remember { mutableStateOf(AdminSubScreen.HOME) }
    
    var selectedReferral by remember { mutableStateOf<Referral?>(null) }
    var selectedHistoryPatientId by remember { mutableStateOf<String?>(null) }

    // Scoped data streams
    val doctorCases by doctorViewModel.scopedCases.collectAsStateWithLifecycle()
    val doctorAppointments by doctorViewModel.appointments.collectAsStateWithLifecycle()
    val doctorDispensaryStock by doctorViewModel.dispensaryStock.collectAsStateWithLifecycle()
    val selectedDoctorCase by doctorViewModel.selectedCase.collectAsStateWithLifecycle()
    val patientPrescriptions by doctorViewModel.patientPrescriptions.collectAsStateWithLifecycle()
    val patientProfile by doctorViewModel.patientProfile.collectAsStateWithLifecycle()

    val villages by repository.getVillages().collectAsStateWithLifecycle(initialValue = emptyList())
    val patients by repository.getPatients().collectAsStateWithLifecycle(initialValue = emptyList())
    val notices by repository.getNotices().collectAsStateWithLifecycle(initialValue = emptyList())
    val prescriptions by repository.getPrescriptionsForPatient(activePatient.id).collectAsStateWithLifecycle(initialValue = emptyList())
    val appointments by repository.getAppointmentsForPatient(activePatient.id).collectAsStateWithLifecycle(initialValue = emptyList())
    val doctors by repository.getDoctors().collectAsStateWithLifecycle(initialValue = emptyList())
    val schemes by repository.getGovernmentSchemes().collectAsStateWithLifecycle(initialValue = emptyList())

    val effectivePatient = activeProxyPatient ?: activePatient

    val activeUserName = when (currentRole) {
        UserRole.PATIENT -> effectivePatient.name
        UserRole.ASHA -> activeAsha.name
        UserRole.DOCTOR -> activeDoctor.name
        UserRole.ADMIN -> "District CMO (Rampur)"
    }

    AnimatedContent(
        targetState = isLoggedIn,
        label = "AuthTransition"
    ) { loggedIn ->
        if (!loggedIn) {
            val authViewModel: com.vitalsense.app.feature.auth.AuthViewModel = hiltViewModel()

            LoginScreen(
                onPatientLogin = { selectedPatient ->
                    authViewModel.signInAnonymously(UserRole.PATIENT, selectedPatient.id) { success ->
                        if (success) {
                            coroutineScope.launch {
                                appStateHolder.loginAsPatient(selectedPatient)
                            }
                        }
                    }
                },
                onAshaLogin = { selectedAsha ->
                    authViewModel.signInAnonymously(UserRole.ASHA, selectedAsha.id) { success ->
                        if (success) {
                            coroutineScope.launch {
                                appStateHolder.loginAsAsha(selectedAsha)
                            }
                        }
                    }
                },
                onDoctorLogin = { selectedDoctor ->
                    authViewModel.signInAnonymously(UserRole.DOCTOR, selectedDoctor.id) { success ->
                        if (success) {
                            coroutineScope.launch {
                                appStateHolder.loginAsDoctor(selectedDoctor)
                            }
                        }
                    }
                },
                onAdminLogin = {
                    authViewModel.signInAnonymously(UserRole.ADMIN, "admin") { success ->
                        if (success) {
                            coroutineScope.launch {
                                appStateHolder.loginAsAdmin()
                            }
                        }
                    }
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
                        onToggleOffline = {
                            appStateHolder.toggleOffline()
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentRole) {
                        UserRole.PATIENT -> {
                            when (patientSubScreen) {
                                PatientSubScreen.HOME -> {
                                    if (activeProxyPatient != null) {
                                        BackHandler {
                                            appStateHolder.clearProxy()
                                            appStateHolder.switchRole(UserRole.ASHA)
                                        }
                                    } else {
                                        BackHandler { appStateHolder.logout() }
                                    }

                                    PatientHomeScreen(
                                        patient = effectivePatient,
                                        onCategoryClick = { category ->
                                            if (category == ConditionCategory.MENTAL_HEALTH) {
                                                patientSubScreen = PatientSubScreen.MENTAL_WELLNESS
                                            } else {
                                                selectedConditionCategory = category
                                                patientSubScreen = PatientSubScreen.CONDITION_ENTRY
                                            }
                                        },
                                        onViewHealthCard = { patientSubScreen = PatientSubScreen.HEALTH_CARD },
                                        onViewPrescriptions = { patientSubScreen = PatientSubScreen.PRESCRIPTIONS },
                                        onViewAppointments = { patientSubScreen = PatientSubScreen.APPOINTMENTS },
                                        onViewDoctorMap = { patientSubScreen = PatientSubScreen.DOCTOR_MAP },
                                        onViewSchemes = { patientSubScreen = PatientSubScreen.SCHEMES },
                                        onViewOcr = { patientSubScreen = PatientSubScreen.OCR },
                                        onViewManual = { patientSubScreen = PatientSubScreen.MANUAL },
                                        onTriggerSos = {
                                            coroutineScope.launch {
                                                repository.triggerEmergencySos(effectivePatient, null, null)
                                            }
                                        }
                                    )
                                }

                                PatientSubScreen.HEALTH_CARD -> {
                                    HealthCardViewerScreen(
                                        patient = effectivePatient,
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.CONDITION_ENTRY -> {
                                    ConditionEntryScreen(
                                        patientId = effectivePatient.id,
                                        patientName = effectivePatient.name,
                                        villageId = effectivePatient.villageId,
                                        villageName = effectivePatient.villageName,
                                        initialCategory = selectedConditionCategory,
                                        onLogCondition = { record ->
                                            coroutineScope.launch { repository.logCondition(record) }
                                        },
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.PRESCRIPTIONS -> {
                                    PrescriptionsListScreen(
                                        prescriptions = prescriptions,
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.APPOINTMENTS -> {
                                    AppointmentsScreen(
                                        appointments = appointments,
                                        patient = effectivePatient,
                                        onProposeAppointment = { appt ->
                                            coroutineScope.launch { repository.scheduleAppointment(appt) }
                                        },
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.DOCTOR_MAP -> {
                                    DoctorMapListScreen(
                                        doctors = doctors,
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.SCHEMES -> {
                                    SchemesBrowserScreen(
                                        schemes = schemes,
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.MENTAL_WELLNESS -> {
                                    MentalWellnessScreen(
                                        patient = effectivePatient,
                                        onLogMood = { notes, severity ->
                                            patientViewModel.logMentalWellness(
                                                patient = effectivePatient,
                                                moodNotes = notes,
                                                severityLevel = severity,
                                                isProxy = activeProxyPatient != null
                                            )
                                        },
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.OCR -> {
                                    PrescriptionOcrScreen(
                                        onSavePrescriptionText = { text ->
                                            coroutineScope.launch {
                                                repository.logCondition(
                                                    ConditionRecord(
                                                        id = java.util.UUID.randomUUID().toString(),
                                                        patientId = effectivePatient.id,
                                                        patientName = effectivePatient.name,
                                                        villageId = effectivePatient.villageId,
                                                        villageName = effectivePatient.villageName,
                                                        category = ConditionCategory.GENERAL_MEDICINE,
                                                        severity = SeverityLevel.LOW,
                                                        requestedDoctorType = DoctorSpecialty.GENERAL_PHYSICIAN,
                                                        notes = "Scanned Rx OCR: $text",
                                                        timestamp = System.currentTimeMillis()
                                                    )
                                                )
                                            }
                                        },
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.MANUAL -> {
                                    FullManualScreen(
                                        onBack = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }

                                PatientSubScreen.DEPARTMENTS -> {
                                    val activeDepts by doctorViewModel.departments.collectAsStateWithLifecycle()
                                    DepartmentPickerScreen(
                                        departments = activeDepts,
                                        onDepartmentSelected = { dept ->
                                            // Optional: Launch intent or create appointment for that dept
                                            patientSubScreen = PatientSubScreen.HOME
                                        },
                                        onBackClick = { patientSubScreen = PatientSubScreen.HOME }
                                    )
                                }
                            }
                        }

                        UserRole.ASHA -> {
                            when (ashaSubScreen) {
                                AshaSubScreen.HOME -> {
                                    BackHandler { appStateHolder.logout() }

                                    AshaHomeScreen(
                                        asha = activeAsha,
                                        patients = patients.filter { it.ashaWorkerId == activeAsha.id || it.villageName in activeAsha.assignedVillages },
                                        notices = notices,
                                        onSelectProxyPatient = { selectedPatient ->
                                            appStateHolder.setProxyPatient(selectedPatient)
                                            appStateHolder.switchRole(UserRole.PATIENT)
                                        },
                                        onRegisterPatientClick = { ashaSubScreen = AshaSubScreen.REGISTRATION },
                                        onSendNoticeClick = { ashaSubScreen = AshaSubScreen.BROADCAST },
                                        onOpenPatientChat = { target ->
                                            selectedChatPatient = target
                                            ashaSubScreen = AshaSubScreen.CHAT
                                        }
                                    )
                                }

                                AshaSubScreen.REGISTRATION -> {
                                    PatientRegistrationScreen(
                                        ashaId = activeAsha.id,
                                        ashaName = activeAsha.name,
                                        onSavePatient = { newPatient ->
                                            coroutineScope.launch { repository.savePatient(newPatient) }
                                        },
                                        onBack = { ashaSubScreen = AshaSubScreen.HOME }
                                    )
                                }

                                AshaSubScreen.CHAT -> {
                                    val target = selectedChatPatient ?: patients.firstOrNull() ?: effectivePatient
                                    AshaPatientChatScreen(
                                        patient = target,
                                        ashaName = activeAsha.name,
                                        onBack = { ashaSubScreen = AshaSubScreen.HOME }
                                    )
                                }

                                AshaSubScreen.BROADCAST -> {
                                    BroadcastNoticesScreen(
                                        ashaId = activeAsha.id,
                                        ashaName = activeAsha.name,
                                        onSendNotice = { notice ->
                                            coroutineScope.launch { repository.sendNotice(notice) }
                                        },
                                        onBack = { ashaSubScreen = AshaSubScreen.HOME }
                                    )
                                }
                            }
                        }

                        UserRole.DOCTOR -> {
                            val activeCase = selectedDoctorCase
                            if (activeCase != null) {
                                BackHandler { doctorViewModel.clearSelectedCase() }

                                CaseDetailScreen(
                                    record = activeCase,
                                    patient = patientProfile,
                                    priorPrescriptions = patientPrescriptions,
                                    dispensaryStock = doctorDispensaryStock,
                                    currentDoctor = activeDoctor,
                                    onBack = { doctorViewModel.clearSelectedCase() },
                                    onSubmitResponse = { responseText, privateNotes ->
                                        doctorViewModel.submitMedicalResponse(
                                            caseId = activeCase.id,
                                            responseText = responseText,
                                            privateNotes = privateNotes
                                        )
                                    },
                                    onIssuePrescription = { medicines, instructions ->
                                        doctorViewModel.issuePrescription(
                                            caseId = activeCase.id,
                                            patientId = activeCase.patientId,
                                            patientName = activeCase.patientName,
                                            medicines = medicines,
                                            instructions = instructions
                                        )
                                    },
                                    onProposeAppointment = { date, timeSlot ->
                                        doctorViewModel.proposeAppointment(
                                            patientId = activeCase.patientId,
                                            patientName = activeCase.patientName,
                                            dateFormatted = date,
                                            timeSlot = timeSlot
                                        )
                                    },
                                    onReferCase = {
                                        doctorSubScreen = DoctorSubScreen.CREATE_REFERRAL
                                    }
                                )
                            } else {
                                when (doctorSubScreen) {
                                    DoctorSubScreen.HOME -> {
                                        BackHandler { appStateHolder.logout() }

                                        DoctorHomeScreen(
                                            doctor = activeDoctor,
                                            cases = doctorCases,
                                            appointments = doctorAppointments,
                                            dispensaryStock = doctorDispensaryStock,
                                            patients = patients,
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
                                            onViewIncomingReferrals = { doctorSubScreen = DoctorSubScreen.INCOMING_REFERRALS },
                                            onViewSentReferrals = { doctorSubScreen = DoctorSubScreen.REFERRAL_HISTORY }
                                        )
                                    }

                                    DoctorSubScreen.PENDING_CASES -> {
                                        PendingCasesScreen(
                                            cases = doctorCases,
                                            onSelectCase = { record -> doctorViewModel.selectCase(record) },
                                            onBack = { doctorSubScreen = DoctorSubScreen.HOME }
                                        )
                                    }

                                    DoctorSubScreen.PRESCRIPTION_CREATOR -> {
                                        PrescriptionCreatorScreen(
                                            patientId = effectivePatient.id,
                                            patientName = effectivePatient.name,
                                            doctor = activeDoctor,
                                            onIssuePrescription = { medicines, instructions ->
                                                doctorViewModel.issuePrescription(
                                                    caseId = "case-01",
                                                    patientId = effectivePatient.id,
                                                    patientName = effectivePatient.name,
                                                    medicines = medicines,
                                                    instructions = instructions
                                                )
                                            },
                                            onBack = { doctorSubScreen = DoctorSubScreen.HOME }
                                        )
                                    }

                                    DoctorSubScreen.DISPENSARY_STOCK -> {
                                        com.vitalsense.app.feature.doctor.DispensaryStockScreen(
                                            stockList = doctorDispensaryStock,
                                            onBack = { doctorSubScreen = DoctorSubScreen.HOME }
                                        )
                                    }

                                    DoctorSubScreen.APPOINTMENTS -> {
                                        AppointmentConfirmationScreen(
                                            appointments = doctorAppointments,
                                            onAcceptAppointment = { doctorViewModel.acceptAppointment(it.id) },
                                            onDeclineAppointment = { doctorViewModel.declineAppointment(it.id) },
                                            onBack = { doctorSubScreen = DoctorSubScreen.HOME }
                                        )
                                    }

                                    DoctorSubScreen.INCOMING_REFERRALS -> {
                                        IncomingReferralsScreen(
                                            viewModel = doctorViewModel,
                                            onBackClick = { doctorSubScreen = DoctorSubScreen.HOME },
                                            onAcceptReferral = { referral ->
                                                selectedHistoryPatientId = referral.patientId
                                                doctorSubScreen = DoctorSubScreen.PATIENT_HISTORY
                                            }
                                        )
                                    }

                                    DoctorSubScreen.REFERRAL_HISTORY -> {
                                        ReferralHistoryScreen(
                                            viewModel = doctorViewModel,
                                            onBackClick = { doctorSubScreen = DoctorSubScreen.HOME },
                                            onViewReport = { referral ->
                                                // Normally view read-only report, but here we can just skip or reuse service report
                                            }
                                        )
                                    }

                                    DoctorSubScreen.CREATE_REFERRAL -> {
                                        ReferralCreatorScreen(
                                            viewModel = doctorViewModel,
                                            patientId = effectivePatient.id,
                                            patientName = effectivePatient.name,
                                            caseId = "case-new",
                                            onBackClick = { doctorSubScreen = DoctorSubScreen.HOME },
                                            onReferralCreated = { doctorSubScreen = DoctorSubScreen.REFERRAL_HISTORY }
                                        )
                                    }

                                    DoctorSubScreen.SERVICE_REPORT -> {
                                        selectedReferral?.let { ref ->
                                            ServiceReportScreen(
                                                viewModel = doctorViewModel,
                                                referral = ref,
                                                onBackClick = { doctorSubScreen = DoctorSubScreen.INCOMING_REFERRALS },
                                                onSubmitComplete = { doctorSubScreen = DoctorSubScreen.HOME }
                                            )
                                        }
                                    }

                                    DoctorSubScreen.PATIENT_HISTORY -> {
                                        selectedHistoryPatientId?.let { patId ->
                                            PatientHistoryScreen(
                                                viewModel = doctorViewModel,
                                                patientId = patId,
                                                onBackClick = { doctorSubScreen = DoctorSubScreen.INCOMING_REFERRALS }
                                            )
                                        }
                                    }

                                    DoctorSubScreen.CASE_DETAIL -> {
                                        doctorSubScreen = DoctorSubScreen.HOME
                                    }
                                }
                            }
                        }

                        UserRole.ADMIN -> {
                            when (adminSubScreen) {
                                AdminSubScreen.HOME -> {
                                    BackHandler { appStateHolder.logout() }

                                    AdminHomeScreen(
                                        villages = villages,
                                        notices = notices,
                                        onSendBroadcast = { title, message, village ->
                                            adminViewModel.sendBroadcast(
                                                title = title,
                                                message = message,
                                                targetVillage = village
                                            )
                                        },
                                        onViewVillages = { adminSubScreen = AdminSubScreen.VILLAGE_LIST },
                                        onViewOutbreakGrid = { adminSubScreen = AdminSubScreen.OUTBREAK_GRID },
                                        onViewBroadcast = { adminSubScreen = AdminSubScreen.BROADCAST },
                                        onViewAccounts = { adminSubScreen = AdminSubScreen.ACCOUNTS }
                                    )
                                }

                                AdminSubScreen.VILLAGE_LIST -> {
                                    VillageListScreen(
                                        villages = villages,
                                        onAddVillage = { v -> coroutineScope.launch { repository.addVillage(v) } },
                                        onBack = { adminSubScreen = AdminSubScreen.HOME }
                                    )
                                }

                                AdminSubScreen.OUTBREAK_GRID -> {
                                    VillageOutbreakGridScreen(
                                        villages = villages,
                                        onBack = { adminSubScreen = AdminSubScreen.HOME }
                                    )
                                }

                                AdminSubScreen.BROADCAST -> {
                                    AdminBroadcastScreen(
                                        onSendBroadcast = { notice ->
                                            coroutineScope.launch { repository.sendNotice(notice) }
                                        },
                                        onBack = { adminSubScreen = AdminSubScreen.HOME }
                                    )
                                }

                                AdminSubScreen.ACCOUNTS -> {
                                    ReviewAccountsScreen(
                                        onBack = { adminSubScreen = AdminSubScreen.HOME }
                                    )
                                }

                                AdminSubScreen.DEPARTMENTS -> {
                                    AdminDepartmentScreen(
                                        viewModel = adminViewModel,
                                        onBackClick = { adminSubScreen = AdminSubScreen.HOME }
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

