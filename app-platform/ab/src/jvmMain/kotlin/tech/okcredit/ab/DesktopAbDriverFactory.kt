package tech.okcredit.ab

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.okcredit.ab.local.AbDatabase

class DesktopAbDriverFactory : AbDriverFactory {
    override fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AbDatabase.Schema.create(driver)
        return driver
    }
}
