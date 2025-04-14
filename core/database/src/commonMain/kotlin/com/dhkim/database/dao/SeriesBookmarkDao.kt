package com.dhkim.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhkim.database.SeriesBookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesBookmarkDao {
    @Query("SELECT * FROM SeriesBookmarkEntity")
    fun getBookmarks(): Flow<List<SeriesBookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(series: SeriesBookmarkEntity)

    @Delete
    suspend fun delete(series: SeriesBookmarkEntity)
}