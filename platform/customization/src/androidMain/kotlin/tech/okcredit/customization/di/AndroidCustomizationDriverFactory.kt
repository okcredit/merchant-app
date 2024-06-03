package tech.okcredit.customization.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.local.CustomizationDatabase

@Inject
class AndroidCustomizationDriverFactory(
    private val context: Context,
): CustomizationDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(CustomizationDatabase.Schema, context, "okcredit_customization.db")
    }
}