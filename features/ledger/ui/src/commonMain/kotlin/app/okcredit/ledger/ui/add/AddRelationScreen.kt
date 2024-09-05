package app.okcredit.ledger.ui.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.ui.LedgerScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.moveTo
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

data class AddRelationScreen(val accountType: AccountType) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<AddRelationScreenModel>()

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        AddRelationScreenUi(
            state = state,
            accountType = accountType,
            onBackClicked = {
                navigator.pop()
            },
            onSubmitted = { name, mobile ->
                screenModel.pushIntent(
                    AddRelationContract.Intent.OnSubmitClicked(
                        name = name,
                        mobile = mobile,
                        accountType = accountType,
                    ),
                )
            },
            onViewLedgerClicked = { accountId, accountType ->
                navigator.pop()
                navigator.moveTo(
                    if (accountType == AccountType.CUSTOMER) {
                        LedgerScreenRegistry.CustomerLedger(customerId = accountId)
                    } else {
                        LedgerScreenRegistry.SupplierLedger(supplierId = accountId)
                    },
                )
            },
            onDismissDialog = {
                screenModel.pushIntent(AddRelationContract.Intent.OnDismissDialog)
            },
        )

        screenModel.observeViewEvents {
            when (it) {
                is AddRelationContract.ViewEvent.Success -> {
                    navigator.pop()
                    navigator.moveTo(
                        if (accountType == AccountType.CUSTOMER) {
                            LedgerScreenRegistry.CustomerLedger(customerId = it.accountId)
                        } else {
                            LedgerScreenRegistry.SupplierLedger(supplierId = it.accountId)
                        },
                    )
                }
            }
        }
    }
}
