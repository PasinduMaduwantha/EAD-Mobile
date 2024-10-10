//package com.example.ead_kotlin.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.ead_kotlin.api.ProductDto
//import com.example.ead_kotlin.viewmodels.ProductListViewModel
//import com.example.ead_kotlin.viewmodels.UserSession
//import kotlin.collections.groupBy
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CategoryListScreen(navController: NavController) {
//    val viewModel: ProductListViewModel = viewModel()
//    val products by viewModel.products.collectAsState()
//
//    // Group products by category name
//    val groupedProducts = products.groupBy { it.Category.Name }
//
//    LaunchedEffect(Unit) {
//        viewModel.getAllProducts()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Categories") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
//            // Iterate over each category and its associated products
//            groupedProducts.forEach { (categoryName, categoryProducts) ->
//                item {
//                    CategoryCard(categoryName, categoryProducts)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategoryCard(categoryName: String, products: List<ProductDto>) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        Column(modifier = Modifier.padding(2.dp)) {
//            // Display the category name
//            Text(
//                text = "Category: $categoryName",
//                style = MaterialTheme.typography.titleLarge
//            )
//            Spacer(modifier = Modifier.height(2.dp))
//
//            // Display all products within this category
//            products.forEach { product ->
//                ProductItem(product)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun ProductItem(product: ProductDto) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 2.dp)
//    ) {
//        Text(
//            text = "Name: ${product.Name}",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = product.Description,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = "Price: \$${product.Price}",
//            style = MaterialTheme.typography.bodyMedium
//        )
//        Text(
//            text = "Qty: ${product.Qty}",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.weight(1f)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val bitmap = loadImageFromUrl(product.ImageUrl)
//        bitmap?.let {
//            Image(
//                bitmap = it.asImageBitmap(),
//                contentDescription = product.Name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//            )
//        } ?: run {
//            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.CenterVertically)
//            )
//        }
//    }
//}

package com.example.ead_kotlin.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ead_kotlin.api.ProductDto
import com.example.ead_kotlin.viewmodels.CartViewModel
import com.example.ead_kotlin.viewmodels.ProductListViewModel
import kotlin.collections.groupBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(cartViewModel: CartViewModel, navController: NavController) {
    val viewModel: ProductListViewModel = viewModel()
    val products by viewModel.products.collectAsState()

    // Group products by category name
    val groupedProducts = products.groupBy { it.Category.Name }

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Iterate over each category and its associated products
            groupedProducts.forEach { (categoryName, categoryProducts) ->
                item {
                    CategoryCard(cartViewModel, navController,categoryName, categoryProducts)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(cartViewModel: CartViewModel, navController: NavController, categoryName: String, products: List<ProductDto>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(2.dp)) {
            // Display the category name
            Text(
                text = "Category: $categoryName",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(2.dp))

            // Display all products within this category
            products.forEach { product ->
                ProductItem(cartViewModel, navController, product)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductItem(cartViewModel: CartViewModel, navController: NavController, product: ProductDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        // Display product name
        Text(
            text = "Name: ${product.Name}",
            style = MaterialTheme.typography.bodyMedium
        )
        // Display product description
        Text(
            text = product.Description,
            style = MaterialTheme.typography.bodyMedium
        )
        // Display product quantity
        Text(
            text = "Qty: ${product.Qty}",
            style = MaterialTheme.typography.bodyMedium
        )
        // Display product price
        Text(
            text = "Price: \$${product.Price}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Load and display product image
        val bitmap = loadImageFromUrl(product.ImageUrl)
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = product.Name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Set a fixed height for the image
                    .padding(top = 8.dp) // Padding above the image
            )
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = {
                cartViewModel.addToCart(product)
                navController.navigate("cart")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }
    }
}
