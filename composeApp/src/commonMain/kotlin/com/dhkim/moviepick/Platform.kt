package com.dhkim.moviepick

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform