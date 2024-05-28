package app.okcredit.ledger.core.di

import app.okcredit.ledger.contract.Ledger
import app.okcredit.ledger.contract.usecase.GetAllCustomers
import app.okcredit.ledger.contract.usecase.GetAllSuppliers
import app.okcredit.ledger.contract.usecase.GetTransactionsForAccount
import app.okcredit.ledger.core.LedgerImpl
import app.okcredit.ledger.core.local.LedgerSqlDriver
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.usecase.GetAllCustomersImpl
import app.okcredit.ledger.core.usecase.GetAllSuppliersImpl
import app.okcredit.ledger.core.usecase.GetCustomerTransactionsImpl
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory

typealias LedgerDriverFactory = SqlDriverFactory

interface LedgerComponent {

    @Provides
    fun ledgerApiClient(ktorfit: Ktorfit): LedgerApiClient {
        return ktorfit.create()
    }

    @Provides
    fun sqlDriver(factory: LedgerDriverFactory): LedgerSqlDriver {
        return factory.createDriver()
    }

    @Provides
    fun LedgerImpl.bind(): Ledger = this

    @Provides
    fun GetAllCustomersImpl.bind(): GetAllCustomers = this

    @Provides
    fun GetAllSuppliersImpl.bind(): GetAllSuppliers = this

    @Provides
    fun GetCustomerTransactionsImpl.bind(): GetTransactionsForAccount = this
}
