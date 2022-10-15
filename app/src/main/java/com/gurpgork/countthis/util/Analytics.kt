package com.gurpgork.countthis.util

interface Analytics {
    fun trackScreenView(
        label: String,
        route: String?,
        arguments: Any? = null
    )
}