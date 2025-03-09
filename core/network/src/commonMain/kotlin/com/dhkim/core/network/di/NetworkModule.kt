package com.dhkim.core.network.di

import com.dhkim.core.network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val networkModule = module {
    single { createHttpClient(get()) }
}