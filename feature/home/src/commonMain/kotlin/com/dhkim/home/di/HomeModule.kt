package com.dhkim.home.di


import com.dhkim.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val homeModule = module {
    viewModel { HomeViewModel(get(), get()) }
}