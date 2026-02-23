package com.orukunnn.shapesnapapp.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.PresetEntity
import com.orukunnn.shapesnapapp.data.repository.PresetsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class HomeScreenViewModel(
    private val presetsRepository: PresetsRepository
): ViewModel() {
    val _presets = MutableStateFlow<List<PresetEntity>>(emptyList())
    val presets: StateFlow<List<PresetEntity>> = _presets.asStateFlow()

    init {
        loadPresets()
    }

    fun loadPresets() {
        viewModelScope.launch {
            try {
                val result = presetsRepository.getFirstPresets()
                _presets.value = result.first
                Log.d("HomeScreenViewModel", "Loaded ${result.first.size} presets")
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "loadPresets: ${e.message}")
            }

        }
    }
}