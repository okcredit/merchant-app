@file:OptIn(ExperimentalCoroutinesApi::class)

package app.okcredit.ledger.ui.supplier

import app.okcredit.ledger.ui.*
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.*
import app.okcredit.ledger.ui.supplier.usecase.GetSupplierData
import app.okcredit.ledger.ui.supplier.usecase.GetSupplierLedgerData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@Inject
class SupplierLedgerModel(
    private val getSupplierData: GetSupplierData,
    private val getSupplierLedgerData: GetSupplierLedgerData,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeSupplierData(),
            observeSupplierLedgerItems(),
        )
    }

    private fun observeSupplierLedgerItems() = intent<Intent.LoadTransaction>()
        .flatMapLatest {
            wrap(
                getSupplierLedgerData.execute(
                    supplierId = "",
                    showOldClicked = it.showOldClicked
                )
            )
        }
        .map {
            when (it) {
                is Result.Failure -> PartialState.SetLoading(false)
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Success -> PartialState.SetLedgerItems(it.value)
            }
        }

    private fun observeSupplierData() = wrap(getSupplierData.execute(supplierId = ""))
        .map {
            when (it) {
                is Result.Failure -> PartialState.SetLoading(false)
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Success -> {
                    val res = it.value
                    if (res == null) {
                        emitViewEvent(ViewEvent.ShowError(Res.string.something_went_wrong))
                        PartialState.SetLoading(true)
                    } else {
                        PartialState.SetSupplierData(res.supplier, res.toolbarData)
                    }
                }
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            is PartialState.NoChange -> currentState
            is PartialState.SetLedgerItems -> currentState.copy(
                ledgerItems = partialState.ledgerItems,
                loading = false
            )

            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)
            is PartialState.SetSupplierData -> currentState.copy(
                supplierDetails = partialState.supplierDetails,
                toolbarData = partialState.toolbarData
            )
        }
    }

}