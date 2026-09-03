package com.vitalsense.app.feature.patient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.components.InlineHelpBanner
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@Composable
fun FullManualScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimaryNearBlack
                    )
                }
                Text(
                    text = "SehatSetu User Guide & Manual",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextPrimaryNearBlack
                )
            }
        }

        item {
            InlineHelpBanner(
                title = "Consolidated App Manual",
                message = "Complete guide on using offline health cards, logging symptoms, contacting ASHA helpers, and emergency features."
            )
        }

        val manualItems = listOf(
            "🪪 1. Offline Health Card" to "Your health card stores your age, blood group, allergies, active conditions, and emergency contact locally in Room database. It is accessible even without mobile internet.",
            "📝 2. Logging Health Symptoms" to "Tap any health category on the home screen to report symptoms. Select your discomfort level (Low, Moderate, High, Severe) and requested specialist.",
            "💊 3. Digital Prescriptions" to "View prescription instructions issued by doctors. You can also scan physical paper prescriptions using the OCR scanner.",
            "📅 4. Doctor Appointments" to "Propose consultation time slots with sub-district doctors and check approval status directly from your device.",
            "🚨 5. Emergency SOS" to "Press the red Emergency SOS button in urgent situations to trigger an immediate SMS alert and notification to your assigned ASHA worker.",
            "🧠 6. Mental Wellness" to "Check in your daily mood and follow guided breathing exercises designed for stress relief in rural environments.",
            "🏛️ 7. Government Schemes" to "Browse free government healthcare benefits such as Ayushman Bharat, PMMVY, RKSK, and Nikshay Poshan."
        )

        manualItems.forEach { (title, description) ->
            item {
                VitalSenseCard(elevation = 2.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryNearBlack.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}