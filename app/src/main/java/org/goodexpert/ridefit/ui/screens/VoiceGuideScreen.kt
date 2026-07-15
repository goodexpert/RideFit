package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.components.BannerAd
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.components.VoiceScriptCard
import org.goodexpert.ridefit.ui.components.VoiceWave
import org.goodexpert.ridefit.ui.components.button.SecondaryButton
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun VoiceGuideScreen(
    modifier: Modifier = Modifier,
    selectedMode: DriveMode = DriveMode.QUIET,
    isPlaying: Boolean = true,
    onSkip: () -> Unit = {},
    onCancel: () -> Unit = {},
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
                .padding(16.dp),
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

            CancelButton(onClick = onCancel)

            SkipButton(onClick = onSkip)
        }

        // 하단 고정 배너 광고
        BannerAd()
    }
}

@Composable
private fun VoiceGuideWaveSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(
                    color = MaterialTheme.rideFitColors.subtleContainer,
                    shape = RoundedCornerShape(24.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            VoiceWave(color = MaterialTheme.rideFitColors.brand)
        }
        Text(
            text = stringResource(R.string.intro_playing_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = MaterialTheme.rideFitColors.subtleContainer
    val borderColor = MaterialTheme.rideFitColors.cardBorder
    val contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)

    SecondaryButton(
        title = stringResource(R.string.intro_cancel),
        subtitle = stringResource(R.string.intro_cancel_hint),
        contentDescription = stringResource(R.string.intro_cancel),
        onClick = onClick,
        modifier = modifier,
        containerColor = bgColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        },
    )
}

@Composable
private fun SkipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = MaterialTheme.rideFitColors.subtleContainer
    val borderColor = MaterialTheme.rideFitColors.cardBorder
    val contentColor = MaterialTheme.rideFitColors.brandAccent

    SecondaryButton(
        title = stringResource(R.string.intro_skip),
        subtitle = stringResource(R.string.intro_skip_hint),
        contentDescription = stringResource(R.string.intro_skip),
        onClick = onClick,
        modifier = modifier,
        containerColor = bgColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.SkipNext,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        },
    )
}

private fun DriveMode.labelRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet
    DriveMode.FAST -> R.string.mode_fast
    DriveMode.SAFE -> R.string.mode_safe
    DriveMode.MEDIA -> R.string.mode_media
}

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
