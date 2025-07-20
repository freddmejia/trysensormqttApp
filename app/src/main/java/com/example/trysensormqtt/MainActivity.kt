package com.example.trysensormqtt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.trysensormqtt.ui.dialogs.AppInfoDialog
import com.example.trysensormqtt.ui.menu.TopBar
import com.example.trysensormqtt.ui.menu.navigation.AppNavHostApp
import com.example.trysensormqtt.ui.menu.navigation.BottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
            /*TrySensorMqttTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        AppInfoDialog(onDismiss = { showInfoDialog = false })
    }
    Scaffold(
        topBar = {
            TopBar(
                title = "TrySensorMqtt",
                onMenuClick = { showInfoDialog = true }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHostApp(navController = navController)
        }
    }
}