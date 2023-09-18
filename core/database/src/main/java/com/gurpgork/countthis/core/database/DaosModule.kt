package com.gurpgork.countthis.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {
    @Provides
    fun provideCounterDao(db: CtDatabase) = db.counterDao()

    @Provides
    fun provideHistoryDao(db: CtDatabase) = db.historyDao()

    @Provides
    fun provideIncrementDao(db: CtDatabase) = db.incrementDao()

    @Provides
    fun provideLocationDao(db: CtDatabase) = db.locationDao()
}
