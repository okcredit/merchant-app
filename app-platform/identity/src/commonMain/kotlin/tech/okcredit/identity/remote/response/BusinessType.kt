package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class BusinessType(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("image_url")
    val image_url: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("sub_title")
    val sub_title: String? = null,
)
