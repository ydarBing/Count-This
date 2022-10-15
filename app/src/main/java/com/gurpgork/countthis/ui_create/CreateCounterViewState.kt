package com.gurpgork.countthis.ui_create

//@Immutable
internal data class CreateCounterViewState (
    //var counterId : Int = 0,
    var name: String = "",
    var trackLocation: Boolean = false,
    var startCount: Int = 0,
    var goal: Int = 0,
    var incrementBy: Int = 1,
    //var counterEntity: CounterEntity = CounterEntity.EMPTY_COUNTER
) {
    companion object {
        val Empty = CreateCounterViewState()
    }
}