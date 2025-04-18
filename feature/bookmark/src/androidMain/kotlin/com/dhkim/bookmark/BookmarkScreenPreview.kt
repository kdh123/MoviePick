package com.dhkim.bookmark

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookmarkScreenDarkPreview(
    @PreviewParameter(BookmarkPreviewProvider::class) uiState: BookmarkUiState
) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BookmarkScreen(
                uiState = uiState,
                navigateToDetail = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun BookmarkScreenPreview(
    @PreviewParameter(BookmarkPreviewProvider::class) uiState: BookmarkUiState
) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BookmarkScreen(
                uiState = uiState,
                navigateToDetail = { _, _ -> }
            )
        }
    }
}

class BookmarkPreviewProvider : PreviewParameterProvider<BookmarkUiState> {

    private val bookmarks = mutableListOf<SeriesBookmark>().apply {
        repeat(20) {
            add(
                SeriesBookmark(
                    id = "id$it",
                    title = if (it == 4) {
                        "titletitletitletitletitletitletitletitletitletitle$it"
                    } else {
                        "title$it"
                    },
                    imageUrl = "imageUrl$it",
                    seriesType = SeriesType.MOVIE
                )
            )
        }
    }

    override val values: Sequence<BookmarkUiState>
        get() = sequenceOf(
            BookmarkUiState(
                displayState = BookmarkDisplayState.Contents(bookmarks.toImmutableList())
            ),
            BookmarkUiState(
                displayState = BookmarkDisplayState.Contents(persistentListOf())
            )
        )
}