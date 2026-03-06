package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.model.preset.Preset

interface UserRepository {
    suspend fun getPresetIdsOf(userId: String): List<String>
    suspend fun getPresetsBy(presetIds: List<String>): List<Preset>
    suspend fun deletePresetBy(presetId: String, userId: String)
    suspend fun saveUserIfNotExists(userId: String)
}
