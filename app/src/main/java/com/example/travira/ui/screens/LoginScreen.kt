package com.example.travira.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.travira.api.ApiClient
import com.example.travira.api.LoginRequest
import com.example.travira.auth.AuthManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigate: (String) -> Unit) {
    var username by remember { mutableStateOf("tourist_john") } // Pre-fill for demo
    var password by remember { mutableStateOf("tourist123") } // Pre-fill for demo
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Title
        Text(
            text = "TraVira",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Tourist Safety App",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Login Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                
                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Login Button
                Button(
                    onClick = {
                        if (username.isNotBlank() && password.isNotBlank()) {
                            scope.launch {
                                isLoading = true
                                try {
                                    android.util.Log.d("LoginAttempt", "Starting login for user: $username")
                                    val loginRequest = LoginRequest(username, password)
                                    android.util.Log.d("LoginAttempt", "Created LoginRequest: ${loginRequest}")
                                    
                                    val response = ApiClient.apiService.login(loginRequest)
                                    android.util.Log.d("LoginAttempt", "Received response: ${response.code()} - ${response.message()}")
                                    
                                    if (response.isSuccessful) {
                                        val authResponse = response.body()
                                        if (authResponse?.success == true) {
                                            // Save authentication data
                                            AuthManager.saveAuthData(
                                                context,
                                                authResponse.token,
                                                authResponse.user.username,
                                                authResponse.user.role
                                            )
                                            
                                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                            
                                            // Navigate based on role
                                            when (authResponse.user.role) {
                                                "admin", "officer" -> onNavigate("admin_home")
                                                else -> onNavigate("user_home")
                                            }
                                        } else {
                                            Toast.makeText(context, authResponse?.error ?: "Login failed", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    android.util.Log.e("LoginAttempt", "Login exception occurred", e)
                                    android.util.Log.e("LoginAttempt", "Exception type: ${e.javaClass.simpleName}")
                                    android.util.Log.e("LoginAttempt", "Exception message: ${e.message}")
                                    Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                                isLoading = false
                            }
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Login")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Demo Credentials Helper
                Text(
                    text = "Demo Accounts:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tourist: tourist_john / tourist123",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Officer: officer2 / officer123",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Admin: admin / admin123",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Network Test Button
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                android.util.Log.d("NetworkTest", "Starting network test...")
                                val response = ApiClient.apiService.testConnection()
                                if (response.isSuccessful) {
                                    val testResponse = response.body()
                                    Toast.makeText(context, "✓ Connection successful: ${testResponse?.message}", Toast.LENGTH_LONG).show()
                                    android.util.Log.d("NetworkTest", "Success: ${testResponse?.message}")
                                } else {
                                    Toast.makeText(context, "✗ Connection failed: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                                    android.util.Log.e("NetworkTest", "HTTP Error: ${response.code()} - ${response.message()}")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "✗ Network error: ${e.message}", Toast.LENGTH_LONG).show()
                                android.util.Log.e("NetworkTest", "Network error", e)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("🔧 Test Backend Connection")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Raw Connection Test Button
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                android.util.Log.d("RawTest", "Starting raw connection test...")
                                val result = ApiClient.testRawConnection()
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                                android.util.Log.d("RawTest", result)
                            } catch (e: Exception) {
                                Toast.makeText(context, "✗ Raw test failed: ${e.message}", Toast.LENGTH_LONG).show()
                                android.util.Log.e("RawTest", "Raw test error", e)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("🔴 Raw Connection Test")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Signup Button
                TextButton(
                    onClick = { onNavigate("signup") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Don't have an account? Sign up")
                }
            }
        }
    }
}
