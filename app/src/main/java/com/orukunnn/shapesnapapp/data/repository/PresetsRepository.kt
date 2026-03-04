package com.orukunnn.shapesnapapp.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.model.preset.Preset

interface PresetsRepository {
    suspend fun getFirstPresets(): Pair<List<Preset>, DocumentSnapshot?>
//    fun getNextPresetsFrom(presetId: String): List<PresetEntity>
//    fun likePreset(presetId: String)
//    fun unlikePreset(presetId: String)
}