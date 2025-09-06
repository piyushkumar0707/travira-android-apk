package com.example.travira.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectionScreen(
    onAdminClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Choose your role", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onAdminClick,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 16.dp)
            ) {
                Text("Login as Admin")
            }

            Button(
                onClick = onUserClick,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Login as User")
            }
        }
    }
}
