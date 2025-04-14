package com.dhkim.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getSeriesDatabase(): RoomDatabase.Builder<SeriesDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "series.db")
    return Room.databaseBuilder<SeriesDatabase>(
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
}