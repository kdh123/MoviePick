package com.dhkim.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toDate(): String {
    return try {
        val instant = Instant.parse(this)
        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
        localDate.toString()
    } catch (e: Exception) {
        ""
    }
}