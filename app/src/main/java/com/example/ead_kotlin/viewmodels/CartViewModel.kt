package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.CreateOrderDto
import com.example.ead_kotlin.api.OrderItemDto
import com.example.ead_kotlin.api.ProductDto
import com.example.ead_kotlin.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CartItem(val product: ProductDto, var quantity: Int)

class CartViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    fun addToCart(product: ProductDto) {
        println("cart item: $product")
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.Id == product.Id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(product, 1))
        }
        _cartItems.value = currentItems
        updateTotalAmount()

        println("all items: $cartItems")
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.Id != productId }
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
        val item = currentItems.find { it.product.Id == productId }
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
        _totalAmount.value = _cartItems.value.sumOf { it.product.Price * it.quantity }
    }

    fun placeOrder(token: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Use the token in the API request
            val orderItems = _cartItems.value.map { OrderItemDto(it.product.Id, it.quantity) }
            val createOrderDto = CreateOrderDto(orderItems)
            val response = apiService.createOrder("Bearer $token", createOrderDto) // Assuming API expects "Bearer" token

            if (response.isSuccessful) {
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }


//    fun placeOrder(onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val orderItems = _cartItems.value.map { OrderItemDto(it.product.Id, it.quantity) }
//                val createOrderDto = CreateOrderDto(orderItems)
//                val response = apiService.createOrder("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYWRlZXBhbGFrc2hhbjA4MDRAZ21haWwuY29tIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoidmVuZG9yIiwianRpIjoiYTRmY2IxNDEtMjY0Zi00N2NkLWJiZTktZGE0ZWY5MDYyOWU1IiwidXNlcklkIjoiNjcwM2ZhNGE3MzIxMmM2NDg5MDg4NTU1IiwiZXhwIjoxNzI5NDY4MDA4LCJpc3MiOiJ5b3VyX2lzc3VlciIsImF1ZCI6InlvdXJfYXVkaWVuY2UifQ.b_38jKXDbtbb8-R5wxhq0I3ebOPDhuFAQh_x2HrPQVg", createOrderDto)
//                if (response.isSuccessful) {
//                    _cartItems.value = emptyList()
//                    _totalAmount.value = 0.0
//                    onResult(true)
//                } else {
//                    onResult(false)
//                }
//            } catch (e: Exception) {
//                onResult(false)
//            }
//        }
//    }
}

//package com.example.ead_kotlin.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ead_kotlin.api.ApiService
//import com.example.ead_kotlin.api.CreateOrderDto
//import com.example.ead_kotlin.api.OrderItemDto
//import com.example.ead_kotlin.api.ProductDto
//import com.example.ead_kotlin.data.UserPreferences
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//
//data class CartItem(val product: ProductDto, var quantity: Int)
//
//class CartViewModel(private val userPreferences: UserPreferences) : ViewModel() {
//    private val apiService = ApiService.create()
//
//    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
//    val cartItems: StateFlow<List<CartItem>> = _cartItems
//
//    private val _totalAmount = MutableStateFlow(0.0)
//    val totalAmount: StateFlow<Double> = _totalAmount
//
//    fun addToCart(product: ProductDto) {
//        val currentItems = _cartItems.value.toMutableList()
//        val existingItem = currentItems.find { it.product.Id == product.Id }
//        if (existingItem != null) {
//            existingItem.quantity++
//        } else {
//            currentItems.add(CartItem(product, 1))
//        }
//        _cartItems.value = currentItems
//        updateTotalAmount()
//    }
//
//    fun removeFromCart(productId: String) {
//        _cartItems.value = _cartItems.value.filter { it.product.Id != productId }
//        updateTotalAmount()
//    }
//
//    fun increaseQuantity(productId: String) {
//        updateQuantity(productId, 1)
//    }
//
//    fun decreaseQuantity(productId: String) {
//        updateQuantity(productId, -1)
//    }
//
//    private fun updateQuantity(productId: String, change: Int) {
//        val currentItems = _cartItems.value.toMutableList()
//        val item = currentItems.find { it.product.Id == productId }
//        item?.let {
//            it.quantity += change
//            if (it.quantity <= 0) {
//                currentItems.remove(it)
//            }
//        }
//        _cartItems.value = currentItems
//        updateTotalAmount()
//    }
//
//    private fun updateTotalAmount() {
//        _totalAmount.value = _cartItems.value.sumOf { it.product.Price * it.quantity }
//    }
//
//    // Place order with JWT token
//    fun placeOrder(onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                // Retrieve the JWT token from UserPreferences
//                val token = userPreferences.authToken.first()
//
//                println("token in place order: $token")
//
//                // If token is null, handle authentication failure
//                if (token.isNullOrEmpty()) {
//                    onResult(false)
//                    return@launch
//                }
//
//                // Create the order
//                val orderItems = _cartItems.value.map { OrderItemDto(it.product.Id, it.quantity) }
//                val createOrderDto = CreateOrderDto(orderItems)
//
//                // Make the API call with the JWT token
//                val response = apiService.createOrder(createOrderDto, "Bearer $token")
//                if (response.isSuccessful) {
//                    _cartItems.value = emptyList()
//                    _totalAmount.value = 0.0
//                    onResult(true)
//                } else {
//                    onResult(false)
//                }
//            } catch (e: Exception) {
//                onResult(false)
//            }
//        }
//    }
//}
