package com.gurpgork.countthis.core.datastore

import androidx.datastore.core.DataStore
import com.gurpgork.countthis.DarkThemeConfigProto
import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import com.gurpgork.countthis.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CtPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                countersTrackingLocation = it.countersTrackingLocation,
                useButtonIncrements = it.shouldUseButtonsForIncrements,
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                    ->
                        DarkThemeConfig.FOLLOW_SYSTEM
                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                useDynamicColor = it.useDynamicColor,
                shouldHideOnboarding = it.shouldHideOnboarding,
            )
        }

    suspend fun incrementCountersTrackingLocation() {
        userPreferences.updateData {
            it.copy {
                this.countersTrackingLocation = this.countersTrackingLocation + 1
            }
        }
    }

    suspend fun decrementCountersTrackingLocation() {
        userPreferences.updateData {
            it.copy {
                if(this.countersTrackingLocation > 0)
                    this.countersTrackingLocation = this.countersTrackingLocation - 1
            }
        }
    }
    suspend fun setUseButtonIncrements(useButtonIncrements: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldUseButtonsForIncrements = useButtonIncrements
            }
        }
    }
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }
}

//private fun UserPreferencesKt.Dsl.updateShouldHideOnboardingIfNecessary() {
//    if (countersTrackingLocation == 0) {
//        shouldHideOnboarding = false
//    }
//}
