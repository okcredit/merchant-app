package tech.okcredit.okdoc

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.okdoc.local.OkDocDatabase

@Inject
@Singleton
class AndroidOkDocDriverFactory(private val context: Context) : OkDocDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(OkDocDatabase.Schema, context, "okcredit_okdoc.db")
    }
}
