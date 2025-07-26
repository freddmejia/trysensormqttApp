package fredd.dev.trysensormqtt.ui.helpers
import fredd.dev.trysensormqtt.R
sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    object Sensor : BottomNavItem("sensor", "Sensor", R.drawable.sensors_device)
    object Configuration : BottomNavItem("configuration", "Configuration", R.drawable.config_sensor_icon)
}
