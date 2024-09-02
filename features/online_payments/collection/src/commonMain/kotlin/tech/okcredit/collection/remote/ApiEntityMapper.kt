package tech.okcredit.collection.remote

import tech.okcredit.collection.model.CollectionCustomerProfile
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.CollectionProfiles
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.model.OnlinePayment.Companion.PAYMENT_TYPE_MERCHANT_QR
import tech.okcredit.collection.remote.response.ApiCollection
import tech.okcredit.collection.remote.response.CollectionOnlinePaymentApi
import tech.okcredit.collection.remote.response.CustomerCollectionProfileResponse
import tech.okcredit.collection.remote.response.MerchantCollectionProfileResponse
import tech.okcredit.collection.remote.response.SupplierCollectionProfileResponse

object ApiEntityMapper {

    fun collectionMapper(businessId: String): Converter<ApiCollection, OnlinePayment> =
        object : Converter<ApiCollection, OnlinePayment> {
            override fun doForward(api: ApiCollection): OnlinePayment {
                return OnlinePayment(
                    id = api.id,
                    createTime = (api.create_time * 1000),
                    updateTime = (api.update_time * 1000),
                    status = api.status,
                    accountId = api.customer_id,
                    amount = api.amount_requested,
                    paymentId = api.paymentId,
                    paymentSource = api.paymentSource.removeQuotes(),
                    paymentMode = api.payment?.origin?.type ?: "",
                    type = if (api.merchantId != businessId) OnlinePayment.TYPE_PAID else OnlinePayment.TYPE_RECEIVED,
                    errorCode = api.errorCode ?: "",
                    errorDescription = api.errorDescription ?: "",
                    read = false,
                    surcharge = api.surcharge ?: 0L,
                    paymentFrom = api.paymentFrom.removeQuotes(),
                    paymentUtr = api.paymentUtr.removeQuotes(),
                    payoutUtr = api.payoutUtr?.removeQuotes(),
                    businessId = businessId,
                    payoutDestination = api.paymentDestination.removeQuotes(),
                    payoutTo = api.paymentTo.removeQuotes(),
                    platformFee = api.fee ?: 0L,
                    estimatedSettlementTime = api.labels?.get("estimated_settlement_time")?.toLongOrNull()?.times(1000L),
                    discount = api.discount ?: 0L,
                )
            }

            override fun doBackward(b: OnlinePayment): ApiCollection {
                throw RuntimeException("illegal operation: cannot convert ApiCollection domain entity to api entity")
            }
        }

    fun supplierCollectionMapper(businessId: String): Converter<ApiCollection, OnlinePayment> =
        object : Converter<ApiCollection, OnlinePayment> {
            override fun doForward(api: ApiCollection): OnlinePayment {
                return OnlinePayment(
                    id = api.id,
                    createTime = (api.create_time * 1000),
                    updateTime = (api.update_time * 1000),
                    status = api.status,
                    accountId = api.customer_id,
                    amount = api.amount_requested,
                    paymentId = api.paymentId,
                    paymentSource = api.paymentSource.removeQuotes(),
                    paymentMode = api.payment?.origin?.type ?: "",
                    type = if (api.merchantId == businessId) OnlinePayment.TYPE_PAID else OnlinePayment.TYPE_RECEIVED,
                    errorCode = api.errorCode ?: "",
                    errorDescription = api.errorDescription ?: "",
                    read = false,
                    surcharge = api.surcharge ?: 0,
                    paymentFrom = api.paymentFrom.removeQuotes(),
                    paymentUtr = api.paymentUtr.removeQuotes(),
                    payoutUtr = api.payoutUtr?.removeQuotes(),
                    payoutDestination = api.paymentDestination.removeQuotes(),
                    payoutTo = api.paymentTo.removeQuotes(),
                    businessId = businessId,
                )
            }

            override fun doBackward(b: OnlinePayment): ApiCollection {
                throw RuntimeException("illegal operation: cannot convert Collection domain entity to api entity")
            }
        }

    val COLLECTION_MERCHANT_PROFILE: Converter<MerchantCollectionProfileResponse, CollectionProfiles> =
        object :
            Converter<MerchantCollectionProfileResponse, CollectionProfiles> {
            override fun doForward(api: MerchantCollectionProfileResponse): CollectionProfiles {
                return CollectionProfiles(
                    CollectionMerchantProfile(
                        merchant_id = api.businessId.orEmpty(),
                        name = api.destination?.name,
                        payment_address = api.destination?.paymentAddress.orEmpty(),
                        type = api.destination?.type.orEmpty(),
                        merchant_vpa = api.merchantVpa,
                        limit = api.limit?.toLongOrNull() ?: 0L,
                        limitType = api.limitType,
                        remainingLimit = api.remainingLimit?.toLongOrNull() ?: 0L,
                        merchantQrEnabled = api.merchantQrEnabled ?: false,
                        merchantLink = api.merchantLink,
                        qrIntent = api.qrIntent,
                        kycStatus = api.kycStatus.orEmpty(),
                        riskCategory = api.riskCategory ?: "NO_RISK",
                    ),
                )
            }

            override fun doBackward(b: CollectionProfiles): MerchantCollectionProfileResponse {
                throw RuntimeException("illegal operation: cannot convert Collection domain entity to api entity")
            }
        }

    val COLLECTION_CUSTOMER_PROFILE: Converter<CustomerCollectionProfileResponse, CollectionCustomerProfile> =
        object : Converter<CustomerCollectionProfileResponse, CollectionCustomerProfile> {
            override fun doForward(api: CustomerCollectionProfileResponse): CollectionCustomerProfile {
                return CollectionCustomerProfile(
                    accountId = api.customerId ?: "",
                    message_link = api.profile?.messageLink,
                    link_intent = api.profile?.linkIntent,
                    qr_intent = api.profile?.qrIntent,
                    isSupplier = false,

                    // these properties are added for single list feature
                    linkVpa = api.destination?.upiVpa,
                    paymentAddress = api.destination?.paymentAddress,
                    type = api.destination?.type,
                    mobile = api.destination?.mobile,
                    name = api.destination?.name,
                    upiVpa = api.destination?.upiVpa,
                    fromMerchantPaymentLink = api.profile?.fromMerchantPaymentLink,
                    fromMerchantUpiIntent = api.profile?.fromMerchantUpiIntent,
                    linkId = api.profile?.linkId,
                )
            }

            override fun doBackward(b: CollectionCustomerProfile): CustomerCollectionProfileResponse {
                throw RuntimeException("illegal operation: cannot convert Collection domain entity to api entity")
            }
        }

    val COLLECTION_SUPPLIER_PROFILE: Converter<SupplierCollectionProfileResponse, CollectionCustomerProfile> =
        object : Converter<SupplierCollectionProfileResponse, CollectionCustomerProfile> {
            override fun doForward(api: SupplierCollectionProfileResponse): CollectionCustomerProfile {
                return CollectionCustomerProfile(
                    accountId = api.accountId ?: "",
                    message_link = api.supplierProfile?.messageLink,
                    link_intent = api.supplierProfile?.linkIntent,
                    isSupplier = true,
                    linkVpa = api.destination?.upiVpa,
                    paymentAddress = api.destination?.paymentAddress,
                    type = api.destination?.type,
                    mobile = api.destination?.mobile,
                    name = api.destination?.name,
                    upiVpa = api.destination?.upiVpa,
                    linkId = api.supplierProfile?.linkId,
                    destinationUpdateAllowed = api.destinationUpdateAllowed ?: true,
                )
            }

            override fun doBackward(b: CollectionCustomerProfile): SupplierCollectionProfileResponse {
                throw RuntimeException("illegal operation: cannot convert Collection domain entity to api entity ")
            }
        }

    fun onlinePaymentConverter(businessId: String): Converter<CollectionOnlinePaymentApi, OnlinePayment> =
        object : Converter<CollectionOnlinePaymentApi, OnlinePayment> {
            override fun doForward(api: CollectionOnlinePaymentApi): OnlinePayment {
                return OnlinePayment(
                    id = api.id,
                    createTime = (api.createdTime * 1000),
                    updateTime = (api.updatedTime * 1000),
                    status = api.status,
                    accountId = api.accountId,
                    amount = api.amount.toLong(),
                    paymentId = api.paymentId ?: "",
                    paymentSource = api.paymentSource.removeQuotes(),
                    paymentMode = api.paymentMode ?: "",
                    type = if (api.type.contains(PAYMENT_TYPE_MERCHANT_QR)) OnlinePayment.TYPE_RECEIVED else OnlinePayment.TYPE_LINK_RECEIVED,
                    errorCode = api.errorCode ?: "",
                    errorDescription = api.errorDescription ?: "",
                    read = false,
                    surcharge = api.surcharge ?: 0,
                    paymentFrom = api.paymentFrom.removeQuotes(),
                    paymentUtr = api.paymentUtr.removeQuotes(),
                    payoutUtr = api.payoutUtr?.removeQuotes(),
                    businessId = businessId,
                    payoutDestination = api.paymentDestination.removeQuotes(),
                    payoutTo = api.paymentTo.removeQuotes(),
                    estimatedSettlementTime = api.labels?.get("estimated_settlement_time")?.toLongOrNull()?.times(1000L),
                )
            }

            override fun doBackward(b: OnlinePayment): CollectionOnlinePaymentApi {
                throw RuntimeException("illegal operation: cannot convert OnlinePayment domain entity to api entity")
            }
        }

    fun sendMoneyMapper(businessId: String): Converter<CollectionOnlinePaymentApi, OnlinePayment> =
        object : Converter<CollectionOnlinePaymentApi, OnlinePayment> {
            override fun doForward(api: CollectionOnlinePaymentApi): OnlinePayment {
                return OnlinePayment(
                    id = api.id,
                    createTime = (api.createdTime * 1000),
                    updateTime = (api.updatedTime * 1000),
                    status = api.status,
                    accountId = api.accountId,
                    amount = api.amount.toLong(),
                    paymentId = api.paymentId ?: "",
                    paymentSource = api.paymentSource.removeQuotes(),
                    paymentMode = api.paymentMode ?: "",
                    type = OnlinePayment.TYPE_PAID,
                    errorCode = api.errorCode ?: "",
                    errorDescription = api.errorDescription ?: "",
                    read = false,
                    surcharge = api.surcharge ?: 0,
                    paymentFrom = api.paymentFrom.removeQuotes(),
                    paymentUtr = api.paymentUtr.removeQuotes(),
                    payoutUtr = api.payoutUtr?.removeQuotes(),
                    payoutDestination = api.paymentDestination.removeQuotes(),
                    payoutTo = api.paymentTo.removeQuotes(),
                    businessId = businessId,
                )
            }

            override fun doBackward(b: OnlinePayment): CollectionOnlinePaymentApi {
                throw RuntimeException("illegal operation: cannot convert Collection domain entity to api entity")
            }
        }
}

fun String?.removeQuotes() = this?.replace("\"", "")

interface Converter<Input, Output> {
    fun doForward(api: Input): Output
    fun doBackward(b: Output): Input
}
