package org.goodexpert.ridefit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified,
    showDivider: Boolean = true,
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val dividerColor = if (!appColors.isDark) Color(0xFFE8EEFF) else Color(0xFF252545)
    val effectiveValueColor = if (valueColor == Color.Unspecified) colorScheme.onSurface else valueColor

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = appColors.onBackgroundSecondary,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = effectiveValueColor,
            )
        }
        if (showDivider) {
            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
            )
        }
    }
}
