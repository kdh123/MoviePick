package com.dhkim.home

sealed interface Category {

    enum class Region(val country: String, val code: String) : Category {
        Korea("한국", "KR"),
        US("미국", "US"),
    }

    enum class Genre(val id: Int, val genre: String) : Category {
        ACTION(28, "액션"),
        ADVENTURE(12, "어드벤처"),
        ANIMATION(16, "애니메이션"),
        COMEDY(35, "코미디"),
        CRIME(80, "범죄"),
        DOCUMENTARY(99, "다큐멘터리"),
        DRAMA(18, "드라마"),
        FAMILY(10751, "가족"),
        FANTASY(14, "판타지"),
        HISTORY(36, "역사"),
        HORROR(27, "공포"),
        MUSIC(10402, "뮤지컬"),
        MYSTERY(9648, "미스테리"),
        ROMANCE(10749, "로맨스"),
        SCIENCE_FICTION(878, "SF"),
        TV_MOVIE(10770, "TV 영화"),
        THRILLER(53, "스릴러"),
        WAR(10752, "전쟁"),
        WESTERN(37, "서부"),
    }
}