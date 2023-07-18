package com.gurpgork.countthis.core.data.di

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.data.repository.DefaultCounterRepository
import com.gurpgork.countthis.core.data.repository.DefaultIncrementRepository
import com.gurpgork.countthis.core.data.repository.IncrementRepository
import com.gurpgork.countthis.core.data.repository.LocationRepository
import com.gurpgork.countthis.core.data.repository.OfflineFirstLocationRepository
import com.gurpgork.countthis.core.data.repository.OfflineFirstUserDataRepository
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsCounterRepository(
        counterRepository: DefaultCounterRepository,
    ): CounterRepository
    @Binds
    fun bindsIncrementRepository(
        incrementRepository: DefaultIncrementRepository,
    ): IncrementRepository

    @Binds
    fun bindsLocationRepository(
        locationRepository: OfflineFirstLocationRepository,
    ): LocationRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

//    @Binds
//    fun bindsNetworkMonitor(
//        networkMonitor: ConnectivityManagerNetworkMonitor,
//    ): NetworkMonitor
}
