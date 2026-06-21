package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.ui.components.StarRating
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun CompletedScreen(
    modifier: Modifier = Modifier,
    rating: Int = 5,
    onNewRide: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CompletedHeader()
        CompletedCard(rating = rating)
        NewRideButton(onClick = onNewRide)
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun CompletedHeader(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark
    val boxBg = if (!isDark) Color(0xFFDCFCE7) else Color(0xFF052E16)
    val accentColor = if (!isDark) appColors.confirmGreen else Color(0xFF4ADE80)

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
        Text(
            text = stringResource(R.string.completed_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
        )
    }
}

// ── Completed card ────────────────────────────────────────────────────────────

@Composable
private fun CompletedCard(
    modifier: Modifier = Modifier,
    rating: Int = 5,
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark
    val bgColor = if (!isDark) colorScheme.surface else Color(0xFF14142A)
    val borderColor = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)
    val labelColor = if (!isDark) Color(0xFF6677AA) else Color(0xFF4A4A7A)
    val scriptColor = if (!isDark) Color(0xFF3A5080) else Color(0xFF8899CC)

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
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
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
                modifier = Modifier.padding(bottom = 14.dp),
            )
            StarRating(
                modifier = Modifier.fillMaxWidth(),
                rating = rating,
            )
        }
    }
}

// ── New ride button ───────────────────────────────────────────────────────────

@Composable
private fun NewRideButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.brand),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.completed_new_ride),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                ),
                color = Color.White,
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

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
