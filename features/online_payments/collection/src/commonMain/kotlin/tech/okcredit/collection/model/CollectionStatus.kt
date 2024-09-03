package tech.okcredit.collection.model

object CollectionStatus {
    const val ACTIVE: Int = 1 // pending
    const val PAID = 2 // pending
    const val EXPIRED = 3
    const val CANCELLED = 4
    const val COMPLETE = 5 // success
    const val FAILED = 6
    const val REFUNDED = 7
    const val REFUND_INITIATED = 8
    const val PAYOUT_FAILED = 9
    const val MIGRATED = 10
    const val PAYOUT_INITIATED = 11
    const val BLINDPAY_PAID = 14
}
