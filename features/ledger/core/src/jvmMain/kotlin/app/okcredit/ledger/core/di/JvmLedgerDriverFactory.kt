package app.okcredit.ledger.core.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.okcredit.ledger.local.LedgerDatabase
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton

@Inject
@Singleton
class JvmLedgerDriverFactory : LedgerDriverFactory  {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
        ).also { db ->
            LedgerDatabase.Schema.create(db)
        }
    }
}