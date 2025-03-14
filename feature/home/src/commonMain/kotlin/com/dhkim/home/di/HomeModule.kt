package com.dhkim.home.di


import com.dhkim.core.movie.data.di.NOW_PLAYING_MOVIES_KEY
import com.dhkim.core.movie.data.di.TOP_RATED_MOVIES_KEY
import com.dhkim.core.movie.data.di.movieModule
import com.dhkim.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val homeFeatureModule = module {
    viewModel { HomeViewModel(get(named(TOP_RATED_MOVIES_KEY)), get(named(NOW_PLAYING_MOVIES_KEY))) }
    includes(movieModule)
}

@ExperimentalCoroutinesApi
val homeModule = module {
    includes(homeFeatureModule)
}