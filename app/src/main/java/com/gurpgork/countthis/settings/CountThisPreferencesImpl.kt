package com.gurpgork.countthis.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.edit
import com.gurpgork.countthis.R
import com.gurpgork.countthis.settings.CountThisPreferences.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named


class CountThisPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("app") private val sharedPreferences: SharedPreferences,
) : CountThisPreferences {
    private val defaultThemeValue = context.getString(R.string.pref_theme_default_value)
    @ChecksSdkIntAtLeast(api = 31)
    private val defaultDynamicColors = Build.VERSION.SDK_INT >= 31

    private var defaultButtonIncrements = false

    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        preferenceKeyChangedFlow.tryEmit(key)
    }

    companion object {
        const val KEY_THEME = "pref_theme"
        const val KEY_DYNAMIC_COLORS = "pref_dynamic_colors"
        const val KEY_BUTTON_INCREMENTS = "pref_button_increments"
        const val KEY_FOREGROUND_LOCATION_ENABLED = "pref_tracking_foreground_location"
    }

    override fun setup() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override var theme: Theme
        get() = getThemeForStorageValue(sharedPreferences.getString(KEY_THEME, defaultThemeValue)!!)
        set(value) = sharedPreferences.edit {
            putString(KEY_THEME, value.storageKey)
        }
    override fun observeTheme(): Flow<Theme> = createPreferenceFlow(KEY_THEME) { theme }


    override var dynamicColors: Boolean
        get() = sharedPreferences.getBoolean(KEY_DYNAMIC_COLORS, defaultDynamicColors)
        set(value) = sharedPreferences.edit{
            putBoolean(KEY_DYNAMIC_COLORS, value)
        }
    override fun observeDynamicColors(): Flow<Boolean> {
        return createPreferenceFlow(KEY_DYNAMIC_COLORS) { dynamicColors }
    }

    override var buttonIncrements: Boolean
        get() = sharedPreferences.getBoolean(KEY_BUTTON_INCREMENTS, defaultButtonIncrements)
        set(value) = sharedPreferences.edit{
            putBoolean(KEY_BUTTON_INCREMENTS, value)
        }

    override fun observeButtonIncrements(): Flow<Boolean> {
        return createPreferenceFlow(KEY_BUTTON_INCREMENTS) { buttonIncrements }
    }

    override var requestingLocationUpdates: Int
        get() = sharedPreferences.getInt(KEY_FOREGROUND_LOCATION_ENABLED, 0)
        set(value) = sharedPreferences.edit {
            putInt(KEY_FOREGROUND_LOCATION_ENABLED, value)
        }
    override fun observeTrackingLocation(): Flow<Int> = createPreferenceFlow(
        KEY_FOREGROUND_LOCATION_ENABLED) { requestingLocationUpdates}


    private inline fun <T> createPreferenceFlow(
        key: String,
        crossinline getValue: () -> T,
    ): Flow<T> = preferenceKeyChangedFlow
        .onStart { emit(key) }
        .filter { it == key }
        .map { getValue() }
        .distinctUntilChanged()

    private val Theme.storageKey: String
        get() = when (this) {
            Theme.LIGHT -> context.getString(R.string.pref_theme_light_value)
            Theme.DARK -> context.getString(R.string.pref_theme_dark_value)
            Theme.SYSTEM -> context.getString(R.string.pref_theme_system_value)
        }

    private fun getThemeForStorageValue(value: String) = when (value) {
        context.getString(R.string.pref_theme_light_value) -> Theme.LIGHT
        context.getString(R.string.pref_theme_dark_value) -> Theme.DARK
        else -> Theme.SYSTEM
    }

//    /**
//     * Returns true if requesting location updates, otherwise returns false.
//     */
//    fun getLocationTrackingPref() : Boolean =
//        sharedPreferences.getBoolean(KEY_FOREGROUND_LOCATION_ENABLED, false)


}
