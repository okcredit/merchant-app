package tech.okcredit.device

object DeviceUtils {

    const val WHATSAPP_PACKAGE_NAME = "com.whatsapp"
    const val WHATSAPP_BUSINESS_PACKAGE_NAME = "com.whatsapp.w4b"

    suspend fun fetchAdId(): String {
        return "Empty Aaid"
    }

    fun getApiLevel(): Int {
        return 1
    }

    fun getDeviceBrandName(): String {
        return getPlatform().brandName
    }

    fun getDeviceModelName(): String {
        return getPlatform().model
    }
}
