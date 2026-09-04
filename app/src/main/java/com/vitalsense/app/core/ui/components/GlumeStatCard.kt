package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vitalsense.app.core.ui.theme.VS_Primary

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
