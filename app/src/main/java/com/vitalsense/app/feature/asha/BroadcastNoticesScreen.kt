package com.vitalsense.app.feature.asha
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitalsense.app.core.data.model.BroadcastNotice
import com.vitalsense.app.core.data.model.UserRole
import com.vitalsense.app.core.ui.components.VitalSenseButton
import java.util.UUID
@Composable
fun BroadcastNoticesScreen(ashaName: String, onSend: (BroadcastNotice) -> Unit) {
    var title by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.sendNoticeToCaseload), style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text(stringResource(R.string.broadcastTitle)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = msg, onValueChange = { msg = it }, label = { Text(stringResource(R.string.broadcastMessage)) }, modifier = Modifier.fillMaxWidth())
        VitalSenseButton("Send Broadcast", onClick = {
            onSend(BroadcastNotice(id = UUID.randomUUID().toString(), senderRole = UserRole.ASHA, senderName = ashaName, targetRole = "PATIENT", targetVillage = "All", title = title, message = msg, timestamp = System.currentTimeMillis(), isUrgent = false))
        })
    }
}