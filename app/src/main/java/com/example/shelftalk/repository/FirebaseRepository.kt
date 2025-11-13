package com.example.shelftalk.repository

import com.example.shelftalk.models.Review
import com.example.shelftalk.models.Summary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Authentication
    suspend fun register(email: String, password: String): Result<String> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success("Registration successful")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success("Login successful")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    fun getCurrentUserEmail() = auth.currentUser?.email ?: ""

    fun isUserLoggedIn() = auth.currentUser != null

    // Reviews
    suspend fun addReview(review: Review): Result<String> {
        return try {
            val docRef = firestore.collection("reviews").document()
            val reviewWithId = review.copy(id = docRef.id)
            docRef.set(reviewWithId).await()
            Result.success("Review added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllReviews(): Result<List<Review>> {
        return try {
            val snapshot = firestore.collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val reviews = snapshot.documents.mapNotNull { it.toObject(Review::class.java) }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Summaries
    suspend fun addSummary(summary: Summary): Result<String> {
        return try {
            val docRef = firestore.collection("summaries").document()
            val summaryWithId = summary.copy(id = docRef.id)
            docRef.set(summaryWithId).await()
            Result.success("Summary added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllSummaries(): Result<List<Summary>> {
        return try {
            val snapshot = firestore.collection("summaries")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val summaries = snapshot.documents.mapNotNull { it.toObject(Summary::class.java) }
            Result.success(summaries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}