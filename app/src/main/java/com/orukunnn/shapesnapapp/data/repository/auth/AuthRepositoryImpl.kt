package com.orukunnn.shapesnapapp.data.repository.auth

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val firebaseAuthDatasource: FirebaseAuthDatasource
) : AuthRepository {
    override val currentUser: Flow<FirebaseUser?> = firebaseAuthDatasource.currentUserFlow

    override suspend fun signInWithGoogle(context: Context, serverClientId: String): FirebaseUser? {
        return try {
            firebaseAuthDatasource.signInWithGoogle(context, serverClientId)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signInWithGoogle: ${e.message}")
            null
        }
    }

    override suspend fun signOut(context: Context) {
        try {
            firebaseAuthDatasource.signOut(context)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signOut: ${e.message}")
        }
    }
}
