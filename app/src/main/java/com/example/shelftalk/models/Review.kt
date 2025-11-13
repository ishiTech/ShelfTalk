package com.example.shelftalk.models

data class Review(
    val id: String = "",
    val bookTitle: String = "",
    val author: String = "",
    val rating: Float = 0f,
    val reviewText: String = "",
    val userEmail: String = "",
    val timestamp: Long = 0L
)