package com.dhkim.home

import com.dhkim.core.testing.movie.FakeGetMovieWithCategoryUseCase
import com.dhkim.core.testing.movie.FakeGetTodayRecommendationMovieUseCase
import com.dhkim.core.testing.series.FakeAddSeriesBookmarkUseCase
import com.dhkim.core.testing.series.FakeDeleteSeriesBookmarkUseCase
import com.dhkim.core.testing.series.FakeGetSeriesBookmarksUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
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
    private lateinit var getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase
    private lateinit var addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase
    private lateinit var deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getTodayRecommendationMovieUseCase = FakeGetTodayRecommendationMovieUseCase()
        getMovieWithCategoryUseCase = FakeGetMovieWithCategoryUseCase()
        getSeriesBookmarksUseCase = FakeGetSeriesBookmarksUseCase()
        addSeriesBookmarkUseCase = FakeAddSeriesBookmarkUseCase()
        deleteSeriesBookmarkUseCase = FakeDeleteSeriesBookmarkUseCase()
        viewModel = MovieViewModel(
            getTodayRecommendationMovieUseCase,
            getMovieWithCategoryUseCase,
            getSeriesBookmarksUseCase,
            addSeriesBookmarkUseCase,
            deleteSeriesBookmarkUseCase
        )
    }

    @Test
    fun `영화 가져오기 성공`() = runTest {
        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }
}