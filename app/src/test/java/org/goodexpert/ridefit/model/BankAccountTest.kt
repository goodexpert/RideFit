package org.goodexpert.ridefit.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BankAccountTest {

    @Test
    fun isConfigured_falseWhenAllFieldsEmpty() {
        assertFalse(BankAccount().isConfigured)
    }

    @Test
    fun isConfigured_falseWhenBankNameMissing() {
        assertFalse(
            BankAccount(holderName = "홍길동", accountNumber = "3333-01-1234567").isConfigured,
        )
    }

    @Test
    fun isConfigured_falseWhenHolderNameMissing() {
        assertFalse(
            BankAccount(bankName = "카카오뱅크", accountNumber = "3333-01-1234567").isConfigured,
        )
    }

    @Test
    fun isConfigured_falseWhenAccountNumberMissing() {
        assertFalse(
            BankAccount(bankName = "카카오뱅크", holderName = "홍길동").isConfigured,
        )
    }

    @Test
    fun isConfigured_falseWhenAnyFieldIsBlank() {
        assertFalse(
            BankAccount(bankName = "  ", holderName = "홍길동", accountNumber = "3333-01-1234567").isConfigured,
        )
    }

    @Test
    fun isConfigured_trueWhenAllFieldsFilled() {
        assertTrue(
            BankAccount(
                bankName = "카카오뱅크",
                holderName = "홍길동",
                accountNumber = "3333-01-1234567",
            ).isConfigured,
        )
    }

    @Test
    fun spokenAccountNumber_readsEachDigitInKorean() {
        val account = BankAccount(accountNumber = "3333-0448-7729-1234")
        assertEquals("삼 삼 삼 삼, 공 사 사 팔, 칠 칠 이 구, 일 이 삼 사", account.spokenAccountNumber)
    }

    @Test
    fun spokenAccountNumber_groupsSeparatedByComma() {
        val account = BankAccount(accountNumber = "3333-01-1234567")
        assertEquals("삼 삼 삼 삼, 공 일, 일 이 삼 사 오 육 칠", account.spokenAccountNumber)
    }

    @Test
    fun spokenAccountNumber_noDashes_readsAllDigits() {
        val account = BankAccount(accountNumber = "333301234567")
        assertEquals("삼 삼 삼 삼 공 일 이 삼 사 오 육 칠", account.spokenAccountNumber)
    }

    @Test
    fun spokenAccountNumber_allDigitsCovered() {
        val account = BankAccount(accountNumber = "0123456789")
        assertEquals("공 일 이 삼 사 오 육 칠 팔 구", account.spokenAccountNumber)
    }

    @Test
    fun spokenAccountNumber_emptyWhenAccountNumberEmpty() {
        assertEquals("", BankAccount().spokenAccountNumber)
    }

    // ── digits ────────────────────────────────────────────────────────────────

    @Test
    fun digits_returnsKoreanNamePerDigit() {
        val account = BankAccount(accountNumber = "3333-0448-7729-1234")
        assertEquals(
            listOf("삼", "삼", "삼", "삼", "공", "사", "사", "팔", "칠", "칠", "이", "구", "일", "이", "삼", "사"),
            account.digits,
        )
    }

    @Test
    fun digits_allDigitValues() {
        val account = BankAccount(accountNumber = "0123456789")
        assertEquals(
            listOf("공", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"),
            account.digits,
        )
    }

    @Test
    fun digits_emptyWhenAccountNumberEmpty() {
        assertEquals(emptyList<String>(), BankAccount().digits)
    }
}
