package com.orukunnn.shapesnapapp.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface PostManageState {
    data class Success(val presets: List<Preset>) : PostManageState
    data object Loading : PostManageState
    data object Error : PostManageState
}

class PostManageViewModel(
    val authRepository: AuthRepository,
    val userRepository: UserRepository,
) : ViewModel() {
    val currentUser = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _state = MutableStateFlow<PostManageState>(PostManageState.Loading)
    val state: StateFlow<PostManageState> = _state.asStateFlow()

    private val _showDeleteConfirmDialog = MutableStateFlow(false)
    val showDeleteConfirmDialog = _showDeleteConfirmDialog.asStateFlow()

    private val _deleteTargetId = MutableStateFlow("")
    val deleteTargetId = _deleteTargetId.asStateFlow()

    init {
        viewModelScope.launch {
            currentUser.collectLatest { user ->
                if (user != null) {
                    loadPresets(user.uid)
                } else {
                    _state.value = PostManageState.Success(emptyList())
                }
            }
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

    fun deletePreset(presetId: String) {
        viewModelScope.launch {
            userRepository.deletePresetBy(presetId, currentUser.value?.uid ?: "")
            _deleteTargetId.value = ""
            loadPresets(currentUser.value?.uid ?: "")
        }
    }

    fun loadPresets(uid: String) {
        if (uid.isBlank()) return

        viewModelScope.launch {
            _state.value = PostManageState.Loading
            try {
                val presetIds = userRepository.getPresetIdsOf(uid)
                val presets = userRepository.getPresetsBy(presetIds)
                _state.value = PostManageState.Success(presets)
            } catch (e: Exception) {
                _state.value = PostManageState.Error
            }
        }
    }

    companion object {
        private const val MAX_PRESET_NUM = "5"
    }
}
