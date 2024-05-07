package tech.okcredit.device

import me.tatarka.inject.annotations.Inject
import tech.okcredit.device.local.DeviceLocalSource
import tech.okcredit.device.remote.DeviceRemoteSource

@Inject
class DeviceSyncer(
    private val deviceRemoteSource: DeviceRemoteSource,
    private val deviceLocalSource: DeviceLocalSource,
) {
    suspend fun executeSyncDevice() {
        deviceLocalSource.getDevice()?.let { device ->
            deviceRemoteSource.createOrUpdateDevice(device)
        }
    }
}
