package com.vitalsense.app.feature.patient
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Doctor
import com.vitalsense.app.core.ui.components.VitalSenseCard
@Composable
fun DoctorMapListScreen(doctors: List<Doctor>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.nearestDoctorsListView), style = MaterialTheme.typography.headlineMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(doctors) { doc ->
                VitalSenseCard {
                    Column {
                        Text(doc.name, style = MaterialTheme.typography.titleMedium)
                        Text(doc.specialty.name)
                        Text(stringResource(R.string.distanceMocked))
                    }
                }
            }
        }
    }
}