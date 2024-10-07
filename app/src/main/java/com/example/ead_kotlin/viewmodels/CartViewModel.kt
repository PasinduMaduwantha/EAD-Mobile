package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.CreateOrderDto
import com.example.ead_kotlin.api.OrderItemDto
import com.example.ead_kotlin.api.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartItem(val product: ProductDto, var quantity: Int)

class CartViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    fun addToCart(product: ProductDto) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(product, 1))
        }
        _cartItems.value = currentItems
        updateTotalAmount()
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
        updateTotalAmount()
    }

    fun increaseQuantity(productId: String) {
        updateQuantity(productId, 1)
    }

    fun decreaseQuantity(productId: String) {
        updateQuantity(productId, -1)
    }

    private fun updateQuantity(productId: String, change: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.product.id == productId }
        item?.let {
            it.quantity += change
            if (it.quantity <= 0) {
                currentItems.remove(it)
            }
        }
        _cartItems.value = currentItems
        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.product.price * it.quantity }
    }

    fun placeOrder(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val orderItems = _cartItems.value.map { OrderItemDto(it.product.id, it.quantity) }
                val createOrderDto = CreateOrderDto(orderItems)
                val response = apiService.createOrder(createOrderDto)
                if (response.isSuccessful) {
                    _cartItems.value = emptyList()
                    _totalAmount.value = 0.0
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}