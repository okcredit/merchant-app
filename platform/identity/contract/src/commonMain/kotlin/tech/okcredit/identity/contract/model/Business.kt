package tech.okcredit.identity.contract.model

data class Business(
    val id: String,
    val name: String,
    val mobile: String,
    val profileImage: String? = null,
    val address: String? = null,
    val addressLatitude: Double? = null,
    val addressLongitude: Double? = null,
    val about: String? = null,
    val email: String? = null,
    val contactName: String? = null,
    val createdAt: Long,
    val categoryId: String? = null,
    val updateCategory: Boolean = false,
    val currentMobileOTPToken: String? = null,
    val newMobileOTPToken: String? = null,
    val updateMobile: Boolean = false,
    val othersCategoryName: String? = null,
    val category: Category? = null,
    val businessType: BusinessType? = null,
    val isFirst: Boolean = false,
) {
    fun isNameSet(): Boolean {
        return (name != mobile)
    }
}
