package okcredit.base.network

data class ApiError(val code: Int = 500, val error: String = "unknown") : Throwable(message = error) {
    fun mapCode(errorMapping: Pair<Int, Throwable>): Throwable = mapCode(mapOf(errorMapping))

    fun mapCode(errorMappings: Map<Int, Throwable>): Throwable = errorMappings[code] ?: this

    fun mapError(errorMapping: Pair<String, Throwable>): Throwable = mapError(mapOf(errorMapping))

    fun mapError(vararg errorMappings: Pair<String, Throwable>): Throwable = mapError(errorMappings.toMap())

    fun mapError(errorMappings: Map<String, Throwable>): Throwable = errorMappings[error] ?: this
}

fun <T> Response<T>.getOrThrow(): T {
    if (this.isSuccessful) {
        return this.body()!!
    }

    throw this.asError()
}

fun <T> Response<T>.getOrNull(): T? {
    if (this.isSuccessful) {
        return this.body()
    }
    return null
}

fun Response<*>.asError(): ApiError {
    if (this.isSuccessful) throw IllegalStateException("cannot parse ApiError from a successful api call")
    return ApiError(code = code, error = message)
}
