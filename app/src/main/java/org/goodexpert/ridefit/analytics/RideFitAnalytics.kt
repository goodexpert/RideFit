package org.goodexpert.ridefit.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import org.goodexpert.ridefit.model.DriveMode

@Suppress("TooManyFunctions")
class RideFitAnalytics(private val firebase: FirebaseAnalytics) {

    fun logRideStarted(mode: DriveMode) {
        firebase.logEvent("ride_started") {
            param("drive_mode", mode.toAnalyticsParam())
        }
    }

    fun logRideCompleted(mode: DriveMode) {
        firebase.logEvent("ride_completed") {
            param("drive_mode", mode.toAnalyticsParam())
        }
    }

    fun logHomeButtonPressed() {
        firebase.logEvent("home_button_pressed") {}
    }

    fun logGuideCancelled() {
        firebase.logEvent("guide_cancelled") {}
    }

    fun logEmergencyVideosOpened() {
        firebase.logEvent("emergency_videos_opened") {}
    }

    fun logDriveModeSelected(mode: DriveMode) {
        firebase.logEvent("drive_mode_selected") {
            param("mode", mode.toAnalyticsParam())
        }
    }

    fun logSettingsOpened() {
        firebase.logEvent("settings_opened") {}
    }

    fun logSettingsSaved(isConfigured: Boolean) {
        firebase.logEvent("settings_saved") {
            param("is_configured", if (isConfigured) 1L else 0L)
        }
    }

    fun logSettingsCancelled() {
        firebase.logEvent("settings_cancelled") {}
    }

    fun logBankPickerOpened() {
        firebase.logEvent("bank_picker_opened") {}
    }

    fun logBankSelected(bankName: String) {
        firebase.logEvent("bank_selected") {
            param("bank_name", bankName)
        }
    }

    fun logIntroSkipped() {
        firebase.logEvent("intro_skipped") {}
    }

    fun logModeIntroSkipped(mode: DriveMode) {
        firebase.logEvent("mode_intro_skipped") {
            param("drive_mode", mode.toAnalyticsParam())
        }
    }

    fun logEmergencyPressed() {
        firebase.logEvent("emergency_pressed") {}
    }

    fun logEmergencyRecordingStopped() {
        firebase.logEvent("emergency_recording_stopped") {}
    }

    fun logEmergencyVideoPlayed() {
        firebase.logEvent("emergency_video_played") {}
    }

    fun logEmergencyVideoShared() {
        firebase.logEvent("emergency_video_shared") {}
    }
}

private fun DriveMode.toAnalyticsParam(): String = when (this) {
    DriveMode.QUIET -> "quiet"
    DriveMode.FAST  -> "fast"
    DriveMode.SAFE  -> "safe"
    DriveMode.MEDIA -> "media"
}
