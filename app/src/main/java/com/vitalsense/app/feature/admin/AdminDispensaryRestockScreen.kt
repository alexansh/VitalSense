package com.vitalsense.app.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.DispensaryItem
import com.vitalsense.app.core.ui.components.ButtonStyle
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.DismissedNoticeHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDispensaryRestockScreen(
    dispensaryStock: List<DispensaryItem>,
    onBackClick: () -> Unit,
    onSaveItem: (DispensaryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dispensary Restock", color = VS_OnBackground) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = VS_OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VS_Background
                )
            )
        },
        containerColor = VS_Background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        val context = androidx.compose.ui.platform.LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.md)
        ) {
            Text(
                text = "Manage Inventory",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground,
                modifier = Modifier.padding(bottom = Spacing.sm, top = Spacing.sm)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                contentPadding = PaddingValues(bottom = Spacing.xxl)
            ) {
                // Low stock items first
                val sortedStock = dispensaryStock.sortedBy { !it.isLowStock }
                items(sortedStock, key = { it.id }) { item ->
                    DispensaryRestockCard(
                        item = item,
                        onUpdateStock = { newQuantity ->
                            val updatedItem = item.copy(
                                availableQuantity = newQuantity,
                                lastRestockDateFormatted = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
                            )
                            DismissedNoticeHelper.clearRemindedMedicine(context, item.id)
                            onSaveItem(updatedItem)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DispensaryRestockCard(
    item: DispensaryItem,
    onUpdateStock: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    VitalSenseCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.medicineName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = VS_OnBackground
                    )
                    Text(
                        text = "${item.category} • Minimum: ${item.reorderThreshold} ${item.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VS_OnSurfaceVariant
                    )
                    item.lastRestockDateFormatted?.let { date ->
                        Text(
                            text = "Last Restocked: $date",
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${item.availableQuantity} ${item.unit}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (item.isLowStock) VS_Error else VS_Primary
                        )
                    )
                    if (item.isLowStock) {
                        Surface(
                            shape = PillShape,
                            color = VS_ErrorContainer,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "LOW STOCK",
                                style = MaterialTheme.typography.labelSmall.copy(color = VS_Error, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = Spacing.sm, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            VitalSenseButton(
                text = "Restock Item",
                onClick = { showDialog = true },
                style = ButtonStyle.OUTLINED,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showDialog) {
        var quantityText by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Restock ${item.medicineName}") },
            text = {
                Column {
                    Text("Current stock: ${item.availableQuantity} ${item.unit}")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it.filter { char -> char.isDigit() } },
                        label = { Text("Add quantity") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val addedQty = quantityText.toIntOrNull() ?: 0
                        if (addedQty > 0) {
                            onUpdateStock(item.availableQuantity + addedQty)
                        }
                        showDialog = false
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

