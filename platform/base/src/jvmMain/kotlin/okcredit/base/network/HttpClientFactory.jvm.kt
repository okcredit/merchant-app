package okcredit.base.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun providePlatformClient(): HttpClient {
    return HttpClient(CIO)
}
