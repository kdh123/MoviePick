package com.dhkim.moviepick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
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
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import com.dhkim.home.navigation.home
import com.dhkim.home.navigation.movie
import com.dhkim.home.navigation.seriesCollection
import com.dhkim.home.navigation.tv
import com.dhkim.moviepick.navigation.seriesDetail
import com.dhkim.upcoming.navigation.upcoming
import com.dhkim.video.navigation.video
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

@ExperimentalSharedTransitionApi
@ExperimentalCoroutinesApi
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    val appState = rememberAppState()

    SharedTransitionLayout {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = appState.showBottomNavigation,
                        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
                        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
                    ) {
                        BottomBar(appState)
                    }
                }
            ) { padding ->
                NavHost(
                    navController = appState.navController,
                    startDestination = Screen.Home.route[0],
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
                ) {
                    home(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToSeriesDetail = appState::navigateToSeriesDetail,
                        navigateToVideo = appState::navigateToVideo,
                        navigateToMovie = appState::navigateToMovie,
                        navigateToTv = appState::navigateToTv,
                        onBack = appState::onBack
                    )
                    movie(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToVideo = appState::navigateToVideo,
                        navigateToSeriesDetail = appState::navigateToSeriesDetail,
                        navigateToSeriesCollection = appState::navigateToSeriesCollection,
                        onBack = appState::onBack
                    )
                    tv(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToSeriesDetail = appState::navigateToSeriesDetail,
                        navigateToVideo = appState::navigateToVideo,
                        navigateToSeriesCollection = appState::navigateToSeriesCollection,
                        onBack = appState::onBack
                    )
                    seriesDetail(
                        navigateToVideo = appState::navigateToVideo,
                        onBack = appState::onBack
                    )
                    seriesCollection(
                        navigateToSeriesDetail = appState::navigateToSeriesDetail,
                        onBack = appState::onBack
                    )
                    upcoming()
                    video()
                }
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
            val isSelected = screen.route.contains(appState.currentDestination)
            val onBottomClick = remember {
                {
                    appState.navigateToTopLevelDestination(screen.route[0])
                }
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.res),
                        contentDescription = screen.route[0],
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                },
                selected = isSelected,
                onClick = onBottomClick
            )
        }
    }
}
