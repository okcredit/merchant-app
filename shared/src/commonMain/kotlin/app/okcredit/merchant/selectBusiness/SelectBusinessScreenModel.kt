package app.okcredit.merchant.selectBusiness

import okcredit.base.ui.BaseCoroutineScreenModel
import app.okcredit.merchant.selectBusiness.SelectBusinessContract.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.Result
import tech.okcredit.identity.contract.usecase.GetAllBusinesses
import tech.okcredit.identity.contract.usecase.SetActiveBusinessId

@Inject
class SelectBusinessScreenModel(
    private val getAllBusinesses: GetAllBusinesses,
    private val setActiveBusinessId: SetActiveBusinessId,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            loadAllBusinesses(),
            observeOnBusinessSelected()
        )
    }

    private fun loadAllBusinesses(): Flow<PartialState> {
        return wrap(getAllBusinesses.execute()).map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetBusinesses(it.value)
            }
        }
    }

    private fun observeOnBusinessSelected() = intent<Intent.OnBusinessSelected>()
        .flatMapLatest { wrap { setActiveBusinessId.execute(it.businessId) } }
        .map {
            if (it is Result.Success) {
                emitViewEvent(ViewEvent.MoveToHome)
            }
        }.dropAll()

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetBusinesses -> currentState.copy(
                businesses = partialState.businesses,
                loading = false
            )
        }
    }
}