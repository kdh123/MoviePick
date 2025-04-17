package com.dhkim.home.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toContent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class TvViewModel(
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
    private val getTvWithCategoryUseCase: GetTvWithCategoryUseCase
) : ViewModel() {

    private val shouldShowTvGenres = listOf(
        Genre.COMEDY,
        Genre.ANIMATION,
        Genre.NEWS
    ).map { it.genre }

    private val appBarItems = listOf(
        SeriesItem.AppBar(group = Group.TvGroup.APP_BAR),
        SeriesItem.Category(group = Group.TvGroup.CATEGORY),
    ).toImmutableList()

    private val _uiState = MutableStateFlow(TvUiState())
    val uiState: StateFlow<TvUiState> = _uiState
        .onStart {
            init()
        }.onetimeStateIn(
            scope = viewModelScope,
            initialValue = TvUiState(displayState = TvDisplayState.Contents(appBarItems))
        )

    private fun init() {
        viewModelScope.handle(
            block = {
                val language = Language.Korea
                val region = Region.Korea
                val jobs = mutableListOf<Deferred<SeriesItem.Content>>()
                val genres = Genre.entries.filter { shouldShowTvGenres.contains(it.genre) }
                val mainRecommendationTv = async {
                    getTvsUseCase[TODAY_RECOMMENDATION_TV_KEY]!!.invoke(language)
                        .toContent(group = Group.TvGroup.MAIN_RECOMMENDATION_TV, scope = viewModelScope)
                }
                jobs.add(mainRecommendationTv)

                for (genre in genres) {
                    val tvsSeriesItem = async {
                        getTvWithCategoryUseCase(language, genre, region)
                            .toContent(group = Group.TvGroup.entries.first { it.genre == genre }, scope = viewModelScope)
                    }
                    jobs.add(tvsSeriesItem)
                }

                listOf(
                    AIRING_TODAY_TVS_KEY to Group.TvGroup.AIRING_TODAY_TV,
                    ON_THE_AIR_TVS_KEY to Group.TvGroup.ON_THE_AIR_TV,
                    TOP_RATED_TVS_KEY to Group.TvGroup.TOP_RATED_TV
                ).map {
                    async {
                        getTvsUseCase[it.first]!!(language).toContent(group = it.second, scope = viewModelScope)
                    }
                }.forEach { job ->
                    jobs.add(job)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.TvGroup.APP_BAR),
                    SeriesItem.Category(group = Group.TvGroup.CATEGORY),
                ) + jobs.awaitAll()

                _uiState.update { TvUiState(displayState = TvDisplayState.Contents(series.toImmutableList())) }
            },
            error = {
            }
        )
    }

    fun onAction(action: TvAction) {

    }
}