package tech.okcredit.okdoc

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton

@Inject
@Singleton
class DesktopOkDocDriverFactory : OkDocDriverFactory {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    }
}
