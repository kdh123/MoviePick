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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.dhkim.home.HomeViewModel
import com.dhkim.home.navigation.home
import com.dhkim.upcoming.navigation.upcoming
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appState = rememberAppState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            bottomBar = {
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
        ) { padding ->
            NavHost(
                navController = appState.navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .padding(bottom = padding.calculateBottomPadding())
            ) {
                home()
                upcoming()
            }
        }


        /*Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {

            }) {
                Text("Click me!", style = MoviePickTheme.typography.headlineLarge)
            }
            HomeScreen(uiState)
        }*/
    }
}
