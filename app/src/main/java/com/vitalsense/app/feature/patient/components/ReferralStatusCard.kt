package com.vitalsense.app.feature.patient.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Referral
import com.vitalsense.app.core.data.model.ReferralStatus
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper

@Composable
fun ReferralStatusCard(
    referral: Referral,
    language: AppLanguage,
    onCardClick: () -> Unit = {},
    onScheduleCall: () -> Unit = onCardClick,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Icon based on specialty
    val iconEmoji = when {
        referral.targetSpecialty.contains("Cardio", ignoreCase = true) -> "🩺 ➔ 🫀"
        referral.targetSpecialty.contains("Derma", ignoreCase = true) -> "🩺 ➔ 🔬"
        referral.targetSpecialty.contains("Pedia", ignoreCase = true) -> "🩺 ➔ 👶"
        referral.targetSpecialty.contains("Gynae", ignoreCase = true) || referral.targetSpecialty.contains("Maternal", ignoreCase = true) -> "🩺 ➔ 🌸"
        referral.targetSpecialty.contains("Ortho", ignoreCase = true) -> "🩺 ➔ 🦴"
        referral.targetSpecialty.contains("Psych", ignoreCase = true) || referral.targetSpecialty.contains("Mental", ignoreCase = true) -> "🩺 ➔ 🧠"
        else -> "🩺 ➔ 👨‍⚕️"
    }

    val (titleText, subtitleText, speakText) = when (referral.status) {
        ReferralStatus.CREATED, ReferralStatus.SENT -> {
            when (language) {
                AppLanguage.HINDI -> Triple(
                    "विशेषज्ञ डॉक्टर को रेफ़रल भेजा गया",
                    "आपके डॉक्टर ने आपके इलाज के लिए ${referral.targetSpecialty} विशेषज्ञ को अनुरोध भेजा है। विशेषज्ञ डॉक्टर जल्द समीक्षा करेंगे।",
                    "नमस्ते। आपके डॉक्टर ने आपको विशेषज्ञ के पास रेफ़र किया है। डॉक्टर जल्द ही आपकी रिपोर्ट देखकर संपर्क करेंगे।"
                )
                AppLanguage.TAMIL -> Triple(
                    "சிறப்பு மருத்துவருக்குப் பரிந்துரை அனுப்பப்பட்டது",
                    "உங்கள் மருத்துவர் ${referral.targetSpecialty} சிறப்பு மருத்துவரிடம் ஆலோசனைக் கோரியுள்ளார். விரைவில் ஆய்வு செய்யப்படும்.",
                    "வணக்கம். உங்கள் மருத்துவர் உங்களை சிறப்பு மருத்துவரிடம் பரிந்துரைத்துள்ளார். விரைவில் உங்கள் விவரங்கள் ஆய்வு செய்யப்படும்."
                )
                AppLanguage.MARATHI -> Triple(
                    "तज्ज्ञ डॉक्टरांकडे रेफरल पाठवले गेले",
                    "आपल्या डॉक्टरांनी आपल्या उपचारासाठी ${referral.targetSpecialty} तज्ज्ञांकडे विचारणा पाठवली आहे. तज्ज्ञ डॉक्टर लवकरच तपासणी करतील.",
                    "नमस्ते. आपल्या डॉक्टरांनी आपल्याला तज्ज्ञ डॉक्टरांकडे पाठवले आहे. लवकरच ते आपल्या अहवालांची तपासणी करतील."
                )
                AppLanguage.ENGLISH -> Triple(
                    "Referred to Specialist Doctor",
                    "Your doctor has connected you with a ${referral.targetSpecialty} specialist for advanced care review.",
                    "Hello. Your doctor has referred you to a specialist for further evaluation. The specialist will review your case shortly."
                )
            }
        }
        ReferralStatus.ACCEPTED, ReferralStatus.APPOINTMENT_SCHEDULED, ReferralStatus.PATIENT_REACHED, ReferralStatus.IN_PROGRESS -> {
            when (language) {
                AppLanguage.HINDI -> Triple(
                    "विशेषज्ञ ने आपका रेफ़रल स्वीकार किया",
                    "डॉक्टर ${referral.targetDoctorName ?: referral.targetSpecialty} ने समीक्षा स्वीकार कर ली है। जल्द ही आपसे परामर्श होगा।",
                    "विशेषज्ञ डॉक्टर ने आपका अनुरोध स्वीकार कर लिया है। जल्द ही आपसे वीडियो या वॉयस कॉल पर परामर्श होगा।"
                )
                AppLanguage.TAMIL -> Triple(
                    "சிறப்பு மருத்துவர் வழக்கை ஏற்றுக்கொண்டார்",
                    "மருத்துவர் ${referral.targetDoctorName ?: referral.targetSpecialty} உங்கள் வழக்கை ஏற்றுக்கொண்டார். விரைவில் ஆலோசனை தொடங்கும்.",
                    "சிறப்பு மருத்துவர் உங்கள் வழக்கை ஏற்றுக்கொண்டார். விரைவில் உங்களுடன் ஆலோசனை நடத்தப்படும்."
                )
                AppLanguage.MARATHI -> Triple(
                    "तज्ज्ञ डॉक्टरांनी तपासणी स्वीकारली",
                    "डॉक्टर ${referral.targetDoctorName ?: referral.targetSpecialty} यांनी तपासणी स्वीकारली आहे. लवकरच सल्लामसलत होईल.",
                    "तज्ज्ञ डॉक्टरांनी आपले प्रकरण स्वीकारले आहे. लवकरच कॉलद्वारे सल्ला दिला जाईल."
                )
                AppLanguage.ENGLISH -> Triple(
                    "Specialist Accepted Your Case",
                    "Dr. ${referral.targetDoctorName ?: referral.targetSpecialty} has accepted your referral. Consultation will take place soon.",
                    "The specialist doctor has accepted your case. A consultation call will be initiated shortly."
                )
            }
        }
        ReferralStatus.CONSULTATION_COMPLETED, ReferralStatus.FOLLOW_UP, ReferralStatus.COMPLETED -> {
            when (language) {
                AppLanguage.HINDI -> Triple(
                    "विशेषज्ञ डॉक्टर की सलाह प्राप्त हुई",
                    referral.specialistRecommendations ?: "विशेषज्ञ की रिपोर्ट आपके डॉक्टर तक पहुंच गई है।",
                    "विशेषज्ञ डॉक्टर की रिपोर्ट और सलाह मिल गई है। आपका इलाज जारी है।"
                )
                AppLanguage.TAMIL -> Triple(
                    "சிறப்பு மருத்துவரின் ஆலோசனைகள் பெறப்பட்டன",
                    referral.specialistRecommendations ?: "சிறப்பு மருத்துவரின் அறிக்கை உங்கள் மருத்துவருக்கு அனுப்பப்பட்டது.",
                    "சிறப்பு மருத்துவரின் அறிக்கை மற்றும் ஆலோசனைகள் பெறப்பட்டு உங்கள் பதிவேட்டில் சேர்க்கப்பட்டன."
                )
                AppLanguage.MARATHI -> Triple(
                    "तज्ज्ञ डॉक्टरांचा सल्ला प्राप्त झाला",
                    referral.specialistRecommendations ?: "तज्ज्ञांचा अहवाल आपल्या डॉक्टरांपर्यंत पोहोचला आहे.",
                    "तज्ज्ञ डॉक्टरांचा अहवाल आणि सल्ला मिळाला आहे. पुढील उपचार सुरू आहेत."
                )
                AppLanguage.ENGLISH -> Triple(
                    "Specialist Advice & Plan Received",
                    referral.specialistRecommendations ?: "The specialist has closed the loop with your primary doctor.",
                    "Specialist advice and recommendations have been received and added to your health chart."
                )
            }
        }
        ReferralStatus.INFO_REQUESTED -> {
            when (language) {
                AppLanguage.HINDI -> Triple(
                    "डॉक्टर आपकी रिपोर्ट की जांच कर रहे हैं",
                    "विशेषज्ञ ने अतिरिक्त विवरण मांगे हैं। आपके डॉक्टर इसे अपडेट कर रहे हैं।",
                    "डॉक्टर आपकी जांच रिपोर्ट का मिलान कर रहे हैं।"
                )
                AppLanguage.TAMIL -> Triple(
                    "மருத்துவர்கள் அறிக்கைகளை ஆய்வு செய்கின்றனர்",
                    "சிறப்பு மருத்துவர் கூடுதல் விவரங்களைக் கோரியுள்ளார்.",
                    "மருத்துவர்கள் உங்கள் மருத்துவ அறிக்கைகளை ஆய்வு செய்து வருகின்றனர்."
                )
                AppLanguage.MARATHI -> Triple(
                    "डॉक्टर अहवालांची फेरतपासणी करत आहेत",
                    "तज्ज्ञ डॉक्टरांनी अधिक माहिती मागितली आहे. आपले डॉक्टर ती अद्यतनित करत आहेत.",
                    "डॉक्टर तपासणी अहवालांची पडताळणी करत आहेत."
                )
                AppLanguage.ENGLISH -> Triple(
                    "Doctors Reviewing Records",
                    "The specialist requested additional diagnostic details from your doctor.",
                    "Doctors are reviewing your medical investigation reports."
                )
            }
        }
        ReferralStatus.DECLINED -> {
            when (language) {
                AppLanguage.HINDI -> Triple(
                    "रेफ़रल का पुनः निर्धारण",
                    "इस विभाग में सीट उपलब्ध न होने के कारण दूसरे विशेषज्ञ को भेजा जा रहा है।",
                    "दूसरे विशेषज्ञ डॉक्टर से संपर्क किया जा रहा है।"
                )
                AppLanguage.TAMIL -> Triple(
                    "பரிந்துரை மறுஒதுக்கீடு",
                    "மாற்று சிறப்பு மருத்துவத் துறைக்கு பரிந்துரை மாற்றப்படுகிறது.",
                    "உங்கள் பரிந்துரை வேறொரு சிறப்பு மருத்துவருக்கு மாற்றப்படுகிறது."
                )
                AppLanguage.MARATHI -> Triple(
                    "रेफरलचे पुनर्नियोजन",
                    "दुसऱ्या तज्ज्ञ विभागाकडे रेफरल पाठवले जात आहे.",
                    "आपले रेफरल पर्यायी तज्ज्ञ डॉक्टरांकडे पाठवले जात आहे."
                )
                AppLanguage.ENGLISH -> Triple(
                    "Referral Rerouting",
                    "Rerouting to alternate specialist department.",
                    "Your referral is being reassigned to another specialist."
                )
            }
        }
        else -> {
            Triple("Specialist Referral", "Active clinical coordination", "Active referral in progress")
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (referral.status) {
                ReferralStatus.COMPLETED -> VS_Success.copy(alpha = 0.08f)
                ReferralStatus.ACCEPTED, ReferralStatus.IN_PROGRESS -> VS_PrimaryContainer.copy(alpha = 0.35f)
                else -> VS_Surface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when (referral.status) {
                ReferralStatus.COMPLETED -> VS_Success.copy(alpha = 0.4f)
                ReferralStatus.ACCEPTED, ReferralStatus.IN_PROGRESS -> VS_PrimaryContainer.copy(alpha = 0.5f)
                else -> VS_Outline
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = iconEmoji, fontSize = 20.sp)
                    Column {
                        Text(
                            text = titleText,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${referral.targetSpecialty} • Dr. ${referral.referringUserName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Audio Guidance Play Button
                Surface(
                    onClick = {
                        AudioGuidanceHelper.speak(
                            context = context,
                            text = speakText,
                            language = language
                        )
                    },
                    shape = PillShape,
                    color = VS_Primary.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🔊", fontSize = 16.sp)
                    }
                }
            }

            Text(
                text = subtitleText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = PillShape,
                    color = when (referral.status) {
                        ReferralStatus.COMPLETED -> VS_Success.copy(alpha = 0.2f)
                        ReferralStatus.ACCEPTED, ReferralStatus.IN_PROGRESS -> VS_PrimaryContainer
                        else -> VS_WarningContainer
                    }
                ) {
                    Text(
                        text = when (referral.status) {
                            ReferralStatus.COMPLETED -> when (language) {
                                AppLanguage.HINDI -> "✓ पूर्ण (सलाह दर्ज)"
                                AppLanguage.TAMIL -> "✓ முடிந்தது"
                                AppLanguage.MARATHI -> "✓ पूर्ण (सल्ला प्राप्त)"
                                AppLanguage.ENGLISH -> "✓ Completed"
                            }
                            ReferralStatus.ACCEPTED -> when (language) {
                                AppLanguage.HINDI -> "स्वीकार किया गया"
                                AppLanguage.TAMIL -> "ஏற்றுக்கொள்ளப்பட்டது"
                                AppLanguage.MARATHI -> "स्वीकारले गेले"
                                AppLanguage.ENGLISH -> "Accepted"
                            }
                            ReferralStatus.SENT -> when (language) {
                                AppLanguage.HINDI -> "समीक्षा जारी"
                                AppLanguage.TAMIL -> "ஆய்வு நிலுவையில்"
                                AppLanguage.MARATHI -> "तपासणी सुरू"
                                AppLanguage.ENGLISH -> "Pending Review"
                            }
                            else -> referral.status.displayName
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = when (referral.status) {
                                ReferralStatus.COMPLETED -> VS_Success
                                ReferralStatus.ACCEPTED -> VS_PrimaryContainer
                                else -> VS_Warning
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                val urgencyEmoji = when (referral.urgency) {
                    com.vitalsense.app.core.data.model.ReferralUrgency.EMERGENCY -> "🚨"
                    com.vitalsense.app.core.data.model.ReferralUrgency.URGENT -> "⚡"
                    com.vitalsense.app.core.data.model.ReferralUrgency.ROUTINE -> "📋"
                }
                Text(
                    text = "$urgencyEmoji ${referral.urgency.name}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
