package tech.okcredit.ab

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.ab.local.AbDatabase

@Inject
@Singleton
class AndroidAbDriverFactory(private val context: Context) : AbDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AbDatabase.Schema, context, "okcredit_ab.db")
    }
}
