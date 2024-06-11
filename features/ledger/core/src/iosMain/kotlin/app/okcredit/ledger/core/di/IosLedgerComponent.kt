package app.okcredit.ledger.core.di

import app.okcredit.ledger.core.syncer.CustomerSyncer
import app.okcredit.ledger.core.syncer.IosCustomerSyncer
import app.okcredit.ledger.core.syncer.IosSupplierSyncer
import app.okcredit.ledger.core.syncer.IosTransactionSyncer
import app.okcredit.ledger.core.syncer.SupplierSyncer
import app.okcredit.ledger.core.syncer.TransactionSyncer
import me.tatarka.inject.annotations.Provides

interface IosLedgerComponent : LedgerComponent {

    @Provides
    fun IosLedgerDriverFactory.bind(): LedgerDriverFactory = this

    @Provides
    fun IosTransactionSyncer.bind(): TransactionSyncer = this

    @Provides
    fun IosCustomerSyncer.bind(): CustomerSyncer = this

    @Provides
    fun IosSupplierSyncer.bind(): SupplierSyncer = this
}
