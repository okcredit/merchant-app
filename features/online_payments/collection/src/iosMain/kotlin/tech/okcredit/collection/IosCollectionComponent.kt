package tech.okcredit.collection

import me.tatarka.inject.annotations.Provides
import tech.okcredit.collection.di.CollectionComponent
import tech.okcredit.collection.local.CollectionSettingsFactory
import tech.okcredit.collection.local.CollectionsDriverFactory
import tech.okcredit.collection.syncer.CollectionEverythingSyncer
import tech.okcredit.collection.syncer.IosCollectionEverythingSyncer
import tech.okcredit.collection.syncer.IosMerchantPaymentSyncer
import tech.okcredit.collection.syncer.IosMerchantProfileSyncer
import tech.okcredit.collection.syncer.MerchantPaymentSyncer
import tech.okcredit.collection.syncer.MerchantProfileSyncer

interface IosCollectionComponent : CollectionComponent {

    @Provides
    fun IosMerchantProfileSyncer.binds(): MerchantProfileSyncer = this

    @Provides
    fun IosMerchantPaymentSyncer.binds(): MerchantPaymentSyncer = this

    @Provides
    fun IosCollectionEverythingSyncer.binds(): CollectionEverythingSyncer = this

    @Provides
    fun IosCollectionsDriverFactory.bind(): CollectionsDriverFactory = this

    @Provides
    fun IosCollectionSettingsFactory.binds(): CollectionSettingsFactory {
        return this
    }
}
