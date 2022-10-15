package com.gurpgork.countthis.appilitializers

import android.app.Application
import com.gurpgork.countthis.settings.CountThisPreferences
import javax.inject.Inject

class PreferencesInitializer @Inject constructor(
    private val prefs: CountThisPreferences
) : AppInitializer {
    override fun init(application: Application) {
        prefs.setup()
    }
}