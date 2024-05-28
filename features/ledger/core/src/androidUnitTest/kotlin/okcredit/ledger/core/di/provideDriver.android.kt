package okcredit.ledger.core.di

import app.cash.sqldelight.db.SqlDriver

actual fun provideDriver(): SqlDriver {
    throw UnsupportedOperationException("Not supported in Android")
}
