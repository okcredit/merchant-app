package tech.okcredit.collection

import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import okcredit.base.syncer.WorkerFactoryPair
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.collection.di.CollectionComponent
import tech.okcredit.collection.di.CollectionDriverFactory
import tech.okcredit.collection.local.CollectionDatabase
import tech.okcredit.collection.local.CollectionSettingsFactory
import tech.okcredit.collection.syncer.AndroidCollectionEverythingSyncer
import tech.okcredit.collection.syncer.AndroidMerchantPaymentSyncer
import tech.okcredit.collection.syncer.AndroidMerchantProfileSyncer
import tech.okcredit.collection.syncer.CollectionEverythingSyncer
import tech.okcredit.collection.syncer.MerchantPaymentSyncer
import tech.okcredit.collection.syncer.MerchantProfileSyncer
import tech.okcredit.collection.syncer.SyncCollectionEverythingWorker
import tech.okcredit.collection.syncer.SyncMerchantPaymentWorker
import tech.okcredit.collection.syncer.SyncMerchantProfileWorker

@ContributesTo(AppScope::class)
interface AndroidCollectionComponent : CollectionComponent {

    @Provides
    fun AndroidMerchantProfileSyncer.binds(): MerchantProfileSyncer = this

    @Provides
    fun AndroidMerchantPaymentSyncer.binds(): MerchantPaymentSyncer = this

    @Provides
    fun AndroidCollectionEverythingSyncer.binds(): CollectionEverythingSyncer = this

    @Provides
    fun AndroidCollectionSettingsFactory.binds(): CollectionSettingsFactory = this

    @Provides
    fun collectionDriverFactory(context: android.content.Context): CollectionDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = CollectionDatabase.Schema,
            name = "okcredit_collections.db",
        )
    }

    @Provides
    @IntoMap
    fun merchantProfileWorker(factory: SyncMerchantProfileWorker.Factory): WorkerFactoryPair {
        return SyncMerchantProfileWorker::class to factory
    }

    @Provides
    @IntoMap
    fun merchantPaymentWorker(factory: SyncMerchantPaymentWorker.Factory): WorkerFactoryPair {
        return SyncMerchantPaymentWorker::class to factory
    }

    @Provides
    @IntoMap
    fun collectionEverythingWorker(factory: SyncCollectionEverythingWorker.Factory): WorkerFactoryPair {
        return SyncCollectionEverythingWorker::class to factory
    }
}
