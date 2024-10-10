//package com.example.ead_kotlin.ui.components
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.ead_kotlin.api.ProductDto
//
//@Composable
//fun ProductListItem(product: ProductDto, onItemClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable(onClick = onItemClick)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(product.Name, style = MaterialTheme.typography.titleMedium)
//            Text("$${String.format("%.2f", product.Price)}", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}

package com.example.ead_kotlin.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.ead_kotlin.api.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ProductListItem(product: ProductDto, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Load and display the image
            val bitmap = loadImageFromUrl(product.ImageUrl)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = product.Name,
                    modifier = Modifier
                        .size(80.dp) // Set image size
                )
            } ?: run {
                // Placeholder for when the image is not yet loaded
                CircularProgressIndicator(modifier = Modifier.size(80.dp))
            }

            // Display the product details
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(product.Name, style = MaterialTheme.typography.titleMedium)
                Text("$${String.format("%.2f", product.Price)}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Qty: ${product.Qty}", style = MaterialTheme.typography.titleMedium)
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
