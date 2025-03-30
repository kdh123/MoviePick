package com.dhkim.home

import app.cash.paging.testing.asSnapshot
import com.dhkim.core.testing.movie.FakeGetMovieWithCategoryUseCase
import com.dhkim.core.testing.movie.FakeGetTodayRecommendationMovieUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.home.movie.MovieAction
import com.dhkim.home.movie.MovieDisplayState
import com.dhkim.home.movie.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: MovieViewModel
    private lateinit var getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase
    private lateinit var getTodayRecommendationMovieUseCase: GetMoviesUseCase

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getTodayRecommendationMovieUseCase = FakeGetTodayRecommendationMovieUseCase()
        getMovieWithCategoryUseCase = FakeGetMovieWithCategoryUseCase()
        viewModel = MovieViewModel(getTodayRecommendationMovieUseCase, getMovieWithCategoryUseCase)
    }

    @Test
    fun `영화 가져오기 성공`() = runTest {
        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }

    @Test
    fun `영화 카테고리 선택`() = runTest {
        viewModel.uiState.first()
        viewModel.onAction(MovieAction.SelectCategory(Category.MovieGenre.ACTION))
        val state = viewModel.uiState.value.displayState
        if (state is MovieDisplayState.CategoryContents) {
            println("movies : ${state.movies.asSnapshot()}")
        }
    }
}