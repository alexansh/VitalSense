package com.vitalsense.app.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import com.vitalsense.app.core.util.DismissedNoticeHelper
import com.vitalsense.app.core.ui.util.touchSpring
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource


@Composable
fun LoginScreen(
    currentLanguage: AppLanguage,
    onToggleLanguage: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit = {},
    onPatientLogin: (Patient) -> Unit,
    onAshaLogin: (AshaWorker) -> Unit,
    onDoctorLogin: (Doctor) -> Unit,
    onAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        ChangeLanguageDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { lang ->
                onSelectLanguage(lang)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    // expandedRole controls whether 4 title cards are shown (null) or a selected ID card is expanded
    var expandedRole by remember { mutableStateOf<UserRole?>(null) }
    var isSignUpMode by remember { mutableStateOf(false) }

    // Patient Form States
    var patientEmailOrPhone by remember { mutableStateOf("") }
    var patientPassword by remember { mutableStateOf("") }

    // Doctor Form States
    var doctorIdInput by remember { mutableStateOf("DOC-101") }
    var doctorPassword by remember { mutableStateOf("docpass123") }

    // ASHA Form States
    var ashaIdInput by remember { mutableStateOf("ASHA-401") }
    var ashaPinInput by remember { mutableStateOf("1234") }

    // Admin Form States
    var adminEmailInput by remember { mutableStateOf("admin@vitalsense.gov.in") }
    var adminPasswordInput by remember { mutableStateOf("adminpass") }

    var showAshaQrClaimDialog by remember { mutableStateOf(false) }

    val samplePatients = remember { SeedDataProvider.initialPatients }
    val sampleAshas = remember { SeedDataProvider.initialAshaWorkers }
    val sampleDoctors = remember { SeedDataProvider.initialDoctors }

    if (showAshaQrClaimDialog) {
        com.vitalsense.app.feature.auth.components.AshaQrClaimDialog(
            language = currentLanguage,
            onDismiss = { showAshaQrClaimDialog = false },
            onPatientClaimed = { claimedPatient ->
                onPatientLogin(claimedPatient)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        contentPadding = PaddingValues(top = Spacing.md, bottom = Spacing.xxl)
    ) {
        // 1. App Header & Quick Language Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(VS_Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🫀", fontSize = 20.sp)
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "VitalSense",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = VS_OnBackground
                            )
                            Surface(
                                shape = PillShape,
                                color = VS_Primary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "सेहतसेतु",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VS_Primary
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.tagline),
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    // Audio guidance chip
                    Surface(
                        onClick = {
                            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                            val speech = if (currentLanguage == AppLanguage.HINDI)
                                "नमस्ते। कृपया अपना लॉगिन रोल चुनें: मरीज़, डॉक्टर, आशा कार्यकर्ता या व्यवस्थापक।"
                            else
                                "Welcome to VitalSense. Please select your role to proceed: Patient, Doctor, ASHA Worker, or Administrator."
                            AudioGuidanceHelper.speak(context, speech, currentLanguage)
                        },
                        shape = PillShape,
                        color = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🔊", fontSize = 13.sp)
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "सुनें" else "Listen",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                        }
                    }

                    // Language selector pill
                    Surface(
                        onClick = { showLanguageDialog = true },
                        shape = PillShape,
                        color = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline),
                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🌐", fontSize = 13.sp)
                            Text(
                                text = currentLanguage.nativeName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                        }
                    }
                }
            }
        }

        // Animated Switch between 4-Role Title Cards and Expanded ID Card View
        item {
            AnimatedContent(
                targetState = expandedRole,
                transitionSpec = {
                    fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "RoleExpansionTransition"
            ) { activeRole ->
                if (activeRole == null) {
                    // INITIAL VIEW: 4 Title Cards for Role Selection
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        // Title & Prompt Header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.xs),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                        ) {
                            Text(
                                text = stringResource(R.string.whoIsUsing),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                ),
                                color = VS_OnBackground,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.selectRoleDesc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        // 4 Interactive Title Cards (2x2 Grid with high contrast elevation & badges)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            RoleTitleCard(
                                title = stringResource(R.string.rolePatient),
                                roleTag = stringResource(R.string.patientPortal),
                                subtitle = if (currentLanguage == AppLanguage.HINDI) "पर्चे, ओपीडी टोकन और अपॉइंटमेंट" else "Prescriptions, OPD tokens & consults",
                                avatarEmoji = "🧑",
                                accentColor = VS_Primary,
                                onClick = {
                                    AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                    expandedRole = UserRole.PATIENT
                                    isSignUpMode = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                            RoleTitleCard(
                                title = stringResource(R.string.roleDoctor),
                                roleTag = stringResource(R.string.doctorPortal),
                                subtitle = if (currentLanguage == AppLanguage.HINDI) "मरीज़ कतार, ई-पर्चे और नैदानिक समीक्षा" else "Patient queues, e-prescriptions & review",
                                avatarEmoji = "👨‍⚕️",
                                accentColor = VS_Primary,
                                onClick = {
                                    AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                    expandedRole = UserRole.DOCTOR
                                    isSignUpMode = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            RoleTitleCard(
                                title = stringResource(R.string.roleAsha),
                                roleTag = stringResource(R.string.ashaPortal),
                                subtitle = if (currentLanguage == AppLanguage.HINDI) "घर-घर स्वास्थ्य सर्वे, ऑफलाइन सिंक व SOS" else "Door-to-door vitals, offline sync & SOS",
                                avatarEmoji = "🩺",
                                accentColor = VS_Warning,
                                onClick = {
                                    AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                    expandedRole = UserRole.ASHA
                                    isSignUpMode = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                            RoleTitleCard(
                                title = stringResource(R.string.roleAdmin),
                                roleTag = stringResource(R.string.adminPortal),
                                subtitle = if (currentLanguage == AppLanguage.HINDI) "रोग निगरानी, बिस्तर/OT डेस्क व दवा पुनःपूर्ति" else "Surveillance, bed/OT roster & restock",
                                avatarEmoji = "🛡️",
                                accentColor = VS_Error,
                                onClick = {
                                    AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                    expandedRole = UserRole.ADMIN
                                    isSignUpMode = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Bottom Help & Zero-Internet Indicator
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing.xs),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = PillShape,
                                color = VS_SuccessContainer,
                                border = BorderStroke(1.dp, VS_Success.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("⚡", fontSize = 11.sp)
                                    Text(
                                        text = stringResource(R.string.offline),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnSuccessContainer
                                    )
                                }
                            }

                            Surface(
                                onClick = {
                                    com.vitalsense.app.core.util.EmergencySosHelper.dialEmergencyCall(context, "108")
                                },
                                shape = PillShape,
                                color = VS_ErrorContainer,
                                border = BorderStroke(1.dp, VS_Error.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("📞", fontSize = 11.sp)
                                    Text(
                                        text = "108 Ambulance",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_Error
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // DYNAMICALLY EXPANDED ID CARD VIEW
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        // Change Role Back Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                    expandedRole = null
                                },
                                shape = PillShape,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = VS_Primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (currentLanguage == AppLanguage.HINDI) "← भूमिका बदलें" else "← Change Role",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_Primary
                                    )
                                }
                            }

                            Surface(
                                shape = PillShape,
                                color = when (activeRole) {
                                    UserRole.PATIENT -> VS_Primary.copy(alpha = 0.12f)
                                    UserRole.DOCTOR -> VS_Primary.copy(alpha = 0.12f)
                                    UserRole.ASHA -> VS_Warning.copy(alpha = 0.12f)
                                    UserRole.ADMIN -> VS_Error.copy(alpha = 0.12f)
                                }
                            ) {
                                Text(
                                    text = when (activeRole) {
                                        UserRole.PATIENT -> "PATIENT ID"
                                        UserRole.DOCTOR -> "DOCTOR ID"
                                        UserRole.ASHA -> "ASHA CREDENTIAL"
                                        UserRole.ADMIN -> "ADMINISTRATOR ID"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = when (activeRole) {
                                        UserRole.PATIENT -> VS_Primary
                                        UserRole.DOCTOR -> VS_Primary
                                        UserRole.ASHA -> VS_Warning
                                        UserRole.ADMIN -> VS_Error
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Physical Credential ID Card Container (Horizontal Layout: Left ID Badge, Right Sign-In)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.08f)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = VS_Surface),
                            border = BorderStroke(1.5.dp, VS_Outline)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Top Lanyard Hole Accent
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 36.dp, height = 6.dp)
                                            .clip(PillShape)
                                            .background(VS_Outline)
                                    )
                                }

                                // Main Horizontal ID Split
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // LEFT COLUMN: Physical Credential ID Badge
                                    PhysicalCredentialBadge(
                                        role = activeRole,
                                        language = currentLanguage,
                                        samplePatient = samplePatients.first(),
                                        sampleDoctor = sampleDoctors.first(),
                                        sampleAsha = sampleAshas.first(),
                                        modifier = Modifier.weight(0.38f)
                                    )

                                    // Vertical Divider
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .fillMaxHeight()
                                            .background(VS_Outline)
                                    )

                                    // RIGHT COLUMN: Role-Specific Sign-In Options (On the same horizontal level)
                                    Column(
                                        modifier = Modifier.weight(0.62f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        when (activeRole) {
                                            UserRole.PATIENT -> {
                                                PatientSignInOptions(
                                                    language = currentLanguage,
                                                    emailOrPhone = patientEmailOrPhone,
                                                    onEmailOrPhoneChange = { patientEmailOrPhone = it },
                                                    password = patientPassword,
                                                    onPasswordChange = { patientPassword = it },
                                                    isSignUpMode = isSignUpMode,
                                                    onToggleSignUp = { isSignUpMode = !isSignUpMode },
                                                    onSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onPatientLogin(samplePatients.first())
                                                    },
                                                    onGoogleSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onPatientLogin(samplePatients.first())
                                                    },
                                                    onDemoSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onPatientLogin(samplePatients.first())
                                                    },
                                                    onScanQrClaim = { showAshaQrClaimDialog = true }
                                                )
                                            }

                                            UserRole.DOCTOR -> {
                                                DoctorSignInOptions(
                                                    language = currentLanguage,
                                                    doctorId = doctorIdInput,
                                                    onDoctorIdChange = { doctorIdInput = it },
                                                    password = doctorPassword,
                                                    onPasswordChange = { doctorPassword = it },
                                                    onSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onDoctorLogin(sampleDoctors.first())
                                                    },
                                                    onDemoSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onDoctorLogin(sampleDoctors.first())
                                                    }
                                                )
                                            }

                                            UserRole.ASHA -> {
                                                AshaSignInOptions(
                                                    language = currentLanguage,
                                                    ashaId = ashaIdInput,
                                                    onAshaIdChange = { ashaIdInput = it },
                                                    pin = ashaPinInput,
                                                    onPinChange = { ashaPinInput = it },
                                                    onSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onAshaLogin(sampleAshas.first())
                                                    },
                                                    onDemoSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onAshaLogin(sampleAshas.first())
                                                    }
                                                )
                                            }

                                            UserRole.ADMIN -> {
                                                AdminSignInOptions(
                                                    language = currentLanguage,
                                                    email = adminEmailInput,
                                                    onEmailChange = { adminEmailInput = it },
                                                    password = adminPasswordInput,
                                                    onPasswordChange = { adminPasswordInput = it },
                                                    isSignUpMode = isSignUpMode,
                                                    onToggleSignUp = { isSignUpMode = !isSignUpMode },
                                                    onSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onAdminLogin()
                                                    },
                                                    onDemoSignIn = {
                                                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                                                        DismissedNoticeHelper.clearDismissedAdvisories(context)
                                                        onAdminLogin()
                                                    }
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

// -------------------------------------------------------------------------------------
// 1. Role Title Card (Initial 4-Card Selector)
// -------------------------------------------------------------------------------------
@Composable
private fun RoleTitleCard(
    title: String,
    roleTag: String,
    subtitle: String,
    avatarEmoji: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
            .clickable(onClick = onClick)
            .touchSpring(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = VS_Surface),
        border = BorderStroke(1.dp, VS_Outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = avatarEmoji, fontSize = 24.sp)
                }

                Surface(
                    shape = PillShape,
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = roleTag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = VS_OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enter →",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = accentColor
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------
// 2. Physical Credential ID Badge (Left Column of Expanded View)
// -------------------------------------------------------------------------------------
@Composable
private fun PhysicalCredentialBadge(
    role: UserRole,
    language: AppLanguage,
    samplePatient: Patient,
    sampleDoctor: Doctor,
    sampleAsha: AshaWorker,
    modifier: Modifier = Modifier
) {
    val accentColor = when (role) {
        UserRole.PATIENT -> VS_Primary
        UserRole.DOCTOR -> VS_Primary
        UserRole.ASHA -> VS_Warning
        UserRole.ADMIN -> VS_Error
    }

    val badgeName = when (role) {
        UserRole.PATIENT -> samplePatient.name
        UserRole.DOCTOR -> sampleDoctor.name
        UserRole.ASHA -> sampleAsha.name
        UserRole.ADMIN -> "Dr. V. K. Gupta"
    }

    val badgeId = when (role) {
        UserRole.PATIENT -> "ABHA-${samplePatient.id.uppercase()}"
        UserRole.DOCTOR -> sampleDoctor.specialty.displayName
        UserRole.ASHA -> sampleAsha.ashaUniqueId
        UserRole.ADMIN -> "DIRECTOR HEALTH"
    }

    val avatarEmoji = when (role) {
        UserRole.PATIENT -> "🧑"
        UserRole.DOCTOR -> "👨‍⚕️"
        UserRole.ASHA -> "🩺"
        UserRole.ADMIN -> "🛡️"
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        color = VS_SurfaceVariant,
        border = BorderStroke(1.dp, VS_Outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Official Seal Label
            Text(
                text = "SMART HEALTH ID",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 8.5.sp,
                    letterSpacing = 0.5.sp
                ),
                color = VS_OnSurfaceVariant
            )

            // Avatar Frame with Verified Badge
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(accentColor.copy(alpha = 0.2f), accentColor.copy(alpha = 0.05f))
                            )
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = avatarEmoji, fontSize = 30.sp)
                }
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(VS_Success),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Credential Name & Role
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = badgeName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                    color = VS_OnBackground,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Surface(
                    shape = PillShape,
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = when (role) {
                            UserRole.PATIENT -> "PATIENT"
                            UserRole.DOCTOR -> "CHIEF DOCTOR"
                            UserRole.ASHA -> "ASHA LEAD"
                            UserRole.ADMIN -> "ADMIN"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 8.sp),
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = badgeId,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontFamily = FontFamily.Monospace),
                    color = VS_OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Simulated Barcode / Microchip Graphic
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(14) { index ->
                        Box(
                            modifier = Modifier
                                .width(if (index % 3 == 0) 2.5.dp else 1.2.dp)
                                .height(16.dp)
                                .background(VS_OnSurfaceVariant.copy(alpha = 0.6f))
                        )
                    }
                }
                Text(
                    text = "SECURE VERIFIED",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.5.sp, fontWeight = FontWeight.Bold),
                    color = VS_OnSuccessContainer
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------
// 3. Role-Specific Sign-In Options (Right Column)
// -------------------------------------------------------------------------------------

// --- Patient Options ---
@Composable
private fun PatientSignInOptions(
    language: AppLanguage,
    emailOrPhone: String,
    onEmailOrPhoneChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isSignUpMode: Boolean,
    onToggleSignUp: () -> Unit,
    onSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onDemoSignIn: () -> Unit,
    onScanQrClaim: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = if (isSignUpMode) "Patient Registration" else "Patient Sign In",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = VS_OnBackground
        )

        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = onEmailOrPhoneChange,
            label = { Text(if (language == AppLanguage.HINDI) "मोबाइल / ईमेल" else "Mobile or Email", fontSize = 11.sp) },
            placeholder = { Text("9811122334 / patient@vitalsense.org", fontSize = 11.sp) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(if (language == AppLanguage.HINDI) "पासवर्ड" else "Password", fontSize = 11.sp) },
            placeholder = { Text("••••••••", fontSize = 11.sp) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = if (isSignUpMode) "Create Patient Account" else "Sign In with Password",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Google Sign In
        OutlinedButton(
            onClick = onGoogleSignIn,
            shape = PillShape,
            border = BorderStroke(1.dp, VS_Outline),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = VS_Surface),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("🌐", fontSize = 14.sp)
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            }
        }

        // Instant Demo Sign In (1-Tap)
        Button(
            onClick = onDemoSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Text(
                text = "⚡ Instant Demo Sign In",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // QR Claim Button
        OutlinedButton(
            onClick = onScanQrClaim,
            shape = PillShape,
            border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().height(36.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "🪪 Scan ASHA Card (QR Claim)",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = VS_Primary
            )
        }

        // Sign Up Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSignUpMode) "Already have an account? " else "New to VitalSense? ",
                style = MaterialTheme.typography.labelSmall,
                color = VS_OnSurfaceVariant
            )
            Text(
                text = if (isSignUpMode) "Sign In" else "Sign Up",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = VS_Primary,
                modifier = Modifier.clickable(onClick = onToggleSignUp)
            )
        }
    }
}

// --- Doctor Options ---
@Composable
private fun DoctorSignInOptions(
    language: AppLanguage,
    doctorId: String,
    onDoctorIdChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onDemoSignIn: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Doctor Consultation Desk",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = VS_OnBackground
        )

        OutlinedTextField(
            value = doctorId,
            onValueChange = onDoctorIdChange,
            label = { Text("Unique Doctor ID", fontSize = 11.sp) },
            placeholder = { Text("e.g. DOC-101", fontSize = 11.sp) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password", fontSize = 11.sp) },
            placeholder = { Text("••••••••", fontSize = 11.sp) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = "Sign In with Doctor ID",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Instant Demo Sign In (1-Tap)
        Button(
            onClick = onDemoSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Text(
                text = "⚡ Instant Demo Sign In",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

// --- ASHA Options ---
@Composable
private fun AshaSignInOptions(
    language: AppLanguage,
    ashaId: String,
    onAshaIdChange: (String) -> Unit,
    pin: String,
    onPinChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onDemoSignIn: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "ASHA Field Worker Desk",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = VS_OnBackground
        )

        OutlinedTextField(
            value = ashaId,
            onValueChange = onAshaIdChange,
            label = { Text("Unique ASHA ID", fontSize = 11.sp) },
            placeholder = { Text("e.g. ASHA-401", fontSize = 11.sp) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pin,
            onValueChange = onPinChange,
            label = { Text("PIN / Passcode", fontSize = 11.sp) },
            placeholder = { Text("••••", fontSize = 11.sp) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Warning),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = "Sign In with ASHA ID",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Instant Demo Sign In (1-Tap)
        Button(
            onClick = onDemoSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Text(
                text = "⚡ Instant Demo Sign In",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

// --- Admin Options ---
@Composable
private fun AdminSignInOptions(
    language: AppLanguage,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isSignUpMode: Boolean,
    onToggleSignUp: () -> Unit,
    onSignIn: () -> Unit,
    onDemoSignIn: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = if (isSignUpMode) "Request Admin Access" else "District Command Login",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = VS_OnBackground
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Official Gov Email", fontSize = 11.sp) },
            placeholder = { Text("admin@vitalsense.gov.in", fontSize = 11.sp) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Passcode", fontSize = 11.sp) },
            placeholder = { Text("••••••••", fontSize = 11.sp) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = if (isSignUpMode) "Submit Access Request" else "Sign In to Command",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Instant Demo Sign In (1-Tap)
        Button(
            onClick = onDemoSignIn,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Text(
                text = "⚡ Instant Demo Sign In",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        // Sign Up Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSignUpMode) "Already have admin access? " else "Need official access? ",
                style = MaterialTheme.typography.labelSmall,
                color = VS_OnSurfaceVariant
            )
            Text(
                text = if (isSignUpMode) "Sign In" else "Request Access",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = VS_Error,
                modifier = Modifier.clickable(onClick = onToggleSignUp)
            )
        }
    }
}
