package app.okcredit.merchant.home

import app.okcredit.merchant.home.HomeContract.Intent
import app.okcredit.merchant.home.HomeContract.PartialState
import app.okcredit.merchant.home.HomeContract.State
import app.okcredit.merchant.home.HomeContract.ViewEvent
import app.okcredit.merchant.usecase.HomeDataSyncer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import tech.okcredit.identity.contract.usecase.GetActiveBusiness

@Inject
class HomeScreenModel(
    private val getActiveBusiness: GetActiveBusiness,
    private val homeDataSyncer: HomeDataSyncer,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(initialState = State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            syncHomeData(),
            loadActiveBusiness(),
        )
    }

    private fun loadActiveBusiness() = wrap(getActiveBusiness.execute())
        .map {
            if (it is Result.Success) {
                PartialState.SetBusiness(business = it.value)
            } else {
                PartialState.NoChange
            }
        }

    private fun syncHomeData() = wrap {
        homeDataSyncer.execute()
    }.dropAll()

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetBusiness -> currentState.copy(business = partialState.business)
        }
    }
}
