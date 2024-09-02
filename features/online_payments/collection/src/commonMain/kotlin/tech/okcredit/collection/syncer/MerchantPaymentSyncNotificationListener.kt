package tech.okcredit.collection.syncer

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SyncNotificationListener

@Inject
class MerchantPaymentSyncNotificationListener(
    private val collectionSyncer: CollectionSyncer,
) : SyncNotificationListener {

    override fun onSyncNotification(payload: Map<String, Any?>) {
        collectionSyncer.scheduleSyncMerchantPayments(
            businessId = payload["businessId"] as String,
            source = "notification",
        )
    }
}
