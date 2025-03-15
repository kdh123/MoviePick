package com.dhkim.core.tv.data.di

import com.dhkim.core.network.di.networkModule
import com.dhkim.core.network.di.platformModule
import com.dhkim.core.tv.data.usecase.GetAiringTodayTvsUseCase
import com.dhkim.core.tv.data.usecase.GetOnTheAirTvsUseCase
import com.dhkim.core.tv.data.usecase.GetTopRatedTvsUseCase
import com.dhkim.core.tv.data.datasource.RemoteTvDataSourceImpl
import com.dhkim.core.tv.data.repository.TvRepositoryImpl
import com.dhkim.core.tv.domain.datasource.RemoteTvDataSource
import com.dhkim.core.tv.domain.repository.TvRepository
import com.dhkim.core.tv.domain.usecase.GetTvsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val AIRING_TODAY_TVS_KEY = "airingTodayTv"
const val ON_THE_AIR_TVS_KEY = "onTheAirTv"
const val TOP_RATED_TVS_KEY = "topRatedTv"

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