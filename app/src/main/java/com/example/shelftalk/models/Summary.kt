package com.example.shelftalk.models

data class Summary(
    val id: String = "",
    val bookTitle: String = "",
    val summaryText: String = "",
    val lovedPart: String = "",
    val userEmail: String = "",
    val timestamp: Long = 0L
)