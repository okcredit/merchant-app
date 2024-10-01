package app.okcredit.ledger.core.di

import app.okcredit.ledger.core.syncer.CustomerSyncer
import app.okcredit.ledger.core.syncer.IosCustomerSyncer
import app.okcredit.ledger.core.syncer.IosSupplierSyncer
import app.okcredit.ledger.core.syncer.IosTransactionSyncer
import app.okcredit.ledger.core.syncer.SupplierSyncer
import app.okcredit.ledger.core.syncer.TransactionSyncer
import app.okcredit.ledger.local.LedgerDatabase
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface IosLedgerComponent : LedgerComponent {

    @Provides
    fun provideDriverFactory(): LedgerDriverFactory {
        return IosSqlDriverFactory(LedgerDatabase.Schema, "okcredit_ledger.db")
    }

    @Provides
    fun IosTransactionSyncer.bind(): TransactionSyncer = this

    @Provides
    fun IosCustomerSyncer.bind(): CustomerSyncer = this

    @Provides
    fun IosSupplierSyncer.bind(): SupplierSyncer = this
}
