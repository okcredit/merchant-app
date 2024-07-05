package app.okcredit.ledger.core.di

import android.content.Context
import androidx.work.ListenableWorker
import app.okcredit.ledger.core.syncer.AndroidCustomerSyncer
import app.okcredit.ledger.core.syncer.AndroidSupplierSyncer
import app.okcredit.ledger.core.syncer.AndroidTransactionSyncer
import app.okcredit.ledger.core.syncer.CustomerSyncer
import app.okcredit.ledger.core.syncer.SupplierSyncer
import app.okcredit.ledger.core.syncer.SyncCustomersWorker
import app.okcredit.ledger.core.syncer.SyncTransactionsWorker
import app.okcredit.ledger.core.syncer.TransactionSyncer
import app.okcredit.ledger.local.LedgerDatabase
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import okcredit.base.syncer.ChildWorkerFactory
import kotlin.reflect.KClass

interface AndroidLedgerComponent : LedgerComponent {

    @Provides
    fun provideDriverFactory(context: Context): LedgerDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = LedgerDatabase.Schema,
            name = "okcredit_ledger.db",
        )
    }

    @Provides
    fun AndroidTransactionSyncer.bind(): TransactionSyncer = this

    @Provides
    fun AndroidCustomerSyncer.bind(): CustomerSyncer = this

    @Provides
    fun AndroidSupplierSyncer.bind(): SupplierSyncer = this

    @Provides
    @IntoMap
    fun syncTransactionsWorker(factory: SyncTransactionsWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return SyncTransactionsWorker::class to factory
    }

    @Provides
    @IntoMap
    fun syncCustomersWorker(factory: SyncCustomersWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return SyncCustomersWorker::class to factory
    }
}
