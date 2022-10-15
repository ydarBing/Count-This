package com.gurpgork.countthis.appilitializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}