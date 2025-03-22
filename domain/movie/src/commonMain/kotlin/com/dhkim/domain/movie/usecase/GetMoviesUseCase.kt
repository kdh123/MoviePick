package com.dhkim.domain.movie.usecase

import app.cash.paging.PagingData
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

const val TODAY_RECOMMENDATION_MOVIE_KEY = "todayRecommendationMovie"
const val TODAY_TOP_10_MOVIES_KEY = "todayTop10Movie"
const val TOP_RATED_MOVIES_KEY = "topRatedMovie"
const val NOW_PLAYING_MOVIES_KEY = "nowPlayingMovie"
const val UPCOMING_MOVIES_KEY = "upcomingMovie"

interface GetMoviesUseCase {

    operator fun invoke(language: Language, region: Region): Flow<PagingData<Movie>>
}