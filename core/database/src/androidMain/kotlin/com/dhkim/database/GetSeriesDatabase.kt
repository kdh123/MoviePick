package com.dhkim.database

import android.content.Context
import androidx.room.Room

fun getSeriesDatabase(context: Context): SeriesDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("series.db")
    return Room.databaseBuilder<SeriesDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).build()
}