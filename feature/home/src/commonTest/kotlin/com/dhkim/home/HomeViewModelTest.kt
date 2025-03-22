import com.dhkim.core.testing.movie.FakeGetNowPlayingMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetTopRatedMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetTodayRecommendationMovieUseCase
import com.dhkim.core.testing.movie.FakeGetTodayTop10MoviesUseCase
import com.dhkim.core.testing.movie.MovieStatus
import com.dhkim.core.testing.tv.FakeGetAiringTodayTvsUseCase
import com.dhkim.core.testing.tv.FakeGetOnTheAirTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.home.HomeViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var getTodayRecommendationMovieUseCase: GetMoviesUseCase
    private lateinit var getTodayTop10MoviesUseCase: GetMoviesUseCase
    private lateinit var getTopRatedMoviesUseCase: GetMoviesUseCase
    private lateinit var getNowPlayingMoviesUseCase: GetMoviesUseCase
    private lateinit var getAiringTodayTvsUseCase: GetTvsUseCase
    private lateinit var getOnTheAirTvsUseCase: GetTvsUseCase
    private lateinit var getTopRatedTvsUseCase: GetTvsUseCase
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `영화 가져오기 성공`() = runTest {
        getTodayRecommendationMovieUseCase = FakeGetTodayRecommendationMovieUseCase()
        getTodayTop10MoviesUseCase = FakeGetTodayTop10MoviesUseCase()
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase()
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()
        getAiringTodayTvsUseCase = FakeGetAiringTodayTvsUseCase()
        getOnTheAirTvsUseCase = FakeGetOnTheAirTvsUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()

        viewModel = HomeViewModel(
            mapOf(
                TODAY_RECOMMENDATION_MOVIE_KEY to getTodayRecommendationMovieUseCase,
                TODAY_TOP_10_MOVIES_KEY to getTodayTop10MoviesUseCase,
                TOP_RATED_MOVIES_KEY to getTopRatedMoviesUseCase,
                NOW_PLAYING_MOVIES_KEY to getNowPlayingMoviesUseCase
            ),
            mapOf(
                AIRING_TODAY_TVS_KEY to getAiringTodayTvsUseCase,
                ON_THE_AIR_TVS_KEY to getOnTheAirTvsUseCase,
                TOP_RATED_TVS_KEY to getTopRatedTvsUseCase
            )
        )

        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }

    @Test
    fun `영화 가져오기 실패`() = runTest {
        getTodayRecommendationMovieUseCase = FakeGetTodayRecommendationMovieUseCase()
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase().apply {
            setStatus(MovieStatus.Error)
        }
        getTodayTop10MoviesUseCase = FakeGetTodayTop10MoviesUseCase()
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()
        getAiringTodayTvsUseCase = FakeGetAiringTodayTvsUseCase()
        getOnTheAirTvsUseCase = FakeGetOnTheAirTvsUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()

        viewModel = HomeViewModel(
            mapOf(
                TODAY_RECOMMENDATION_MOVIE_KEY to getTodayRecommendationMovieUseCase,
                TODAY_TOP_10_MOVIES_KEY to getTodayTop10MoviesUseCase,
                TOP_RATED_MOVIES_KEY to getTopRatedMoviesUseCase,
                NOW_PLAYING_MOVIES_KEY to getNowPlayingMoviesUseCase
            ),
            mapOf(
                AIRING_TODAY_TVS_KEY to getAiringTodayTvsUseCase,
                ON_THE_AIR_TVS_KEY to getOnTheAirTvsUseCase,
                TOP_RATED_TVS_KEY to getTopRatedTvsUseCase
            )
        )

        viewModel.uiState.first()

        println("answer : ${viewModel.uiState.value}")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        (getTopRatedMoviesUseCase as FakeGetTopRatedMoviesUseCase).setStatus(MovieStatus.Success)
        (getNowPlayingMoviesUseCase as FakeGetNowPlayingMoviesUseCase).setStatus(MovieStatus.Success)
        Dispatchers.resetMain()
    }
}
