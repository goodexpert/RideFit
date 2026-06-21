package org.goodexpert.ridefit.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.goodexpert.ridefit.model.BankAccount

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class AccountRepository(private val context: Context) {

    private val keyBankName = stringPreferencesKey("bank_name")
    private val keyHolderName = stringPreferencesKey("holder_name")
    private val keyAccountNumber = stringPreferencesKey("account_number")

    val bankAccount: Flow<BankAccount> = context.dataStore.data.map { prefs ->
        BankAccount(
            bankName = prefs[keyBankName].orEmpty(),
            holderName = prefs[keyHolderName].orEmpty(),
            accountNumber = prefs[keyAccountNumber].orEmpty(),
        )
    }

    suspend fun save(account: BankAccount) {
        context.dataStore.edit { prefs ->
            prefs[keyBankName] = account.bankName
            prefs[keyHolderName] = account.holderName
            prefs[keyAccountNumber] = account.accountNumber
        }
    }
}
