package com.gurpgork.countthis.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val settingsViewState: StateFlow<SettingsViewState> =
        userDataRepository.userData
            .map {userData ->
                SettingsViewState.Success(
                    settings = UserEditableSettings(
                        useButtonIncrement = userData.useButtonIncrements,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                        useCrashAnalytics = !userData.shouldDisableCrashAnalytics,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                // Starting eagerly means the user data is ready when the SettingsDialog is laid out
                // for the first time. Without this, due to b/221643630 the layout is done using the
                // "Loading" text, then replaced with the user editable fields once loaded, however,
                // the layout height doesn't change meaning all the fields are squashed into a small
                // scrollable column.
                // TODO: Change to SharingStarted.WhileSubscribed(5_000) when b/221643630 is fixed
                started = SharingStarted.Eagerly,
                initialValue = SettingsViewState.Loading,
            )

    fun updateButtonIncrementPreference(useButtonIncrement: Boolean){
        viewModelScope.launch {
            userDataRepository.setUseButtonIncrements(useButtonIncrement)
        }
    }
    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }
    fun updateDynamicColorPreference(useDynamicColor: Boolean){
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
    fun updateCrashAnalyticsPreference(enableAnalytics: Boolean){
        viewModelScope.launch {
            userDataRepository.setCrashAnalyticsPreference(enableAnalytics)
            Firebase.crashlytics.setCrashlyticsCollectionEnabled(enableAnalytics)
        }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val useButtonIncrement: Boolean,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val useCrashAnalytics: Boolean,
)
sealed interface SettingsViewState {
    object Loading : SettingsViewState
    data class Success(val settings: UserEditableSettings) : SettingsViewState
}