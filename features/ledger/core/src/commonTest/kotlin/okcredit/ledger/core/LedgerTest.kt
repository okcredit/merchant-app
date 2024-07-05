package okcredit.ledger.core

import app.okcredit.ledger.contract.Ledger
import app.okcredit.ledger.core.LedgerImpl
import app.okcredit.ledger.core.usecase.AddAccount
import app.okcredit.ledger.core.usecase.DeleteCustomer
import app.okcredit.ledger.core.usecase.DeleteTransaction
import kotlin.test.BeforeTest

class LedgerTest {

    private lateinit var ledger: Ledger

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        ledger = LedgerImpl(
            recordTransactionLazy = lazyOf(ledgerTestHelper.recordTransaction),
            addAccountLazy = lazyOf(
                AddAccount(
                    getActiveBusinessIdLazy = lazyOf(ledgerTestHelper.getActiveBusinessId),
                    customerRepositoryLazy = lazyOf(ledgerTestHelper.customerRepository),
                    supplierRepositoryLazy = lazyOf(ledgerTestHelper.supplierRepository),
                ),
            ),
            deleteCustomerLazy = lazyOf(
                DeleteCustomer(
                    repository = ledgerTestHelper.customerRepository,
                ),
            ),
            deleteTransactionLazy = lazyOf(
                DeleteTransaction(
                    ledgerLocalSource = ledgerTestHelper.ledgerLocalSource,
                    ledgerSyncManager = ledgerTestHelper.ledgerSyncManager,
                ),
            ),
        )
    }
}
