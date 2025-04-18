package com.dhkim.home

import com.dhkim.core.testing.movie.FakeGetTodayRecommendationTvUseCase
import com.dhkim.core.testing.tv.FakeGetAiringTodayTvsUseCase
import com.dhkim.core.testing.tv.FakeGetOnTheAirTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTopRatedTvsUseCase
import com.dhkim.core.testing.tv.FakeGetTvWithCategoryUseCase
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.home.tv.TvViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class TvViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: TvViewModel

    private lateinit var getTodayRecommendationTvUseCase: GetTvsUseCase
    private lateinit var getAiringTodayTvsUseCase: GetTvsUseCase
    private lateinit var getOnTheAirTvsUseCase: GetTvsUseCase
    private lateinit var getTopRatedTvsUseCase: GetTvsUseCase
    private lateinit var getTvWithCategoryUseCase: GetTvWithCategoryUseCase
    private lateinit var getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase
    private lateinit var addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase
    private lateinit var deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getTodayRecommendationTvUseCase = FakeGetTodayRecommendationTvUseCase()
        getAiringTodayTvsUseCase = FakeGetAiringTodayTvsUseCase()
        getOnTheAirTvsUseCase = FakeGetOnTheAirTvsUseCase()
        getTopRatedTvsUseCase = FakeGetTopRatedTvsUseCase()
        getTvWithCategoryUseCase = FakeGetTvWithCategoryUseCase()
        viewModel = TvViewModel(
            mapOf(
                TODAY_RECOMMENDATION_TV_KEY to getTodayRecommendationTvUseCase,
                AIRING_TODAY_TVS_KEY to getAiringTodayTvsUseCase,
                ON_THE_AIR_TVS_KEY to getOnTheAirTvsUseCase,
                TOP_RATED_TVS_KEY to getTopRatedTvsUseCase
            ),
            getTvWithCategoryUseCase,
            getSeriesBookmarksUseCase,
            addSeriesBookmarkUseCase,
            deleteSeriesBookmarkUseCase
        )
    }

    @Test
    fun `TV 가져오기 성공`() = runTest {
        viewModel.uiState.first()
        println("answer : ${viewModel.uiState.value}")
    }
}