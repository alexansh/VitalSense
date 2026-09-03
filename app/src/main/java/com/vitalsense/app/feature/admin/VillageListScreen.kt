package com.vitalsense.app.feature.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HolidayVillage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*
import java.util.UUID

@Composable
fun VillageListScreen(
    villages: List<Village>,
    onAddVillage: (Village) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var showAddDialog by remember { mutableStateOf(false) }

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
                    text = "Sub-District Village Directory",
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
                title = "Village Region Registry",
                message = "Manage registered village clusters, assigned ASHA helpers, and population stats."
            )
        }

        item {
            VitalSenseButton(
                text = "+ Add New Village Cluster",
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.DARK
            )
        }

        items(villages) { village ->
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
                            Icon(Icons.Default.HolidayVillage, contentDescription = null, tint = DarkCharcoal)
                            Text(
                                text = village.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = if (false) CoralAlert.copy(alpha = 0.2f) else SoftMintSuccess
                        ) {
                            Text(
                                text = if (false) "ALERT OUTBREAK 🔴" else "STABLE 🟢",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (false) CoralAlert else TextPrimaryNearBlack
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "District: ${village.district} · Sub-District: ${village.subDistrict}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryMuted
                    )

                    Text(
                        text = "Total Population: ${village.population} · Active ASHA Workers: ${village.assignedAshaIds.size}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = TextPrimaryNearBlack
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var population by remember { mutableStateOf("1200") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Register Village Cluster", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Village Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = population,
                        onValueChange = { population = it },
                        label = { Text("Estimated Population") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                VitalSenseButton(
                    text = "Save Village",
                    onClick = {
                        if (name.isNotBlank()) {
                            onAddVillage(
                                Village(
                                    id = UUID.randomUUID().toString(),
                                    name = name,
                                    
                                    district = "Rampur",
                                    population = population.toIntOrNull() ?: 1000,
                                    
                                    assignedAshaIds = listOf("asha-01")
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    style = ButtonStyle.PRIMARY
                )
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondaryMuted)
                }
            }
        )
    }
}
