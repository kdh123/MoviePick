package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

const val TODAY_RECOMMENDATION_MOVIE_KEY = "todayRecommendationMovie"
const val TOP_RATED_MOVIES_KEY = "topRatedMovie"
const val NOW_PLAYING_MOVIES_KEY = "nowPlayingMovie"
const val UPCOMING_MOVIES_KEY = "upcomingMovie"

interface GetMoviesUseCase {

    operator fun invoke(language: String, region: String): Flow<PagingData<Movie>>
}