package com.gurpgork.countthis.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val ctDispatcher: CtDispatchers)

enum class CtDispatchers {
    Default,
    IO,
    Main,
}
