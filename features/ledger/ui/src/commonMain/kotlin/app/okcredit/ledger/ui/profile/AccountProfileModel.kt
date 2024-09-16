package app.okcredit.ledger.ui.profile

import app.okcredit.ledger.core.usecase.RequestUpdateAccount
import app.okcredit.ledger.core.usecase.UpdateAccount
import app.okcredit.ledger.ui.profile.AccountProfileContract.*
import app.okcredit.ledger.ui.profile.usecase.GetAccountDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@Inject
@OptIn(ExperimentalCoroutinesApi::class)
class AccountProfileModel(
    private val updateAccount: UpdateAccount,
    private val getAccountDetails: GetAccountDetails,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeAccountDetails(),
            observeInfoDialogType(),
            observeBottomSheetType(),
            observeSubmitPhoneNumber(),
            observeSubmitName(),
            observeAddTransactionPermissionChange(),
            observeModifyState(),
            observeSubmitAddress(),
        )
    }

    private fun observeSubmitAddress() = intent<Intent.SubmitAddress>()
        .flatMapWrapped {
            updateAccount.execute(
                accountId = currentState.accountId,
                request = RequestUpdateAccount.UpdateAddress(it.newAddress),
                accountType = currentState.accountType
            )
        }.map {
            if (it is Result.Success) {
                PartialState.SetBottomSheetType(null)
            } else {
                PartialState.NoChange
            }
        }

    private fun observeModifyState() = intent<Intent.ModifyState>()
        .flatMapWrapped {
            updateAccount.execute(
                accountId = currentState.accountId,
                request = RequestUpdateAccount.UpdateAccountState(it.block),
                accountType = currentState.accountType
            )
        }.map {
            if (it is Result.Success) {
                PartialState.SetBottomSheetType(null)
            } else {
                PartialState.NoChange
            }
        }

    private fun observeAddTransactionPermissionChange() = intent<Intent.UpdateAddTransactionPermission>()
        .flatMapLatest {
            wrap {
                updateAccount.execute(
                    accountId = currentState.accountId,
                    request = RequestUpdateAccount.UpdateAddTransactionRestrictedStatus(it.switch),
                    accountType = currentState.accountType
                )
            }
        }.map {
            if (it is Result.Success) {
                PartialState.SetBottomSheetType(null)
            } else {
                PartialState.NoChange
            }
        }

    private fun observeSubmitName() = intent<Intent.SubmitName>()
        .flatMapLatest {
            wrap {
                updateAccount.execute(
                    accountId = currentState.accountId,
                    request = RequestUpdateAccount.UpdateName(it.name),
                    accountType = currentState.accountType
                )
            }
        }.map {
            if (it is Result.Success) {
                PartialState.SetBottomSheetType(null)
            } else {
                PartialState.NoChange
            }
        }

    private fun observeSubmitPhoneNumber() = intent<Intent.SubmitPhoneNumber>()
        .flatMapLatest {
            wrap {
                updateAccount.execute(
                    accountId = currentState.accountId,
                    request = RequestUpdateAccount.UpdateMobile(it.mobileNumber),
                    accountType = currentState.accountType
                )
            }
        }.map {
            if (it is Result.Success) {
                PartialState.SetBottomSheetType(null)
            } else {
                PartialState.NoChange
            }
        }

    private fun observeBottomSheetType() = intent<Intent.SetBottomSheetType>()
        .map {
            PartialState.SetBottomSheetType(it.type)
        }

    private fun observeInfoDialogType() = intent<Intent.SetInfoDialogType>()
        .map {
            PartialState.SetInfoDialogType(it.type)
        }

    private fun observeAccountDetails() = intent<Intent.LoadDetails>()
        .flatMapLatest {
            wrap(getAccountDetails.execute(it.accountId, it.accountType))
        }.map {
            when (it) {
                is Result.Progress -> PartialState.SetLoading(true)
                is Result.Success -> {
                    val res = it.value
                    if (res == null) {
                        PartialState.SetError("Something went wrong")
                    } else {
                        PartialState.RelationshipDetails(res)
                    }
                }

                is Result.Failure -> PartialState.SetError("Something went wrong")
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            is PartialState.NoChange -> currentState

            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)


            is PartialState.SetMobileNumber -> currentState.copy(
                mobile = partialState.mobileNumber
            )

            is PartialState.SetInfoDialogType -> currentState.copy(
                infoDialogType = partialState.type
            )

            is PartialState.RelationshipDetails -> currentState.copy(
                loading = false,
                name = partialState.res.name,
                mobile = partialState.res.mobile,
                profileImage = partialState.res.profileImage,
                registered = partialState.res.registered,
                blocked = partialState.res.blocked,
                transactionRestricted = partialState.res.transactionRestricted
            )

            is PartialState.SetError -> currentState.copy(
                errorMessage = partialState.errorMessage,
                loading = false
            )

            is PartialState.SetBottomSheetType -> currentState.copy(
                bottomSheetType = partialState.type
            )
        }
    }
}