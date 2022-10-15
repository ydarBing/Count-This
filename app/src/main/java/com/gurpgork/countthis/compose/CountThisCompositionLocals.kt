package com.gurpgork.countthis.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.gurpgork.countthis.util.CountThisDateFormatter

val LocalCountThisDateFormatter = staticCompositionLocalOf<CountThisDateFormatter> {
    error("CountThisDateFormatter not provided")
}

//val LocalCountThis = staticCompositionLocalOf<CountThisDateFormatter> {
//    error("CountThisDateFormatter not provided")
//}