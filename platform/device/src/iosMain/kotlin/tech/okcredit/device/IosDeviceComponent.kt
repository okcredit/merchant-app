package tech.okcredit.device

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.device.di.DeviceComponent

@ContributesTo(AppScope::class)
interface IosDeviceComponent : DeviceComponent {

    @Provides
    fun settingsFactory(iosSettingsFactory: IosDeviceSettingsFactory): DeviceSettingsFactory {
        return iosSettingsFactory
    }
}
