package com.vitalsense.app.feature.patient.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.VitalSenseDialog
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import kotlinx.coroutines.delay

@Composable
fun SensorPairingDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onReadingCaptured: (heartRate: Int, spO2: Int, bloodPressure: String, temperature: String) -> Unit,
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(1) }

    // Measured simulated vitals
    var liveHeartRate by remember { mutableStateOf(72) }
    var liveSpO2 by remember { mutableStateOf(98) }

    // Step 2 & 3 Auto-simulation
    LaunchedEffect(currentStep) {
        if (currentStep == 2) {
            val connectingText = when (language) {
                AppLanguage.HINDI -> "ब्लूटूथ सेंसर से कनेक्ट किया जा रहा है..."
                AppLanguage.TAMIL -> "புளூடூத் சென்சாருடன் இணைக்கப்படுகிறது..."
                AppLanguage.MARATHI -> "ब्लूटूथ सेन्सरशी जोडले जात आहे..."
                AppLanguage.ENGLISH -> "Connecting to Bluetooth sensor..."
            }
            AudioGuidanceHelper.speak(context = context, text = connectingText, language = language)
            delay(2800)
            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
            currentStep = 3
        } else if (currentStep == 3) {
            val capturingText = when (language) {
                AppLanguage.HINDI -> "रीडिंग ली जा रही है। कृपया शांत बैठें।"
                AppLanguage.TAMIL -> "அளவீடு எடுக்கப்படுகிறது. அமைதியாக இருங்கள்."
                AppLanguage.MARATHI -> "रीडिंग घेतली जात आहे. कृपया शांत बसा."
                AppLanguage.ENGLISH -> "Capturing live vitals. Please stay still."
            }
            AudioGuidanceHelper.speak(context = context, text = capturingText, language = language)
            for (i in 1..4) {
                delay(700)
                liveHeartRate = (72..78).random()
                liveSpO2 = (98..99).random()
            }
            delay(500)
            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
            currentStep = 4
        }
    }

    val dialogTitle = when (language) {
        AppLanguage.HINDI -> "🩺 ब्लूटूथ स्वास्थ्य सेंसर"
        AppLanguage.TAMIL -> "🩺 புளூடூத் சுகாதார சென்சார்"
        AppLanguage.MARATHI -> "🩺 ब्लूटूथ आरोग्य सेन्सर"
        AppLanguage.ENGLISH -> "🩺 Health Sensor"
    }

    val searchSensorBtn = when (language) {
        AppLanguage.HINDI -> "सेंसर खोजें (Step 2)"
        AppLanguage.TAMIL -> "சென்சாரைத் தேடு (படி 2)"
        AppLanguage.MARATHI -> "सेन्सर शोधा (Step 2)"
        AppLanguage.ENGLISH -> "Search Sensor"
    }

    val saveRecordBtn = when (language) {
        AppLanguage.HINDI -> "✓ स्वास्थ्य रिकॉर्ड में सहेजें"
        AppLanguage.TAMIL -> "✓ சுகாதாரப் பதிவில் சேமி"
        AppLanguage.MARATHI -> "✓ आरोग्य नोंदीत जतन करा"
        AppLanguage.ENGLISH -> "✓ Save to Health Record"
    }

    val cancelBtn = when (language) {
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.TAMIL -> "ரத்துசெய்"
        AppLanguage.MARATHI -> "रद्द करा"
        AppLanguage.ENGLISH -> "Cancel"
    }

    VitalSenseDialog(
        onDismissRequest = onDismiss,
        title = dialogTitle,
        icon = { Text("🩺", fontSize = 22.sp) },
        confirmButton = {
            if (currentStep == 1) {
                Button(
                    onClick = {
                        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
                        currentStep = 2
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(text = searchSensorBtn, style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            } else if (currentStep == 4) {
                Button(
                    onClick = {
                        onReadingCaptured(liveHeartRate, liveSpO2, "120/80", "98.4°F")
                        onDismiss()
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(text = saveRecordBtn, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = VS_Background)
                }
            }
        },
        dismissButton = {
            if (currentStep != 4) {
                TextButton(onClick = onDismiss, shape = PillShape) {
                    Text(text = cancelBtn, color = VS_OnSurfaceVariant, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            // 1. Progress Step Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val stepLabel = when (language) {
                    AppLanguage.HINDI -> "चरण $currentStep / 3:"
                    AppLanguage.TAMIL -> "படி $currentStep / 3:"
                    AppLanguage.MARATHI -> "टप्पा $currentStep / 3:"
                    AppLanguage.ENGLISH -> "Step $currentStep of 3:"
                }
                Text(
                    text = stepLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_PrimaryContainer
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..3) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (i <= currentStep) VS_Primary else VS_Outline)
                        )
                    }
                }
            }

            HorizontalDivider(color = VS_Outline)

            // Step Content
            when (currentStep) {
                1 -> {
                    val step1Title = when (language) {
                        AppLanguage.HINDI -> "अपनी उंगली में क्लिप लगाएं"
                        AppLanguage.TAMIL -> "உங்கள் விரலில் கிளிப்பைப் பொருத்தவும்"
                        AppLanguage.MARATHI -> "आपल्या बोटावर क्लिप लावा"
                        AppLanguage.ENGLISH -> "Put the clip on your finger"
                    }
                    val step1Desc = when (language) {
                        AppLanguage.HINDI -> "सेंसर क्लिप को तर्जनी उंगली में सुरक्षित लगाएं और बटन चालू करें।"
                        AppLanguage.TAMIL -> "பல்ஸ் ஆக்சிமீட்டர் கிளிப்பை உங்கள் ஆள்காட்டி விரலில் பொருத்தி ஆன் செய்யவும்."
                        AppLanguage.MARATHI -> "पल्स ऑक्सिमीटर क्लिप आपल्या तर्जनी बोटावर लावा आणि चालू करा."
                        AppLanguage.ENGLISH -> "Attach the pulse oximeter clip to your index finger and turn it on."
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(text = step1Title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CardShape)
                                .background(VS_SurfaceVariant)
                                .padding(Spacing.sm),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👆📎", fontSize = 48.sp)
                        }
                        Text(text = step1Desc, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }
                2 -> {
                    val step2Title = when (language) {
                        AppLanguage.HINDI -> "📶 सेंसर खोजा जा रहा है..."
                        AppLanguage.TAMIL -> "📶 சென்சார் தேடப்படுகிறது..."
                        AppLanguage.MARATHI -> "📶 सेन्सर शोधत आहे..."
                        AppLanguage.ENGLISH -> "📶 Searching for sensor..."
                    }
                    val step2Desc = when (language) {
                        AppLanguage.HINDI -> "ब्लूटूथ पल्स-ऑक्सीमीटर सिग्नल मिला। कनेक्ट हो रहा है..."
                        AppLanguage.TAMIL -> "புளூடூத் சிக்னல் கண்டறியப்பட்டது. இணைக்கப்படுகிறது..."
                        AppLanguage.MARATHI -> "ब्लूटूथ सिग्नल सापडला. जोडणी होत आहे..."
                        AppLanguage.ENGLISH -> "Bluetooth pulse oximeter beacon detected. Pairing..."
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(text = step2Title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(VS_PrimaryContainer.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = VS_PrimaryContainer, modifier = Modifier.size(54.dp))
                        }
                        Text(text = step2Desc, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }
                3 -> {
                    val step3Title = when (language) {
                        AppLanguage.HINDI -> "💓 लाइव धड़कन मापी जा रही है..."
                        AppLanguage.TAMIL -> "💓 நேரலை இதயத் துடிப்பு அளவிடப்படுகிறது..."
                        AppLanguage.MARATHI -> "💓 थेट हृदयाचे ठोके मोजले जात आहेत..."
                        AppLanguage.ENGLISH -> "💓 Reading live heartbeat..."
                    }
                    val step3Desc = when (language) {
                        AppLanguage.HINDI -> "कृपया हिलें-डुलें नहीं। रीडिंग स्थिर हो रही है..."
                        AppLanguage.TAMIL -> "அசையாமல் இருங்கள். அளவீடு பதிவு செய்யப்படுகிறது..."
                        AppLanguage.MARATHI -> "कृपया हलचाल करू नका. रीडिंग स्थिर होत आहे..."
                        AppLanguage.ENGLISH -> "Please stay still. Stabilizing reading..."
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(text = step3Title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnBackground)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CardShape,
                                color = VS_SurfaceVariant,
                                border = BorderStroke(1.dp, VS_Outline),
                                modifier = Modifier.padding(Spacing.xs)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "$liveHeartRate", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = VS_Error)
                                    Text(text = "BPM", style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                                }
                            }
                            Surface(
                                shape = CardShape,
                                color = VS_SurfaceVariant,
                                border = BorderStroke(1.dp, VS_Outline),
                                modifier = Modifier.padding(Spacing.xs)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "$liveSpO2%", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = VS_Success)
                                    Text(text = stringResource(R.string.spo2Label), style = MaterialTheme.typography.labelSmall, color = VS_OnSurfaceVariant)
                                }
                            }
                        }
                        Text(text = step3Desc, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }
                4 -> {
                    val step4Title = when (language) {
                        AppLanguage.HINDI -> "✅ रीडिंग सफलतापूर्वक पूरी हुई!"
                        AppLanguage.TAMIL -> "✅ அளவீடு வெற்றிகரமாக முடிந்தது!"
                        AppLanguage.MARATHI -> "✅ रीडिंग यशस्वीरित्या पूर्ण झाली!"
                        AppLanguage.ENGLISH -> "✅ Reading Completed Successfully!"
                    }
                    val step4Desc = when (language) {
                        AppLanguage.HINDI -> "हृदय गति: $liveHeartRate BPM • ऑक्सीजन: $liveSpO2% • रक्तचाप: 120/80"
                        AppLanguage.TAMIL -> "இதயத் துடிப்பு: $liveHeartRate BPM • ஆக்சிஜன்: $liveSpO2% • இரத்த அழுத்தம்: 120/80"
                        AppLanguage.MARATHI -> "हृदयाचे ठोके: $liveHeartRate BPM • ऑक्सिजन: $liveSpO2% • रक्तदाब: 120/80"
                        AppLanguage.ENGLISH -> "Heart Rate: $liveHeartRate BPM • SpO2: $liveSpO2% • BP: 120/80"
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(text = step4Title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_Success)
                        Text(text = step4Desc, style = MaterialTheme.typography.bodyMedium, color = VS_OnBackground, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
