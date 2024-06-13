package app.okcredit.staff_link.data.remote

import app.okcredit.staff_link.data.remote.request.ApproveCollectionListRequest
import app.okcredit.staff_link.data.remote.request.CreateStaffLinkRequest
import app.okcredit.staff_link.data.remote.request.CreateSubscriptionRequest
import app.okcredit.staff_link.data.remote.request.DeleteCollectionList
import app.okcredit.staff_link.data.remote.request.DeleteStaffLinkRequest
import app.okcredit.staff_link.data.remote.request.GetActiveSubscriptionRequest
import app.okcredit.staff_link.data.remote.request.GetCollectionListBillDetailsRequest
import app.okcredit.staff_link.data.remote.request.GetCollectionListDetailRequest
import app.okcredit.staff_link.data.remote.request.PendingTransactionRequest
import app.okcredit.staff_link.data.remote.request.UpdateCollectionListRequest
import me.tatarka.inject.annotations.Inject
import merchant.okcredit.staff_link.data.remote.response.ApproveCollectionListResponse
import app.okcredit.staff_link.data.remote.response.CreateStaffLinkResponse
import app.okcredit.staff_link.data.remote.response.CreateSubscriptionResponse
import app.okcredit.staff_link.data.remote.response.GetActiveSubscriptionResponse
import app.okcredit.staff_link.data.remote.response.GetCollectionListBillDetailsResponse
import merchant.okcredit.staff_link.data.remote.response.GetCollectionListDetailResponse
import app.okcredit.staff_link.data.remote.response.GetCollectionListResponse
import app.okcredit.staff_link.data.remote.response.GetSubscriptionPlansResponse
import app.okcredit.staff_link.data.remote.response.GetTransactionsForApprovalResponse
import okcredit.base.network.ApiError
import okcredit.base.network.getOrThrow

@Inject
class StaffLinkRemoteSource(
    private val staffLinkApiService: Lazy<StaffLinkApiService>,
) {

    suspend fun createCustomerStaffLink(
        staffLinkRequest: CreateStaffLinkRequest,
        businessId: String,
    ): CreateStaffLinkResponse {
        return staffLinkApiService.value.createStaffLink(staffLinkRequest, businessId)
    }

    suspend fun getCollectionLists(businessId: String, usageType: Int): GetCollectionListResponse {
        return staffLinkApiService.value.getCollectionLists(
            businessId = businessId,
            pageNumber = 1,
            limit = 1000,
            usageType = usageType
        )
    }

    suspend fun deleteCollectionStaffLink(businessId: String, id: String) {
        staffLinkApiService.value.deleteStaffLink(
            request = DeleteStaffLinkRequest(
                DeleteCollectionList(id = id)
            ),
            businessId = businessId
        )
    }

    suspend fun editCollectionStaffLink(request: UpdateCollectionListRequest, businessId: String) {
        staffLinkApiService.value.editStaffLink(request, businessId)
    }

    suspend fun getPendingTransactionsForApproval(businessId: String): GetTransactionsForApprovalResponse {
        return staffLinkApiService.value.getPendingTransactionsForApproval(businessId)
    }

    suspend fun approvePendingTransaction(
        businessId: String,
        pendingTransactionId: String,
    ) {
        staffLinkApiService.value.bulkUpdateTransaction(
            businessId = businessId,
            request = ApproveCollectionListRequest(
                transactions = listOf(
                    PendingTransactionRequest(
                        id = pendingTransactionId,
                        approvedByMerchant = true,
                        businessId = businessId
                    )
                )
            ),
            updatedBy = "accountant"
        )
    }

    suspend fun getActiveSubscription(
        businessId: String,
        planName: String,
    ): GetActiveSubscriptionResponse? {
        return try {
            staffLinkApiService.value.getActiveSubscription(
                businessId = businessId,
                request = GetActiveSubscriptionRequest(
                    merchantId = businessId,
                    planName = planName
                )
            )
        } catch (httpException: ApiError) {
            null
        }
    }

    suspend fun getSubscriptionsPlans(businessId: String): GetSubscriptionPlansResponse {
        return staffLinkApiService.value.getSubscriptionsPlans(
            businessId = businessId,
            request = GetActiveSubscriptionRequest(
                merchantId = businessId,
            )
        )
    }

    suspend fun createSubscription(businessId: String, planId: String): CreateSubscriptionResponse {
        val response =  staffLinkApiService.value.createSubscription(
            businessId = businessId,
            request = CreateSubscriptionRequest(
                merchantId = businessId,
                planId = planId,
            )
        )

        return response.getOrThrow()
    }

    suspend fun getCollectionListDetails(
        businessId: String,
        collectionListId: String
    ): GetCollectionListDetailResponse {
        return staffLinkApiService.value.getCollectionListDetails(
            businessId = businessId,
            request = GetCollectionListDetailRequest(
                businessId = businessId,
                collectionListId = collectionListId
            ),
            pageNumber = 1,
            limit = 1000
        )
    }

    suspend fun getCollectionListBillDetails(
        businessId: String,
        collectionListId: String
    ): GetCollectionListBillDetailsResponse {
        return staffLinkApiService.value.getCollectionListBillDetails(
            businessId = businessId,
            request = GetCollectionListBillDetailsRequest(
                businessId = businessId,
                collectionListId = collectionListId
            )
        )
    }

    suspend fun approveAllTransactions(
        businessId: String,
        request: ApproveCollectionListRequest
    ): ApproveCollectionListResponse {
        return staffLinkApiService.value.bulkUpdateTransaction(
            businessId = businessId,
            request = request,
            updatedBy = "accountant"
        )
    }
}
