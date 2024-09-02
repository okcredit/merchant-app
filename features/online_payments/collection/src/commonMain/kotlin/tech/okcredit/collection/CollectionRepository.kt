package tech.okcredit.collection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.local.CollectionLocalSource
import tech.okcredit.collection.model.CollectionCustomerProfile
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.remote.CollectionRemoteSource
import tech.okcredit.collection.remote.response.MerchantCollectionProfileResponse
import tech.okcredit.collection.syncer.CollectionSyncer

@Inject
class CollectionRepository(
    private val localSource: CollectionLocalSource,
    private val remoteSource: CollectionRemoteSource,
    private val syncer: CollectionSyncer,
) {

    fun observeBusinessCollectionProfile(businessId: String): Flow<CollectionMerchantProfile> {
        return flow {
            val profile = localSource.getCollectionMerchantProfile(businessId).firstOrNull()
            if (!profile?.payment_address.isNullOrEmpty() && profile?.merchant_vpa.isNullOrEmpty()) {
                syncer.executeSyncMerchantCollectionProfile(
                    businessId = businessId,
                    source = "CollectionSyncer.Source.MERCHANT_PROFILE",
                )
            }
            emitAll(localSource.getCollectionMerchantProfile(businessId))
        }
    }

    fun getCollectionCustomerProfile(
        businessId: String,
        customerId: String,
    ): Flow<CollectionCustomerProfile> {
        return flow {
            val profile = localSource.getCustomerCollectionProfile(customerId, businessId)
                .firstOrNull()
            if (profile?.qr_intent.isNullOrEmpty() && profile?.message_link.isNullOrEmpty()) {
                syncer.executeSyncCollectionProfileForCustomer(customerId, businessId, "")
            }
            emitAll(localSource.getCustomerCollectionProfile(customerId, businessId))
        }
    }

    fun observeAllPayments(businessId: String): Flow<List<OnlinePayment>> {
        return localSource.listCollections(businessId)
    }

    fun observeCollectionsForAccount(
        businessId: String,
        accountId: String,
    ): Flow<List<OnlinePayment>> {
        return localSource.observeCollectionsForAccount(businessId, accountId)
    }

    fun getCollection(
        collectionId: String,
        businessId: String,
    ): Flow<OnlinePayment> {
        return localSource.getCollection(collectionId, businessId)
    }

    suspend fun clearLocalData() {
        return localSource.clearCollectionSDK()
    }

    suspend fun setActiveDestination(
        collectionMerchantProfile: CollectionMerchantProfile,
        async: Boolean,
        businessId: String,
    ): MerchantCollectionProfileResponse {
        val profileResponse =
            remoteSource
                .setActiveDestination(collectionMerchantProfile, async, businessId)
        return if (collectionMerchantProfile.payment_address.isEmpty()) {
            localSource.clearCollectionMerchantProfile(businessId)
            profileResponse
        } else {
            localSource.setCollectionMerchantProfile(collectionMerchantProfile)
            syncer.executeSyncMerchantCollectionProfile(businessId, "set_destination")
            localSource.setPaymentReminderEducation(true)
            localSource.setPaymentReminderAfterFirstDestinationAdded(true, businessId)
            profileResponse
        }
    }

    suspend fun validatePaymentAddress(
        paymentAddressType: String,
        paymentAddress: String,
        businessId: String,
    ): Pair<Boolean, String> {
        return remoteSource.validatePaymentAddress(
            paymentAddressType,
            paymentAddress,
            businessId,
        )
    }

    fun isCollectionActivated(businessId: String): Flow<Boolean> {
        return localSource.getCollectionMerchantProfile(businessId = businessId).map {
            it.payment_address.isEmpty().not()
        }
    }

    fun listSupplierCollectionProfiles(businessId: String): Flow<List<CollectionCustomerProfile>> {
        return localSource.listSupplierCollectionProfiles(businessId)
    }

    fun getSupplierCollectionProfile(
        businessId: String,
        accountId: String,
    ): Flow<CollectionCustomerProfile> {
        return localSource.getSupplierCollectionProfile(accountId, businessId)
    }

    suspend fun getSuppliersWithGivenPaymentAddress(
        paymentAddress: String,
        businessId: String,
    ): List<CollectionCustomerProfile> {
        return localSource.getSuppliersWithGivenPaymentAddress(paymentAddress, businessId)
    }

    fun getOnlinePaymentsCount(businessId: String): Flow<Int> {
        return localSource.getOnlinePaymentsCount(businessId)
    }

    fun lastOnlinePayment(businessId: String): Flow<OnlinePayment> {
        return localSource.lastOnlinePayment(businessId)
    }

    fun listOfNewOnlinePaymentsCount(businessId: String): Flow<Int> {
        return localSource.listOfNewOnlinePaymentsCount(businessId)
    }

    suspend fun tagMerchantPaymentWithCustomer(
        customerId: String,
        paymentId: String,
        businessId: String,
    ) {
        remoteSource.tagMerchantPaymentWithCustomer(customerId, paymentId, businessId)
        localSource.tagCustomerToPayment(paymentId, customerId, businessId)
    }

    fun getKycStatus(businessId: String): Flow<String> {
        return localSource.getKycStatus(businessId)
    }

    fun getOnlinePaymentCount(businessId: String): Flow<Int> {
        return localSource.getOnlinePaymentsCount(businessId)
    }

    suspend fun setOnlinePaymentTag(businessId: String) {
        val time = localSource.getLastSyncOnlineCollectionsTime(businessId)
        remoteSource.setPaymentTag(time.div(1000), businessId)
    }

    suspend fun getPaymentReminderEducation(): Boolean {
        return localSource.getPaymentReminderEducation()
    }

    suspend fun setPaymentReminderEducation(show: Boolean) {
        localSource.setPaymentReminderEducation(show)
    }

    suspend fun getPaymentReminderAfterFirstDestinationAdded(businessId: String): Boolean {
        return if (localSource.getPaymentReminderAfterFirstDestinationAdded(businessId)) {
            // Set value to false again as for true value we want show ui once
            localSource.setPaymentReminderAfterFirstDestinationAdded(false, businessId)
            true
        } else {
            false
        }
    }
}
