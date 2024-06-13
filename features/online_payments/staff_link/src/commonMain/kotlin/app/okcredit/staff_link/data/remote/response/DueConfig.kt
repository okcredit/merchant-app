package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DueConfig(
    @SerialName(value = "config")
    val config: Int,
    @SerialName(value = "end_time")
    val endTime: Long? = null,
    @SerialName(value = "start_time")
    val startTime: Long? = null,
)
