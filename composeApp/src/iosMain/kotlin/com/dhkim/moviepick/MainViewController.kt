package com.dhkim.moviepick

import androidx.compose.ui.window.ComposeUIViewController
import com.dhkim.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}