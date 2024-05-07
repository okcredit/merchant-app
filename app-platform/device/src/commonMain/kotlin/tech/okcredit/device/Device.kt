package tech.okcredit.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: String,
    val versionCode: Int,
    val apiLevel: Int,
    val aaid: String? = null,
    val fcmToken: String? = null,
    val androidId: String? = null,
    val createTime: Long,
    val updateTime: Long,
    @SerialName("rooted")
    val isRooted: Boolean = false,
    @SerialName("make")
    val make: String?,
    @SerialName("model")
    val model: String?,
)

data class IpAddressData(
    val city: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val ip: String? = null,
    val region: String? = null,
    val region_code: String? = null,
)
