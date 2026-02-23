package com.orukunnn.shapesnapapp.data.datasource

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.orukunnn.shapesnapapp.data.model.PresetEntity
import kotlinx.coroutines.tasks.await

class FirestoreDatasource(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPresets(limit: Long, lastVisibleDocument: DocumentSnapshot?): Pair<List<PresetEntity>, DocumentSnapshot?> {
        var query = firestore.collection(PRESETS_COLLECTION)
//            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
        if (lastVisibleDocument != null) {
            query = query.startAfter(lastVisibleDocument)
        }
        val querySnapshot = query.get().await()
        Log.d("FirestoreDatasource", "Snapshot size: ${querySnapshot.size()}")
        val presets = querySnapshot.toObjects<PresetEntity>()
        Log.d("FirestoreDatasource", "Converted presets: ${presets.size}")
        val lastDocInPage = querySnapshot.documents.lastOrNull()

        return Pair(presets, lastDocInPage)
    }

    companion object {
        private const val PRESETS_COLLECTION = "presets"
    }
}