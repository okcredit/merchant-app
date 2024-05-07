package tech.okcredit.device.remote.request

@kotlinx.serialization.Serializable
data class CreateOrUpdateDeviceRequest(
    val device: ApiDevice,
)

@kotlinx.serialization.Serializable
data class ApiDevice(
    val name: String,
    val version_code: Int,
    val api_level: Int,
    val fcm_token: String?,
    val android_id: String?,
    val aa_id: String?,
    val make: String,
    val model: String,
)
