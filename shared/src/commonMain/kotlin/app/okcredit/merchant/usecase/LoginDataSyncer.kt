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

    suspend fun execute(): Boolean {
        val individual = getIndividual.execute().firstOrNull() ?: return false
        individual.businessIds.forEach { businessId ->
            syncDataForBusiness(businessId)
        }

        return individual.businessIds.size > 1
    }

    private suspend fun syncDataForBusiness(businessId: String) {
        abDataSyncManager.syncProfile(businessId, "sync_screen")
        ledgerSyncManager.syncAllCustomers(businessId, "sync_screen")
        ledgerSyncManager.syncAllSuppliers(businessId, "sync_screen")
        ledgerSyncManager.syncAllCustomerTransactions(businessId, "sync_screen")
        customizationSyncManager.syncCustomization(businessId)
    }
}
