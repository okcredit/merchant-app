package tech.okcredit.identity.contract.model

data class Category(
    val id: String? = null,
    val type: Int,
    val name: String? = null,
    val imageUrl: String? = null,
    val isPopular: Boolean = true,
)
