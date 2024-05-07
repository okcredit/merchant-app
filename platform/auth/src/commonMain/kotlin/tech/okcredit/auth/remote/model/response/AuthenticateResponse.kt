package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.okcredit.auth.remote.AuthRemoteSource.Companion.LATEST_SERVER_AUTH_VERSION

@Serializable
data class AuthenticateResponse(
    @SerialName("refresh_token")
    val refresh_token: String,
    @SerialName("expires_in")
    val expires_in: Int,
    @SerialName("new_user")
    val new_user: Boolean = false,
    @SerialName("mobile")
    val mobile: String? = null,
    @SerialName("app_lock")
    val app_lock: Boolean = false,
    @SerialName("version")
    val version: Int = LATEST_SERVER_AUTH_VERSION,
)
