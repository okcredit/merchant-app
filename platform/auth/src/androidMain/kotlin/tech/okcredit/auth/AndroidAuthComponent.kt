package tech.okcredit.auth

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.auth.di.AuthComponent

@ContributesTo(AppScope::class)
interface AndroidAuthComponent : AuthComponent {

    @Provides
    fun settingsFactory(factory: AndroidSettingsFactory): SettingsFactory {
        return factory
    }
}
