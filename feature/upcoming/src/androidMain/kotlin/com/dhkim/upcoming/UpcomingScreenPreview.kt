package com.dhkim.upcoming

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dhkim.common.Genre
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.Movie
import kotlinx.collections.immutable.toImmutableList


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UpcomingScreenDarkPreview(@PreviewParameter(UpcomingUiStatePreviewParameter::class) uiState: UpcomingUiState) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UpcomingScreen(
                uiState = uiState,
                navigateToDetail = { _, _ -> }
            )
        }
    }
}

class UpcomingUiStatePreviewParameter : PreviewParameterProvider<UpcomingUiState> {
    private val upcomingMovies = mutableListOf<FeaturedSeries>().apply {
        repeat(50) {
            add(
                FeaturedSeries(
                    series = Movie(
                        id = "upcomingId$it",
                        title = "upcoming title$it",
                        adult = false,
                        overview = "overview $it",
                        genre = listOf(Genre.ACTION, Genre.ROMANCE, Genre.THRILLER, Genre.ANIMATION).map { it.genre },
                        imageUrl = "imageUrl$it",
                        releasedDate = "2025-05-12",
                        voteAverage = 4.3 + it.toDouble(),
                        popularity = 45.38 + it.toDouble(),
                    ),
                    group = FeaturedSeriesGroup.Upcoming
                )

            )
        }
    }.toImmutableList()

    override val values: Sequence<UpcomingUiState>
        get() = sequenceOf(
            UpcomingUiState(displayState = UpcomingDisplayState.Loading),
            UpcomingUiState(displayState = UpcomingDisplayState.Contents(series = upcomingMovies)),
            UpcomingUiState(displayState = UpcomingDisplayState.Error(errorCode = "404", message = "정보를 불러올 수 없습니다."))
        )
}