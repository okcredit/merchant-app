package tech.okcredit.customization.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.JvmSqlDriverFactory
import tech.okcredit.customization.local.CustomizationDatabase

interface JvmCustomizationComponent : CustomizationComponent {

    @Provides
    fun provideCustomizationDriverFactory(): CustomizationDriverFactory {
        return JvmSqlDriverFactory {
            CustomizationDatabase.Schema.create(it)
        }
    }
}
