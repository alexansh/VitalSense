package com.vitalsense.app.feature.asha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.ImmunizationRecord
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImmunizationTrackerScreen(
    records: List<ImmunizationRecord>,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Immunization Tracker") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VS_SurfaceVariant,
                    titleContentColor = VS_OnBackground,
                    navigationIconContentColor = VS_OnBackground
                )
            )
        },
        containerColor = VS_Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            contentPadding = PaddingValues(top = Spacing.sm, bottom = Spacing.xxl)
        ) {
            item {
                Text(
                    text = "Maternal & Child Records",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            }

            if (records.isEmpty()) {
                item {
                    Text(
                        text = "No records found.",
                        color = VS_OnSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(records) { record ->
                    VitalSenseCard(
                        backgroundColor = VS_Surface,
                        border = BorderStroke(1.dp, VS_Outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = record.childName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Text(
                                    text = "DOB: ${record.dobFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }
                            Text(
                                text = "Mother: ${record.motherName} | Village: ${record.villageName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )

                            HorizontalDivider(color = VS_Outline, thickness = 1.dp)

                            Text(
                                text = "Vaccination Schedule",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_PrimaryContainer
                            )

                            record.vaccines.forEach { vaccine ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = vaccine.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = VS_OnBackground
                                        )
                                        Text(
                                            text = "Due: ${vaccine.dueDateFormatted}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = VS_OnSurfaceVariant
                                        )
                                    }
                                    
                                    val statusColor = when (vaccine.status) {
                                        "Completed" -> VS_Success
                                        "Overdue" -> VS_Error
                                        else -> VS_OnSurfaceVariant
                                    }
                                    Text(
                                        text = vaccine.status,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = statusColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
