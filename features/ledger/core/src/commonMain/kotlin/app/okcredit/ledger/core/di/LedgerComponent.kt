package app.okcredit.ledger.core.di

import app.okcredit.ledger.contract.Ledger
import app.okcredit.ledger.contract.usecase.GetAccountStatement
import app.okcredit.ledger.contract.usecase.GetAccounts
import app.okcredit.ledger.core.LedgerImpl
import app.okcredit.ledger.core.local.LedgerSqlDriver
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.core.usecase.GetAccountsImpl
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
    fun GetAccountsImpl.bind(): GetAccounts = this

    @Provides
    fun GetAccountStatementImpl.bind(): GetAccountStatement = this
}
