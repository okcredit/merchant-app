package tech.okcredit.auth.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.di.Singleton
import okcredit.base.network.DefaultHttpClient
import okcredit.base.network.post
import okcredit.base.randomUUID
import tech.okcredit.auth.*
import tech.okcredit.auth.local.AuthLocalSource
import tech.okcredit.auth.remote.AuthApiClient.Companion.GRANT_TYPE_REFRESH_TOKEN
import tech.okcredit.auth.remote.model.request.AuthenticateRequest
import tech.okcredit.auth.remote.model.response.AuthenticateResponse
import tech.okcredit.auth.usecases.CookieHelper

@Singleton
@Inject
class CookieProvider(
    private val authLocalSource: AuthLocalSource,
    private val cookieHelperLazy: Lazy<CookieHelper>,
    private val baseUrl: BaseUrl,
    private val defaultHttpClient: DefaultHttpClient,
) {

    private val cookieHelper by lazy { cookieHelperLazy.value }

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
        val response = defaultHttpClient.post<AuthenticateRequest, AuthenticateResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v3/auth",
            requestBody = AuthenticateRequest(
                grant_type = GRANT_TYPE_REFRESH_TOKEN,
                refresh_token = grant.refreshToken,
                origin = 0,
                version = grant.version,
            ),
            headers = mapOf(AuthApiClient.OKC_LOGIN_FLOW_ID to flowId),
        )

        // if token not expired, return current token
        if (response.code == RESPONSE_CODE_EARLY_ACCESS) {
            return grant.accessToken
        }

        if (response.code == RESPONSE_CODE_SESSION_LIMIT_EXCEEDED) {
            // log session exceeded
        }
        if (!response.isSuccessful) throw response.asAuthenticationErrors()

        val cookie = cookieHelper.getCookie(response.headers)
        grant = response.body()!!.toGrant(cookie)

        // save new grant
        authLocalSource.setGrant(grant)
        return grant.accessToken
    }
}
