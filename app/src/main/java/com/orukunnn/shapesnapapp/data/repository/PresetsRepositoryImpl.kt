package com.orukunnn.shapesnapapp.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.Preset

class PresetsRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource
) : PresetsRepository {
    override suspend fun getFirstPresets(): Pair<List<Preset>, DocumentSnapshot?> {
        return try {
            firestoreDatasource.getPresets(PAGE_SIZE, null)
        } catch (e: Exception) {
            Log.d("PresetsRepositoryImpl", "getFirstPresets: ${e.message}")
            Pair(emptyList(), null)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10L
    }
}