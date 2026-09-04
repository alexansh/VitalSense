package com.vitalsense.app.feature.doctor
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.ConditionRecord
import com.vitalsense.app.core.ui.components.VitalSenseCard
@Composable
fun PendingCasesScreen(conditions: List<ConditionRecord>, onSelect: (ConditionRecord) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.pendingCasesTitle), style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(conditions) { cond ->
                VitalSenseCard(onClick = { onSelect(cond) }) {
                    Column {
                        Text("${cond.patientName} (${cond.villageName})")
                        Text("Category: ${cond.category.displayName} | Risk: ${cond.severity.name}")
                    }
                }
            }
        }
    }
}