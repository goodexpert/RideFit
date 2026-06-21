package org.goodexpert.ridefit.ui.components

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    rating: Int = 5,
    maxStars: Int = 5,
    starSize: Dp = 30.dp,
    filledColor: Color = Color(0xFFD97706),
    emptyColor: Color = if (MaterialTheme.rideFitColors.isDark) Color(0xFF2A2A4A) else Color(0xFFD0DEFF),
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
