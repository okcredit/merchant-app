package app.okcredit.ledger.core.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.okcredit.ledger.local.LedgerDatabase
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton

@Inject
@Singleton
class AndroidLedgerDriverFactory(private val context: Context) : LedgerDriverFactory  {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(LedgerDatabase.Schema, context, "okcredit_ledger.db")
    }
}
