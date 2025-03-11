package com.dhkim.core.movie.data.di

import com.dhkim.core.movie.data.repository.MovieRepositoryImpl
import com.dhkim.core.movie.data.datasource.RemoteMovieDataSourceImpl
import com.dhkim.core.movie.data.usecase.GetTopRatedMoviesUseCase
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val movieDataModule = module {
    singleOf(::RemoteMovieDataSourceImpl).bind<RemoteMovieDataSource>()
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    single<GetMoviesUseCase>(named("topRated")) { GetTopRatedMoviesUseCase(get()) }
}

val movieModule = module {
    includes(platformModule, networkModule, movieDataModule)
}

