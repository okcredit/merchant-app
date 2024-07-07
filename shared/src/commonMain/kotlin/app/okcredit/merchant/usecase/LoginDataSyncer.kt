package app.okcredit.merchant.usecase

import app.okcredit.ledger.core.syncer.LedgerSyncManager
import kotlinx.coroutines.flow.firstOrNull
import me.tatarka.inject.annotations.Inject
import tech.okcredit.ab.AbDataSyncManager
import tech.okcredit.customization.syncer.CustomizationSyncManager
import tech.okcredit.identity.contract.usecase.GetIndividual

@Inject
class LoginDataSyncer(
    private val getIndividual: GetIndividual,
    private val abDataSyncManager: AbDataSyncManager,
    private val ledgerSyncManager: LedgerSyncManager,
    private val customizationSyncManager: CustomizationSyncManager,
) {

    suspend fun execute() {
        val individual = getIndividual.execute().firstOrNull() ?: return
        individual.businessIds.forEach { businessId ->
            syncDataForBusiness(businessId)
        }
    }

    private suspend fun syncDataForBusiness(businessId: String) {
        abDataSyncManager.syncProfile(businessId, "LoginDataSyncer")
        ledgerSyncManager.syncAllCustomers(businessId, "LoginDataSyncer")
        ledgerSyncManager.syncAllSuppliers(businessId, "LoginDataSyncer")
        ledgerSyncManager.syncAllCustomerTransactions(businessId, "LoginDataSyncer")
        customizationSyncManager.syncCustomization(businessId)
    }
}
