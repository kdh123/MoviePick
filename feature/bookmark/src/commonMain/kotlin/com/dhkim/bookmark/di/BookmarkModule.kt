package com.dhkim.bookmark.di

import com.dhkim.bookmark.BookmarkViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val bookmarkModule = module {
    viewModel { BookmarkViewModel(get(), get(), get()) }
}