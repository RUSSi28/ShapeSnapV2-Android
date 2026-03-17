package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getSavedPresetsFlow(userId: String): Flow<List<Preset>>
    fun getUserFlow(userId: String): Flow<User?>
    suspend fun getPresetIdsOf(userId: String): List<String>
    suspend fun getStorageIdsOf(userId: String): List<String>
    suspend fun getPresetsBy(presetIds: List<String>): List<Preset>
    suspend fun deletePresetBy(presetId: String, userId: String)
    suspend fun addStorageBy(presetId: String, userId: String)
    suspend fun removeStorageBy(presetId: String, userId: String)
    suspend fun toggleLike(presetId: String, userId: String)
    suspend fun saveUserIfNotExists(userId: String)
}
