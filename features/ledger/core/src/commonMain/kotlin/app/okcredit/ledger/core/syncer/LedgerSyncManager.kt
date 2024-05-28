package app.okcredit.ledger.core.syncer

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toJsonObject

typealias TransactionSyncer = OneTimeDataSyncer
typealias CustomerSyncer = OneTimeDataSyncer
typealias SupplierSyncer = OneTimeDataSyncer

@Inject
class LedgerSyncManager(
    private val transactionSyncer: TransactionSyncer,
    private val customerSyncer: CustomerSyncer,
    private val supplierSyncer: SupplierSyncer,
) {
    companion object {
        const val BUSINESS_ID = "business_id"
        const val SOURCE = "source"
    }


    fun scheduleTransactionSync(businessId: String, source: String? = null) {
        transactionSyncer.schedule(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

    fun scheduleCustomerSync(businessId: String, source: String? = null) {
        customerSyncer.schedule(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

    fun scheduleSupplierSync(businessId: String, source: String? = null) {
        supplierSyncer.schedule(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

    suspend fun executeCustomerTransactionSync(businessId: String, source: String? = null) {
        transactionSyncer.execute(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

    suspend fun executeCustomerSync(businessId: String, source: String? = null) {
        customerSyncer.execute(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

    suspend fun executeSupplierSync(businessId: String, source: String? = null) {
        supplierSyncer.execute(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }

}