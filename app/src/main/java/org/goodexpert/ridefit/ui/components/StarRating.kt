package org.goodexpert.ridefit.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    rating: Int = 5,
    maxStars: Int = 5,
    starSize: Dp = 30.dp,
    filledColor: Color = Color(0xFFD97706),
    emptyColor: Color = MaterialTheme.rideFitColors.cardBorder,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        repeat(maxStars) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating) filledColor else emptyColor,
                modifier = Modifier.size(starSize),
            )
        }
    }
}

/** Sample star counts (full, partial, empty) for previewing [StarRating]. */
class StarRatingProvider : PreviewParameterProvider<Int> {
    override val values = sequenceOf(5, 3, 0)
}

@Preview(name = "StarRating · Day", showBackground = true)
@Composable
private fun StarRatingDayPreview(
    @PreviewParameter(StarRatingProvider::class) rating: Int,
) {
    RideFitTheme(darkTheme = false) {
        StarRating(rating = rating)
    }
}

@Preview(name = "StarRating · Night", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StarRatingNightPreview(
    @PreviewParameter(StarRatingProvider::class) rating: Int,
) {
    RideFitTheme(darkTheme = true) {
        StarRating(rating = rating)
    }
}
