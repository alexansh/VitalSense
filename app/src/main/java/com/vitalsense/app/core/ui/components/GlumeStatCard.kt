package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.ui.theme.VS_OnSurface
import com.vitalsense.app.core.ui.theme.VS_Primary
import com.vitalsense.app.core.ui.theme.VS_Success
import com.vitalsense.app.core.ui.theme.VS_SurfaceVariant

@Composable
fun GlumeStatCard(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier,
    unit: String? = null,
    badgeText: String? = null,
    badgeColor: Color = VS_Primary,
    trendText: String? = null,
    isTrendPositive: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val subtitleText = trendText ?: badgeText
    val finalValue = if (unit != null) "$value $unit" else value

    VSStatCard(
        title = label,
        value = finalValue,
        subtitle = subtitleText,
        icon = null,
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier
    )
}

@Composable
fun GlumeProgressRing(
    progressFraction: Float,
    size: Dp = 68.dp,
    strokeWidth: Dp = 7.dp,
    ringColor: Color = VS_Success,
    trackColor: Color = VS_SurfaceVariant,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit = {
        Text(
            text = "${(progressFraction * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = VS_OnSurface
        )
    }
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progressFraction.coerceIn(0f, 1f),
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        centerContent()
    }
}
