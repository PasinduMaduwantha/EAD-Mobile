package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _product = MutableStateFlow<ProductDto?>(null)
    val product: StateFlow<ProductDto?> = _product

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

}