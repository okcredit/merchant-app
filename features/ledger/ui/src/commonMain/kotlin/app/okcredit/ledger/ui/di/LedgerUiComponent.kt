package app.okcredit.ledger.ui.di

import app.okcredit.ledger.ui.add.AddRelationScreenModel
import app.okcredit.ledger.ui.customer.CustomerLedgerModel
import app.okcredit.ledger.ui.delete.DeleteAccountModel
import app.okcredit.ledger.ui.profile.AccountProfileModel
import app.okcredit.ledger.ui.supplier.SupplierLedgerModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair

interface LedgerUiComponent {

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

    @Provides
    @IntoMap
    fun addRelationScreenModel(screenModel: AddRelationScreenModel): ScreenModelPair {
        return AddRelationScreenModel::class to screenModel
    }

    @Provides
    @IntoMap
    fun accountProfileMode(accountProfileModel: AccountProfileModel): ScreenModelPair {
        return AccountProfileModel::class to accountProfileModel
    }

    @Provides
    @IntoMap
    fun deleteAccountModel(deleteAccountModel: DeleteAccountModel): ScreenModelPair {
        return DeleteAccountModel::class to deleteAccountModel
    }
}
