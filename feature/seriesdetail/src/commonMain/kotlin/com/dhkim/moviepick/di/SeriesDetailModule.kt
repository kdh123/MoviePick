package com.dhkim.moviepick.di


import com.dhkim.moviepick.SeriesDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val seriesDetailModule = module {
    viewModel { parametersHolder ->
        SeriesDetailViewModel(parametersHolder.get(), parametersHolder.get(), get(), get())
    }
}