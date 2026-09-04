package com.vitalsense.app.feature.doctor.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*

/**
 * A reusable accordion (expandable/collapsible) card for the Doctor Dashboard.
 *
 * Matches the reference design: rounded card with colored icon circle on the left,
 * bold title + subtitle in the center, and a rotating chevron on the right.
 * Tapping the card toggles the expandable content below with a smooth animation.
 *
 * @param icon Emoji or text to display inside the icon circle
 * @param iconBackgroundColor Background color for the circular icon container
 * @param title Bold heading text for this section
 * @param subtitle Description text below the title
 * @param expanded Whether this accordion is currently expanded
 * @param onToggle Callback when the user taps to expand/collapse
 * @param modifier Optional modifier for the outer card
 * @param content The expandable content shown when expanded
 */
@Composable
fun DashboardAccordionItem(
    icon: String,
    iconBackgroundColor: Color,
    title: String,
    subtitle: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "chevronRotation"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = VS_Surface,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, VS_Outline)
    ) {
        Column {
            // Header row — always visible, clickable to toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Colored icon circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconBackgroundColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 22.sp
                    )
                }

                // Title + subtitle
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = VS_OnBackground
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }

                // Rotating chevron
                Text(
                    text = "⌄",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VS_OnSurfaceVariant,
                    modifier = Modifier.rotate(chevronRotation)
                )
            }

            // Expandable content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    content = content
                )
            }
        }
    }
}
