package com.vitalsense.app.feature.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LogoScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "LogoAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "PulseRing")
    val pulseRingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseRingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1900) // Show for 1.9 seconds for a smooth brand intro
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VS_Background,
                        VS_PrimaryContainer,
                        VS_Background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            // Glowing Animated "V" Logo Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale)
            ) {
                // Expanding Pulse Halo
                if (startAnimation) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(pulseRingScale)
                            .clip(CircleShape)
                            .background(VS_Primary.copy(alpha = pulseRingAlpha))
                    )
                }

                // Outer Soft Glow Ring
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(VS_PrimaryContainer)
                )

                // Main Core "V" Shield
                Surface(
                    shape = CircleShape,
                    color = VS_Primary,
                    border = BorderStroke(2.dp, VS_PrimaryContainer),
                    shadowElevation = 16.dp,
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "V",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 42.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            // Animated ECG Heartbeat Waveform
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(30.dp)
                    .scale(alpha),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    val w = size.width
                    val h = size.height
                    val midY = h / 2f

                    path.moveTo(0f, midY)
                    path.lineTo(w * 0.3f, midY)
                    path.lineTo(w * 0.38f, midY - 14f)
                    path.lineTo(w * 0.44f, midY + 18f)
                    path.lineTo(w * 0.52f, midY - 22f)
                    path.lineTo(w * 0.60f, midY + 12f)
                    path.lineTo(w * 0.68f, midY)
                    path.lineTo(w, midY)

                    drawPath(
                        path = path,
                        color = VS_Success,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }

            // Brand Typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                modifier = Modifier.scale(alpha)
            ) {
                Text(
                    text = "VitalSense",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        letterSpacing = 1.sp
                    ),
                    color = VS_OnBackground
                )
                Text(
                    text = "SEHAT SETU · सेहत सेतु",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = VS_PrimaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bridging Rural Healthcare · Zero-Internet Ready",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            }
        }

        // Bottom Footer: Government Mission & Encrypted Offline Badge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing.xl)
                .scale(alpha)
        ) {
            Surface(
                shape = PillShape,
                color = VS_SurfaceVariant,
                border = BorderStroke(1.dp, VS_Outline)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔒", fontSize = 11.sp)
                    Text(
                        text = "Encrypted Offline SQLite · ABHA Ready",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VS_OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}
