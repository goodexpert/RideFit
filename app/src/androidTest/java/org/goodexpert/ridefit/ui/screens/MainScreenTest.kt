package org.goodexpert.ridefit.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // ── Standby state (isRiding = false) ─────────────────────────────────────

    @Test
    fun standby_title_isDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen() }
        }
        rule.onNodeWithText("준비 완료").assertIsDisplayed()
    }

    @Test
    fun standby_statusBadge_showsStandbyText() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = false) }
        }
        rule.onNodeWithText("승객 탑승 전").assertIsDisplayed()
    }

    @Test
    fun standby_startGuideButton_isDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen() }
        }
        rule.onNodeWithText("탑승 안내 시작").assertIsDisplayed()
    }

    @Test
    fun standby_completeButton_isNotDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = false) }
        }
        rule.onNodeWithText("도착 안내").assertIsNotDisplayed()
    }

    @Test
    fun standby_allDriveModes_areDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen() }
        }
        rule.onNodeWithText("내비기준운행").assertIsDisplayed()
        rule.onNodeWithText("빠른 이동").assertIsDisplayed()
        rule.onNodeWithText("안전 운행").assertIsDisplayed()
        rule.onNodeWithText("미디어 청취").assertIsDisplayed()
    }

    @Test
    fun standby_clickingDriveMode_invokesOnModeChange() {
        var result: DriveMode? = null
        rule.setContent {
            RideFitTheme {
                MainScreen(onModeChange = { result = it })
            }
        }
        rule.onNodeWithText("빠른 이동").performClick()
        assertEquals(DriveMode.FAST, result)
    }

    @Test
    fun standby_clickingStartGuide_invokesCallback() {
        var called = false
        rule.setContent {
            RideFitTheme {
                MainScreen(onStartGuide = { called = true })
            }
        }
        rule.onNodeWithText("탑승 안내 시작").performClick()
        assertTrue(called)
    }

    @Test
    fun standby_noModeSelected_allModeCardsAreDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(selectedMode = null) }
        }
        rule.onNodeWithText("내비기준운행").assertIsDisplayed()
        rule.onNodeWithText("빠른 이동").assertIsDisplayed()
        rule.onNodeWithText("안전 운행").assertIsDisplayed()
        rule.onNodeWithText("미디어 청취").assertIsDisplayed()
    }

    // ── Riding state (isRiding = true) ────────────────────────────────────────

    @Test
    fun riding_title_isDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        // "운행 중" appears in both the title and StatusBadge — verify at least one is displayed
        rule.onAllNodesWithText("운행 중")[0].assertIsDisplayed()
    }

    @Test
    fun riding_statusBadge_showsRidingText() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        // "운행 중" appears in both the StatusBadge and title — verify the second occurrence too
        rule.onAllNodesWithText("운행 중")[1].assertIsDisplayed()
    }

    @Test
    fun riding_completeButton_isDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        rule.onNodeWithText("도착 안내").assertIsDisplayed()
    }

    @Test
    fun riding_startGuideButton_isNotDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        rule.onNodeWithText("탑승 안내 시작").assertIsNotDisplayed()
    }

    @Test
    fun riding_modeGrid_isDisplayed() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        // "내비기준운행" appears in both DriveModeHintCard (default) and the grid card
        rule.onAllNodesWithText("내비기준운행")[0].assertIsDisplayed()
        rule.onNodeWithText("빠른 이동").assertIsDisplayed()
    }

    @Test
    fun riding_clickingComplete_invokesCallback() {
        var called = false
        rule.setContent {
            RideFitTheme {
                MainScreen(isRiding = true, onComplete = { called = true })
            }
        }
        rule.onNodeWithText("도착 안내").performClick()
        assertTrue(called)
    }

    @Test
    fun riding_modeHintCard_showsQuietHint() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true, selectedMode = DriveMode.QUIET) }
        }
        rule.onNodeWithText(
            "내비게이션 기준으로 조용히 이동합니다.",
        ).assertIsDisplayed()
    }

    @Test
    fun riding_clickingDriveMode_invokesOnModeChange() {
        var result: DriveMode? = null
        rule.setContent {
            RideFitTheme {
                MainScreen(isRiding = true, onModeChange = { result = it })
            }
        }
        rule.onNodeWithText("안전 운행").performClick()
        assertEquals(DriveMode.SAFE, result)
    }

    // ── Account info button ───────────────────────────────────────────────────

    @Test
    fun accountInfoButton_isDisplayed_inStandby() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = false) }
        }
        rule.onNodeWithText("계좌 안내").assertIsDisplayed()
    }

    @Test
    fun accountInfoButton_isDisplayed_whileRiding() {
        rule.setContent {
            RideFitTheme { MainScreen(isRiding = true) }
        }
        rule.onNodeWithText("계좌 안내").assertIsDisplayed()
    }

    @Test
    fun clickingAccountInfoButton_invokesCallback() {
        var called = false
        rule.setContent {
            RideFitTheme {
                MainScreen(onAccountInfo = { called = true })
            }
        }
        rule.onNodeWithText("계좌 안내").performClick()
        assertTrue(called)
    }
}
