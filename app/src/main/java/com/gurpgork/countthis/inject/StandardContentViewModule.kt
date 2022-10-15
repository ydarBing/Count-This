package com.gurpgork.countthis.inject

import com.gurpgork.countthis.compose.ContentViewSetter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object StandardContentViewModule {
    @Provides
    fun provideContentViewSetter(): ContentViewSetter = ContentViewSetter { activity, view ->
        activity.setContentView(view)
    }
}