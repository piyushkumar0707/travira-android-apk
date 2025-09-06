package com.example.travira.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.travira.ui.viewmodel.LocationViewModel
import com.example.travira.ui.viewmodel.LocationViewModelFactory

@Composable
fun MapScreen() {
    val ctx = LocalContext.current
    val vm: LocationViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(
            factory = LocationViewModelFactory(ctx.applicationContext)
        )

    val loc by vm.location.collectAsState()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            vm.startLocationUpdates()
        }
    }

    // Start tracking immediately if permission already granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            vm.startLocationUpdates()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Map Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (!hasPermission) {
            Button(onClick = {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text("Grant Location Permission")
            }
        } else {
            loc?.let {
                Text("Latitude: ${it.latitude}")
                Text("Longitude: ${it.longitude}")
            } ?: CircularProgressIndicator()
        }
    }
}
