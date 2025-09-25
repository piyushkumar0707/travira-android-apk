package com.example.travira.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.travira.api.ApiClient
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(onNavigate: (String) -> Unit) {
    var touristStatus by remember { mutableStateOf<com.example.travira.api.TouristStatusResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Fetch tourist status on screen load
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.apiService.getTouristStatus(
                    ApiClient.createAuthHeader("demo-token") // In real app, get from secure storage
                )
                if (response.isSuccessful) {
                    touristStatus = response.body()
                }
            } catch (e: Exception) {
                // Handle error silently for now
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Welcome, Tourist!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Stay safe during your travels",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Safety Score Card
        touristStatus?.let { status ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🛡️ Safety Score: ${status.safetyScore}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            status.safetyScore >= 80 -> Color.Green
                            status.safetyScore >= 60 -> Color(0xFFFFA500) // Orange
                            else -> Color.Red
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    status.tourist?.let { tourist ->
                        Text(
                            text = "Status: ${tourist.status.uppercase()}",
                            color = when (tourist.status) {
                                "active" -> Color.Green
                                "high-risk" -> Color.Red
                                else -> Color.Gray
                            }
                        )
                        Text(
                            text = "Location: ${tourist.location.lat.toString().take(7)}, ${tourist.location.lng.toString().take(7)}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
        
        // Quick Actions
        Text(
            text = "Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Location Sharing Button
        Button(
            onClick = { onNavigate("map_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Share Location & View Map")
        }
        
        // Emergency Button
        Button(
            onClick = { onNavigate("panic_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Emergency",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("EMERGENCY PANIC BUTTON")
        }
        
        // Safety Tips Section
        touristStatus?.safetyTips?.let { tips ->
            if (tips.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Safety Tips",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        tips.forEach { tip ->
                            Text(
                                text = "• $tip",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Nearby Services
        touristStatus?.nearbyServices?.let { services ->
            if (services.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nearby Emergency Services",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        services.forEach { service ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = service.type.uppercase(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = service.distance,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                Text(
                                    text = service.contact,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
