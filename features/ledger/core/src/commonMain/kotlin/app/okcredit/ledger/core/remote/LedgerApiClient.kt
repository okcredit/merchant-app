package app.okcredit.ledger.core.remote

import app.okcredit.ledger.core.remote.models.AddCustomerRequest
import app.okcredit.ledger.core.remote.models.AddSupplierRequest
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.remote.models.ApiTransaction
import app.okcredit.ledger.core.remote.models.GetFileIdForSyncTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryResponse
import app.okcredit.ledger.core.remote.models.GetTransactionFileIdResponse
import app.okcredit.ledger.core.remote.models.GetTransactionFileRequest
import app.okcredit.ledger.core.remote.models.GetTransactionFileResponse
import app.okcredit.ledger.core.remote.models.GetTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionsResponse
import app.okcredit.ledger.core.remote.models.SetRemindersApiRequest
import app.okcredit.ledger.core.remote.models.SyncTransactionRequest
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.remote.models.UpdateCustomerRequest
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import okcredit.base.network.HEADER_BUSINESS_ID

interface LedgerApiClient {

    @POST("ledger/v1.0/sync-transactions")
    suspend fun syncTransactions(
        @Body request: SyncTransactionRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Header(OKC_SYNC_ID_HEADER) flowId: String,
    ): Response<SyncTransactionResponse?>

    @POST("ledger/v2/GetTransactions")
    suspend fun getTransactions(
        @Body request: GetTransactionsRequest,
        @Header("X-Source") source: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetTransactionsResponse?>

    @POST("ledger/v1.0/new/GetTransactionsForLogin")
    suspend fun getFileIdForSyncTransactions(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Body request: GetFileIdForSyncTransactionsRequest,
    ): Response<GetTransactionFileIdResponse>

    @POST("ledger/v1.0/new/GetTransactionFile")
    fun getTransactionFile(
        @Body req: GetTransactionFileRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetTransactionFileResponse>

    @GET("ledger/v1.0/transaction/{tx_id}")
    suspend fun getTransaction(
        @Path("tx_id") transactionId: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiTransaction>

    @POST("ledger/v1.0/new/GetTxnAmountHistory")
    suspend fun getTransactionAmountHistory(
        @Body request: GetTransactionAmountHistoryRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetTransactionAmountHistoryResponse?>

    @POST("ledger/v1.0/customer")
    suspend fun addCustomer(
        @Body request: AddCustomerRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiCustomer?>

    @DELETE("ledger/v1.0/customer/{customer_id}")
    suspend fun deleteCustomer(
        @Path("customer_id") customerId: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<Unit?>

    @PUT("ledger/v1.0/customer/{customer_id}")
    suspend fun updateCustomer(
        @Path("customer_id") customerId: String,
        @Body request: UpdateCustomerRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiCustomer?>

    @Headers("Content-Type: application/json", "Content-Encoding: gzip")
    @PUT("ledger/v1.0/customers/reminders")
    suspend fun setReminderTimeForCustomers(
        @Body request: SetRemindersApiRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )

    @GET("ledger/v1.0/customer")
    suspend fun listCustomers(
        @Query("mobile") mobile: String?,
        @Query("deleted") deleted: Boolean,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<List<ApiCustomer>>

    @GET("ledger/v1.0/customer/{customer_id}")
    suspend fun getCustomer(
        @Path("customer_id") customerId: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiCustomer?>

    @POST("ledger/v1.0/sc/suppliers")
    suspend fun addSupplier(
        @Body request: AddSupplierRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiSupplier?>

    companion object {
        const val OKC_SYNC_ID_HEADER = "OKC-SYNC-ID"
    }
}
