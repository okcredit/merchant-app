package app.okcredit.merchant.home

import okcredit.base.ui.BaseCoroutineScreenModel
import app.okcredit.merchant.home.HomeContract.*
import app.okcredit.merchant.home.usecase.GetHomeMoreOptionItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.Result

@Inject
class HomeScreenModel(
    private val getHomeMoreOptionItems: GetHomeMoreOptionItems,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            loadHomeMoreOptions(),
        )
    }

    private fun loadHomeMoreOptions() =  wrap(getHomeMoreOptionItems.execute()).map {
        when (it) {
            is Result.Failure -> PartialState.NoChange
            is Result.Progress -> PartialState.NoChange
            is Result.Success -> {
                println("More Option result - ${it.value.size}")
                PartialState.SetHomeMoreOptionItems(it.value)
            }
        }
    }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetHomeMoreOptionItems -> currentState.copy(
                moreOptions = partialState.items
            )
        }
    }
}