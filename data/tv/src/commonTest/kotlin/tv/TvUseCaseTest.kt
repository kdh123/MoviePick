package tv

import app.cash.paging.testing.asSnapshot
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.core.testing.movie.FakeGetTodayRecommendationTvUseCase
import com.dhkim.core.testing.tv.FakeGetAiringTodayTvsUseCase
import com.dhkim.core.testing.tv.FakeGetOnTheAirTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTvWithCategoryUseCase
import com.dhkim.data.tv.di.tvModule
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
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

class TvUseCaseTest : KoinTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(tvModule)
        }
    }

    @Test
    fun `오늘의 추천 TV 가져오기_Real - Only Android`() = runTest {
        val moviesUseCase = get<GetTvsUseCase>(named(TODAY_RECOMMENDATION_TV_KEY))
        moviesUseCase(Language.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `오늘의 추천 TV 가져오기 성공_Fake`() = runTest {
        FakeGetTodayRecommendationTvUseCase().invoke(Language.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Airing Today TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY))
        getTvsUseCase(Language.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Airing Today TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetAiringTodayTvsUseCase().invoke(Language.Korea).asSnapshot()
        println(data)
    }

    @Test
    fun `On The Air TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY))
        getTvsUseCase(Language.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `On The Air TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetOnTheAirTvsUseCase().invoke(Language.Korea).asSnapshot()
        println(data)
    }

    @Test
    fun `Top Rated TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY))
        getTvsUseCase(Language.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Top Rated TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedTvsUseCase().invoke(Language.Korea).asSnapshot()
        println(data)
    }

    @Test
    fun `카테고리에 해당하는 TV 가져오기_Real - Only Android`() = runTest {
        val getTvWithCategoryUseCase = get<GetTvWithCategoryUseCase>()
        getTvWithCategoryUseCase(language = Language.Korea,  region = Region.Korea).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `카테고리에 해당하는 TV 가져오기_Fake - Only Android`() = runTest {
        FakeGetTvWithCategoryUseCase().invoke(language = Language.Korea, genre = Genre.ROMANCE).asSnapshot().forEach {
            println(it)
        }
    }
}