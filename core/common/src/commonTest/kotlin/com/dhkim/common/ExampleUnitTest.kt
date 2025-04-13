package com.dhkim.common

import kotlin.test.Test

class ExampleUnitTest {

    @Test
    fun `Date 테스트`() {
        val isAfter = Date.isTodayAfter(date = "2025-04-14")
        println(isAfter)
    }
}