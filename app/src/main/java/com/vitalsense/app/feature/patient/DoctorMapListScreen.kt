package com.vitalsense.app.feature.patient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.CameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Doctor
import com.vitalsense.app.core.ui.components.InlineHelpBanner
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

data class DoctorDistanceItem(
    val doctor: Doctor,
    val mockDistanceKm: Double,
    val primaryFacility: String,
    val location: LatLng
)

@Composable
fun DoctorMapListScreen(
    doctors: List<Doctor>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    // Mock starting location (Rampur center roughly)
    val userLocation = LatLng(28.8157, 79.0252)

    val sortedList = remember(doctors) {
        doctors.mapIndexed { index, doc ->
            DoctorDistanceItem(
                doctor = doc,
                mockDistanceKm = 2.4 + (index * 3.1),
                primaryFacility = if (index % 2 == 0) "Rampur Sub-District Hospital" else "Kalyanpur Community Health Center",
                location = LatLng(userLocation.latitude + (index * 0.01), userLocation.longitude + (index * 0.015))
            )
        }.sortedBy { it.mockDistanceKm }
    }

    val cameraPositionState = remember {
        CameraPositionState(position = CameraPosition.fromLatLngZoom(userLocation, 12f))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                text = "Nearest Doctors",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TextPrimaryNearBlack
            )
        }

        // Map View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // User marker
                Marker(
                    state = MarkerState(position = userLocation),
                    title = "Your Location",
                    snippet = "Approximate Location"
                )
                // Doctor markers
                sortedList.forEach { item ->
                    Marker(
                        state = MarkerState(position = item.location),
                        title = item.doctor.name,
                        snippet = item.primaryFacility
                    )
                }
            }
        }

        // List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                InlineHelpBanner(
                    title = "Medical Directory",
                    message = "List of verified medical officers available in your block/district, ordered by distance."
                )
            }

            items(sortedList) { item ->
                VitalSenseCard(elevation = 2.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = DarkCharcoal)
                                Text(
                                    text = item.doctor.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimaryNearBlack
                                )
                            }
                            Surface(shape = PillShape, color = SoftMintSuccess) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextPrimaryNearBlack)
                                    Text(
                                        text = "${String.format("%.1f", item.mockDistanceKm)} km",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimaryNearBlack
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Specialty: ${item.doctor.specialty.displayName}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimaryNearBlack
                        )

                        Text(
                            text = "Facility: ${item.primaryFacility}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )

                        Text(
                            text = "Contact: ${item.doctor.phone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        }
    }
}