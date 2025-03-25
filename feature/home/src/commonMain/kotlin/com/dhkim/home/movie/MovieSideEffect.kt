package com.dhkim.home.movie

sealed interface MovieSideEffect {

    data class ShowCategoryModal(val show: Boolean): MovieSideEffect
}