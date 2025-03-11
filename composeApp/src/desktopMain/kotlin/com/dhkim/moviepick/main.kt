package com.dhkim.moviepick

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dhkim.di.initKoin

/*fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}*/

fun main(){
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MoviePick",
        ) {
            App()
        }
    }
}