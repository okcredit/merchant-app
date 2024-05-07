package tech.okcredit.auth

import me.tatarka.inject.annotations.Provides

interface DesktopAuthComponent {

    @Provides
    fun settingsFactory(factory: DesktopAuthSettingsFactory): SettingsFactory {
        return factory
    }
}
