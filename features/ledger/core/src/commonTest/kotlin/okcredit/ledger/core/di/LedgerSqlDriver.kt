package okcredit.ledger.core.di

import app.okcredit.ledger.core.local.LedgerSqlDriver

expect fun provideDriver(): LedgerSqlDriver
