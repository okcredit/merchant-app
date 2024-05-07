package okcredit.base.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
import okcredit.base.di.AppVersion
import okcredit.base.di.Debug
import okcredit.base.randomUUID

@OptIn(ExperimentalSerializationApi::class)
fun createKtorFit(
    baseUrl: String,
    appVersion: AppVersion,
    debug: Debug,
    clientConfigs: Set<ClientConfig>,
) = Ktorfit.Builder()
    .baseUrl(baseUrl)
    .httpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                },
            )
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(stdHeaderPlugin) {
            this.appVersion = appVersion
        }

        clientConfigs.forEach {
            it.config.invoke(this@httpClient)
        }

        if (debug) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d(message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
    .converterFactories(ResponseConverterFactory())
    .build()

class StdHeadersConfig(
    var appVersion: AppVersion = "",
)

val stdHeaderPlugin = createClientPlugin("StdHeaders", ::StdHeadersConfig) {
    onRequest { request, _ ->
        request.header(HEADER_REQUEST_ID, randomUUID())
        request.header(HEADER_DATE, Clock.System.now().epochSeconds)
        request.header(HEADER_APP_VERSION, this@createClientPlugin.pluginConfig.appVersion)
    }
}

class Unauthorized : Throwable("unauthorized")

const val HEADER_BUSINESS_ID = "OKC-BUSINESS-ID"
private const val HEADER_REQUEST_ID = "X-Request-ID"
private const val HEADER_DATE = "Date"
private const val HEADER_APP_VERSION = "OKC_APP_VERSION"
