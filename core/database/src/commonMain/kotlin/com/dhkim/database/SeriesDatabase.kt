package com.dhkim.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.dhkim.database.dao.SeriesBookmarkDao

@Database(
    entities = [SeriesBookmarkEntity::class],
    version = 2
)
@ConstructedBy(SeriesDatabaseConstructor::class)
abstract class SeriesDatabase: RoomDatabase() {

    abstract fun seriesBookmarkDao(): SeriesBookmarkDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object SeriesDatabaseConstructor : RoomDatabaseConstructor<SeriesDatabase> {
    override fun initialize(): SeriesDatabase
}