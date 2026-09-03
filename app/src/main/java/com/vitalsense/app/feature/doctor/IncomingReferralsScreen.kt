package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalsense.app.core.data.model.Referral
import com.vitalsense.app.core.data.model.ReferralStatus
import com.vitalsense.app.core.data.model.ReferralType
import com.vitalsense.app.core.ui.components.CategoryChip
import com.vitalsense.app.core.ui.components.VitalSenseButton
import com.vitalsense.app.core.ui.components.VitalSenseCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingReferralsScreen(
    viewModel: DoctorViewModel,
    onBackClick: () -> Unit,
    onAcceptReferral: (Referral) -> Unit
) {
    val pendingReferrals by viewModel.pendingReferrals.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Incoming Referrals", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (pendingReferrals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No pending referrals for your department.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendingReferrals) { referral ->
                    ReferralCard(
                        referral = referral,
                        onAccept = {
                            viewModel.acceptReferral(referral.id)
                            onAcceptReferral(referral)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReferralCard(
    referral: Referral,
    onAccept: () -> Unit
) {
    VitalSenseCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${referral.referralType.emoji} ${referral.patientName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(shape = com.vitalsense.app.core.ui.theme.PillShape, color = androidx.compose.ui.graphics.Color(referral.urgency.colorHex)) { Text(text = referral.urgency.displayName, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall) }
            }

            Text(
                text = "From: ${referral.fromDoctorName} (${referral.fromDepartmentName})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (referral.reason.isNotBlank()) {
                Text(
                    text = "Reason: ${referral.reason}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            Text(
                text = "Received: ${dateFormat.format(Date(referral.createdAt))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            VitalSenseButton(
                text = if (referral.referralType == ReferralType.SERVICE) "Accept & Process Report" else "Accept Transfer",
                onClick = onAccept,
                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

