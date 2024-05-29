package app.okcredit.onboarding.businessName

import app.okcredit.onboarding.businessName.EnterBusinessNameContract.Intent
import app.okcredit.onboarding.businessName.EnterBusinessNameContract.PartialState
import app.okcredit.onboarding.businessName.EnterBusinessNameContract.State
import app.okcredit.onboarding.businessName.EnterBusinessNameContract.ViewEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import tech.okcredit.identity.contract.model.BusinessConstants
import tech.okcredit.identity.contract.usecase.Request
import tech.okcredit.identity.contract.usecase.UpdateBusiness

@Inject
class EnterBusinessNameScreenModel(
    private val updateBusiness: UpdateBusiness,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(
    State(),
) {
    override fun partialStates(): Flow<PartialState> {
        return merge(
            observeSetBusinessName(),
        )
    }

    private fun observeSetBusinessName() = intent<Intent.SetBusinessName>()
        .flatMapLatest {
            wrap {
                updateBusiness.execute(
                    Request(
                        BusinessConstants.BUSINESS_NAME,
                        it.businessName,
                    ),
                )
            }
        }.map {
            when (it) {
                is Result.Failure -> PartialState.HideLoading
                is Result.Progress -> PartialState.ShowLoading
                is Result.Success -> {
                    emitViewEvent(ViewEvent.GoToHome)
                    PartialState.HideLoading
                }
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.HideLoading -> currentState.copy(loading = false)
            PartialState.ShowLoading -> currentState.copy(loading = true)
        }
    }
}
