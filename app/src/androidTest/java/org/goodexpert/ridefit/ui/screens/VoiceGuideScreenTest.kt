package org.goodexpert.ridefit.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoiceGuideScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun modeLabel_isDisplayedInBadge() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen(selectedMode = DriveMode.FAST) }
        }
        rule.onNodeWithText("빠른 이동").assertIsDisplayed()
    }

    @Test
    fun playingLabel_isDisplayed() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen() }
        }
        rule.onNodeWithText("탑승 안내 중...").assertIsDisplayed()
    }

    @Test
    fun voiceScriptLabel_isDisplayed() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen() }
        }
        rule.onNodeWithText("탑승 안내 중").assertIsDisplayed()
    }

    @Test
    fun skipButton_isDisplayed() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen() }
        }
        rule.onNodeWithText("건너뛰기").assertIsDisplayed()
    }

    @Test
    fun skipHint_isDisplayed() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen() }
        }
        rule.onNodeWithText("안내를 건너뛰고 바로 이동합니다").assertIsDisplayed()
    }

    @Test
    fun clickingSkip_invokesCallback() {
        var called = false
        rule.setContent {
            RideFitTheme {
                VoiceGuideScreen(onSkip = { called = true })
            }
        }
        rule.onNodeWithText("건너뛰기").performClick()
        assertTrue(called)
    }

    @Test
    fun differentModes_showCorrectLabel() {
        rule.setContent {
            RideFitTheme { VoiceGuideScreen(selectedMode = DriveMode.SAFE) }
        }
        rule.onNodeWithText("안전 운행").assertIsDisplayed()
    }
}
