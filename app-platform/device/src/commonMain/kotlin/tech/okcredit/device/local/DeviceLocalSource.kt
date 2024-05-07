package tech.okcredit.device.local

import me.tatarka.inject.annotations.Inject
import tech.okcredit.device.Device

@Inject
class DeviceLocalSource constructor(
    private val devicePreferences: DevicePreferences,
) {

    suspend fun getDevice(): Device? {
        return devicePreferences.getDevice()?.let {
            convertToDevice(it)
        }
    }

    suspend fun putDevice(device: Device) {
        devicePreferences.getIpRegion()
            .also {
                devicePreferences.putDevice(convertToDbDevice(device, it))
            }
    }

    suspend fun getIsIpRegionSynced(): Boolean {
        return devicePreferences.getIpRegion().isNullOrEmpty().not()
    }

    suspend fun getIpRegion(): String? {
        return devicePreferences.getIpRegion()
    }

    suspend fun setIpRegion(value: String) {
        return devicePreferences.setIpRegion(value)
    }

    private fun convertToDevice(device: DbDevice) = Device(
        id = device.id,
        versionCode = device.versionCode,
        apiLevel = device.apiLevel,
        aaid = device.aaid,
        fcmToken = device.fcmToken,
        createTime = device.createTime,
        updateTime = device.updateTime,
        isRooted = device.rooted,
        make = device.make,
        model = device.model,
    )

    private fun convertToDbDevice(device: Device, ipRegion: String?) = DbDevice(
        id = device.id,
        versionCode = device.versionCode,
        apiLevel = device.apiLevel,
        aaid = device.aaid,
        fcmToken = device.fcmToken,
        createTime = device.createTime,
        updateTime = device.updateTime,
        rooted = device.isRooted,
        make = device.make,
        model = device.model,
        ipRegion = ipRegion,
    )
}
