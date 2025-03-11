package com.dhkim.core.movie.domain.model

data class Movie(
    val id: String,
    val title: String,
    val overview: String,
    val genre: List<String>,
    val imageUrl: String,
)

enum class MovieGenre(val id: Int, val genre: String) {
    ACTION(28, "액션"),
    ADVENTURE(12, "어드벤처"),
    ANIMATION(16, "애니메이션"),
    COMEDY(35, "코미디"),
    CRIME(80, "범죄"),
    DOCUMENTARY(99, "다큐"),
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
    Unknown(-1, "알 수 없음");

    companion object {
        fun movieGenre(id: Int): MovieGenre = entries.find { it.id == id } ?: Unknown
    }
}