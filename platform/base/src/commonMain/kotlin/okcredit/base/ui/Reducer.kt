package okcredit.base.ui

/**
 * Interface which can be used move out reducer logic from ViewModel if they are becoming huge.
 */
interface Reducer<S : UiState, P : UiState.Partial> {

    fun reduce(currentState: S, partialState: P): S
}
