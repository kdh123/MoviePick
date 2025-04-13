package seriesdetail

import com.dhkim.common.SeriesType
import com.dhkim.core.testing.movie.FakeGetMovieDetailUseCase
import com.dhkim.core.testing.tv.FakeGetTvDetailUseCase
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import com.dhkim.moviepick.SeriesDetailDisplayState
import com.dhkim.moviepick.SeriesDetailItem
import com.dhkim.moviepick.SeriesDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SeriesDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: SeriesDetailViewModel
    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase
    private lateinit var getTvDetailUseCase: GetTvDetailUseCase

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `영화 상세화면 가져오기`() = runTest {
        getMovieDetailUseCase = FakeGetMovieDetailUseCase()
        getTvDetailUseCase = FakeGetTvDetailUseCase()
        viewModel = SeriesDetailViewModel(
            series = SeriesType.MOVIE.name,
            seriesId = "topRatedId7",
            getMovieDetailUseCase = getMovieDetailUseCase,
            getTvDetailUseCase = getTvDetailUseCase
        )
        viewModel.uiState.first()
        if (viewModel.uiState.value.displayState is SeriesDetailDisplayState.Contents) {
            (viewModel.uiState.value.displayState as SeriesDetailDisplayState.Contents).series.forEach {
                if (it is SeriesDetailItem.ContentTab) {
                    println(it.videos)
                } else {
                    println(it)
                }
            }
        } else {
            println("${viewModel.uiState.value}")
        }
    }

    @Test
    fun `TV 상세화면 가져오기`() = runTest {
        getMovieDetailUseCase = FakeGetMovieDetailUseCase()
        getTvDetailUseCase = FakeGetTvDetailUseCase()
        viewModel = SeriesDetailViewModel(
            series = SeriesType.TV.name,
            seriesId = "airingTodayId8",
            getMovieDetailUseCase = getMovieDetailUseCase,
            getTvDetailUseCase = getTvDetailUseCase
        )
        viewModel.uiState.first()
        if (viewModel.uiState.value.displayState is SeriesDetailDisplayState.Contents) {
            (viewModel.uiState.value.displayState as SeriesDetailDisplayState.Contents).series.forEach {
                if (it is SeriesDetailItem.ContentTab) {
                    println(it.videos)
                } else {
                    println(it)
                }
            }
        }
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}