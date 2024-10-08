package com.example.ead_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ead_kotlin.ui.theme.EadKotlinTheme
import com.example.ead_kotlin.screens.*
import com.example.ead_kotlin.viewmodels.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EadKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()

    // Create the CartViewModel here to maintain its state
    val cartViewModel: CartViewModel = viewModel() // Ensure this is imported

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen( navController) }
        composable("products") { ProductListScreen(navController) }
        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            productId?.let { ProductDetailScreen(cartViewModel,navController, it) }
        }
        // Pass the ViewModel to the CartScreen
        composable("cart") { CartScreen(cartViewModel, navController) } // Updated to pass viewModel
        composable("orders") { OrderListScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("vendor_rating/{vendorId}") { backStackEntry ->
            val vendorId = backStackEntry.arguments?.getString("vendorId")
            vendorId?.let { VendorRatingScreen(navController, it) }
        }
    }
}


//package com.example.ead_kotlin
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.ead_kotlin.ui.theme.EadKotlinTheme
//import com.example.ead_kotlin.screens.*
//import com.example.ead_kotlin.viewmodels.*
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            EadKotlinTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    // Create UserPreferences instance here
//                    MainContent()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MainContent() {
//    val navController = rememberNavController()
//    val userViewModel: UserViewModel = viewModel()
//    // Create the CartViewModel here to maintain its state
//    val cartViewModel: CartViewModel = viewModel() // Ensure this is imported
//
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") { LoginScreen(navController, userViewModel) }
//        composable("register") { RegisterScreen(navController) }
//        composable("home") { HomeScreen( navController, userViewModel) }
//        composable("products") { ProductListScreen(navController) }
//        composable("product/{productId}") { backStackEntry ->
//            val productId = backStackEntry.arguments?.getString("productId")
//            productId?.let { ProductDetailScreen(cartViewModel,navController, it) }
//        }
//        // Pass the ViewModel to the CartScreen
//        composable("cart") { CartScreen(cartViewModel, navController, userViewModel) } // Updated to pass viewModel
//        composable("orders") { OrderListScreen(navController, userViewModel) }
//        composable("profile") { ProfileScreen(navController) }
//        composable("vendor_rating/{vendorId}") { backStackEntry ->
//            val vendorId = backStackEntry.arguments?.getString("vendorId")
//            vendorId?.let { VendorRatingScreen(navController, it) }
//        }
//    }
//}



//package com.example.ead_kotlin
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.ead_kotlin.data.UserPreferences
//import com.example.ead_kotlin.ui.theme.EadKotlinTheme
//import com.example.ead_kotlin.screens.*
//import com.example.ead_kotlin.viewmodels.*
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            EadKotlinTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    // Create UserPreferences instance here
//                    val userPreferences = UserPreferences(this)
//                    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(userPreferences))
//                    MainContent(loginViewModel)
//                }
//            }
//        }
//    }
//}
//
//
//
//class LoginViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//            return LoginViewModel(userPreferences) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//
//@Composable
//fun MainContent(loginViewModel: LoginViewModel) {
//    val navController = rememberNavController()
//    val cartViewModel: CartViewModel = viewModel() // Ensure this is imported
//
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") { LoginScreen(navController, loginViewModel) }
//        composable("register") { RegisterScreen(navController) }
//        composable("home") { HomeScreen(navController, loginViewModel) }
//        composable("products") { ProductListScreen(navController) }
//        composable("product/{productId}") { backStackEntry ->
//            val productId = backStackEntry.arguments?.getString("productId")
//            productId?.let { ProductDetailScreen(cartViewModel, navController, it) }
//        }
//        composable("cart") { CartScreen(cartViewModel, navController, loginViewModel) }
//        composable("orders") { OrderListScreen(navController, loginViewModel) }
//        composable("profile") { ProfileScreen(navController) }
//        composable("vendor_rating/{vendorId}") { backStackEntry ->
//            val vendorId = backStackEntry.arguments?.getString("vendorId")
//            vendorId?.let { VendorRatingScreen(navController, it) }
//        }
//    }
//}
