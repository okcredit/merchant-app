package app.okcredit.staff_link.data.remote

import app.okcredit.staff_link.data.remote.request.ApproveCollectionListRequest
import app.okcredit.staff_link.data.remote.request.ApprovePendingTransactionRequest
import app.okcredit.staff_link.data.remote.request.CreateStaffLinkRequest
import app.okcredit.staff_link.data.remote.request.CreateSubscriptionRequest
import app.okcredit.staff_link.data.remote.request.GetActiveSubscriptionRequest
import app.okcredit.staff_link.data.remote.request.GetCollectionListBillDetailsRequest
import app.okcredit.staff_link.data.remote.request.GetCollectionListDetailRequest
import app.okcredit.staff_link.data.remote.request.UpdateCollectionListRequest
import app.okcredit.staff_link.data.remote.response.CreateStaffLinkResponse
import app.okcredit.staff_link.data.remote.response.CreateSubscriptionResponse
import app.okcredit.staff_link.data.remote.response.GetActiveSubscriptionResponse
import app.okcredit.staff_link.data.remote.response.GetCollectionListBillDetailsResponse
import app.okcredit.staff_link.data.remote.response.GetCollectionListResponse
import app.okcredit.staff_link.data.remote.response.GetSubscriptionPlansResponse
import app.okcredit.staff_link.data.remote.response.GetTransactionsForApprovalResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query
import app.okcredit.staff_link.data.remote.request.DeleteStaffLinkRequest
import de.jensklingenberg.ktorfit.Response
import merchant.okcredit.staff_link.data.remote.response.ApproveCollectionListResponse
import merchant.okcredit.staff_link.data.remote.response.GetCollectionListDetailResponse
import okcredit.base.network.HEADER_BUSINESS_ID

interface StaffLinkApiService {

    @POST("/collection/staff-link/v1/CreateCollectionList")
    suspend fun createStaffLink(
        @Body request: CreateStaffLinkRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): CreateStaffLinkResponse

    @GET("/collection/staff-link/v1/GetCollectionLists")
    suspend fun getCollectionLists(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int,
        @Query("usage_type") usageType: Int,
    ): GetCollectionListResponse

    @POST("/collection/staff-link/v1/UpdateCollectionList")
    suspend fun editStaffLink(
        @Body request: UpdateCollectionListRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )

    @POST("/collection/staff-link/v1/DeleteCollectionList")
    suspend fun deleteStaffLink(
        @Body request: DeleteStaffLinkRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String
    )

    @GET("/collection/staff-link/v1/GetTransactionsForApproval")
    suspend fun getPendingTransactionsForApproval(
        @Header(HEADER_BUSINESS_ID) businessId: String
    ): GetTransactionsForApprovalResponse

    @POST("/collection/staff-link/v1/UpdateTransaction")
    suspend fun approvePendingTransaction(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: ApprovePendingTransactionRequest,
    )

    @POST("payment/v1/RetrieveSubscription")
    suspend fun getActiveSubscription(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: GetActiveSubscriptionRequest,
    ): GetActiveSubscriptionResponse?

    @POST("payment/v1/GetPlans")
    suspend fun getSubscriptionsPlans(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: GetActiveSubscriptionRequest,
    ): GetSubscriptionPlansResponse

    @POST("payment/v1/CreateSubscription")
    suspend fun createSubscription(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: CreateSubscriptionRequest,
    ): Response<CreateSubscriptionResponse>

    @POST("/collection/staff-link/v1/GetCollectionListDetails")
    suspend fun getCollectionListDetails(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: GetCollectionListDetailRequest,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): GetCollectionListDetailResponse

    @PUT("/collection/staff-link/v1/UpdateMultipleTransactions")
    suspend fun bulkUpdateTransaction(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: ApproveCollectionListRequest,
        @Query("updated_by") updatedBy: String,
    ): ApproveCollectionListResponse

    @POST("/collection/staff-link/v2/GetCollectionListBillDetails")
    suspend fun getCollectionListBillDetails(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: GetCollectionListBillDetailsRequest,
    ): GetCollectionListBillDetailsResponse
}
