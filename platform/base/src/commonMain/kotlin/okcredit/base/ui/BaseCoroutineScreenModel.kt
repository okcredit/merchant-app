package okcredit.base.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okcredit.base.appDispatchers

/**
 * Base class for screen models that use coroutines to handle intents and state.
 *
 * @param S the type of the state
 * @param P the type of the partial state
 * @param E the type of the view events
 * @param I the type of the intents
 */
abstract class BaseCoroutineScreenModel<
    S : UiState,
    P : UiState.Partial,
    E : BaseViewEvent,
    I : UserIntent,
    > protected constructor(
    private val initialState: S,
    private val reducer: Reducer<S, P>? = null,
) : ScreenModel, CoroutineScreenModel<S, E, I>, Reducer<S, P>, CoroutineResultWrapper {

    private val stateRelay: MutableStateFlow<S> = MutableStateFlow(initialState)
    protected val currentState get() = stateRelay.value
    final override val states: StateFlow<S> = stateRelay.asStateFlow()

    private val intentRelay: MutableSharedFlow<I> = MutableSharedFlow(extraBufferCapacity = 64)
    final override fun pushIntent(intent: I) = screenModelScope.launch { intentRelay.emit(intent) }
    protected val intents: Flow<I> = intentRelay.asSharedFlow()

    private val viewEventsRelay: Channel<E> = Channel(Channel.BUFFERED)
    protected suspend fun emitViewEvent(event: E) = viewEventsRelay.send(event)
    final override val viewEvents: Flow<E> = viewEventsRelay.receiveAsFlow()

    override fun start() {
        setupState()
    }

    protected abstract fun partialStates(): Flow<P>

    protected inline fun <reified UI : I> intent(): Flow<UI> = intents.filterIsInstance()

    private fun setupState() {
        partialStates()
            .scan(initialState) { state, partial ->
                reducer?.reduce(state, partial) ?: reduce(
                    state,
                    partial,
                )
            }
            .distinctUntilChanged()
            .flowOn(appDispatchers.io)
            .onEach { withContext(Dispatchers.Main) { stateRelay.emit(it) } }
            .catch { it.printStackTrace() }
            .launchIn(screenModelScope)
    }
}
