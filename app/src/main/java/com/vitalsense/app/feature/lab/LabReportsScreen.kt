package com.vitalsense.app.feature.lab

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.vitalsense.app.core.data.model.LabReport
import com.vitalsense.app.core.data.model.LabTestItem
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseTextField
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabReportsScreen(
    patient: Patient,
    labReports: List<LabReport>,
    onBackClick: () -> Unit,
    onOrderNewTest: (LabReport) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showOrderDialog by remember { mutableStateOf(false) }
    var selectedReportForDetails by remember { mutableStateOf<LabReport?>(null) }

    val categories = listOf("All", "CBC", "Biochemistry", "Serology", "Antenatal")

    val filteredReports = remember(selectedCategory, labReports) {
        if (selectedCategory == "All") {
            labReports
        } else {
            labReports.filter { it.testCategory.contains(selectedCategory, ignoreCase = true) }
        }
    }

    val totalAbnormalFlags = remember(labReports) {
        labReports.sumOf { report -> report.items.count { it.flag != "NORMAL" } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.diagnosticLabReports),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "E-Diagnostics Lab · ${patient.name}",
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
                    IconButton(onClick = { showOrderDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = stringResource(R.string.viewLabDiagnostics),
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
            // 1. Metrics Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = VS_SurfaceVariant,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.statTotal),
                                style = MaterialTheme.typography.labelSmall,
                                color = VS_OnSurfaceVariant
                            )
                            Text(
                                text = "${labReports.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )
                        }
                    }

                    VitalSenseCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = if (totalAbnormalFlags > 0) VS_ErrorContainer.copy(alpha = 0.4f) else VS_SurfaceVariant,
                        border = BorderStroke(1.dp, if (totalAbnormalFlags > 0) VS_Error.copy(alpha = 0.5f) else VS_Outline)
                    ) {
                        Column {
                            Text(
                                text = "Abnormal Findings",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (totalAbnormalFlags > 0) VS_Error else VS_OnSurfaceVariant
                            )
                            Text(
                                text = "$totalAbnormalFlags Parameters",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (totalAbnormalFlags > 0) VS_Error else VS_OnSuccessContainer
                            )
                        }
                    }
                }
            }

            // 2. Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) VS_Primary else VS_SurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline),
                            modifier = Modifier.clickable { selectedCategory = category }
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                color = if (isSelected) Color.White else VS_OnBackground,
                                modifier = Modifier.padding(horizontal = Spacing.md, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // 3. Lab Reports List
            if (filteredReports.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.xl),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = VS_OnSurfaceVariant
                            )
                            Spacer(Modifier.height(Spacing.sm))
                            Text(
                                text = "No lab investigations in this category",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredReports, key = { it.id }) { report ->
                    LabReportCard(
                        report = report,
                        onViewDetails = { selectedReportForDetails = report }
                    )
                }
            }

            item { Spacer(Modifier.height(Spacing.xl)) }
        }
    }

    // Order Lab Test Dialog
    if (showOrderDialog) {
        OrderLabTestDialog(
            patient = patient,
            onDismiss = { showOrderDialog = false },
            onConfirmOrder = { newReport ->
                onOrderNewTest(newReport)
                showOrderDialog = false
            }
        )
    }

    // Detailed Report Modal
    selectedReportForDetails?.let { report ->
        LabReportDetailModal(
            report = report,
            onDismiss = { selectedReportForDetails = null }
        )
    }
}

@Composable
fun LabReportCard(
    report: LabReport,
    onViewDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val abnormalCount = report.items.count { it.flag != "NORMAL" }

    VitalSenseCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = VS_SurfaceVariant,
        border = BorderStroke(1.dp, VS_Outline)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
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
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(VS_PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = VS_Primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = report.testCategory,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Prescribed by ${report.doctorName} · ${report.dateFormatted}",
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = PillShape,
                    color = if (abnormalCount > 0) VS_ErrorContainer else VS_SuccessContainer
                ) {
                    Text(
                        text = if (abnormalCount > 0) "$abnormalCount ${stringResource(R.string.outOfStock)}" else stringResource(R.string.normalRange),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (abnormalCount > 0) VS_Error else VS_OnSuccessContainer,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                    )
                }
            }

            HorizontalDivider(color = VS_Outline.copy(alpha = 0.5f))

            // Highlight top 3 test items preview
            report.items.take(3).forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.testName,
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(
                            text = "${item.resultValue} ${item.unit}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = when (item.flag) {
                                "HIGH" -> VS_Error
                                "LOW" -> VS_Warning
                                else -> VS_OnBackground
                            }
                        )
                        if (item.flag != "NORMAL") {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (item.flag == "HIGH") VS_ErrorContainer else VS_WarningContainer
                            ) {
                                Text(
                                    text = item.flag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.flag == "HIGH") VS_Error else VS_OnWarningContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (report.items.size > 3) {
                Text(
                    text = "+ ${report.items.size - 3} more parameters measured",
                    style = MaterialTheme.typography.labelSmall,
                    color = VS_PrimaryContainer
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onViewDetails) {
                    Text(
                        text = "View Full E-Report ➔",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_Primary
                    )
                }
            }
        }
    }
}

@Composable
fun LabReportDetailModal(
    report: LabReport,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = report.testCategory,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Text(
                        text = "Certified Laboratory Report",
                        style = MaterialTheme.typography.labelSmall,
                        color = VS_OnSuccessContainer
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = VS_OnSurfaceVariant)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VS_SurfaceVariant,
                        border = BorderStroke(1.dp, VS_Outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text("Patient: ${report.patientName}", style = MaterialTheme.typography.labelSmall, color = VS_OnBackground)
                            Text("Consultant: ${report.doctorName}", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                            Text("Report Date: ${report.dateFormatted}", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                        }
                    }
                }

                item {
                    Text(
                        text = "Investigation Findings",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_PrimaryContainer
                    )
                }

                items(report.items) { item ->
                    VitalSenseCard(
                        backgroundColor = VS_SurfaceVariant,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.testName,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = when (item.flag) {
                                        "HIGH" -> VS_ErrorContainer
                                        "LOW" -> VS_WarningContainer
                                        else -> VS_SuccessContainer
                                    }
                                ) {
                                    Text(
                                        text = item.flag,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (item.flag) {
                                                "HIGH" -> VS_Error
                                                "LOW" -> VS_OnWarningContainer
                                                else -> VS_OnSuccessContainer
                                            }
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Observed: ${item.resultValue} ${item.unit}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = when (item.flag) {
                                        "HIGH" -> VS_Error
                                        "LOW" -> VS_Warning
                                        else -> VS_OnBackground
                                    }
                                )
                                Text(
                                    text = "Ref: ${item.referenceRange}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VS_PrimaryContainer.copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text(
                                text = "Pathologist Clinical Notes",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )
                            Text(
                                text = report.notes,
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnBackground
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            VitalSenseButton(
                text = "Close E-Report",
                onClick = onDismiss,
                style = ButtonStyle.SECONDARY
            )
        },
        containerColor = VS_Background
    )
}

@Composable
fun OrderLabTestDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onConfirmOrder: (LabReport) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Complete Blood Count (CBC)") }
    var doctorName by remember { mutableStateOf("Dr. Rajesh Kumar") }
    var clinicalNotes by remember { mutableStateOf("Routine diagnostic workup for febrile symptoms.") }

    val presetCategories = listOf(
        "Complete Blood Count (CBC)",
        "Biochemistry / Fasting Blood Sugar",
        "Liver Function Test (LFT)",
        "Dengue & Febrile Serology",
        "Urinalysis & Kidney Function"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Order Diagnostic Lab Test",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    text = "Select Investigation Panel:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnSurfaceVariant
                )

                presetCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) VS_PrimaryContainer else VS_SurfaceVariant,
                        border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = category }
                    ) {
                        Row(
                            modifier = Modifier.padding(Spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            RadioButton(selected = isSelected, onClick = { selectedCategory = category })
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                color = VS_OnBackground
                            )
                        }
                    }
                }

                VitalSenseTextField(
                    value = doctorName,
                    onValueChange = { doctorName = it },
                    label = "Ordering Physician"
                )

                VitalSenseTextField(
                    value = clinicalNotes,
                    onValueChange = { clinicalNotes = it },
                    label = "Clinical Indication / Symptoms"
                )
            }
        },
        confirmButton = {
            VitalSenseButton(
                text = "Issue Order",
                onClick = {
                    val dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                    val sampleItems = when (selectedCategory) {
                        "Complete Blood Count (CBC)" -> listOf(
                            LabTestItem("Hemoglobin", "12.5", "g/dL", "13.0 - 17.0", "LOW"),
                            LabTestItem("Total Leukocyte Count (WBC)", "8,200", "/mcL", "4,000 - 11,000", "NORMAL"),
                            LabTestItem("Platelet Count", "2.10", "Lakh/mcL", "1.50 - 4.50", "NORMAL")
                        )
                        "Biochemistry / Fasting Blood Sugar" -> listOf(
                            LabTestItem("Fasting Blood Sugar", "110", "mg/dL", "70 - 100", "HIGH"),
                            LabTestItem("Serum Creatinine", "0.85", "mg/dL", "0.7 - 1.3", "NORMAL")
                        )
                        else -> listOf(
                            LabTestItem("Serology Marker", "Negative", "-", "Negative", "NORMAL"),
                            LabTestItem("Inflammatory Index", "Normal", "-", "Normal", "NORMAL")
                        )
                    }

                    val newReport = LabReport(
                        id = "lab_${System.currentTimeMillis()}",
                        patientId = patient.id,
                        patientName = patient.name,
                        testCategory = selectedCategory,
                        doctorName = doctorName.ifBlank { "PHC Consultant" },
                        dateFormatted = dateFormatted,
                        items = sampleItems,
                        notes = clinicalNotes
                    )
                    onConfirmOrder(newReport)
                },
                style = ButtonStyle.PRIMARY
            )
        },
        dismissButton = {
            VitalSenseButton(
                text = "Cancel",
                onClick = onDismiss,
                style = ButtonStyle.SECONDARY
            )
        },
        containerColor = VS_Background
    )
}

