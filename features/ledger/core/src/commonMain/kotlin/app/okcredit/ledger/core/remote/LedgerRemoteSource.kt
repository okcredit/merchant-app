package app.okcredit.ledger.core.remote

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.AccountStatus
import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.core.remote.models.AddCustomerRequest
import app.okcredit.ledger.core.remote.models.AddSupplierRequest
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryResponse
import app.okcredit.ledger.core.remote.models.GetTransactionFileRequest
import app.okcredit.ledger.core.remote.models.GetTransactionFileResponse
import app.okcredit.ledger.core.remote.models.GetTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionsResponse
import app.okcredit.ledger.core.remote.models.SuppliersResponse
import app.okcredit.ledger.core.remote.models.SyncTransactionRequest
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.remote.models.TransactionAmountHistory
import app.okcredit.ledger.core.remote.models.TransactionFile
import app.okcredit.ledger.core.remote.models.TransactionsRequest
import app.okcredit.ledger.core.remote.models.UpdateCustomerRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierResponse
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.di.Singleton
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.delete
import okcredit.base.network.get
import okcredit.base.network.getOrThrow
import okcredit.base.network.patch
import okcredit.base.network.post
import okcredit.base.network.put
import okcredit.base.units.paisa
import okcredit.base.units.timestamp

@Inject
@Singleton
class LedgerRemoteSource(
    private val baseUrl: BaseUrl,
    private val authorizedHttpClient: AuthorizedHttpClient,
) {

    companion object {
        const val OKC_SYNC_ID_HEADER = "OKC-SYNC-ID"
    }

    suspend fun syncTransactions(
        request: SyncTransactionRequest,
        businessId: String,
        flowId: String,
    ): SyncTransactionResponse? {
        return authorizedHttpClient.post<SyncTransactionRequest, SyncTransactionResponse?>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/sync-transactions",
            requestBody = request,
            headers = mapOf(HEADER_BUSINESS_ID to businessId, OKC_SYNC_ID_HEADER to flowId),
        ).getOrThrow()
    }

    suspend fun getTransactions(
        startDate: Long?,
        source: String,
        businessId: String,
        customerId: String? = null,
        type: Int = 0,
    ): GetTransactionsResponse? {
        return authorizedHttpClient.post<GetTransactionsRequest, GetTransactionsResponse?>(
            baseUrl = baseUrl,
            endPoint = "ledger/v2/GetTransactions",
            requestBody = GetTransactionsRequest(
                TransactionsRequest(
                    type = type,
                    role = 1,
                    accountId = customerId,
                    startTimeMs = startDate,
                ),
            ),
            headers = mapOf("X-Source" to "core_module_$source", HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun getTransactionsProtoFileUrl(
        transactionFileId: String,
        businessId: String,
    ): TransactionFile? {
        return authorizedHttpClient.post<GetTransactionFileRequest, GetTransactionFileResponse>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/new/GetTransactionFile",
            requestBody = GetTransactionFileRequest(transactionFileId),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
            .takeIf { it.txnFile.status == 1 }
            ?.txnFile
    }

    suspend fun getTransactionAmountHistory(
        transactionId: String,
        businessId: String,
    ): TransactionAmountHistory? {
        return authorizedHttpClient.post<GetTransactionAmountHistoryRequest, GetTransactionAmountHistoryResponse?>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/new/GetTxnAmountHistory",
            requestBody = GetTransactionAmountHistoryRequest(transactionId),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()?.transaction
    }

    suspend fun addCustomer(
        name: String,
        mobile: String?,
        reactivate: Boolean,
        businessId: String,
    ): Customer {
        val response = authorizedHttpClient.post<AddCustomerRequest, ApiCustomer?>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/customer",
            requestBody = AddCustomerRequest(
                mobile = mobile,
                description = name,
                reactivate = reactivate,
                profileImage = null,
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()

        return response?.toDomainCustomer(businessId)
            ?: throw IllegalStateException("Add customer failed")
    }

    suspend fun listCustomers(
        businessId: String,
    ): List<Customer> {
        return authorizedHttpClient.get<List<ApiCustomer>>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/customer",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
            queryParams = mapOf("deleted" to false),
        ).getOrThrow().map {
            it.toDomainCustomer(businessId)
        }
    }

    suspend fun deleteCustomer(
        customerId: String,
        businessId: String,
    ) {
        return authorizedHttpClient.delete<Unit>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/customer/$customerId",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun addSupplier(
        businessId: String,
        name: String,
        mobile: String?,
    ): Supplier {
        val response = authorizedHttpClient.post<AddSupplierRequest, ApiSupplier?>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/sc/suppliers",
            requestBody = AddSupplierRequest(
                mobile = mobile,
                name = name,
                profileImage = null,
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        )

        return response.getOrThrow()?.toDomainSupplier(businessId)
            ?: throw IllegalStateException("Add supplier failed")
    }

    suspend fun listSuppliers(
        businessId: String,
    ): List<Supplier> {
        return authorizedHttpClient.get<SuppliersResponse>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/sc/suppliers",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().suppliers.map {
            it.toDomainSupplier(businessId)
        }
    }

    suspend fun deleteSupplier(
        supplierId: String,
        businessId: String,
    ) {
        return authorizedHttpClient.delete<Unit>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/sc/suppliers/$supplierId",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun updateSupplier(
        supplierId: String,
        businessId: String,
        request: UpdateSupplierRequest
    ): Supplier {

        val response = authorizedHttpClient.patch<UpdateSupplierRequest, UpdateSupplierResponse>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/sc/suppliers/$supplierId",
            requestBody = request,
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()

        return response.supplier.toDomainSupplier(businessId)
    }

    suspend fun updateCustomer(
        businessId: String,
        customerId: String,
        request: UpdateCustomerRequest
    ): Customer {
        val response = authorizedHttpClient.put<UpdateCustomerRequest, ApiCustomer>(
            baseUrl = baseUrl,
            endPoint = "ledger/v1.0/customer/$customerId",
            requestBody = request,
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()

        return response.toDomainCustomer(businessId)
    }
}

private fun ApiCustomer.toDomainCustomer(businessId: String): Customer {
    return Customer(
        businessId = businessId,
        id = this.id,
        status = AccountStatus.from(this.status),
        accountUrl = this.accountUrl,
        name = this.description,
        createdAt = this.createdAt.timestamp,
        gstNumber = this.gstNumber,
        mobile = this.mobile,
        profileImage = this.profileImage,
        registered = this.registered,
        updatedAt = this.updatedAt.timestamp,
        settings = Customer.CustomerSettings(
            reminderMode = this.reminderMode,
            restrictContactSync = this.restrictContactSync,
            addTransactionRestricted = this.addTransactionRestricted,
            txnAlertEnabled = this.txnAlertEnabled,
            blockedByCustomer = this.blockedByCustomer,
        ),
        summary = Customer.CustomerSummary(
            balance = 0L.paisa,
            transactionCount = 0,
            lastActivity = createdAt.timestamp,
            lastActivityMetaInfo = 4,
        ),
        address = this.address
    )
}

private fun ApiSupplier.toDomainSupplier(businessId: String): Supplier {
    return Supplier(
        businessId = businessId,
        id = this.id,
        name = this.name,
        mobile = this.mobile,
        profileImage = this.profileImage,
        createdAt = this.createTime.timestamp,
        registered = this.registered,
        updatedAt = 0L.timestamp,
        settings = Supplier.SupplierSettings(
            addTransactionRestricted = this.addTransactionRestricted,
            txnAlertEnabled = this.txnAlertEnabled,
            blockedBySupplier = this.blockedBySupplier,
            lang = this.lang ?: "en",
        ),
        summary = Supplier.SupplierSummary(
            balance = 0L.paisa,
            transactionCount = 0,
            lastActivity = 0L.timestamp,
            lastActivityMetaInfo = 4,
        ),
        status = AccountStatus.from(this.state),
        address = this.address
    )
}
