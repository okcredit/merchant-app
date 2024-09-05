package app.okcredit.ledger.ui

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
    }
}


sealed class LedgerScreenRegistry : ScreenProvider {
    data class CustomerLedger(val customerId: String) : LedgerScreenRegistry()

    data class SupplierLedger(val supplierId: String) : LedgerScreenRegistry()
}