package com.orukunnn.shapesnapapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val currentUser = authRepository.currentUser
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
                val user = authRepository.signInWithGoogle(
                    context = context,
                    serverClientId = serverClientId,
                )
                user?.uid?.let { uid ->
                    userRepository.saveUserIfNotExists(uid)
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
                authRepository.signOut(context)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
