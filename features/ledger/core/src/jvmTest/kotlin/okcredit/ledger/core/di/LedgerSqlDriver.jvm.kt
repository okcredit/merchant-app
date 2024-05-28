package okcredit.ledger.core.di

import app.okcredit.ledger.core.di.JvmLedgerDriverFactory
import app.okcredit.ledger.core.local.LedgerSqlDriver

actual fun provideDriver(): LedgerSqlDriver {
    return JvmLedgerDriverFactory().createDriver()
}
