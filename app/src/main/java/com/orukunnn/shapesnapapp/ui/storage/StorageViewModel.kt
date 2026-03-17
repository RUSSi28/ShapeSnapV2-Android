package com.orukunnn.shapesnapapp.ui.storage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.SharedPreferenceDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface StorageState {
    data class Success(val presets: List<Preset>) : StorageState
    data object Loading : StorageState
    data object Error : StorageState
}

class StorageViewModel(
    private val userRepository: UserRepository,
    private val sharedPreferenceDatasource: SharedPreferenceDatasource,
) : ViewModel() {

    private val userId = sharedPreferenceDatasource.getUserId()

    val state: StateFlow<StorageState> = (if (userId != null) {
        userRepository.getSavedPresetsFlow(userId)
    } else {
        flowOf(emptyList())
    }).map { presets -> StorageState.Success(presets) as StorageState }
        .catch { e ->
            Log.e("StorageViewModel", "Error fetching storage: ${e.message}")
            emit(StorageState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StorageState.Loading
        )

    private val _showDeleteConfirmDialog = MutableStateFlow(false)
    val showDeleteConfirmDialog = _showDeleteConfirmDialog.asStateFlow()

    private val _deleteTargetId = MutableStateFlow("")
    val deleteTargetId = _deleteTargetId.asStateFlow()

    fun setShowDeleteConfirmDialog(
        show: Boolean,
        targetPresetId: String = "",
    ) {
        _showDeleteConfirmDialog.value = show
        if (targetPresetId.isBlank()) return
        _deleteTargetId.value = targetPresetId
    }

    fun removeStorage(presetId: String) {
        val userId = sharedPreferenceDatasource.getUserId() ?: return
        viewModelScope.launch {
            try {
                userRepository.removeStorageBy(presetId, userId)
                _deleteTargetId.value = ""
            } catch (e: Exception) {
                Log.e("StorageViewModel", "Failed to remove from storage: ${e.message}")
            }
        }
    }
}
