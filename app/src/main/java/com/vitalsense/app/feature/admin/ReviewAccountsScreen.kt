package com.vitalsense.app.feature.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.components.*
import com.vitalsense.app.core.ui.theme.*

data class UserAccountReview(
    val id: String,
    val name: String,
    val role: String,
    val uniqueId: String,
    val status: String
)

@Composable
fun ReviewAccountsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var accountList by remember {
        mutableStateOf(
            listOf(
                UserAccountReview("1", "Priya Devi", "ASHA Worker", "ASHA-7701", "VERIFIED ✓"),
                UserAccountReview("2", "Sunita Sharma", "ASHA Worker", "ASHA-8842", "VERIFIED ✓"),
                UserAccountReview("3", "Dr. Rajesh Varma", "General Physician", "DOC-101", "VERIFIED ✓"),
                UserAccountReview("4", "Dr. Ananya Sen", "Psychologist", "DOC-202", "VERIFIED ✓")
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimaryNearBlack
                    )
                }
                Text(
                    text = "Medical & ASHA Credential Review",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextPrimaryNearBlack
                )
            }
        }

        item {
            InlineHelpBanner(
                title = "Personnel Credentialing",
                message = "Approve, flag, or audit medical officer and ASHA helper credentials across the district."
            )
        }

        items(accountList) { account ->
            VitalSenseCard(elevation = 2.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = DarkCharcoal)
                            Text(
                                text = account.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryNearBlack
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = when {
                                account.status.contains("VERIFIED") -> SoftMintSuccess
                                account.status.contains("FLAGGED") -> CoralAlert
                                else -> AmberWarning
                            }
                        ) {
                            Text(
                                text = account.status,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = TextPrimaryNearBlack
                            )
                        }
                    }

                    Text(
                        text = "Role: ${account.role} · System ID: ${account.uniqueId}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimaryNearBlack
                    )

                    HorizontalDivider(color = TextSecondaryMuted.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VitalSenseButton(
                            text = "Approve",
                            onClick = {
                                accountList = accountList.map {
                                    if (it.id == account.id) it.copy(status = "VERIFIED ✓") else it
                                }
                            },
                            modifier = Modifier.weight(1f),
                            style = ButtonStyle.PRIMARY
                        )

                        VitalSenseButton(
                            text = "Flag / Audit",
                            onClick = {
                                accountList = accountList.map {
                                    if (it.id == account.id) it.copy(status = "FLAGGED 🔴") else it
                                }
                            },
                            modifier = Modifier.weight(1f),
                            style = ButtonStyle.DANGER
                        )
                    }
                }
            }
        }
    }
}