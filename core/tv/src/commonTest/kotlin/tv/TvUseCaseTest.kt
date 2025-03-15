package tv

import app.cash.paging.testing.asSnapshot
import com.dhkim.common.Language
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.core.tv.data.di.AIRING_TODAY_TVS_KEY
import com.dhkim.core.tv.data.di.ON_THE_AIR_TVS_KEY
import com.dhkim.core.tv.data.di.TOP_RATED_TVS_KEY
import com.dhkim.core.tv.data.di.tvModule
import com.dhkim.core.tv.domain.usecase.GetTvsUseCase
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
    fun `Airing Today TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY))
        getTvsUseCase(Language.Korea.code).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Airing Today TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedTvsUseCase().invoke(Language.Korea.code).asSnapshot()
        println(data)
    }

    @Test
    fun `On The Air TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY))
        getTvsUseCase(Language.Korea.code).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `On The Air TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedTvsUseCase().invoke(Language.Korea.code).asSnapshot()
        println(data)
    }

    @Test
    fun `Top Rated TV 가져오기_Real - Only Android`() = runTest {
        val getTvsUseCase = get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY))
        getTvsUseCase(Language.Korea.code).asSnapshot().forEach {
            println(it)
        }
    }

    @Test
    fun `Top Rated TV 가져오기 성공_Fake`() = runTest {
        val data = FakeGetTopRatedTvsUseCase().invoke(Language.Korea.code).asSnapshot()
        println(data)
    }
}