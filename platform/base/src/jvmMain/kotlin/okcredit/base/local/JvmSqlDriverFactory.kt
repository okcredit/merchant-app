package okcredit.base.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class JvmSqlDriverFactory(
    private val onDriverCreated: (SqlDriver) -> Unit,
) : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
        ).also {
            onDriverCreated(it)
        }
    }
}
