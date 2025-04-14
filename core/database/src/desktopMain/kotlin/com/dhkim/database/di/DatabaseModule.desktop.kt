package com.dhkim.database.di

import androidx.room.RoomDatabase
import com.dhkim.database.SeriesDatabase
import com.dhkim.database.getSeriesDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module = module {
    single<RoomDatabase.Builder<SeriesDatabase>> {
        getSeriesDatabase()
    }
}