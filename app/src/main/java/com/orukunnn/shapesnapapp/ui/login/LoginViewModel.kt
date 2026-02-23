package com.orukunnn.shapesnapapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(
    private val firebaseAuthDatasource: FirebaseAuthDatasource
) : ViewModel() {
    val currentUser = firebaseAuthDatasource.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun createSignInWithGoogleIntent() =
        firebaseAuthDatasource.createSignInWithGoogleIntent()
}