package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Category(
    @SerialName("id")
    val id: String?,
    @SerialName("type")
    val type: Int? = null,
    @SerialName("name")
    val name: String?,
    @SerialName("image_url")
    val image_url: String? = null,
    @SerialName("is_popular")
    val is_popular: Boolean? = null,
)
