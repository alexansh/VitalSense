package com.vitalsense.app.feature.doctor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.data.model.DoctorSpecialty
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

@Composable
fun PendingCasesScreen(
    cases: List<ConditionRecord>,
    onSelectCase: (ConditionRecord) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedSpecialtyFilter by remember { mutableStateOf<DoctorSpecialty?>(null) }

    val filteredCases = remember(cases, selectedSpecialtyFilter) {
        if (selectedSpecialtyFilter == null) cases
        else cases.filter { it.requestedDoctorType == selectedSpecialtyFilter }
    }

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
                    text = "Pending Clinical Cases Queue",
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
                title = "Clinical Case Review",
                message = "Review pending patient symptom submissions grouped and filterable by requested specialty."
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedSpecialtyFilter == null,
                        onClick = { selectedSpecialtyFilter = null },
                        label = { Text("All Specialties") },
                        shape = PillShape
                    )
                }
                items(DoctorSpecialty.values()) { specialty ->
                    FilterChip(
                        selected = selectedSpecialtyFilter == specialty,
                        onClick = { selectedSpecialtyFilter = specialty },
                        label = { Text(specialty.displayName) },
                        shape = PillShape
                    )
                }
            }
        }

        if (filteredCases.isEmpty()) {
            item {
                VitalSenseCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🩺", fontSize = 32.sp)
                        Text(
                            text = "No Pending Cases Found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryNearBlack
                        )
                        Text(
                            text = "All reported patient symptoms for this specialty have been reviewed.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryMuted
                        )
                    }
                }
            }
        } else {
            items(filteredCases) { record ->
                VitalSenseCard(
                    elevation = 2.dp,
                    onClick = { onSelectCase(record) }
                ) {
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
                                Icon(Icons.Default.MedicalServices, contentDescription = null, tint = DarkCharcoal)
                                Text(
                                    text = record.patientName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimaryNearBlack
                                )
                            }
                            SeverityBadge(severity = record.severity)
                        }

                        Text(
                            text = "Category: ${record.category.displayName} · Village: ${record.villageName}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = TextPrimaryNearBlack
                        )

                        if (record.notes.isNotBlank()) {
                            Text(
                                text = "Notes: ${record.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryMuted
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Specialty: ${record.requestedDoctorType.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryMuted
                            )
                            Surface(shape = PillShape, color = LimePrimary) {
                                Text(
                                    text = "Review Case →",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = DarkCharcoal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}