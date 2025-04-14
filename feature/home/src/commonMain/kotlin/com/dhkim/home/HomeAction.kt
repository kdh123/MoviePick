package com.dhkim.home

import com.dhkim.common.Series

sealed interface HomeAction {

    data class AddBookmark(val series: Series) : HomeAction
}