package com.dhkim.data.series

import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.database.SeriesBookmarkEntity

fun SeriesBookmarkEntity.toSeriesBookmark(): SeriesBookmark {
    return SeriesBookmark(
        id = id,
        title = title,
        imageUrl = imageUrl,
        seriesType = SeriesType.entries.first { it.name == seriesType }
    )
}

fun SeriesBookmark.toSeriesBookmarkEntity(): SeriesBookmarkEntity {
    return SeriesBookmarkEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        seriesType = seriesType.name
    )
}