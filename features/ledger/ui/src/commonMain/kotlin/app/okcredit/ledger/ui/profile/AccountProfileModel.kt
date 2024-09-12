package app.okcredit.ledger.ui.profile

import app.okcredit.ledger.core.usecase.UpdateAccount
import app.okcredit.ledger.ui.profile.AccountProfileContract.*
import app.okcredit.ledger.ui.profile.usecase.GetRelationshipDetails
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
    private val getRelationshipDetails: GetRelationshipDetails,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeRelationshipDetails(),
        )
    }

    private fun observeRelationshipDetails() = intent<Intent.LoadDetails>()
        .flatMapLatest {
            wrap(getRelationshipDetails.execute(it.accountId, it.accountType))
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

            is PartialState.SetBottomSheetType -> currentState.copy(
                bottomSheetType = partialState.type
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
        }
    }
}