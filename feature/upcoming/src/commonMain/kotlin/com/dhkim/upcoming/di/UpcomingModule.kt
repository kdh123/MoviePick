package com.dhkim.upcoming.di


import com.dhkim.domain.movie.usecase.TOP_RATED_MOVIES_KEY
import com.dhkim.domain.tv.usecase.TOP_RATED_TVS_KEY
import com.dhkim.upcoming.UpcomingViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val upcomingModule = module {
    viewModel { UpcomingViewModel(get(), get(named(TOP_RATED_MOVIES_KEY)), get(named(TOP_RATED_TVS_KEY))) }
}