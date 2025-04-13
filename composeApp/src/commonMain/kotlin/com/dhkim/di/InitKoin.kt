package com.dhkim.di

import com.dhkim.bookmark.di.bookmarkModule
import com.dhkim.core.di.coreModule
import com.dhkim.home.di.homeModule
import com.dhkim.moviepick.di.seriesDetailModule
import com.dhkim.upcoming.di.upcomingModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

@ExperimentalCoroutinesApi
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(coreModule, homeModule, seriesDetailModule, upcomingModule, bookmarkModule)
    }
}