package com.dhkim.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import androidx.paging.testing.asSnapshot
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class UpcomingViewModel(
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getTvsUseCase: GetTvsUseCase
) : ViewModel() {

    val uiState = combine(
        getUpcomingMoviesUseCase(Language.Korea, Region.Korea),
        getMoviesUseCase(Language.Korea, Region.Korea),
        getTvsUseCase(Language.Korea)
    ) { upcomingMovies, movies, tvs ->
        val upcomingSeries = upcomingMovies.map {
            FeaturedSeries(
                series = it,
                group = FeaturedSeriesGroup.Upcoming
            )
        }
        val topRatedMovies = flowOf(movies.map {
            FeaturedSeries(
                series = it,
                group = FeaturedSeriesGroup.TopRated
            )
        }).asSnapshot()

        val topRatedTvs = flowOf(tvs.map {
            FeaturedSeries(
                series = it,
                group = FeaturedSeriesGroup.TopRated
            )
        }).asSnapshot()

        val featuredSeries = (upcomingSeries + topRatedMovies + topRatedTvs).toImmutableList()
        UpcomingUiState(displayState = UpcomingDisplayState.Contents(featuredSeries))
    }.catch {
        emit(UpcomingUiState(displayState = UpcomingDisplayState.Error(errorCode = "", message = it.message ?: "정보를 불러올 수 없습니다.")))
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = UpcomingUiState()
    )
}