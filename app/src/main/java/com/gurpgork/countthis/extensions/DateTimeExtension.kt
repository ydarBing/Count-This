package com.gurpgork.countthis.extensions

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.time.format.DateTimeFormatter

fun DateTimeFormatter.withLocale(context: Context): DateTimeFormatter {
    val locales = ConfigurationCompat.getLocales(context.resources.configuration)
    return when {
        locales.isEmpty -> this
        else -> withLocale(locales[0])
    }
}