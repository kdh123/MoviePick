package com.dhkim.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.handle(block: suspend CoroutineScope.() -> Unit, error: ((Throwable) -> Unit)? = null) {
    launch(CoroutineExceptionHandler { _, throwable ->
        error?.invoke(throwable)
    }) {
        block()
    }
}

@Composable
fun <T> LifecycleOwner.onStartCollect(flow: Flow<T>, block: suspend (T) -> Unit) {
    LaunchedEffect(flow) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(block)
            }
        }
    }
}

fun <T> Flow<T>.onetimeStateIn(
    scope: CoroutineScope,
    initialValue: T,
    stopTimeOut: Long = 5_000,
): StateFlow<T> {
    val restarter = OneTimeSharingStarted(stopTimeOut = stopTimeOut)
    val stateFlow = stateIn(
        scope = scope,
        started = restarter,
        initialValue = initialValue
    )

    return stateFlow
}

class OneTimeSharingStarted(
    private val stopTimeOut: Long,
    private val replayExpiration: Long = Long.MAX_VALUE,
) : SharingStarted {

    private val hasCollected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val restartFlow: MutableSharedFlow<SharingCommand> =
        MutableSharedFlow(extraBufferCapacity = 2)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return merge(
            restartFlow,
            subscriptionCount.transformLatest { count ->
                if (count > 0 && !hasCollected.value) {
                    emit(SharingCommand.START)
                    hasCollected.value = true
                } else {
                    delay(stopTimeOut)
                    emit(SharingCommand.STOP)
                    if (replayExpiration > 0) {
                        delay(replayExpiration)
                        emit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
                    }
                }
            }.dropWhile {
                it != SharingCommand.START
            }.distinctUntilChanged()
        )
    }
}