package tech.okcredit.device.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.network.asError
import tech.okcredit.device.Device
import tech.okcredit.device.IpAddressData
import tech.okcredit.device.remote.request.CreateOrUpdateDeviceRequest

@Inject
class DeviceRemoteSource constructor(private val deviceApiClient: DeviceApiClient) {

    suspend fun createOrUpdateDevice(device: Device) {
        val response = deviceApiClient
            .createOrUpdateDeviceSingle(CreateOrUpdateDeviceRequest(device.toApiModel()))

        if (!response.isSuccessful) {
            throw response.asError()
        }
    }

    suspend fun getIpAddressData(): IpAddressData {
        val response = deviceApiClient.getIpAddressData(IP_LOOKUP_URL)
        if (response.isSuccessful) {
            return response.body() as IpAddressData
        } else {
            throw response.asError()
        }
    }

    companion object {
        // Contact Saket for changes to this
        const val IP_LOOKUP_URL =
            "https://api.ipdata.co/?api-key=58bab55ef703bc8a92776eb15e6f6d78324d1752d2a316b6f87792d7"
    }
}
