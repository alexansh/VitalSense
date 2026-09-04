package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.ui.theme.*

/**
 * Status tone — maps a semantic meaning to a (foreground, container) pair.
 * Icons and text always use the SAME single tone — never mix multiple
 * bright colors on one pill/badge/icon (that's the "eye-catchy" look to avoid).
 */
enum class VSStatus { SUCCESS, WARNING, ERROR, INFO, PENDING }

private fun VSStatus.colors(): Pair<Color, Color> = when (this) {
    VSStatus.SUCCESS -> VS_OnSuccessContainer to VS_SuccessContainer
    VSStatus.WARNING -> VS_OnWarningContainer to VS_WarningContainer
    VSStatus.ERROR -> VS_OnErrorContainer to VS_ErrorContainer
    VSStatus.INFO -> VS_OnPrimaryContainer to VS_PrimaryContainer
    VSStatus.PENDING -> VS_OnSurfaceVariant to VS_PendingContainer
}

/**
 * A quiet status pill — e.g. "Live", "Pending review", "Sync failed".
 * Single-tone icon + label. No gradients, no multi-color icon fills.
 */
@Composable
fun VSStatusPill(
    label: String,
    status: VSStatus,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    val (fg, bg) = status.colors()
    Row(
        modifier = modifier
            .background(bg, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null, tint = fg, modifier = Modifier.size(16.dp))
        }
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = fg)
    }
}

/**
 * A flat, low-elevation stat card — for dashboard summaries
 * (e.g. active patients, pending SOS alerts, sync status).
 */
@Composable
fun VSStatCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(VS_Surface, RoundedCornerShape(12.dp))
            .border(1.dp, VS_OutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            icon?.let {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(VS_SurfaceVariant, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(it, contentDescription = null, tint = VS_OnSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
            Text(title, style = MaterialTheme.typography.bodyMedium, color = VS_OnSurfaceVariant)
        }
        Spacer(Modifier.height(10.dp))
        Text(value, style = MaterialTheme.typography.headlineMedium, color = VS_OnSurface)
        subtitle?.let {
            Spacer(Modifier.height(2.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium, color = VS_OnSurfaceVariant)
        }
    }
}

data class VSTimelineStep(
    val title: String,
    val timestamp: String?,
    val completed: Boolean,
    val description: String? = null
)

/**
 * Vertical progress timeline — e.g. referral status, appointment
 * lifecycle, prescription review status. Restyled version of the
 * order-tracker pattern from the reference screenshot, in-palette.
 */
@Composable
fun VSTimeline(steps: List<VSTimelineStep>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        steps.forEachIndexed { index, step ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (step.completed) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (step.completed) VS_Primary else VS_Outline,
                        modifier = Modifier.size(22.dp)
                    )
                    if (index != steps.lastIndex) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(36.dp)
                                .background(if (step.completed) VS_Primary else VS_OutlineVariant)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        step.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (step.completed) VS_OnSurface else VS_OnSurfaceVariant
                    )
                    Text(
                        step.timestamp ?: "Pending",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VS_OnSurfaceVariant
                    )
                    step.description?.let { desc ->
                        Spacer(Modifier.height(4.dp))
                        Text(
                            desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Circle avatar frame — used for patient/doctor/ASHA lists.
 * Deliberately no colored ring/gradient border — a single thin
 * neutral outline only.
 */
@Composable
fun VSAvatarFrame(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(VS_SurfaceVariant, CircleShape)
            .border(1.dp, VS_OutlineVariant, CircleShape),
        contentAlignment = Alignment.Center,
        content = content
    )
}

