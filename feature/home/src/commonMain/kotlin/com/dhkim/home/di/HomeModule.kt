package com.dhkim.home.di


import com.dhim.core.movie.data.di.movieModule
import com.dhkim.home.HomeViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {
    viewModel { HomeViewModel() }
    includes(movieModule)
}

val homeModule = module {
    includes(homeFeatureModule, movieModule)
}