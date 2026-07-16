package org.goodexpert.ridefit.viewmodel

import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.modules.main.MainContract.AppScreen
import org.goodexpert.ridefit.modules.main.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowMediaPlayer

@Suppress("TooManyFunctions")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        ShadowMediaPlayer.setMediaInfoProvider { ShadowMediaPlayer.MediaInfo(FAKE_AUDIO_DURATION_MS, 0) }
        viewModel = MainViewModel(RuntimeEnvironment.getApplication())
    }

    private companion object {
        const val FAKE_AUDIO_DURATION_MS = 1_000
    }

    private val state get() = viewModel.viewState.value

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun initialState_screenIsStandby() {
        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    @Test
    fun initialState_notRiding() {
        assertFalse(state.isRiding)
    }

    @Test
    fun initialState_noModeSelected() {
        assertNull(state.selectedMode)
    }

    @Test
    fun initialState_noDialogsVisible() {
        assertFalse(state.showExitDialog)
        assertFalse(state.showCancelGuideDialog)
        assertFalse(state.showStopRecordingDialog)
        assertFalse(state.showSettingsSheet)
    }

    @Test
    fun initialState_notRecording() {
        assertFalse(state.isRecording)
    }

    // ── 탑승 안내 시작 → INTRO → STANDBY (isRiding) ──────────────────────────

    @Test
    fun startGuide_navigatesToIntro() {
        viewModel.onStartGuideHandler()
        assertEquals(AppScreen.INTRO, state.currentScreen)
    }

    @Test
    fun startGuide_setsModeToQuiet() {
        viewModel.onStartGuideHandler()
        assertEquals(DriveMode.QUIET, state.selectedMode)
    }

    @Test
    fun introSkip_returnsToStandby_andStartsRiding() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertTrue(state.isRiding)
    }

    // ── 모드 카드 탭 → MODE_INTRO → STANDBY (isRiding) ───────────────────────

    @Test
    fun modeSelected_notRiding_navigatesToModeIntro() {
        viewModel.onModeSelectedHandler(DriveMode.FAST)
        assertEquals(AppScreen.MODE_INTRO, state.currentScreen)
        assertEquals(DriveMode.FAST, state.selectedMode)
    }

    @Test
    fun modeIntroSkip_returnsToStandby_andStartsRiding() {
        viewModel.onModeSelectedHandler(DriveMode.FAST)
        viewModel.onModeIntroSkipHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertTrue(state.isRiding)
    }

    @Test
    fun modeSelected_allModes_reflectedInState() {
        DriveMode.entries.forEach { mode ->
            viewModel.onModeSelectedHandler(mode)
            assertEquals(mode, state.selectedMode)
        }
    }

    // ── 운행 중 모드 변경 ─────────────────────────────────────────────────────

    @Test
    fun modeSelected_whileRiding_updatesMode_doesNotChangeScreen() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()

        viewModel.onModeSelectedHandler(DriveMode.SAFE)

        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertEquals(DriveMode.SAFE, state.selectedMode)
        assertTrue(state.isRiding)
    }

    @Test
    fun modeSelected_whileRiding_doesNotNavigateToModeIntro() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()

        viewModel.onModeSelectedHandler(DriveMode.MEDIA)

        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    // ── 운행 완료 ─────────────────────────────────────────────────────────────

    @Test
    fun rideComplete_navigatesToCompleted_stopsRiding() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()
        viewModel.onRideCompleteHandler()

        assertEquals(AppScreen.COMPLETED, state.currentScreen)
        assertFalse(state.isRiding)
    }

    @Test
    fun newRide_returnsToStandby_clearsMode() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()
        viewModel.onRideCompleteHandler()
        viewModel.onNewRideHandler()

        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertFalse(state.isRiding)
        assertNull(state.selectedMode)
    }

    // ── Back 버튼 ─────────────────────────────────────────────────────────────

    @Test
    fun back_onStandby_showsExitDialog() {
        viewModel.onBackHandler()
        assertTrue(state.showExitDialog)
    }

    @Test
    fun back_whileRiding_stopsRiding_returnsToStandby() {
        viewModel.onStartGuideHandler()
        viewModel.onIntroSkipHandler()

        viewModel.onBackHandler()

        assertFalse(state.isRiding)
        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    @Test
    fun back_onEmergencyVideos_returnsToStandby() {
        // SheetNavigated(EMERGENCY_VIDEOS) simulates the Activity callback after sheet hide animation
        viewModel.onSheetNavigatedHandler(AppScreen.EMERGENCY_VIDEOS)

        viewModel.onBackHandler()

        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    // ── 앱 종료 다이얼로그 ────────────────────────────────────────────────────

    @Test
    fun dismissExitDialog_hidesDialog() {
        viewModel.onBackHandler()
        viewModel.onExitDismissHandler()
        assertFalse(state.showExitDialog)
    }

    // ── 설정 시트 ─────────────────────────────────────────────────────────────

    @Test
    fun openSettings_showsSheet() {
        viewModel.onOpenSettingsHandler()
        assertTrue(state.showSettingsSheet)
    }

    @Test
    fun sheetClosed_hidesSheet() {
        viewModel.onOpenSettingsHandler()
        viewModel.onSheetClosedHandler()
        assertFalse(state.showSettingsSheet)
    }

    @Test
    fun sheetNavigated_hidesSheet_andChangesScreen() {
        viewModel.onOpenSettingsHandler()
        viewModel.onSheetNavigatedHandler(AppScreen.SETTINGS)
        assertFalse(state.showSettingsSheet)
        assertEquals(AppScreen.SETTINGS, state.currentScreen)
    }

    @Test
    fun settingsSave_navigatesToStandby() {
        viewModel.onSettingsSaveHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    @Test
    fun settingsBack_navigatesToStandby() {
        viewModel.onSettingsBackHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    // ── 계좌 안내 흐름 ────────────────────────────────────────────────────────

    @Test
    fun showAccountGuide_navigatesToAccountGuide() {
        viewModel.onShowAccountGuideHandler(BankAccount())
        assertEquals(AppScreen.ACCOUNT_GUIDE, state.currentScreen)
    }

    @Test
    fun accountGuideConfirm_returnsToStandby() {
        viewModel.onShowAccountGuideHandler(BankAccount())
        viewModel.onAccountGuideConfirmHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
    }

    @Test
    fun accountGuideSetup_navigatesToSettings() {
        viewModel.onShowAccountGuideHandler(BankAccount())
        viewModel.onAccountGuideSetupHandler()
        assertEquals(AppScreen.SETTINGS, state.currentScreen)
    }

    // ── 안내 취소 다이얼로그 ──────────────────────────────────────────────────

    @Test
    fun cancelGuidePressed_showsCancelGuideDialog() {
        viewModel.onStartGuideHandler()
        viewModel.onCancelGuidePressedHandler()
        assertTrue(state.showCancelGuideDialog)
    }

    @Test
    fun cancelGuidePressed_thenConfirm_returnsToStandby_notRiding() {
        viewModel.onModeSelectedHandler(DriveMode.FAST)
        viewModel.onCancelGuidePressedHandler()
        viewModel.onCancelGuideConfirmHandler()

        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertFalse(state.isRiding)
        // Cancelling clears the in-progress mode selection so STANDBY is a clean pre-ride state.
        assertNull(state.selectedMode)
        assertFalse(state.showCancelGuideDialog)
    }

    @Test
    fun cancelGuidePressed_thenDismiss_staysOnIntro() {
        viewModel.onStartGuideHandler()
        viewModel.onCancelGuidePressedHandler()
        viewModel.onCancelGuideDismissHandler()

        assertEquals(AppScreen.INTRO, state.currentScreen)
        assertFalse(state.showCancelGuideDialog)
    }

    @Test
    fun cancelGuideDismiss_hidesDialog() {
        viewModel.onCancelGuideDismissHandler()
        assertFalse(state.showCancelGuideDialog)
    }

    @Test
    fun cancelGuideConfirm_returnsToStandby_hidesDialog() {
        viewModel.onCancelGuideConfirmHandler()
        assertEquals(AppScreen.STANDBY, state.currentScreen)
        assertFalse(state.showCancelGuideDialog)
    }

    // ── 다크 모드 ─────────────────────────────────────────────────────────────

    @Test
    fun initialState_darkModeDisabled() {
        assertFalse(state.isDarkMode)
    }

    @Test
    fun darkModeToggle_on_enablesDarkMode() {
        viewModel.onDarkModeToggleHandler(true)
        assertTrue(state.isDarkMode)
    }

    @Test
    fun darkModeToggle_off_disablesDarkMode() {
        viewModel.onDarkModeToggleHandler(true)
        viewModel.onDarkModeToggleHandler(false)
        assertFalse(state.isDarkMode)
    }

    @Test
    fun darkModeToggle_multipleToggles_reflectsLatestState() {
        viewModel.onDarkModeToggleHandler(true)
        viewModel.onDarkModeToggleHandler(false)
        viewModel.onDarkModeToggleHandler(true)
        assertTrue(state.isDarkMode)
    }

    // ── 비상 녹화 ─────────────────────────────────────────────────────────────

    @Test
    fun emergencyRecordingStarted_setsIsRecording() {
        viewModel.onEmergencyRecordingStartedHandler()
        assertTrue(state.isRecording)
    }

    @Test
    fun stopRecordingFabPressed_showsDialog() {
        viewModel.onEmergencyRecordingStartedHandler()
        viewModel.onStopRecordingFabPressedHandler()
        assertTrue(state.showStopRecordingDialog)
    }

    @Test
    fun stopRecordingDismiss_hidesDialog() {
        viewModel.onStopRecordingFabPressedHandler()
        viewModel.onStopRecordingDismissHandler()
        assertFalse(state.showStopRecordingDialog)
    }

    @Test
    fun stopRecordingConfirm_stopsRecording_hidesDialog() {
        viewModel.onEmergencyRecordingStartedHandler()
        viewModel.onStopRecordingFabPressedHandler()
        viewModel.onStopRecordingConfirmHandler()
        assertFalse(state.isRecording)
        assertFalse(state.showStopRecordingDialog)
    }
}
