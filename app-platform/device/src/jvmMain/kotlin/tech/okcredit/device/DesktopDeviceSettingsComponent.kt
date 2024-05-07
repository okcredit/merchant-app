package tech.okcredit.device

import me.tatarka.inject.annotations.Provides

interface DesktopDeviceSettingsComponent {

    @Provides
    fun settingsFactory(factory: DesktopDeviceSettingsFactory): DeviceSettingsFactory {
        return factory
    }
}
