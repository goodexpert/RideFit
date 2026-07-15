package org.goodexpert.ridefit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
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
class SecondaryButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val container = Color(0xFFDCFCE7)
    private val content = Color(0xFF15803D)

    @Test
    fun click_invokesOnClick() {
        var clicked = false
        composeRule.setContent {
            RideFitTheme {
                SecondaryButton(
                    title = "이체 완료 확인",
                    onClick = { clicked = true },
                    containerColor = container,
                    contentColor = content,
                )
            }
        }
        composeRule.onNodeWithText("이체 완료 확인").performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabled_isNotEnabledAndDoesNotClick() {
        var clicked = false
        composeRule.setContent {
            RideFitTheme {
                SecondaryButton(
                    title = "이체 완료 확인",
                    onClick = { clicked = true },
                    enabled = false,
                    containerColor = container,
                    contentColor = content,
                )
            }
        }
        composeRule.onNodeWithText("이체 완료 확인").assertIsNotEnabled()
        composeRule.onNodeWithText("이체 완료 확인").performClick()
        assertFalse(clicked)
    }

    @Test
    fun contentDescription_isExposedForAccessibility() {
        composeRule.setContent {
            RideFitTheme {
                SecondaryButton(
                    title = "확인",
                    contentDescription = "이체 완료 확인",
                    onClick = {},
                    containerColor = container,
                    contentColor = content,
                )
            }
        }
        composeRule.onNodeWithContentDescription("이체 완료 확인").assertExists()
    }

    @Test
    fun subtitleAndBorder_variantRendersSubtitle() {
        composeRule.setContent {
            RideFitTheme {
                SecondaryButton(
                    title = "건너뛰기",
                    subtitle = "안내를 건너뛰고 바로 이동합니다",
                    onClick = {},
                    containerColor = container,
                    contentColor = content,
                    border = BorderStroke(1.dp, content),
                )
            }
        }
        composeRule.onNodeWithText("안내를 건너뛰고 바로 이동합니다").assertExists()
    }
}
