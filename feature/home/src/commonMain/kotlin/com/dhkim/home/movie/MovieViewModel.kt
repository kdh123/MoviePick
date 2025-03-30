package com.dhkim.home.movie

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
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.home.Category
import com.dhkim.home.Group
import com.dhkim.home.SeriesItem
import com.dhkim.home.toContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
class MovieViewModel(
    private val getMainRecommendationMoviesUseCase: GetMoviesUseCase,
    private val getMovieWithCategoryUseCase: GetMovieWithCategoryUseCase
) : ViewModel() {

    private val shouldShowMovieGenres = listOf(
        Genre.ACTION,
        Genre.ROMANCE,
        Genre.COMEDY,
        Genre.THRILLER,
        Genre.ADVENTURE,
        Genre.ANIMATION
    ).map { it.genre }

    private val appBarItems = listOf(
        SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
        SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
    ).toImmutableList()

    private val currentMovieContents = MutableStateFlow<ImmutableList<SeriesItem>>(persistentListOf())

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = combine(
        currentMovieContents,
        _uiState
    ) { seriesItems, uiState ->
        uiState.createUiState(seriesItems)
    }.onStart {
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
                        .toContent(group = Group.MovieGroup.MAIN_RECOMMENDATION_MOVIE, viewModelScope)
                }
                jobs.add(mainRecommendationMovie)
                for (genre in genres) {
                    val movieSeriesItem = async {
                        getMovieWithCategoryUseCase(language, genre, region)
                            .toContent(group = Group.MovieGroup.entries.first { it.genre == genre }, viewModelScope)
                    }
                    jobs.add(movieSeriesItem)
                }

                val series = listOf(
                    SeriesItem.AppBar(group = Group.MovieGroup.APP_BAR),
                    SeriesItem.Category(group = Group.MovieGroup.CATEGORY),
                ) + jobs.awaitAll()
                currentMovieContents.update { series.toImmutableList() }
            },
            error = {
            }
        )
    }

    fun onAction(action: MovieAction) {
        when (action) {
            is MovieAction.SelectCategory -> {
                selectCategory(action.category)
            }

            MovieAction.BackToMovieMain -> {
                _uiState.update {
                    MovieUiState(displayState = MovieDisplayState.Contents(currentMovieContents.value))
                }
            }
        }
    }

    private fun selectCategory(category: Category) {
        viewModelScope.handle(
            block = {
                when (category) {
                    is Category.MovieGenre -> {
                        val movies = getGenreMovies(category.id)
                        _uiState.update {
                            MovieUiState(displayState = MovieDisplayState.CategoryContents(category = category.genre, movies = movies))
                        }
                    }

                    is Category.Region -> {
                        val movies = getRegionMovies(category.code)
                        _uiState.update {
                            MovieUiState(displayState = MovieDisplayState.CategoryContents(category = category.country, movies = movies))
                        }
                    }

                    is Category.TvGenre -> {}
                }
            },
            error = {

            }
        )
    }

    private suspend fun getGenreMovies(categoryId: Int): StateFlow<PagingData<Series>> {
        val movies = getMovieWithCategoryUseCase(
            language = Language.Korea,
            genre = Genre.seriesGenre(categoryId)
        ).first()
        return flowOf(movies)
            .map { pagingData ->
                pagingData.map { it as Series }
            }
            .catch { }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope)
    }

    private suspend fun getRegionMovies(countryCode: String): StateFlow<PagingData<Series>> {
        val movies = getMovieWithCategoryUseCase(
            language = Language.Korea,
            region = Region.entries.firstOrNull { it.code == countryCode }
        ).first()
        return flowOf(movies)
            .map { pagingData ->
                pagingData.map { it as Series }
            }
            .catch { }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope)
    }

    private fun MovieUiState.createUiState(seriesItems: ImmutableList<SeriesItem>): MovieUiState {
        return when (displayState) {
            MovieDisplayState.Loading -> {
                MovieUiState(displayState = MovieDisplayState.Contents(seriesItems))
            }

            is MovieDisplayState.Contents -> {
                MovieUiState(displayState = MovieDisplayState.Contents(seriesItems))
            }

            is MovieDisplayState.CategoryContents -> {
                this
            }

            is MovieDisplayState.Error -> {
                this
            }
        }
    }
}