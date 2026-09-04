package com.vitalsense.app.feature.asha
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.AshaMedicine
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineRestockScreen(
    medicines: List<AshaMedicine>,
    onBackClick: () -> Unit,
    onRequestRestock: (AshaMedicine) -> Unit = {}
) {
    var restockSuccessMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.medicineRestockTracker)) },
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
                    text = stringResource(R.string.ashaFieldKitStock),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
            }

            if (restockSuccessMsg != null) {
                item {
                    Surface(
                        shape = PillShape,
                        color = VS_SuccessContainer,
                        border = BorderStroke(1.dp, VS_Success),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = restockSuccessMsg ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = VS_Success
                            )
                            IconButton(onClick = { restockSuccessMsg = null }, modifier = Modifier.size(24.dp)) {
                                Text("✕", color = VS_Success, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (medicines.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.noMedicinesInKit),
                        color = VS_OnSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(medicines) { medicine ->
                    val isOutOfStock = medicine.availableQuantity <= 0
                    val isLowStock = medicine.availableQuantity <= medicine.minStockQuantity && !isOutOfStock

                    val statusColor = when {
                        isOutOfStock -> VS_Error
                        isLowStock -> VS_Warning
                        else -> VS_Success
                    }
                    val statusText = when {
                        isOutOfStock -> "Out of Stock"
                        isLowStock -> "Low Stock"
                        else -> "In Stock"
                    }

                    VitalSenseCard(
                        backgroundColor = if (isOutOfStock) VS_ErrorContainer else VS_Surface,
                        border = BorderStroke(1.dp, if (isOutOfStock) VS_Error.copy(alpha = 0.4f) else VS_Outline)
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
                                    text = medicine.medicineName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = VS_OnBackground
                                )
                                Surface(shape = CircleShape, color = statusColor.copy(alpha = 0.2f)) {
                                    Text(
                                        text = statusText,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = statusColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            
                            Text(
                                text = "Quantity: ${medicine.availableQuantity} ${medicine.unit} (Min: ${medicine.minStockQuantity})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VS_OnBackground
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Expiry: ${medicine.expiryDateFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                                Text(
                                    text = "Last Restock: ${medicine.lastRestockDateFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VS_OnSurfaceVariant
                                )
                            }

                            if (isLowStock || isOutOfStock) {
                                HorizontalDivider(color = VS_Outline, modifier = Modifier.padding(vertical = 4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.kitRefillNeededPhc),
                                        style = MaterialTheme.typography.bodySmall.copy(color = VS_Error, fontSize = 11.sp)
                                    )
                                    Button(
                                        onClick = {
                                            val updated = medicine.copy(
                                                availableQuantity = medicine.availableQuantity + 50,
                                                lastRestockDateFormatted = "Today"
                                            )
                                            onRequestRestock(updated)
                                            restockSuccessMsg = "✓ Indent submitted for 50 ${medicine.unit} of ${medicine.medicineName}!"
                                        },
                                        shape = PillShape,
                                        colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text(stringResource(R.string.requestRefill50), style = MaterialTheme.typography.labelSmall, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
