package com.orukunnn.shapesnapapp.data.datasource

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetEntity
import com.orukunnn.shapesnapapp.data.model.user.User
import kotlinx.coroutines.tasks.await

class FirestoreDatasource(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPresets(
        limit: Long,
        lastVisibleDocument: DocumentSnapshot?
    ): Pair<List<Preset>, DocumentSnapshot?> {
        var query = firestore.collection(PRESETS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
        if (lastVisibleDocument != null) {
            query = query.startAfter(lastVisibleDocument)
        }
        val querySnapshot = query.get().await()
        Log.d("FirestoreDatasource", "Snapshot size: ${querySnapshot.size()}")
        val presetEntitys = querySnapshot.toObjects(PresetEntity::class.java)
        val presets = presetEntitys.map {
            Preset(it)
        }
        Log.d("FirestoreDatasource", "Converted presets: ${presets.size}")
        val lastDocInPage = querySnapshot.documents.lastOrNull()

        return Pair(presets, lastDocInPage)
    }

    suspend fun saveUserIfNotExists(userId: String) {
        val userRef = firestore.collection(USERS_COLLECTION).document(userId)
        val snapshot = userRef.get().await()
        if (!snapshot.exists()) {
            val newUser = User(
                uid = userId,
                posts = emptyList(),
                storage = emptyList()
            )
            userRef.set(newUser).await()
        }
    }

    companion object {
        private const val PRESETS_COLLECTION = "presets"
        private const val USERS_COLLECTION = "users"
    }
}
