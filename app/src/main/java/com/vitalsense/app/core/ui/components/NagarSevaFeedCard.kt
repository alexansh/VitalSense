package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.touchSpring

/**
 * NagarSeva Item Feed / List Card:
 * Card-based list item with category icon in tinted circle, multiline title,
 * ward/village location tag, time badge, right-aligned colored status chip,
 * and tactile scale-down touch spring physics.
 */
@Composable
fun NagarSevaFeedCard(
    title: String,
    categoryIcon: String,
    locationTag: String,
    timeBadge: String,
    statusText: String,
    statusBgColor: Color = VS_SuccessContainer,
    statusTextColor: Color = VS_Success,
    categoryBgColor: Color = VS_PrimaryContainer,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    VitalSenseCard(
        modifier = modifier,
        onClick = onClick,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.Top
        ) {
            // Category Icon in Tinted Circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(categoryBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = categoryIcon, fontSize = 20.sp)
            }

            // Main Content Area
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Top Header Row with Title and Right-Aligned Status Chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 2
                    )

                    Surface(
                        shape = PillShape,
                        color = statusBgColor,
                        modifier = Modifier.padding(start = Spacing.xs)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = statusTextColor,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant,
                        maxLines = 2
                    )
                }

                // Bottom Meta Row: Location Tag & Time Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = PillShape,
                        color = VS_SurfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "📍", fontSize = 10.sp)
                            Text(
                                text = locationTag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = VS_OnSurfaceVariant
                                )
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = "⏱️", fontSize = 10.sp)
                        Text(
                            text = timeBadge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                color = VS_OnSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
