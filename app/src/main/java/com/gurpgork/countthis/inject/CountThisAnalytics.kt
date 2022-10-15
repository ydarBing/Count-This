package com.gurpgork.countthis.inject

import com.gurpgork.countthis.util.Analytics
import javax.inject.Inject

internal class CountThisAnalytics @Inject constructor(
//TODO incorporate and implement FirebaseAnalytics
//private val firebaseAnalytics: Provider<FirebaseAnalytics>
) : Analytics {
    override fun trackScreenView(
        label: String,
        route: String?,
        arguments: Any?
    ) {
        try {
//            firebaseAnalytics.get().logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
//                param(FirebaseAnalytics.Param.SCREEN_NAME, label)
//                if (route != null) param("screen_route", route)
//
//                // Expand out the rest of the parameters
//                when {
//                    arguments is Bundle -> {
//                        for (key in arguments.keySet()) {
//                            val value = arguments.getString(key)
//                            // We don't want to include the label or route twice
//                            if (value == label || value == route) continue
//
//                            param("screen_arg_$key", value ?: "")
//                        }
//                    }
//                    arguments != null -> param("screen_arg", arguments.toString())
//                }
//            }
        } catch (t: Throwable) {
            // Ignore, Firebase might not be setup for this project
        }
    }
}
