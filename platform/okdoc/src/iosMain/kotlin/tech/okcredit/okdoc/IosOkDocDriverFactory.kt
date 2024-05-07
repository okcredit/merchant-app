package tech.okcredit.okdoc

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.okdoc.local.OkDocDatabase

@Inject
@Singleton
class IosOkDocDriverFactory : OkDocDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(OkDocDatabase.Schema, "okcredit/okdoc.db")
    }
}
