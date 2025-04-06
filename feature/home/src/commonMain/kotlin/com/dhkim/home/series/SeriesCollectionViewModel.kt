package com.dhkim.home.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.SeriesType
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class SeriesCollectionViewModel(
    private val series: String,
    private val genreId: Int?,
    private val regionCode: String?,
    private val getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase,
    private val getTvWithCategoryUseCase: GetTvWithCategoryUseCase,
) : ViewModel(), KoinComponent {
    val genre = genreId?.let(Genre::seriesGenre).run {
        if (this == Genre.Unknown) null else this
    }
    val region = regionCode?.let(Region::seriesRegion)
    val category: String = when {
        genre != null -> Genre.seriesGenre(genre.id)?.genre
        region != null -> Region.seriesRegion(region.code)?.country
        else -> null
    }.run {
        this ?: "알 수 없음"
    }

    val uiState: StateFlow<SeriesCollectionUiState> = if (series == SeriesType.MOVIE.name) {
        getMovieWithCategoryUseCase(language = Language.Korea, genre = genre, region = region)
    } else {
        getTvWithCategoryUseCase(language = Language.Korea, genre = genre, region = region)
    }.map { pagingData ->
        pagingData.map { it as com.dhkim.common.Series }
    }.catch { }
        .cachedIn(viewModelScope)
        .map {
            it.toUiState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SeriesCollectionUiState()
        )

    private suspend fun PagingData<com.dhkim.common.Series>.toUiState(): SeriesCollectionUiState {
        return SeriesCollectionUiState(
            categoryName = category,
            displayState = SeriesCollectionDisplayState.Contents(series = flowOf(this).stateIn(viewModelScope))
        )
    }
}