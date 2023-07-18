package com.gurpgork.countthis.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.gurpgork.countthis.R
import com.gurpgork.countthis.core.designsystem.icon.CtIcons
import com.gurpgork.countthis.feature.allcounters.R as allCountersR

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    ALL_COUNTERS(
        selectedIcon = CtIcons.AllCounters,
        unselectedIcon = CtIcons.AllCountersBorder,
        iconTextId = allCountersR.string.counter_list_title,
        titleTextId = R.string.app_name,
    ),
//    GROUPS(
//        selectedIcon = CtIcons.Groups,
//        unselectedIcon = CtIcons.GroupsBorder,
//        iconTextId = groupsR.string.groups,
//        titleTextId = R.string.groups_title,
//    ),
}
