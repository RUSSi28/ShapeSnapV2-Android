package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.User
import com.orukunnn.shapesnapapp.data.model.user.toUserEntity

class UserRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource
) : UserRepository {
    override suspend fun getPresetIdsOf(userId: String): List<String> {
        return firestoreDatasource.getPresetIdsOf(userId)
    }

    override suspend fun getStorageIdsOf(userId: String): List<String> {
        return firestoreDatasource.getStorageIdsOf(userId)
    }

    override suspend fun getPresetsBy(presetIds: List<String>): List<Preset> {
        return firestoreDatasource.getPresetsBy(presetIds)
    }

    override suspend fun deletePresetBy(presetId: String, userId: String) {
        return firestoreDatasource.deletePresetBy(presetId, userId)
    }

    override suspend fun addStorageBy(presetId: String, userId: String) {
        return firestoreDatasource.addStorageBy(presetId, userId)
    }

    override suspend fun removeStorageBy(presetId: String, userId: String) {
        return firestoreDatasource.removeStorageBy(presetId, userId)
    }

    override suspend fun saveUserIfNotExists(userId: String) {
        val user = firestoreDatasource.getUser(userId)?.let { User(it) }
        if (user != null) return
        val newUser = User(
            uid = userId,
            posts = emptyList(),
            storage = emptyList(),
        )
        firestoreDatasource.saveUser(
            newUser.toUserEntity()
        )
    }
}