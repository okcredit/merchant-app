package tech.okcredit.customization.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.customization.local.CustomizationSqlDriver

typealias CustomizationDriverFactory = SqlDriverFactory

interface CustomizationComponent {

    @Provides
    fun customizationSqlDriver(factory: CustomizationDriverFactory): CustomizationSqlDriver {
        return factory.createDriver()
    }
}
