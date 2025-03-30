package com.dhkim.home

import com.dhkim.common.Genre

enum class Series {
    MOVIE,
    TV
}

sealed interface Group {

    enum class HomeGroup(val title: String, val series: Series? = null): Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화", Series.MOVIE),
        TODAY_TOP_10_MOVIES(title = "오늘의 TOP 10 영화", Series.MOVIE),
        NOW_PLAYING_MOVIE(title = "상영 중인 영화", Series.MOVIE),
        TOP_RATED_MOVIE(title = "평단의 극찬을 받은 영화 명작", Series.MOVIE),
        AIRING_TODAY_TV(title = "오늘 방영하는 TV 프로그램", Series.TV),
        ON_THE_AIR_TV(title = "지금 방송 중인 TV 프로그램", Series.TV),
        TOP_RATED_TV(title = "인기 많은 TV 프로그램", Series.TV),
    }

    enum class MovieGroup(val title: String, val genre: Genre? = null) : Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화"),
        ACTION_MOVIE(title = "액션 영화", genre = Genre.ACTION),
        ROMANCE_MOVIE(title = "로맨스 영화", genre = Genre.ROMANCE),
        COMEDY_MOVIE(title = "코미디 영화", genre = Genre.COMEDY),
        THRILLER_MOVIE(title = "스릴러 영화", genre = Genre.THRILLER),
        ADVENTURE_MOVIE(title = "모험 영화", genre = Genre.ADVENTURE),
        ANIMATION_MOVIE(title = "애니메이션", genre = Genre.ANIMATION),
    }

    enum class TvGroup(val title: String, val genre: Genre? = null) : Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_TV(title = "오늘 대표 추천 TV"),
        COMEDY_TV(title = "코미디", genre = Genre.COMEDY),
        ANIMATION_TV(title = "애니메이션", genre = Genre.ANIMATION),
        NEWS_TV(title = "뉴스", genre = Genre.NEWS),
        AIRING_TODAY_TV(title = "오늘 방영하는 TV 프로그램"),
        ON_THE_AIR_TV(title = "지금 방송 중인 TV 프로그램"),
        TOP_RATED_TV(title = "인기 많은 TV 프로그램"),
    }
}