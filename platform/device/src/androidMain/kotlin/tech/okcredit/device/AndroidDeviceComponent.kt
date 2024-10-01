package tech.okcredit.device

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.device.di.DeviceComponent

@ContributesTo(AppScope::class)
interface AndroidDeviceComponent : DeviceComponent {

    @Provides
    fun settingsFactory(factory: AndroidDeviceSettingsFactory): DeviceSettingsFactory {
        return factory
    }
}
