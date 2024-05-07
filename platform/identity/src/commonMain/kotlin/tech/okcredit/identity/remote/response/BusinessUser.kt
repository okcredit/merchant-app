package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class BusinessUser(
    @SerialName("id")
    val id: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("display_name")
    val display_name: String?,
    @SerialName("profile_image")
    val profile_image: String? = null,
    @SerialName("address")
    val address: Address? = null,
    @SerialName("about")
    val about: String? = null,
    @SerialName("create_time")
    val create_time: Long,
)
