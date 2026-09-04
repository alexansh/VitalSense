package com.vitalsense.app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalsense.app.core.ui.theme.*

/**
 * Reusable Tabular Layout Component System for clinical data inspection.
 * Gives all metrics, queues, labs, and inventories a clean, scalar, high-contrast matrix layout.
 */

@Composable
fun TabularCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    headerTrailing: @Composable (() -> Unit)? = null,
    backgroundColor: Color = VS_Surface,
    borderColor: Color = VS_Outline,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = VS_OnBackground
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = VS_OnSurfaceVariant
                            )
                        }
                    }
                    if (headerTrailing != null) {
                        headerTrailing()
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun TabularHeaderRow(
    columns: List<Pair<String, Float>>, // Pair of Column Title and Column Weight
    modifier: Modifier = Modifier
) {
    Surface(
        color = VS_Outline.copy(alpha = 0.35f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns.forEach { (title, weight) ->
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        fontSize = 11.sp
                    ),
                    color = VS_OnSurfaceVariant,
                    modifier = Modifier.weight(weight),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TabularDataRow(
    modifier: Modifier = Modifier,
    isEven: Boolean = false,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 10.dp,
    content: @Composable RowScope.() -> Unit
) {
    val rowBg = if (isEven) VS_Background.copy(alpha = 0.5f) else Color.Transparent
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(rowBg, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun TabularStatusChip(
    statusText: String,
    containerColor: Color = NagarSevaStatusNormalBg,
    textColor: Color = VS_Success,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            textAlign = TextAlign.Center
        )
    }
}
