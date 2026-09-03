package com.vitalsense.app.feature.auth

import com.google.firebase.auth.FirebaseAuth
import com.vitalsense.app.core.data.model.UserRole
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationManager @Inject constructor() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * In a full production app, this would use FirebaseAuth.signInWithEmailAndPassword
     * or Phone Auth. For this phase, we map hardcoded roles to Firebase Anonymous Auth
     * or custom tokens. 
     */
    suspend fun signIn(role: UserRole, identifier: String, credential: String? = null): Boolean {
        return try {
            // Placeholder: Always sign in anonymously for this phase
            // (We assume Firestore rules allow anonymous writes in test mode)
            if (firebaseAuth.currentUser == null) {
                firebaseAuth.signInAnonymously().await()
            }
            // Real auth logic would parse 'identifier' and 'credential'
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid
}
