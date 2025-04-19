package com.dhkim.home

import com.dhkim.common.Genre
import com.dhkim.common.SeriesType

sealed interface Group {

    enum class HomeGroup(val title: String, val seriesType: SeriesType? = null): Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화", SeriesType.MOVIE),
        TODAY_TOP_10_MOVIES(title = "오늘의 TOP 10 영화", SeriesType.MOVIE),
        NOW_PLAYING_MOVIE(title = "상영 중인 영화", SeriesType.MOVIE),
        TOP_RATED_MOVIE(title = "평단의 극찬을 받은 영화 명작", SeriesType.MOVIE),
        AIRING_TODAY_TV(title = "오늘 방영하는 TV 프로그램", SeriesType.TV),
        ON_THE_AIR_TV(title = "지금 방송 중인 TV 프로그램", SeriesType.TV),
        TOP_RATED_TV(title = "인기 많은 TV 프로그램", SeriesType.TV),
    }

    enum class MovieGroup(val title: String, val genre: Genre? = null) : Group {
        APP_BAR(title = "앱바"),
        CATEGORY(title = "카테고리"),
        MAIN_RECOMMENDATION_MOVIE(title = "오늘 대표 추천 영화"),
        ACTION_MOVIE(title = "액션", genre = Genre.ACTION),
        FANTASY_MOVIE(title = "판타지", genre = Genre.FANTASY),
        MUSIC_MOVIE(title = "음악", genre = Genre.MUSIC),
        THRILLER_MOVIE(title = "스릴러", genre = Genre.THRILLER),
        ADVENTURE_MOVIE(title = "어드벤처", genre = Genre.ADVENTURE),
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