package com.example.travira.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.UserViewModel

@Composable
fun UserHomeScreen(onNavigate: (String) -> Unit) {
    val userVm: UserViewModel = viewModel()
    val tourist by userVm.tourist.collectAsState()
    val safety by userVm.safety.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, Tourist!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        tourist?.let {
            Text("Name: ${it.name}")
            Spacer(Modifier.height(8.dp))
            Text("VC: ${it.vcHash}")
        } ?: Text("No registered tourist (use Signup).")

        Spacer(Modifier.height(20.dp))

        Button(onClick = { onNavigate("map_screen") }, modifier = Modifier.fillMaxWidth()) {
            Text("Open Map")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { onNavigate("panic_screen") }, modifier = Modifier.fillMaxWidth()) {
            Text("Panic / SOS")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = { userVm.fetchSafetyScore() }, modifier = Modifier.fillMaxWidth()) {
            Text("Fetch Safety Score (dummy)")
        }

        safety?.let {
            Spacer(Modifier.height(12.dp))
            Text("Safety Score: ${it.score} (${it.status})")
            Text("Flags: ${it.flags.joinToString()}")
        }
    }
}
