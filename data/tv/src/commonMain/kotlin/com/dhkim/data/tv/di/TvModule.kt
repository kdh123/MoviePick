package com.dhkim.data.tv.di

import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.domain.tv.usecase.GetAiringTodayTvsUseCase
import com.dhkim.domain.tv.usecase.GetOnTheAirTvsUseCase
import com.dhkim.domain.tv.usecase.GetTopRatedTvsUseCase
import com.dhkim.data.tv.datasource.RemoteTvDataSourceImpl
import com.dhkim.data.tv.repository.TvRepositoryImpl
import com.dhkim.data.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.repository.TvRepository
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTodayRecommendationTvUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCase
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvVideoUseCase
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCase
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvWithCategoryUseCaseImpl
import com.dhkim.domain.tv.usecase.GetTvVideoUseCaseImpl
import com.dhkim.domain.tv.usecase.TODAY_RECOMMENDATION_TV_KEY
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val tvModule = module {
    includes(platformModule, networkModule)
    factoryOf(::RemoteTvDataSourceImpl).bind<RemoteTvDataSource>()
    factoryOf(::TvRepositoryImpl).bind<TvRepository>()
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
            TODAY_RECOMMENDATION_TV_KEY to get<GetTvsUseCase>(named(TODAY_RECOMMENDATION_TV_KEY)),
            AIRING_TODAY_TVS_KEY to get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)),
            ON_THE_AIR_TVS_KEY to get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)),
            TOP_RATED_TVS_KEY to get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY))
        )
    }
}