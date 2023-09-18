package com.gurpgork.countthis.core.domain.util

sealed class CounterOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): CounterOrder(orderType)
    class Incremented(orderType: OrderType): CounterOrder(orderType)
    class Created(orderType: OrderType): CounterOrder(orderType)
    class Custom(orderType: OrderType): CounterOrder(orderType)
}