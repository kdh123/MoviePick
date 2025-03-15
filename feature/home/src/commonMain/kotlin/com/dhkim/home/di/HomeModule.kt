package com.dhkim.home.di


import com.dhkim.core.movie.data.datasource.RemoteMovieDataSourceImpl
import com.dhkim.core.movie.data.di.NOW_PLAYING_MOVIES_KEY
import com.dhkim.core.movie.data.di.TOP_RATED_MOVIES_KEY
import com.dhkim.core.movie.data.di.UPCOMING_MOVIES_KEY
import com.dhkim.core.movie.data.repository.MovieRepositoryImpl
import com.dhkim.core.movie.data.usecase.GetNowPlayingMoviesUseCase
import com.dhkim.core.movie.data.usecase.GetTopRatedMoviesUseCase
import com.dhkim.core.movie.data.usecase.GetUpcomingMoviesUseCase
import com.dhkim.core.movie.domain.datasource.RemoteMovieDataSource
import com.dhkim.core.movie.domain.repository.MovieRepository
import com.dhkim.core.movie.domain.usecase.GetMoviesUseCase
import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.core.tv.data.datasource.RemoteTvDataSourceImpl
import com.dhkim.core.tv.data.di.AIRING_TODAY_TVS_KEY
import com.dhkim.core.tv.data.di.ON_THE_AIR_TVS_KEY
import com.dhkim.core.tv.data.di.TOP_RATED_TVS_KEY
import com.dhkim.core.tv.data.repository.TvRepositoryImpl
import com.dhkim.core.tv.data.usecase.GetAiringTodayTvsUseCase
import com.dhkim.core.tv.data.usecase.GetOnTheAirTvsUseCase
import com.dhkim.core.tv.data.usecase.GetTopRatedTvsUseCase
import com.dhkim.core.tv.domain.datasource.RemoteTvDataSource
import com.dhkim.core.tv.domain.repository.TvRepository
import com.dhkim.core.tv.domain.usecase.GetTvsUseCase
import com.dhkim.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val homeModule = module {
    includes(platformModule, networkModule)
    singleOf(::RemoteTvDataSourceImpl).bind<RemoteTvDataSource>()
    singleOf(::TvRepositoryImpl).bind<TvRepository>()
    single<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)) { GetAiringTodayTvsUseCase(get()) }
    single<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)) { GetOnTheAirTvsUseCase(get()) }
    single<GetTvsUseCase>(named(TOP_RATED_TVS_KEY)) { GetTopRatedTvsUseCase(get()) }

    singleOf(::RemoteMovieDataSourceImpl).bind<RemoteMovieDataSource>()
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    single<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)) { GetTopRatedMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)) { GetNowPlayingMoviesUseCase(get()) }
    single<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY)) { GetUpcomingMoviesUseCase(get()) }

    single {
        mapOf(
            AIRING_TODAY_TVS_KEY to get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)),
            ON_THE_AIR_TVS_KEY to get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)),
            TOP_RATED_TVS_KEY to get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY)),
            TOP_RATED_MOVIES_KEY to get<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)),
            NOW_PLAYING_MOVIES_KEY to get<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)),
            UPCOMING_MOVIES_KEY to get<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY))
        )
    }

    viewModel { HomeViewModel(get(), get()) }
}