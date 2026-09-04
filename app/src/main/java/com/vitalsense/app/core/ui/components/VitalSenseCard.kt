package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.util.touchSpring

/**
 * NagarSeva High-Contrast Elevated Card:
 * Surface card with 18dp corner radius, subtle shadow, hairline border,
 * and tactile spring micro-interaction feedback on touch.
 */
@Composable
fun VitalSenseCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = VS_Surface,
    elevation: Dp = 2.dp,
    border: BorderStroke? = BorderStroke(1.dp, VS_Outline),
    contentPadding: Dp = Spacing.md,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .touchSpring(onClick = onClick),
            shape = CardShape,
            color = backgroundColor,
            shadowElevation = elevation,
            border = border
        ) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = CardShape,
            color = backgroundColor,
            shadowElevation = elevation,
            border = border
        ) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}
