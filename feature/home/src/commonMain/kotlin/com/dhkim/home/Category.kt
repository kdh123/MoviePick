package com.dhkim.home

sealed interface Category {

    enum class Region(val country: String, val code: String) : Category {
        Korea("한국", "KR"),
        US("미국", "US"),
    }

    enum class MovieGenre(val id: Int, val genre: String) : Category {
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

    enum class TvGenre(val id: Int, val genre: String) : Category {
        ACTION_ADVENTURE(10759, "액션 & 어드벤처"),
        ANIMATION(16, "애니메이션"),
        COMEDY(35, "코미디"),
        CRIME(80, "범죄"),
        DOCUMENTARY(99, "다큐멘터리"),
        DRAMA(18, "드라마"),
        FAMILY(10751, "가족"),
        KIDS(10762, "Kids"),
        MYSTERY(9648, "미스터리"),
        NEWS(10763, "뉴스"),
        REALITY(10764, "리얼리티"),
        SCI_FI_FANTASY(10765, "SF & 판타지"),
        SOAP(10766, "연속극"),
        TALK(10767, "토크쇼"),
        WAR_POLITICS(10768, "전쟁 & 정치"),
        WESTERN(37, "서부");
    }
}