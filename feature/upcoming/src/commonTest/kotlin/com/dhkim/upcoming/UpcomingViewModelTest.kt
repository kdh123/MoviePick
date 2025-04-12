package com.dhkim.upcoming

import com.dhkim.core.testing.movie.FakeGetTopRatedMoviesUseCase
import com.dhkim.core.testing.movie.FakeGetUpcomingMoviesUseCase
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
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

class UpcomingViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var getUpcomingUseCase: GetUpcomingMoviesUseCase
    private lateinit var getTopRatedMoviesUseCase: GetMoviesUseCase
    private lateinit var getTopRatedTvsUseCase: GetTvsUseCase
    private lateinit var viewModel: UpcomingViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `Upcoming 시리즈 가져오기`() = runTest {
        getUpcomingUseCase = FakeGetUpcomingMoviesUseCase()
        getTopRatedMoviesUseCase = FakeGetTopRatedMoviesUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()

        viewModel = UpcomingViewModel(
            getUpcomingUseCase,
            getTopRatedMoviesUseCase,
            getTopRatedTvsUseCase
        )

        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}