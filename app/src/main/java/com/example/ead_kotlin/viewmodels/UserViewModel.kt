package com.example.ead_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.data.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun saveUserDetails(token: String) {
        viewModelScope.launch {
            userPreferences.saveAuthToken(token)
        }
    }
}
