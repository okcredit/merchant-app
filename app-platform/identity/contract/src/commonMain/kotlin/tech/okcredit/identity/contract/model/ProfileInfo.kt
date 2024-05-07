package tech.okcredit.identity.contract.model

data class ProfileInfo(
    val name: String? = "",
    val email: String? = "",
    val businessName: String? = "",
    val address: String? = "",
    val profileImage: String? = "",
)
