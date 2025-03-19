package com.dhkim.video

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.ui.youtube.YouTubePlayerComposable

@Composable
actual fun VideoScreen(videoUrl: String) {
    val playerHost = remember { MediaPlayerHost(mediaUrl = "https://www.youtube.com/watch?v=$videoUrl") }

    YouTubePlayerComposable(
        modifier = Modifier.fillMaxSize(),
        playerHost = playerHost
    )
}