package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset

class UserRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource
) : UserRepository {
    override suspend fun getPresetIdsOf(userId: String): List<String> {
        return firestoreDatasource.getPresetIdsOf(userId)
    }

    override suspend fun getPresetsBy(presetIds: List<String>): List<Preset> {
        return firestoreDatasource.getPresetsBy(presetIds)
    }

    override suspend fun deletePresetBy(presetId: String, userId: String) {
        return firestoreDatasource.deletePresetBy(presetId, userId)
    }

    override suspend fun saveUserIfNotExists(userId: String) {
        firestoreDatasource.saveUserIfNotExists(userId)
    }
}