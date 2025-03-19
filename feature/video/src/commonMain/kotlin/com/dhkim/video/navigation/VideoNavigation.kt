package com.dhkim.video.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.video.VideoScreen

const val VIDEO_ROUTE = "video_route"
const val KEY_VIDEO_URL = "videoUrl"

fun NavController.navigateToVideo(videoUrl: String) {
    navigate("$VIDEO_ROUTE/$videoUrl")
}

fun NavGraphBuilder.video() {
    composable("$VIDEO_ROUTE/{videoUrl}") {
        val videoUrl = it.arguments?.getString(KEY_VIDEO_URL) ?: ""
        VideoScreen(videoUrl)
    }
}