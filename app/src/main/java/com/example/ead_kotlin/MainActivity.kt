package com.example.ead_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("products") { ProductListScreen(navController) }
        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            productId?.let { ProductDetailScreen(navController, it) }
        }
        composable("cart") { CartScreen(navController) }
        composable("orders") { OrderListScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("vendor_rating/{vendorId}") { backStackEntry ->
            val vendorId = backStackEntry.arguments?.getString("vendorId")
            vendorId?.let { VendorRatingScreen(navController, it) }
        }
    }
}