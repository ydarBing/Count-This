package com.gurpgork.countthis.inject

import com.gurpgork.countthis.appilitializers.AppInitializer
import com.gurpgork.countthis.appilitializers.PreferencesInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {
//
//    @Binds
//    @IntoSet
//    abstract fun provideEmojiInitializer(bind: EmojiInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun providePreferencesInitializer(bind: PreferencesInitializer): AppInitializer

}
