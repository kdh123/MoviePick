package com.dhkim.data.di

import com.dhkim.data.repository.MovieRepositoryImpl
import com.dhkim.domain.movie.usecase.GetNowPlayingMoviesUseCase
import com.dhkim.domain.movie.usecase.GetTopRatedMoviesUseCase
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.data.datasource.RemoteMovieDataSource
import com.dhkim.data.datasource.RemoteMovieDataSourceImpl
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCaseImpl
import com.dhkim.domain.movie.usecase.GetTodayRecommendationMovieUseCase
import com.dhkim.domain.movie.usecase.GetTodayTop10MoviesUseCase
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCase
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.movie.usecase.UPCOMING_MOVIES_KEY
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val movieModule = module {
    includes(platformModule, networkModule)
    singleOf(::RemoteMovieDataSourceImpl).bind<RemoteMovieDataSource>()
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    factory<GetMovieVideoUseCase> { GetMovieVideoUseCaseImpl(get()) }
    single<GetMoviesUseCase>(named(TODAY_RECOMMENDATION_MOVIE_KEY)) { GetTodayRecommendationMovieUseCase(get(), get()) }
    single<GetMoviesUseCase>(named(TODAY_TOP_10_MOVIES_KEY)) { GetTodayTop10MoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)) { GetTopRatedMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)) { GetNowPlayingMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY)) { GetUpcomingMoviesUseCase(get()) }
    single {
        mapOf(
            TODAY_RECOMMENDATION_MOVIE_KEY to get<GetMoviesUseCase>(named(TODAY_RECOMMENDATION_MOVIE_KEY)),
            TODAY_TOP_10_MOVIES_KEY to get<GetMoviesUseCase>(named(TODAY_TOP_10_MOVIES_KEY)),
            TOP_RATED_MOVIES_KEY to get<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)),
            NOW_PLAYING_MOVIES_KEY to get<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)),
            UPCOMING_MOVIES_KEY to get<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY))
        )
    }
}