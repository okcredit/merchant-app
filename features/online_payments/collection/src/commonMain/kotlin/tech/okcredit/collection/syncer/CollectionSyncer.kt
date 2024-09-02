package tech.okcredit.collection.syncer

import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toJsonObject
import tech.okcredit.collection.local.CollectionLocalSource
import tech.okcredit.collection.local.mapList
import tech.okcredit.collection.model.CollectionServerErrors
import tech.okcredit.collection.model.CollectionSource
import tech.okcredit.collection.model.CollectionStatus
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.remote.ApiEntityMapper
import tech.okcredit.collection.remote.CollectionRemoteSource
import kotlin.coroutines.cancellation.CancellationException

typealias MerchantPaymentSyncer = OneTimeDataSyncer
typealias MerchantProfileSyncer = OneTimeDataSyncer
typealias CollectionEverythingSyncer = OneTimeDataSyncer

@Inject
class CollectionSyncer(
    private val localSource: CollectionLocalSource,
    private val remoteSource: CollectionRemoteSource,
    private val merchantProfileSyncer: MerchantProfileSyncer,
    private val merchantPaymentSyncer: MerchantPaymentSyncer,
    private val collectionEverythingSyncer: CollectionEverythingSyncer,
) {

    companion object {
        const val WORKER_TAG_BASE = "collection"
        const val WORKER_TAG_SYNC_EVERYTHING = "collection/scheduleSyncEverything"
        const val WORKER_TAG_SYNC_COLLECTION = "collection/syncCollection"
        const val WORKER_TAG_SYNC_COLLECTION_MERCHANT_PROFILE = "collection/syncCollectionProfile"
        const val ACCOUNT_ID = "account_id"
        const val BUSINESS_ID = "business_id"
        const val SOURCE = "source"
    }

    suspend fun syncAll(businessId: String, source: String) {
        coroutineScope {
            val merchantProfile =
                async { executeSyncMerchantCollectionProfile(businessId, source) }
            val merchantCollections =
                async { executeSyncMerchantQrPayments(businessId, source) }
            val customerCollection =
                async { executeSyncCustomerCollections(businessId, source) }
            val supplierCollections =
                async { executeSyncSupplierCollections(businessId, source) }

            awaitAll(
                merchantProfile,
                merchantCollections,
                customerCollection,
                supplierCollections,
            )
        }
    }

    suspend fun executeSyncCustomerCollections(businessId: String, source: String) {
        try {
            Logger.d { "executeSyncCustomerCollections $businessId" }

            val lastSyncTime = localSource.getLastSyncCustomerCollectionsTime(businessId)
            val list =
                remoteSource.getCustomerCollections(null, lastSyncTime.div(1000) + 1, businessId)
            if (list.isEmpty().not()) {
                if (source == CollectionSource.SYNC_SCREEN) {
                    localSource.putCollections(list)
                } else {
                    list.forEach { payment ->
                        val updated = localSource.putCollection(payment, businessId)
                    }
                }
                val time = list.maxOfOrNull { it.updateTime } ?: Clock.System.now().epochSeconds
                localSource.setLastSyncCustomerCollectionsTime(time, businessId)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Throwable) {
            Logger.e(exception) { "CollectionSyncer: executeSyncCustomerCollections: ${exception.message}" }
        }
    }

    suspend fun executeSyncSupplierCollections(businessId: String, source: String) {
        try {
            val lastSyncTime = localSource.getLastSyncSupplierCollectionsTime(businessId)

            val list =
                remoteSource.getSupplierCollections(null, lastSyncTime.div(1000) + 1, businessId)
            if (list.isEmpty().not()) {
                if (source == CollectionSource.SYNC_SCREEN) {
                    localSource.putCollections(list)
                } else {
                    list.forEach { payment ->
                        localSource.putCollection(payment, businessId)
                    }
                }
                val time = list.maxOfOrNull { it.updateTime } ?: Clock.System.now().epochSeconds
                localSource.setLastSyncSupplierCollectionsTime(time, businessId)
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: executeSyncSupplierCollections: ${exception.message}" }
        }
    }

    suspend fun executeSyncMerchantQrPayments(businessId: String, source: String) {
        try {
            val syncTime = localSource.getLastSyncMerchantCollectionsTime(businessId)
            var lastSyncTime = 0L
            if (syncTime != null && syncTime > 0) {
                lastSyncTime = syncTime.div(1000) + 1
            }

            val onlinePaymentResponse = remoteSource.getOnlinePaymentsList(lastSyncTime, businessId)
            if (onlinePaymentResponse.onlinePayments.isEmpty().not()) {
                val list = mapList(
                    onlinePaymentResponse.onlinePayments,
                    ApiEntityMapper.onlinePaymentConverter(businessId),
                )
                if (source == CollectionSource.SYNC_SCREEN) {
                    localSource.putCollections(list)
                } else {
                    list.forEach { onlinePayment ->
                        localSource.putCollection(onlinePayment, businessId)
                    }
                }
                val time = list.maxOfOrNull { it.updateTime } ?: Clock.System.now().epochSeconds
                localSource.setLastSyncMerchantCollectionsTime(time, businessId)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: executeSyncMerchantQrPayments: ${exception.message}" }
            exception.printStackTrace()
        }
    }

    suspend fun executeSyncSuppliersCollectionProfile(businessId: String, source: String) {
        try {
            val lastSyncTime = localSource.getLastSyncSupplierCollectionProfileTime(businessId)
            val response = remoteSource.getSupplierCollectionProfiles(businessId)

            if (!response.supplierPaymentProfilesList.isNullOrEmpty()) {
                val list = mapList(
                    response.supplierPaymentProfilesList.filterNotNull(),
                    ApiEntityMapper.COLLECTION_SUPPLIER_PROFILE,
                )
                localSource.putSupplierCollectionProfiles(businessId, list)
                localSource.setLastSyncSupplierCollectionProfileTime(
                    Clock.System.now().epochSeconds,
                    businessId,
                )
            } else {
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
        }
    }

    suspend fun syncCollectionFromNotification(
        onlinePayment: OnlinePayment,
        businessId: String,
        source: String,
        paymentType: String,
    ) {
        try {
            verifyAndUpdateForOnlinePayment(
                onlinePayment = onlinePayment,
                businessId = businessId,
                paymentType = paymentType,
            )
            mayBePlaySoundNotification(businessId, onlinePayment)
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: syncCollectionFromNotification: ${exception.message}" }
        }
    }

    private suspend fun mayBePlaySoundNotification(
        businessId: String,
        onlinePayment: OnlinePayment,
    ) {
        // check if payment was received
        if (onlinePayment.type != OnlinePayment.TYPE_RECEIVED && onlinePayment.type != OnlinePayment.TYPE_LINK_RECEIVED) {
            return
        }

        // playing sound notification only when money is credited to bank account
        if (onlinePayment.status != CollectionStatus.COMPLETE) return

        // check if it is enabled in settings
        if (!localSource.shouldPlaySoundNotification(businessId).first()) return
    }

    suspend fun syncOnlinePaymentsFromNotification(
        onlinePayment: OnlinePayment,
        businessId: String,
        source: String,
        paymentType: String,
    ) {
        try {
            verifyAndUpdateForOnlinePayment(
                onlinePayment = onlinePayment,
                businessId = businessId,
                paymentType = paymentType,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: syncCollectionFromNotification: ${exception.message}" }
        }
    }

    private suspend fun verifyAndUpdateForOnlinePayment(
        onlinePayment: OnlinePayment,
        businessId: String,
        paymentType: String,
    ) {
        try {
            val exist = kotlin.runCatching {
                localSource.paymentExists(onlinePayment.id)
            }.getOrNull() ?: false

            val updated = localSource.putCollection(onlinePayment, businessId)
            // no need to proceed if there was not previous entry
            if (!exist) {
                return
            }

            val existingPayment =
                localSource.getCollection(onlinePayment.id, businessId).firstOrNull()
                    ?: return
            if (existingPayment.status == onlinePayment.status) {
                return
            }

            val list = listOf(
                CollectionStatus.PAID,
                CollectionStatus.PAYOUT_INITIATED,
                CollectionStatus.PAYOUT_FAILED,
                CollectionStatus.REFUND_INITIATED,
                CollectionStatus.REFUNDED,
                CollectionStatus.FAILED,
                CollectionStatus.COMPLETE,
            )
            val shouldVerify = when (onlinePayment.status) {
                CollectionStatus.REFUNDED,
                CollectionStatus.FAILED,
                CollectionStatus.COMPLETE,
                -> false

                CollectionStatus.PAYOUT_INITIATED,
                -> existingPayment.status in list.subList(2, list.size)

                CollectionStatus.PAYOUT_FAILED,
                -> existingPayment.status in list.subList(3, list.size)

                CollectionStatus.REFUND_INITIATED,
                -> existingPayment.status in list.subList(4, list.size)

                else -> true
            }

            if (!shouldVerify) {
                return
            }

            val type = paymentType.ifEmpty { guessPaymentTypeForOnlinePayment(onlinePayment) }
            val response = remoteSource.getTransactionStatus(onlinePayment.id, type, businessId)
            var status = -1
            try {
                status = response.status.toInt()
            } catch (e: NumberFormatException) {
            }
            if (onlinePayment.status != status && status > 0) {
                val newUpdated =
                    localSource.putCollection(onlinePayment.copy(status = status), businessId)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: executeSyncCollectionProfileForSupplier: ${exception.message}" }
        }
    }

    /**
     * This is the corner case where we didn't get any payment type from backend, so we are guessing it from the info that
     * we have.
     */
    private fun guessPaymentTypeForOnlinePayment(onlinePayment: OnlinePayment): String {
        if (onlinePayment.accountId.isNullOrEmpty()) {
            return OnlinePayment.PAYMENT_TYPE_MERCHANT_QR
        }

        val isCustomer = true
        if (isCustomer) {
            return OnlinePayment.PAYMENT_TYPE_CUSTOMER_COLLECTION
        }

        return OnlinePayment.PAYMENT_TYPE_SUPPLIER_COLLECTION
    }

    suspend fun executeSyncMerchantCollectionProfile(businessId: String, source: String?) {
        try {
            val response = remoteSource.getCollectionProfiles(businessId)
            localSource.setCollectionMerchantProfile(response.collectionMerchantProfile)
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            exception.printStackTrace()
            if (exception is CollectionServerErrors.AddressNotFound) {
                localSource.clearCollectionMerchantProfile(businessId)
            }
        }
    }

    suspend fun executeSyncCollectionProfileForCustomer(
        customerId: String,
        businessId: String,
        source: String?,
    ) {
        try {
            val customerProfile = remoteSource.getCollectionCustomerProfile(customerId, businessId)
            localSource.putCustomerCollectionProfile(customerProfile, businessId)
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: syncCollectionProfileForCustomerInternal: ${exception.message}" }
        }
    }

    suspend fun executeSyncCollectionProfileForSupplier(
        accountId: String,
        businessId: String,
        source: String?,
    ) {
        try {
            val supplierProfile = remoteSource.getCollectionSupplierProfile(accountId, businessId)
            localSource.putSupplierCollectionProfile(supplierProfile, businessId)
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: executeSyncCollectionProfileForSupplier: ${exception.message}" }
        }
    }

    suspend fun executeSendMoneyTransactions(businessId: String, source: String) {
        try {
            val syncTime = localSource.getLastSyncSendMoneyTime(businessId)
            var lastSyncTime = 0L
            if (syncTime > 0) {
                lastSyncTime = syncTime.div(1000) + 1
            }

            val response =
                remoteSource.listSendMoneyTransactions(businessId, lastSyncTime)
            if (response.isNotEmpty()) {
                localSource.putCollections(response)
                val time = response.maxOfOrNull { it.updateTime } ?: Clock.System.now().epochSeconds
                localSource.setLastSyncSendMoneyTime(time, businessId)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            Logger.e(exception) { "CollectionSyncer: executeSendMoneyTransactions: ${exception.message}" }
        }
    }

    fun scheduleSyncMerchantPayments(businessId: String, source: String) {
        merchantPaymentSyncer.schedule(
            input = mapOf(
                BUSINESS_ID to businessId,
                SOURCE to source,
            ).toJsonObject(),
        )
    }

    fun scheduleSyncMerchantProfile(businessId: String, source: String) {
        merchantProfileSyncer.schedule(
            input = mapOf(
                BUSINESS_ID to businessId,
                SOURCE to source,
            ).toJsonObject(),
        )
    }

    fun scheduleSyncEverything(businessId: String, source: String) {
        collectionEverythingSyncer.schedule(
            input = mapOf(
                BUSINESS_ID to businessId,
                SOURCE to source,
            ).toJsonObject(),
        )
    }
}
