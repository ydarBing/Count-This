package com.gurpgork.countthis.core.database.view

import androidx.room.DatabaseView

// TODO not currently being used!!!!!!!!!!!!!!!!!!!


@DatabaseView(
    """
    SELECT counter_id as id, COUNT(*) as todayCount
FROM increments
WHERE date >= DATE('now') AND date < DATE('now', '+1 day')
    GROUP BY counter_id
"""
)
data class IncrementsToday(
    val id: Long,
    val todayCount: Int,
)