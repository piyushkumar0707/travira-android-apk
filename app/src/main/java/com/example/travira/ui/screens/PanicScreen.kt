package com.example.travira.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travira.ui.viewmodel.AlertViewModel

@Composable
fun PanicScreen() {
    val alertVm: AlertViewModel = viewModel()
    val sending by alertVm.sending.collectAsState()
    val result by alertVm.result.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Panic / SOS", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Button(onClick = { alertVm.sendPanic(touristId = null, lat = null, lon = null) }, modifier = Modifier.fillMaxWidth(), enabled = !sending) {
            if (sending) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Send SOS")
        }

        result?.let {
            Spacer(Modifier.height(12.dp))
            Text(it)
        }
    }
}
