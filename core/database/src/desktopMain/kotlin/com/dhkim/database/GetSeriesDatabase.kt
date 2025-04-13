package com.dhkim.database

import androidx.room.Room
import java.io.File

fun getSeriesDatabase(): SeriesDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "series.db")
    return Room.databaseBuilder<SeriesDatabase>(
        name = dbFile.absolutePath,
    ).build()
}