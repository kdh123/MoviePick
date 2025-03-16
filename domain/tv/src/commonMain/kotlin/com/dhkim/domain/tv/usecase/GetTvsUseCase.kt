package com.dhkim.domain.tv.usecase

import app.cash.paging.PagingData
import com.dhkim.domain.tv.model.Tv
import kotlinx.coroutines.flow.Flow

const val AIRING_TODAY_TVS_KEY = "airingTodayTv"
const val ON_THE_AIR_TVS_KEY = "onTheAirTv"
const val TOP_RATED_TVS_KEY = "topRatedTv"

interface GetTvsUseCase {

    operator fun invoke(language: String): Flow<PagingData<Tv>>
}