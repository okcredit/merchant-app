package app.okcredit.ledger.core.remote

import app.okcredit.ledger.core.remote.models.AddCustomerRequest
import app.okcredit.ledger.core.remote.models.AddSupplierRequest
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.remote.models.GetFileIdForSyncTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryResponse
import app.okcredit.ledger.core.remote.models.GetTransactionFileIdResponse
import app.okcredit.ledger.core.remote.models.GetTransactionFileRequest
import app.okcredit.ledger.core.remote.models.GetTransactionFileResponse
import app.okcredit.ledger.core.remote.models.GetTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionsResponse
import app.okcredit.ledger.core.remote.models.SetRemindersApiRequest
import app.okcredit.ledger.core.remote.models.SuppliersResponse
import app.okcredit.ledger.core.remote.models.SyncTransactionRequest
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.remote.models.UpdateCustomerRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
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
    suspend fun getTransactionFile(
        @Body req: GetTransactionFileRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetTransactionFileResponse>

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

    @PUT("ledger/v1.0/customers/reminders")
    suspend fun setReminderTimeForCustomers(
        @Body request: SetRemindersApiRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )

    @GET("ledger/v1.0/customer")
    suspend fun listAllCustomers(
        @Query("mobile") mobile: String?,
        @Query("deleted") deleted: Boolean,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<List<ApiCustomer>>

    @POST("ledger/v1.0/sc/suppliers")
    suspend fun addSupplier(
        @Body request: AddSupplierRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<ApiSupplier?>

    @PATCH("ledger/v1.0/sc/suppliers/{supplier_id}")
    suspend fun updateSupplier(
        @Path("supplier_id") supplierId: String,
        @Body req: UpdateSupplierRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<UpdateSupplierResponse>

    // delete Supplier
    @DELETE("ledger/v1.0/sc/suppliers/{supplier_id}")
    suspend fun deleteSupplier(
        @Path("supplier_id") supplierId: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<Unit>

    @GET("ledger/v1.0/sc/suppliers")
    suspend fun listAllSuppliers(
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<SuppliersResponse>

    companion object {
        const val OKC_SYNC_ID_HEADER = "OKC-SYNC-ID"
    }
}
