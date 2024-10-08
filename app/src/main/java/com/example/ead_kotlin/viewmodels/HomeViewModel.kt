package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _featuredProducts = MutableStateFlow<List<ProductDto>>(emptyList())
    val featuredProducts: StateFlow<List<ProductDto>> = _featuredProducts

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getFeaturedProducts() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllProducts()
                if (response.isSuccessful && response.body()?.IsSuccessful == true) {
                    // Get all products and take the first 4 as featured products
                    _featuredProducts.value = response.body()?.Data?.take(4) ?: emptyList()
                } else {
                    _errorMessage.value = response.body()?.Message
                        ?: "Failed to fetch featured products: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}