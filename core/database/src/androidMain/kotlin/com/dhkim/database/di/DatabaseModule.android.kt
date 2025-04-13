package com.dhkim.database.di

import com.dhkim.database.getSeriesDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module = module {
    single {
        getSeriesDatabase(get())
    }
}