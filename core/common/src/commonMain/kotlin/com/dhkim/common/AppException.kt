package com.dhkim.common

class AppException(
    val code: Int,
    message: String? = null
) : Exception(message)

fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        else -> AppException(code = -1, message = this.message)
    }
}