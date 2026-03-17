package com.orukunnn.shapesnapapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.SharedPreferenceDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.User
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetsRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: StateFlow<User?> = authRepository.currentUser
        .flatMapLatest { firebaseUser ->
            if (firebaseUser != null) {
                userRepository.getUserFlow(firebaseUser.uid)
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val state: StateFlow<HomeState> = presetsRepository.getPresetsFlow()
        .map<List<Preset>, HomeState> { presets -> HomeState.Success(presets) }
        .catch { e ->
            Log.e("HomeScreenViewModel", "Error fetching presets: ${e.message}")
            emit(HomeState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState.Loading
        )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _showLimitReachedDialog = MutableStateFlow(false)
    val showLimitReachedDialog = _showLimitReachedDialog.asStateFlow()

    fun dismissLimitDialog() {
        _showLimitReachedDialog.value = false
    }

    fun refreshPresets() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // FirestoreのデータはFlowでリアルタイムに更新されますが、
            // インジケータのアニメーションを適切に完了させるために、
            // またユーザーに更新が行われたことを視覚的に伝えるために、一定時間のディレイを入れます。
            delay(1000)
            _isRefreshing.value = false
        }
    }

    fun toggleLike(presetId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            try {
                userRepository.toggleLike(presetId, user.uid)
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to toggle like: ${e.message}")
            }
        }
    }

    fun saveToStorage(presetId: String) {
        val user = currentUser.value ?: return
        val currentState = state.value
        if (currentState !is HomeState.Success) return

        val isAlreadySaved = currentState.presets.find { it.presetId == presetId }
            ?.savedUserIds?.contains(user.uid) ?: false

        if (!isAlreadySaved && !user.isSubscribed && user.storage.size >= FREE_LIMIT) {
            _showLimitReachedDialog.value = true
            return
        }

        viewModelScope.launch {
            try {
                if (isAlreadySaved) {
                    userRepository.removeStorageBy(presetId, user.uid)
                } else {
                    userRepository.addStorageBy(presetId, user.uid)
                }
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to toggle storage: ${e.message}")
            }
        }
    }

    companion object {
        const val FREE_LIMIT = 5
    }
}
