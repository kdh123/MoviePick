package com.dhkim.home.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Series
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.home.Category
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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

    private val currentTvContents = MutableStateFlow<ImmutableList<SeriesItem>>(persistentListOf())

    private val _uiState = MutableStateFlow(TvUiState())
    val uiState: StateFlow<TvUiState> = combine(
        currentTvContents,
        _uiState
    ) { seriesItems, uiState ->
        uiState.createUiState(seriesItems)
    }.onStart {
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
                        .toContent(group = Group.TvGroup.MAIN_RECOMMENDATION_TV, viewModelScope)
                }
                jobs.add(mainRecommendationTv)

                for (genre in genres) {
                    val tvsSeriesItem = async {
                        getTvWithCategoryUseCase(language, genre, region)
                            .toContent(group = Group.TvGroup.entries.first { it.genre == genre }, viewModelScope)
                    }
                    jobs.add(tvsSeriesItem)
                }

                listOf(
                    AIRING_TODAY_TVS_KEY to Group.TvGroup.AIRING_TODAY_TV,
                    ON_THE_AIR_TVS_KEY to Group.TvGroup.ON_THE_AIR_TV,
                    TOP_RATED_TVS_KEY to Group.TvGroup.TOP_RATED_TV
                ).map {
                    async {
                        getTvsUseCase[it.first]!!(language).toContent(group = it.second, viewModelScope)
                    }
                }.forEach { job ->
                    jobs.add(job)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.TvGroup.APP_BAR),
                    SeriesItem.Category(group = Group.TvGroup.CATEGORY),
                ) + jobs.awaitAll()
                currentTvContents.update { series.toImmutableList() }
            },
            error = {
            }
        )
    }

    fun onAction(action: TvAction) {
        when (action) {
            is TvAction.SelectCategory -> {
                selectCategory(action.category)
            }

            TvAction.BackToTvMain -> {
                _uiState.update {
                    TvUiState(displayState = TvDisplayState.Contents(currentTvContents.value))
                }
            }
        }
    }

    private fun selectCategory(category: Category) {
        viewModelScope.handle(
            block = {
                when (category) {
                    is Category.TvGenre -> {
                        val tvs = getGenreTvs(category.id)
                        _uiState.update {
                            TvUiState(displayState = TvDisplayState.CategoryContents(category = category.genre, tvs = tvs))
                        }
                    }

                    is Category.Region -> {
                        val tvs = getRegionTvs(category.code)
                        _uiState.update {
                            TvUiState(displayState = TvDisplayState.CategoryContents(category = category.code, tvs = tvs))
                        }
                    }

                    is Category.MovieGenre -> {}
                }
            },
            error = {

            }
        )
    }

    private suspend fun getGenreTvs(categoryId: Int): StateFlow<PagingData<Series>> {
        val tvs = getTvWithCategoryUseCase(
            language = Language.Korea,
            genre = Genre.seriesGenre(categoryId)
        ).first()
        return flowOf(tvs)
            .map { pagingData ->
                pagingData.map { it as Series }
            }
            .catch { }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope)
    }

    private suspend fun getRegionTvs(countryCode: String): StateFlow<PagingData<Series>> {
        val tvs = getTvWithCategoryUseCase(
            language = Language.Korea,
            region = Region.entries.firstOrNull { it.code == countryCode }
        ).first()
        return flowOf(tvs)
            .map { pagingData ->
                pagingData.map { it as Series }
            }
            .catch { }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope)
    }

    private fun TvUiState.createUiState(seriesItems: ImmutableList<SeriesItem>): TvUiState {
        return when (displayState) {
            TvDisplayState.Loading -> {
                TvUiState(displayState = TvDisplayState.Contents(seriesItems))
            }

            is TvDisplayState.Contents -> {
                TvUiState(displayState = TvDisplayState.Contents(seriesItems))
            }

            is TvDisplayState.CategoryContents -> {
                this
            }

            is TvDisplayState.Error -> {
                this
            }
        }
    }
}