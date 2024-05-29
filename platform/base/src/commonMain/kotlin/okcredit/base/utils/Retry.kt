package okcredit.base.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

suspend fun <T> withRetry(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100,
    maxDelay: Long = 1000,
    factor: Double = 2.0,
    shouldRetry: (Throwable) -> Boolean = { true },
    block: suspend () -> T,
): T {
    var currentDelay = initialDelay

    if (times <= 0) throw IllegalArgumentException("times should be > 0 but is $times")

    repeat(times - 1) {
        try {
            return block()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Throwable) {
            if (shouldRetry(e)) {
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
            } else {
                throw e
            }
        }
    }

    return block() // last attempt
}
