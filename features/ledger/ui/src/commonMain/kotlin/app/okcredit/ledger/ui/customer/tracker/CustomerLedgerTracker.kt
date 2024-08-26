package app.okcredit.ledger.ui.customer.tracker

import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider


@Inject
class CustomerLedgerTracker(
    analyticsProvider: Lazy<AnalyticsProvider>
) {

    private val tracker by lazy { analyticsProvider.value }

    companion object {
        const val RECEIPT_RETRY_BUTTON_CLICKED = "Receipt : retry button clicked"

        const val TRANSACTION_ID = "transaction_id"
        const val URL = "url"
    }

    fun trackRetryButtonClicked(txnId: String, imageUrl: String) {
        tracker.logProductEvent(
            RECEIPT_RETRY_BUTTON_CLICKED,
            mapOf(TRANSACTION_ID to txnId, URL to imageUrl)
        )
    }
}