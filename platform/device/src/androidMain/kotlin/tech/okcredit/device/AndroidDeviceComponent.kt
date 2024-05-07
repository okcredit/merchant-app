package tech.okcredit.device

import me.tatarka.inject.annotations.Provides
import tech.okcredit.device.di.DeviceComponent

interface AndroidDeviceComponent : DeviceComponent {

    @Provides
    fun settingsFactory(factory: AndroidDeviceSettingsFactory): DeviceSettingsFactory {
        return factory
    }
}
