package com.gurpgork.countthis.core.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}