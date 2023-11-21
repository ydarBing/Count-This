package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.analytics.AnalyticsHelper
import com.gurpgork.countthis.core.datastore.CtPreferencesDataSource
import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import com.gurpgork.countthis.core.model.data.SortOption
import com.gurpgork.countthis.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val ctPreferencesDataSource: CtPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = ctPreferencesDataSource.userData

    override suspend fun incrementCountersTrackingLocation() {
        ctPreferencesDataSource.incrementCountersTrackingLocation()
        analyticsHelper.logIncrementCountersTrackingLocation()
    }

    override suspend fun decrementCountersTrackingLocation() {
        ctPreferencesDataSource.decrementCountersTrackingLocation()
        analyticsHelper.logDecrementCountersTrackingLocation()
    }

    override suspend fun setUseButtonIncrements(useButtonIncrements: Boolean) {
        ctPreferencesDataSource.setUseButtonIncrements(useButtonIncrements)
        analyticsHelper.logSetUseButtonIncrementsChanged(useButtonIncrements)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        ctPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        analyticsHelper.logDarkThemeConfigChanged(darkThemeConfig.name)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        ctPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        analyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        ctPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
        analyticsHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }

    override suspend fun setSort(sort: SortOption) {
        ctPreferencesDataSource.setSort(sort)
        analyticsHelper.logSortStateChanged(sort.name)
    }

    override suspend fun setSortAsc(sortAsc: Boolean){
        ctPreferencesDataSource.setSortAsc(sortAsc)
        analyticsHelper.logSortAscStateSet(sortAsc)
    }
    override suspend fun toggleSortAsc() {
        ctPreferencesDataSource.toggleSortAsc()
        analyticsHelper.logSortAscStateToggled(userData.last().sortAsc)
    }
    override suspend fun setCrashAnalyticsPreference(enableAnalytics: Boolean){
        ctPreferencesDataSource.setCrashAnalyticsPreference(!enableAnalytics)
        analyticsHelper.logCrashAnalyticsStateChanged(!enableAnalytics)
    }
}
