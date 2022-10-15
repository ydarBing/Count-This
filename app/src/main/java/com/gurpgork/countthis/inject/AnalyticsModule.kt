package com.gurpgork.countthis.inject

import com.gurpgork.countthis.util.Analytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AnalyticsModule {
    @Singleton
    @Binds
    internal abstract fun provideAnalytics(bind: CountThisAnalytics): Analytics
}