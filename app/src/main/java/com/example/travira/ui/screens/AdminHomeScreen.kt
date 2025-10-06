package com.example.travira.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHomeScreen() {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Admin Home (Placeholder)", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Admin-specific functionality will be implemented here.")
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = {
            // Clear any auth state and return to role selection
            com.example.travira.auth.AuthManager.logout(ctx)
        }) {
            Text("Logout")
        }
    }
}
