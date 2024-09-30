package app.okcredit.ledger.ui.customer.tracker

import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider

@Inject
class CustomerLedgerTracker(
    analyticsProvider: Lazy<AnalyticsProvider>,
) {

    private val tracker by lazy { analyticsProvider.value }

    companion object {
        const val RECEIPT_RETRY_BUTTON_CLICKED = "Receipt : retry button clicked"
        const val RECEIPT_LOAD_FAILED = "Receipt Load Failed"
        const val RECEIPT_RETRY_BUTTON_SHOWN = "Receipt : retry button shown"

        const val TRANSACTION_ID = "transaction_id"
        const val URL = "url"
        const val REASON = "reason"
        const val STACKTRACE = "stacktrace"
    }

    fun trackRetryButtonClicked(txnId: String, imageUrl: String) {
        tracker.logProductEvent(
            RECEIPT_RETRY_BUTTON_CLICKED,
            mapOf(TRANSACTION_ID to txnId, URL to imageUrl),
        )
    }

    fun trackReceiptLoadFailed(
        transactionId: String,
        url: String,
        reason: String,
        stacktrace: String,
    ) {
        val properties = mapOf(
            TRANSACTION_ID to transactionId,
            URL to url,
            REASON to reason,
            STACKTRACE to stacktrace,
        )
        tracker.logProductEvent(RECEIPT_LOAD_FAILED, properties)
    }

    fun trackRetryButtonShown(txnId: String, imageUrl: String) {
        tracker.logProductEvent(
            RECEIPT_RETRY_BUTTON_SHOWN,
            mapOf(TRANSACTION_ID to txnId, URL to imageUrl),
        )
    }
}
