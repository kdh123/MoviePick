package com.dhkim.home.di


import com.dhkim.domain.movie.usecase.TODAY_RECOMMENDATION_MOVIE_KEY
import com.dhkim.home.HomeViewModel
import com.dhkim.home.movie.MovieViewModel
import com.dhkim.home.series.SeriesCollectionViewModel
import com.dhkim.home.tv.TvViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val homeModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { MovieViewModel(get(named(TODAY_RECOMMENDATION_MOVIE_KEY)), get(), get(), get(), get()) }
    viewModel { TvViewModel(get(), get(), get(), get(), get()) }
    viewModel { parametersHolder ->
        SeriesCollectionViewModel(
            parametersHolder.get(),
            parametersHolder.get(),
            parametersHolder.get(),
            get(),
            get()
        )
    }
}