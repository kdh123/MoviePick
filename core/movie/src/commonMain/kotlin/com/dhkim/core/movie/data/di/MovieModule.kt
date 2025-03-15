package com.dhkim.core.movie.data.di

import com.dhkim.core.movie.data.repository.MovieRepositoryImpl
import com.dhkim.core.movie.data.datasource.RemoteMovieDataSourceImpl
import com.dhkim.core.movie.data.usecase.GetNowPlayingMoviesUseCase
import com.dhkim.core.movie.data.usecase.GetTopRatedMoviesUseCase
import com.dhkim.core.movie.data.usecase.GetUpcomingMoviesUseCase
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val TOP_RATED_MOVIES_KEY = "topRated"
const val NOW_PLAYING_MOVIES_KEY = "nowPlaying"
const val UPCOMING_MOVIES_KEY = "upcoming"

val movieModule = module {
    singleOf(::RemoteMovieDataSourceImpl).bind<RemoteMovieDataSource>()
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    single<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)) { GetTopRatedMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)) { GetNowPlayingMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY)) { GetUpcomingMoviesUseCase(get()) }
    single {
        mapOf(
            TOP_RATED_MOVIES_KEY to get<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)),
            NOW_PLAYING_MOVIES_KEY to get<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)),
            UPCOMING_MOVIES_KEY to get<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY))
        )
    }
    includes(platformModule, networkModule)
}