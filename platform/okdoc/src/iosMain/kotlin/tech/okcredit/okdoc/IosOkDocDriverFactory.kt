package tech.okcredit.okdoc

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.okdoc.di.OkDocDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase

@Inject
class IosOkDocDriverFactory : OkDocDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(OkDocDatabase.Schema, "okcredit_okdoc.db")
    }
}
