package com.example.shelftalk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftalk.models.Summary
import com.example.shelftalk.repository.FirebaseRepository
import kotlinx.coroutines.launch

class SummaryViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _summaryState = MutableLiveData<SummaryState>()
    val summaryState: LiveData<SummaryState> = _summaryState

    private val _summaries = MutableLiveData<List<Summary>>()
    val summaries: LiveData<List<Summary>> = _summaries

    fun addSummary(bookTitle: String, summaryText: String, lovedPart: String) {
        if (!validateInput(bookTitle, summaryText, lovedPart)) return

        _summaryState.value = SummaryState.Loading
        viewModelScope.launch {
            val summary = Summary(
                bookTitle = bookTitle,
                summaryText = summaryText,
                lovedPart = lovedPart,
                userEmail = repository.getCurrentUserEmail(),
                timestamp = System.currentTimeMillis()
            )

            val result = repository.addSummary(summary)
            _summaryState.value = if (result.isSuccess) {
                SummaryState.Success(result.getOrNull()!!)
            } else {
                SummaryState.Error(result.exceptionOrNull()?.message ?: "Failed to add summary")
            }
        }
    }

    fun loadSummaries() {
        _summaryState.value = SummaryState.Loading
        viewModelScope.launch {
            val result = repository.getAllSummaries()
            if (result.isSuccess) {
                _summaries.value = result.getOrNull()!!
                _summaryState.value = SummaryState.Loaded
            } else {
                _summaryState.value = SummaryState.Error(
                    result.exceptionOrNull()?.message ?: "Failed to load summaries"
                )
            }
        }
    }

    private fun validateInput(bookTitle: String, summaryText: String, lovedPart: String): Boolean {
        if (bookTitle.isBlank() || summaryText.isBlank() || lovedPart.isBlank()) {
            _summaryState.value = SummaryState.Error("All fields are required")
            return false
        }
        return true
    }
}

sealed class SummaryState {
    object Loading : SummaryState()
    object Loaded : SummaryState()
    data class Success(val message: String) : SummaryState()
    data class Error(val message: String) : SummaryState()
}
