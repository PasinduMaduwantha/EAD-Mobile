package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.GetOrderDto
import com.example.ead_kotlin.api.OrderDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderListViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _orders = MutableStateFlow<List<GetOrderDto>>(emptyList())
    val orders: StateFlow<List<GetOrderDto>> = _orders

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _orderCancel = MutableStateFlow<String?>(null)
    val orderCancel: StateFlow<String?> = _orderCancel

    fun getCustomerOrders(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCustomerOrders("Bearer $token")
                println("orders :"+response)
                if (response.isSuccessful) {
                    _orders.value = response.body()?.Data ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to fetch orders: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun cancelOrder(token: String?, orderId: String, order: GetOrderDto){
        val orderDto = OrderDto(orderId,order.CustomerId, order.OrderItems, order.TotalAmount, "Cancel Order")

        viewModelScope.launch {
            try{
                val response = apiService.cancelOrder("Bearer $token", orderId, orderDto)
                if(response.isSuccessful){
                    _orderCancel.value = response.body()?.Data?.OrderStatus
                }else{
                    _errorMessage.value = "Failed to Cancel the order: ${response.errorBody()?.string()}"
                }
            }catch(e: Exception){
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}