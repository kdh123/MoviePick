package com.dhkim.data.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.database.SeriesBookmarkEntity

fun SeriesBookmarkEntity.toSeriesBookmark(): SeriesBookmark {
    return SeriesBookmark(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}

fun SeriesBookmark.toSeriesBookmarkEntity(): SeriesBookmarkEntity {
    return SeriesBookmarkEntity(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}