package fredd.dev.trysensormqtt.ui.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fredd.dev.trysensormqtt.ui.helpers.BottomNavItem
import fredd.dev.trysensormqtt.ui.menu.ConfigurationScreen
import fredd.dev.trysensormqtt.ui.menu.SensorScreen

@Composable
fun AppNavHostApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Sensor.route
    ) {
        composable(BottomNavItem.Sensor.route) {
            SensorScreen()
        }
        composable(BottomNavItem.Configuration.route) {
            ConfigurationScreen()
        }
    }
}