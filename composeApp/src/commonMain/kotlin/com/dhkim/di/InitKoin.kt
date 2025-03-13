package com.dhkim.di

import com.dhkim.home.di.homeModule
import com.dhkim.upcoming.di.upcomingModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(homeModule, upcomingModule)
    }
}