package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.touchSpring

enum class ButtonStyle {
    PRIMARY,   // NagarSeva Electric Violet (#7C5CFF) with pure white text
    DARK,      // Elevated Surface (#F1F5F9)
    SECONDARY, // Soft Indigo Container (#EEF2FF)
    DANGER,    // Alert Rose/Red (#EF4444) with pure white text
    OUTLINED   // Transparent with subtle border
}

@Composable
fun VitalSenseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    icon: (@Composable () -> Unit)? = null,
    minHeight: Dp = 48.dp,
    enabled: Boolean = true
) {
    val containerColor = when (style) {
        ButtonStyle.PRIMARY -> VS_Primary
        ButtonStyle.DARK -> VS_SurfaceVariant
        ButtonStyle.SECONDARY -> VS_PrimaryContainer
        ButtonStyle.DANGER -> VS_Error
        ButtonStyle.OUTLINED -> Color.Transparent
    }

    val contentColor = when (style) {
        ButtonStyle.PRIMARY -> Color.White
        ButtonStyle.DARK -> VS_OnBackground
        ButtonStyle.SECONDARY -> VS_Primary
        ButtonStyle.DANGER -> Color.White
        ButtonStyle.OUTLINED -> VS_OnBackground
    }

    val border = if (style == ButtonStyle.OUTLINED) {
        BorderStroke(1.dp, VS_Outline)
    } else null

    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .fillMaxWidth()
            .touchSpring(onClick = if (enabled) onClick else null),
        enabled = enabled,
        shape = PillShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = VS_SurfaceVariant.copy(alpha = 0.5f),
            disabledContentColor = VS_OnSurfaceVariant
        ),
        border = border,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(Spacing.xs))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
