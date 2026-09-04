package com.vitalsense.app.feature.admin
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.FacilityQuality
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFacilityQualityScreen(
    onNavigateBack: () -> Unit
) {
    // Mock Data for Facility Quality Metrics
    val facilityQualities = listOf(
        FacilityQuality(
            id = "q1", facilityId = "PHC-A", cleanlinessScore = 85,
            staffAvailabilityScore = 90, equipmentReadinessScore = 75,
            patientFeedbackScore = 4.2f, lastAssessmentDate = System.currentTimeMillis()
        ),
        FacilityQuality(
            id = "q2", facilityId = "CHC-B", cleanlinessScore = 60,
            staffAvailabilityScore = 55, equipmentReadinessScore = 50,
            patientFeedbackScore = 3.1f, lastAssessmentDate = System.currentTimeMillis() - 86400000L
        ),
        FacilityQuality(
            id = "q3", facilityId = "DH-C", cleanlinessScore = 95,
            staffAvailabilityScore = 95, equipmentReadinessScore = 90,
            patientFeedbackScore = 4.8f, lastAssessmentDate = System.currentTimeMillis() - 172800000L
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.facilityQualityMetrics)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(stringResource(R.string.backAction)) // Or an icon
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(stringResource(R.string.overallHealthSystemQuality), style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            items(facilityQualities) { quality ->
                FacilityQualityCard(quality)
            }
        }
    }
}

@Composable
fun FacilityQualityCard(quality: FacilityQuality) {
    val scoreColor = when {
        quality.cleanlinessScore + quality.staffAvailabilityScore + quality.equipmentReadinessScore > 240 -> VS_Success
        quality.cleanlinessScore + quality.staffAvailabilityScore + quality.equipmentReadinessScore > 180 -> VS_Warning
        else -> VS_Error
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Facility: ${quality.facilityId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, contentDescription = "Rating", tint = com.vitalsense.app.core.ui.theme.VS_Warning)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = quality.patientFeedbackScore.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            QualityMetricBar("Cleanliness", quality.cleanlinessScore, scoreColor)
            QualityMetricBar("Staff Availability", quality.staffAvailabilityScore, scoreColor)
            QualityMetricBar("Equipment Readiness", quality.equipmentReadinessScore, scoreColor)
        }
    }
}

@Composable
fun QualityMetricBar(label: String, score: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(text = "$score/100", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

