package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.GroupedProductsDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _groupedProducts = MutableStateFlow<GroupedProductsDto?>(null)
    val groupedProducts: StateFlow<GroupedProductsDto?> = _groupedProducts

    fun getGroupedProducts() {
        viewModelScope.launch {
            try {
                val response = apiService.getProductsByCategory()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.IsSuccessful == true) {
                        println("category res: ${responseBody.Data.categories}")
                        _groupedProducts.value = responseBody.Data
                    } else {
                        _errorMessage.value = responseBody?.Message ?: "Failed to fetch Categories"
                    }
                } else {
                    _errorMessage.value = "Failed to fetch Categories: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}


