package com.dhkim.moviepick.di

import com.dhkim.moviepick.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val searchModule = module {
    viewModel { SearchViewModel(get(), get()) }
}