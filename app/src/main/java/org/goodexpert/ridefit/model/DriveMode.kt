package org.goodexpert.ridefit.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.vector.ImageVector
import org.goodexpert.ridefit.R

enum class DriveMode { QUIET, FAST, SAFE, MEDIA }

internal fun DriveMode.icon(): ImageVector = when (this) {
    DriveMode.QUIET -> Icons.AutoMirrored.Filled.VolumeOff
    DriveMode.FAST  -> Icons.Filled.Bolt
    DriveMode.SAFE  -> Icons.Filled.Security
    DriveMode.MEDIA -> Icons.Filled.Headphones
}

@StringRes
internal fun DriveMode.labelRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet
    DriveMode.FAST  -> R.string.mode_fast
    DriveMode.SAFE  -> R.string.mode_safe
    DriveMode.MEDIA -> R.string.mode_media
}

@StringRes
internal fun DriveMode.descRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.mode_quiet_desc
    DriveMode.FAST  -> R.string.mode_fast_desc
    DriveMode.SAFE  -> R.string.mode_safe_desc
    DriveMode.MEDIA -> R.string.mode_media_desc
}

@StringRes
internal fun DriveMode.hintRes(): Int = when (this) {
    DriveMode.QUIET -> R.string.ride_hint_quiet
    DriveMode.FAST  -> R.string.ride_hint_fast
    DriveMode.SAFE  -> R.string.ride_hint_safe
    DriveMode.MEDIA -> R.string.ride_hint_media
}
