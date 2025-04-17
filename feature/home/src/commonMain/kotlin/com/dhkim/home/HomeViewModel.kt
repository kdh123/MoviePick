package com.dhkim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getMoviesUseCase: Map<String, GetMoviesUseCase>,
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
    private val getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
    private val deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase
) : ViewModel() {

    val bookmarks: StateFlow<ImmutableList<SeriesBookmark>> = getSeriesBookmarksUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = persistentListOf()
        )

    val uiState: StateFlow<HomeUiState> = getSeriesItems().map { seriesItems ->
        listOf(
            SeriesItem.AppBar(group = Group.HomeGroup.APP_BAR),
            SeriesItem.Category(group = Group.HomeGroup.CATEGORY),
        ) + seriesItems
    }.map {
        HomeUiState(displayState = HomeDisplayState.Contents(series = it.toImmutableList()))
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = HomeUiState()
    )

    private fun getSeriesItems(): Flow<List<SeriesItem>> {
        return flow {
            val language = Language.Korea
            val region = Region.Korea
            val jobs = mutableListOf<Deferred<SeriesItem.Content>>()

            withContext(Dispatchers.IO) {
                listOf(
                    TODAY_RECOMMENDATION_MOVIE_KEY to Group.HomeGroup.MAIN_RECOMMENDATION_MOVIE,
                    TODAY_TOP_10_MOVIES_KEY to Group.HomeGroup.TODAY_TOP_10_MOVIES,
                    TOP_RATED_MOVIES_KEY to Group.HomeGroup.TOP_RATED_MOVIE,
                    NOW_PLAYING_MOVIES_KEY to Group.HomeGroup.NOW_PLAYING_MOVIE,
                    AIRING_TODAY_TVS_KEY to Group.HomeGroup.AIRING_TODAY_TV,
                    ON_THE_AIR_TVS_KEY to Group.HomeGroup.ON_THE_AIR_TV,
                    TOP_RATED_TVS_KEY to Group.HomeGroup.TOP_RATED_TV
                ).map {
                    async {
                        when (it.second.seriesType) {
                            SeriesType.MOVIE -> {
                                getMoviesUseCase[it.first]!!(language, region).toContent(group = it.second, scope = viewModelScope)
                            }

                            SeriesType.TV -> {
                                getTvsUseCase[it.first]!!(language).toContent(group = it.second, scope = viewModelScope)
                            }

                            null -> throw Exception("Series is null")
                        }
                    }
                }.forEach { job ->
                    jobs.add(job)
                }
            }

            val seriesItems = jobs.awaitAll().map { SeriesItem.Content(it.group, it.series) }
            emit(seriesItems)
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.AddBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action.series) {
                        SeriesBookmark(
                            id = id,
                            title = title,
                            imageUrl = imageUrl
                        )
                    }
                    addSeriesBookmarkUseCase(seriesEntity)
                }
            }

            is HomeAction.DeleteBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action.series) {
                        SeriesBookmark(
                            id = id,
                            title = title,
                            imageUrl = imageUrl
                        )
                    }
                    deleteSeriesBookmarkUseCase(seriesEntity)
                }
            }
        }
    }
}


