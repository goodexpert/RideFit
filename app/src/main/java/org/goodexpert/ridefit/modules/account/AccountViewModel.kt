package org.goodexpert.ridefit.modules.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.goodexpert.ridefit.analytics.RideFitAnalytics
import org.goodexpert.ridefit.modules.account.AccountContract.Actions
import org.goodexpert.ridefit.repository.AccountRepository

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AccountRepository(application)
    private val analytics = RideFitAnalytics(FirebaseAnalytics.getInstance(application))

    private val _viewState = MutableStateFlow(AccountContract.ViewState())
    val viewState: StateFlow<AccountContract.ViewState> = _viewState.asStateFlow()

    init {
        repository.bankAccount
            .onEach { account ->
                _viewState.update { it.copy(bankAccount = account, draft = account) }
            }
            .launchIn(viewModelScope)
    }

    // ── Event Handlers ────────────────────────────────────────────────────────

    fun onUpdateBankNameHandler(name: String) {
        dispatch(Actions.UpdateBankName(name))
    }

    fun onUpdateHolderNameHandler(name: String) {
        dispatch(Actions.UpdateHolderName(name))
    }

    fun onUpdateAccountNumberHandler(number: String) {
        dispatch(Actions.UpdateAccountNumber(number))
    }

    fun onBankPickerOpenedHandler() {
        analytics.logBankPickerOpened()
        dispatch(Actions.BankPickerOpened)
    }

    fun onBankSelectedHandler(name: String) {
        analytics.logBankSelected(name)
        dispatch(Actions.UpdateBankName(name))
    }

    fun onSaveHandler() {
        analytics.logSettingsSaved(_viewState.value.draft.isConfigured)
        viewModelScope.launch { repository.save(_viewState.value.draft) }
        dispatch(Actions.Save)
    }

    fun onResetDraftHandler() {
        dispatch(Actions.ResetDraft)
    }

    // ── Dispatch + Reduce ─────────────────────────────────────────────────────

    private fun dispatch(action: Actions) {
        _viewState.update { reduce(it, action) }
    }

    private fun reduce(state: AccountContract.ViewState, action: Actions): AccountContract.ViewState = when (action) {
        is Actions.UpdateBankName -> state.copy(draft = state.draft.copy(bankName = action.name))
        is Actions.UpdateHolderName -> state.copy(draft = state.draft.copy(holderName = action.name))
        is Actions.UpdateAccountNumber -> state.copy(draft = state.draft.copy(accountNumber = action.number))
        Actions.Save -> state
        Actions.ResetDraft -> state.copy(draft = state.bankAccount)
        Actions.BankPickerOpened -> state
    }
}
