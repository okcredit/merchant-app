package tech.okcredit.ab

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.ab.local.AbDatabase

@Inject
class IosAbAbDriverFactory : AbDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AbDatabase.Schema, "okcredit_ab.db")
    }
}
