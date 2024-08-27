package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.ui.customer.tracker.CustomerLedgerTracker
import me.tatarka.inject.annotations.Inject

@Inject
class TrackWithIntent(
    customerLedgerTracker: Lazy<CustomerLedgerTracker>
) {
    private val tracker by lazy { customerLedgerTracker.value }

    sealed class EventType {
        data class TrackReceiptLoadFailed(
            val txnId: String,
            val imageUrl: String,
            val errorMessage: String
        ) : EventType()

        data class TrackRetryButtonClicked(val txnId: String, val imageUrl: String) : EventType()
        data class TrackRetryButtonShown(val txnId: String, val imageUrl: String) : EventType()
    }

    fun execute(eventType: EventType) {
        when (eventType) {
            is EventType.TrackReceiptLoadFailed -> tracker.trackReceiptLoadFailed(
                eventType.txnId,
                eventType.imageUrl,
                eventType.errorMessage,
                ""
            )

            is EventType.TrackRetryButtonClicked -> tracker.trackRetryButtonClicked(
                eventType.txnId,
                eventType.imageUrl
            )

            is EventType.TrackRetryButtonShown -> tracker.trackRetryButtonShown(
                eventType.txnId,
                eventType.imageUrl
            )
        }
    }
}