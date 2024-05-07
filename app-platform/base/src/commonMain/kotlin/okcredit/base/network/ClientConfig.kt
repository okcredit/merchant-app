package okcredit.base.network

import io.ktor.client.*

interface ClientConfig {
    val config: HttpClientConfig<*>.() -> Unit
}
