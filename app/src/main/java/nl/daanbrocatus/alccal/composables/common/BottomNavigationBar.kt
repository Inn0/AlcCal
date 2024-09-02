package nl.daanbrocatus.alccal.composables.common

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import nl.daanbrocatus.alccal.common.BottomNavigationItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationItems = BottomNavigationItem.items
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.title,
                onClick = {
                    navController.navigate(item.title) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if (currentRoute == item.title) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
