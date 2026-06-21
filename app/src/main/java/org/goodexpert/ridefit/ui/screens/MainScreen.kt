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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.theme.RideFitColors
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
    onAccountInfo: () -> Unit = {},
    onEmergency: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors

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
        MainScreenHeader(isRiding = isRiding, onOpenSettings = onOpenSettings)

        if (isRiding) {
            DriveModeHintCard(mode = selectedMode ?: DriveMode.QUIET)
            CompleteButton(onClick = onComplete)
        } else {
            StartGuideButton(onClick = onStartGuide, brandColor = appColors.brand)
        }

        SectionLabel(textRes = R.string.section_drive_mode)
        DriveModeGrid(
            selectedMode = selectedMode,
            onModeChange = onModeChange,
            appColors = appColors,
        )
        AccountInfoButton(
            onClick = onAccountInfo,
            appColors = appColors,
        )
        EmergencyButton(
            isRecording = isRecording,
            onLongClick = onEmergency,
            appColors = appColors,
        )
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun MainScreenHeader(isRiding: Boolean, onOpenSettings: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

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
                color = colorScheme.onBackground,
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onBackground.copy(alpha = 0.55f),
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

// ── Start Guide Button ────────────────────────────────────────────────────────

@Composable
private fun StartGuideButton(onClick: () -> Unit, brandColor: Color) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = brandColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp),
            )
            Column {
                Text(
                    text = stringResource(R.string.start_guide_label),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                    ),
                    color = Color.White,
                )
                Text(
                    text = stringResource(R.string.start_guide_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.65f),
                )
            }
        }
    }
}

// ── Complete Button ───────────────────────────────────────────────────────────

@Composable
private fun CompleteButton(onClick: () -> Unit) {
    val isDark = MaterialTheme.rideFitColors.isDark

    val bgColor      = if (!isDark) Color(0xFFDCFCE7) else Color(0xFF052E16)
    val contentColor = if (!isDark) Color(0xFF15803D) else Color(0xFF4ADE80)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
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
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.ride_complete),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                ),
                color = contentColor,
            )
        }
    }
}

// ── Drive Mode Hint Card ──────────────────────────────────────────────────────

@Composable
private fun DriveModeHintCard(mode: DriveMode) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark

    val bgColor     = if (!isDark) colorScheme.surface else Color(0xFF141428)
    val borderColor = if (!isDark) Color(0xFFD0DEFF)   else Color(0xFF2A2A4A)
    val iconColor   = if (!isDark) appColors.brand      else Color(0xFF5577FF)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = mode.icon(),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(22.dp)
                    .padding(top = 2.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(mode.labelRes()),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = colorScheme.onSurface,
                )
                Text(
                    text = stringResource(mode.hintRes()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface.copy(alpha = 0.65f),
                )
            }
        }
    }
}

// ── Section Label ─────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(@StringRes textRes: Int) {
    Text(
        text = stringResource(textRes),
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
    )
}

// ── Drive Mode Grid ───────────────────────────────────────────────────────────

@Composable
private fun DriveModeGrid(
    selectedMode: DriveMode?,
    onModeChange: (DriveMode) -> Unit,
    appColors: RideFitColors,
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
                appColors = appColors,
                modifier = Modifier.weight(1f),
            )
            DriveModeCard(
                mode = DriveMode.FAST,
                isSelected = selectedMode == DriveMode.FAST,
                onClick = { onModeChange(DriveMode.FAST) },
                appColors = appColors,
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
                appColors = appColors,
                modifier = Modifier.weight(1f),
            )
            DriveModeCard(
                mode = DriveMode.MEDIA,
                isSelected = selectedMode == DriveMode.MEDIA,
                onClick = { onModeChange(DriveMode.MEDIA) },
                appColors = appColors,
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
    appColors: RideFitColors,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = appColors.isDark

    val containerColor = when {
        isSelected && !isDark -> Color(0xFFEEF2FF)
        isSelected &&  isDark -> Color(0xFF1A1A3A)
        !isDark               -> colorScheme.surface
        else                  -> Color(0xFF14142A)
    }
    val borderColor = when {
        isSelected -> appColors.brand
        !isDark    -> Color(0xFFD0DEFF)
        else       -> Color(0xFF2A2A4A)
    }
    val iconColor  = if (isDark) Color(0xFF5577FF) else appColors.brand
    val labelColor = if (isSelected && !isDark) appColors.brand else colorScheme.onSurface

    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 88.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(
                imageVector = mode.icon(),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.height(7.dp))
            Text(
                text = stringResource(mode.labelRes()),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                color = labelColor,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(mode.descRes()),
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

// ── Account Info Button ───────────────────────────────────────────────────────

@Composable
private fun AccountInfoButton(
    onClick: () -> Unit,
    appColors: RideFitColors,
    modifier: Modifier = Modifier,
) {
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
                imageVector = Icons.Filled.AccountBalance,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.main_account_info),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                ),
                color = Color.White,
            )
        }
    }
}

// ── Emergency Button ──────────────────────────────────────────────────────────

@Composable
private fun EmergencyButton(
    isRecording: Boolean,
    onLongClick: () -> Unit,
    appColors: RideFitColors,
    modifier: Modifier = Modifier,
) {
    val isDark = appColors.isDark

    val bgColor     = if (!isDark) Color(0xFFFFF5F5) else Color(0xFF1A0808)
    val borderColor = when {
        isRecording -> appColors.emergencyRed
        !isDark     -> Color(0xFFFFCCCC)
        else        -> Color(0xFF4A1515)
    }
    val labelColor = if (!isDark) Color(0xFFCC2222) else Color(0xFFFF5555)
    val subColor   = if (!isDark) Color(0xFFAA4444) else Color(0xFF883333)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongClick() })
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(if (isRecording) 2.dp else 1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PulseDot(color = appColors.emergencyRed)
            Column {
                Text(
                    text = stringResource(
                        if (isRecording) R.string.emergency_title_recording else R.string.emergency_title,
                    ),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
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

// ── Shared components ─────────────────────────────────────────────────────────

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

// ── DriveMode helpers ─────────────────────────────────────────────────────────

private fun DriveMode.icon(): ImageVector = when (this) {
    DriveMode.QUIET -> Icons.AutoMirrored.Filled.VolumeOff
    DriveMode.FAST  -> Icons.Filled.Bolt
    DriveMode.SAFE  -> Icons.Filled.Security
    DriveMode.MEDIA -> Icons.Filled.Headphones
}

@StringRes
private fun DriveMode.labelRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet
    DriveMode.FAST  -> R.string.mode_fast
    DriveMode.SAFE  -> R.string.mode_safe
    DriveMode.MEDIA -> R.string.mode_media
}

@StringRes
private fun DriveMode.descRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet_desc
    DriveMode.FAST  -> R.string.mode_fast_desc
    DriveMode.SAFE  -> R.string.mode_safe_desc
    DriveMode.MEDIA -> R.string.mode_media_desc
}

@StringRes
private fun DriveMode.hintRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.ride_hint_quiet
    DriveMode.FAST  -> R.string.ride_hint_fast
    DriveMode.SAFE  -> R.string.ride_hint_safe
    DriveMode.MEDIA -> R.string.ride_hint_media
}

// ── Previews ──────────────────────────────────────────────────────────────────

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
