package tech.okcredit.device.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.network.ClientConfig
import okcredit.base.network.DeviceIdProvider
import okcredit.base.syncer.AppInitializer
import tech.okcredit.device.DeviceAppInitializer
import tech.okcredit.device.remote.DeviceApiClient
import tech.okcredit.device.remote.createDeviceApiClient
import tech.okcredit.device.usecase.GetOrCreateDeviceId

interface DeviceComponent {

    @Provides
    fun deviceApiClient(ktorfit: Ktorfit): DeviceApiClient {
        return ktorfit.createDeviceApiClient()
    }

    @IntoSet
    @Provides
    fun deviceAppInitializer(initializer: DeviceAppInitializer): AppInitializer {
        return initializer
    }

    @Provides
    fun GetOrCreateDeviceId.deviceIdProvider(): DeviceIdProvider {
        return this
    }

    @IntoSet
    @Provides
    fun deviceClientConfig(config: DeviceClientConfig): ClientConfig {
        return config
    }
}
