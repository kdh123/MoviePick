package com.dhkim.home.tv

import com.dhkim.home.Category

sealed interface TvAction {

    data class SelectCategory(val category: Category) : TvAction
    data object BackToTvMain : TvAction
}