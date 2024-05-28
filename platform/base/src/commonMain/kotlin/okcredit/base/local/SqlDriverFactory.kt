package okcredit.base.local

import app.cash.sqldelight.db.SqlDriver

interface SqlDriverFactory {
    fun createDriver(): SqlDriver
}