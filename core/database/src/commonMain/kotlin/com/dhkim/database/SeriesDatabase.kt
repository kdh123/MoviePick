package com.dhkim.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhkim.database.dao.SeriesBookmarkDao

@Database(
    entities = [SeriesBookmarkEntity::class],
    version = 1
)

abstract class SeriesDatabase: RoomDatabase() {

    abstract fun seriesBookmarkDao(): SeriesBookmarkDao
}