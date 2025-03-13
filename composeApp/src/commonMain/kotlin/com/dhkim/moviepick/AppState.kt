package com.dhkim.moviepick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhkim.home.navigation.HOME_ROUTE
import com.dhkim.upcoming.navigation.UPCOMING_ROUTE
import moviepick.composeapp.generated.resources.Res
import moviepick.composeapp.generated.resources.ic_home
import moviepick.composeapp.generated.resources.ic_upcoming
import org.jetbrains.compose.resources.DrawableResource

@Stable
class AppState(val navController: NavHostController) {
    val bottomNavItems = listOf(Screen.Home, Screen.Upcoming)
    private val routes = listOf(HOME_ROUTE, UPCOMING_ROUTE)

    val currentDestination: String
        @Composable get() {
            val entry = navController.currentBackStackEntryAsState().value
            val route = entry?.destination?.parent?.route ?: entry?.destination?.route ?: return Screen.Home.route

            return route
        }

    fun navigateToTopLevelDestination(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().route ?: return@navigate) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}

sealed class Screen(val res: DrawableResource, val route: String) {
    data object Home : Screen(Res.drawable.ic_home, HOME_ROUTE)
    data object Upcoming : Screen(Res.drawable.ic_upcoming, UPCOMING_ROUTE)
}

