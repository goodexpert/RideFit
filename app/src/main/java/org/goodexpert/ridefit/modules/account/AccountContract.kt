package org.goodexpert.ridefit.modules.account

import org.goodexpert.ridefit.model.BankAccount

interface AccountContract {

    sealed class Actions {
        data class UpdateBankName(val name: String) : Actions()
        data class UpdateHolderName(val name: String) : Actions()
        data class UpdateAccountNumber(val number: String) : Actions()
        object Save : Actions()
        object ResetDraft : Actions()
        object BankPickerOpened : Actions()
    }

    data class ViewState(
        val bankAccount: BankAccount = BankAccount(),
        val draft: BankAccount = BankAccount(),
    ) {
        val isDirty: Boolean get() = draft != bankAccount
    }
}
