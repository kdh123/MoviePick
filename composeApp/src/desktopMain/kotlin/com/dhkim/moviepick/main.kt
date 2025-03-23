package com.dhkim.moviepick

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.di.initKoin
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MoviePick",
        ) {
            MoviePickTheme(darkTheme = isSystemInDarkTheme()) {
                App()
            }
        }
    }
}