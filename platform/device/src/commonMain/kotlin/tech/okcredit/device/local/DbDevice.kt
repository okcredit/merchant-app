package tech.okcredit.device.local

@kotlinx.serialization.Serializable
data class DbDevice(
    val id: String,
    val versionCode: Int,
    val apiLevel: Int,
    val aaid: String? = null,
    val fcmToken: String? = null,
    val createTime: Long,
    val updateTime: Long,
    val rooted: Boolean = false,
    val make: String?,
    val model: String?,
    val ipRegion: String? = null,
)
