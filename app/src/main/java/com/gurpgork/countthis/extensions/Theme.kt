package com.gurpgork.countthis.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.core.content.res.use

@SuppressLint("Recycle")
fun Context.resolveThemeColor(@AttrRes resId: Int, defaultColor: Int = Color.MAGENTA): Int {
    return obtainStyledAttributes(intArrayOf(resId)).use {
        it.getColor(0, defaultColor)
    }
}
