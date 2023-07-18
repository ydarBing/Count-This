package com.gurpgork.countthis.core.analytics

interface Analytics {
    fun trackScreenView(
        label: String,
        route: String?,
        arguments: Any? = null
    )
}