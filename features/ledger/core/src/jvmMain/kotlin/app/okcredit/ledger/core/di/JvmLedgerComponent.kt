package app.okcredit.ledger.core.di

import app.okcredit.ledger.local.LedgerDatabase
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.JvmSqlDriverFactory

interface JvmLedgerComponent {

    @Provides
    fun provideLedgerDriverFactory(): LedgerDriverFactory {
        return JvmSqlDriverFactory {
            LedgerDatabase.Schema.create(it)
        }
    }
}
