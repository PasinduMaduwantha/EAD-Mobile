//package com.example.ead_kotlin.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ead_kotlin.api.ApiService
//import com.example.ead_kotlin.api.LoginDto
//import com.example.ead_kotlin.api.LoginResponse
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import androidx.lifecycle.ViewModelProvider
//import com.example.ead_kotlin.data.UserPreferences
//
//
//class LoginViewModel() : ViewModel() {
//    private val apiService = ApiService.create()
//
//    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
//    val loginState: StateFlow<LoginState> = _loginState
//
//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _loginState.value = LoginState.Loading
//            try {
//                val response = apiService.login(LoginDto(email, password))
//                if (response.isSuccessful) {
//                    val loginResponse = response.body()
//                    if (loginResponse != null) {
//                        // TODO: Save token and user details to local storage
//
//                        _loginState.value = LoginState.Success(loginResponse)
//                    } else {
//                        _loginState.value = LoginState.Error("Empty response body")
//                    }
//                } else {
//                    _loginState.value = LoginState.Error("Login failed: ${response.errorBody()?.string()}")
//                }
//            } catch (e: Exception) {
//                _loginState.value = LoginState.Error("Network error: ${e.message}")
//            }
//        }
//    }
//}
//
//sealed class LoginState {
//    object Idle : LoginState()
//    object Loading : LoginState()
//    data class Success(val loginResponse: LoginResponse) : LoginState()
//    data class Error(val message: String) : LoginState()
//}


package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.LoginDto
import com.example.ead_kotlin.api.LoginResponse // Import the UserSession object
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginDto(email, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Store the token and email in the UserSession
                        println("token in login"+ loginResponse.Data.Token)
                        val token = loginResponse.Data.Token
                        val user = loginResponse.Data.User

                        println("user status: "+ user.Status)

                        if(user.Status == "active"){
                            // Store the token in UserSession
                            UserSession.token = token
                            _loginState.value = LoginState.Success(loginResponse)
                        }else{
                            _loginState.value = LoginState.Error("User is not Activated yet.")
                        }
                    } else {
                        _loginState.value = LoginState.Error("Empty response body")
                    }
                } else {
                    _loginState.value = LoginState.Error("Login failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Network error: ${e.message}")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val loginResponse: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
