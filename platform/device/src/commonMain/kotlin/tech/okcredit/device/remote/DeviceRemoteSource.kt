package tech.okcredit.device.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.DefaultHttpClient
import okcredit.base.network.getOrThrow
import okcredit.base.network.put
import tech.okcredit.device.Device
import tech.okcredit.device.remote.request.ApiDevice
import tech.okcredit.device.remote.request.CreateOrUpdateDeviceRequest

@Inject
class DeviceRemoteSource(
    private val baseUrl: BaseUrl,
    private val defaultHttpClient: DefaultHttpClient,
) {

    suspend fun createOrUpdateDevice(device: Device) {
        defaultHttpClient.put<CreateOrUpdateDeviceRequest, ApiDevice>(
            baseUrl = baseUrl,
            endPoint = "app/v2.0/devices",
            requestBody = CreateOrUpdateDeviceRequest(device.toApiModel()),
        ).getOrThrow()
    }
}
