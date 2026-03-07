package com.orukunnn.shapesnapapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.SharedPreferenceDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetsRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeState {
    data class Success(val presets: List<Preset>) : HomeState
    data object Loading : HomeState
    data object Error : HomeState
}

class HomeScreenViewModel(
    private val presetsRepository: PresetsRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sharedPreferenceDatasource: SharedPreferenceDatasource,
) : ViewModel() {
    val currentUser = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadPresets()
    }

    fun loadPresets() {
        viewModelScope.launch {
            try {
                _state.value = HomeState.Loading
                val result = presetsRepository.getInitialPresets()
                _state.value = HomeState.Success(result.first)
                Log.d("HomeScreenViewModel", "Loaded ${result.first.size} presets")
            } catch (e: Exception) {
                _state.value = HomeState.Error
                Log.d("HomeScreenViewModel", "loadPresets: ${e.message}")
            }
        }
    }

    fun refreshPresets() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                val result = presetsRepository.getInitialPresets()
                _state.value = HomeState.Success(result.first)
                Log.d("HomeScreenViewModel", "Refreshed ${result.first.size} presets")
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "refreshPresets error: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun saveToStorage(presetId: String) {
        val userId = sharedPreferenceDatasource.getUserId()
        Log.d("HomeScreenViewModel", "saveToStorage: $userId")
        if (userId == null) {
            Log.e("HomeScreenViewModel", "saveToStorage: userId is null")
            return
        }

        viewModelScope.launch {
            try {
                userRepository.addStorageBy(presetId, userId)
                Log.d("HomeScreenViewModel", "Saved $presetId to storage")
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to save to storage: ${e.message}")
            }
        }
    }
}
