package movie

import app.cash.paging.testing.asSnapshot
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.core.testing.movie.FakeGetMovieDetailUseCase
import com.dhkim.core.testing.movie.FakeGetMovieReviewsUseCase
import com.dhkim.core.testing.movie.FakeGetMovieVideoUseCase
import com.dhkim.core.testing.movie.FakeGetMovieWithCategoryUseCase
import com.dhkim.core.testing.movie.FakeGetTopRatedMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetUpcomingMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetTodayRecommendationMovieUseCase
import com.dhkim.data.di.movieModule
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.movie.usecase.GetMovieReviewsUseCase
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MovieUseCaseTest : KoinTest {

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
    fun `오늘의 추천 영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetMoviesUseCase>(named(TODAY_RECOMMENDATION_MOVIE_KEY))
        moviesUseCase(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `오늘의 추천 영화 가져오기 성공_Fake`() = runTest {
        FakeGetTodayRecommendationMovieUseCase().invoke(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `오늘의 TOP 10 영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetMoviesUseCase>(named(TODAY_TOP_10_MOVIES_KEY))
        moviesUseCase(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `오늘의 TOP 10 영화 가져오기 성공_Fake`() = runTest {
        FakeGetTodayRecommendationMovieUseCase().invoke(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Top Rated 영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY))
        moviesUseCase(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Top Rated 영화 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedMoviesUseCase().invoke(Language.Korea, Region.Korea).asSnapshot()
        println(data)
    }

    @Test
    fun `Now Playing 영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY))
        moviesUseCase(Language.Korea, Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Now Playing 영화 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedMoviesUseCase().invoke(Language.Korea, Region.Korea).asSnapshot()
        println(data)
    }

    @Test
    fun `Upcoming 영화 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetUpcomingMoviesUseCase>()
        moviesUseCase(Language.Korea, Region.Korea).first().forEach {
            println(it)
        }
    }

    @Test
    fun `Upcoming 영화 가져오기 성공_Fake`() = runTest {
        val data = FakeGetUpcomingMoviesUseCase().invoke(Language.Korea, Region.Korea).first()
        println(data)
    }

    @Test
    fun `양화 비디오 가져오기_Real - Only Android`() = runTest {
        val movieId = "447273"
        val getMovieVideoUseCase = get<GetMovieVideoUseCase>()
        val video = getMovieVideoUseCase(movieId, Language.Korea).first()
        println(video)
    }

    @Test
    fun `양화 비디오 가져오기_Fake - Only Android`() = runTest {
        val movieId = "447273"
        val video = FakeGetMovieVideoUseCase().invoke(movieId, Language.Korea).first()
        println(video)
    }

    @Test
    fun `카테고리에 해당하는 영화 가져오기_Real - Only Android`() = runTest {
        val getMovieWithCategoryUseCase = get<GetMovieWithCategoryUseCase>()
        getMovieWithCategoryUseCase(language = Language.Korea,  region = null, genre = Genre.ADVENTURE).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `카테고리에 해당하는 영화 가져오기_Fake - Only Android`() = runTest {
        FakeGetMovieWithCategoryUseCase().invoke(language = Language.Korea, genre = Genre.ROMANCE).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `영화 상세 정보 가져오기_Real - Only Android`() = runTest {
        //베테랑 : 995926
        //백설공주 : 447273
        //부산행 : 396535
        //야당 : 1137179
        //극장판 뱅드림 : 1231799

        val getMovieDetailUseCase = get<GetMovieDetailUseCase>()
        val movie = getMovieDetailUseCase(id = "995926", language = Language.Korea).first()
        println(movie)
        movie.actors.forEach {
            println(it)
        }
        flowOf(movie.review).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `영화 상세 정보 가져오기_Fake - Only Android`() = runTest {
        val movie = FakeGetMovieDetailUseCase().invoke(id = "topRatedId7", language = Language.Korea)
        println(movie.first())
    }

    @Test
    fun `영화 리뷰 가져오기_Real - Only Android`() = runTest {
        val getMovieReviewsUseCase = get<GetMovieReviewsUseCase>()
        getMovieReviewsUseCase(id = "447273").asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `영화 리뷰 가져오기_Fake - Only Android`() = runTest {
        FakeGetMovieReviewsUseCase().invoke(id = "447273").asSnapshot().forEach {
            println(it)
        }
    }
}