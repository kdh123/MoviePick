package com.dhkim.home.movie

import com.dhkim.home.Category

sealed interface MovieAction {

    data class SelectCategory(val category: Category) : MovieAction
}