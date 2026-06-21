package org.goodexpert.ridefit.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountSettingsScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun fieldLabels_areDisplayed() {
        rule.setContent {
            RideFitTheme { AccountSettingsScreen() }
        }
        rule.onNodeWithText("은행명").assertIsDisplayed()
        rule.onNodeWithText("예금주명").assertIsDisplayed()
        rule.onNodeWithText("계좌번호").assertIsDisplayed()
    }

    @Test
    fun saveButton_isDisplayed() {
        rule.setContent {
            RideFitTheme { AccountSettingsScreen() }
        }
        rule.onNodeWithText("계좌 정보 저장").assertIsDisplayed()
    }

    @Test
    fun clickingSave_invokesOnSave() {
        var saved = false
        rule.setContent {
            RideFitTheme {
                AccountSettingsScreen(onSave = { saved = true })
            }
        }
        rule.onNodeWithText("계좌 정보 저장").performClick()
        assertTrue(saved)
    }

    @Test
    fun clickingBack_invokesOnBack() {
        var backed = false
        rule.setContent {
            RideFitTheme {
                AccountSettingsScreen(onBack = { backed = true })
            }
        }
        rule.onNodeWithContentDescription("뒤로").performClick()
        assertTrue(backed)
    }

    @Test
    fun clickingBankField_opensBankPicker() {
        rule.setContent {
            RideFitTheme { AccountSettingsScreen() }
        }
        rule.onNodeWithText("은행명").assertIsDisplayed()
        // Click the field with the placeholder to open picker
        rule.onNodeWithText("은행을 선택하세요").performClick()
        // Wait for bottom sheet and check title
        rule.onNodeWithText("은행선택").assertIsDisplayed()
    }
}
