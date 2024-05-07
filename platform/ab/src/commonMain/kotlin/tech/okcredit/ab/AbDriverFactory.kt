package tech.okcredit.ab

import app.cash.sqldelight.db.SqlDriver

interface AbDriverFactory {
    fun createDriver(): SqlDriver
}
