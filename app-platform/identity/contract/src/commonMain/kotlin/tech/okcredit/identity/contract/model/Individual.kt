package tech.okcredit.identity.contract.model

data class Individual(
    val id: String,
    val createTime: Long?,
    val mobile: String,
    val email: String?,
    val registerTime: Long?,
    val lang: String?,
    val displayName: String?,
    val profileImage: String?,
    val addressText: String?,
    val longitude: Double?,
    val latitude: Double?,
    val about: String?,
    val recoveryMobile: String?,
    val businessIds: List<String>,
)
