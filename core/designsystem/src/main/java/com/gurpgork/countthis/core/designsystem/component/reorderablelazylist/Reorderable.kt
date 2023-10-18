package com.gurpgork.countthis.core.designsystem.component.reorderablelazylist

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow

fun Modifier.reorderable(
    state: ReorderableState<*>
) = then(
    Modifier.onGloballyPositioned { state.layoutWindowPosition.value = it.positionInWindow()}.pointerInput(Unit) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)

            val dragResult = drag(down.id) {
                if (state.draggingItemIndex != null){
                    state.onDrag(it.positionChange().x.toInt(), it.positionChange().y.toInt())
                    it.consume()
                }
            }

            if (dragResult) {
                // consume up if we quit drag gracefully with the up
                currentEvent.changes.forEach {
                    if (it.changedToUp()) it.consume()
                }
            }

            state.onDragCanceled()
        }
    }
)