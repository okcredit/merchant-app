package tech.okcredit.auth

import me.tatarka.inject.annotations.Provides
import tech.okcredit.auth.di.AuthComponent

interface IosAuthComponent : AuthComponent {

    @Provides
    fun settingsFactory(iosSettingsFactory: IosSettingsFactory): SettingsFactory {
        return iosSettingsFactory
    }
}
