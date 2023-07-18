package com.gurpgork.countthis.feature.editcreate

sealed class ValidationEvent {
    object Success: ValidationEvent()
}