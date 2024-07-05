package tech.okcredit.customization.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.customization.local.CustomizationSqlDriver
import tech.okcredit.customization.remote.CustomizationApiClient

typealias CustomizationDriverFactory = SqlDriverFactory

interface CustomizationComponent {

    @Provides
    fun customizationSqlDriver(factory: CustomizationDriverFactory): CustomizationSqlDriver {
        return factory.createDriver()
    }

    @Provides
    fun customizationApiClient(ktorfit: Ktorfit): CustomizationApiClient {
        return ktorfit.create()
    }
}
