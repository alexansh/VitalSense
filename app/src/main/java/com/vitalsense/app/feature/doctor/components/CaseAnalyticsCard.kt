package com.vitalsense.app.feature.doctor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.DoctorCaseAnalytics
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.VS_OnBackground
import com.vitalsense.app.core.ui.theme.VS_OnSurfaceVariant
import com.vitalsense.app.core.ui.theme.Spacing
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun CaseAnalyticsCard(
    analytics: DoctorCaseAnalytics,
    modifier: Modifier = Modifier
) {
    val lowRatio = if (analytics.totalCases > 0) analytics.lowCount.toFloat() / analytics.totalCases else 0f
    val modRatio = if (analytics.totalCases > 0) analytics.moderateCount.toFloat() / analytics.totalCases else 0f
    val highRatio = if (analytics.totalCases > 0) analytics.highCount.toFloat() / analytics.totalCases else 0f
    val sevRatio = if (analytics.totalCases > 0) analytics.severeCount.toFloat() / analytics.totalCases else 0f

    VitalSenseCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(
                text = stringResource(R.string.triageBreakdownTitle),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground
            )

            // Horizontal Stacked Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)) // Fallback if empty
            ) {
                if (analytics.totalCases == 0) {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F0F0)))
                } else {
                    if (lowRatio > 0f) Box(modifier = Modifier.weight(lowRatio).fillMaxHeight().background(Color(SeverityLevel.LOW.badgeColorHex)))
                    if (modRatio > 0f) Box(modifier = Modifier.weight(modRatio).fillMaxHeight().background(Color(SeverityLevel.MODERATE.badgeColorHex)))
                    if (highRatio > 0f) Box(modifier = Modifier.weight(highRatio).fillMaxHeight().background(Color(SeverityLevel.HIGH.badgeColorHex)))
                    if (sevRatio > 0f) Box(modifier = Modifier.weight(sevRatio).fillMaxHeight().background(Color(SeverityLevel.SEVERE.badgeColorHex)))
                }
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LegendItem("${stringResource(R.string.lowRisk)} (${analytics.lowCount})", Color(SeverityLevel.LOW.badgeColorHex))
                LegendItem("${stringResource(R.string.moderateRisk)} (${analytics.moderateCount})", Color(SeverityLevel.MODERATE.badgeColorHex))
                LegendItem("${stringResource(R.string.highRisk)} (${analytics.highCount})", Color(SeverityLevel.HIGH.badgeColorHex))
                LegendItem("${stringResource(R.string.urgent)} (${analytics.severeCount})", Color(SeverityLevel.SEVERE.badgeColorHex))
            }

            Spacer(modifier = Modifier.height(Spacing.xs))

            // 4 Mini Stat Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                MiniStatTile(
                    label = stringResource(R.string.statTotal),
                    value = analytics.totalCases.toString(),
                    modifier = Modifier.weight(1f)
                )
                MiniStatTile(
                    label = stringResource(R.string.statPending),
                    value = analytics.pendingCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                MiniStatTile(
                    label = stringResource(R.string.statResolved),
                    value = analytics.respondedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                MiniStatTile(
                    label = stringResource(R.string.statReferred),
                    value = analytics.referredCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = VS_OnSurfaceVariant
        )
    }
}

@Composable
private fun MiniStatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7)) // Very light gray surface
            .padding(Spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = VS_OnBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = VS_OnSurfaceVariant
        )
    }
}
