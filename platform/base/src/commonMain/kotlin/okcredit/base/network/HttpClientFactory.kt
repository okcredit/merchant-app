@file:OptIn(ExperimentalSerializationApi::class)

package okcredit.base.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.AppVersion
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Singleton
import okcredit.base.randomUUID

typealias AuthorizedHttpClient = HttpClient
typealias DefaultHttpClient = HttpClient

@Singleton
@Inject
class HttpClientFactory(
    private val serializerModules: Set<SerializersModule> = emptySet(),
    private val debug: Debug,
    private val appVersion: AppVersion,
    private val clientConfigs: Set<ClientConfig>,
    private val baseUrl: BaseUrl,
) {

    fun createHttpClient(): HttpClient {
        return providePlatformClient().config {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HEADER_REQUEST_ID, randomUUID())
                header(HEADER_DATE, Clock.System.now().epochSeconds)
                header(HEADER_APP_VERSION, appVersion)
                url(baseUrl)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        prettyPrint = debug
                        if (serializerModules.isNotEmpty()) {
                            serializersModule =
                                serializerModules.reduce { acc, serializersModule -> acc + serializersModule }
                        }
                    },
                )
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(HttpRequestRetry) {
                retryOnExceptionOrServerErrors(maxRetries = 3)
                exponentialDelay()
            }

            clientConfigs.forEach {
                it.config.invoke(this@config)
            }

            if (debug) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
}

expect fun providePlatformClient(): HttpClient

const val HEADER_BUSINESS_ID = "OKC-BUSINESS-ID"
const val HEADER_REQUEST_ID = "X-Request-ID"
const val HEADER_DATE = "Date"
const val HEADER_APP_VERSION = "OKC_APP_VERSION"
