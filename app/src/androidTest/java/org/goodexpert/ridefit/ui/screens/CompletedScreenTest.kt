package org.goodexpert.ridefit.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompletedScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun title_isDisplayed() {
        rule.setContent {
            RideFitTheme { CompletedScreen() }
        }
        rule.onNodeWithText("도착 안내").assertIsDisplayed()
    }

    @Test
    fun voiceScript_isDisplayed() {
        rule.setContent {
            RideFitTheme { CompletedScreen() }
        }
        rule.onNodeWithText("목적지 도착안내").assertIsDisplayed()
    }

    @Test
    fun homeButton_isDisplayed() {
        rule.setContent {
            RideFitTheme { CompletedScreen() }
        }
        rule.onNodeWithText("홈으로 가기").assertIsDisplayed()
    }

    @Test
    fun clickingHome_invokesCallback() {
        var called = false
        rule.setContent {
            RideFitTheme {
                CompletedScreen(onNewRide = { called = true })
            }
        }
        rule.onNodeWithText("홈으로 가기").performClick()
        assertTrue(called)
    }
}
