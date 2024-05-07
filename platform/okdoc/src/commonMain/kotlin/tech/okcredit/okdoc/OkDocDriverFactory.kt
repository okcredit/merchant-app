package tech.okcredit.okdoc

import app.cash.sqldelight.db.SqlDriver

interface OkDocDriverFactory {
    fun createDriver(): SqlDriver
}
