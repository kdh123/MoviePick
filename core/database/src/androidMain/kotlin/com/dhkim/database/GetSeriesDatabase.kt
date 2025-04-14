package com.dhkim.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getSeriesDatabase(context: Context): RoomDatabase.Builder<SeriesDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("series.db")
    return Room.databaseBuilder<SeriesDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
