package tech.okcredit.identity

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class DesktopIdentityDriverFactory : IdentityDriverFactory {
    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    }
}
