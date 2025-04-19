import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Review
import com.dhkim.common.SeriesType
import com.dhkim.common.Video
import com.dhkim.common.VideoType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.domain.movie.model.MovieDetail
import com.dhkim.moviepick.SeriesDetailDisplayState
import com.dhkim.moviepick.SeriesDetailItem
import com.dhkim.moviepick.SeriesDetailScreen
import com.dhkim.moviepick.SeriesDetailUiState
import com.dhkim.moviepick.toVideoItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesDetailScreenDarkPreview(@PreviewParameter(SeriesDetailItemPreviewParameter::class) uiState: SeriesDetailUiState) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SeriesDetailScreen(
                uiState = uiState,
                bookmarks = persistentListOf(),
                onAction = {},
                navigateToVideo = {},
                onBack = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SeriesDetailScreenPreview(@PreviewParameter(SeriesDetailItemPreviewParameter::class) uiState: SeriesDetailUiState) {
    MoviePickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SeriesDetailScreen(
                uiState = uiState,
                bookmarks = persistentListOf(),
                onAction = {},
                navigateToVideo = {},
                onBack = {}
            )
        }
    }
}


class SeriesDetailItemPreviewParameter : PreviewParameterProvider<SeriesDetailUiState> {
    private val movieVideos = mutableListOf<Video>().apply {
        repeat(10) {
            add(
                Video(
                    id = "videoId$it",
                    key = "videoKey$it",
                    videoUrl = "videoUrl$it",
                    name = "name$it",
                    type = VideoType.Teaser
                )
            )
        }
    }

    private val movieReviews = mutableListOf<Review>().apply {
        repeat(50) {
            add(
                Review(
                    id = "$it",
                    author = "author$it",
                    createdAt = "2025-12-24",
                    content = "정말 좋은 영화입니다!",
                    rating = 5.0
                )
            )
        }
    }

    val movieDetail = MovieDetail(
        id = "movieId",
        title = "title",
        adult = false,
        overview = "눈보라가 몰아치던 겨울 밤 태어난 백설공주. 온정이 넘치던 왕국에서 모두의 사랑을 받았지만, 강력한 어둠의 힘으로 왕국을 빼앗은 여왕의 위협에 숲으로 도망친다.",
        images = listOf("imageUrl1", "imageUrl2", "imageUrl3"),
        genre = listOf(Genre.ANIMATION.genre),
        openDate = "2025-03-08",
        popularity = 35.8,
        runtime = 90,
        productionCompany = "Disney",
        actors = listOf("배우1", "배우2", "배우3", "배우4", "배우5", "배우6"),
        country = "미국",
        videos = movieVideos,
        review = PagingData.from(movieReviews)
    )


    override val values: Sequence<SeriesDetailUiState>
        get() = sequenceOf(
            SeriesDetailUiState(
                seriesType = SeriesType.MOVIE,
                displayState = SeriesDetailDisplayState.Contents(
                    isUpcoming = false,
                    series = persistentListOf(
                        SeriesDetailItem.AppBar(),
                        SeriesDetailItem.SeriesDetailPoster(imageUrl = movieDetail.images.firstOrNull() ?: ""),
                        SeriesDetailItem.Information(
                            seriesType = SeriesType.MOVIE,
                            series = movieDetail
                        ),
                        SeriesDetailItem.ContentTab(
                            videos = movieDetail.videos.map { it.toVideoItem("thumbnail") }.toImmutableList(),
                            reviews = flowOf(movieDetail.review)
                        )
                    )
                ),
            ),
            SeriesDetailUiState(
                seriesType = SeriesType.MOVIE,
                displayState = SeriesDetailDisplayState.Contents(
                    isUpcoming = false,
                    series = persistentListOf(
                        SeriesDetailItem.AppBar(),
                        SeriesDetailItem.SeriesDetailPoster(imageUrl = movieDetail.images.firstOrNull() ?: ""),
                        SeriesDetailItem.Information(
                            seriesType = SeriesType.MOVIE,
                            series = movieDetail
                        ),
                        SeriesDetailItem.ContentTab(
                            videos = movieDetail.videos.map { it.toVideoItem("thumbnail") }.toImmutableList(),
                            reviews = flowOf()
                        )
                    )
                ),
            )
        )
}