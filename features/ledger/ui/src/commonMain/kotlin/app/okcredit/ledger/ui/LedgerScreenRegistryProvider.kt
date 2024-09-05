package app.okcredit.ledger.ui

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.ui.add.AddRelationScreen
import app.okcredit.ledger.ui.customer.CustomerLedgerScreen
import app.okcredit.ledger.ui.supplier.SupplierLedgerScreen
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import me.tatarka.inject.annotations.Inject

@Inject
class LedgerScreenRegistryProvider {

    fun screenRegistry() = screenModule {
        register<LedgerScreenRegistry.CustomerLedger> {
            CustomerLedgerScreen(it.customerId)
        }

        register<LedgerScreenRegistry.SupplierLedger> {
            SupplierLedgerScreen(it.supplierId)
        }

        register<LedgerScreenRegistry.AddRelation> {
            AddRelationScreen(it.accountType)
        }
    }
}


sealed class LedgerScreenRegistry : ScreenProvider {
    data class CustomerLedger(val customerId: String) : LedgerScreenRegistry()

    data class SupplierLedger(val supplierId: String) : LedgerScreenRegistry()

    data class AddRelation(val accountType: AccountType) : LedgerScreenRegistry()
}