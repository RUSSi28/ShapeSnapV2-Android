package com.orukunnn.shapesnapapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val firebaseAuthDatasource: FirebaseAuthDatasource
) : ViewModel() {
    val currentUser = firebaseAuthDatasource.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun signInWithGoogle(context: Context, serverClientId: String) {
        viewModelScope.launch {
            try {
                firebaseAuthDatasource.signInWithGoogle(context, serverClientId)
            } catch (e: Exception) {
                // TODO: Handle error
                e.printStackTrace()
            }
        }
    }
}
