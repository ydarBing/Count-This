package com.gurpgork.countthis.ui_create

import androidx.core.text.isDigitsOnly

object Validator {

    fun validateName(name: String): ValidationResult {
        return ValidationResult(name.isNotEmpty() /*&& name.isNotBlank()&& name.length > 5*/)
    }

    fun validateCount(count: String): ValidationResult {
        return ValidationResult(count.isEmpty() || count.isDigitsOnly())
    }

    fun validateIncrement(increment: String): ValidationResult {
        return ValidationResult(increment.isEmpty() || increment.isDigitsOnly())
    }

    fun validateGoal(goal: String): ValidationResult {
        return ValidationResult(goal.isEmpty() || goal.isDigitsOnly())
    }

    fun validateTrackLocation(track: Boolean): ValidationResult {
        return ValidationResult(true)
    }
}

data class ValidationResult(
    val status: Boolean = false,
)