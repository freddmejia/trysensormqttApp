package fredd.dev.trysensormqtt.ui.menu.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import fredd.dev.trysensormqtt.ui.helpers.BottomNavItem


@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Sensor,
        BottomNavItem.Configuration
    )

    NavigationBar(
        containerColor = Color(0xFF2B2B2B),
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            val animatedBackgroundColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF6FA88A) else Color.Transparent,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )

            val animatedHorizontalPadding by animateDpAsState(
                targetValue = if (isSelected) 16.dp else 0.dp,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )

            NavigationBarItem(
                selected  = isSelected,
                onClick   = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor      = Color.Transparent,
                    selectedIconColor   = Color.White,
                    unselectedIconColor = Color.White,
                    disabledIconColor   = Color.White
                ),
                icon = {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = animatedBackgroundColor,
                        tonalElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = animatedHorizontalPadding, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (item) {
                                is BottomNavItem.Sensor -> Icon(
                                    Icons.Default.Sensors,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(if (isSelected) 18.dp else 24.dp),
                                    tint = Color.White
                                )

                                is BottomNavItem.Configuration -> Icon(
                                    Icons.Default.Settings,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(if (isSelected) 18.dp else 24.dp),
                                    tint = Color.White
                                )
                            }
                            if (isSelected) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

/*
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Sensor,
        BottomNavItem.Configuration
    )

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        is BottomNavItem.Sensor -> Icon(
                            Icons.Default.Sensors,
                            contentDescription = item.title
                        )
                        is BottomNavItem.Configuration -> Icon(
                            Icons.Default.Settings,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color(0xFF4CAF50),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}*/