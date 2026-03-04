package com.orukunnn.shapesnapapp.data.repository.preset

import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.model.preset.Preset

interface PresetsRepository {
    suspend fun getInitialPresets(): Pair<List<Preset>, DocumentSnapshot?>
    suspend fun getPostedPresetsOf(userId: String): List<Preset>
//    fun getNextPresetsFrom(presetId: String): List<PresetEntity>
//    fun likePreset(presetId: String)
//    fun unlikePreset(presetId: String)
}