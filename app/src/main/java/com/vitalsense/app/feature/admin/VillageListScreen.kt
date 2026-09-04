package com.vitalsense.app.feature.admin
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.Village
import com.vitalsense.app.core.ui.components.VitalSenseCard
@Composable
fun VillageListScreen(villages: List<Village>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.villagesCategory), style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(villages) { v ->
                VitalSenseCard {
                    Text(v.name)
                    Text("Population: ${v.population}")
                }
            }
        }
    }
}