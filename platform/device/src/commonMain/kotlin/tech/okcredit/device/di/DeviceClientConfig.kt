package tech.okcredit.device.di

import io.ktor.client.*
import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import me.tatarka.inject.annotations.Inject
import okcredit.base.network.ClientConfig
import okcredit.base.randomUUID
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.device.usecase.GetOrCreateDeviceId

@Inject
@ContributesBinding(AppScope::class, multibinding = true)
class DeviceClientConfig(private val getOrCreateDeviceId: GetOrCreateDeviceId) : ClientConfig {
    override val config: HttpClientConfig<*>.() -> Unit
        get() = {
            install(deviceIddHeaderPlugin) {
                getDeviceId = {
                    getOrCreateDeviceId.current()
                }
            }
        }
}

class DeviceIdHeaderConfig(
    var getDeviceId: suspend () -> String = { randomUUID() },
)

val deviceIddHeaderPlugin = createClientPlugin("DeviceIdHeader", ::DeviceIdHeaderConfig) {
    onRequest { request, _ ->
        val deviceId = this@createClientPlugin.pluginConfig.getDeviceId()
        request.header(HEADER_DEVICE_ID, deviceId)
    }
}

private const val HEADER_DEVICE_ID = "X-DeviceId"
