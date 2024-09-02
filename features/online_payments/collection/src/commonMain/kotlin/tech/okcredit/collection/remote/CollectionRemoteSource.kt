package tech.okcredit.collection.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.ApiError
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.asError
import okcredit.base.network.get
import okcredit.base.network.getOrThrow
import okcredit.base.network.post
import tech.okcredit.collection.CollectionConstants
import tech.okcredit.collection.model.CollectionCustomerProfile
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.CollectionProfiles
import tech.okcredit.collection.model.CollectionServerErrors
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.remote.request.*
import tech.okcredit.collection.remote.response.*

@Inject
class CollectionRemoteSource(
    private val authorizedHttpClient: AuthorizedHttpClient,
    private val baseUrl: BaseUrl,
) {

    suspend fun getCustomerCollections(
        customerId: String?,
        timestamp: Long?,
        businessId: String,
    ): List<OnlinePayment> {
        return authorizedHttpClient.get<List<ApiCollection>>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/ListCustomerCollections",
            queryParams = mapOf(
                "customer_id" to (customerId ?: ""),
                "after" to (timestamp ?: 0L),
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
            .map {
                ApiEntityMapper.collectionMapper(businessId).doForward(it)
            }
    }

    suspend fun getSupplierCollections(
        customerId: String?,
        timestamp: Long?,
        businessId: String,
    ): List<OnlinePayment> {
        return authorizedHttpClient.get<List<ApiCollection>>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/ListSupplierCollections",
            queryParams = mapOf(
                "customer_id" to (customerId ?: ""),
                "after" to (timestamp ?: 0L),
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().map {
            ApiEntityMapper.supplierCollectionMapper(businessId).doForward(it)
        }
    }

    suspend fun listSendMoneyTransactions(
        businessId: String,
        timestamp: Long?,
    ): List<OnlinePayment> {
        val response =
            authorizedHttpClient.post<ListSendMoneyTransactionsRequest, ListSendMoneyTransactionsResponse>(
                baseUrl = baseUrl,
                endPoint = "collection/v1/ListSendMoneyTransactions",
                requestBody = ListSendMoneyTransactionsRequest(
                    updateTime = timestamp,
                    businessId = businessId,
                ),
                headers = mapOf(HEADER_BUSINESS_ID to businessId),
            ).getOrThrow()

        return response.transactions.map {
            ApiEntityMapper.sendMoneyMapper(businessId).doForward(it)
        }
    }

    suspend fun getCollectionProfiles(businessId: String): CollectionProfiles {
        val response = authorizedHttpClient.post<Unit, MerchantCollectionProfileResponse>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/GetMerchantProfile",
            requestBody = Unit,
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
        if (response.destination == null) {
            throw CollectionServerErrors.AddressNotFound()
        }
        return ApiEntityMapper.COLLECTION_MERCHANT_PROFILE.doForward(response)
    }

    suspend fun getCollectionCustomerProfile(
        customerId: String,
        businessId: String,
    ): CollectionCustomerProfile {
        val response =
            authorizedHttpClient.post<CustomerCollectionProfileRequest, CustomerCollectionProfileResponse>(
                baseUrl = baseUrl,
                endPoint = "collection/v1/GetCustomerCollectionProfile",
                requestBody = CustomerCollectionProfileRequest(
                    merchant_id = businessId,
                    customer_id = customerId,
                ),
                headers = mapOf(HEADER_BUSINESS_ID to businessId),
            ).getOrThrow()
        return ApiEntityMapper.COLLECTION_CUSTOMER_PROFILE.doForward(
            response,
        )
    }

    suspend fun setActiveDestination(
        collectionMerchantProfile: CollectionMerchantProfile,
        async: Boolean,
        businessId: String,
    ): MerchantCollectionProfileResponse {
        val apiMessages = DestinationRequest(
            "",
            collectionMerchantProfile.name,
            collectionMerchantProfile.payment_address,
            collectionMerchantProfile.type,
        )

        try {
            val response =
                authorizedHttpClient.post<SetActiveDestinationRequest, MerchantCollectionProfileResponse>(
                    baseUrl = baseUrl,
                    endPoint = "collection/v1/SetActiveDestination",
                    requestBody = SetActiveDestinationRequest(
                        merchant_id = collectionMerchantProfile.merchant_id,
                        destination = apiMessages,
                        async = async,
                    ),
                    headers = mapOf(HEADER_BUSINESS_ID to businessId),
                )

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            }

            throw response.asError()
        } catch (error: ApiError) {
            if (error.code == 400) {
                when (error.message) {
                    CollectionConstants.INVALID_ACCOUNT_NUMBER -> {
                        throw CollectionServerErrors.InvalidAccountNumber()
                    }

                    CollectionConstants.INVALID_IFSC -> {
                        throw CollectionServerErrors.InvalidIFSCcode()
                    }
                }
            }

            throw error
        }
    }

    suspend fun validatePaymentAddress(
        payment_address_type: String,
        payment_address: String,
        businessId: String,
    ): Pair<Boolean, String> {
        try {
            val response =
                authorizedHttpClient.post<ValidatePaymentAddressRequest, ValidatePaymentAddressResponse>(
                    baseUrl = baseUrl,
                    endPoint = "collection/v1/ValidatePaymentAddress",
                    requestBody = ValidatePaymentAddressRequest(
                        payment_address_type,
                        payment_address,
                    ),
                    headers = mapOf(HEADER_BUSINESS_ID to businessId),
                )

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.valid) {
                    return response.body()!!.valid to response.body()!!.name!!
                } else {
                    throw CollectionServerErrors.InvalidAPaymentAddress()
                }
            }

            throw response.asError()
        } catch (error: ApiError) {
            if (error.code == 400) {
                when (error.message) {
                    CollectionConstants.INVALID_ACCOUNT_NUMBER -> {
                        throw CollectionServerErrors.InvalidAccountNumber()
                    }

                    CollectionConstants.INVALID_IFSC -> {
                        throw CollectionServerErrors.InvalidIFSCcode()
                    }

                    CollectionConstants.DAILY_LIMIT_EXCEEDED -> {
                        throw CollectionServerErrors.DailyLimitExceeded()
                    }
                }
            }

            throw error
        }
    }

    suspend fun getCollectionSupplierProfile(
        accountId: String,
        businessId: String,
    ): CollectionCustomerProfile {
        return ApiEntityMapper.COLLECTION_SUPPLIER_PROFILE.doForward(
            authorizedHttpClient.post<GetSupplierCollectionProfileRequest, SupplierCollectionProfileResponse>(
                baseUrl = baseUrl,
                endPoint = "collection/v1/GetSupplierCollectionProfile",
                requestBody = GetSupplierCollectionProfileRequest(accountId),
                headers = mapOf(HEADER_BUSINESS_ID to businessId),
            ).getOrThrow(),
        )
    }

    suspend fun getOnlinePaymentsList(
        startTime: Long,
        businessId: String,
    ): GetOnlinePaymentResponse {
        return authorizedHttpClient.post<GetOnlinePaymentsRequest, GetOnlinePaymentResponse>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/ListMerchantCollections",
            requestBody = GetOnlinePaymentsRequest(startTime, businessId),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun tagMerchantPaymentWithCustomer(
        customerId: String,
        paymentId: String,
        businessId: String,
    ) {
        authorizedHttpClient.post<TagMerchantPaymentRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/TagMerchantPayment",
            requestBody = TagMerchantPaymentRequest(customerId, paymentId),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun setPaymentOutDestination(
        accountId: String,
        accountType: String,
        paymentType: String,
        paymentAddress: String,
        businessId: String,
    ) {
        try {
            val response =
                authorizedHttpClient.post<SetPaymentOutDestinationRequest, SetPaymentOutDestinationResponse>(
                    baseUrl = baseUrl,
                    endPoint = "collection/v1/SetPaymentOutDestination",
                    requestBody = SetPaymentOutDestinationRequest(
                        account_id = accountId,
                        destination = PaymentOutDestination(
                            type = paymentType,
                            paymentAddress = paymentAddress,
                        ),
                        accountType = accountType,
                    ),
                    headers = mapOf(HEADER_BUSINESS_ID to businessId),
                )
            if (response.isSuccessful.not()) {
                throw response.asError()
            }
        } catch (error: ApiError) {
            if (error.code == 400) {
                when (error.message) {
                    CollectionConstants.INVALID_ACCOUNT_NUMBER -> {
                        throw CollectionServerErrors.InvalidAccountNumber()
                    }

                    CollectionConstants.INVALID_IFSC -> {
                        throw CollectionServerErrors.InvalidIFSCcode()
                    }

                    CollectionConstants.INVALID_PAYMENT_ADDRESS -> {
                        throw CollectionServerErrors.InvalidAPaymentAddress()
                    }
                }
            }
            throw error
        }
    }

    suspend fun setPaymentTag(timestamp: Long, businessId: String) {
        authorizedHttpClient.post<PaymentTagRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/PaymentTags",
            requestBody = PaymentTagRequest(
                tags = TagRequest("true"),
                timestamp = timestamp,
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun getTransactionStatus(
        transactionId: String,
        type: String,
        businessId: String,
    ): GetTransactionStatusResponse {
        return authorizedHttpClient.post<GetTransactionStatusRequest, GetTransactionStatusResponse>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/GetCollectionTransactionStatus",
            requestBody = GetTransactionStatusRequest(
                transactionId = transactionId,
                type = type,
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun getSupplierCollectionProfiles(businessId: String): SupplierCollectionProfilesResponse {
        return authorizedHttpClient.post<Unit, SupplierCollectionProfilesResponse>(
            baseUrl = baseUrl,
            endPoint = "collection/v1/ListAllSupplierProfiles",
            requestBody = Unit,
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }
}
