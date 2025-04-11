package com.dhkim.common

import kotlinx.datetime.Clock

data class SeriesImage(
    val id: String = "${Clock.System.now().toEpochMilliseconds()}",
    val imageUrl: String,
    val imageType: ImageType
)

enum class ImageType {
    Portrait,
    Landscape
}