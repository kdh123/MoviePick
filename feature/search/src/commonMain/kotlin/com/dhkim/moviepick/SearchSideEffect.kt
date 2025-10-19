package com.dhkim.moviepick

sealed interface SearchSideEffect {

    data object ScrollToFirstItem : SearchSideEffect
}