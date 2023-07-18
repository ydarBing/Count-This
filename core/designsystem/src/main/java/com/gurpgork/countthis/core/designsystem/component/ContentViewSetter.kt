package com.gurpgork.countthis.core.designsystem.component

import android.app.Activity
import android.view.View

fun interface ContentViewSetter {
    fun setContentView(activity: Activity, view: View)
}