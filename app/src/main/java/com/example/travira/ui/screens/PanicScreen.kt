package com.example.travira.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.travira.api.ApiClient
import com.example.travira.api.PanicAlert
import com.example.travira.auth.AuthManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.AlertViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanicScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var alertSent by remember { mutableStateOf(false) }
    var incidentId by remember { mutableStateOf<String?>(null) }
    var customMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    fun sendPanicAlert(message: String = "Emergency assistance needed!") {
        scope.launch {
            isLoading = true
            try {
                // In a real app, you'd get actual GPS coordinates
                val mockLat = 28.6139 // Delhi coordinates for demo
                val mockLng = 77.2090
                
                // Get the real authentication token
                val authToken = AuthManager.getAuthToken()
                if (authToken == null) {
                    Toast.makeText(context, "Please login again", Toast.LENGTH_LONG).show()
                    return@launch
                }
                
                val response = ApiClient.apiService.sendPanicAlert(
                    ApiClient.createAuthHeader(authToken),
                    PanicAlert(mockLat, mockLng, message)
                )
                
                if (response.isSuccessful) {
                    val panicResponse = response.body()
                    if (panicResponse?.success == true) {
                        alertSent = true
                        incidentId = panicResponse.incidentId
                        Toast.makeText(context, panicResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, panicResponse?.error ?: "Failed to send alert", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to send panic alert: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Network error: Check if backend is running", Toast.LENGTH_LONG).show()
            }
            isLoading = false
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (!alertSent) {
            // Pre-alert screen
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Emergency",
                    tint = Color.Red,
                    modifier = Modifier.size(80.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "EMERGENCY PANIC BUTTON",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Press the button below to send an immediate emergency alert to nearby authorities and registered contacts.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Custom message input
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { Text("Optional: Describe your emergency") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    enabled = !isLoading,
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Main panic button
                Button(
                    onClick = {
                        val message = if (customMessage.isNotBlank()) customMessage else "Emergency assistance needed!"
                        sendPanicAlert(message)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 16.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.White
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Emergency",
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "SEND SOS ALERT",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Quick call buttons
                Text(
                    text = "Or call directly:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { 
                            Toast.makeText(context, "Calling Police (100)", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Call",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Police 100")
                    }
                    
                    Button(
                        onClick = { 
                            Toast.makeText(context, "Calling Emergency (102)", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Call",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Emergency 102")
                    }
                }
            }
        } else {
            // Post-alert confirmation screen
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Alert Sent",
                    tint = Color.Green,
                    modifier = Modifier.size(80.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "ALERT SENT SUCCESSFULLY!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Your emergency alert has been sent to nearby authorities. Help is on the way.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                incidentId?.let { id ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Incident ID: $id",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "What happens next?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        listOf(
                            "✓ Emergency services have been notified",
                            "✓ Your location has been shared",
                            "✓ Response team is being dispatched",
                            "✓ Estimated arrival: 5-10 minutes"
                        ).forEach { item ->
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        alertSent = false
                        incidentId = null
                        customMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send Another Alert")
                }
            }
        }
    }
}
