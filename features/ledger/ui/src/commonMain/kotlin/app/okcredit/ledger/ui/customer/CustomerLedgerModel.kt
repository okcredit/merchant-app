package app.okcredit.ledger.ui.customer

import app.okcredit.ledger.ui.customer.CustomerLedgerContract.*
import app.okcredit.ledger.ui.customer.usecase.GetCustomerDetails
import app.okcredit.ledger.ui.customer.usecase.GetCustomerLedgerData
import app.okcredit.ledger.ui.customer.usecase.GetCustomerToolbarData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@OptIn(ExperimentalCoroutinesApi::class)
@Inject
class CustomerLedgerModel(
    private val getCustomerDetails: GetCustomerDetails,
    private val getToolbarData: GetCustomerToolbarData,
    private val getLedgerItems: GetCustomerLedgerData
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeCustomerDetails(),
            observeToolbarData(),
            observeLedgerItems()
        )
    }

    private fun observeToolbarData() = wrap(getToolbarData.execute(""))
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetToolbarData(it.value)
            }
        }

    private fun observeLedgerItems() = intent<Intent.LoadTransactions>()
        .flatMapLatest { wrap(getLedgerItems.execute("", showOldClicked = it.showOldClicked)) }
        .map {
            when (it) {
                is Result.Failure -> PartialState.SetLoading(false)
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Success -> PartialState.SetLedgerItems(it.value.customerLedgerItems)
            }
        }

    private fun observeCustomerDetails() = wrap(getCustomerDetails.execute(""))
        .map {
            when (it) {
                is Result.Failure -> PartialState.SetLoading(false)
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Success -> {
                    val customer = it.value
                    if (customer == null) {
                        emitViewEvent(ViewEvent.ShowError("Something went wrong"))
                        PartialState.SetLoading(true)
                    } else {
                        PartialState.SetCustomerDetails(customer)
                    }
                }
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            is PartialState.NoChange -> currentState
            is PartialState.SetCustomerDetails -> currentState.copy(customerDetails = partialState.customerDetails)
            is PartialState.SetToolbarData -> currentState.copy(toolbarData = partialState.toolbarData)
            is PartialState.SetLedgerItems -> currentState.copy(ledgerItems = partialState.ledgerItems)
            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)
        }
    }

}