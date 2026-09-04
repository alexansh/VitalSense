package com.vitalsense.app.feature.admin.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.SeverityBadge
import com.vitalsense.app.core.ui.theme.*
import kotlin.math.max
import kotlin.math.sqrt

enum class MapLayerType {
    STANDARD,
    SATELLITE,
    DARK
}

@Composable
fun DistrictOutbreakMapView(
    villages: List<Village>,
    selectedVillage: Village?,
    onSelectVillage: (Village) -> Unit,
    onBroadcastToVillage: (Village) -> Unit,
    modifier: Modifier = Modifier
) {
    var mapLayer by remember { mutableStateOf(MapLayerType.STANDARD) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .border(BorderStroke(1.dp, VS_Outline), CardShape),
        color = when (mapLayer) {
            MapLayerType.STANDARD -> Color(0xFFF4F3F0)
            MapLayerType.SATELLITE -> Color(0xFF1E281E)
            MapLayerType.DARK -> Color(0xFF12141C)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            // 1. Google Map Interactive Pan, Drag & Zoom Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    // 2-Finger Pinch to Zoom & 1-Finger Free Pan Dragging in All Directions
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            zoomLevel = (zoomLevel * zoom).coerceIn(0.7f, 3.2f)
                            val maxPan = 600f * zoomLevel
                            panOffset = Offset(
                                x = (panOffset.x + pan.x).coerceIn(-maxPan, maxPan),
                                y = (panOffset.y + pan.y).coerceIn(-maxPan, maxPan)
                            )
                        }
                    }
                    // Tap detection for selecting village pins
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            val width = size.width.toFloat()
                            val height = size.height.toFloat()

                            val tappedVillage = villages.minByOrNull { village ->
                                val (vx, vy) = getVillageScreenCoordinates(village, width, height, zoomLevel, panOffset)
                                val distSq = (tapOffset.x - vx) * (tapOffset.x - vx) + (tapOffset.y - vy) * (tapOffset.y - vy)
                                distSq
                            }

                            if (tappedVillage != null) {
                                val (vx, vy) = getVillageScreenCoordinates(tappedVillage, width, height, zoomLevel, panOffset)
                                val dist = sqrt((tapOffset.x - vx) * (tapOffset.x - vx) + (tapOffset.y - vy) * (tapOffset.y - vy))
                                if (dist < 45f * zoomLevel) {
                                    onSelectVillage(tappedVillage)
                                }
                            }
                        }
                    }
            ) {
                val w = size.width
                val h = size.height

                // Apply Pan & Zoom transformation matrix for map geometry
                val centerX = w / 2f
                val centerY = h / 2f

                fun transformOffset(ox: Float, oy: Float): Offset {
                    val zx = centerX + (ox - centerX) * zoomLevel + panOffset.x
                    val zy = centerY + (oy - centerY) * zoomLevel + panOffset.y
                    return Offset(zx, zy)
                }

                // A. Base Ground & Terrain Areas
                when (mapLayer) {
                    MapLayerType.STANDARD -> {
                        // Standard Google Maps Land Color
                        drawRect(Color(0xFFF3F1EC))

                        // Green Parks & Forest Land (Google Maps Green #D2EBD2)
                        val greenPath1 = Path().apply {
                            val p1 = transformOffset(0f, 0f)
                            val p2 = transformOffset(w * 0.45f, 0f)
                            val p3 = transformOffset(w * 0.35f, h * 0.32f)
                            val p4 = transformOffset(0f, h * 0.38f)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            cubicTo(p3.x, p3.y, p3.x, p4.y, p4.x, p4.y)
                            close()
                        }
                        drawPath(greenPath1, Color(0xFFD6EAD8))

                        val greenPath2 = Path().apply {
                            val p1 = transformOffset(w * 0.65f, h)
                            val p2 = transformOffset(w, h * 0.60f)
                            val p3 = transformOffset(w, h)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                            close()
                        }
                        drawPath(greenPath2, Color(0xFFCCE6CE))

                        // Ramganga River Stream (Google Maps Blue #AAD3DF)
                        val riverPath = Path().apply {
                            val p1 = transformOffset(0f, h * 0.82f)
                            val p2 = transformOffset(w * 0.30f, h * 0.68f)
                            val p3 = transformOffset(w * 0.48f, h * 0.88f)
                            val p4 = transformOffset(w * 0.75f, h * 0.46f)
                            val p5 = transformOffset(w, h * 0.18f)
                            moveTo(p1.x, p1.y)
                            cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
                            cubicTo(p4.x, p4.y, p5.x - 20f, p5.y + 20f, p5.x, p5.y)
                        }
                        drawPath(riverPath, Color(0xFFAAD3DF), style = Stroke(width = 24f * zoomLevel))

                        // Secondary Road Network (Google Maps White/Gray Streets)
                        for (i in -2..6) {
                            val startH = transformOffset(-w * 0.5f, h * (i / 4f))
                            val endH = transformOffset(w * 1.5f, h * (i / 4f) + 15f)
                            drawLine(
                                color = Color(0xFFFFFFFF),
                                start = startH,
                                end = endH,
                                strokeWidth = 3f * zoomLevel
                            )

                            val startV = transformOffset(w * (i / 4f), -h * 0.5f)
                            val endV = transformOffset(w * (i / 4f) - 20f, h * 1.5f)
                            drawLine(
                                color = Color(0xFFFFFFFF),
                                start = startV,
                                end = endV,
                                strokeWidth = 3f * zoomLevel
                            )
                        }

                        // Primary National Highway NH-24 (Google Maps Orange-Yellow Highway #FCD475)
                        val highwayPath = Path().apply {
                            val p1 = transformOffset(-w * 0.5f, h * 0.32f)
                            val p2 = transformOffset(w * 0.48f, h * 0.45f)
                            val p3 = transformOffset(w * 1.5f, h * 0.58f)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                        }
                        // Highway Casing (Orange Border)
                        drawPath(highwayPath, Color(0xFFF9B858), style = Stroke(width = 10f * zoomLevel))
                        // Highway Fill (Yellow Highway)
                        drawPath(highwayPath, Color(0xFFFFDF88), style = Stroke(width = 7f * zoomLevel))

                        // State Highway SH-43
                        val stateHwyPath = Path().apply {
                            val p1 = transformOffset(w * 0.35f, -h * 0.5f)
                            val p2 = transformOffset(w * 0.48f, h * 0.45f)
                            val p3 = transformOffset(w * 0.62f, h * 1.5f)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                        }
                        drawPath(stateHwyPath, Color(0xFFFBD78D), style = Stroke(width = 6f * zoomLevel))
                    }

                    MapLayerType.SATELLITE -> {
                        // Satellite Earth Surface
                        drawRect(Color(0xFF1F2B1D))

                        val patch1TopLeft = transformOffset(0f, 0f)
                        drawRect(Color(0xFF283626), topLeft = patch1TopLeft, size = Size(w * 0.5f * zoomLevel, h * 0.45f * zoomLevel))

                        // River Stream in Satellite (Dark Navy Water #15222E)
                        val riverPath = Path().apply {
                            val p1 = transformOffset(0f, h * 0.82f)
                            val p2 = transformOffset(w * 0.30f, h * 0.68f)
                            val p3 = transformOffset(w * 0.48f, h * 0.88f)
                            val p4 = transformOffset(w * 0.75f, h * 0.46f)
                            val p5 = transformOffset(w, h * 0.18f)
                            moveTo(p1.x, p1.y)
                            cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
                            cubicTo(p4.x, p4.y, p5.x, p5.y, p5.x, p5.y)
                        }
                        drawPath(riverPath, Color(0xFF1B3245), style = Stroke(width = 22f * zoomLevel))

                        // Highways in Satellite (Clean White Lines with Glow)
                        val highwayPath = Path().apply {
                            val p1 = transformOffset(-w * 0.5f, h * 0.32f)
                            val p2 = transformOffset(w * 0.48f, h * 0.45f)
                            val p3 = transformOffset(w * 1.5f, h * 0.58f)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                        }
                        drawPath(highwayPath, Color(0xCCFFFFFF), style = Stroke(width = 4f * zoomLevel))
                    }

                    MapLayerType.DARK -> {
                        // Google Maps Dark Night Mode (#141620)
                        drawRect(Color(0xFF141620))

                        // Dark River (#0F2133)
                        val riverPath = Path().apply {
                            val p1 = transformOffset(0f, h * 0.82f)
                            val p2 = transformOffset(w * 0.30f, h * 0.68f)
                            val p3 = transformOffset(w * 0.48f, h * 0.88f)
                            val p4 = transformOffset(w * 0.75f, h * 0.46f)
                            val p5 = transformOffset(w, h * 0.18f)
                            moveTo(p1.x, p1.y)
                            cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
                            cubicTo(p4.x, p4.y, p5.x, p5.y, p5.x, p5.y)
                        }
                        drawPath(riverPath, Color(0xFF0F263B), style = Stroke(width = 22f * zoomLevel))

                        // Highways in Dark Mode
                        val highwayPath = Path().apply {
                            val p1 = transformOffset(-w * 0.5f, h * 0.32f)
                            val p2 = transformOffset(w * 0.48f, h * 0.45f)
                            val p3 = transformOffset(w * 1.5f, h * 0.58f)
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                        }
                        drawPath(highwayPath, Color(0xFF2C2F42), style = Stroke(width = 6f * zoomLevel))
                    }
                }

                // B. Village Outbreak Heat Radius & Google Maps Location Markers
                villages.forEach { village ->
                    val (vx, vy) = getVillageScreenCoordinates(village, w, h, zoomLevel, panOffset)
                    val isSelected = selectedVillage?.id == village.id

                    val pinColor = when {
                        village.highRiskCount > 0 -> Color(0xFFEA4335) // Google Maps Red
                        village.activeCases > 5 -> Color(0xFFFBBC04) // Google Maps Yellow/Amber
                        else -> Color(0xFF34A853) // Google Maps Green
                    }

                    val heatRadius = (max(village.activeCases, 3) * 6.5f * zoomLevel).coerceIn(24f, 85f)

                    // 1. Heat Radius Circle Overlay
                    drawCircle(
                        color = pinColor.copy(alpha = if (isSelected) 0.35f else 0.18f),
                        radius = heatRadius,
                        center = Offset(vx, vy)
                    )
                    drawCircle(
                        color = pinColor.copy(alpha = if (isSelected) 0.8f else 0.4f),
                        radius = heatRadius,
                        center = Offset(vx, vy),
                        style = Stroke(width = if (isSelected) 2.5f else 1.2f)
                    )

                    // 2. Google Maps Teardrop Pin Marker
                    val pinSize = if (isSelected) 32f else 24f

                    // Shadow underneath pin
                    drawCircle(
                        color = Color(0x44000000),
                        radius = pinSize * 0.45f,
                        center = Offset(vx, vy + pinSize * 0.2f)
                    )

                    // Pin Head
                    drawCircle(
                        color = pinColor,
                        radius = pinSize * 0.7f,
                        center = Offset(vx, vy - pinSize * 0.6f)
                    )
                    // Pin Inner White Eye
                    drawCircle(
                        color = Color.White,
                        radius = pinSize * 0.28f,
                        center = Offset(vx, vy - pinSize * 0.6f)
                    )
                    // Pin Tip Arrow
                    val tipPath = Path().apply {
                        moveTo(vx - pinSize * 0.4f, vy - pinSize * 0.45f)
                        lineTo(vx + pinSize * 0.4f, vy - pinSize * 0.45f)
                        lineTo(vx, vy)
                        close()
                    }
                    drawPath(tipPath, pinColor)
                }
            }

            // 2. Floating Marker Label Overlay Chips (Moves with Pan & Zoom)
            villages.forEach { village ->
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val (vx, vy) = getVillageScreenCoordinates(village, maxWidth.value, maxHeight.value, zoomLevel, panOffset)
                    val isSelected = selectedVillage?.id == village.id

                    val pinColor = when {
                        village.highRiskCount > 0 -> VS_Error
                        village.activeCases > 5 -> VS_Warning
                        else -> VS_Success
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (vx - 48).dp, y = (vy - 52).dp)
                            .clickable { onSelectVillage(village) }
                    ) {
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) VS_Primary else Color.White,
                            shadowElevation = 4.dp,
                            border = BorderStroke(1.dp, if (isSelected) Color.White else pinColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(pinColor)
                                )
                                Text(
                                    text = "${village.name} (${village.activeCases})",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF1E293B)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Top-Right Map Controls (Layer Switcher)
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
            ) {
                // Layer Selector Pill
                Surface(
                    shape = PillShape,
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        listOf(
                            MapLayerType.STANDARD to "🗺️ Map",
                            MapLayerType.SATELLITE to "🛰️ Satellite",
                            MapLayerType.DARK to "🌙 Night"
                        ).forEach { (type, title) ->
                            val active = mapLayer == type
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(if (active) VS_Primary else Color.Transparent)
                                    .clickable { mapLayer = type }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                        color = if (active) Color.White else Color(0xFF475569)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 4. Floating Zoom & Recenter Controls on Middle-Right
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing.xs),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Zoom In
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { zoomLevel = (zoomLevel + 0.3f).coerceAtMost(3.2f) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF334155))
                    }
                }

                // Zoom Out
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { zoomLevel = (zoomLevel - 0.3f).coerceAtLeast(0.7f) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("−", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF334155))
                    }
                }

                // Recenter Button
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable {
                            zoomLevel = 1.0f
                            panOffset = Offset.Zero
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📍", fontSize = 14.sp)
                    }
                }
            }

            // 5. Google Maps Bottom Watermark & Scale Bar
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White.copy(alpha = 0.85f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "G", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF4285F4))
                        Text(text = "o", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFFEA4335))
                        Text(text = "o", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFFFBBC04))
                        Text(text = "g", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF4285F4))
                        Text(text = "l", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF34A853))
                        Text(text = " " + stringResource(R.string.mapsLabel), fontWeight = FontWeight.Medium, fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                }

                Text(
                    text = stringResource(R.string.kmDragPan),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (mapLayer == MapLayerType.STANDARD) Color(0xFF475569) else Color(0xFF94A3B8)
                )
            }
        }

        // 6. Interactive Google Map Info Card (When a Village Marker is Selected)
        AnimatedVisibility(
            visible = selectedVillage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (selectedVillage != null) {
                Surface(
                    color = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📍 ${selectedVillage.name} (${selectedVillage.district})",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "Population: ${selectedVillage.population} · Active Cases: ${selectedVillage.activeCases} · Critical: ${selectedVillage.highRiskCount}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            SeverityBadge(
                                severity = if (selectedVillage.highRiskCount > 2) SeverityLevel.SEVERE else if (selectedVillage.highRiskCount > 0) SeverityLevel.HIGH else if (selectedVillage.activeCases > 5) SeverityLevel.MODERATE else SeverityLevel.LOW
                            )
                        }

                        Button(
                            onClick = { onBroadcastToVillage(selectedVillage) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary)
                        ) {
                            Text(
                                text = "📢 Send Advisory to ${selectedVillage.name} Residents",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getVillageScreenCoordinates(
    village: Village,
    width: Float,
    height: Float,
    zoom: Float,
    pan: Offset = Offset.Zero
): Pair<Float, Float> {
    val centerX = width / 2f
    val centerY = height / 2f

    val (baseXFraction, baseYFraction) = when (village.name.lowercase()) {
        "sundarpura" -> 0.28f to 0.42f
        "kalyanpur" -> 0.72f to 0.38f
        "bhimnagar" -> 0.48f to 0.75f
        else -> 0.50f to 0.50f
    }

    val unzoomedX = width * baseXFraction
    val unzoomedY = height * baseYFraction

    val zoomedX = centerX + (unzoomedX - centerX) * zoom + pan.x
    val zoomedY = centerY + (unzoomedY - centerY) * zoom + pan.y

    return zoomedX to zoomedY
}
