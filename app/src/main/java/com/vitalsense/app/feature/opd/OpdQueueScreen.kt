package com.vitalsense.app.feature.opd

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.OpdToken
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.feature.doctor.components.TeleConsultationModal
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpdQueueScreen(
    patient: Patient,
    opdTokens: List<OpdToken>,
    onBackClick: () -> Unit,
    onBookToken: (OpdToken) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBookTokenDialog by remember { mutableStateOf(false) }

    val activeToken = opdTokens.firstOrNull { it.patientId == patient.id && it.status != "Completed" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.opdLiveQueueAndTokens),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = stringResource(R.string.opdSubtitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.exit),
                            tint = VS_OnBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showBookTokenDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = stringResource(R.string.bookOpdToken),
                            tint = VS_Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VS_Background)
            )
        },
        containerColor = VS_Background,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // 1. Active Token Hero Card
            item {
                if (activeToken != null) {
                    ActiveTokenCard(token = activeToken, patient = patient)
                } else {
                    NoActiveTokenCard(onBookClick = { showBookTokenDialog = true })
                }
            }

            // 2. Hospital Department Live Queue Display
            item {
                Text(
                    text = stringResource(R.string.hospitalDeptsLiveBoard),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_PrimaryContainer
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    DepartmentQueueRow(
                        department = "General Medicine (OPD-A)",
                        doctor = "Dr. Rajesh Kumar",
                        room = "Room 4",
                        currentServing = "A-21",
                        totalQueue = 42
                    )
                    DepartmentQueueRow(
                        department = "Maternal & Antenatal (OPD-B)",
                        doctor = "Dr. Priya (MO)",
                        room = "Room 2",
                        currentServing = "B-12",
                        totalQueue = 18
                    )
                    DepartmentQueueRow(
                        department = "Orthopedics & Trauma (OPD-C)",
                        doctor = "Dr. Ayushman Dev Singh",
                        room = "Trauma Bay 1",
                        currentServing = "C-03",
                        totalQueue = 11
                    )
                    DepartmentQueueRow(
                        department = "Pediatrics & Child Care (OPD-D)",
                        doctor = "Dr. S. K. Verma",
                        room = "Room 7",
                        currentServing = "D-09",
                        totalQueue = 24
                    )
                }
            }

            // 3. Queue History
            item {
                Text(
                    text = stringResource(R.string.yourActiveTokens),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_PrimaryContainer
                )
            }

            if (opdTokens.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.noActiveTokens),
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                }
            } else {
                items(opdTokens) { token ->
                    PastTokenCard(token = token)
                }
            }

            item { Spacer(Modifier.height(Spacing.xl)) }
        }
    }

    if (showBookTokenDialog) {
        BookOpdTokenDialog(
            patient = patient,
            onDismiss = { showBookTokenDialog = false },
            onConfirmBook = { newToken ->
                onBookToken(newToken)
                showBookTokenDialog = false
            }
        )
    }
}

@Composable
fun ActiveTokenCard(
    token: OpdToken,
    patient: Patient,
    modifier: Modifier = Modifier
) {
    var showTeleconsultation by remember { mutableStateOf(false) }

    VitalSenseCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = VS_SurfaceVariant,
        border = BorderStroke(2.dp, VS_Primary)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = PillShape,
                    color = VS_PrimaryContainer
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(VS_OnSuccessContainer)
                        )
                        Text(
                            text = stringResource(R.string.liveOpdQueueTitle),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = VS_Primary
                            )
                        )
                    }
                }

                Surface(
                    shape = PillShape,
                    color = if (token.status == "Serving") VS_SuccessContainer else VS_WarningContainer
                ) {
                    Text(
                        text = token.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (token.status == "Serving") VS_OnSuccessContainer else VS_OnWarningContainer
                        ),
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 2.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.yourTokenNumber),
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_OnSurfaceVariant
                    )
                    Text(
                        text = token.tokenNumber,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                        color = VS_PrimaryContainer
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.nowServingLabel),
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_OnSurfaceVariant
                    )
                    Text(
                        text = token.currentServingToken,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnSuccessContainer
                    )
                }
            }

            HorizontalDivider(color = VS_Outline.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.departmentLabel), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                    Text(token.department, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                }
                Column {
                    Text(stringResource(R.string.roomCabinLabel), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                    Text(token.cabinNumber, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(stringResource(R.string.estWaitTime), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                    Text("~${token.estimatedWaitMinutes} mins", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnWarningContainer)
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xs))
            
            VitalSenseButton(
                text = stringResource(R.string.joinCall),
                onClick = { showTeleconsultation = true },
                style = ButtonStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showTeleconsultation) {
        TeleConsultationModal(
            patientName = patient.name,
            doctorName = token.doctorName,
            specialty = token.department,
            villageName = patient.villageName,
            patientAge = patient.age,
            isDoctorViewer = false,
            onDismiss = { showTeleconsultation = false },
            onEndCall = { showTeleconsultation = false }
        )
    }
}

@Composable
fun NoActiveTokenCard(
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VitalSenseCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = VS_SurfaceVariant,
        border = BorderStroke(1.dp, VS_Outline)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = VS_Primary
            )
            Text(
                text = stringResource(R.string.noActiveOpdToken),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
            Text(
                text = stringResource(R.string.opdDigitalSlipDesc),
                style = MaterialTheme.typography.bodySmall,
                color = VS_OnSurfaceVariant
            )
            VitalSenseButton(
                text = stringResource(R.string.bookOpdTokenNow),
                onClick = onBookClick,
                style = ButtonStyle.PRIMARY
            )
        }
    }
}

@Composable
fun DepartmentQueueRow(
    department: String,
    doctor: String,
    room: String,
    currentServing: String,
    totalQueue: Int
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = VS_SurfaceVariant,
        border = BorderStroke(1.dp, VS_Outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(department, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                Text("$doctor · $room", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(stringResource(R.string.servingTokenPrefix), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                    Text(currentServing, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = VS_OnSuccessContainer)
                }
                Text("Queue: $totalQueue", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun PastTokenCard(token: OpdToken) {
    VitalSenseCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = VS_SurfaceVariant,
        border = BorderStroke(1.dp, VS_Outline)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${token.tokenNumber} · ${token.department}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "${token.doctorName} · ${token.dateFormatted}",
                    style = MaterialTheme.typography.labelSmall,
                    color = VS_OnSurfaceVariant
                )
            }
            Surface(shape = PillShape, color = VS_SuccessContainer) {
                Text(
                    text = token.status,
                    style = MaterialTheme.typography.labelSmall.copy(color = VS_OnSuccessContainer, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun BookOpdTokenDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onConfirmBook: (OpdToken) -> Unit
) {
    var selectedDept by remember { mutableStateOf("General Medicine") }
    var selectedDoctor by remember { mutableStateOf("Dr. Rajesh Varma") }

    val departments = listOf(
        "General Medicine" to "Dr. Rajesh Varma",
        "Maternal & Antenatal Care" to "Dr. Priya (MO)",
        "Orthopedics & Trauma Surgery" to "Dr. Ayushman Dev Singh",
        "Pediatrics & Child Care" to "Dr. S. K. Verma"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.bookHospitalOpdToken),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    text = stringResource(R.string.selectDepartment),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnSurfaceVariant
                )

                departments.forEach { (dept, doc) ->
                    val isSelected = selectedDept == dept
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                        border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedDept = dept
                                selectedDoctor = doc
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(Spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedDept = dept
                                    selectedDoctor = doc
                                }
                            )
                            Column {
                                Text(
                                    text = dept,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "Consultant: $doc",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            VitalSenseButton(
                text = stringResource(R.string.confirmBooking),
                onClick = {
                    val tokenPrefix = when (selectedDept) {
                        "General Medicine" -> "OPD-A"
                        "Maternal & Antenatal Care" -> "OPD-B"
                        "Orthopedics & Trauma Surgery" -> "OPD-C"
                        else -> "OPD-D"
                    }
                    val tokenNum = (10..50).random()
                    val newToken = OpdToken(
                        id = "tok_${System.currentTimeMillis()}",
                        tokenNumber = "$tokenPrefix$tokenNum",
                        patientId = patient.id,
                        patientName = patient.name,
                        doctorName = selectedDoctor,
                        department = selectedDept,
                        cabinNumber = if (selectedDept.contains("Trauma")) "Trauma Bay 1" else "Room ${(1..6).random()}",
                        currentServingToken = "$tokenPrefix${maxOf(1, tokenNum - 3)}",
                        estimatedWaitMinutes = 15,
                        status = "In Queue",
                        dateFormatted = "Today"
                    )
                    onConfirmBook(newToken)
                },
                style = ButtonStyle.PRIMARY
            )
        },
        dismissButton = {
            VitalSenseButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss,
                style = ButtonStyle.SECONDARY
            )
        },
        containerColor = VS_Background
    )
}

