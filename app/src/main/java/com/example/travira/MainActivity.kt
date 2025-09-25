package com.example.travira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travira.ui.screens.*
import com.example.travira.ui.viewmodel.LocationViewModel
import com.example.travira.ui.viewmodel.LocationViewModelFactory
import com.example.travira.auth.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize authentication manager
        AuthManager.initialize(this)
        
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    Scaffold { padding ->
        NavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    // LocationViewModel needs context; create factory where required in screens
    NavHost(
        navController = navController,
        startDestination = "role_selection",
        modifier = modifier
    ) {
        composable("role_selection") {
            RoleSelectionScreen(
                onAdminClick = { navController.navigate("admin_home") },
                onUserClick = { navController.navigate("login") }
            )
        }
        composable("login") { LoginScreen(onNavigate = { navController.navigate(it) }) }
        composable("signup") { SignupScreen(onNavigate = { navController.navigate(it) }) }
        composable("user_home") { UserHomeScreen(onNavigate = { navController.navigate(it) }) }
        composable("map_screen") {
            MapScreen()
        }

        composable("panic_screen") { PanicScreen() }
        composable("admin_home") { AdminHomeScreen() }
    }
}

// Helper to expose Activity as Context to viewModel factory in compose
// Using a tiny singleton provider to avoid passing context everywhere
object LocalActivityProvider {
    // This will be set from MainActivity when content is created.
    // Simpler apps can instead create factory inside composable with LocalContext.current.applicationContext.
    // For clarity we will set it here in onCreate.
    // NOTE: In this code sample we fallback to application context method inside factory if null.
    var current: android.content.Context? = null
}
