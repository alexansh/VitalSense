package com.vitalsense.app.feature.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.call.AppointmentScheduleHelper
import com.vitalsense.app.core.call.JoinWindowStatus
import com.vitalsense.app.core.call.TeleCallingManager
import com.vitalsense.app.core.data.model.Appointment
import com.vitalsense.app.core.data.model.CallType
import com.vitalsense.app.core.data.model.UserRole
import com.vitalsense.app.core.ui.components.TabularStatusChip
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.AdaptiveScreenContainer
import com.vitalsense.app.core.ui.util.touchSpring
import com.vitalsense.app.core.util.AudioGuidanceHelper
import com.vitalsense.app.feature.doctor.components.TeleConsultationModal
import java.text.SimpleDateFormat
import java.util.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointments: List<Appointment>,
    onRequestNew: () -> Unit,
    onBackClick: () -> Unit,
    onCheckIn: (appointmentId: String) -> Unit,
    onViewLiveQueue: () -> Unit,
    onBookAppointment: (Appointment) -> Unit = {},
    language: AppLanguage = AppLanguage.ENGLISH
) {
    val context = LocalContext.current
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    var showBookCallDialog by remember { mutableStateOf(false) }
    var activeCallAppt by remember { mutableStateOf<Appointment?>(null) }
    var waitingRoomAppt by remember { mutableStateOf<Appointment?>(null) }

    AdaptiveScreenContainer {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.scheduledAppointments),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = VS_OnBackground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val audioSpoken = when (language) {
                                AppLanguage.HINDI -> "यहाँ आपके सभी डॉक्टर अपॉइंटमेंट और वीडियो कॉल उपलब्ध हैं। समय होने पर आप कॉल से जुड़ सकते हैं।"
                                AppLanguage.TAMIL -> "உங்கள் அனைத்து மருத்துவ ஆலோசனைகளும் இங்கே உள்ளன. நேரம் வரும்போது அழைப்பில் இணையலாம்."
                                AppLanguage.MARATHI -> "येथे आपल्या सर्व डॉक्टरांच्या भेटी आणि व्हिडिओ कॉल उपलब्ध आहेत. वेळ झाल्यावर आपण कॉलला जोडू शकता."
                                AppLanguage.ENGLISH -> "Here are all your doctor appointments and video consultations. You can join the call when scheduled."
                            }
                            AudioGuidanceHelper.speak(
                                context = context,
                                text = audioSpoken,
                                language = language
                            )
                        }) {
                            Text("🔊", fontSize = 20.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = VS_Background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showBookCallDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .touchSpring(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                        ) {
                            Icon(Icons.Outlined.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(stringResource(R.string.bookACall), fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = onViewLiveQueue,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .touchSpring(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, VS_Primary)
                        ) {
                            Text(stringResource(R.string.liveQueueHud), fontWeight = FontWeight.Bold, color = VS_Primary)
                        }
                    }
                }

                if (appointments.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = VS_Surface),
                            border = BorderStroke(1.dp, VS_Outline)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("📅", fontSize = 36.sp)
                                    Text(
                                        text = stringResource(R.string.noUpcomingAppointments),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = stringResource(R.string.bookConsultationSubtitle),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                } else {
                    items(appointments, key = { it.id }) { appt ->
                        val joinStatus = AppointmentScheduleHelper.evaluateJoinWindow(appt)
                        val isVideo = appt.callType == CallType.VIDEO

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .touchSpring(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = VS_Surface),
                            border = BorderStroke(
                                1.5.dp,
                                when (joinStatus) {
                                    JoinWindowStatus.JOIN_ACTIVE -> VS_Success
                                    JoinWindowStatus.AFTER_WINDOW_MISSED -> VS_Error.copy(alpha = 0.5f)
                                    JoinWindowStatus.BEFORE_WINDOW -> VS_Outline
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isVideo) VS_PrimaryContainer else VS_SuccessContainer,
                                            modifier = Modifier.size(42.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(if (isVideo) "📹" else "🎙️", fontSize = 20.sp)
                                            }
                                        }

                                        Column {
                                            Text(
                                                text = "Dr. ${appt.doctorName}",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = VS_OnBackground
                                            )
                                            Text(
                                                text = "${appt.dateFormatted} · ${appt.timeSlot} · ${if (isVideo) "Video Call" else "Voice Call"}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = VS_OnSurfaceVariant
                                            )
                                        }
                                    }

                                    TabularStatusChip(
                                        statusText = when (joinStatus) {
                                            JoinWindowStatus.JOIN_ACTIVE -> "READY TO JOIN"
                                            JoinWindowStatus.AFTER_WINDOW_MISSED -> "MISSED"
                                            JoinWindowStatus.BEFORE_WINDOW -> appt.status.uppercase()
                                        },
                                        containerColor = when (joinStatus) {
                                            JoinWindowStatus.JOIN_ACTIVE -> NagarSevaStatusNormalBg
                                            JoinWindowStatus.AFTER_WINDOW_MISSED -> VS_ErrorContainer
                                            JoinWindowStatus.BEFORE_WINDOW -> NagarSevaStatusProgressBg
                                        },
                                        textColor = when (joinStatus) {
                                            JoinWindowStatus.JOIN_ACTIVE -> VS_Success
                                            JoinWindowStatus.AFTER_WINDOW_MISSED -> VS_Error
                                            JoinWindowStatus.BEFORE_WINDOW -> VS_Warning
                                        }
                                    )
                                }

                                // Interactive join or reschedule action (56dp min height touch targets)
                                when (joinStatus) {
                                    JoinWindowStatus.JOIN_ACTIVE -> {
                                        Button(
                                            onClick = {
                                                TeleCallingManager.startAppointmentCall(appt, isDoctor = false)
                                                waitingRoomAppt = appt
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                                .touchSpring(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = VS_Success)
                                        ) {
                                            Text(if (isVideo) "📹" else "🎙️", fontSize = 18.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isVideo) "Join Video Consultation Now" else "Join Voice Consultation Now",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = VS_Background
                                            )
                                        }
                                    }

                                    JoinWindowStatus.BEFORE_WINDOW -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Room opens 10m before ${appt.timeSlot}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = VS_OnSurfaceVariant
                                            )
                                            OutlinedButton(
                                                onClick = { showBookCallDialog = true },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(38.dp)
                                            ) {
                                                Text("Reschedule", fontWeight = FontWeight.Bold, color = VS_Primary)
                                            }
                                        }
                                    }

                                    JoinWindowStatus.AFTER_WINDOW_MISSED -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Doctor didn't join · Rebook slot?",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = VS_Error,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Button(
                                                onClick = { showBookCallDialog = true },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                                modifier = Modifier.height(38.dp)
                                            ) {
                                                Text("Rebook Call", color = Color.White, fontWeight = FontWeight.Bold)
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

    // Waiting Room Dialog (Calm state with estimated queue position)
    waitingRoomAppt?.let { appt ->
        AlertDialog(
            onDismissRequest = {
                waitingRoomAppt = null
                TeleCallingManager.endCall("Patient left waiting room")
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⏳", fontSize = 24.sp)
                    Text(
                        text = "Waiting for Doctor to Join…",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "You are in the waiting room for Dr. ${appt.doctorName}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VS_OnBackground
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = VS_PrimaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "STATUS: Next in Queue",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = VS_PrimaryContainer
                                )
                            )
                            Text(
                                text = "The doctor is wrapping up their previous patient note and will join momentarily. Please do not close the app.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnBackground
                            )
                        }
                    }
                    Text(
                        text = "Call Type: ${if (appt.callType == CallType.VOICE) "🎙️ Voice Only (Low Bandwidth)" else "📹 Video Consultation"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_OnSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val current = waitingRoomAppt
                        waitingRoomAppt = null
                        activeCallAppt = current
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enter Consultation Room →", color = VS_Background, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        waitingRoomAppt = null
                        TeleCallingManager.endCall("Patient cancelled waiting")
                    }
                ) {
                    Text("Cancel / Leave", color = VS_Error)
                }
            }
        )
    }

    // Active In-Call Screen
    activeCallAppt?.let { appt ->
        TeleConsultationModal(
            patientName = appt.patientName,
            doctorName = appt.doctorName,
            specialty = appt.doctorSpecialty,
            onDismiss = {
                activeCallAppt = null
                TeleCallingManager.endCall("Patient ended consultation")
            },
            onEndCall = {
                activeCallAppt = null
                TeleCallingManager.endCall("Consultation completed")
            }
        )
    }

    // Book a Call Dialog
    if (showBookCallDialog) {
        var selectedCallType by remember { mutableStateOf(CallType.VIDEO) }
        var selectedTimeSlot by remember { mutableStateOf("11:30 AM") }

        AlertDialog(
            onDismissRequest = { showBookCallDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📅", fontSize = 24.sp)
                    Text(stringResource(R.string.bookTeleConsultation), fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select consultation mode based on your internet connection:")

                    // Video Call Option
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedCallType == CallType.VIDEO) VS_PrimaryContainer else VS_Background,
                        border = BorderStroke(
                            1.5.dp,
                            if (selectedCallType == CallType.VIDEO) VS_Primary else VS_Outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCallType = CallType.VIDEO }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("📹", fontSize = 24.sp)
                            Column {
                                Text("Video Call (HD)", fontWeight = FontWeight.Bold, color = VS_OnBackground)
                                Text("Requires 4G / Wi-Fi signal", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            }
                        }
                    }

                    // Voice Call Option (Recommended for 2G / rural areas)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedCallType == CallType.VOICE) VS_SuccessContainer else VS_Background,
                        border = BorderStroke(
                            1.5.dp,
                            if (selectedCallType == CallType.VOICE) VS_Success else VS_Outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCallType = CallType.VOICE }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🎙️", fontSize = 24.sp)
                            Column {
                                Text("Voice Call (Low Bandwidth)", fontWeight = FontWeight.Bold, color = VS_OnBackground)
                                Text("Recommended for 2G / weak village signal", style = MaterialTheme.typography.labelSmall, color = VS_Success)
                            }
                        }
                    }

                    Text("Time Slot: Today, $selectedTimeSlot", style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newAppt = Appointment(
                            id = "appt_${UUID.randomUUID().toString().take(8)}",
                            patientId = "pat_ramesh",
                            patientName = "Ramesh Kumar",
                            doctorId = "doc_rajesh",
                            doctorName = "Dr. Rajesh Varma",
                            doctorSpecialty = "General Physician",
                            dateFormatted = today,
                            timeSlot = selectedTimeSlot,
                            status = "Confirmed",
                            proposedBy = UserRole.PATIENT,
                            callType = selectedCallType,
                            scheduledTimestamp = System.currentTimeMillis()
                        )
                        onBookAppointment(newAppt)
                        showBookCallDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Confirm Booking ✓", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookCallDialog = false }) {
                    Text("Cancel", color = VS_OnSurfaceVariant)
                }
            }
        )
    }
}

