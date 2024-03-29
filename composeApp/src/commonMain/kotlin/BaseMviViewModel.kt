import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseMviViewModel<STATE, EVENT>(
    private val componentContext: ComponentContext,
    initialState: STATE,
) : ComponentContext by componentContext {
    val stateFlow
        get() = _stateFlow.asStateFlow()

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
}
