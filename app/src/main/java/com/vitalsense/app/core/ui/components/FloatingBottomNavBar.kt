package com.vitalsense.app.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.touchSpring

data class NavItem(
    val id: String,
    val label: String,
    val icon: String,
    val badgeCount: Int = 0
)

/**
 * NagarSeva Floating Bottom Navigation:
 * Rounded floating pill dock (28dp corner radius) housing navigation items with smooth selection indicators.
 */
@Composable
fun FloatingBottomNavBar(
    items: List<NavItem>,
    selectedItemId: String,
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = VS_Surface,
        border = BorderStroke(1.dp, VS_Outline),
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.sm, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.id == selectedItemId
                val containerColor by animateColorAsState(
                    targetValue = if (isSelected) VS_PrimaryContainer else Color.Transparent,
                    label = "NavContainerColor"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) VS_Primary else VS_OnSurfaceVariant,
                    label = "NavContentColor"
                )

                Surface(
                    onClick = { onItemSelected(item) },
                    shape = PillShape,
                    color = containerColor,
                    modifier = Modifier.touchSpring { onItemSelected(item) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Text(text = item.icon, fontSize = 18.sp)
                            if (item.badgeCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(VS_Error)
                                )
                            }
                        }
                        if (isSelected) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
