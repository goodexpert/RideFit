package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.model.descRes
import org.goodexpert.ridefit.model.hintRes
import org.goodexpert.ridefit.model.icon
import org.goodexpert.ridefit.model.labelRes
import org.goodexpert.ridefit.ui.components.BannerAd
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.components.button.PrimaryButton
import org.goodexpert.ridefit.ui.components.button.SecondaryButton
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    selectedMode: DriveMode? = null,
    isRiding: Boolean = false,
    isRecording: Boolean = false,
    onModeChange: (DriveMode) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onStartGuide: () -> Unit = {},
    onComplete: () -> Unit = {},
    onCancelRide: () -> Unit = {},
    onAccountInfo: () -> Unit = {},
    onEmergency: () -> Unit = {},
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
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MainScreenHeader(isRiding = isRiding, onOpenSettings = onOpenSettings)

            if (isRiding) {
                DriveModeHintCard(mode = selectedMode ?: DriveMode.QUIET)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CompleteButton(
                        onClick = onComplete,
                        modifier = Modifier.weight(1f),
                    )
                    CancelRideButton(
                        onClick = onCancelRide,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                PrimaryButton(
                    title = stringResource(R.string.start_guide_label),
                    subtitle = stringResource(R.string.start_guide_hint),
                    contentDescription = stringResource(R.string.start_guide_label),
                    onClick = onStartGuide,
                    containerColor = MaterialTheme.rideFitColors.brand,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                        )
                    },
                )
            }

            SectionLabel(textRes = R.string.section_drive_mode)
            DriveModeGrid(
                selectedMode = selectedMode,
                onModeChange = onModeChange,
            )
            PrimaryButton(
                title = stringResource(R.string.main_account_info),
                contentDescription = stringResource(R.string.main_account_info),
                onClick = onAccountInfo,
                horizontalAlignment = Alignment.CenterHorizontally,
                containerColor = MaterialTheme.rideFitColors.brand,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
            )
            EmergencyButton(
                isRecording = isRecording,
                onLongClick = onEmergency,
            )
        }

        // 하단 고정 배너 광고
        BannerAd()
    }
}

@Composable
private fun MainScreenHeader(isRiding: Boolean, onOpenSettings: () -> Unit) {
    val badgeText = stringResource(if (isRiding) R.string.ride_status else R.string.standby_status)
    val title     = stringResource(if (isRiding) R.string.ride_title_main else R.string.standby_title)
    val desc      = stringResource(if (isRiding) R.string.ride_desc else R.string.standby_desc)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            StatusBadge(text = badgeText)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
        }
        IconButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings_open),
                tint = MaterialTheme.rideFitColors.onBackgroundSecondary,
            )
        }
    }
}

@Composable
private fun CompleteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    SecondaryButton(
        title = stringResource(R.string.ride_complete),
        contentDescription = stringResource(R.string.ride_complete),
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.rideFitColors.confirmContainer,
        contentColor = MaterialTheme.rideFitColors.onConfirmContainer,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
    )
}

/**
 * Cancels the ride straight to standby — no arrival guidance / completion screen.
 * Mirrors the "운행 취소" button on [VoiceGuideScreen] (same label, icon, and tonal style).
 */
@Composable
private fun CancelRideButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    SecondaryButton(
        title = stringResource(R.string.intro_cancel),
        contentDescription = stringResource(R.string.intro_cancel),
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.rideFitColors.subtleContainer,
        contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
        border = BorderStroke(1.dp, MaterialTheme.rideFitColors.cardBorder),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
    )
}

@Composable
private fun DriveModeHintCard(mode: DriveMode) {
    val bgColor     = MaterialTheme.rideFitColors.cardContainer
    val borderColor = MaterialTheme.rideFitColors.cardBorder
    val iconColor   = MaterialTheme.rideFitColors.brandAccent

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = mode.icon(),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(mode.labelRes()),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(mode.hintRes()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(@StringRes textRes: Int) {
    Text(
        text = stringResource(textRes),
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
    )
}

@Composable
private fun DriveModeGrid(
    selectedMode: DriveMode?,
    onModeChange: (DriveMode) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DriveModeCard(
                mode = DriveMode.QUIET,
                isSelected = selectedMode == DriveMode.QUIET,
                onClick = { onModeChange(DriveMode.QUIET) },
                modifier = Modifier.weight(1f),
            )
            DriveModeCard(
                mode = DriveMode.FAST,
                isSelected = selectedMode == DriveMode.FAST,
                onClick = { onModeChange(DriveMode.FAST) },
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DriveModeCard(
                mode = DriveMode.SAFE,
                isSelected = selectedMode == DriveMode.SAFE,
                onClick = { onModeChange(DriveMode.SAFE) },
                modifier = Modifier.weight(1f),
            )
            DriveModeCard(
                mode = DriveMode.MEDIA,
                isSelected = selectedMode == DriveMode.MEDIA,
                onClick = { onModeChange(DriveMode.MEDIA) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DriveModeCard(
    mode: DriveMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) MaterialTheme.rideFitColors.subtleContainer else MaterialTheme.rideFitColors.cardContainer
    val borderColor = if (isSelected) MaterialTheme.rideFitColors.brand else MaterialTheme.rideFitColors.cardBorder
    val accentColor = MaterialTheme.rideFitColors.brandAccent
    val labelColor = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 88.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .heightIn(96.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = mode.icon(),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(24.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(mode.labelRes()),
                    style = MaterialTheme.typography.titleMedium,
                    color = labelColor,
                )

                Text(
                    text = stringResource(mode.descRes()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
private fun EmergencyButton(
    isRecording: Boolean,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor     = MaterialTheme.rideFitColors.emergencyContainer
    val borderColor = if (isRecording) MaterialTheme.rideFitColors.emergencyRed else MaterialTheme.rideFitColors.emergencyBorder
    val labelColor = MaterialTheme.rideFitColors.onEmergency
    val subColor   = MaterialTheme.rideFitColors.onEmergencyVariant

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongClick() })
            },
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(if (isRecording) 2.dp else 1.5.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PulseDot(color = MaterialTheme.rideFitColors.emergencyRed)
            Column {
                Text(
                    text = stringResource(
                        if (isRecording) R.string.emergency_title_recording else R.string.emergency_title,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = labelColor,
                )
                Text(
                    text = stringResource(
                        if (isRecording) R.string.emergency_desc_recording else R.string.emergency_desc,
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = subColor,
                )
            }
        }
    }
}

@Composable
fun PulseDot(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 11.dp,
) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dotAlpha",
    )
    Box(
        modifier = modifier
            .size(size)
            .background(color.copy(alpha = alpha), CircleShape),
    )
}

@Preview(name = "Main · Standby · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MainScreenStandbyDayPreview() {
    RideFitTheme(darkTheme = false) {
        var mode by remember { mutableStateOf(DriveMode.QUIET) }
        MainScreen(selectedMode = mode, isRiding = false, onModeChange = { mode = it })
    }
}

@Preview(name = "Main · Riding · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MainScreenRidingDayPreview() {
    RideFitTheme(darkTheme = false) {
        var mode by remember { mutableStateOf(DriveMode.QUIET) }
        MainScreen(selectedMode = mode, isRiding = true, onModeChange = { mode = it })
    }
}

@Preview(
    name = "Main · Standby · Night",
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun MainScreenStandbyNightPreview() {
    RideFitTheme(darkTheme = true) {
        var mode by remember { mutableStateOf(DriveMode.FAST) }
        MainScreen(selectedMode = mode, isRiding = false, onModeChange = { mode = it })
    }
}

@Preview(
    name = "Main · Riding · Night",
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun MainScreenRidingNightPreview() {
    RideFitTheme(darkTheme = true) {
        var mode by remember { mutableStateOf(DriveMode.SAFE) }
        MainScreen(selectedMode = mode, isRiding = true, onModeChange = { mode = it })
    }
}
