package com.vitalsense.app.feature.doctor
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Appointment
import com.vitalsense.app.core.ui.components.VitalSenseCard
@Composable
fun AppointmentConfirmationScreen(appointments: List<Appointment>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.pendingAppointmentsTitle), style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(appointments) { appt ->
                VitalSenseCard {
                    Column {
                        Text("${appt.patientName} on ${appt.dateFormatted}")
                        Text("Status: ${appt.status}")
                        // Add buttons for Accept/Reject in a real impl
                    }
                }
            }
        }
    }
}