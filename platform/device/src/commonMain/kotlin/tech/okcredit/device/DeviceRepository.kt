package tech.okcredit.device

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.AppVersionCode
import okcredit.base.randomUUID
import tech.okcredit.device.local.DeviceLocalSource
import tech.okcredit.device.usecase.GetOrCreateDeviceId

@Inject
class DeviceRepository(
    private val versionCode: AppVersionCode,
    private val deviceLocalSourceLazy: Lazy<DeviceLocalSource>,
    private val deviceSyncerLazy: Lazy<DeviceSyncer>,
    private val deviceIdProviderLazy: Lazy<GetOrCreateDeviceId>,
) {

    private val deviceLocalSource by lazy { deviceLocalSourceLazy.value }
    private val deviceSyncer by lazy { deviceSyncerLazy.value }
    private val deviceIdProvider by lazy { deviceIdProviderLazy.value }

    private val isReady = MutableStateFlow(false)
    private val mutex = Mutex()

    private var deviceInMemoryCache: Device? = null

    private val isDeviceReady by lazy { isReady.asStateFlow() }

    suspend fun syncDeviceData() {
        mutex.withLock {
            if (isDeviceReady.value) {
                return@withLock
            }

            val initialDevice = runCatching { deviceLocalSource.getDevice() }.getOrNull()
            val aaid = DeviceUtils.fetchAdId()

            var newDevice = initialDevice ?: run {
                createNewDevice(id = deviceIdProvider.current(), aaid = aaid)
            }

            if (
                newDevice.versionCode != versionCode ||
                newDevice.apiLevel != DeviceUtils.getApiLevel() ||
                newDevice.aaid != aaid
            ) {
                newDevice = newDevice.copy(
                    versionCode = versionCode,
                    apiLevel = DeviceUtils.getApiLevel(),
                    updateTime = Clock.System.now().toEpochMilliseconds(),
                    isRooted = false,
                )
            }

            if (initialDevice != newDevice) {
                putDeviceAndUpdateCache(newDevice)
                deviceSyncer.executeSyncDevice()
            } else {
                deviceInMemoryCache = newDevice
            }

            isReady.emit(true)
        }
    }

    private suspend fun putDeviceAndUpdateCache(device: Device) {
        deviceLocalSource.putDevice(device)
        deviceInMemoryCache = device
    }

    private fun createNewDevice(id: String, aaid: String): Device {
        return Device(
            id = id,
            versionCode = versionCode,
            apiLevel = DeviceUtils.getApiLevel(),
            aaid = aaid,
            fcmToken = null,
            androidId = null,
            createTime = Clock.System.now().toEpochMilliseconds(),
            updateTime = Clock.System.now().toEpochMilliseconds(),
            isRooted = false,
            make = DeviceUtils.getDeviceBrandName(),
            model = DeviceUtils.getDeviceModelName(),
        )
    }

    suspend fun updateFcmToken(token: String) {
        val device = deviceInMemoryCache ?: deviceLocalSource.getDevice() ?: createNewDevice(id = randomUUID(), aaid = "")
        val updatedDevice = device.copy(fcmToken = token)
        putDeviceAndUpdateCache(updatedDevice)
        syncDeviceData()
    }
}
