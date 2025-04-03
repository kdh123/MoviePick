package com.dhkim.common

enum class Region(val country: String, val code: String) {
    Korea("대한민국", "KR"),
    US("미국", "US"),
    Japan("일본", "JP"),
    Unknown("알 수 없음", "Unknown");

    companion object {
        fun seriesRegion(code: String): Region? = Region.entries.find { it.code == code }
    }
}