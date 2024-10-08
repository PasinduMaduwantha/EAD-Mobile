package com.example.ead_kotlin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ead_kotlin.viewmodels.CartItem
import com.example.ead_kotlin.viewmodels.CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.example.ead_kotlin.viewmodels.UserSession
import com.example.ead_kotlin.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val token = UserSession.token
    // Debugging statement to check cart items
    println("Cart Items: $cartItems")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if(cartItems.isNotEmpty()){
                        items(cartItems) { cartItem ->
                            CartItemRow(cartItem, viewModel)
                        }
                    }

                }
            }

            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge)
                Text("$${String.format("%.2f", totalAmount)}", style = MaterialTheme.typography.titleLarge)
            }
            Button(
                onClick = {
//                    viewModel.placeOrder { success ->
//                        if (success) {
//                            navController.navigate("orders")
//                        }
//                    }
                    token?.let {
                        viewModel.placeOrder(it) { success ->
                            if (success) {
//                                cartItems.drop()
                                navController.navigate("orders")
                            }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Place Order")
            }
        }
    }
}


@Composable
fun CartItemRow(cartItem: CartItem, viewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Load and display the image
            val bitmap = loadImageFromUrl(cartItem.product.ImageUrl) // Ensure this property exists
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = cartItem.product.Name,
                    modifier = Modifier
                        .size(80.dp) // Set image size
                )
            } ?: run {
                // Placeholder for when the image is not yet loaded
                CircularProgressIndicator(modifier = Modifier.size(80.dp))
            }

            Column {
                Text(cartItem.product.Name, style = MaterialTheme.typography.titleMedium)
                Text("$${String.format("%.2f", cartItem.product.Price)}", style = MaterialTheme.typography.bodyMedium)
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.decreaseQuantity(cartItem.product.Id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Decrease")
            }
            Text(
                cartItem.quantity.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = { viewModel.increaseQuantity(cartItem.product.Id) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}


//fun CartItemRow(cartItem: CartItem, viewModel: CartViewModel) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(cartItem.product.Name, style = MaterialTheme.typography.titleMedium)
//                Text("$${String.format("%.2f", cartItem.product.Price)}", style = MaterialTheme.typography.bodyMedium)
//            }
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { viewModel.decreaseQuantity(cartItem.product.Id) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Decrease")
//                }
//                Text(
//                    cartItem.quantity.toString(),
//                    modifier = Modifier.padding(horizontal = 8.dp),
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                IconButton(onClick = { viewModel.increaseQuantity(cartItem.product.Id) }) {
//                    Icon(Icons.Default.Add, contentDescription = "Increase")
//                }
//            }
//        }
//    }
//}


