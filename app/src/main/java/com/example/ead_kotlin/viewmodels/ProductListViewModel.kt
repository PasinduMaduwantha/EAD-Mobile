package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> = _products

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getAllProducts() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllProducts()
                if (response.isSuccessful && response.isSuccessful == true) {
                    println("get all products: "+response.body())
                    _products.value = response.body()?.Data ?: emptyList()
                } else {
                    _errorMessage.value = response.body()?.Message
                        ?: "Failed to fetch products: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}
