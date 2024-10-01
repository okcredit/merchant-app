package tech.okcredit.collection

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.collection.di.CollectionComponent
import tech.okcredit.collection.di.CollectionDriverFactory
import tech.okcredit.collection.local.CollectionDatabase
import tech.okcredit.collection.local.CollectionSettingsFactory
import tech.okcredit.collection.syncer.CollectionEverythingSyncer
import tech.okcredit.collection.syncer.IosCollectionEverythingSyncer
import tech.okcredit.collection.syncer.IosMerchantPaymentSyncer
import tech.okcredit.collection.syncer.IosMerchantProfileSyncer
import tech.okcredit.collection.syncer.MerchantPaymentSyncer
import tech.okcredit.collection.syncer.MerchantProfileSyncer

@ContributesTo(AppScope::class)
interface IosCollectionComponent : CollectionComponent {

    @Provides
    fun provideCollectionDriverFactory(): CollectionDriverFactory {
        return IosSqlDriverFactory(CollectionDatabase.Schema, "okcredit_collections.db")
    }

    @Provides
    fun IosMerchantProfileSyncer.binds(): MerchantProfileSyncer = this

    @Provides
    fun IosMerchantPaymentSyncer.binds(): MerchantPaymentSyncer = this

    @Provides
    fun IosCollectionEverythingSyncer.binds(): CollectionEverythingSyncer = this

    @Provides
    fun IosCollectionSettingsFactory.binds(): CollectionSettingsFactory {
        return this
    }
}
