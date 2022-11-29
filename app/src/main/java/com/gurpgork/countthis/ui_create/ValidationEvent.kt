package com.gurpgork.countthis.ui_create

sealed class ValidationEvent {
    object Success: ValidationEvent()
}