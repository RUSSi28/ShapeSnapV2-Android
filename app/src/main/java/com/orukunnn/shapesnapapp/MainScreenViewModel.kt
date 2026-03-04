package com.orukunnn.shapesnapapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val firebaseAuthDatasource: FirebaseAuthDatasource,
    private val firestoreDatasource: FirestoreDatasource
): ViewModel() {
    val currentUser = firebaseAuthDatasource.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _showLogOutConfirmDialog = MutableStateFlow(false)
    val showLogOutConfirmDialog = _showLogOutConfirmDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setShowLogOutConfirmDialog(show: Boolean) {
        _showLogOutConfirmDialog.value = show
    }

    fun signInWithGoogle(context: Context, serverClientId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = firebaseAuthDatasource.signInWithGoogle(
                    context = context,
                    serverClientId = serverClientId,
                )
                user?.uid?.let { uid ->
                    firestoreDatasource.saveUserIfNotExists(uid)
                }
            } catch (e: Exception) {
                // Handle error if needed
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logOut(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firebaseAuthDatasource.signOut(context)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
