package org.goodexpert.ridefit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.goodexpert.ridefit.modules.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@Suppress("TooManyFunctions")
@RunWith(AndroidJUnit4::class)
class RideFitE2ETest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Before
    fun clearSavedAccount() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        File(ctx.filesDir, "datastore/account.preferences_pb").delete()
    }

    // ── Navigation helpers ────────────────────────────────────────────────────

    private fun selectMode(modeLabel: String = "내비기준운행") =
        rule.onNodeWithText(modeLabel).performClick()

    private fun skipVoiceGuide() =
        rule.onNodeWithText("건너뛰기").performClick()

    private fun goToRide(modeLabel: String = "내비기준운행") {
        selectMode(modeLabel)
        skipVoiceGuide()
    }

    private fun goToCompleted(modeLabel: String = "내비기준운행") {
        goToRide(modeLabel)
        rule.onNodeWithText("도착 안내").performClick()
    }

    private fun goToSettings() {
        // Tap the gear icon to open the settings sheet, then tap the account settings item
        rule.onNodeWithContentDescription("계좌 설정", useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.onNodeWithText("계좌 설정").performClick()
    }

    private fun openBankPicker() =
        rule.onNodeWithText("은행을 선택하세요").performClick()

    // ── Mode card → VoiceGuide → Ride flow ───────────────────────────────────

    @Test
    fun scenario_selectMode_showsVoiceGuideScreen() {
        selectMode("내비기준운행")

        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
        rule.onNodeWithText("탑승 안내 중...").assertIsDisplayed()
    }

    @Test
    fun scenario_selectFastMode_voiceGuideShowsFastLabel() {
        selectMode("빠른 이동")

        rule.onNodeWithText("빠른 이동").assertIsDisplayed()
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
    }

    @Test
    fun scenario_selectSafeMode_voiceGuideShowsSafeLabel() {
        selectMode("안전 운행")

        rule.onNodeWithText("안전 운행").assertIsDisplayed()
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
    }

    @Test
    fun scenario_voiceGuide_skip_navigatesToRideScreen() {
        goToRide()

        rule.onNodeWithText("도착 안내").assertIsDisplayed()
    }

    @Test
    fun scenario_rideComplete_navigatesToCompletedScreen() {
        goToCompleted()

        rule.onNodeWithText("도착 안내").assertIsDisplayed()
        rule.onNodeWithText("목적지 도착안내").assertIsDisplayed()
    }

    @Test
    fun scenario_newRide_returnsToStandbyScreen() {
        goToCompleted()
        rule.onNodeWithText("홈으로 가기").performClick()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
        rule.onNodeWithText("탑승 안내 시작").assertIsDisplayed()
    }

    @Test
    fun scenario_fullRideCycle_standby_ride_completed_standby() {
        rule.onNodeWithText("준비 완료").assertIsDisplayed()

        goToCompleted()
        rule.onNodeWithText("목적지 도착안내").assertIsDisplayed()

        rule.onNodeWithText("홈으로 가기").performClick()
        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    // ── 탑승 안내 시작 flow (intro → riding) ─────────────────────────────────

    @Test
    fun scenario_startGuide_showsVoiceGuideScreen() {
        rule.onNodeWithText("탑승 안내 시작").performClick()

        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
        rule.onNodeWithText("탑승 안내 중...").assertIsDisplayed()
    }

    @Test
    fun scenario_startGuide_skip_navigatesToRidingScreen() {
        rule.onNodeWithText("탑승 안내 시작").performClick()
        skipVoiceGuide()

        rule.onNodeWithText("도착 안내").assertIsDisplayed()
    }

    // ── Back button flow ──────────────────────────────────────────────────────

    @Test
    fun scenario_backOnStandby_showsExitDialog() {
        pressBack()

        rule.onNodeWithText("앱 종료").assertIsDisplayed()
        rule.onNodeWithText("RideFit을 종료하시겠습니까?").assertIsDisplayed()
    }

    @Test
    fun scenario_exitDialog_cancel_dismissesDialog() {
        pressBack()
        rule.onNodeWithText("취소").performClick()

        rule.onNodeWithText("앱 종료").assertIsNotDisplayed()
        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    @Test
    fun scenario_backOnRide_returnsToStandby() {
        goToRide()
        rule.onNodeWithText("도착 안내").assertIsDisplayed()

        pressBack()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    // ── Cancel guide dialog (back during VoiceGuide) ──────────────────────────

    @Test
    fun scenario_backOnVoiceGuide_whilePlaying_showsCancelDialog() {
        selectMode("내비기준운행")
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()

        pressBack()

        rule.onNodeWithText("안내 재생 중").assertIsDisplayed()
        rule.onNodeWithText("음성 안내를 중단하시겠습니까?").assertIsDisplayed()
    }

    @Test
    fun scenario_cancelDialog_confirm_stopsAndReturnsToMain() {
        selectMode("내비기준운행")
        pressBack()
        rule.onNodeWithText("중단").performClick()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
        rule.onNodeWithText("안내 재생 중").assertIsNotDisplayed()
    }

    @Test
    fun scenario_cancelDialog_continue_dismissesAndStaysOnVoiceGuide() {
        selectMode("내비기준운행")
        pressBack()
        rule.onNodeWithText("계속").performClick()

        rule.onNodeWithText("안내 재생 중").assertIsNotDisplayed()
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
    }

    @Test
    fun scenario_backOnStartGuideVoiceGuide_whilePlaying_showsCancelDialog() {
        rule.onNodeWithText("탑승 안내 시작").performClick()
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()

        pressBack()

        rule.onNodeWithText("안내 재생 중").assertIsDisplayed()
    }

    // ── 운행 취소 button on VoiceGuide ────────────────────────────────────────

    @Test
    fun scenario_cancelButton_onVoiceGuide_showsCancelDialog() {
        selectMode("내비기준운행")
        rule.onNodeWithText("운행 취소").assertIsDisplayed()

        rule.onNodeWithText("운행 취소").performClick()

        rule.onNodeWithText("안내 재생 중").assertIsDisplayed()
        rule.onNodeWithText("음성 안내를 중단하시겠습니까?").assertIsDisplayed()
    }

    @Test
    fun scenario_cancelButton_confirm_returnsToStandby_notRiding() {
        selectMode("내비기준운행")
        rule.onNodeWithText("운행 취소").performClick()
        rule.onNodeWithText("중단").performClick()

        // Back to standby in the pre-ride state, not the riding state
        rule.onNodeWithText("준비 완료").assertIsDisplayed()
        rule.onNodeWithText("안내 재생 중").assertIsNotDisplayed()
    }

    @Test
    fun scenario_cancelButton_continue_staysOnVoiceGuide() {
        selectMode("내비기준운행")
        rule.onNodeWithText("운행 취소").performClick()
        rule.onNodeWithText("계속").performClick()

        rule.onNodeWithText("안내 재생 중").assertIsNotDisplayed()
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
    }

    // ── Account Settings flow ─────────────────────────────────────────────────

    @Test
    fun scenario_openSettings_showsAccountSettingsScreen() {
        goToSettings()

        rule.onNodeWithText("계좌 정보 관리").assertIsDisplayed()
    }

    @Test
    fun scenario_settings_showsInputFields() {
        goToSettings()

        rule.onNodeWithText("은행을 선택하세요").assertIsDisplayed()
        rule.onNodeWithText("예금주명을 입력하세요").assertIsDisplayed()
        rule.onNodeWithText("계좌번호를 입력하세요").assertIsDisplayed()
    }

    @Test
    fun scenario_backFromSettings_returnsToStandby() {
        goToSettings()
        pressBack()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    @Test
    fun scenario_saveSettings_returnsToStandby() {
        goToSettings()
        rule.onNodeWithText("계좌 정보 저장").performClick()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    // ── Bank Picker flow ──────────────────────────────────────────────────────

    @Test
    fun scenario_bankField_tap_opensBankPicker() {
        goToSettings()
        openBankPicker()

        rule.onNodeWithText("은행선택").assertIsDisplayed()
        rule.onNodeWithText("카카오뱅크").assertIsDisplayed()
    }

    @Test
    fun scenario_bankPicker_selectBank_updatesFieldAndClosesPicker() {
        goToSettings()
        openBankPicker()
        rule.onNodeWithText("카카오뱅크").performClick()

        rule.onNodeWithText("은행선택").assertIsNotDisplayed()
        rule.onNodeWithText("카카오뱅크").assertIsDisplayed()
    }

    @Test
    fun scenario_bankPicker_cancel_dismissesWithoutChange() {
        goToSettings()
        openBankPicker()
        rule.onNodeWithText("취소").performClick()

        rule.onNodeWithText("은행선택").assertIsNotDisplayed()
        rule.onNodeWithText("계좌 정보 관리").assertIsDisplayed()
    }

    @Test
    fun scenario_bankPicker_search_filtersResults() {
        goToSettings()
        openBankPicker()
        rule.onNodeWithText("은행검색").performTextInput("카카오")

        rule.onNodeWithText("카카오뱅크").assertIsDisplayed()
        rule.onNodeWithText("하나은행").assertIsNotDisplayed()
    }

    @Test
    fun scenario_selectBankThenSave_returnsToStandby() {
        goToSettings()
        openBankPicker()
        rule.onNodeWithText("카카오뱅크").performClick()
        rule.onNodeWithText("계좌 정보 저장").performClick()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    // ── Account info button & AccountGuideScreen flow ─────────────────────────

    @Test
    fun scenario_accountInfoButton_isDisplayed() {
        rule.onNodeWithText("계좌 안내").assertIsDisplayed()
    }

    @Test
    fun scenario_accountInfoButton_navigatesToAccountGuideScreen() {
        rule.onNodeWithText("계좌 안내").performClick()

        rule.onNodeWithText("계좌 정보 안내").assertIsDisplayed()
    }

    @Test
    fun scenario_accountGuideScreen_withoutAccount_showsNoAccountCard() {
        rule.onNodeWithText("계좌 안내").performClick()

        rule.onNodeWithText("계좌 정보 미설정").assertIsDisplayed()
        rule.onNodeWithText("계좌 설정하기").assertIsDisplayed()
    }

    @Test
    fun scenario_accountGuideScreen_setupAccount_navigatesToSettings() {
        rule.onNodeWithText("계좌 안내").performClick()
        rule.onNodeWithText("계좌 설정하기").performClick()

        rule.onNodeWithText("계좌 정보 관리").assertIsDisplayed()
    }

    @Test
    fun scenario_accountGuideScreen_backButton_returnsToMain() {
        rule.onNodeWithText("계좌 안내").performClick()
        pressBack()

        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }
}
