//package com.example.ead_kotlin.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.ead_kotlin.viewmodels.ProductDetailViewModel
//import com.example.ead_kotlin.viewmodels.CartViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductDetailScreen(navController: NavController, productId: String) {
//    val viewModel: ProductDetailViewModel = viewModel()
//    val cartViewModel: CartViewModel = viewModel()
//    val product by viewModel.product.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    LaunchedEffect(productId) {
//        viewModel.getProductById(productId)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Product Details") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            product?.let { product ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        text = product.Name,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = product.Description,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "$${String.format("%.2f", product.Price)}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = {
//                            cartViewModel.addToCart(product)
//                            navController.navigate("cart")
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Add to Cart")
//                    }
//                }
//            } ?: run {
//                if (errorMessage != null) {
//                    Text(
//                        text = errorMessage!!,
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                } else {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//            }
//        }
//    }
//}


package com.example.ead_kotlin.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.example.ead_kotlin.viewmodels.ProductDetailViewModel
import com.example.ead_kotlin.viewmodels.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(cartViewModel: CartViewModel,navController: NavController, productId: String) {
    val viewModel: ProductDetailViewModel = viewModel()
//    val cartViewModel: CartViewModel = viewModel()
    val product by viewModel.product.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            product?.let { product ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Load and display image manually from URL
                    val bitmap = loadImageFromUrl(product.ImageUrl)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = product.Name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    } ?: run {
                        // Show placeholder if image not loaded
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Name
                    Text(
                        text = product.Name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Product Description
                    Text(
                        text = product.Description,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Product Price
                    Text(
                        text = "$${String.format("%.2f", product.Price)}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Add to Cart button
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
            } ?: run {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

// Helper function to load image from a URL
@Composable
fun loadImageFromUrl(url: String): Bitmap? {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    return bitmap
}
