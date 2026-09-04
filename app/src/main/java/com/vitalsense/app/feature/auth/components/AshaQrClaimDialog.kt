package com.vitalsense.app.feature.auth.components
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.Patient
import com.vitalsense.app.core.ui.components.VitalSenseCard
import com.vitalsense.app.core.ui.components.VitalSenseDialog
import com.vitalsense.app.core.ui.theme.*
import com.vitalsense.app.core.util.AudioGuidanceHelper
import kotlinx.coroutines.delay

/**
 * ASHA-Assisted QR Claim Onboarding Flow
 * as specified in VitalSense_UX_Architecture.md §1.4.
 *
 * Allows rural patients without typing literacy to claim their health profile
 * by scanning their ASHA-issued physical health card QR matrix.
 */
@Composable
fun AshaQrClaimDialog(
    language: AppLanguage = AppLanguage.HINDI,
    onDismiss: () -> Unit,
    onPatientClaimed: (Patient) -> Unit
) {
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(true) }
    var claimedPatient by remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(isScanning) {
        if (isScanning) {
            AudioGuidanceHelper.speak(
                context = context,
                text = if (language == AppLanguage.HINDI) "आशा स्वास्थ्य कार्ड का क्यूआर कोड कैमरे के सामने लाएं..." else "Align the ASHA Health Card QR in front of the camera...",
                language = language
            )
            delay(2400)
            AudioGuidanceHelper.provideHapticFeedback(context, isSuccess = true)
            // Claim sample patient Ramesh Kumar
            claimedPatient = SeedDataProvider.initialPatients.first()
            isScanning = false
        }
    }

    VitalSenseDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.scanAshaCardQr),
        icon = { Text("🪪", fontSize = 22.sp) },
        confirmButton = {
            if (!isScanning && claimedPatient != null) {
                Button(
                    onClick = {
                        onPatientClaimed(claimedPatient!!)
                        onDismiss()
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VS_Success),
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "✓ प्रोफ़ाइल से जुड़ें (Claim Profile)" else "✓ Claim & Enter Profile",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = VS_Background
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = PillShape) {
                Text(
                    text = if (language == AppLanguage.HINDI) "रद्द करें" else "Cancel",
                    color = VS_OnSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            if (isScanning) {
                // QR Scanning Camera Viewport Simulation
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CardShape)
                        .background(VS_SurfaceVariant)
                        .padding(Spacing.sm),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("📷", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(Spacing.xxs))
                        Text(
                            text = stringResource(R.string.scanningAshaQr),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_PrimaryContainer
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.scanPhysicalCardZeroPwdDesc),
                    style = MaterialTheme.typography.bodySmall,
                    color = VS_OnSurfaceVariant
                )
            } else if (claimedPatient != null) {
                // Verified Patient Identity
                val patient = claimedPatient!!
                Surface(
                    shape = PillShape,
                    color = VS_SuccessContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("✓ ", color = VS_Success, fontWeight = FontWeight.Bold)
                        Text(
                            text = stringResource(R.string.patientIdentityVerified),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = VS_Success
                        )
                    }
                }

                VitalSenseCard(
                    backgroundColor = VS_SurfaceVariant,
                    border = BorderStroke(1.dp, VS_Success.copy(alpha = 0.4f))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text(
                            text = patient.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        Text(
                            text = stringResource(R.string.villageAgeLabel),
                            style = MaterialTheme.typography.bodySmall,
                            color = VS_OnSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.ashaWorkerLabel),
                            style = MaterialTheme.typography.labelSmall,
                            color = VS_PrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
