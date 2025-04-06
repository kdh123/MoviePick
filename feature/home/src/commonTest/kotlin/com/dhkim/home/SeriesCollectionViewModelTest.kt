package com.dhkim.home

import app.cash.paging.testing.asSnapshot
import com.dhkim.common.Region
import com.dhkim.common.SeriesType
import com.dhkim.core.testing.movie.FakeGetMovieWithCategoryUseCase
import com.dhkim.core.testing.tv.FakeGetTvWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.home.series.SeriesCollectionDisplayState
import com.dhkim.home.series.SeriesCollectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SeriesCollectionViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: SeriesCollectionViewModel
    private lateinit var getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase
    private lateinit var getTvWithCategoryUseCase: GetTvWithCategoryUseCase

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `영화 가져오기 성공`() = runTest {
        viewModel = SeriesCollectionViewModel(
            series = SeriesType.MOVIE.name,
            genreId = null,
            regionCode = Region.Korea.code,
            getMovieWithCategoryUseCase = FakeGetMovieWithCategoryUseCase(),
            getTvWithCategoryUseCase = FakeGetTvWithCategoryUseCase()
        )


        viewModel.uiState.first()
        val displayState = (viewModel.uiState.value.displayState as? SeriesCollectionDisplayState.Contents) ?: return@runTest


        if (viewModel.uiState.value.displayState is SeriesCollectionDisplayState.Contents) {
            println("movies : ${displayState.series.asSnapshot()}")
        }

    }

}