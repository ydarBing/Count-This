package com.gurpgork.countthis.inject

import android.app.Application
import android.content.Context
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import com.gurpgork.countthis.core.ui.ApplicationId
import com.gurpgork.countthis.core.ui.MediumDate
import com.gurpgork.countthis.core.ui.MediumDateTime
import com.gurpgork.countthis.core.ui.ShortDate
import com.gurpgork.countthis.core.ui.ShortTime
import com.gurpgork.countthis.core.ui.withLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
object AppModule {
    @ApplicationId
    @Provides
    fun provideApplicationId(application: Application): String = application.packageName

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    @Singleton
    @Named("cache")
    fun provideCacheDir(
        @ApplicationContext context: Context
    ): File = context.cacheDir

    @Singleton
    @Provides
    @MediumDate
    fun provideMediumDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDateTime
    fun provideDateTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortDate
    fun provideShortDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortTime
    fun provideShortTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(context)
    }
}