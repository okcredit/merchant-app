package tech.okcredit.collection.local

import tech.okcredit.collection.local.database.CollectionProfile
import tech.okcredit.collection.local.database.CustomerCollectionProfile
import tech.okcredit.collection.local.database.OnlinePaymentEntity
import tech.okcredit.collection.local.database.SupplierCollectionProfile
import tech.okcredit.collection.model.CollectionCustomerProfile
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.KycStatus
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.remote.Converter

object DbEntityMapper {

    fun onlinePaymentEntityConverter(): Converter<OnlinePayment, OnlinePaymentEntity> =
        object : Converter<OnlinePayment, OnlinePaymentEntity> {
            override fun doForward(api: OnlinePayment): OnlinePaymentEntity {
                return OnlinePaymentEntity(
                    id = api.id,
                    createdTime = api.createTime,
                    updatedTime = api.updateTime,
                    status = api.status,
                    amount = api.amount,
                    accountId = api.accountId,
                    paymentId = api.paymentId,
                    errorCode = api.errorCode,
                    errorDescription = api.errorDescription,
                    businessId = api.businessId,
                    paymentMode = api.paymentMode ?: "",
                    paymentSource = api.paymentSource ?: "",
                    type = api.type,
                    payoutTo = api.payoutTo,
                    payoutDestination = api.payoutDestination,
                    platformFee = api.platformFee,
                    discount = api.discount,
                    estimatedSettlementTime = api.estimatedSettlementTime,
                    read = api.read,
                    paymentFrom = api.paymentFrom,
                    paymentUtr = api.paymentUtr,
                    payoutUtr = api.payoutUtr,
                    surcharge = api.surcharge,
                )
            }

            override fun doBackward(b: OnlinePaymentEntity): OnlinePayment {
                return OnlinePayment(
                    id = b.id,
                    createTime = b.createdTime,
                    updateTime = b.updatedTime,
                    status = b.status,
                    amount = b.amount,
                    accountId = b.accountId,
                    paymentId = b.paymentId,
                    errorCode = b.errorCode,
                    errorDescription = b.errorDescription,
                    surcharge = b.surcharge,
                    type = b.type,
                    paymentFrom = b.paymentFrom,
                    paymentUtr = b.paymentUtr,
                    payoutUtr = b.payoutUtr,
                    read = b.read,
                    paymentSource = b.paymentSource,
                    paymentMode = b.paymentMode,
                    businessId = b.businessId,
                    payoutTo = b.payoutTo,
                    payoutDestination = b.payoutDestination,
                    platformFee = b.platformFee,
                    estimatedSettlementTime = b.estimatedSettlementTime,
                    discount = b.discount,
                )
            }
        }

    val collectionMerchantProfile: Converter<CollectionMerchantProfile, CollectionProfile> =
        object :
            Converter<CollectionMerchantProfile, CollectionProfile> {
            override fun doForward(api: CollectionMerchantProfile): CollectionProfile {
                return CollectionProfile(
                    businessId = api.merchant_id,
                    name = api.name,
                    payment_address = api.payment_address,
                    type = api.type,
                    merchant_vpa = api.merchant_vpa,
                    limit_type = api.limitType,
                    kyc_limit = api.limit,
                    remaining_limit = api.remainingLimit,
                    merchant_qr_enabled = api.merchantQrEnabled,
                    merchantLink = api.merchantLink,
                    qrIntent = api.qrIntent,
                    kycStatus = api.kycStatus,
                    riskCategory = api.riskCategory,
                )
            }

            override fun doBackward(b: CollectionProfile): CollectionMerchantProfile {
                return CollectionMerchantProfile(
                    merchant_id = b.businessId,
                    name = b.name,
                    payment_address = b.payment_address,
                    type = b.type,
                    merchant_vpa = b.merchant_vpa,
                    limit = b.kyc_limit,
                    limitType = b.limit_type,
                    remainingLimit = b.remaining_limit,
                    merchantQrEnabled = b.merchant_qr_enabled,
                    merchantLink = b.merchantLink,
                    qrIntent = b.qrIntent,
                    kycStatus = b.kycStatus ?: KycStatus.NOT_SET.name,
                    riskCategory = b.riskCategory ?: "",
                )
            }
        }

    fun customerCollectionProfileConverter(businessId: String): Converter<CollectionCustomerProfile, CustomerCollectionProfile> =
        object :
            Converter<CollectionCustomerProfile, CustomerCollectionProfile> {
            override fun doForward(api: CollectionCustomerProfile): CustomerCollectionProfile {
                return CustomerCollectionProfile(
                    customerId = api.accountId,
                    messageLink = api.message_link,
                    qrIntent = api.qr_intent,
                    linkId = api.linkId,
                    businessId = businessId,
                )
            }

            override fun doBackward(b: CustomerCollectionProfile): CollectionCustomerProfile {
                return CollectionCustomerProfile(
                    accountId = b.customerId,
                    message_link = b.messageLink,
                    qr_intent = b.qrIntent,
                    linkId = b.linkId,
                )
            }
        }

    fun supplierProfileConverter(businessId: String): Converter<CollectionCustomerProfile, SupplierCollectionProfile> =
        object :
            Converter<CollectionCustomerProfile, SupplierCollectionProfile> {
            override fun doForward(api: CollectionCustomerProfile): SupplierCollectionProfile {
                return SupplierCollectionProfile(
                    accountId = api.accountId,
                    messageLink = api.message_link,
                    name = api.name,
                    type = api.type,
                    paymentAddress = api.paymentAddress,
                    linkId = api.linkId,
                    destinationUpdateAllowed = api.destinationUpdateAllowed,
                    businessId = businessId,
                )
            }

            override fun doBackward(b: SupplierCollectionProfile): CollectionCustomerProfile {
                return CollectionCustomerProfile(
                    accountId = b.accountId,
                    message_link = b.messageLink,
                    name = b.name,
                    type = b.type,
                    paymentAddress = b.paymentAddress,
                    linkId = b.linkId,
                    destinationUpdateAllowed = b.destinationUpdateAllowed,
                )
            }
        }
}
