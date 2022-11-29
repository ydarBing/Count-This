package com.gurpgork.countthis.ui_create

object Validator {

    fun validateName(name: String): ValidationResult {
        return ValidationResult(name.isNotEmpty() /*&& name.isNotBlank()&& name.length > 5*/)
    }

    fun validateCount(count: Int): ValidationResult {
        return ValidationResult(true)
    }

    fun validateIncrement(increment: Int): ValidationResult {
        return ValidationResult(true)
    }

    fun validateGoal(goal: Int): ValidationResult {
        return ValidationResult(true)
    }

    fun validateTrackLocation(track: Boolean): ValidationResult {
        return ValidationResult(true)
    }
}

data class ValidationResult(
    val status: Boolean = false,
)