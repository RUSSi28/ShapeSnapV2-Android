package com.orukunnn.shapesnapapp.ui.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.SharedPreferenceDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow<StorageState>(StorageState.Loading)
    val state: StateFlow<StorageState> = _state.asStateFlow()

    private val _showDeleteConfirmDialog = MutableStateFlow(false)
    val showDeleteConfirmDialog = _showDeleteConfirmDialog.asStateFlow()

    private val _deleteTargetId = MutableStateFlow("")
    val deleteTargetId = _deleteTargetId.asStateFlow()

    init {
        val userId = sharedPreferenceDatasource.getUserId()
        if (userId != null) {
            loadStorage(userId)
        } else {
            _state.value = StorageState.Success(emptyList())
        }
    }

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
            userRepository.removeStorageBy(presetId, userId)
            _deleteTargetId.value = ""
            loadStorage(userId)
        }
    }

    fun loadStorage(uid: String) {
        if (uid.isBlank()) return

        viewModelScope.launch {
            _state.value = StorageState.Loading
            try {
                val storageIds = userRepository.getStorageIdsOf(uid)
                val presets = userRepository.getPresetsBy(storageIds)
                _state.value = StorageState.Success(presets)
            } catch (e: Exception) {
                _state.value = StorageState.Error
            }
        }
    }
}
