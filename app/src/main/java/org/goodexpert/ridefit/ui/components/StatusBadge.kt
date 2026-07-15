package org.goodexpert.ridefit.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors

    val bgColor = appColors.cardContainer
    val borderColor = appColors.cardBorder
    val textColor = appColors.brandAccent

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(appColors.brand, CircleShape),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = textColor,
            )
        }
    }
}

/** Sample status labels used across the app (standby, riding, drive modes). */
class StatusBadgeTextProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "승객 탑승 전",
        "운행 중",
        "빠른 이동",
        "안전 운행",
    )
}

@Preview(name = "StatusBadge · Day", showBackground = true)
@Composable
private fun StatusBadgeDayPreview(
    @PreviewParameter(StatusBadgeTextProvider::class) text: String,
) {
    RideFitTheme(darkTheme = false) {
        StatusBadge(text = text)
    }
}

@Preview(name = "StatusBadge · Night", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StatusBadgeNightPreview(
    @PreviewParameter(StatusBadgeTextProvider::class) text: String,
) {
    RideFitTheme(darkTheme = true) {
        StatusBadge(text = text)
    }
}
