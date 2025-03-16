package com.dhkim.home

import app.cash.paging.PagingData
import com.dhkim.common.Series
import kotlinx.coroutines.flow.StateFlow

data class HomeMovieItem(
    val group: HomeMovieGroup,
    val series: StateFlow<PagingData<Series>>,
)

enum class HomeMovieGroup(val title: String) {
    TODAY_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화"),
    NOW_PLAYING_MOVIE_TOP_10(title = "오늘 영화 TOP 10 시리즈"),
    TOP_RATED_MOVIE(title = "평단의 극찬을 받은 영화 명작"),
    AIRING_TODAY_TV(title = "오늘 방영하는 TV 시리즈"),
    ON_THE_AIR_TV(title = "지금 방송 중인 TV 시리즈"),
    TOP_RATED_TV(title = "평단의 극찬을 받은 TV 시리즈"),
}
