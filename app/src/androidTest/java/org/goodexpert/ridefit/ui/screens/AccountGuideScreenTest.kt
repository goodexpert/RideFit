package org.goodexpert.ridefit.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountGuideScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val account = BankAccount(
        bankName = "카카오뱅크",
        holderName = "홍길동",
        accountNumber = "3333-0448-7729-1234",
    )

    @Test
    fun withAccount_bankInfo_isDisplayed() {
        rule.setContent {
            RideFitTheme { AccountGuideScreen(bankAccount = account) }
        }
        rule.onNodeWithText("카카오뱅크").assertIsDisplayed()
        rule.onNodeWithText("홍길동").assertIsDisplayed()
    }

    @Test
    fun withAccount_actionButtons_areDisplayed() {
        rule.setContent {
            RideFitTheme { AccountGuideScreen(bankAccount = account) }
        }
        rule.onNodeWithText("다시 읽어주기").assertIsDisplayed()
        rule.onNodeWithText("이체 완료 확인").assertIsDisplayed()
    }

    @Test
    fun clickingRepeat_invokesOnRepeat() {
        var called = false
        rule.setContent {
            RideFitTheme {
                AccountGuideScreen(bankAccount = account, onRepeat = { called = true })
            }
        }
        rule.onNodeWithText("다시 읽어주기").performClick()
        assertTrue(called)
    }

    @Test
    fun clickingConfirm_invokesOnConfirm() {
        var called = false
        rule.setContent {
            RideFitTheme {
                AccountGuideScreen(bankAccount = account, onConfirm = { called = true })
            }
        }
        rule.onNodeWithText("이체 완료 확인").performClick()
        assertTrue(called)
    }

    @Test
    fun withoutAccount_noAccountCard_isDisplayed() {
        rule.setContent {
            RideFitTheme { AccountGuideScreen(bankAccount = BankAccount()) }
        }
        rule.onNodeWithText("계좌 정보 미설정").assertIsDisplayed()
        rule.onNodeWithText("계좌 설정하기").assertIsDisplayed()
    }

    @Test
    fun withoutAccount_clickSetup_invokesOnSetupAccount() {
        var called = false
        rule.setContent {
            RideFitTheme {
                AccountGuideScreen(bankAccount = BankAccount(), onSetupAccount = { called = true })
            }
        }
        rule.onNodeWithText("계좌 설정하기").performClick()
        assertTrue(called)
    }
}
