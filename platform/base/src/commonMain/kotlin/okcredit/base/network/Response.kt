package okcredit.base.network

import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

/** An HTTP response.  */
@Suppress("MemberVisibilityCanBePrivate")
class Response<T> private constructor(
    private val rawStatus: HttpStatusCode,
    private val rawHeaders: Headers,
    private val body: T?,
    private val errorBody: Any?,
) {
    /** The raw response from the HTTP client.  */

    /** HTTP status.  */
    val status: HttpStatusCode get() = rawStatus

    /** HTTP status code.  */
    val code: Int
        get() = status.value

    /** HTTP status message or null if unknown.  */
    public val message: String
        get() = status.toString()

    /** HTTP headers.  */
    public val headers: Headers
        get() = rawHeaders

    /** Returns true if status code is in the range [200..300).  */
    public val isSuccessful: Boolean
        get() = status.isSuccess()

    /** The deserialized response body of a [isSuccessful] response.  */
    public fun body(): T? = body

    /** The raw response body of an [unsuccessful] response.  */
    public fun errorBody(): Any? = errorBody

    override fun toString(): String = "Response{status=$status, headers=$headers, body=$body, errorBody=$errorBody}"

    public companion object {
        /**
         * Create a successful response from `rawResponse` with `body` as the deserialized
         * body.
         */
        public fun <T> success(
            body: T,
            status: HttpStatusCode,
            headers: Headers,
        ): Response<T> {
            require(status.isSuccess()) { "rawResponse must be successful response" }
            return Response(status, headers, body, null)
        }

        /** Create an error response from `rawResponse` with `body` as the error body.  */
        public fun <T> error(
            body: Any,
            status: HttpStatusCode,
            headers: Headers,
        ): Response<T> {
            require(!status.isSuccess()) { "rawResponse should not be successful response" }
            return Response(status, headers, null, body)
        }
    }
}
