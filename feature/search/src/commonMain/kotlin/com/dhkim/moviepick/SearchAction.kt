package com.dhkim.moviepick

sealed interface SearchAction {

    data class TypeSearchQuery(val query: String) : SearchAction
}