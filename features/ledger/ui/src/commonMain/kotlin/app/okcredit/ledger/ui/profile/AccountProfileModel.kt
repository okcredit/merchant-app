package app.okcredit.ledger.ui.profile

import app.okcredit.ledger.core.usecase.UpdateAccount
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.Intent
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.PartialState
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.State
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.ViewEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel

@Inject
class AccountProfileModel(
    private val updateAccount: UpdateAccount
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(
    State()
) {
    override fun partialStates(): Flow<PartialState> {
        return merge()
    }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return currentState
    }
}