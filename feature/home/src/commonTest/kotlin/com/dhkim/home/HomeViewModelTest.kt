import com.dhkim.core.testing.movie.FakeGetNowPlayingMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetTopRatedMoviesUseCase
import com.dhkim.core.testing.movie.MovieStatus
import com.dhkim.core.testing.tv.FakeGetAiringTodayTvsUseCase
import com.dhkim.core.testing.tv.FakeGetOnTheAirTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
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
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase()
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()
        getAiringTodayTvsUseCase = FakeGetAiringTodayTvsUseCase()
        getOnTheAirTvsUseCase = FakeGetOnTheAirTvsUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()

        viewModel = HomeViewModel(
            mapOf(
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
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase().apply {
            setStatus(MovieStatus.Error)
        }
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()
        getAiringTodayTvsUseCase = FakeGetAiringTodayTvsUseCase()
        getOnTheAirTvsUseCase = FakeGetOnTheAirTvsUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()

        viewModel = HomeViewModel(
            mapOf(
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
