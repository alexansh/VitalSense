package com.vitalsense.app.feature.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import com.vitalsense.app.core.ui.components.CategoryChip
import com.vitalsense.app.core.ui.components.VitalSenseCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralHistoryScreen(
    viewModel: DoctorViewModel,
    onBackClick: () -> Unit,
    onViewReport: (Referral) -> Unit
) {
    val sentReferrals by viewModel.sentReferrals.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sent Referrals", fontWeight = FontWeight.Bold) },
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
        if (sentReferrals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No referrals sent yet.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sentReferrals) { referral ->
                    SentReferralCard(
                        referral = referral,
                        onViewReport = { onViewReport(referral) }
                    )
                }
            }
        }
    }
}

@Composable
fun SentReferralCard(
    referral: Referral,
    onViewReport: () -> Unit
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
                    text = "${referral.referralType.emoji} To ${referral.toDepartmentName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(shape = com.vitalsense.app.core.ui.theme.PillShape, color = androidx.compose.ui.graphics.Color(referral.status.colorHex)) { Text(text = referral.status.displayName, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall) }
            }

            Text(
                text = "Patient: ${referral.patientName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Reason: ${referral.reason}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            Text(
                text = "Sent: ${dateFormat.format(Date(referral.createdAt))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (referral.status == ReferralStatus.REPORT_SUBMITTED || referral.status == ReferralStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onViewReport,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Service Report")
                }
            }
        }
    }
}

