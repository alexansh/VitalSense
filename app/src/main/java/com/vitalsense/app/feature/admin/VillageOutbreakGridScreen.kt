package com.vitalsense.app.feature.admin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.theme.VS_Error
import com.vitalsense.app.core.ui.theme.VS_Primary
@Composable
fun VillageOutbreakGridScreen(villages: List<Village>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Village Outbreak Heatmap", style = MaterialTheme.typography.headlineMedium)
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(villages) { v ->
                val bg = if (v.highRiskCount > 0) VS_Error else VS_Primary
                VitalSenseCard(backgroundColor = bg) {
                    Column {
                        Text(v.name, style = MaterialTheme.typography.titleMedium)
                        Text("Active Cases: ${v.activeCases}")
                        Text("High Risk: ${v.highRiskCount}")
                    }
                }
            }
        }
    }
}