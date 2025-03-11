package movie

import app.cash.paging.testing.asSnapshot
import com.dhkim.core.movie.data.di.movieModule
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhkim.core.testing.FakeGetTopRatedMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MoviesUseCaseTest : KoinTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(movieModule)
        }
    }

    @Test
    fun `영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetMoviesUseCase>(named("topRated"))
        moviesUseCase().asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `영화 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedMoviesUseCase().invoke().asSnapshot()
        println(data)
    }
}