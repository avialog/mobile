import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseMviViewModel<STATE, EVENT>(
    private val componentContext: ComponentContext,
    initialState: STATE,
) : ComponentContext by componentContext {
    private val wasInitialisedCalled: AtomicBoolean = atomic(initial = false)

    protected open fun initialised() {}

    val stateFlow
        get() =
            _stateFlow.asStateFlow()
                .onSubscriptionRemainingStateFlow {
                    if (wasInitialisedCalled.compareAndSet(expect = false, update = true)) {
                        initialised()
                    }
                }

    protected val viewModelScope: CoroutineScope =
        CoroutineScope(
            Dispatchers.Main.immediate + SupervisorJob(),
        )

    val actualState: STATE
        get() = stateFlow.value

    private val _stateFlow by lazy {
        MutableStateFlow(value = initialState)
    }

    protected fun updateState(update: STATE.() -> STATE) {
        _stateFlow.update {
            it.update()
        }
    }

    abstract fun onNewEvent(event: EVENT)

    init {
        componentContext.lifecycle.doOnDestroy {
            viewModelScope.cancel()
        }
    }

    private fun <T> StateFlow<T>.onSubscriptionRemainingStateFlow(action: () -> Unit): StateFlow<T> {
        val parent = this
        return object : StateFlow<T> {
            override val replayCache: List<T>
                get() = parent.replayCache
            override val value: T
                get() = parent.value

            override suspend fun collect(collector: FlowCollector<T>): Nothing {
                action()
                parent.collect {
                    collector.emit(it)
                }
            }
        }
    }
}
