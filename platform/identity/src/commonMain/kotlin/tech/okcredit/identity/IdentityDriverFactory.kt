package tech.okcredit.identity

import app.cash.sqldelight.db.SqlDriver

interface IdentityDriverFactory {
    fun createDriver(): SqlDriver
}
