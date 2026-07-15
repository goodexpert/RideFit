package org.goodexpert.ridefit.modules.main

import org.goodexpert.ridefit.model.DriveMode
import java.io.File

interface MainContract {

    enum class AppScreen {
        STANDBY, SETTINGS, INTRO, MODE_INTRO, COMPLETED, EMERGENCY_VIDEOS, ACCOUNT_GUIDE
    }

    sealed class Actions {
        object StartGuide : Actions()
        data class ModeSelected(val mode: DriveMode) : Actions()
        object IntroSkip : Actions()
        object IntroCompleted : Actions()
        object ModeIntroSkip : Actions()
        object ModeIntroCompleted : Actions()
        object RideComplete : Actions()
        object NewRide : Actions()
        object Back : Actions()
        object OpenSettings : Actions()
        object CloseSettings : Actions()
        data class SheetNavigated(val destination: AppScreen) : Actions()
        object SettingsSave : Actions()
        object SettingsBack : Actions()
        object ShowAccountGuide : Actions()
        object AccountGuideConfirm : Actions()
        object AccountGuideSetup : Actions()
        object EmergencyRecordingStarted : Actions()
        object EmergencyRecordingStopped : Actions()
        object ShowCancelGuideDialog : Actions()
        object DismissCancelGuideDialog : Actions()
        object CancelGuideConfirm : Actions()
        object ShowExitDialog : Actions()
        object DismissExitDialog : Actions()
        object ShowStopRecordingDialog : Actions()
        object DismissStopRecordingDialog : Actions()
        data class ToggleDarkMode(val enabled: Boolean) : Actions()
    }

    sealed class SideEffect {
        object Finish : SideEffect()
        object StartEmergencyRecording : SideEffect()
        object StopEmergencyRecording : SideEffect()
        data class PlayVideo(val file: File) : SideEffect()
        data class ShareVideo(val file: File) : SideEffect()
        object HideSettings : SideEffect()
        data class HideSettingsThenNavigate(val destination: AppScreen) : SideEffect()
    }

    data class ViewState(
        val currentScreen: AppScreen = AppScreen.STANDBY,
        val selectedMode: DriveMode? = null,
        val isPlayingAudio: Boolean = false,
        val isRiding: Boolean = false,
        val isSpeaking: Boolean = false,
        val isRecording: Boolean = false,
        val showExitDialog: Boolean = false,
        val showCancelGuideDialog: Boolean = false,
        val showStopRecordingDialog: Boolean = false,
        val showSettingsSheet: Boolean = false,
        val isDarkMode: Boolean = false,
    )
}
