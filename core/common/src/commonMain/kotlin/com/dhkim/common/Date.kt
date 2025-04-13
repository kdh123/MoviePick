package com.dhkim.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Date {

    fun isTodayAfter(date: String): Boolean {
        val currentMoment = Clock.System.now()
        val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        val now = Instant.parse("${currentDateTime.date}T12:00:00Z")

        return now < Instant.parse("${date}T12:00:00Z")
    }
}