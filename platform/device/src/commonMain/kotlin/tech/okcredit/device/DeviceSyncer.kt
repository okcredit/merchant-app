package tech.okcredit.device

import me.tatarka.inject.annotations.Inject
import tech.okcredit.device.local.DeviceLocalSource
import tech.okcredit.device.remote.DeviceRemoteSource

@Inject
class DeviceSyncer(
    private val deviceLocalSourceLazy: Lazy<DeviceLocalSource>,
    private val deviceRemoteSourceLazy: Lazy<DeviceRemoteSource>,
) {

    private val deviceLocalSource by lazy { deviceLocalSourceLazy.value }
    private val deviceRemoteSource by lazy { deviceRemoteSourceLazy.value }

    suspend fun executeSyncDevice() {
        deviceLocalSource.getDevice()?.let { device ->
            deviceRemoteSource.createOrUpdateDevice(device)
        }
    }
}
