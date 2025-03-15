package com.dhkim.upcoming.di


import com.dhkim.upcoming.UpcomingViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val upcomingModule = module {
    viewModel { UpcomingViewModel() }
}