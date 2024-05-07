package tech.okcredit.auth.usecases

import io.ktor.http.*
import me.tatarka.inject.annotations.Inject
import tech.okcredit.auth.AccessTokenNotFound

@Inject
class CookieHelper {
    fun getCookie(headers: Headers): String {
        if (headers[COOKIE_RESPONSE_HEADER_NAME].isNullOrEmpty().not()) {
            return headers[COOKIE_RESPONSE_HEADER_NAME]!!
        } else {
            throw AccessTokenNotFound()
        }
    }
}

const val COOKIE_RESPONSE_HEADER_NAME = "Set-Cookie"
