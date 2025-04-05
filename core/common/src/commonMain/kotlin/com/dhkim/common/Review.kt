package com.dhkim.common

data class Review(
    val id: String,
    val author: String,
    val createdAt: String,
    val content: String,
    val rating: Double
)
