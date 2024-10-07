package com.example.ead_kotlin.viewmodels

import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(firstName: String, lastName: String, email: String, password: String, age: Int, role: String, status: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = apiService.register(UserDto(firstName = firstName, lastName = lastName, email = email, password = password, age = age, role = role, status = status))
                if (response.isSuccessful) {
                    val userDto = response.body()
                    if (userDto != null) {
                        _registerState.value = RegisterState.Success(userDto)
                    } else {
                        _registerState.value = RegisterState.Error("Empty response body")
                    }
                } else {
                    _registerState.value = RegisterState.Error("Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Network error: ${e.message}")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: UserDto) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
