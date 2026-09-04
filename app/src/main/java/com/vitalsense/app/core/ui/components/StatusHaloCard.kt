package com.vitalsense.app.core.ui.components

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper

@Composable
fun StatusHaloCard(
    patient: Patient,
    heartRate: Int = 76,
    spO2: Int = 98,
    bloodPressure: String = "120/80",
    temperature: String = "98.4°F",
    onTakeReadingClick: () -> Unit = {},
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val haloColor by animateColorAsState(
        targetValue = when (patient.currentRiskLevel) {
            SeverityLevel.LOW -> VS_Success
            SeverityLevel.MODERATE -> VS_Warning
            SeverityLevel.HIGH, SeverityLevel.SEVERE -> VS_Error
        },
        animationSpec = tween(durationMillis = 600),
        label = "HaloColorAnim"
    )

    val verdictWord = when (patient.currentRiskLevel) {
        SeverityLevel.LOW -> when (language) {
            AppLanguage.HINDI -> "ठीक हैं"
            AppLanguage.TAMIL -> "நலமாக உள்ளீர்கள்"
            AppLanguage.MARATHI -> "ठीक आहात"
            AppLanguage.ENGLISH -> "You're Fine"
        }
        SeverityLevel.MODERATE -> when (language) {
            AppLanguage.HINDI -> "ध्यान दें"
            AppLanguage.TAMIL -> "கவனம் தேவை"
            AppLanguage.MARATHI -> "लक्ष द्या"
            AppLanguage.ENGLISH -> "Pay Attention"
        }
        SeverityLevel.HIGH, SeverityLevel.SEVERE -> when (language) {
            AppLanguage.HINDI -> "तुरंत मदद लें"
            AppLanguage.TAMIL -> "உதவி பெறவும்"
            AppLanguage.MARATHI -> "तातडीने मदत घ्या"
            AppLanguage.ENGLISH -> "Get Help Now"
        }
    }

    val verdictSubtitle = when (patient.currentRiskLevel) {
        SeverityLevel.LOW -> when (language) {
            AppLanguage.HINDI -> "सभी स्वास्थ्य पैरामीटर सामान्य हैं"
            AppLanguage.TAMIL -> "அனைத்து முக்கிய அளவீடுகளும் இயல்பாக உள்ளன"
            AppLanguage.MARATHI -> "सर्व आरोग्य मापदंड सामान्य आहेत"
            AppLanguage.ENGLISH -> "All vital signs are healthy"
        }
        SeverityLevel.MODERATE -> when (language) {
            AppLanguage.HINDI -> "परामर्श की आवश्यकता हो सकती है"
            AppLanguage.TAMIL -> "மருத்துவ ஆலோசனை தேவைப்படலாம்"
            AppLanguage.MARATHI -> "सल्लामसलतीची आवश्यकता असू शकते"
            AppLanguage.ENGLISH -> "May require consultation"
        }
        SeverityLevel.HIGH, SeverityLevel.SEVERE -> when (language) {
            AppLanguage.HINDI -> "तत्काल डॉक्टर संपर्क करें"
            AppLanguage.TAMIL -> "உடனடியாக மருத்துவரைத் தொடர்பு கொள்ளவும்"
            AppLanguage.MARATHI -> "त्वरित डॉक्टरांशी संपर्क साधा"
            AppLanguage.ENGLISH -> "Immediate consultation advised"
        }
    }

    val greetingText = when (language) {
        AppLanguage.HINDI -> "नमस्ते, ${patient.name} जी 🙏"
        AppLanguage.TAMIL -> "வணக்கம், ${patient.name} 🙏"
        AppLanguage.MARATHI -> "नमस्ते, ${patient.name} जी 🙏"
        AppLanguage.ENGLISH -> "Hello, ${patient.name} 👋"
    }

    val syncedText = when (language) {
        AppLanguage.HINDI -> "सुरक्षित"
        AppLanguage.TAMIL -> "பாதுகாக்கப்பட்டது"
        AppLanguage.MARATHI -> "सुरक्षित"
        AppLanguage.ENGLISH -> "Synced"
    }

    val normalStatusText = when (language) {
        AppLanguage.HINDI -> "सामान्य"
        AppLanguage.TAMIL -> "இயல்பு"
        AppLanguage.MARATHI -> "सामान्य"
        AppLanguage.ENGLISH -> "Normal"
    }

    val heartRateLabel = when (language) {
        AppLanguage.HINDI -> "दिल की धड़कन"
        AppLanguage.TAMIL -> "இதயத் துடிப்பு"
        AppLanguage.MARATHI -> "हृदयाचे ठोके"
        AppLanguage.ENGLISH -> "Heart Rate"
    }

    val spo2Label = when (language) {
        AppLanguage.HINDI -> "ऑक्सीजन (SpO2)"
        AppLanguage.TAMIL -> "ஆக்சிஜன் (SpO2)"
        AppLanguage.MARATHI -> "ऑक्सिजन (SpO2)"
        AppLanguage.ENGLISH -> "Oxygen (SpO2)"
    }

    val bpLabel = when (language) {
        AppLanguage.HINDI -> "रक्तचाप (BP)"
        AppLanguage.TAMIL -> "இரத்த அழுத்தம் (BP)"
        AppLanguage.MARATHI -> "रक्तदाब (BP)"
        AppLanguage.ENGLISH -> "Blood Pressure"
    }

    val tempLabel = when (language) {
        AppLanguage.HINDI -> "तापमान (Temp)"
        AppLanguage.TAMIL -> "உடல் வெப்பநிலை"
        AppLanguage.MARATHI -> "तापमान (Temp)"
        AppLanguage.ENGLISH -> "Temperature"
    }

    val listenBtnText = when (language) {
        AppLanguage.HINDI -> "सुनें (Listen)"
        AppLanguage.TAMIL -> "கேளுங்கள்"
        AppLanguage.MARATHI -> "ऐका"
        AppLanguage.ENGLISH -> "Listen"
    }

    val takeReadingBtnText = when (language) {
        AppLanguage.HINDI -> "🩺 नई रीडिंग लें"
        AppLanguage.TAMIL -> "🩺 புதிய அளவீடு எடு"
        AppLanguage.MARATHI -> "🩺 नवीन रीडिंग घ्या"
        AppLanguage.ENGLISH -> "🩺 Take Reading"
    }

    VitalSenseCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, haloColor.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            // Header Bar: Greeting & Sync Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greetingText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${patient.villageName} · ASHA: ${patient.ashaWorkerName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Sync Status Chip
                Surface(
                    shape = PillShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("☁️✓", fontSize = 11.sp, color = VS_Primary)
                        Text(
                            text = syncedText,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 1. THE STATUS HALO (Hero Circle Element)
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(haloColor.copy(alpha = 0.12f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(134.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when (patient.currentRiskLevel) {
                                SeverityLevel.LOW -> "🟢"
                                SeverityLevel.MODERATE -> "🟡"
                                SeverityLevel.HIGH, SeverityLevel.SEVERE -> "🔴"
                            },
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = verdictWord,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            ),
                            color = haloColor
                        )
                        Text(
                            text = when (patient.currentRiskLevel) {
                                SeverityLevel.LOW -> "(Safe)"
                                SeverityLevel.MODERATE -> "(Attention)"
                                SeverityLevel.HIGH, SeverityLevel.SEVERE -> "(Urgent)"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = verdictSubtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // 2. 2x2 VITAL TILES
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalTile(
                        icon = "❤️",
                        label = heartRateLabel,
                        value = "$heartRate bpm",
                        status = normalStatusText,
                        statusColor = VS_Primary,
                        modifier = Modifier.weight(1f)
                    )

                    VitalTile(
                        icon = "🫁",
                        label = spo2Label,
                        value = "$spO2%",
                        status = normalStatusText,
                        statusColor = VS_Primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    VitalTile(
                        icon = "💧",
                        label = bpLabel,
                        value = bloodPressure,
                        status = normalStatusText,
                        statusColor = VS_Primary,
                        modifier = Modifier.weight(1f)
                    )

                    VitalTile(
                        icon = "🌡️",
                        label = tempLabel,
                        value = temperature,
                        status = normalStatusText,
                        statusColor = VS_Primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bottom Action Bar: Audio Narration & Take a Reading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = PillShape,
                    color = VS_PrimaryContainer,
                    modifier = Modifier.clickable {
                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                        val speech = AudioGuidanceHelper.getSpokenHealthSummary(
                            patientName = patient.name,
                            severity = patient.currentRiskLevel,
                            heartRate = heartRate,
                            spO2 = spO2,
                            language = language
                        )
                        AudioGuidanceHelper.speak(context, speech, language)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🔊", fontSize = 14.sp)
                        Text(
                            text = listenBtnText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = VS_Primary
                            )
                        )
                    }
                }

                Button(
                    onClick = onTakeReadingClick,
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    contentPadding = PaddingValues(horizontal = Spacing.md, vertical = 6.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Text(
                        text = takeReadingBtnText,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun VitalTile(
    icon: String,
    label: String,
    value: String,
    status: String,
    statusColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = modifier.defaultMinSize(minHeight = 64.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(icon, fontSize = 16.sp)
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = statusColor
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
