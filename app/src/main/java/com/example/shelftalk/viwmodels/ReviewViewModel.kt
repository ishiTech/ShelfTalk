package com.example.shelftalk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftalk.models.Review
import com.example.shelftalk.repository.FirebaseRepository
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _reviewState = MutableLiveData<ReviewState>()
    val reviewState: LiveData<ReviewState> = _reviewState

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    fun addReview(bookTitle: String, author: String, rating: Float, reviewText: String) {
        if (!validateInput(bookTitle, author, reviewText)) return

        _reviewState.value = ReviewState.Loading
        viewModelScope.launch {
            val review = Review(
                bookTitle = bookTitle,
                author = author,
                rating = rating,
                reviewText = reviewText,
                userEmail = repository.getCurrentUserEmail(),
                timestamp = System.currentTimeMillis()
            )

            val result = repository.addReview(review)
            _reviewState.value = if (result.isSuccess) {
                ReviewState.Success(result.getOrNull()!!)
            } else {
                ReviewState.Error(result.exceptionOrNull()?.message ?: "Failed to add review")
            }
        }
    }

    fun loadReviews() {
        _reviewState.value = ReviewState.Loading
        viewModelScope.launch {
            val result = repository.getAllReviews()
            if (result.isSuccess) {
                _reviews.value = result.getOrNull()!!
                _reviewState.value = ReviewState.Loaded
            } else {
                _reviewState.value = ReviewState.Error(
                    result.exceptionOrNull()?.message ?: "Failed to load reviews"
                )
            }
        }
    }

    private fun validateInput(bookTitle: String, author: String, reviewText: String): Boolean {
        if (bookTitle.isBlank() || author.isBlank() || reviewText.isBlank()) {
            _reviewState.value = ReviewState.Error("All fields are required")
            return false
        }
        return true
    }
}

sealed class ReviewState {
    object Loading : ReviewState()
    object Loaded : ReviewState()
    data class Success(val message: String) : ReviewState()
    data class Error(val message: String) : ReviewState()
}