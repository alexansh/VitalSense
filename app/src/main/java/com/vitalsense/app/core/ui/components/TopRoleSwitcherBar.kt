package com.vitalsense.app.core.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.data.model.UserRole
import com.vitalsense.app.core.network.ConnectivityState
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.R
import androidx.compose.ui.res.stringResource

@Composable
fun TopRoleSwitcherBar(
    currentRole: UserRole,
    activeUserName: String = "",
    activeProxyPatient: Patient? = null,
    onExitProxy: () -> Unit = {},
    isOffline: Boolean = false,
    connectivityState: ConnectivityState = ConnectivityState.ONLINE,
    isSyncing: Boolean = false,
    pendingOutboxCount: Int = 0,
    onManualSync: () -> Unit = {},
    onToggleOffline: () -> Unit = {},
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onToggleLanguage: () -> Unit = {},
    onSelectLanguage: (AppLanguage) -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        ChangeLanguageDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { lang ->
                onSelectLanguage(lang)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VS_Background)
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
    ) {
        // Floating Top Island Header (NagarSeva Design System)
        Surface(
            shape = PillShape,
            color = VS_Surface,
            border = BorderStroke(1.dp, VS_Outline),
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.sm, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Logo & Role Scoped User Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(VS_Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (currentRole) {
                                UserRole.PATIENT -> "🧑"
                                UserRole.ASHA -> "🩺"
                                UserRole.DOCTOR -> "👨‍⚕️"
                                UserRole.ADMIN -> "🛡️"
                            },
                            fontSize = 18.sp
                        )
                    }
                    Column {
                        Text(
                            text = if (activeUserName.isNotBlank()) activeUserName else stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground,
                            maxLines = 1
                        )
                        Text(
                            text = when (currentRole) {
                                UserRole.PATIENT -> stringResource(R.string.patientPortal)
                                UserRole.ASHA -> stringResource(R.string.ashaPortal)
                                UserRole.DOCTOR -> stringResource(R.string.doctorPortal)
                                UserRole.ADMIN -> stringResource(R.string.adminPortal)
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = VS_PrimaryContainer
                        )
                    }
                }

            // Right Actions: Sync Status, Connectivity Pill, Language & Logout
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
            ) {
                // Syncing Active Spinner / Pending Changes Badge
                if (isSyncing) {
                    Surface(
                        shape = PillShape,
                        color = VS_PrimaryContainer,
                        border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.4f)),
                        modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                strokeWidth = 2.dp,
                                color = VS_Primary
                            )
                            Text(
                                text = stringResource(R.string.syncing),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                color = VS_Primary
                            )
                        }
                    }
                } else if (pendingOutboxCount > 0) {
                    Surface(
                        onClick = onManualSync,
                        shape = PillShape,
                        color = Color(0xFFFFF8E1), // Warm amber background
                        border = BorderStroke(1.dp, Color(0xFFFFB300)),
                        modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🔄", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
                            Text(
                                text = "$pendingOutboxCount",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }

                // Global Language Switcher Pill
                Surface(
                    onClick = { showLanguageDialog = true },
                    shape = PillShape,
                    color = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(text = "🌐", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = currentLanguage.nativeName,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                    }
                }

                // Connectivity Mode Pill (Displays Online, Slow, or Offline)
                val effectiveOffline = isOffline || connectivityState == ConnectivityState.OFFLINE
                val isSlow = !effectiveOffline && connectivityState == ConnectivityState.SLOW_NETWORK

                val pillBgColor = when {
                    effectiveOffline -> VS_SurfaceVariant
                    isSlow -> Color(0xFFFFF3CD)
                    else -> VS_SuccessContainer
                }
                val pillBorderColor = when {
                    effectiveOffline -> VS_Outline
                    isSlow -> Color(0xFFFFC107).copy(alpha = 0.6f)
                    else -> VS_Success.copy(alpha = 0.4f)
                }
                val dotColor = when {
                    effectiveOffline -> VS_OnSurfaceVariant
                    isSlow -> Color(0xFFFF9800)
                    else -> VS_Success
                }
                val textColor = when {
                    effectiveOffline -> VS_OnSurfaceVariant
                    isSlow -> Color(0xFF856404)
                    else -> VS_OnSuccessContainer
                }
                val statusLabel = when {
                    effectiveOffline -> stringResource(R.string.offline)
                    isSlow -> stringResource(R.string.slowNetwork)
                    else -> stringResource(R.string.online)
                }

                Surface(
                    onClick = onToggleOffline,
                    shape = PillShape,
                    color = pillBgColor,
                    border = BorderStroke(1.dp, pillBorderColor),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = textColor
                        )
                    }
                }

                // Logout / Exit Button
                Surface(
                    onClick = onLogout,
                    shape = PillShape,
                    color = VS_Surface,
                    border = BorderStroke(1.dp, VS_Outline),
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(text = "🚪", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = stringResource(R.string.exit),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                    }
                }
            }
        }
    }

        // ASHA Proxy Indicator Banner
        AnimatedVisibility(
            visible = activeProxyPatient != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            if (activeProxyPatient != null) {
                Surface(
                    color = VS_PrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md, vertical = Spacing.xxs),
                    shape = CardShape,
                    border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "🤝", style = MaterialTheme.typography.titleMedium)
                            Column {
                                Text(
                                    text = stringResource(R.string.actingAsProxy),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = VS_PrimaryContainer
                                )
                                Text(
                                    text = "${activeProxyPatient.name} (${activeProxyPatient.villageName})",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = VS_OnBackground
                                )
                            }
                        }
                        Button(
                            onClick = onExitProxy,
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VS_Primary,
                                contentColor = VS_OnBackground
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(text = stringResource(R.string.exitProxy), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = VS_Outline
        )
    }
}
