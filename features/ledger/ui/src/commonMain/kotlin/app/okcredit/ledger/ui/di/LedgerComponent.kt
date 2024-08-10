package app.okcredit.ledger.ui.di

import app.okcredit.ledger.ui.customer.CustomerLedgerModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair


interface LedgerComponent {

    @Provides
    @IntoMap
    fun customerScreenModel(customerScreenModel: CustomerLedgerModel): ScreenModelPair {
        return CustomerLedgerModel::class to customerScreenModel
    }
}
