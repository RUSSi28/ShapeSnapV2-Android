package com.orukunnn.shapesnapapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val firebaseAuthDatasource: FirebaseAuthDatasource
): ViewModel() {
    val currentUser = firebaseAuthDatasource.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _showLogOutConfirmDialog = MutableStateFlow(false)
    val showLogOutConfirmDialog = _showLogOutConfirmDialog.asStateFlow()

    fun setShowLogOutConfirmDialog(show: Boolean) {
        _showLogOutConfirmDialog.value = show
    }

    fun signInWithGoogle(context: Context, serverClientId: String) {
        viewModelScope.launch {
            firebaseAuthDatasource.signInWithGoogle(
                context = context,
                serverClientId = serverClientId,
            )
        }
    }

    fun logOut(context: Context) {
        viewModelScope.launch {
            firebaseAuthDatasource.signOut(context)
        }
    }
}
