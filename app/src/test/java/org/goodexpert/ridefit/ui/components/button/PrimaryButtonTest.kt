package org.goodexpert.ridefit.ui.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PrimaryButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun click_invokesOnClick() {
        var clicked = false
        composeRule.setContent {
            RideFitTheme { PrimaryButton(title = "시작", onClick = { clicked = true }) }
        }
        composeRule.onNodeWithText("시작").performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabled_isNotEnabledAndDoesNotClick() {
        var clicked = false
        composeRule.setContent {
            RideFitTheme {
                PrimaryButton(title = "시작", enabled = false, onClick = { clicked = true })
            }
        }
        composeRule.onNodeWithText("시작").assertIsNotEnabled()
        composeRule.onNodeWithText("시작").performClick()
        assertFalse(clicked)
    }

    @Test
    fun contentDescription_isExposedForAccessibility() {
        composeRule.setContent {
            RideFitTheme {
                PrimaryButton(title = "시작", contentDescription = "탑승 안내 시작", onClick = {})
            }
        }
        composeRule.onNodeWithContentDescription("탑승 안내 시작").assertExists()
    }

    @Test
    fun subtitle_isRendered() {
        composeRule.setContent {
            RideFitTheme { PrimaryButton(title = "시작", subtitle = "버튼을 누르세요", onClick = {}) }
        }
        composeRule.onNodeWithText("버튼을 누르세요").assertExists()
    }

    @Test
    fun leadingIcon_slotIsRendered() {
        composeRule.setContent {
            RideFitTheme {
                PrimaryButton(
                    title = "시작",
                    onClick = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "재생",
                        )
                    },
                )
            }
        }
        composeRule.onNodeWithContentDescription("재생").assertExists()
    }
}
