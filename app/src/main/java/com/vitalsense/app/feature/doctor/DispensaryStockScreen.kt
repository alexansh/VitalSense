package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitalsense.app.R
import com.vitalsense.app.core.data.model.DispensaryItem
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.*

@Composable
fun DispensaryStockScreen(
    stock: List<DispensaryItem>,
    lastUpdated: String = "Today",
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.dispensaryStock),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = VS_OnBackground
                )
                Text(
                    text = "📦 Cached offline inventory • Updated $lastUpdated",
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            contentPadding = PaddingValues(bottom = Spacing.xxl)
        ) {
            items(stock, key = { it.id }) { item ->
                VitalSenseCard(
                    backgroundColor = VS_Surface,
                    border = BorderStroke(1.dp, if (item.isLowStock) VS_Error.copy(alpha = 0.5f) else VS_OutlineVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = item.medicineName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnSurface
                            )
                            Text(
                                text = "Category: ${item.category} • Unit: ${item.unit}",
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                            if (item.isLowStock) {
                                Surface(
                                    color = VS_ErrorContainer,
                                    shape = PillShape
                                ) {
                                    Text(
                                        text = "⚠️ LOW STOCK ALERT (Min: ${item.reorderThreshold} ${item.unit})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VS_Error,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = if (item.isLowStock) VS_ErrorContainer else VS_SuccessContainer,
                            modifier = Modifier.defaultMinSize(minWidth = 60.dp)
                        ) {
                            Text(
                                text = "${item.availableQuantity} units",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (item.isLowStock) VS_Error else VS_Success,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
