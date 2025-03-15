import com.dhkim.core.movie.data.di.NOW_PLAYING_MOVIES_KEY
import com.dhkim.core.movie.data.di.TOP_RATED_MOVIES_KEY
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhkim.core.testing.FakeGetNowPlayingMoviesUseCase
import com.dhkim.core.testing.FakeGetTopRatedMoviesUseCase
import com.dhkim.core.testing.MovieStatus
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
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `영화 탑 순위 가져오기 성공`() = runTest {
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase()
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()

        viewModel = HomeViewModel(
            mapOf(
                TOP_RATED_MOVIES_KEY to getTopRatedMoviesUseCase,
                NOW_PLAYING_MOVIES_KEY to getNowPlayingMoviesUseCase
            )
        )

        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }

    @Test
    fun `영화 탑 순위 가져오기 실패`() = runTest {
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase().apply {
            setStatus(MovieStatus.Error)
        }
        getNowPlayingMoviesUseCase = FakeGetNowPlayingMoviesUseCase()

        viewModel = HomeViewModel(
            mapOf(
                TOP_RATED_MOVIES_KEY to getTopRatedMoviesUseCase,
                NOW_PLAYING_MOVIES_KEY to getNowPlayingMoviesUseCase
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
