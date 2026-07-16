package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.ui.components.BannerAd
import org.goodexpert.ridefit.ui.components.StarRating
import org.goodexpert.ridefit.ui.components.button.PrimaryButton
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun CompletedScreen(
    modifier: Modifier = Modifier,
    rating: Int = 5,
    onNewRide: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            CompletedHeader()
            CompletedCard(rating = rating)
            PrimaryButton(
                title = stringResource(R.string.completed_new_ride),
                contentDescription = stringResource(R.string.completed_new_ride),
                onClick = onNewRide,
                horizontalAlignment = Alignment.CenterHorizontally,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
            )
        }

        // 하단 고정 배너 광고
        BannerAd()
    }
}

@Composable
private fun CompletedHeader(modifier: Modifier = Modifier) {
    val boxBg = MaterialTheme.rideFitColors.confirmContainer
    val accentColor = MaterialTheme.rideFitColors.onConfirmContainer

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(boxBg, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(42.dp),
            )
        }
        Text(
            text = stringResource(R.string.completed_title),
            style = MaterialTheme.typography.headlineLarge,
            color = accentColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CompletedCard(
    modifier: Modifier = Modifier,
    rating: Int = 5,
) {
    val bgColor = MaterialTheme.rideFitColors.cardContainer
    val borderColor = MaterialTheme.rideFitColors.cardBorder
    val labelColor = MaterialTheme.rideFitColors.onSurfaceFaint
    val scriptColor = MaterialTheme.rideFitColors.onBackgroundSecondary

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.completed_voice_label),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = labelColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            )
            Text(
                text = stringResource(R.string.script_completed),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                ),
                color = scriptColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            StarRating(
                modifier = Modifier.fillMaxWidth(),
                rating = rating,
            )
        }
    }
}

@Preview(name = "Completed · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CompletedScreenDayPreview() {
    RideFitTheme(darkTheme = false) {
        CompletedScreen()
    }
}

@Preview(name = "Completed · Night", showBackground = true, widthDp = 360, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CompletedScreenNightPreview() {
    RideFitTheme(darkTheme = true) {
        CompletedScreen()
    }
}
