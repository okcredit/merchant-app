package tech.okcredit.customization.di

import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.serialization.modules.SerializersModule
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.customization.local.CustomizationSqlDriver
import tech.okcredit.customization.models.Action
import tech.okcredit.customization.models.ActionSerializer
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

    @Provides
    @IntoSet
    fun actionSerializer(): SerializersModule {
        return SerializersModule { polymorphicDefaultDeserializer(Action::class) { ActionSerializer } }
    }
}