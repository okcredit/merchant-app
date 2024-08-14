package okcredit.base.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun providePlatformClient(): HttpClient {
    return HttpClient(Darwin)
}
