package tech.okcredit.customization.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.local.CustomizationDatabase

@Inject
class IosCustomizationDriverFactory : CustomizationDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CustomizationDatabase.Schema, "okcredit_customization.db")
    }
}