package com.orukunnn.shapesnapapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import com.orukunnn.shapesnapapp.data.model.Preset
import com.orukunnn.shapesnapapp.data.repository.PresetsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeState {
    data class Success(val presets: List<Preset>) : HomeState
    data object Loading : HomeState
    data object Error : HomeState
}

class HomeScreenViewModel(
    private val presetsRepository: PresetsRepository,
    private val firebaseAuthDatasource: FirebaseAuthDatasource
) : ViewModel() {
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
                val result = presetsRepository.getFirstPresets()
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
                val result = presetsRepository.getFirstPresets()
                _state.value = HomeState.Success(result.first)
                Log.d("HomeScreenViewModel", "Refreshed ${result.first.size} presets")
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "refreshPresets error: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun logOut(context: Context, onCompleted: () -> Unit) {
        viewModelScope.launch {
            firebaseAuthDatasource.signOut(context)
            onCompleted()
        }
    }
}
