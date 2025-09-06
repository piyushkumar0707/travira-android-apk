package com.example.travira.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.AuthViewModel

@Composable
fun SignupScreen(onNavigate: (String) -> Unit) {
    val authVm: AuthViewModel = viewModel()
    val loading by authVm.loading.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            // For demo: call register and then navigate to user_home
            authVm.register(if (name.isBlank()) "John Doe" else name)
            onNavigate("user_home")
        }, modifier = Modifier.fillMaxWidth(), enabled = !loading) {
            if (loading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Sign Up")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = { onNavigate("login") }) {
            Text("Already have an account? Login")
        }
    }
}
