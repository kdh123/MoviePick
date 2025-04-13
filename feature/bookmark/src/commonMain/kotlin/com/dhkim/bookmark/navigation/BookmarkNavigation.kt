package com.dhkim.bookmark.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.bookmark.BookmarkScreen
import com.dhkim.bookmark.BookmarkViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

const val BOOKMARK_ROUTE = "bookmark_route"

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.bookmark() {
    composable(BOOKMARK_ROUTE) {
        val viewModel = koinViewModel<BookmarkViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        BookmarkScreen(
            uiState = uiState
        )
    }
}