package com.vitalsense.app.core.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AppUpdateChecker
import com.vitalsense.app.core.util.AppUpdateInfo

@Composable
fun AppUpdateBanner(
    updateInfo: AppUpdateInfo?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AnimatedVisibility(
        visible = updateInfo != null && updateInfo.isUpdateAvailable,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        if (updateInfo != null) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs),
                shape = CardShape,
                color = VS_SurfaceVariant,
                border = BorderStroke(1.dp, VS_Primary.copy(alpha = 0.6f)),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VS_PrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🚀", fontSize = 16.sp)
                            }
                        }

                        Column {
                            Text(
                                text = "New Version Available (${updateInfo.latestVersionName})",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = VS_OnBackground
                            )
                            Text(
                                text = "Interactive Google Maps & enhancements",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Button(
                            onClick = {
                                AppUpdateChecker.openDownloadLink(context, updateInfo.downloadUrl)
                            },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = "Update",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VS_OnSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }
}
