package tech.okcredit.customization.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.local.CustomizationDatabase

@Inject
class JvmCustomizationDriverFactory : CustomizationDriverFactory {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
        ).also { db ->
            CustomizationDatabase.Schema.create(db)
        }
    }
}