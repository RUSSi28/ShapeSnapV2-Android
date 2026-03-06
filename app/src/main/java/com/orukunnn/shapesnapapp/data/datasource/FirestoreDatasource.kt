package com.orukunnn.shapesnapapp.data.datasource

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
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
        val presetEntities = querySnapshot.toObjects(PresetEntity::class.java)
        val presets = presetEntities.map {
            Preset(it)
        }
        Log.d("FirestoreDatasource", "Converted presets: ${presets.size}")
        val lastDocInPage = querySnapshot.documents.lastOrNull()

        return Pair(presets, lastDocInPage)
    }

    suspend fun getPresetIdsOf(userId: String): List<String> {
        if (userId.isBlank()) return emptyList()
        val userRef = firestore.collection(USERS_COLLECTION).document(userId)
        val snapshot = userRef.get().await()
        return if (snapshot.exists()) {
            val user = snapshot.toObject(User::class.java)
            user?.posts ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getPresetsBy(presetIds: List<String>): List<Preset> {
        if (presetIds.isEmpty()) return emptyList()
        return presetIds.mapNotNull { id ->
            if (id.isBlank()) return@mapNotNull null
            firestore.collection(PRESETS_COLLECTION)
                .document(id)
                .get()
                .await()
                .toObject(PresetEntity::class.java)
                ?.let { Preset(it) }
        }
    }

    suspend fun deletePresetBy(presetId: String, userId: String) {
        if (userId.isBlank()) return
        if (presetId.isBlank()) return
        firestore.collection(PRESETS_COLLECTION)
            .document(presetId)
            .delete()
            .await()
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update("posts", FieldValue.arrayRemove(presetId))
            .await()
    }

    suspend fun saveUserIfNotExists(userId: String) {
        if (userId.isBlank()) return
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
