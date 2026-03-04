package com.orukunnn.shapesnapapp.data.repository.auth

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<FirebaseUser?>
    suspend fun signInWithGoogle(context: Context, serverClientId: String): FirebaseUser?
    suspend fun signOut(context: Context)
}
