package org.goodexpert.ridefit.viewmodel

import org.goodexpert.ridefit.modules.account.AccountViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Suppress("TooManyFunctions")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AccountViewModelTest {

    private lateinit var viewModel: AccountViewModel

    @Before
    fun setUp() {
        viewModel = AccountViewModel(RuntimeEnvironment.getApplication())
    }

    private val state get() = viewModel.viewState.value

    // ── onUpdateBankNameHandler ───────────────────────────────────────────────

    @Test
    fun updateBankName_reflectedInDraft() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        assertEquals("카카오뱅크", state.draft.bankName)
    }

    @Test
    fun updateBankName_doesNotChangeOtherFields() {
        viewModel.onUpdateHolderNameHandler("홍길동")
        viewModel.onUpdateAccountNumberHandler("3333-01-1234567")

        viewModel.onUpdateBankNameHandler("신한은행")

        assertEquals("홍길동", state.draft.holderName)
        assertEquals("3333-01-1234567", state.draft.accountNumber)
    }

    // ── onUpdateHolderNameHandler ─────────────────────────────────────────────

    @Test
    fun updateHolderName_reflectedInDraft() {
        viewModel.onUpdateHolderNameHandler("홍길동")
        assertEquals("홍길동", state.draft.holderName)
    }

    @Test
    fun updateHolderName_doesNotChangeOtherFields() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onUpdateAccountNumberHandler("3333-01-1234567")

        viewModel.onUpdateHolderNameHandler("김철수")

        assertEquals("카카오뱅크", state.draft.bankName)
        assertEquals("3333-01-1234567", state.draft.accountNumber)
    }

    // ── onUpdateAccountNumberHandler ──────────────────────────────────────────

    @Test
    fun updateAccountNumber_reflectedInDraft() {
        viewModel.onUpdateAccountNumberHandler("3333-01-1234567")
        assertEquals("3333-01-1234567", state.draft.accountNumber)
    }

    @Test
    fun updateAccountNumber_doesNotChangeOtherFields() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onUpdateHolderNameHandler("홍길동")

        viewModel.onUpdateAccountNumberHandler("110-123-456789")

        assertEquals("카카오뱅크", state.draft.bankName)
        assertEquals("홍길동", state.draft.holderName)
    }

    // ── draft completeness ────────────────────────────────────────────────────

    @Test
    fun draft_initiallyNotConfigured() {
        assertFalse(state.draft.isConfigured)
    }

    @Test
    fun draft_configuredAfterAllFieldsSet() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onUpdateHolderNameHandler("홍길동")
        viewModel.onUpdateAccountNumberHandler("3333-01-1234567")

        assertTrue(state.draft.isConfigured)
    }

    @Test
    fun draft_notConfiguredWhenOnlyBankNameSet() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        assertFalse(state.draft.isConfigured)
    }

    @Test
    fun draft_lastUpdateWins() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onUpdateBankNameHandler("신한은행")
        assertEquals("신한은행", state.draft.bankName)
    }

    // ── onResetDraftHandler ───────────────────────────────────────────────────

    @Test
    fun resetDraft_discardsPendingChanges() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onUpdateHolderNameHandler("홍길동")
        viewModel.onUpdateAccountNumberHandler("3333-01-1234567")

        viewModel.onResetDraftHandler()

        assertEquals("", state.draft.bankName)
        assertEquals("", state.draft.holderName)
        assertEquals("", state.draft.accountNumber)
    }

    @Test
    fun resetDraft_onFreshViewModel_draftRemainsEmpty() {
        viewModel.onResetDraftHandler()
        assertFalse(state.draft.isConfigured)
    }

    // ── isDirty ───────────────────────────────────────────────────────────────

    @Test
    fun isDirty_initiallyFalse() {
        assertFalse(state.isDirty)
    }

    @Test
    fun isDirty_trueAfterUpdate() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        assertTrue(state.isDirty)
    }

    @Test
    fun isDirty_falseAfterReset() {
        viewModel.onUpdateBankNameHandler("카카오뱅크")
        viewModel.onResetDraftHandler()
        assertFalse(state.isDirty)
    }

    // ── onBankSelectedHandler ─────────────────────────────────────────────────

    @Test
    fun bankSelected_updatesDraftBankName() {
        viewModel.onBankSelectedHandler("하나은행")
        assertEquals("하나은행", state.draft.bankName)
    }

    @Test
    fun bankSelected_doesNotChangeOtherFields() {
        viewModel.onUpdateHolderNameHandler("홍길동")
        viewModel.onUpdateAccountNumberHandler("110-123-456789")

        viewModel.onBankSelectedHandler("우리은행")

        assertEquals("홍길동", state.draft.holderName)
        assertEquals("110-123-456789", state.draft.accountNumber)
    }
}
