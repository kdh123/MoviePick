package com.dhkim.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dhkim.database.SeriesBookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesBookmarkDao {
    @Query("SELECT * FROM SeriesBookmarkEntity")
    fun getBookmarks(): Flow<List<SeriesBookmarkEntity>>

    @Insert
    fun insert(series: SeriesBookmarkEntity)

    @Delete
    fun delete(series: SeriesBookmarkEntity)
}