package app.okcredit.ledger.ui.di

import app.okcredit.ledger.ui.customer.CustomerLedgerModel
import app.okcredit.ledger.ui.supplier.SupplierLedgerModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair


interface LedgerComponent {

    @Provides
    @IntoMap
    fun customerScreenModel(customerScreenModel: CustomerLedgerModel): ScreenModelPair {
        return CustomerLedgerModel::class to customerScreenModel
    }

    @Provides
    @IntoMap
    fun supplierScreenModel(supplierScreenModel: SupplierLedgerModel): ScreenModelPair {
        return SupplierLedgerModel::class to supplierScreenModel
    }

}
