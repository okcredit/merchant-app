package tech.okcredit.device.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Url
import tech.okcredit.device.IpAddressData
import tech.okcredit.device.remote.request.ApiDevice
import tech.okcredit.device.remote.request.CreateOrUpdateDeviceRequest

interface DeviceApiClient {

    @PUT("app/v2.0/devices")
    suspend fun createOrUpdateDeviceSingle(@Body req: CreateOrUpdateDeviceRequest): Response<ApiDevice>

    @GET
    suspend fun getIpAddressData(@Url url: String): Response<IpAddressData>
}
