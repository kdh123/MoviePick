package com.dhkim.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeriesBookmarkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String
)