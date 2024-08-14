package okcredit.base.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun providePlatformClient(): HttpClient {
    return HttpClient(OkHttp)
}
