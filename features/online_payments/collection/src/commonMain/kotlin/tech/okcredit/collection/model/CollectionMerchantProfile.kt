package tech.okcredit.collection.model

data class CollectionMerchantProfile(
    val merchant_id: String,
    val name: String? = "",
    val payment_address: String = "",
    val type: String = "",
    val merchant_vpa: String? = "",
    val limitType: String? = null,
    val limit: Long = 0L,
    val remainingLimit: Long = 0L,
    val merchantQrEnabled: Boolean = false,
    val merchantLink: String? = "",
    val qrIntent: String? = null,
    val riskCategory: String = "NO_RISK",
    val kycStatus: String = "",
) {

    companion object {
        const val TRIAL = "TRIAL"
        const val DAILY = "DAILY"

        fun empty(): CollectionMerchantProfile {
            return CollectionMerchantProfile("")
        }
    }
}
