package com.vitalsense.app.feature.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.DiseaseTrendRecord
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDiseaseTrendsScreen(
    villages: List<Village>,
    trendRecords: List<DiseaseTrendRecord>,
    onBackClick: () -> Unit,
    onSaveRecord: (DiseaseTrendRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedVillageId by remember { mutableStateOf<String?>(villages.firstOrNull()?.id) }
    var expanded by remember { mutableStateOf(false) }

    val selectedVillageName = villages.find { it.id == selectedVillageId }?.name ?: "Select Village"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Disease Trends", color = VS_OnBackground) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = VS_OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VS_Background
                )
            )
        },
        containerColor = VS_Background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.md),
            contentPadding = PaddingValues(bottom = Spacing.xxl)
        ) {
            item {
                Text(
                    text = "Village Selection",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground,
                    modifier = Modifier.padding(bottom = Spacing.sm, top = Spacing.sm)
                )

                // Village Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedVillageName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        villages.forEach { village ->
                            DropdownMenuItem(
                                text = { Text(village.name) },
                                onClick = {
                                    selectedVillageId = village.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.lg))
            }

            // Chart Section
            item {
                if (selectedVillageId != null) {
                    val filteredRecords = trendRecords
                        .filter { it.villageName == selectedVillageName }
                        .sortedBy { it.dateFormatted }

                    if (filteredRecords.isNotEmpty()) {
                        Text(
                            text = "Outbreak Trends (Total Cases)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground,
                            modifier = Modifier.padding(bottom = Spacing.md)
                        )
                        VitalSenseCard {
                            DiseaseTrendLineChart(
                                records = filteredRecords,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(Spacing.sm)
                            )
                        }
                    } else {
                        VitalSenseCard {
                            Text(
                                text = "No trend data available for this village.",
                                color = VS_OnSurfaceVariant,
                                modifier = Modifier.padding(Spacing.md)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Spacing.lg))
                }
            }

            // Entry Section
            item {
                if (selectedVillageId != null) {
                    ManualEntrySection(
                        selectedVillageName = selectedVillageName,
                        onSaveRecord = onSaveRecord
                    )
                }
            }
        }
    }
}

@Composable
fun DiseaseTrendLineChart(
    records: List<DiseaseTrendRecord>,
    modifier: Modifier = Modifier
) {
    // Group by date, sum cases
    val aggregated = records.groupBy { it.dateFormatted }
        .mapValues { entry -> entry.value.sumOf { it.caseCount } }
        .entries.sortedBy { it.key }

    if (aggregated.isEmpty()) return

    val maxCases = aggregated.maxOf { it.value }.toFloat().coerceAtLeast(10f)
    val pointCount = aggregated.size

    val lineColor = VS_Primary
    val textColor = VS_OnSurfaceVariant.copy(alpha = 0.6f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val paddingX = 40.dp.toPx()
        val paddingY = 30.dp.toPx()

        val graphWidth = width - paddingX
        val graphHeight = height - paddingY

        // Draw axes
        drawLine(
            color = textColor,
            start = Offset(paddingX, 0f),
            end = Offset(paddingX, graphHeight),
            strokeWidth = 2f
        )
        drawLine(
            color = textColor,
            start = Offset(paddingX, graphHeight),
            end = Offset(width, graphHeight),
            strokeWidth = 2f
        )

        if (pointCount > 0) {
            val spacePerPoint = if (pointCount > 1) graphWidth / (pointCount - 1) else graphWidth / 2f

            val path = Path()
            val points = mutableListOf<Offset>()

            aggregated.forEachIndexed { index, entry ->
                val x = paddingX + (index * spacePerPoint)
                // Invert Y axis
                val y = graphHeight - ((entry.value / maxCases) * graphHeight)
                points.add(Offset(x, y))

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 4.dp.toPx())
            )

            // Draw points
            points.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = 6.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = point
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntrySection(
    selectedVillageName: String,
    onSaveRecord: (DiseaseTrendRecord) -> Unit
) {
    var selectedDisease by remember { mutableStateOf("Malaria") }
    var diseaseExpanded by remember { mutableStateOf(false) }
    var caseCountText by remember { mutableStateOf("") }
    val diseases = listOf("Malaria", "Dengue", "Cholera", "Typhoid", "Tuberculosis")

    Text(
        text = "Record New Data",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = VS_OnBackground,
        modifier = Modifier.padding(bottom = Spacing.sm)
    )

    VitalSenseCard {
        Column(modifier = Modifier.padding(Spacing.sm)) {
            ExposedDropdownMenuBox(
                expanded = diseaseExpanded,
                onExpandedChange = { diseaseExpanded = !diseaseExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDisease,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Disease") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = diseaseExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = diseaseExpanded,
                    onDismissRequest = { diseaseExpanded = false }
                ) {
                    diseases.forEach { disease ->
                        DropdownMenuItem(
                            text = { Text(disease) },
                            onClick = {
                                selectedDisease = disease
                                diseaseExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            OutlinedTextField(
                value = caseCountText,
                onValueChange = { caseCountText = it.filter { char -> char.isDigit() } },
                label = { Text("Total Cases") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            VitalSenseButton(
                text = "Save Record",
                onClick = {
                    val count = caseCountText.toIntOrNull()
                    if (count != null && count >= 0) {
                        val record = DiseaseTrendRecord(
                            id = "trend_${System.currentTimeMillis()}",
                            villageName = selectedVillageName,
                            diseaseName = selectedDisease,
                            caseCount = count,
                            dateFormatted = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date()),
                            severity = null
                        )
                        onSaveRecord(record)
                        caseCountText = ""
                    }
                },
                style = ButtonStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
