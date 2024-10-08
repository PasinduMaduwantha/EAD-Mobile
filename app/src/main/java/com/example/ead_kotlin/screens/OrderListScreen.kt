package com.example.ead_kotlin.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ead_kotlin.api.GetOrderDto
import com.example.ead_kotlin.api.GetOrderItemDto
import com.example.ead_kotlin.viewmodels.OrderListViewModel
import com.example.ead_kotlin.viewmodels.UserSession
import com.example.ead_kotlin.viewmodels.UserSession.token
import kotlin.text.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController) {
    val viewModel: OrderListViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val token = UserSession.token

    LaunchedEffect(Unit) {
        token?.let { viewModel.getCustomerOrders(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
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
            items(orders) { order ->
                OrderItem(order)
            }
        }
    }
}

@Composable
fun OrderItem(order: GetOrderDto) {
    val viewModel: OrderListViewModel = viewModel()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order ID: ${order.Id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Total Amount: $${order.TotalAmount}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Status: ${order.OrderStatus}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (order.OrderStatus != "Canceled") {
                Button(
                    onClick = { viewModel.cancelOrder(token,order.Id, order) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Cancel Order")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Items:",
                style = MaterialTheme.typography.titleSmall
            )
            order.OrderItems.forEach { item ->
                OrderItemRow(item)
            }
        }
    }
}

@Composable
fun OrderItemRow(item: GetOrderItemDto) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "Name: ${item.Product.Name}:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Quantity ${item.Quantity}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        val bitmap = loadImageFromUrl(item.Product.ImageUrl)
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = item.Product.Name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } ?: run {
            // Show placeholder if image not loaded
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

