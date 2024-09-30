package app.okcredit.ledger.ui.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.isSupplier
import app.okcredit.ledger.ui.delete.composable.DeleteScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

data class DeleteAccountScreen(
    val accountId: String,
    val accountType: AccountType,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<DeleteAccountModel>()

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state, navigator)
    }

    @Composable
    private fun render(
        screenModel: DeleteAccountModel,
        state: DeleteAccountContract.State,
        navigator: Navigator
    ) {
        DeleteScreen(
            loadDetails = {
                screenModel.pushIntent(
                    DeleteAccountContract.Intent.Load(
                        accountId = accountId,
                        accountType = accountType
                    )
                )
            },
            name = state.name,
            balance = state.balance.value,
            onBackClicked = { navigator.pop() },
            onDeleteClicked = {
                screenModel.pushIntent(DeleteAccountContract.Intent.DeleteRelationshipClicked(accountId, accountType))
            },
            isSupplier = accountType.isSupplier(),
            onSettlementClicked = {},
            mobile = state.mobile,
            isLoading = state.isLoading,
            shouldSettle = state.shouldSettle
        )
    }

    private fun handleViewEvent(event: DeleteAccountContract.ViewEvent, navigator: Navigator) {
        when (event) {
            DeleteAccountContract.ViewEvent.FinishActivity -> {
                navigator.popUntilRoot()
            }
            is DeleteAccountContract.ViewEvent.GoToAddTransactionScreen -> {}
            DeleteAccountContract.ViewEvent.ShowRetryDialog -> {}
        }
    }
}