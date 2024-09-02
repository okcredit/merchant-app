package tech.okcredit.collection.syncer

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SyncNotificationListener

@Inject
class MerchantProfileSyncNotificationListener(
    private val collectionSyncer: CollectionSyncer,
) : SyncNotificationListener {

    override fun onSyncNotification(payload: Map<String, Any?>) {
        collectionSyncer.scheduleSyncMerchantProfile(payload["businessId"] as String, "fcm")
    }
}
