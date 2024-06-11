package app.okcredit.ledger.core.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.okdoc.local.OkDocDatabase

@Inject
class IosLedgerDriverFactory : LedgerDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(OkDocDatabase.Schema, "okcredit/okdoc.db")
    }
}
