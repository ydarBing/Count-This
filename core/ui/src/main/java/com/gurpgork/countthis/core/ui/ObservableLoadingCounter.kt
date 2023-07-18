package com.gurpgork.countthis.core.ui

import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.extensions.InvokeError
import com.gurpgork.countthis.core.extensions.InvokeStarted
import com.gurpgork.countthis.core.extensions.InvokeStatus
import com.gurpgork.countthis.core.extensions.InvokeSuccess
import com.gurpgork.countthis.core.extensions.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
    }
}

