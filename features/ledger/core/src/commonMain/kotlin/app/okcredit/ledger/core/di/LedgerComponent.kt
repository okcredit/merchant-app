package app.okcredit.ledger.core.di

import app.okcredit.ledger.core.local.LedgerSqlDriver
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory

typealias LedgerDriverFactory = SqlDriverFactory

interface LedgerComponent {

    @Provides
    fun sqlDriver(factory: LedgerDriverFactory): LedgerSqlDriver {
        return factory.createDriver()
    }
}
