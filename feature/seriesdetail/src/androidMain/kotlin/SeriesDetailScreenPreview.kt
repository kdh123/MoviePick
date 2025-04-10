import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Review
import com.dhkim.common.SeriesType
import com.dhkim.common.Video
import com.dhkim.common.VideoType
import com.dhkim.domain.movie.model.MovieDetail
import com.dhkim.moviepick.SeriesDetailDisplayState
import com.dhkim.moviepick.SeriesDetailItem
import com.dhkim.moviepick.SeriesDetailScreen
import com.dhkim.moviepick.SeriesDetailUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

@Preview(showBackground = true)
@Composable
private fun SeriesDetailScreenPreview(@PreviewParameter(SeriesDetailItemPreviewParameter::class) uiState: SeriesDetailUiState) {
    SeriesDetailScreen(
        uiState = uiState,
        onBack = {}
    )
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
        overview = "눈보라가 몰아치던 겨울 밤 태어난 백설공주. 온정이 넘치던 왕국에서 모두의 사랑을 받았지만, 강력한 어둠의 힘으로 왕국을 빼앗은 여왕의 위협에 숲으로 도망친다. 마법의 숲에서 간신히 살아남은 백설공주는 신비로운 일곱 광부들과 만나게 되며 새로운 세상을 마주하고, 마음속 깊이 숨겨진 용기와 선한 힘을 깨닫게 된다. 그리고 마침내, 빼앗긴 왕국을 되찾기 위해 여왕과 맞서 싸우기로 결심하는데…",
        imageUrl = "imageUrl",
        genre = listOf(Genre.ANIMATION.genre),
        releasedDate = "2025-03-08",
        popularity = 35.8,
        runtime = 90,
        productionCompany = "Disney",
        country = "미국",
        videos = movieVideos,
        review = PagingData.from(movieReviews)
    )


    override val values: Sequence<SeriesDetailUiState>
        get() = sequenceOf(
            SeriesDetailUiState(
                seriesType = SeriesType.MOVIE,
                displayState = SeriesDetailDisplayState.Contents(
                    series = persistentListOf(
                        SeriesDetailItem.AppBar(),
                        SeriesDetailItem.SeriesDetailPoster(imageUrl = movieDetail.imageUrl),
                        SeriesDetailItem.Information(
                            seriesType = SeriesType.MOVIE,
                            series = movieDetail
                        ),
                        SeriesDetailItem.ContentTab(
                            videos = movieDetail.videos,
                            reviews = flowOf(movieDetail.review)
                        )
                    )
                ),
            )
        )
}