package com.gurpgork.countthis.core.analytics.di

import com.gurpgork.countthis.core.analytics.AnalyticsHelper
import com.gurpgork.countthis.core.analytics.StubAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: StubAnalyticsHelper): AnalyticsHelper
}