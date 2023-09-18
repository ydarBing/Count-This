package com.gurpgork.countthis.core.model.data

enum class SortOption(val sqlValue: String) {
    USER_SORTED("list_index"),
    LAST_UPDATED("most_recent_increment_date"),
    ALPHABETICAL("name"),
    DATE_ADDED("creation_date")
}