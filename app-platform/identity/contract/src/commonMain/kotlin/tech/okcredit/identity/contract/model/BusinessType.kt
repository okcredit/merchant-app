package tech.okcredit.identity.contract.model

data class BusinessType(
    val id: String,
    val name: String? = null,
    val image_url: String? = null,
    val title: String? = null,
    val sub_title: String? = null,
)
