package com.dhkim.home.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.filter
import app.cash.paging.map
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Series
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TvViewModel(
    private val getTvsUseCase: Map<String, GetTvsUseCase>,
    private val getTvWithCategoryUseCase: GetTvWithCategoryUseCase,
    private val getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
    private val deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase
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

    val bookmarks: StateFlow<ImmutableList<SeriesBookmark>> = getSeriesBookmarksUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = persistentListOf()
        )

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
        when (action) {
            is TvAction.AddBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.imageUrl ?: "",
                            seriesType = seriesType
                        )
                    }
                    addSeriesBookmarkUseCase(seriesEntity)
                }
            }

            is TvAction.DeleteBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.imageUrl ?: "",
                            seriesType = seriesType
                        )
                    }
                    deleteSeriesBookmarkUseCase(seriesEntity)
                }
            }
        }
    }

    private suspend fun Flow<PagingData<out Series>>.toContent(group: Group, scope: CoroutineScope): SeriesItem.Content {
        return SeriesItem.Content(
            group = group,
            series = map { pagingData ->
                val seenIds = mutableSetOf<String>()
                pagingData.filter { seenIds.add(it.id) }.map { it as Series }
            }
                .catch { error ->
                    _uiState.update {
                        TvUiState(
                            displayState = TvDisplayState.Error(
                                errorCode = "",
                                message = error.message ?: "TV 정보를 불러올 수 없습니다."
                            )
                        )
                    }
                }
                .cachedIn(scope)
                .stateIn(scope)
        )
    }
}