//package com.example.ead_kotlin.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.ShoppingCart
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.ead_kotlin.viewmodels.HomeViewModel
//import com.example.ead_kotlin.ui.components.ProductListItem
//import com.example.ead_kotlin.viewmodels.UserViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(navController: NavController) {
//    val viewModel = remember { HomeViewModel() }
//    val featuredProducts by viewModel.featuredProducts.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.getFeaturedProducts()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Home") },
//                actions = {
//                    IconButton(onClick = { navController.navigate("cart") }) {
//                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
//                    }
//                    IconButton(onClick = { navController.navigate("profile") }) {
//                        Icon(Icons.Default.Person, contentDescription = "Profile")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            item {
//                Text(
//                    "Featured Products",
//                    style = MaterialTheme.typography.headlineSmall,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//            items(featuredProducts) { product ->
//                ProductListItem(product) {
//                    navController.navigate("product/${product.Id}")
//                }
//            }
//            item {
//                Button(
//                    onClick = { navController.navigate("products") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                )
//                {
//                    Text("View All Products")
//                }
//
//                Button(
//                    onClick = { navController.navigate("category") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                )
//                {
//                    Text("View All Categories")
//                }
//
//                Button(
//                    onClick = { navController.navigate("orders") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                )
//                {
//                    Text("View All Orders")
//                }
//            }
//        }
//    }
//}

package com.example.ead_kotlin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ead_kotlin.viewmodels.HomeViewModel
import com.example.ead_kotlin.ui.components.ProductListItem
import com.example.ead_kotlin.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = remember { HomeViewModel() }
    val featuredProducts by viewModel.featuredProducts.collectAsState()
    var searchText by remember { mutableStateOf("") }

    // States for filter checkboxes
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedVendorRatings by remember { mutableStateOf(setOf<Float>()) }

    LaunchedEffect(Unit) {
        viewModel.getFeaturedProducts()
    }

    // Function to filter products based on search text and selected filters
    val filteredProducts = featuredProducts.filter { product ->
        (product.Name.contains(searchText, ignoreCase = true) ||
//                (product.Price.toString() == searchText)||
                product.Category.Name.contains(searchText, ignoreCase = true) ||
                product.Vendor.FirstName.contains(searchText, ignoreCase = true)) &&
                (selectedCategories.isEmpty() || selectedCategories.contains(product.Category.Name))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search Products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Products List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        "Featured Products",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(filteredProducts) { product ->
                    ProductListItem(product) {
                        navController.navigate("product/${product.Id}")
                    }
                }
                item {
                    Button(
                        onClick = { navController.navigate("products") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("View All Products")
                    }

                    Button(
                        onClick = { navController.navigate("category") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("View All Categories")
                    }

                    Button(
                        onClick = { navController.navigate("orders") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("View All Orders")
                    }
                }
            }
        }
    }
}
