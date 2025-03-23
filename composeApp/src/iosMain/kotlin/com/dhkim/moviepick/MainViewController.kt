package com.dhkim.moviepick

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.window.ComposeUIViewController
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.di.initKoin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val isDarkTheme = UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    MoviePickTheme(
        darkTheme = isDarkTheme
    ) {
        App()
    }
}