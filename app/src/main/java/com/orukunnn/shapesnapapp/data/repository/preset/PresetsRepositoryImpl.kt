package com.orukunnn.shapesnapapp.data.repository.preset

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset

class PresetsRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource
) : PresetsRepository {

    override suspend fun loadPresetsPage(lastDocument: DocumentSnapshot?): Pair<List<Preset>, DocumentSnapshot?> {
        return try {
            val pair =
                firestoreDatasource.getPresetEntities(PresetsRepository.PAGE_SIZE, lastDocument)
            val presets = pair.first.map { Preset(it) }
            Pair(presets, pair.second)
        } catch (e: Exception) {
            Log.d("PresetsRepositoryImpl", "loadPresetsPage: ${e.message}")
            Pair(emptyList(), null)
        }
    }

    override suspend fun getPostedPresetsOf(userId: String): List<Preset> {
        return try {
            val presetIds = firestoreDatasource.getPresetIdsOf(userId)
            firestoreDatasource.getPresetsBy(presetIds)
        } catch (e: Exception) {
            Log.d("PresetsRepositoryImpl", "getPostedPresetsOf: ${e.message}")
            emptyList()
        }
    }

}
