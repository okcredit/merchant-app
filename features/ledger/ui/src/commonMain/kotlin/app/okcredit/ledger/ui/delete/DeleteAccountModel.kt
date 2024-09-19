package app.okcredit.ledger.ui.delete

import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.model.isSupplier
import app.okcredit.ledger.ui.delete.DeleteAccountContract.*
import app.okcredit.ledger.ui.delete.usecase.DeleteAccountTracker
import app.okcredit.ledger.ui.delete.usecase.DeleteCustomer
import app.okcredit.ledger.ui.delete.usecase.DeleteSupplier
import app.okcredit.ledger.ui.delete.usecase.SyncAccountAndTransactions
import app.okcredit.ledger.ui.profile.usecase.GetAccountDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.*
import okcredit.base.units.Paisa
import kotlin.math.abs

@Inject
class DeleteAccountModel(
    private val deleteCustomer: DeleteCustomer,
    private val deleteSupplier: DeleteSupplier,
    private val getAccountDetails: GetAccountDetails,
    private val syncAccountAndTransactions: SyncAccountAndTransactions,
    private val tracker: DeleteAccountTracker,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    companion object {
        const val API_ERROR_BALANCE_NOT_ZERO_API_MESSAGE = "balance_not_zero"
        const val INCORRECT_PASSWORD = "incorrect_password"
    }

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeRelationshipDetails(),
            observeDeleteRelationshipClickedIntent(),
            observeDeleteIntent(),
            observeOnSettlementClickedIntent(),
            observeSyncTransactionAndRetryDeleteIntent(),
        )
    }

    private fun observeSyncTransactionAndRetryDeleteIntent() =
        intent<Intent.SyncTransactionAndRetryDelete>()
            .flatMapWrapped {
                syncAccountAndTransactions.execute(accountType = currentState.accountType)
                pushIntent(Intent.Delete)
            }.dropAll()

    private fun observeOnSettlementClickedIntent() = intent<Intent.OnSettlementClicked>()
        .map {
            tracker.trackDeleteSettleClicked()
            if (currentState.balance < Paisa.ZERO) {
                emitViewEvent(
                    ViewEvent.GoToAddTransactionScreen(
                        Transaction.Type.PAYMENT,
                        if (isSupplier()) currentState.balance else Paisa(abs(currentState.balance.value))
                    )
                )
                PartialState.NoChange
            } else if (currentState.balance > Paisa.ZERO) {
                emitViewEvent(
                    ViewEvent.GoToAddTransactionScreen(
                        Transaction.Type.CREDIT,
                        currentState.balance
                    )
                )
                PartialState.NoChange
            } else {
                PartialState.SetError("Something went wrong")
            }
        }

    private fun observeDeleteIntent() = intent<Intent.Delete>()
        .flatMapWrapped {
            if (isSupplier()) {
                deleteSupplier.execute(currentState.accountId)
            } else {
                deleteCustomer.execute(currentState.accountId)
            }
        }
        .map {
            when (it) {
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Failure -> {
                    tracker.trackDeletionError(
                        isSupplier = isSupplier(),
                        error = it.error
                    )
                    val error = it.error
                    when {
                        error.message == API_ERROR_BALANCE_NOT_ZERO_API_MESSAGE -> {
                            emitViewEvent(ViewEvent.ShowRetryDialog)
                        }

                        error.message == INCORRECT_PASSWORD -> {
                            tracker.trackIncorrectPassword()
                        }

                        else -> {
                            PartialState.SetError("Something went wrong")
                        }
                    }
                    PartialState.SetError("Something went wrong")
                }

                is Result.Success -> {
                    tracker.trackDeleteRelationship(
                        isSupplier = isSupplier(),
                        accountId = currentState.accountId
                    )
                    emitViewEvent(ViewEvent.FinishActivity)
                    PartialState.NoChange
                }
            }
        }

    private fun observeDeleteRelationshipClickedIntent() =
        intent<Intent.DeleteRelationshipClicked>()
            .map {
                tracker.trackDeleteRelationshipClicked(
                    isSupplier = isSupplier(),
                    accountId = currentState.accountId
                )
                emitViewEvent(ViewEvent.GoToAppLockScreenForAuthentication)
                PartialState.SetLoading(true)
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeRelationshipDetails() = intent<Intent.Load>()
        .flatMapLatest {
            wrap(
                getAccountDetails.execute(
                    currentState.accountId,
                    currentState.accountType,
                )
            )
        }.map {
            when (it) {
                is Result.Progress -> PartialState.NoChange
                is Result.Failure -> {
                    PartialState.SetError("Something went wrong")
                }

                is Result.Success -> {
                    val res = it.value
                    if (res != null) {
                        PartialState.SetRelationshipDetails(res)
                    } else {
                        PartialState.NoChange
                    }
                }
            }
        }

    private fun isSupplier() = currentState.accountType.isSupplier()

    override fun reduce(
        currentState: State,
        partialState: PartialState
    ): State {
        return when (partialState) {
            is PartialState.NoChange -> currentState
            is PartialState.SetRelationshipDetails -> currentState.copy(
                name = partialState.res.name,
                balance = partialState.res.balance,
                shouldSettle = partialState.res.balance != Paisa.ZERO,
                accountType = currentState.accountType,
                mobile = partialState.res.mobile
            )

            is PartialState.SetLoading -> currentState.copy(isLoading = partialState.value)
            is PartialState.SetError -> currentState.copy(errorMessage = partialState.message)
        }
    }
}