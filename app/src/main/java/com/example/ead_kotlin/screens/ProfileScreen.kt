package com.example.ead_kotlin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ead_kotlin.viewmodels.ProfileViewModel
import com.example.ead_kotlin.viewmodels.ProfileViewModelFactory
import com.example.ead_kotlin.data.UserPreferences
import com.example.ead_kotlin.data.CartManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val cartManager = remember { CartManager() }
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(userPreferences, cartManager)
    )

    val userProfile by viewModel.userProfile.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val deactivateStatus by viewModel.deactivate.collectAsState()


    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getUserProfile()
    }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            id = it.id.toString()
            firstName = it.FirstName
            lastName = it.LastName
            email=it.Email
            age = it.Age.toString()
            status = it.Status.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Status: $status",
                style = MaterialTheme.typography.bodyLarge,)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateProfile(firstName, lastName, email,age.toIntOrNull() ?: 0, status)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Profile")
            }
            Spacer(modifier = Modifier.height(16.dp))



            Button(
                onClick = {
                    viewModel.updateProfileStatus(id, "Deactivate")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Deactivate Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    viewModel.logout {
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }

            // Show update status
            updateStatus?.let { status ->
                Text(
                    text = status,
                    color = if (status.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            deactivateStatus?.let { status ->
                Text(
                    text = status,
                    color = if (status.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}