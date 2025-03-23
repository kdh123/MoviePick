package com.dhkim.home

sealed interface HomeAction {

    data object BackToHome : HomeAction
}