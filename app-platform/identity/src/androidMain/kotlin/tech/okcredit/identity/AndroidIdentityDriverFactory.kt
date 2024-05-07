package tech.okcredit.identity

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.identity.local.IdentityDatabase

@Inject
@Singleton
class AndroidIdentityDriverFactory(private val context: Context) : IdentityDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(IdentityDatabase.Schema, context, "okcredit_identity.db")
    }
}
