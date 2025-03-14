package com.dhkim.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.handle(block: suspend () -> Unit, error: ((Throwable) -> Unit)? = null) {
    launch(CoroutineExceptionHandler { _, throwable ->
        error?.invoke(throwable)
    }) {
        block()
    }
}