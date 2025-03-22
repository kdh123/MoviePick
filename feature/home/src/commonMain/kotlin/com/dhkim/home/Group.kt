package com.dhkim.home

sealed interface Group {

    enum class HomeGroup(val title: String): Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화"),
        TODAY_TOP_10_MOVIES(title = "오늘의 TOP 10 영화"),
        NOW_PLAYING_MOVIE(title = "상영 중인 영화"),
        TOP_RATED_MOVIE(title = "평단의 극찬을 받은 영화 명작"),
        AIRING_TODAY_TV(title = "오늘 방영하는 TV 프로그램"),
        ON_THE_AIR_TV(title = "지금 방송 중인 TV 프로그램"),
        TOP_RATED_TV(title = "인기 많은 TV 프로그램"),
    }
    enum class MovieGroup(val title: String) : Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화"),
        ACTION_MOVIE(title = "액션 영화"),
        ROMANCE_MOVIE(title = "로맨스 영화"),
        COMEDY_MOVIE(title = "코미디 영화"),
        THRILLER_MOVIE(title = "스릴러 영화"),
        ADVENTURE_MOVIE(title = "모험 영화"),
        ANIMATION_MOVIE(title = "애니메이션"),
    }
}