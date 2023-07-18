package com.gurpgork.countthis.core.datastore.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class SettingsModuleBinds {
    @Singleton
    @Binds
    abstract fun providePreferences(bind: CountThisPreferencesImpl): CountThisPreferences
}

@InstallIn(SingletonComponent::class)
@Module
internal object SettingsModule {
    @Named("app")
    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}
