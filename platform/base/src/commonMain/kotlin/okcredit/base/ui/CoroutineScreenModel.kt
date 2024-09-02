package okcredit.base.ui

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * A ViewModel that uses coroutines to handle state and events.
 * This is lifecycle aware and will automatically cancel all coroutines when the screen is destroyed.
 * This ViewModel is also thread safe and will automatically switch to the main thread when updating the state.
 */
interface CoroutineScreenModel<S : UiState, E : BaseViewEvent, I : UserIntent> : ScreenModel {
    /**
     * This method is called when the screen is created.
     * This is the place to initialize the screen state and start any data loading.
     */
    fun start()

    /**
     * Call this method to push any new Intent from the screen or view.
     * @param - intent - The intent to be pushed to the ViewModel
     */
    fun pushIntent(intent: I)

    /**
     * This method returns a [StateFlow] of the current state of the screen.
     */
    val states: StateFlow<S>

    /**
     * This method returns a [Flow] of the view events emitted by the presentation layer.
     */
    val viewEvents: Flow<E>
}
