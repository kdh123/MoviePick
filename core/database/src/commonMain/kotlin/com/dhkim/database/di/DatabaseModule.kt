package com.dhkim.database.di

import com.dhkim.database.CreateDatabase
import com.dhkim.database.SeriesDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val sharedModules = module {
    single<SeriesDatabase> { CreateDatabase(get()).getDatabase() }
}