package com.example.travira.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travira.api.ApiClient
import com.example.travira.auth.AuthManager
import kotlinx.coroutines.launch

@Composable
fun VerifyTouristScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    var uid by remember { mutableStateOf(AuthManager.getTouristUID()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Verify your details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "We will assign a unique Tourist ID to your account.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (uid.isNullOrEmpty()) {
                    Button(
                        onClick = {
                            val token = AuthManager.getAuthToken()
                            if (token.isNullOrEmpty()) {
                                error = "You are not logged in. Please login again."
                                return@Button
                            }
                            scope.launch {
                                isLoading = true
                                error = null
                                try {
                                    val response = ApiClient.apiService.verifyTourist(
                                        ApiClient.createAuthHeader(token)
                                    )
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body?.success == true) {
                                            uid = body.touristUID
                                            AuthManager.saveTouristUID(context, body.touristUID)
                                            Toast.makeText(context, "Verified! ID: ${body.touristUID}", Toast.LENGTH_LONG).show()
                                        } else {
                                            error = body?.error ?: "Verification failed"
                                        }
                                    } else {
                                        error = "Error: ${response.code()} ${response.message()}"
                                    }
                                } catch (e: Exception) {
                                    error = e.message
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Verify me")
                        }
                    }
                } else {
                    Text(
                        text = "Your Tourist ID",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = uid!!,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { onNavigate("user_home") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue to Home")
                    }
                }

                if (!error.isNullOrEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(text = error!!, color = MaterialTheme.colorScheme.error)
                }

            }
        }
    }
}
