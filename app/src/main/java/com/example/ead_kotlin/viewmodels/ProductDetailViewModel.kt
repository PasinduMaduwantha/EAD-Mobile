package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.ProductDto
import com.example.ead_kotlin.api.VendorRatingCreateDto
import com.example.ead_kotlin.viewmodels.UserSession.token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _product = MutableStateFlow<ProductDto?>(null)
    val product: StateFlow<ProductDto?> = _product

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _ratingSubmissionStatus = MutableStateFlow<RatingSubmissionStatus>(RatingSubmissionStatus.Initial)
    val ratingSubmissionStatus: StateFlow<RatingSubmissionStatus> = _ratingSubmissionStatus

    fun getProductById(id: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getProductById(id)

                if (response.isSuccessful && response.isSuccessful == true) {
                    _product.value = response.body()?.Data
                } else {
                    _errorMessage.value = response.body()?.Message?: "Failed to fetch product: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
    fun submitVendorRating(vendorId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            try {
                val ratingDto = VendorRatingCreateDto(vendorId, rating, comment)
                val response = token?.let { apiService.createVendorRating(it, ratingDto) }

                if (response != null) {
                    if (response.isSuccessful && response.body()?.IsSuccessful == true) {
                        _ratingSubmissionStatus.value = RatingSubmissionStatus.Success
                    } else {
                        _ratingSubmissionStatus.value = RatingSubmissionStatus.Error(response.body()?.Message ?: "Failed to submit rating")
                    }
                }
            } catch (e: Exception) {
                _ratingSubmissionStatus.value = RatingSubmissionStatus.Error("Error: ${e.message}")
            }
        }
    }
}

sealed class RatingSubmissionStatus {
    object Initial : RatingSubmissionStatus()
    object Success : RatingSubmissionStatus()
    data class Error(val message: String) : RatingSubmissionStatus()
}