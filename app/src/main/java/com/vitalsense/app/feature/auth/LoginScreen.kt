package com.vitalsense.app.feature.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun LoginScreen(
    onPatientLogin: (Patient) -> Unit,
    onAshaLogin: (AshaWorker) -> Unit,
    onDoctorLogin: (Doctor) -> Unit,
    onAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf(UserRole.PATIENT) }
    var selectedLanguage by remember { mutableStateOf("English") }

    // Form inputs
    var phoneInput by remember { mutableStateOf("") }
    var ashaIdInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var doctorEmailInput by remember { mutableStateOf("") }
    var doctorPasswordInput by remember { mutableStateOf("") }
    var adminPasscodeInput by remember { mutableStateOf("") }

    val samplePatients = remember { 
        listOf(
            Patient("demo_patient_1", "Ramesh Kumar", 45, "Male", "9811100000", "vil_1", "Rampur", "asha_1", "Sita Devi", SeverityLevel.MODERATE, ConditionCategory.com.vitalsense.app.core.data.model.ConditionCategory.GENERAL_FEVER, null, null, "9811122222", null)
        ) 
    }
    val sampleAshas = remember { 
        listOf(
            AshaWorker("demo_asha_1", "Sita Devi", "ASHA-7701", listOf("Rampur", "Shantipur"), 45, "9988776655")
        ) 
    }
    val sampleDoctors = remember { 
        listOf(
            Doctor("demo_doc_1", "Dr. Rajesh Sharma", DoctorSpecialty.GENERAL_PHYSICIAN, "9876543210", "dr.rajesh@vitalsense.org")
        ) 
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 28.dp, bottom = 40.dp)
    ) {
        // 1. App Header & Language Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(LimePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "V",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = TextPrimaryNearBlack
                        )
                    }
                    Column {
                        Text(
                            text = "VitalSense",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "SehatSetu — Rural Health Bridge",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                }

                // Language toggle pill
                Surface(
                    shape = PillShape,
                    color = SurfaceWhite,
                    shadowElevation = 1.dp,
                    modifier = Modifier.clickable {
                        selectedLanguage = if (selectedLanguage == "English") "हिंदी (Hindi)" else "English"
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🌐", fontSize = 12.sp)
                        Text(
                            text = selectedLanguage,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                    }
                }
            }
        }

        // 2. Welcome Title
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Who is using the app?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TextPrimaryNearBlack
            )
            Text(
                text = "Select your role to access your dedicated healthcare portal:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryMuted
            )
        }

        // 3. 4-Role Selector Cards (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RoleCard(
                        role = UserRole.PATIENT,
                        title = "Patient",
                        desc = "Health card & SOS",
                        icon = "👤",
                        color = LimePrimary,
                        isSelected = selectedRole == UserRole.PATIENT,
                        onClick = { selectedRole = UserRole.PATIENT },
                        modifier = Modifier.weight(1f)
                    )
                    RoleCard(
                        role = UserRole.ASHA,
                        title = "ASHA Worker",
                        desc = "Caseload & Proxy",
                        icon = "🤝",
                        color = LavenderSecondary,
                        isSelected = selectedRole == UserRole.ASHA,
                        onClick = { selectedRole = UserRole.ASHA },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RoleCard(
                        role = UserRole.DOCTOR,
                        title = "Doctor",
                        desc = "Review & Prescribe",
                        icon = "🩺",
                        color = BlushPinkTertiary,
                        isSelected = selectedRole == UserRole.DOCTOR,
                        onClick = { selectedRole = UserRole.DOCTOR },
                        modifier = Modifier.weight(1f)
                    )
                    RoleCard(
                        role = UserRole.ADMIN,
                        title = "Admin",
                        desc = "Outbreak Trends",
                        icon = "🛡️",
                        color = AmberWarning,
                        isSelected = selectedRole == UserRole.ADMIN,
                        onClick = { selectedRole = UserRole.ADMIN },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Role Credentials Form & 1-Tap Demo Login
        item {
            VitalSenseCard(
                elevation = 3.dp,
                backgroundColor = SurfaceWhite
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = when (selectedRole) {
                                UserRole.PATIENT -> "👤 Patient Sign-In"
                                UserRole.ASHA -> "🤝 ASHA Worker Sign-In"
                                UserRole.DOCTOR -> "🩺 Doctor Clinical Portal"
                                UserRole.ADMIN -> "🛡️ District Health Admin"
                            },
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                    }

                    when (selectedRole) {
                        UserRole.PATIENT -> {
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { phoneInput = it },
                                label = { Text("Mobile Number") },
                                placeholder = { Text("+91 98111 22334") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            OutlinedTextField(
                                value = ashaIdInput,
                                onValueChange = { ashaIdInput = it },
                                label = { Text("ASHA Helper ID (Optional)") },
                                placeholder = { Text("e.g. ASHA-7701") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            VitalSenseButton(
                                text = "Log In as Patient →",
                                onClick = { onPatientLogin(samplePatients.first()) },
                                style = ButtonStyle.PRIMARY
                            )

                            // 1-Tap Demo Logins for Judges/Evaluators
                            Text(
                                text = "⚡ Quick 1-Tap Demo Login as:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondaryMuted
                            )
                            samplePatients.forEach { patient ->
                                Surface(
                                    shape = PillShape,
                                    color = LimePrimary.copy(alpha = 0.35f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onPatientLogin(patient) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${patient.name} (${patient.villageName})",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimaryNearBlack
                                        )
                                        SeverityBadge(severity = patient.currentRiskLevel)
                                    }
                                }
                            }
                        }

                        UserRole.ASHA -> {
                            OutlinedTextField(
                                value = ashaIdInput,
                                onValueChange = { ashaIdInput = it },
                                label = { Text("Unique ASHA ID") },
                                placeholder = { Text("e.g. ASHA-7701") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            OutlinedTextField(
                                value = pinInput,
                                onValueChange = { pinInput = it },
                                label = { Text("4-Digit Security PIN") },
                                placeholder = { Text("••••") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            VitalSenseButton(
                                text = "Log In to ASHA Caseload →",
                                onClick = { onAshaLogin(sampleAshas.first()) },
                                style = ButtonStyle.DARK
                            )

                            Text(
                                text = "⚡ Quick 1-Tap Demo Login as:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondaryMuted
                            )
                            sampleAshas.forEach { asha ->
                                Surface(
                                    shape = PillShape,
                                    color = LavenderSecondary.copy(alpha = 0.35f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onAshaLogin(asha) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${asha.name} (${asha.ashaUniqueId})",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimaryNearBlack
                                        )
                                        Text(
                                            text = "${asha.activePatientCount} patients",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondaryMuted
                                        )
                                    }
                                }
                            }
                        }

                        UserRole.DOCTOR -> {
                            OutlinedTextField(
                                value = doctorEmailInput,
                                onValueChange = { doctorEmailInput = it },
                                label = { Text("Medical Registration / Email") },
                                placeholder = { Text("dr.rajesh@vitalsense.org") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            OutlinedTextField(
                                value = doctorPasswordInput,
                                onValueChange = { doctorPasswordInput = it },
                                label = { Text("Password") },
                                placeholder = { Text("••••••••") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            VitalSenseButton(
                                text = "Log In to Clinical Portal →",
                                onClick = { onDoctorLogin(sampleDoctors.first()) },
                                style = ButtonStyle.PRIMARY
                            )

                            Text(
                                text = "⚡ Quick 1-Tap Demo Login as:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondaryMuted
                            )
                            sampleDoctors.forEach { doc ->
                                Surface(
                                    shape = PillShape,
                                    color = BlushPinkTertiary.copy(alpha = 0.35f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onDoctorLogin(doc) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = doc.name,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimaryNearBlack
                                        )
                                        Text(
                                            text = doc.specialty.displayName,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondaryMuted
                                        )
                                    }
                                }
                            }
                        }

                        UserRole.ADMIN -> {
                            OutlinedTextField(
                                value = adminPasscodeInput,
                                onValueChange = { adminPasscodeInput = it },
                                label = { Text("District Admin Passcode") },
                                placeholder = { Text("ADMIN-RAMPUR-2026") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = InputShape
                            )
                            VitalSenseButton(
                                text = "Enter District Health Command →",
                                onClick = onAdminLogin,
                                style = ButtonStyle.DARK
                            )

                            Text(
                                text = "⚡ Quick 1-Tap Demo Login as:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondaryMuted
                            )
                            Surface(
                                shape = PillShape,
                                color = AmberWarning.copy(alpha = 0.35f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAdminLogin() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "District Chief Medical Officer (Rampur)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimaryNearBlack
                                    )
                                    Text(
                                        text = "Full Access",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Offline resilience reassurance badge
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📶 Offline-First: Health Card & Core Tools Work With Zero Internet",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryMuted
                )
            }
        }
    }
}

@Composable
private fun RoleCard(
    role: UserRole,
    title: String,
    desc: String,
    icon: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(95.dp)
            .clickable { onClick() },
        shape = CardShape,
        color = if (isSelected) color.copy(alpha = 0.5f) else SurfaceWhite,
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DarkCharcoal) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 20.sp)
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(DarkCharcoal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✓", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimaryNearBlack
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryMuted,
                    maxLines = 1
                )
            }
        }
    }
}

