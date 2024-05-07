package tech.okcredit.identity

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.local.IdentityDatabase

@Inject
class IosIdentityDriverFactory : IdentityDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(IdentityDatabase.Schema, "okcredit_identity.db")
    }
}
