package okcredit.base.ui

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
interface CoroutineResultWrapper {

    fun <R> drip(s: suspend () -> R): Flow<R> = s.asFlow()

    fun <R> Flow<R>.dropAll(): Flow<Nothing> = transform { }

    fun <R> wrap(s: suspend () -> R): Flow<Result<R>> = wrap(s.asFlow())

    fun <R> wrap(f: Flow<R>) = flow<Result<R>> {
        emit(Result.Progress())
        emitAll(
            f.map { Result.Success(it) }
                .catch {
                    logUseCaseErrors(it)
                    emit(Result.Failure(it))
                },
        )
    }

    private fun logUseCaseErrors(it: Throwable) {
        Logger.w(throwable = it) { "Use case error: ${it.message}" }
    }
}
