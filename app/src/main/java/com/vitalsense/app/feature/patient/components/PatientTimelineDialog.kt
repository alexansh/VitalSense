package com.vitalsense.app.feature.patient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.ui.components.VSTimeline
import com.vitalsense.app.core.ui.components.VSTimelineStep

data class TimelineEvent(
    val date: String,
    val title: String,
    val description: String,
    val eventType: EventType,
    val doctorName: String? = null
)

enum class EventType {
    VISIT, REFERRAL, MEDICATION, LAB_TEST
}

fun getMockTimelineEvents(): List<TimelineEvent> {
    return listOf(
        TimelineEvent("Today, 10:30 AM", "ASHA Home Visit", "Blood pressure monitored: 140/90. Patient reported mild dizziness.", EventType.VISIT),
        TimelineEvent("Aug 12, 2026", "Specialist Consultation", "Cardiologist prescribed Ramipril 5mg.", EventType.REFERRAL, "Dr. Sharma (Cardio)"),
        TimelineEvent("Aug 10, 2026", "Lab Test: Lipid Profile", "Cholesterol elevated (240 mg/dL).", EventType.LAB_TEST),
        TimelineEvent("Aug 05, 2026", "PHC Checkup", "Initial hypertension diagnosis.", EventType.VISIT, "Dr. Gupta")
    )
}

@Composable
fun PatientTimelineDialog(
    patient: Patient,
    onDismiss: () -> Unit
) {
    val events = getMockTimelineEvents()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = VS_Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Care Journey",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "Longitudinal Health Record for ${patient.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(VS_SurfaceVariant, CircleShape)
                    ) {
                        Text("?", color = VS_OnBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.md))

                val steps = events.map { event ->
                    VSTimelineStep(
                        title = event.title,
                        timestamp = "${event.date}\n${event.description}" + (if (event.doctorName != null) "\nAttending: ${event.doctorName}" else ""),
                        completed = true
                    )
                }

                VSTimeline(steps = steps, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
