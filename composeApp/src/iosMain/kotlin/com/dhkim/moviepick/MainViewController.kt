package com.dhkim.moviepick

import androidx.compose.ui.window.ComposeUIViewController
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.di.initKoin
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

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