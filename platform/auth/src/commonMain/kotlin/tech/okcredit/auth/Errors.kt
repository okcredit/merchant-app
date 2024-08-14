package tech.okcredit.auth

import io.ktor.utils.io.errors.IOException
import okcredit.base.network.Response
import okcredit.base.network.asError
import tech.okcredit.auth.remote.CookieProvider.Companion.RESPONSE_CODE_INVALID_GRANT
import tech.okcredit.auth.remote.CookieProvider.Companion.RESPONSE_CODE_SESSION_LIMIT_EXCEEDED

class SessionLimitExceeded : IOException("session_limit_exceeded")

class AccessTokenNotFound : IOException("cookies_not_found_in_the_header")

class InvalidPassword : IllegalArgumentException("invalid_password")

class IncorrectPassword : IllegalArgumentException("incorrect_password")

class InvalidOtp : IllegalArgumentException("invalid_otp")

class ExpiredOtp : IllegalArgumentException("expired_otp")

class ExcessiveRequestException : IllegalArgumentException("excessive_requests")

class InvalidMobile : IllegalArgumentException("invalid_mobile")

class TooManyRequests : IllegalArgumentException("too_many_requests")

class Unauthorized : IOException("unauthorized")

fun <T> Response<T>.asAuthenticationErrors(): Exception {
    if (code == RESPONSE_CODE_INVALID_GRANT) {
        throw Unauthorized()
    } else if (code == RESPONSE_CODE_SESSION_LIMIT_EXCEEDED) {
        throw SessionLimitExceeded()
    }
    throw asError()
}
