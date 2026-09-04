package com.vitalsense.app.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape scale — deliberately restrained. Government/official apps read
 * as more trustworthy with modest rounding (8–16dp) rather than the
 * heavily pill-shaped, playful rounding common in consumer SaaS UI.
 */
val VSShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)


val VitalSenseShapes = VSShapes
val PillShape = androidx.compose.foundation.shape.RoundedCornerShape(50)


val DialogShape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
val CardShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
val InputShape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
val StatCardShape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)

