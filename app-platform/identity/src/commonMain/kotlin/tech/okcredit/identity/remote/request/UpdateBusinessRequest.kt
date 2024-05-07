package tech.okcredit.identity.remote.request

import kotlinx.serialization.SerialName
import tech.okcredit.identity.remote.response.GetBusinessResponseWrapper

@kotlinx.serialization.Serializable
data class UpdateBusinessRequest(
    @SerialName("business_user_id")
    val business_user_id: String,
    @SerialName("business_user")
    val business_user: GetBusinessResponseWrapper,
    @SerialName("update_contact_name")
    val update_contact_name: Boolean = false,
    @SerialName("update_category")
    val update_category: Boolean = false,
    @SerialName("update_email")
    val update_email: Boolean = false,
    @SerialName("update_display_name")
    val update_display_name: Boolean = false,
    @SerialName("update_profile_image")
    val update_profile_image: Boolean = false,
    @SerialName("update_about")
    val update_about: Boolean = false,
    @SerialName("update_address")
    val update_address: Boolean = false,
    @SerialName("update_address_longitude")
    val update_address_longitude: Boolean = false,
    @SerialName("update_address_latitude")
    val update_address_latitude: Boolean = false,
    @SerialName("update_business_type")
    val update_business_type: Boolean = false,
)
