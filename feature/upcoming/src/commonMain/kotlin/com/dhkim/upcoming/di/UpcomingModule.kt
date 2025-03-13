package com.dhkim.upcoming.di


import com.dhkim.core.movie.data.di.movieModule
import com.dhkim.upcoming.UpcomingViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val upcomingFeatureModule = module {
    viewModel { UpcomingViewModel() }
    includes(movieModule)
}

val upcomingModule = module {
    includes(upcomingFeatureModule, movieModule)
}