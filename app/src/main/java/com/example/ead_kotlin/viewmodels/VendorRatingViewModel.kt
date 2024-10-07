package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.VendorRatingCreateDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VendorRatingViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _ratingStatus = MutableStateFlow<RatingStatus>(RatingStatus.Idle)
    val ratingStatus: StateFlow<RatingStatus> = _ratingStatus

    fun submitRating(vendorId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            _ratingStatus.value = RatingStatus.Loading
            try {
                val ratingDto = VendorRatingCreateDto(vendorId, rating, comment)
                val response = apiService.createVendorRating(ratingDto)
                if (response.isSuccessful) {
                    _ratingStatus.value = RatingStatus.Success
                } else {
                    _ratingStatus.value = RatingStatus.Error("Failed to submit rating: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _ratingStatus.value = RatingStatus.Error("Error: ${e.message}")
            }
        }
    }
}

sealed class RatingStatus {
    object Idle : RatingStatus()
    object Loading : RatingStatus()
    object Success : RatingStatus()
    data class Error(val message: String) : RatingStatus()
}