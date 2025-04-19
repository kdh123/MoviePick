package com.dhkim.home.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.handle
import com.dhkim.common.onetimeStateIn
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.series.usecase.AddSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.DeleteSeriesBookmarkUseCase
import com.dhkim.domain.series.usecase.GetSeriesBookmarksUseCase
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MovieViewModel(
    private val getMainRecommendationMoviesUseCase: GetMoviesUseCase,
    private val getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase,
    private val getSeriesBookmarksUseCase: GetSeriesBookmarksUseCase,
    private val addSeriesBookmarkUseCase: AddSeriesBookmarkUseCase,
    private val deleteSeriesBookmarkUseCase: DeleteSeriesBookmarkUseCase
) : ViewModel() {

    private val shouldShowMovieGenres = listOf(
        Genre.ACTION,
        Genre.MUSIC,
        Genre.FANTASY,
        Genre.THRILLER,
        Genre.ADVENTURE,
        Genre.ANIMATION
    ).map { it.genre }

    private val appBarItems = listOf(
        SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
        SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
    ).toImmutableList()

    val bookmarks: StateFlow<ImmutableList<SeriesBookmark>> = getSeriesBookmarksUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = persistentListOf()
        )

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.onStart {
        init()
    }.onetimeStateIn(
        scope = viewModelScope,
        initialValue = MovieUiState(displayState = MovieDisplayState.Contents(appBarItems))
    )

    private fun init() {
        viewModelScope.handle(
            block = {
                val language = Language.Korea
                val region = Region.Korea
                val jobs = mutableListOf<Deferred<SeriesItem.Content>>()
                val genres = Genre.entries.filter { shouldShowMovieGenres.contains(it.genre) }
                val mainRecommendationMovie = async {
                    getMainRecommendationMoviesUseCase(language, region)
                        .toContent(group = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE, scope = viewModelScope)
                }
                jobs.add(mainRecommendationMovie)
                for (genre in genres) {
                    val movieSeriesItem = async {
                        getMovieWithCategoryUseCase(language, genre, region)
                            .toContent(group = Group.MovieGroup.entries.first { it.genre == genre }, scope = viewModelScope)
                    }
                    jobs.add(movieSeriesItem)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
                    SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
                ) + jobs.awaitAll()

                _uiState.update { MovieUiState(displayState = MovieDisplayState.Contents(series.toImmutableList())) }
            },
            error = {
            }
        )
    }

    fun onAction(action: MovieAction) {
        when (action) {
            is MovieAction.AddBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.imageUrl,
                            seriesType = seriesType
                        )
                    }
                    addSeriesBookmarkUseCase(seriesEntity)
                }
            }

            is MovieAction.DeleteBookmark -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val seriesEntity = with(action) {
                        SeriesBookmark(
                            id = series.id,
                            title = series.title,
                            imageUrl = series.imageUrl,
                            seriesType = seriesType
                        )
                    }
                    deleteSeriesBookmarkUseCase(seriesEntity)
                }
            }
        }
    }
}