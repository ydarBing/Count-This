package com.gurpgork.countthis.feature.addedit

sealed class ValidationEvent {
    object Success: ValidationEvent()
}