package com.vitalsense.app.feature.patient.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
fun SmartEmergencyDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onSosDispatched: () -> Unit,
    isOffline: Boolean = false,
    language: AppLanguage = AppLanguage.ENGLISH,
    onInitiateVoiceCall: (() -> Unit)? = null,
    onInitiateVideoCall: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var countdownSeconds by remember { mutableStateOf(3) }
    var isCountdownActive by remember { mutableStateOf(true) }
    var alertDispatched by remember { mutableStateOf(false) }

    val countdownText = when (language) {
        AppLanguage.HINDI -> "आपातकालीन अलर्ट $countdownSeconds सेकंड में भेजा जाएगा"
        AppLanguage.TAMIL -> "$countdownSeconds வினாடிகளில் அவசர எச்சரிக்கை அனுப்பப்படும்"
        AppLanguage.MARATHI -> "$countdownSeconds सेकंदात आपत्कालीन अलर्ट पाठवला जाईल"
        AppLanguage.ENGLISH -> "Emergency alert will be dispatched in $countdownSeconds seconds"
    }

    val alertDispatchedSpoken = if (isOffline) {
        when (language) {
            AppLanguage.HINDI -> "डिवाइस ऑफलाइन है। आपातकालीन अलर्ट लोकल सेव किया गया है। तुरंत 108 पर कॉल करें या आशा को SMS भेजें।"
            AppLanguage.TAMIL -> "சாதனம் ஆஃப்லைனில் உள்ளது. அவசர எச்சரிக்கை உள்நாட்டில் சேமிக்கப்பட்டது. 108 ஐ அழைக்கவும் அல்லது SMS அனுப்பவும்."
            AppLanguage.MARATHI -> "डिव्हाइस ऑफलाइन आहे. आपत्कालीन अलर्ट स्थानिक पातळीवर साठवला आहे. त्वरित १०८ वर कॉल करा किंवा आशा सेविकेला SMS पाठवा."
            AppLanguage.ENGLISH -> "Device is offline. Alert saved locally. Please use 1-Tap 108 Call or SMS below."
        }
    } else {
        when (language) {
            AppLanguage.HINDI -> "आपातकालीन सहायता भेजी गई। आशा कार्यकर्ता और 108 एम्बुलेंस को सूचित कर दिया गया है।"
            AppLanguage.TAMIL -> "அவசர உதவி கோரப்பட்டது. ஆஷா மற்றும் 108 ஆம்புலன்ஸுக்கு தகவல் தெரிவிக்கப்பட்டது."
            AppLanguage.MARATHI -> "आपत्कालीन मदत पाठवली गेली. आशा सेविका व १०८ रुग्णवाहिकेला माहिती दिली आहे."
            AppLanguage.ENGLISH -> "Emergency alert sent. ASHA worker and 108 ambulance notified."
        }
    }

    LaunchedEffect(Unit) {
        AudioGuidanceHelper.speak(context = context, text = countdownText, language = language)
        AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = false)

        while (countdownSeconds > 0 && isCountdownActive) {
            delay(1000)
            countdownSeconds--
        }

        if (isCountdownActive && countdownSeconds == 0) {
            alertDispatched = true
            isCountdownActive = false
            onSosDispatched()
            AudioGuidanceHelper.speak(context = context, text = alertDispatchedSpoken, language = language)
            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
        }
    }

    val dialogTitle = when (language) {
        AppLanguage.HINDI -> "🚨 आपातकालीन सहायता (SOS)"
        AppLanguage.TAMIL -> "🚨 அவசர உதவி (SOS)"
        AppLanguage.MARATHI -> "🚨 आपत्कालीन मदत (SOS)"
        AppLanguage.ENGLISH -> "🚨 Emergency SOS"
    }

    val cancelAlertText = when (language) {
        AppLanguage.HINDI -> "✕ अभी रोकें (Cancel Alert)"
        AppLanguage.TAMIL -> "✕ நிறுத்து (Cancel Alert)"
        AppLanguage.MARATHI -> "✕ आत्ताच थांबवा (Cancel Alert)"
        AppLanguage.ENGLISH -> "✕ Cancel Alert"
    }

    val closeText = when (language) {
        AppLanguage.HINDI -> "समझ गया (Close)"
        AppLanguage.TAMIL -> "புரிந்தது (Close)"
        AppLanguage.MARATHI -> "समजले (Close)"
        AppLanguage.ENGLISH -> "Close"
    }

    VitalSenseDialog(
        onDismissRequest = {
            isCountdownActive = false
            onDismiss()
        },
        title = dialogTitle,
        icon = { Text("🚨", fontSize = 24.sp) },
        confirmButton = {
            if (isCountdownActive) {
                Button(
                    onClick = {
                        isCountdownActive = false
                        onDismiss()
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_SurfaceVariant),
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 46.dp)
                ) {
                    Text(
                        text = cancelAlertText,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                }
            } else {
                Button(
                    onClick = onDismiss,
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(text = closeText, style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        },
        dismissButton = {}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            if (isCountdownActive) {
                val sendingInText = when (language) {
                    AppLanguage.HINDI -> "आपातकालीन अलर्ट भेजा जा रहा है:"
                    AppLanguage.TAMIL -> "அவசர எச்சரிக்கை அனுப்பப்படுகிறது:"
                    AppLanguage.MARATHI -> "आपत्कालीन अलर्ट पाठवला जात आहे:"
                    AppLanguage.ENGLISH -> "Emergency Alert Sending in:"
                }
                val accidentalText = when (language) {
                    AppLanguage.HINDI -> "गलती से दबा? 'अभी रोकें' बटन दबाएं।"
                    AppLanguage.TAMIL -> "தவறாக அழுத்தப்பட்டதா? 'நிறுத்து' என்பதைத் தொடவும்."
                    AppLanguage.MARATHI -> "चुकीने दाबले गेले? 'थांबवा' बटण दाबा."
                    AppLanguage.ENGLISH -> "Accidental press? Tap Cancel below."
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(text = sendingInText, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_Error)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(VS_Error.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "$countdownSeconds", fontSize = 44.sp, fontWeight = FontWeight.Black, color = VS_Error)
                    }
                    Text(text = accidentalText, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant, textAlign = TextAlign.Center)
                }
            } else {
                if (isOffline) {
                    val offlineBannerTitle = when (language) {
                        AppLanguage.HINDI -> "⚠️ डिवाइस ऑफलाइन है"
                        AppLanguage.TAMIL -> "⚠️ சாதனம் ஆஃப்லைனில் உள்ளது"
                        AppLanguage.MARATHI -> "⚠️ डिव्हाइस ऑफलाइन आहे"
                        AppLanguage.ENGLISH -> "⚠️ Device is Offline"
                    }
                    val offlineBannerMsg = when (language) {
                        AppLanguage.HINDI -> "अलर्ट स्थानीय आउटबॉक्स में सहेजा गया है। तुरंत सहायता के लिए नीचे 108 कॉल या SMS विकल्प का उपयोग करें।"
                        AppLanguage.TAMIL -> "எச்சரிக்கை உள்ளூர் அவுட்பாக்ஸில் சேமிக்கப்பட்டது. உடனடி உதவிக்கு 108 அழைப்பு அல்லது SMS ஐப் பயன்படுத்தவும்."
                        AppLanguage.MARATHI -> "अलर्ट स्थानिक आऊटबॉक्समध्ये जतन केला आहे. तात्काळ मदतीसाठी खालील १०८ कॉल किंवा SMS पर्यायांचा वापर करा."
                        AppLanguage.ENGLISH -> "Alert queued in local outbox. Use 1-Tap zero-internet fallbacks below for immediate help."
                    }
                    Surface(
                        color = Color(0xFFFFF3CD),
                        shape = CardShape,
                        border = BorderStroke(1.dp, Color(0xFFFFC107)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text(
                                text = offlineBannerTitle,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF856404)
                            )
                            Text(
                                text = offlineBannerMsg,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF856404)
                            )
                        }
                    }
                } else {
                    val helpOnWayText = when (language) {
                        AppLanguage.HINDI -> "सहायता रास्ते में है!"
                        AppLanguage.TAMIL -> "உதவி வழியில் உள்ளது!"
                        AppLanguage.MARATHI -> "मदत येत आहे!"
                        AppLanguage.ENGLISH -> "Help is on the way!"
                    }
                    val dispatchedDesc = when (language) {
                        AppLanguage.HINDI -> "आशा कार्यकर्ता (${patient.ashaWorkerName}) और 108 एम्बुलेंस को तत्काल अलर्ट भेजा गया।"
                        AppLanguage.TAMIL -> "ஆஷா பணியாளர் (${patient.ashaWorkerName}) மற்றும் 108 ஆம்புலன்ஸுக்கு அவசர எச்சரிக்கை அனுப்பப்பட்டது."
                        AppLanguage.MARATHI -> "आशा सेविका (${patient.ashaWorkerName}) आणि १०८ रुग्णवाहिकेला तात्काळ अलर्ट पाठवला गेला."
                        AppLanguage.ENGLISH -> "Alert dispatched to ASHA (${patient.ashaWorkerName}) and 108 Ambulance."
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Text(text = "🚨", fontSize = 42.sp)
                        Text(text = helpOnWayText, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_Error)
                        Text(text = dispatchedDesc, style = MaterialTheme.typography.bodySmall, color = VS_OnSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Zero-Internet Fallback 1: Direct 108 Call
                val call108Text = when (language) {
                    AppLanguage.HINDI -> "📞 तुरंत 108 एम्बुलेंस कॉल करें"
                    AppLanguage.TAMIL -> "📞 108 ஆம்புலன்ஸை உடனடியாக அழைக்கவும்"
                    AppLanguage.MARATHI -> "📞 तात्काळ १०८ रुग्णवाहिकेला कॉल करा"
                    AppLanguage.ENGLISH -> "📞 Call 108 Ambulance Now"
                }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                        context.startActivity(intent)
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Error),
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(text = call108Text, color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                // Zero-Internet Fallback 2: Direct SMS to ASHA with emergency coordinates
                val smsAshaText = when (language) {
                    AppLanguage.HINDI -> "📱 आशा कार्यकर्ता को SMS भेजें"
                    AppLanguage.TAMIL -> "📱 ஆஷா பணியாளருக்கு SMS அனுப்பவும்"
                    AppLanguage.MARATHI -> "📱 आशा सेविकेला SMS पाठवा"
                    AppLanguage.ENGLISH -> "📱 SMS ASHA Worker Directly"
                }
                Button(
                    onClick = {
                        val ashaPhone = if (patient.emergencyContact.isNotBlank()) patient.emergencyContact else "9876543210"
                        val smsBody = "EMERGENCY ALERT: Patient ${patient.name} (${patient.villageName}), Phone: ${patient.phone}, Condition: ${patient.lastCondition}. Please respond urgently."
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$ashaPhone")).apply {
                            putExtra("sms_body", smsBody)
                        }
                        context.startActivity(intent)
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(text = smsAshaText, color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
