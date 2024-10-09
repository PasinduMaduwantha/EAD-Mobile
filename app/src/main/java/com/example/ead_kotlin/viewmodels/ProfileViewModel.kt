package com.example.ead_kotlin.viewmodels

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ead_kotlin.api.ApiService
import com.example.ead_kotlin.api.UserDto
import com.example.ead_kotlin.api.UserProfileDto
import com.example.ead_kotlin.api.UserStatusUpdateDto
import com.example.ead_kotlin.api.UserUpdateDto
import com.example.ead_kotlin.data.UserPreferences
import com.example.ead_kotlin.data.CartManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val cartManager: CartManager
) : ViewModel() {
    private val apiService = ApiService.create()

    private val _userProfile = MutableStateFlow<UserProfileDto?>(null)
    val userProfile: StateFlow<UserProfileDto?> = _userProfile

    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus

    private val _deactivate = MutableStateFlow<String?>(null)
    val deactivate: StateFlow<String?> = _deactivate

    fun getUserProfile() {
        viewModelScope.launch {
            try {
                val response = apiService.getUserProfile("Bearer ${UserSession.token}")
                if (response.isSuccessful) {
                    _userProfile.value = response.body()?.Data
                } else {
                    _updateStatus.value = "Error: Unable to fetch profile"
                }
            } catch (e: Exception) {
                _updateStatus.value = "Error: ${e.message}"
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, email: String, age: Int, status: String) {
        viewModelScope.launch {
            try {
                val userUpdateDto = UserUpdateDto(firstName, lastName, email, age, status)
                val response = apiService.updateUserProfile("Bearer ${UserSession.token}", userUpdateDto)
                if (response.isSuccessful) {
                    _userProfile.value = response.body()?.Data
                    _updateStatus.value = "Profile updated successfully"
                } else {
                    _updateStatus.value = "Error: Unable to update profile"
                }
            } catch (e: Exception) {
                _updateStatus.value = "Error: ${e.message}"
            }
        }
    }fun updateProfileStatus(id: String, status: String) {
        viewModelScope.launch {
            try {
                val userStatusUpdateDto = UserStatusUpdateDto(status)
                val response = apiService.updateUserProfileStatus("Bearer ${UserSession.token}", id,userStatusUpdateDto)
                if (response.isSuccessful) {
                    _userProfile.value = response.body()?.Data
                    _deactivate.value = "Successfully sent Deactivate Profile Request"
                } else {
                    _deactivate.value = "Error: Unable to update profile"
                }
            } catch (e: Exception) {
                _deactivate.value = "Error: ${e.message}"
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                userPreferences.clearAuthToken()
                userPreferences.clearAllUserData()
                _userProfile.value = null
                cartManager.clearCart()
                _updateStatus.value = "Logout successful"
                onLogoutComplete()
            } catch (e: Exception) {
                _updateStatus.value = "Error during logout: ${e.message}"
            }
        }
    }
}

class ProfileViewModelFactory(
    private val userPreferences: UserPreferences,
    private val cartManager: CartManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userPreferences, cartManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}