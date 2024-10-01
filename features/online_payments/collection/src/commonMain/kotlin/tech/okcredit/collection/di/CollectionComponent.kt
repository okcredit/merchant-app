package tech.okcredit.collection.di

import app.cash.sqldelight.ColumnAdapter
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import okcredit.base.syncer.SyncNotificationListener
import okcredit.base.syncer.SyncNotificationType
import tech.okcredit.collection.local.CollectionDatabase
import tech.okcredit.collection.local.database.OnlinePaymentEntity
import tech.okcredit.collection.local.database.OnlinePaymentQueries
import tech.okcredit.collection.syncer.MerchantPaymentSyncNotificationListener
import tech.okcredit.collection.syncer.MerchantProfileSyncNotificationListener

typealias CollectionDriverFactory = SqlDriverFactory

interface CollectionComponent {

    @Provides
    fun onlinePaymentDatabase(driverFactory: CollectionDriverFactory): OnlinePaymentQueries {
        val database = CollectionDatabase(
            driver = driverFactory.createDriver(),
            OnlinePaymentEntityAdapter = OnlinePaymentEntity.Adapter(
                statusAdapter = object : ColumnAdapter<Int, Long> {
                    override fun decode(databaseValue: Long): Int {
                        return databaseValue.toInt()
                    }

                    override fun encode(value: Int): Long {
                        return value.toLong()
                    }
                },
            ),
        )
        return database.onlinePaymentQueries
    }

    @Provides
    @IntoMap
    fun collectionDestinationListener(listener: MerchantProfileSyncNotificationListener): Pair<SyncNotificationType, SyncNotificationListener> {
        return SyncNotificationType.COLLECTION_DESTINATION to listener
    }

    @Provides
    @IntoMap
    fun collectionKycListener(listener: MerchantProfileSyncNotificationListener): Pair<SyncNotificationType, SyncNotificationListener> {
        return SyncNotificationType.COLLECTION_KYC to listener
    }

    @Provides
    @IntoMap
    fun merchantPaymentListener(listener: MerchantPaymentSyncNotificationListener): Pair<SyncNotificationType, SyncNotificationListener> {
        return SyncNotificationType.MERCHANT_PAYMENT to listener
    }
}
