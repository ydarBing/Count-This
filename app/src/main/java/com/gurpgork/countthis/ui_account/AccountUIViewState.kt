package com.gurpgork.countthis.ui_account

import androidx.compose.runtime.Immutable

@Immutable
internal data class AccountUIViewState(
    //val user: CountThisUser? = null,
    //val authState: CountThisAuthState = CountThisAuthState.LOGGED_OUT
    val userId : Int = 0,
) {
    companion object {
        val Empty = AccountUIViewState()
    }
}