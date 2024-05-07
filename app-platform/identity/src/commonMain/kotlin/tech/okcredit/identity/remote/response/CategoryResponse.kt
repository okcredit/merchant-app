package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CategoryResponse(
    @SerialName("categories")
    val categories: List<Category>,
)
