package app.okcredit.ledger.core.remote

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.CustomerStatus
import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.core.remote.models.AddCustomerRequest
import app.okcredit.ledger.core.remote.models.AddSupplierRequest
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.remote.models.ApiTransaction
import app.okcredit.ledger.core.remote.models.GetFileIdForSyncTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionAmountHistoryRequest
import app.okcredit.ledger.core.remote.models.GetTransactionFileRequest
import app.okcredit.ledger.core.remote.models.GetTransactionsRequest
import app.okcredit.ledger.core.remote.models.GetTransactionsResponse
import app.okcredit.ledger.core.remote.models.SupplierRequestForUpdate
import app.okcredit.ledger.core.remote.models.SyncTransactionRequest
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.remote.models.TransactionAmountHistory
import app.okcredit.ledger.core.remote.models.TransactionFile
import app.okcredit.ledger.core.remote.models.TransactionsRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierRequest
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import okcredit.base.network.getOrThrow
import okcredit.base.units.paisa
import okcredit.base.units.timestamp

@Inject
@Singleton
class LedgerRemoteSource(
    private val lazyApiClient: Lazy<LedgerApiClient>,
) {

    private val apiClient by lazy { lazyApiClient.value }

    suspend fun syncTransactions(
        request: SyncTransactionRequest,
        businessId: String,
        flowId: String,
    ): SyncTransactionResponse? {
        return apiClient.syncTransactions(
            request = request,
            businessId = businessId,
            flowId = flowId,
        ).getOrThrow()
    }

    suspend fun getTransactions(
        startDate: Long?,
        source: String,
        businessId: String,
        customerId: String? = null,
        type: Int = 0,
    ): GetTransactionsResponse? {
        return apiClient.getTransactions(
            request = GetTransactionsRequest(
                TransactionsRequest(
                    type = type,
                    role = 1,
                    accountId = customerId,
                    startTimeMs = startDate,
                ),
            ),
            source = "core_module_$source",
            businessId = businessId,
        ).getOrThrow()
    }

    suspend fun getTransactionsProtoFileUrl(
        transactionFileId: String,
        businessId: String,
    ): TransactionFile? {
        return apiClient.getTransactionFile(
            GetTransactionFileRequest(transactionFileId),
            businessId,
        )
            .getOrThrow()
            .takeIf { it.txnFile.status == 1 }
            ?.txnFile
    }

    suspend fun getTransactionAmountHistory(
        transactionId: String,
        businessId: String,
    ): TransactionAmountHistory? {
        return apiClient.getTransactionAmountHistory(
            GetTransactionAmountHistoryRequest(
                transactionId,
            ),
            businessId,
        ).getOrThrow()?.transaction
    }

    suspend fun addCustomer(
        name: String,
        mobile: String?,
        reactivate: Boolean,
        businessId: String,
    ): Customer {
        val response = apiClient.addCustomer(
            request = AddCustomerRequest(
                mobile = mobile,
                description = name,
                reactivate = reactivate,
                profileImage = null,
            ),
            businessId = businessId,
        )

        return response.getOrThrow()?.toDomainCustomer(businessId)
            ?: throw IllegalStateException("Add customer failed")
    }

    suspend fun listCustomers(
        businessId: String,
    ): List<Customer> {
        return apiClient.listAllCustomers(
            mobile = null,
            deleted = false,
            businessId = businessId,
        ).getOrThrow()
            .map {
                it.toDomainCustomer(businessId)
            }
    }

    suspend fun deleteCustomer(
        customerId: String,
        businessId: String,
    ) {
        apiClient.deleteCustomer(
            customerId = customerId,
            businessId = businessId,
        ).getOrThrow()
    }

    suspend fun addSupplier(
        businessId: String,
        name: String,
        mobile: String?,
    ): Supplier {
        val response = apiClient.addSupplier(
            request = AddSupplierRequest(
                mobile = mobile,
                name = name,
                profileImage = null,
            ),
            businessId = businessId,
        )

        return response.getOrThrow()?.toDomainSupplier(businessId)
            ?: throw IllegalStateException("Add supplier failed")
    }

    suspend fun listSuppliers(
        businessId: String,
    ): List<Supplier> {
        return apiClient.listAllSuppliers(
            businessId = businessId,
        ).getOrThrow().suppliers.map {
            it.toDomainSupplier(businessId)
        }
    }

    suspend fun deleteSupplier(
        supplierId: String,
        businessId: String,
    ) {
        apiClient.deleteSupplier(
            supplierId = supplierId,
            businessId = businessId,
        ).getOrThrow()
    }

    suspend fun updateSupplier(
        supplierId: String,
        name: String,
        mobile: String?,
        address: String?,
        profileImage: String?,
        lang: String?,
        txnAlertEnabled: Boolean,
        state: Int,
        updateTxnAlertEnabled: Boolean,
        displayTxnAlertSetting: Boolean,
        updateDisplayTxnAlertSetting: Boolean,
        isForUpdateState: Boolean,
        updatedAt: Long,
        businessId: String,
    ): Supplier {
        val request = SupplierRequestForUpdate(
            id = supplierId,
            name = name,
            mobile = mobile,
            address = address,
            profileImage = profileImage,
            lang = lang,
            txnAlertEnabled = txnAlertEnabled,
            state = state,
            displayTxnAlertSetting = displayTxnAlertSetting,
        )

        val response = apiClient.updateSupplier(
            supplierId,
            UpdateSupplierRequest(
                supplier = request,
                updateTxnAlertEnabled = updateTxnAlertEnabled,
                updateDisplayTxnAlertSetting = updateDisplayTxnAlertSetting,
                state = state,
                updateState = isForUpdateState,
                updateTime = updatedAt
            ),
            businessId
        )

        return response.getOrThrow().supplier.toDomainSupplier(businessId)
    }
}

private fun ApiCustomer.toDomainCustomer(businessId: String): Customer {
    return Customer(
        businessId = businessId,
        id = this.id,
        status = CustomerStatus.from(this.status),
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
    )
}
