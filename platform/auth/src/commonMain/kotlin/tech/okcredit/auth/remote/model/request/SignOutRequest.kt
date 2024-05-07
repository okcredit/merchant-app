package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignOutRequest(
    @SerialName("type")
    val type: Int,
    @SerialName("device_id")
    val device_id: String?,
)
