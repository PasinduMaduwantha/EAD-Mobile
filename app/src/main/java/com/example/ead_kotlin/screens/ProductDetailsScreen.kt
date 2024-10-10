
package com.example.ead_kotlin.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ead_kotlin.viewmodels.ProductDetailViewModel
import com.example.ead_kotlin.viewmodels.CartViewModel
import com.example.ead_kotlin.viewmodels.RatingSubmissionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductDetailScreen(cartViewModel: CartViewModel,navController: NavController, productId: String) {
//    val viewModel: ProductDetailViewModel = viewModel()
////    val cartViewModel: CartViewModel = viewModel()
//    val product by viewModel.product.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
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
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(innerPadding)
//        ) {
//            product?.let { product ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    // Load and display image manually from URL
//                    val bitmap = loadImageFromUrl(product.ImageUrl)
//                    bitmap?.let {
//                        Image(
//                            bitmap = it.asImageBitmap(),
//                            contentDescription = product.Name,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(250.dp)
//                        )
//                    } ?: run {
//                        // Show placeholder if image not loaded
//                        CircularProgressIndicator(
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Product Name
//                    Text(
//                        text = product.Name,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Category: ${product.Category.Name}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Product Description
//                    Text(
//                        text = product.Description,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Quantity: ${product.Qty}",
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Product Price
//                    Text(
//                        text = "Unit Price: $${String.format("%.2f", product.Price)}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Add to Cart button
//                    Button(
//                        onClick = {
//                            cartViewModel.addToCart(product)
//                            navController.navigate("cart")
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Add to Cart")
//                    }
//
////                    Text(
////                        text = "Rate this Vendor",
////                        style = MaterialTheme.typography.headlineMedium
////                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//
//                    val fName = if(product.Vendor.FirstName !== null){
//                        product.Vendor.FirstName
//                    }else{
//                        ""
//                    }
//                    val lName = if(product.Vendor.LastName !== null){
//                        product.Vendor.LastName
//                    }else{
//                        ""
//                    }
//
//                    Text(
//                        text = "Vendor Name: $fName $lName",
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//
//                    Button(
//                        onClick = { navController.navigate("vendor_rating/${product.VendorId}") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    )
//                    {
//                        Text("Rate this Vendor")
//                    }
//
//                }
//            } ?: run {
//                if (errorMessage != null) {
//                    Text(
//                        text = errorMessage!!,
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                } else {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                }
//            }
//        }
//    }
//}
//
//// Helper function to load image from a URL
//@Composable
//fun loadImageFromUrl(url: String): Bitmap? {
//    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//    LaunchedEffect(url) {
//        withContext(Dispatchers.IO) {
//            try {
//                val connection = URL(url).openConnection() as HttpURLConnection
//                connection.doInput = true
//                connection.connect()
//                val input: InputStream = connection.inputStream
//                bitmap = BitmapFactory.decodeStream(input)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    return bitmap
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(cartViewModel: CartViewModel, navController: NavController, productId: String) {
    val viewModel: ProductDetailViewModel = viewModel()
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                product?.let { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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

                        Text(
                            text = "Category: ${product.Category.Name}",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Product Description
                        Text(
                            text = product.Description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Quantity: ${product.Qty}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Product Price
                        Text(
                            text = "Unit Price: $${String.format("%.2f", product.Price)}",
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

                        Spacer(modifier = Modifier.height(8.dp))

                        val fName = product.Vendor.FirstName ?: ""
                        val lName = product.Vendor.LastName ?: ""

                        Text(
                            text = "Vendor Name: $fName $lName",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Button(
                            onClick = { navController.navigate("vendor_rating/${product.VendorId}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Rate this Vendor")
                        }

                        // Add some extra space at the bottom to ensure the last button is not cut off
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                } ?: run {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

// Helper function to load image from a URL (remains unchanged)
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