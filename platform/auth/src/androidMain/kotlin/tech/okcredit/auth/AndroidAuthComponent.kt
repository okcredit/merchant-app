package tech.okcredit.auth

import me.tatarka.inject.annotations.Provides
import tech.okcredit.auth.di.AuthComponent

interface AndroidAuthComponent : AuthComponent {

    @Provides
    fun settingsFactory(factory: AndroidSettingsFactory): SettingsFactory {
        return factory
    }
}
