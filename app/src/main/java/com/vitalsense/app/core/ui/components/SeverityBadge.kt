package com.vitalsense.app.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vitalsense.app.core.data.model.SeverityLevel

@Composable
fun SeverityBadge(
    severity: SeverityLevel,
    modifier: Modifier = Modifier
) {
    val status = when (severity) {
        SeverityLevel.LOW -> VSStatus.SUCCESS
        SeverityLevel.MODERATE -> VSStatus.WARNING
        SeverityLevel.HIGH -> VSStatus.ERROR
        SeverityLevel.SEVERE -> VSStatus.ERROR
    }
    VSStatusPill(
        label = severity.displayName,
        status = status,
        modifier = modifier
    )
}
