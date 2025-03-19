package com.dhkim.moviepick

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dhkim.home.navigation.home
import com.dhkim.upcoming.navigation.upcoming
import com.dhkim.video.navigation.video
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

@ExperimentalCoroutinesApi
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    val appState = rememberAppState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(appState)
            }
        ) { padding ->
            NavHost(
                navController = appState.navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
            ) {
                home(
                    navigateToVideo = {
                        appState.navigateToVideo(it)
                    }
                )
                upcoming()
                video()
            }
        }
    }
}

@Composable
fun BottomBar(appState: AppState) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Bottom)
    ) {
        appState.bottomNavItems.forEach { screen ->
            val isSelected = screen.route == appState.currentDestination
            val onBottomClick = remember {
                {
                    appState.navigateToTopLevelDestination(screen.route)
                }
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.res),
                        contentDescription = screen.route,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                },
                selected = isSelected,
                onClick = onBottomClick
            )
        }
    }
}
