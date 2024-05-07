package tech.okcredit.device

import me.tatarka.inject.annotations.Provides
import tech.okcredit.device.di.DeviceComponent

interface IosDeviceComponent : DeviceComponent {

    @Provides
    fun settingsFactory(iosSettingsFactory: IosDeviceSettingsFactory): DeviceSettingsFactory {
        return iosSettingsFactory
    }
}
