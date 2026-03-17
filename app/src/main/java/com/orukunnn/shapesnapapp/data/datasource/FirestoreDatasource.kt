package com.orukunnn.shapesnapapp.data.datasource

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetEntity
import com.orukunnn.shapesnapapp.data.model.user.UserEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreDatasource(
    private val firestore: FirebaseFirestore
) {

    fun getPresetsFlow(): Flow<List<PresetEntity>> = callbackFlow {
        val subscription = firestore.collection(PRESETS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val presets = snapshot.toObjects(PresetEntity::class.java)
                    trySend(presets)
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getSavedPresetsFlow(userId: String): Flow<List<PresetEntity>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(emptyList())
            return@callbackFlow
        }
        val subscription = firestore.collection(PRESETS_COLLECTION)
            .whereArrayContains("savedUserIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val presets = snapshot.toObjects(PresetEntity::class.java)
                    trySend(presets)
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getUserFlow(userId: String): Flow<UserEntity?> = callbackFlow {
        if (userId.isBlank()) {
            trySend(null)
            return@callbackFlow
        }
        val subscription = firestore.collection(USERS_COLLECTION).document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(UserEntity::class.java))
                } else {
                    trySend(null)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun getPresetEntities(
        limit: Long,
        lastVisibleDocument: DocumentSnapshot? = null
    ): Pair<List<PresetEntity>, DocumentSnapshot?> {
        var query = firestore.collection(PRESETS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
        if (lastVisibleDocument != null) {
            query = query.startAfter(lastVisibleDocument)
        }
        val querySnapshot = query.get().await()
        val presetEntities = querySnapshot.toObjects(PresetEntity::class.java)
        val lastDocInPage = querySnapshot.documents.lastOrNull()

        return Pair(presetEntities, lastDocInPage)
    }

    suspend fun getPresetIdsOf(userId: String): List<String> {
        if (userId.isBlank()) return emptyList()
        val userRef = firestore.collection(USERS_COLLECTION).document(userId)
        val snapshot = userRef.get().await()
        return if (snapshot.exists()) {
            val user = snapshot.toObject(UserEntity::class.java)
            user?.posts ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getStorageIdsOf(userId: String): List<String> {
        if (userId.isBlank()) return emptyList()
        val userRef = firestore.collection(USERS_COLLECTION).document(userId)
        val snapshot = userRef.get().await()
        return if (snapshot.exists()) {
            val user = snapshot.toObject(UserEntity::class.java)
            user?.storage ?: emptyList()
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

    suspend fun addStorageBy(presetId: String, userId: String) {
        if (userId.isBlank()) return
        if (presetId.isBlank()) return
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update("storage", FieldValue.arrayUnion(presetId))
            .await()
        firestore.collection(PRESETS_COLLECTION)
            .document(presetId)
            .update("savedUserIds", FieldValue.arrayUnion(userId))
            .await()
    }

    suspend fun removeStorageBy(presetId: String, userId: String) {
        if (userId.isBlank()) return
        if (presetId.isBlank()) return
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update("storage", FieldValue.arrayRemove(presetId))
            .await()
        firestore.collection(PRESETS_COLLECTION)
            .document(presetId)
            .update("savedUserIds", FieldValue.arrayRemove(userId))
            .await()
    }

    suspend fun toggleLike(presetId: String, userId: String) {
        if (userId.isBlank()) return
        if (presetId.isBlank()) return
        val presetRef = firestore.collection(PRESETS_COLLECTION).document(presetId)
        val snapshot = presetRef.get().await()
        val preset = snapshot.toObject(PresetEntity::class.java) ?: return

        if (preset.likedUserIds.contains(userId)) {
            presetRef.update("likedUserIds", FieldValue.arrayRemove(userId)).await()
        } else {
            presetRef.update("likedUserIds", FieldValue.arrayUnion(userId)).await()
        }
    }

    suspend fun getUser(userId: String): UserEntity? {
        if (userId.isBlank()) return null
        return firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(UserEntity::class.java)
    }

    suspend fun saveUser(user: UserEntity) {
        if (user.uid.isBlank()) return
        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(user)
            .await()
    }

    companion object {
        private const val PRESETS_COLLECTION = "presets"
        private const val USERS_COLLECTION = "users"
    }
}
