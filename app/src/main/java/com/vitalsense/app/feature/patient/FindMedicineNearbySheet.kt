package com.vitalsense.app.feature.patient
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.medicine.*
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.PrescribedMedicine
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import java.util.Locale

@Composable
fun FindMedicineNearbySheet(
    patient: Patient,
    medicine: PrescribedMedicine,
    onDismiss: () -> Unit,
    language: AppLanguage = AppLanguage.ENGLISH,
    medicineRepoOverride: MedicineAvailabilityRepository? = null
) {
    val context = LocalContext.current
    val medicineRepo = remember {
        medicineRepoOverride ?: run {
            val db = VitalSenseDatabase.getDatabase(context)
            MedicineAvailabilityRepositoryImpl(db, context)
        }
    }

    val patientLat = when (patient.villageId) {
        "v_sundarpura" -> 26.8467
        "v_kalyanpur" -> 26.8821
        "v_bhimnagar" -> 26.8150
        else -> 26.8467
    }
    val patientLng = when (patient.villageId) {
        "v_sundarpura" -> 80.9462
        "v_kalyanpur" -> 80.9812
        "v_bhimnagar" -> 80.9120
        else -> 80.9462
    }

    // Active queried medicine name/id (can switch to doctor's alternative)
    var currentMedicineName by remember { mutableStateOf(medicine.name) }
    var currentMedicineId by remember {
        mutableStateOf(medicine.medicineId ?: medicineRepo.findMedicineByName(medicine.name)?.id ?: medicine.name)
    }

    var stores by remember { mutableStateOf<List<StoreAvailabilityResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedStore by remember { mutableStateOf<StoreAvailabilityResult?>(null) }
    var candidateAlternatives by remember { mutableStateOf<List<Medicine>>(emptyList()) }

    // Query stores whenever currentMedicineId changes
    LaunchedEffect(currentMedicineId) {
        isLoading = true
        val results = medicineRepo.getNearbyStoresWithAvailability(currentMedicineId, patientLat, patientLng, 5000)
        stores = results
        selectedStore = results.firstOrNull { it.inStock } ?: results.firstOrNull()
        candidateAlternatives = medicineRepo.suggestAlternatives(currentMedicineId, results)
        isLoading = false
    }

    val inStockCount = stores.count { it.inStock }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(patientLat, patientLng), 13.0f)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f),
            shape = DialogShape,
            color = VS_Surface,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, VS_Outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.md)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.findMedicineNearby),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = "$currentMedicineName · Near ${patient.villageName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                    }

                    // Audio Guidance Button (Low-literacy)
                    IconButton(
                        onClick = {
                            val topStore = stores.firstOrNull { it.inStock }?.storeName ?: "nearby store"
                            val audioMessage = if (inStockCount > 0) {
                                when (language) {
                                    AppLanguage.HINDI -> "$currentMedicineName आस-पास के $inStockCount मेडिकल स्टोर में उपलब्ध होने की संभावना है। सबसे पास $topStore है।"
                                    AppLanguage.TAMIL -> "$currentMedicineName அருகில் உள்ள $inStockCount மருந்தகங்களில் கிடைக்கிறது. மிக அருகில் $topStore உள்ளது."
                                    AppLanguage.MARATHI -> "$currentMedicineName जवळील $inStockCount औषध दुकानांमध्ये उपलब्ध आहे. सर्वात जवळ $topStore आहे."
                                    AppLanguage.ENGLISH -> "$currentMedicineName is likely available at $inStockCount nearby stores. Closest is $topStore."
                                }
                            } else {
                                when (language) {
                                    AppLanguage.HINDI -> "$currentMedicineName अभी नज़दीकी मेडिकल स्टोर में उपलब्ध नहीं है। वैकल्पिक दवा देखें।"
                                    AppLanguage.TAMIL -> "$currentMedicineName தற்போது அருகில் கிடைக்கவில்லை. மாற்று மருந்துகளைப் பார்க்கவும்."
                                    AppLanguage.MARATHI -> "$currentMedicineName सध्या जवळ उपलब्ध नाही. पर्यायी औषधे तपासा."
                                    AppLanguage.ENGLISH -> "$currentMedicineName is currently out of stock nearby. Doctor's suggested alternative is available."
                                }
                            }
                            AudioGuidanceHelper.speak(context, audioMessage, language)
                        }
                    ) {
                        Text(text = "🔊", fontSize = 22.sp)
                    }

                    IconButton(onClick = onDismiss) {
                        Text(text = "✕", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = VS_OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = Spacing.xs))

                // Availability Summary Banner
                if (!isLoading) {
                    if (inStockCount > 0) {
                        Surface(
                            shape = PillShape,
                            color = VS_SuccessContainer,
                            modifier = Modifier.padding(bottom = Spacing.xs)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                            ) {
                                Text(text = "🟢", fontSize = 12.sp)
                                Text(
                                    text = "Likely available at $inStockCount nearby pharmacies",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnSuccessContainer
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = PillShape,
                            color = VS_ErrorContainer,
                            modifier = Modifier.padding(bottom = Spacing.xs)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                            ) {
                                Text(text = "🔴", fontSize = 12.sp)
                                Text(
                                    text = stringResource(R.string.notFoundNearbyAlternative),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnErrorContainer
                                )
                            }
                        }
                    }
                }

                // Interactive Map Component
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(InputShape)
                        .background(VS_SurfaceVariant)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            myLocationButtonEnabled = false,
                            compassEnabled = true
                        )
                    ) {
                        // Patient location pin
                        Marker(
                            state = MarkerState(position = LatLng(patientLat, patientLng)),
                            title = "Patient Location",
                            snippet = patient.villageName,
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )

                        // Pharmacy pins with green (in-stock) / red (out-of-stock)
                        stores.forEach { store ->
                            Marker(
                                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                                title = store.storeName,
                                snippet = if (store.inStock) "Likely Available · ${(store.distanceMeters / 1000).formatDistance()}" else "Out of Stock · ${(store.distanceMeters / 1000).formatDistance()}",
                                icon = BitmapDescriptorFactory.defaultMarker(
                                    if (store.inStock) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED
                                ),
                                onClick = {
                                    selectedStore = store
                                    false
                                }
                            )
                        }
                    }

                    // Map legend in top-right
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = PillShape,
                        color = VS_Surface.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.likelyInStock), style = MaterialTheme.typography.labelSmall, color = VS_OnSuccessContainer)
                            Text(text = "•", color = VS_OnSurfaceVariant)
                            Text(text = stringResource(R.string.outOfStockTag), style = MaterialTheme.typography.labelSmall, color = VS_OnErrorContainer)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Selected Store Spotlight Card (with Call to Confirm)
                selectedStore?.let { store ->
                    VitalSenseCard(
                        backgroundColor = if (store.inStock) VS_SurfaceVariant else VS_ErrorContainer,
                        border = BorderStroke(1.dp, if (store.inStock) VS_Outline else VS_Error)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = store.storeName,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "${(store.distanceMeters / 1000).formatDistance()} away · ${store.address}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                                Text(
                                    text = if (store.inStock) "✓ Likely available (simulated)" else "✕ Out of stock nearby",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (store.inStock) VS_OnSuccessContainer else VS_OnErrorContainer
                                )
                            }

                            if (!store.phoneNumber.isNullOrBlank()) {
                                Button(
                                    onClick = {
                                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${store.phoneNumber}")
                                        }
                                        context.startActivity(dialIntent)
                                    },
                                    shape = PillShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (store.inStock) VS_Primary else VS_Error
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.callPharmacyBtn),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                }
                            }
                        }
                    }
                }

                // Empty State / Doctor's Alternative Shortcut if zero stores have it
                if (inStockCount == 0 && candidateAlternatives.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    VitalSenseCard(
                        backgroundColor = VS_Primary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, VS_Primary)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            Text(
                                text = stringResource(R.string.docSuggestedAlternative),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )
                            val firstAlt = candidateAlternatives.first()
                            Text(
                                text = "Your doctor or health system suggested '${firstAlt.name}' (${firstAlt.genericName}) as a clinically interchangeable substitute for ${firstAlt.commonUseDescription}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnBackground
                            )
                            Button(
                                onClick = {
                                    currentMedicineName = firstAlt.name
                                    currentMedicineId = firstAlt.id
                                },
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = "Check '${firstAlt.name}' Nearby →",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Sorted List of Nearby Pharmacies
                Text(
                    text = "Nearby Medical Stores (${stores.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    items(stores) { store ->
                        val isSelected = selectedStore?.placeId == store.placeId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStore = store },
                            shape = InputShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) VS_SurfaceVariant else VS_Surface
                            ),
                            border = BorderStroke(1.dp, if (isSelected) VS_Primary else VS_Outline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Spacing.sm),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = store.storeName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = VS_OnBackground
                                    )
                                    Text(
                                        text = "${(store.distanceMeters / 1000).formatDistance()} · ${store.address}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VS_OnSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = PillShape,
                                    color = if (store.inStock) VS_SuccessContainer else VS_ErrorContainer
                                ) {
                                    Text(
                                        text = if (store.inStock) "Likely Available" else "Out of Stock",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (store.inStock) VS_OnSuccessContainer else VS_OnErrorContainer,
                                        modifier = Modifier.padding(horizontal = Spacing.xs, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Mandatory disclaimer notice
                Text(
                    text = stringResource(R.string.pharmacyStockNotice),
                    style = MaterialTheme.typography.labelSmall,
                    color = VS_OnSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

private fun Double.formatDistance(): String {
    return String.format(Locale.US, "%.1f km", this)
}
