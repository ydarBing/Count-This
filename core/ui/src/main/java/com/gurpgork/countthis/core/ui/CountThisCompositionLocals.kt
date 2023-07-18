package com.gurpgork.countthis.core.ui

import androidx.compose.runtime.staticCompositionLocalOf

val LocalCountThisDateFormatter = staticCompositionLocalOf<CountThisDateFormatter> {
    error("CountThisDateFormatter not provided")
}

//val LocalCountThis = staticCompositionLocalOf<CountThisDateFormatter> {
//    error("CountThisDateFormatter not provided")
//}