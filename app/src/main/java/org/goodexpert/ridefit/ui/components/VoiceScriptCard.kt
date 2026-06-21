package org.goodexpert.ridefit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun VoiceScriptCard(
    script: String,
    label: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
) {
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark

    val bgColor = if (!isDark) Color(0xFFEEF2FF) else Color(0xFF0B1628)
    val borderColor = if (!isDark) appColors.brand.copy(alpha = 0.25f) else Color(0xFF5577FF).copy(alpha = 0.25f)
    val scriptColor = if (!isDark) appColors.brand else Color(0xFF93C5FD)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (isPlaying) {
                    VoiceWave(color = appColors.brand)
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    ),
                    color = appColors.brand,
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = script,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                ),
                color = scriptColor,
            )
        }
    }
}
