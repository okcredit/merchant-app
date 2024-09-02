package tech.okcredit.collection.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import okcredit.base.local.Scope
import tech.okcredit.collection.local.database.CustomerCollectionProfile
import tech.okcredit.collection.local.database.OnlinePaymentEntity
import tech.okcredit.collection.local.database.OnlinePaymentQueries
import tech.okcredit.collection.local.database.SupplierCollectionProfile
import tech.okcredit.collection.model.CollectionCustomerProfile
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.remote.Converter

@Inject
class CollectionLocalSource(
    private val collectionDao: OnlinePaymentQueries,
    private val collectionPreference: CollectionPreference,
    private val ioDispatcher: IoDispatcher,
) {

    suspend fun setLastSyncMerchantCollectionsTime(time: Long, businessId: String) {
        collectionPreference.set(
            key = KEY_LAST_SYNC_MERCHANT_COLLECTIONS,
            value = time,
            scope = Scope.Business(businessId),
        )
    }

    suspend fun setLastSyncSendMoneyTime(time: Long, businessId: String) {
        collectionPreference.set(KEY_LAST_SYNC_SEND_MONEY, time, Scope.Business(businessId))
    }

    suspend fun getLastSyncMerchantCollectionsTime(businessId: String): Long? {
        return collectionPreference.getLong(
            KEY_LAST_SYNC_MERCHANT_COLLECTIONS,
            Scope.Business(businessId),
        ).firstOrNull()
    }

    suspend fun getLastSyncCustomerCollectionsTime(businessId: String): Long {
        return collectionPreference.getLong(
            KEY_LAST_SYNC_CUSTOMER_COLLECTIONS,
            Scope.Business(businessId),
        ).firstOrNull() ?: 0L
    }

    suspend fun getLastSyncSupplierCollectionsTime(businessId: String): Long {
        return collectionPreference.getLong(
            KEY_LAST_SYNC_SUPPLIER_COLLECTIONS,
            Scope.Business(businessId),
        ).firstOrNull() ?: 0L
    }

    suspend fun getLastSyncSupplierCollectionProfileTime(businessId: String): Long {
        return collectionPreference
            .getLong(KEY_LAST_SYNC_SUPPLIER_COLLECTION_PROFILES, Scope.Business(businessId))
            .firstOrNull() ?: 0L
    }

    suspend fun setLastSyncCustomerCollectionsTime(time: Long, businessId: String) {
        collectionPreference.set(
            KEY_LAST_SYNC_CUSTOMER_COLLECTIONS,
            time,
            Scope.Business(businessId),
        )
    }

    suspend fun setLastSyncSupplierCollectionsTime(time: Long, businessId: String) {
        collectionPreference.set(
            KEY_LAST_SYNC_SUPPLIER_COLLECTIONS,
            time,
            Scope.Business(businessId),
        )
    }

    suspend fun setLastSyncSupplierCollectionProfileTime(time: Long, businessId: String) {
        collectionPreference.set(
            KEY_LAST_SYNC_SUPPLIER_COLLECTION_PROFILES,
            time,
            Scope.Business(businessId),
        )
    }

    suspend fun clearCollectionSDK() {
        collectionDao.deleteAllCollections()
        collectionDao.deleteAllCollectionCustomerProfiles()
        collectionDao.deleteAllSupplierCollectionProfiles()
        collectionDao.deleteMerchantProfile()
        collectionPreference.clearCollectionPref()
    }

    suspend fun getLastSyncOnlineCollectionsTime(businessId: String): Long {
        return withContext(ioDispatcher) {
            collectionDao.lastOnlinePayment(businessId).executeAsOne()
                .updatedTime
        }
    }

    suspend fun paymentExists(id: String): Boolean {
        return withContext(ioDispatcher) {
            collectionDao.isPaymentExist(id).executeAsOne()
        }
    }

    /********************************* Collections *********************************/
    fun listCollections(businessId: String): Flow<List<OnlinePayment>> {
        return collectionDao.listCollections(businessId)
            .asFlow()
            .mapToList(ioDispatcher)
            .map {
                mapList(it, DbEntityMapper.onlinePaymentEntityConverter().reverse())
            }
    }

    fun observeCollectionsForAccount(
        businessId: String,
        customerId: String,
    ): Flow<List<OnlinePayment>> {
        return collectionDao.listCollectionsOfAccount(customerId, businessId)
            .asFlow()
            .mapToList(ioDispatcher)
            .map { paymentEntities ->
                paymentEntities.map { it.toExternal() }
            }
    }

    fun getCollection(collectionId: String, businessId: String): Flow<OnlinePayment> {
        return collectionDao.getCollection(collectionId, businessId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { entity ->
                entity.toExternal()
            }
    }

    suspend fun putCollections(
        onlineCollections: List<OnlinePayment>,
    ) {
        withContext(ioDispatcher) {
            val list = mapList(onlineCollections, DbEntityMapper.onlinePaymentEntityConverter())
            list.forEach { entity ->
                collectionDao.insertCollections(entity)
            }
        }
    }

    suspend fun putCollection(
        onlineCollection: OnlinePayment,
        businessId: String,
    ): Boolean {
        return withContext(ioDispatcher) {
            val existingPayment =
                collectionDao.getCollection(onlineCollection.id, businessId).executeAsOneOrNull()

            return@withContext if (existingPayment != null) {
                collectionDao.updateCollectionEntity(
                    id = onlineCollection.id,
                    updatedTime = onlineCollection.updateTime,
                    accountId = onlineCollection.accountId,
                    status = onlineCollection.status,
                    errorCode = onlineCollection.errorCode,
                    paymentSource = onlineCollection.paymentSource ?: existingPayment.paymentSource,
                    payoutDestination = onlineCollection.payoutDestination
                        ?: existingPayment.payoutDestination ?: "",
                    surcharge = onlineCollection.surcharge,
                    platformFee = onlineCollection.platformFee,
                    estimatedSettlementTime = onlineCollection.estimatedSettlementTime
                        ?: existingPayment.estimatedSettlementTime,
                )
                true
            } else {
                collectionDao.insertCollections(
                    DbEntityMapper.onlinePaymentEntityConverter().doForward(onlineCollection),
                )
                false
            }
        }
    }

    /********************************* Collection Profile *********************************/

    fun getCollectionMerchantProfile(businessId: String): Flow<CollectionMerchantProfile> {
        return collectionDao.getCollectionsProfile(businessId)
            .asFlow()
            .mapToList(ioDispatcher)
            .map { collectionProfile ->
                if (collectionProfile.isNotEmpty()) {
                    DbEntityMapper.collectionMerchantProfile.doBackward(collectionProfile[0])
                } else {
                    CollectionMerchantProfile.empty()
                }
            }
            .distinctUntilChanged()
    }

    fun setCollectionMerchantProfile(collectionMerchantProfile: CollectionMerchantProfile) {
        val dbEntity = DbEntityMapper.collectionMerchantProfile.doForward(
            collectionMerchantProfile,
        )
        return collectionDao.setCollectionsProfile(dbEntity)
    }

    fun clearCollectionMerchantProfile(businessId: String) {
        return collectionDao.deleteMerchantProfileForBusinessId(businessId)
    }

    /********************************* Collection Customer Profile *********************************/

    suspend fun putCustomerCollectionProfile(
        collectionCusProfile: CollectionCustomerProfile,
        businessId: String,
    ) {
        withContext(ioDispatcher) {
            collectionDao.insertCustomerCollectionProfile(
                CustomerCollectionProfile(
                    customerId = collectionCusProfile.accountId,
                    messageLink = collectionCusProfile.message_link,
                    qrIntent = collectionCusProfile.qr_intent,
                    linkId = collectionCusProfile.linkId,
                    businessId = businessId,
                ),
            )
        }
    }

    fun getCustomerCollectionProfile(
        customerId: String,
        businessId: String,
    ): Flow<CollectionCustomerProfile> {
        return collectionDao.getCustomerCollectionProfile(
            businessId = businessId,
            customerId = customerId,
        )
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { profile ->
                DbEntityMapper.customerCollectionProfileConverter(businessId).doBackward(profile)
            }
            .distinctUntilChanged()
    }

    fun listSupplierCollectionProfiles(businessId: String): Flow<List<CollectionCustomerProfile>> {
        return collectionDao.listSupplierCollectionProfiles(businessId)
            .asFlow()
            .mapToList(ioDispatcher)
            .map {
                mapList(it, DbEntityMapper.supplierProfileConverter(businessId).reverse())
            }
    }

    suspend fun putSupplierCollectionProfile(
        collectionCusProfile: CollectionCustomerProfile,
        businessId: String,
    ) {
        withContext(ioDispatcher) {
            collectionDao.insertSupplierCollectionProfile(
                SupplierCollectionProfile(
                    accountId = collectionCusProfile.accountId,
                    messageLink = collectionCusProfile.message_link,
                    name = collectionCusProfile.name,
                    type = collectionCusProfile.type,
                    paymentAddress = collectionCusProfile.paymentAddress,
                    linkId = collectionCusProfile.linkId,
                    destinationUpdateAllowed = collectionCusProfile.destinationUpdateAllowed,
                    businessId = businessId,
                ),
            )
        }
    }

    fun getSupplierCollectionProfile(
        customerId: String,
        businessId: String,
    ): Flow<CollectionCustomerProfile> {
        return collectionDao.getSupplierCollectionProfile(customerId, businessId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { profile ->
                DbEntityMapper.supplierProfileConverter(businessId).doBackward(profile)
            }
            .distinctUntilChanged()
    }

    suspend fun getSuppliersWithGivenPaymentAddress(
        paymentAddress: String,
        businessId: String,
    ): List<CollectionCustomerProfile> {
        return withContext(ioDispatcher) {
            val list = collectionDao.getSuppliersWithGivenPaymentAddress(paymentAddress, businessId)
                .executeAsList()
            val filteredList = mutableListOf<CollectionCustomerProfile>()
            list.forEach { profile ->
                val supplierCollectionProfile =
                    DbEntityMapper.supplierProfileConverter(businessId).doBackward(profile)
                filteredList.add(supplierCollectionProfile)
            }
            return@withContext filteredList
        }
    }

    suspend fun putSupplierCollectionProfiles(
        businessId: String,
        collectionSupplierProfile: List<CollectionCustomerProfile>,
    ) {
        withContext(ioDispatcher) {
            collectionSupplierProfile.forEach { profile ->
                collectionDao.insertSupplierCollectionProfiles(
                    SupplierCollectionProfile(
                        accountId = profile.accountId,
                        messageLink = profile.message_link,
                        name = profile.name,
                        type = profile.type,
                        paymentAddress = profile.paymentAddress,
                        linkId = profile.linkId,
                        destinationUpdateAllowed = profile.destinationUpdateAllowed,
                        businessId = businessId,
                    ),
                )
            }
        }
    }

    fun getKycStatus(merchantId: String): Flow<String> {
        return collectionDao.getCollectionsProfile(merchantId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.kycStatus ?: "" }
    }

    fun lastOnlinePayment(businessId: String): Flow<OnlinePayment> {
        return collectionDao.lastOnlinePayment(businessId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .distinctUntilChanged()
            .map { onlinePayments ->
                DbEntityMapper.onlinePaymentEntityConverter().doBackward(onlinePayments)
            }
    }

    suspend fun tagCustomerToPayment(
        paymentId: String,
        customerId: String,
        businessId: String,
    ) {
        withContext(ioDispatcher) {
            collectionDao.tagCustomerToPayment(paymentId, customerId, businessId)
        }
    }

    fun getOnlinePaymentsCount(businessId: String): Flow<Int> {
        return collectionDao.getOnlinePaymentsCount(businessId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.toInt() }
    }

    suspend fun getPaymentReminderEducation(): Boolean {
        return collectionPreference.getPaymentReminderEducation()
    }

    suspend fun setPaymentReminderEducation(show: Boolean) {
        collectionPreference.setPaymentReminderEducation(show)
    }

    fun getOnlinePaymentsLastSyncTime(businessId: String): Flow<Long?> {
        return collectionPreference.getOnlinePaymentsLastSyncTime(businessId)
    }

    suspend fun setPaymentReminderAfterFirstDestinationAdded(
        show: Boolean,
        businessId: String,
    ) {
        withContext(ioDispatcher) {
            collectionPreference.setPaymentReminderAfterFirstDestinationAdded(show, businessId)
        }
    }

    suspend fun getPaymentReminderAfterFirstDestinationAdded(businessId: String): Boolean {
        return withContext(ioDispatcher) {
            collectionPreference.getPaymentReminderAfterFirstDestinationAdded(businessId)
        }
    }

    fun shouldPlaySoundNotification(businessId: String): Flow<Boolean> {
        return collectionPreference.shouldPlaySoundNotification(businessId)
    }

    suspend fun getLastSyncSendMoneyTime(businessId: String): Long {
        return withContext(ioDispatcher) {
            collectionPreference.getLong(KEY_LAST_SYNC_SEND_MONEY, Scope.Business(businessId))
                .firstOrNull() ?: 0L
        }
    }

    fun listOfNewOnlinePaymentsCount(businessId: String): Flow<Int> {
        return collectionDao.listOfNewOnlinePaymentsCount(businessId)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.toInt() }
    }

    companion object {
        internal const val KEY_LAST_SYNC_CUSTOMER_COLLECTIONS = "last_sync_customer_collections"
        internal const val KEY_LAST_SYNC_SUPPLIER_COLLECTIONS = "last_sync_supplier_collections"
        internal const val KEY_LAST_SYNC_MERCHANT_COLLECTIONS = "last_sync_merchant_collections"
        internal const val KEY_LAST_SYNC_SUPPLIER_COLLECTION_PROFILES =
            "last_sync_supplier_collection_profiles"
        internal const val KEY_LAST_SYNC_SEND_MONEY = "last_sync_send_money"
    }
}

private fun OnlinePaymentEntity.toExternal(): OnlinePayment {
    return OnlinePayment(
        id = id,
        createTime = createdTime,
        updateTime = updatedTime,
        status = status,
        amount = amount,
        accountId = accountId,
        paymentId = paymentId,
        errorCode = errorCode,
        errorDescription = errorDescription,
        surcharge = surcharge,
        type = type,
        paymentFrom = paymentFrom,
        paymentUtr = paymentUtr,
        payoutUtr = payoutUtr,
        read = read,
        paymentSource = paymentSource,
        paymentMode = paymentMode,
        businessId = businessId,
        payoutTo = payoutTo,
        payoutDestination = payoutDestination,
        platformFee = platformFee,
        estimatedSettlementTime = estimatedSettlementTime,
        discount = discount,
    )
}

private fun <Input, Output> Converter<Input, Output>.reverse(): Converter<Output, Input> {
    return object : Converter<Output, Input> {
        override fun doForward(api: Output): Input {
            return this@reverse.doBackward(api)
        }

        override fun doBackward(b: Input): Output {
            return this@reverse.doForward(b)
        }
    }
}

fun <A, B> mapList(aList: List<A>, mapper: Converter<A, B>): List<B> {
    val bList: MutableList<B> = ArrayList(aList.size)
    for (a in aList) {
        bList.add(mapper.doForward(a))
    }
    return bList
}
