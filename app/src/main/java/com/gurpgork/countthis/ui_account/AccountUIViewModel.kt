package com.gurpgork.countthis.ui_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AccountUIViewModel @Inject constructor(
    //observeCountThisAuthState: ObserveCountThisAuthState,
    //observeUserDetails: ObserveUserDetails,
    //private val clearUserDetails: ClearUserDetails
) : ViewModel() {

//    val state: StateFlow<AccountUIViewState> = combine(
//        observeCountThisAuthState.flow,
//        observeUserDetails.flow,
//    ) { authState, user ->
//        AccountUIViewState(
//            user = user,
//            authState = authState
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = AccountUIViewState.Empty,
//    )

    init {
//        observeCountThisAuthState(Unit)
//        observeUserDetails(ObserveUserDetails.Params("me"))
    }

    fun logout() {
        viewModelScope.launch {
            //clearUserDetails(ClearUserDetails.Params("me")).collect()
        }
    }
}
