package tech.okcredit.collection.model

data class OnlinePayment(
    val id: String,
    val createTime: Long,
    val updateTime: Long,
    val status: Int,
    val amount: Long,
    val accountId: String?,
    val paymentId: String,
    val paymentSource: String?,
    val paymentMode: String? = null,
    val type: String,
    val errorCode: String = "",
    val errorDescription: String = "",
    val read: Boolean = false,
    val surcharge: Long,
    val paymentFrom: String? = null,
    val paymentUtr: String? = null,
    val payoutUtr: String? = null,
    val payoutTo: String? = null,
    val payoutDestination: String? = null,
    val businessId: String,
    val platformFee: Long = 0L,
    val estimatedSettlementTime: Long? = null,
    val discount: Long = 0L,
) {

    companion object {
        const val TYPE_PAID = "payment_paid"
        const val TYPE_RECEIVED = "payment_received"
        const val TYPE_LINK_RECEIVED = "payment_link_received"

        const val PAYMENT_TYPE_MERCHANT_QR = "merchant_qr"
        const val PAYMENT_TYPE_CUSTOMER_COLLECTION = "customer_collection"
        const val PAYMENT_TYPE_SUPPLIER_COLLECTION = "supplier_collection"
    }
}
