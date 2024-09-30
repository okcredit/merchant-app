package app.okcredit.ledger.ui.add

import app.okcredit.ledger.contract.usecase.CyclicAccountError
import app.okcredit.ledger.contract.usecase.DeletedCustomerError
import app.okcredit.ledger.contract.usecase.MobileConflictError
import app.okcredit.ledger.core.usecase.AddAccount
import app.okcredit.ledger.ui.add.AddRelationContract.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@Inject
class AddRelationScreenModel(
    private val addAccount: AddAccount,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeOnSubmitted(),
            observeOnDismissDialog(),
        )
    }

    private fun observeOnSubmitted() = intent<Intent.OnSubmitClicked>()
        .flatMapLatest {
            wrap { addAccount.execute(it.name, it.mobile, it.accountType) }
        }
        .map {
            when (it) {
                is Result.Failure -> {
                    when (val error = it.error) {
                        is MobileConflictError -> {
                            // Handle mobile conflict error
                            PartialState.SetErrorDialog(
                                ErrorDialog.CyclicAccountError(
                                    accountId = error.accountId,
                                    name = error.name,
                                ),
                            )
                        }

                        is DeletedCustomerError -> {
                            // Handle deleted customer error
                        }

                        is CyclicAccountError -> {
                            // Handle cyclic account error
                            PartialState.SetErrorDialog(
                                ErrorDialog.CyclicAccountError(
                                    accountId = error.accountId,
                                    name = error.name,
                                ),
                            )
                        }
                    }
                    PartialState.SetLoading(false)
                }

                is Result.Progress -> {
                    PartialState.SetLoading(true)
                }

                is Result.Success -> {
                    emitViewEvent(ViewEvent.Success(it.value.id))
                    PartialState.SetLoading(false)
                }
            }
        }

    private fun observeOnDismissDialog() = intent<Intent.OnDismissDialog>()
        .map {
            PartialState.SetErrorDialog(null)
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)
            is PartialState.SetErrorDialog -> currentState.copy(
                errorDialog = partialState.errorDialog,
            )
        }
    }
}
