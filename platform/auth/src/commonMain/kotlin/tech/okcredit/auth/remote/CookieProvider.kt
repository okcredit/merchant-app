package tech.okcredit.auth.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import okcredit.base.randomUUID
import tech.okcredit.auth.*
import tech.okcredit.auth.local.AuthLocalSource
import tech.okcredit.auth.remote.AuthApiClient.Companion.GRANT_TYPE_REFRESH_TOKEN
import tech.okcredit.auth.remote.model.request.AuthenticateRequest
import tech.okcredit.auth.usecases.CookieHelper

@Singleton
@Inject
class CookieProvider(
    private val authLocalSource: AuthLocalSource,
    private val authApiClient: () -> AuthApiClient,
    private val cookieHelper: CookieHelper,
) {

    companion object {
        const val RESPONSE_CODE_INVALID_GRANT = 400
        const val RESPONSE_CODE_SESSION_LIMIT_EXCEEDED = 403
        const val RESPONSE_CODE_EARLY_ACCESS = 425
    }

    suspend fun current(forceRefresh: Boolean): String? {
        var grant: Grant = authLocalSource.getGrant() ?: return null

        // check if grant is already valid
        if (forceRefresh.not() && grant.isValid()) {
            return grant.accessToken
        }

        val flowId = randomUUID()

        // if not valid, refresh token
        val res = authApiClient.invoke().authenticate(
            req = AuthenticateRequest(
                grant_type = GRANT_TYPE_REFRESH_TOKEN,
                refresh_token = grant.refreshToken,
                origin = 0,
                version = grant.version,
            ),
            flowId = flowId,
        )

        // if token not expired, return current token
        if (res.code == RESPONSE_CODE_EARLY_ACCESS) {
            return grant.accessToken
        }

        if (res.code == RESPONSE_CODE_SESSION_LIMIT_EXCEEDED) {
            // log session exceeded
        }
        if (!res.isSuccessful) throw res.asAuthenticationErrors()

        val cookie = cookieHelper.getCookie(res.headers)
        grant = res.body()!!.toGrant(cookie)

        // save new grant
        authLocalSource.setGrant(grant)
        return grant.accessToken
    }
}
