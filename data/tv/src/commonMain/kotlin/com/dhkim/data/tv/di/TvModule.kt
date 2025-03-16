package com.dhkim.data.tv.di

import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.data.tv.usecase.GetAiringTodayTvsUseCase
import com.dhkim.data.tv.usecase.GetOnTheAirTvsUseCase
import com.dhkim.data.tv.usecase.GetTopRatedTvsUseCase
import com.dhkim.data.tv.datasource.RemoteTvDataSourceImpl
import com.dhkim.data.tv.repository.TvRepositoryImpl
import com.dhkim.domain.tv.datasource.RemoteTvDataSource
import com.dhkim.domain.tv.repository.TvRepository
import com.dhkim.domain.tv.usecase.AIRING_TODAY_TVS_KEY
import com.dhkim.domain.tv.usecase.GetTvsUseCase
import com.dhkim.domain.tv.usecase.ON_THE_AIR_TVS_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val tvModule = module {
    includes(platformModule, networkModule)
    singleOf(::RemoteTvDataSourceImpl).bind<RemoteTvDataSource>()
    singleOf(::TvRepositoryImpl).bind<TvRepository>()
    single<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)) { GetAiringTodayTvsUseCase(get()) }
    single<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)) { GetOnTheAirTvsUseCase(get()) }
    single<GetTvsUseCase>(named(TOP_RATED_TVS_KEY)) { GetTopRatedTvsUseCase(get()) }
    single {
        mapOf(
            AIRING_TODAY_TVS_KEY to get<GetTvsUseCase>(named(AIRING_TODAY_TVS_KEY)),
            ON_THE_AIR_TVS_KEY to get<GetTvsUseCase>(named(ON_THE_AIR_TVS_KEY)),
            TOP_RATED_TVS_KEY to get<GetTvsUseCase>(named(TOP_RATED_TVS_KEY))
        )
    }
}