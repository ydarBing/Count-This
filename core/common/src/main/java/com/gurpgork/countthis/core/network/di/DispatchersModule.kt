package com.gurpgork.countthis.core.network.di

import com.gurpgork.countthis.core.network.CtDispatchers.Default
import com.gurpgork.countthis.core.network.CtDispatchers.IO
import com.gurpgork.countthis.core.network.CtDispatchers.Main
import com.gurpgork.countthis.core.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(Main)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}