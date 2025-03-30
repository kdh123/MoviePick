package com.dhkim.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.dhkim.home.movie.MovieAction
import com.dhkim.home.movie.MovieUiState
import com.dhkim.home.tv.TvAction
import com.dhkim.home.tv.TvUiState

@ExperimentalSharedTransitionApi
@Composable
expect fun MovieScreenContainer(
    uiState: MovieUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onAction: (MovieAction) -> Unit,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
)

@ExperimentalSharedTransitionApi
@Composable
expect fun TvScreenContainer(
    uiState: TvUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onAction: (TvAction) -> Unit,
    navigateToVideo: (String) -> Unit,
    onBack: () -> Unit
)