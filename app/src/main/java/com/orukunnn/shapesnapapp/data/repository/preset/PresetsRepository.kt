package com.orukunnn.shapesnapapp.data.repository.preset

import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.coroutines.flow.Flow

interface PresetsRepository {
    fun getPresetsFlow(): Flow<List<Preset>>
    suspend fun getInitialPresets(): Pair<List<Preset>, DocumentSnapshot?>
    suspend fun getPostedPresetsOf(userId: String): List<Preset>
}
