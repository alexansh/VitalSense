package com.vitalsense.app.feature.prescriptions.ocr
import androidx.compose.ui.res.stringResource
import com.vitalsense.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vitalsense.app.core.ui.theme.*
import java.io.File

/**
 * Review screen shown immediately after camera photo capture.
 * Lets the user inspect the captured photo for blurriness/framing
 * before running on-device ML Kit OCR.
 */
@Composable
fun PrescriptionPhotoReviewScreen(
    photoFile: File,
    onConfirmUsePhoto: (File) -> Unit,
    onRetakePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VS_Background)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = Spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            Text(
                text = stringResource(R.string.reviewPrescriptionPhoto),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = VS_OnBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.ensureHandwritingReadable),
                style = MaterialTheme.typography.bodySmall,
                color = VS_OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // Captured Photo Preview Container
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = Spacing.md),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            border = BorderStroke(1.5.dp, VS_Outline)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = photoFile,
                    contentDescription = "Captured Prescription Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Two Large Action Buttons (56dp height)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            // Confirm button
            Button(
                onClick = { onConfirmUsePhoto(photoFile) },
                colors = ButtonDefaults.buttonColors(containerColor = VS_Primary),
                shape = PillShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = stringResource(R.string.useThisPhotoScanText),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Retake button
            OutlinedButton(
                onClick = onRetakePhoto,
                shape = PillShape,
                border = BorderStroke(1.5.dp, VS_Outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(R.string.retakePhotoBtn),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VS_OnBackground
                )
            }
        }
    }
}
