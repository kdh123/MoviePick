package com.dhkim.core.di

import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.data.datasource.RemoteMovieDataSource
import com.dhkim.data.tv.datasource.RemoteTvDataSourceImpl
import com.dhkim.data.tv.repository.TvRepositoryImpl
import com.dhkim.domain.tv.usecase.GetAiringTodayTvsUseCase
import com.dhkim.domain.tv.usecase.GetOnTheAirTvsUseCase
import com.dhkim.domain.tv.usecase.GetTopRatedTvsUseCase
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.repository.TvRepository
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.movie.usecase.GetNowPlayingMoviesUseCase
import com.dhkim.domain.movie.usecase.GetTopRatedMoviesUseCase
import com.dhkim.domain.movie.usecase.GetUpcomingMoviesUseCase
import com.dhkim.data.repository.MovieRepositoryImpl
import com.dhkim.data.datasource.RemoteMovieDataSourceImpl
import com.dhkim.domain.movie.usecase.GetTodayRecommendationMovieUseCase
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCaseImpl
import com.dhkim.domain.movie.usecase.GetTodayTop10MoviesUseCase
import com.dhkim.domain.movie.repository.MovieRepository
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCaseImpl
import com.dhkim.domain.movie.usecase.GetMovieReviewsUseCase
import com.dhkim.domain.movie.usecase.GetMovieReviewsUseCaseImpl
import com.dhkim.domain.movie.usecase.GetMovieVideoUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCase
import com.dhkim.domain.movie.usecase.GetMovieWithCategoryUseCaseImpl
import com.dhkim.domain.movie.usecase.GetMoviesUseCase
import com.dhkim.domain.movie.usecase.NOW_PLAYING_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.domain.movie.usecase.TODAY_TOP_10_MOVIES_KEY
import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.movie.usecase.UPCOMING_MOVIES_KEY
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTodayRecommendationTvUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCase
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvVideoUseCase
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvVideoUseCaseImpl
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    includes(platformModule, networkModule)

    singleOf(::RemoteMovieDataSourceImpl).bind<RemoteMovieDataSource>()
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    factory<GetMoviesUseCase>(named(TODAY_RECOMMENDATION_MOVIE_KEY)) { GetTodayRecommendationMovieUseCase(get(), get()) }
    factory<GetMoviesUseCase>(named(TODAY_TOP_10_MOVIES_KEY)) { GetTodayTop10MoviesUseCase(get()) }
    factory<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)) { GetTopRatedMoviesUseCase(get()) }
    factory<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)) { GetNowPlayingMoviesUseCase(get()) }
    factory<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY)) { GetUpcomingMoviesUseCase(get()) }
    factoryOf(::GetMovieVideoUseCaseImpl).bind<GetMovieVideoUseCase>()
    factoryOf(::GetMovieWithCategoryUseCaseImpl).bind<GetMovieWithCategoryUseCase>()
    factoryOf(::GetMovieDetailUseCaseImpl).bind<GetMovieDetailUseCase>()
    factoryOf(::GetMovieReviewsUseCaseImpl).bind<GetMovieReviewsUseCase>()

    singleOf(::RemoteTvDataSourceImpl).bind<RemoteTvDataSource>()
    singleOf(::TvRepositoryImpl).bind<TvRepository>()
    factory<GetTvsUseCase>(named(TODAY_RECOMMENDATION_TV_KEY)) { GetTodayRecommendationTvUseCase(get(), get()) }
    factory<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)) { GetAiringTodayTvsUseCase(get()) }
    factory<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)) { GetOnTheAirTvsUseCase(get()) }
    factory<GetTvsUseCase>(named(TOP_RATED_TVS_KEY)) { GetTopRatedTvsUseCase(get()) }
    factoryOf(::GetTvWithCategoryUseCaseImpl).bind<GetTvWithCategoryUseCase>()
    factoryOf(::GetTvVideoUseCaseImpl).bind<GetTvVideoUseCase>()
    factoryOf(::GetTvDetailUseCaseImpl).bind<GetTvDetailUseCase>()
    factoryOf(::GetTvReviewsUseCaseImpl).bind<GetTvReviewsUseCase>()

    factory {
        mapOf(
            TODAY_RECOMMENDATION_MOVIE_KEY to get<GetMoviesUseCase>(named(TODAY_RECOMMENDATION_MOVIE_KEY)),
            TODAY_TOP_10_MOVIES_KEY to get<GetMoviesUseCase>(named(TODAY_TOP_10_MOVIES_KEY)),
            TOP_RATED_MOVIES_KEY to get<GetMoviesUseCase>(named(TOP_RATED_MOVIES_KEY)),
            NOW_PLAYING_MOVIES_KEY to get<GetMoviesUseCase>(named(NOW_PLAYING_MOVIES_KEY)),
            UPCOMING_MOVIES_KEY to get<GetMoviesUseCase>(named(UPCOMING_MOVIES_KEY)),
            TODAY_RECOMMENDATION_TV_KEY to get<GetTvsUseCase>(named(TODAY_RECOMMENDATION_TV_KEY)),
            AIRING_TODAY_TVS_KEY to get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)),
            ON_THE_AIR_TVS_KEY to get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)),
            TOP_RATED_TVS_KEY to get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY)),
        )
    }
}
