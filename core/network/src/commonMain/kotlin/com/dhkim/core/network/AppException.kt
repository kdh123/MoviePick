package com.dhkim.core.network

class AppException(
    val errorCode: Int,
    message: String? = null
) : Exception(message)