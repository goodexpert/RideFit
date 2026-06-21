package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.components.VoiceScriptCard
import org.goodexpert.ridefit.ui.components.VoiceWave
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun VoiceGuideScreen(
    modifier: Modifier = Modifier,
    selectedMode: DriveMode = DriveMode.QUIET,
    isPlaying: Boolean = true,
    onSkip: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        StatusBadge(text = stringResource(selectedMode.labelRes()))

        Spacer(modifier = Modifier.height(32.dp))

        VoiceGuideWaveSection()

        VoiceScriptCard(
            script = stringResource(R.string.script_intro),
            label = stringResource(R.string.intro_badge),
            isPlaying = isPlaying,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SkipButton(onClick = onSkip)
    }
}

// ── Wave section ──────────────────────────────────────────────────────────────

@Composable
private fun VoiceGuideWaveSection(modifier: Modifier = Modifier) {
    val appColors = MaterialTheme.rideFitColors
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(
                    color = if (!appColors.isDark) Color(0xFFEEF2FF) else Color(0xFF0B1628),
                    shape = RoundedCornerShape(22.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            VoiceWave(color = appColors.brand)
        }
        Text(
            text = stringResource(R.string.intro_playing_label),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            ),
            color = colorScheme.onBackground,
        )
    }
}

// ── Skip button ───────────────────────────────────────────────────────────────

@Composable
private fun SkipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark
    val colorScheme = MaterialTheme.colorScheme

    val bgColor = if (!isDark) Color(0xFFF5F7FF) else Color(0xFF14142A)
    val borderColor = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)
    val contentColor = if (!isDark) appColors.brand else Color(0xFF5577FF)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
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
                imageVector = Icons.Filled.SkipNext,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(26.dp),
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = stringResource(R.string.intro_skip),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                    ),
                    color = contentColor,
                )
                Text(
                    text = stringResource(R.string.intro_skip_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                )
            }
        }
    }
}

// ── DriveMode helpers ─────────────────────────────────────────────────────────

private fun DriveMode.labelRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet
    DriveMode.FAST -> R.string.mode_fast
    DriveMode.SAFE -> R.string.mode_safe
    DriveMode.MEDIA -> R.string.mode_media
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "VoiceGuide · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun VoiceGuideScreenDayPreview() {
    RideFitTheme(darkTheme = false) {
        VoiceGuideScreen(selectedMode = DriveMode.FAST)
    }
}

@Preview(name = "VoiceGuide · Night", showBackground = true, widthDp = 360, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun VoiceGuideScreenNightPreview() {
    RideFitTheme(darkTheme = true) {
        VoiceGuideScreen(selectedMode = DriveMode.SAFE)
    }
}
