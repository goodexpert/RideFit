package org.goodexpert.ridefit.modules.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.analytics.RideFitAnalytics
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.modules.main.MainContract.Actions
import org.goodexpert.ridefit.modules.main.MainContract.AppScreen
import org.goodexpert.ridefit.repository.AudioPlayer
import org.goodexpert.ridefit.repository.TtsPlayer
import java.io.File

@Suppress("TooManyFunctions")
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val audioPlayer = AudioPlayer(application)
    private val ttsPlayer = TtsPlayer(application)
    private val analytics = RideFitAnalytics(FirebaseAnalytics.getInstance(application))

    private val _viewState = MutableStateFlow(MainContract.ViewState())
    val viewState: StateFlow<MainContract.ViewState> = _viewState.asStateFlow()

    private val _sideEffect = Channel<MainContract.SideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        audioPlayer.isPlaying
            .onEach { isPlaying -> _viewState.update { it.copy(isPlayingAudio = isPlaying) } }
            .launchIn(viewModelScope)
        ttsPlayer.isSpeaking
            .onEach { isSpeaking -> _viewState.update { it.copy(isSpeaking = isSpeaking) } }
            .launchIn(viewModelScope)
    }

    // ── Event Handlers ────────────────────────────────────────────────────────

    fun onStartGuideHandler() {
        analytics.logRideStarted(DriveMode.QUIET)
        dispatch(Actions.StartGuide)
        audioPlayer.playAsset("00_DriveMode_Intro.mp3") {
            audioPlayer.playAsset(DriveMode.QUIET.audioFileName()) {
                viewModelScope.launch { dispatch(Actions.IntroCompleted) }
            }
        }
    }

    fun onModeSelectedHandler(mode: DriveMode) {
        analytics.logDriveModeSelected(mode)
        val isRiding = _viewState.value.isRiding
        dispatch(Actions.ModeSelected(mode))
        if (isRiding) {
            val resId = mode.welcomeScriptRes() ?: return
            ttsPlayer.speak(getApplication<Application>().getString(resId))
        } else {
            audioPlayer.playAsset(mode.audioFileName()) {
                viewModelScope.launch { dispatch(Actions.ModeIntroCompleted) }
            }
        }
    }

    fun onIntroSkipHandler() {
        analytics.logIntroSkipped()
        audioPlayer.release()
        dispatch(Actions.IntroSkip)
    }

    fun onModeIntroSkipHandler() {
        analytics.logModeIntroSkipped(_viewState.value.selectedMode ?: DriveMode.QUIET)
        audioPlayer.release()
        dispatch(Actions.ModeIntroSkip)
    }

    fun onRideCompleteHandler() {
        analytics.logRideCompleted(_viewState.value.selectedMode ?: DriveMode.QUIET)
        audioPlayer.release()
        dispatch(Actions.RideComplete)
        ttsPlayer.speak(getApplication<Application>().getString(R.string.tts_completed))
    }

    fun onNewRideHandler() {
        analytics.logHomeButtonPressed()
        ttsPlayer.stop()
        audioPlayer.release()
        dispatch(Actions.NewRide)
    }

    fun onBackHandler() {
        val state = _viewState.value
        when {
            state.showSettingsSheet -> emit(MainContract.SideEffect.HideSettings)
            state.currentScreen == AppScreen.EMERGENCY_VIDEOS -> dispatch(Actions.Back)
            (state.currentScreen == AppScreen.INTRO || state.currentScreen == AppScreen.MODE_INTRO) &&
                state.isPlayingAudio -> dispatch(Actions.ShowCancelGuideDialog)
            state.isRiding -> {
                ttsPlayer.stop()
                audioPlayer.release()
                dispatch(Actions.Back)
            }
            state.currentScreen == AppScreen.STANDBY -> dispatch(Actions.ShowExitDialog)
            else -> {
                ttsPlayer.stop()
                audioPlayer.release()
                dispatch(Actions.Back)
            }
        }
    }

    fun onSheetClosedHandler() {
        dispatch(Actions.CloseSettings)
    }

    fun onSheetNavigatedHandler(destination: AppScreen) {
        dispatch(Actions.SheetNavigated(destination))
    }

    fun onOpenSettingsHandler() {
        analytics.logSettingsOpened()
        dispatch(Actions.OpenSettings)
    }

    fun onNavigateToAccountSettingsHandler() {
        emit(MainContract.SideEffect.HideSettingsThenNavigate(AppScreen.SETTINGS))
    }

    fun onNavigateToEmergencyVideosHandler() {
        analytics.logEmergencyVideosOpened()
        emit(MainContract.SideEffect.HideSettingsThenNavigate(AppScreen.EMERGENCY_VIDEOS))
    }

    fun onSettingsSaveHandler() {
        dispatch(Actions.SettingsSave)
    }

    fun onSettingsBackHandler() {
        analytics.logSettingsCancelled()
        dispatch(Actions.SettingsBack)
    }

    fun onShowAccountGuideHandler(account: BankAccount) {
        dispatch(Actions.ShowAccountGuide)
        if (account.isConfigured) speakAccountGuide(account)
    }

    fun onAccountGuideRepeatHandler(account: BankAccount) {
        if (account.isConfigured) speakAccountGuide(account)
    }

    fun onAccountGuideConfirmHandler() {
        ttsPlayer.stop()
        audioPlayer.release()
        dispatch(Actions.AccountGuideConfirm)
    }

    fun onAccountGuideSetupHandler() {
        ttsPlayer.stop()
        audioPlayer.release()
        dispatch(Actions.AccountGuideSetup)
    }

    fun onEmergencyHandler() {
        analytics.logEmergencyPressed()
        emit(MainContract.SideEffect.StartEmergencyRecording)
    }

    fun onEmergencyRecordingStartedHandler() {
        dispatch(Actions.EmergencyRecordingStarted)
    }

    fun onStopRecordingFabPressedHandler() {
        dispatch(Actions.ShowStopRecordingDialog)
    }

    fun onCancelGuideConfirmHandler() {
        analytics.logGuideCancelled()
        ttsPlayer.stop()
        audioPlayer.release()
        dispatch(Actions.CancelGuideConfirm)
    }

    fun onCancelGuideDismissHandler() {
        dispatch(Actions.DismissCancelGuideDialog)
    }

    fun onExitConfirmHandler() {
        emit(MainContract.SideEffect.Finish)
    }

    fun onExitDismissHandler() {
        dispatch(Actions.DismissExitDialog)
    }

    fun onStopRecordingConfirmHandler() {
        analytics.logEmergencyRecordingStopped()
        dispatch(Actions.EmergencyRecordingStopped)
        emit(MainContract.SideEffect.StopEmergencyRecording)
    }

    fun onStopRecordingDismissHandler() {
        dispatch(Actions.DismissStopRecordingDialog)
    }

    fun onPlayVideoHandler(file: File) {
        analytics.logEmergencyVideoPlayed()
        emit(MainContract.SideEffect.PlayVideo(file))
    }

    // ── Dispatch + Reduce ─────────────────────────────────────────────────────

    private fun dispatch(action: Actions) {
        _viewState.update { reduce(it, action) }
    }

    private fun emit(effect: MainContract.SideEffect) {
        viewModelScope.launch { _sideEffect.send(effect) }
    }

    @Suppress("CyclomaticComplexMethod")
    private fun reduce(state: MainContract.ViewState, action: Actions): MainContract.ViewState = when (action) {
        Actions.StartGuide -> state.copy(
            currentScreen = AppScreen.INTRO,
            selectedMode = DriveMode.QUIET,
        )
        is Actions.ModeSelected -> if (state.isRiding) {
            state.copy(selectedMode = action.mode)
        } else {
            state.copy(selectedMode = action.mode, currentScreen = AppScreen.MODE_INTRO)
        }
        Actions.IntroSkip,
        Actions.IntroCompleted,
        -> state.copy(currentScreen = AppScreen.STANDBY, isRiding = true)
        Actions.ModeIntroSkip,
        Actions.ModeIntroCompleted,
        -> state.copy(currentScreen = AppScreen.STANDBY, isRiding = true)
        Actions.RideComplete -> state.copy(currentScreen = AppScreen.COMPLETED, isRiding = false)
        Actions.NewRide -> state.copy(
            currentScreen = AppScreen.STANDBY,
            isRiding = false,
            selectedMode = null,
        )
        Actions.Back -> when {
            state.currentScreen == AppScreen.EMERGENCY_VIDEOS -> state.copy(currentScreen = AppScreen.STANDBY)
            state.isRiding -> state.copy(isRiding = false)
            else -> state.copy(currentScreen = AppScreen.STANDBY)
        }
        Actions.OpenSettings -> state.copy(showSettingsSheet = true)
        Actions.CloseSettings -> state.copy(showSettingsSheet = false)
        is Actions.SheetNavigated -> state.copy(
            showSettingsSheet = false,
            currentScreen = action.destination,
        )
        Actions.SettingsSave,
        Actions.SettingsBack,
        -> state.copy(currentScreen = AppScreen.STANDBY)
        Actions.ShowAccountGuide -> state.copy(currentScreen = AppScreen.ACCOUNT_GUIDE)
        Actions.AccountGuideConfirm -> state.copy(currentScreen = AppScreen.STANDBY)
        Actions.AccountGuideSetup -> state.copy(currentScreen = AppScreen.SETTINGS)
        Actions.EmergencyRecordingStarted -> state.copy(isRecording = true)
        Actions.EmergencyRecordingStopped -> state.copy(
            isRecording = false,
            showStopRecordingDialog = false,
        )
        Actions.ShowCancelGuideDialog -> state.copy(showCancelGuideDialog = true)
        Actions.DismissCancelGuideDialog -> state.copy(showCancelGuideDialog = false)
        Actions.CancelGuideConfirm -> state.copy(
            showCancelGuideDialog = false,
            currentScreen = AppScreen.STANDBY,
        )
        Actions.ShowExitDialog -> state.copy(showExitDialog = true)
        Actions.DismissExitDialog -> state.copy(showExitDialog = false)
        Actions.ShowStopRecordingDialog -> state.copy(showStopRecordingDialog = true)
        Actions.DismissStopRecordingDialog -> state.copy(showStopRecordingDialog = false)
    }

    private fun speakAccountGuide(account: BankAccount) {
        val app = getApplication<Application>()
        val intro = app.getString(R.string.tts_transfer_intro, account.bankName, account.holderName)
        val outro = app.getString(R.string.tts_transfer_outro)
        ttsPlayer.speakQueued(listOf(intro) + account.digits + listOf(outro))
    }

    override fun onCleared() {
        ttsPlayer.shutdown()
        audioPlayer.release()
    }
}

private fun DriveMode.welcomeScriptRes(): Int? = when (this) {
    DriveMode.QUIET -> R.string.tts_welcome_quiet
    DriveMode.FAST  -> R.string.tts_welcome_fast
    DriveMode.SAFE  -> R.string.tts_welcome_safe
    DriveMode.MEDIA -> R.string.tts_welcome_media
}

private fun DriveMode.audioFileName(): String = when (this) {
    DriveMode.QUIET -> "01_DriveMode_Quite.mp3"
    DriveMode.FAST  -> "02_DriveMode_Fast.mp3"
    DriveMode.SAFE  -> "03_DriveMode_Safe.mp3"
    DriveMode.MEDIA -> "04_DriveMode_Media.mp3"
}
