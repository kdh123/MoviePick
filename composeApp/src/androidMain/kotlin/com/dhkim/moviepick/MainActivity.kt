package com.dhkim.moviepick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dhkim.core.designsystem.MoviePickTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    @ExperimentalCoroutinesApi
    @ExperimentalSharedTransitionApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MoviePickTheme {
                App()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalSharedTransitionApi
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}