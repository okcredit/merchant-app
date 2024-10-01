package tech.okcredit.auth.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.utils.io.KtorDsl
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableDeferred
import me.tatarka.inject.annotations.Inject
import okcredit.base.network.ClientConfig
import okcredit.base.network.HttpClientFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import tech.okcredit.auth.isValid
import tech.okcredit.auth.local.AuthLocalSource
import tech.okcredit.auth.remote.CookieProvider

@Inject
@SingleIn(AppScope::class)
class AuthClientConfig(
    cookieProviderLazy: Lazy<CookieProvider>,
    localSourceLazy: Lazy<AuthLocalSource>,
) : ClientConfig {

    private val localSource by lazy { localSourceLazy.value }
    private val cookieProvider by lazy { cookieProviderLazy.value }

    override val config: HttpClientConfig<*>.() -> Unit
        get() = {
            install(Auth) {
                cookie {
                    loadTokens {
                        println("loadTokens")
                        val token = localSource.getGrant().takeIf { it.isValid() }?.accessToken
                        println("loadTokens: $token")
                        token
                    }
                    refreshTokens {
                        println("refreshTokens")
                        cookieProvider.current(true)
                    }
                }
            }
        }
}

fun AuthConfig.cookie(block: CookieAuthConfig.() -> Unit) {
    with(CookieAuthConfig().apply(block)) {
        this@cookie.providers.add(CookieAuthProvider(refreshTokens, loadTokens))
    }
}

@KtorDsl
class CookieAuthConfig {
    internal var refreshTokens: suspend () -> String? = { null }
    internal var loadTokens: suspend () -> String? = { null }

    /**
     * Configures a callback that refreshes a token when the 401 status code is received.
     */
    fun refreshTokens(block: suspend () -> String?) {
        refreshTokens = block
    }

    /**
     * Configures a callback that loads a cached token from a local storage.
     * Note: Using the same client instance here to make a request will result in a deadlock.
     */
    fun loadTokens(block: suspend () -> String?) {
        loadTokens = block
    }
}

class CookieAuthProvider(
    private val refreshTokens: suspend () -> String?,
    loadTokens: suspend () -> String?,
) : AuthProvider {

    private val cookieHolder = CookieHolder(loadTokens)

    override val sendWithoutRequest: Boolean
        get() = true

    override suspend fun addRequestHeaders(
        request: HttpRequestBuilder,
        authHeader: HttpAuthHeader?,
    ) {
        val currentToken = cookieHolder.loadToken()
        currentToken?.let { request.header(HEADER_COOKIE_NAME, it) }
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return true
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean {
        val newToken = cookieHolder.setToken {
            refreshTokens()
        }
        return newToken != null
    }
}

class CookieHolder<T>(private val loadTokens: suspend () -> T) {
    private val refreshTokensDeferred = atomic<CompletableDeferred<T?>?>(null)
    private val loadTokensDeferred = atomic<CompletableDeferred<T?>?>(null)

    internal suspend fun loadToken(): T? {
        var deferred: CompletableDeferred<T?>?
        lateinit var newDeferred: CompletableDeferred<T?>
        while (true) {
            deferred = loadTokensDeferred.value
            val newValue = deferred ?: CompletableDeferred()
            if (loadTokensDeferred.compareAndSet(deferred, newValue)) {
                newDeferred = newValue
                break
            }
        }

        // if there's already a pending loadTokens(), just wait for it to complete
        if (deferred != null) {
            return deferred.await()
        }

        val newTokens = loadTokens()

        // [loadTokensDeferred.value] could be null by now (if clearToken() was called while
        // suspended), which is why we are using [newDeferred] to complete the suspending callback.
        newDeferred.complete(newTokens)

        return newTokens
    }

    internal suspend fun setToken(block: suspend () -> T?): T? {
        var deferred: CompletableDeferred<T?>?
        lateinit var newDeferred: CompletableDeferred<T?>
        while (true) {
            deferred = refreshTokensDeferred.value
            val newValue = deferred ?: CompletableDeferred()
            if (refreshTokensDeferred.compareAndSet(deferred, newValue)) {
                newDeferred = newValue
                break
            }
        }

        val newToken = if (deferred == null) {
            val newTokens = block()

            // [refreshTokensDeferred.value] could be null by now (if clearToken() was called while
            // suspended), which is why we are using [newDeferred] to complete the suspending callback.
            newDeferred.complete(newTokens)
            refreshTokensDeferred.value = null
            newTokens
        } else {
            deferred.await()
        }
        loadTokensDeferred.value = CompletableDeferred(newToken)
        return newToken
    }
}

private const val HEADER_COOKIE_NAME = "Cookie"

@SingleIn(AppScope::class)
@Inject
class AuthorizedHttpClientFactory(
    private val authClientConfig: AuthClientConfig,
    private val httpClientFactory: HttpClientFactory,
) {

    fun createAuthorizedHttpClient() = httpClientFactory.createHttpClient()
        .config {
            authClientConfig.config.invoke(this)
        }
}
