package tech.okcredit.ab.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import okcredit.base.network.HEADER_BUSINESS_ID

interface AbApiClient {

    @GET("ab/v2/GetProfile")
    suspend fun getProfile(
        @Query("device_id") deviceId: String,
        @Header("X-App-Source") source: String,
        @Header("X-App-Source-Type") sourceType: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetProfileResponse>

    @POST("ab/v2/Ack")
    suspend fun acknowledge(
        @Body req: AcknowledgementRequest,
        @Header("X-App-Source") source: String,
        @Header("X-App-Source-Type") sourceType: String,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )

    @POST("ab/v1/DisableFeature")
    suspend fun disableFeature(
        @Body req: DisableFeatureRequest,
        @Header("X-App-Source") source: String,
        @Header("X-App-Source-Type") sourceType: String,
    )

    @POST("ab/v1/EnableFeature")
    suspend fun enableFeature(
        @Body req: EnableFeatureRequest,
        @Header("X-App-Source") source: String,
        @Header("X-App-Source-Type") sourceType: String,
    )
}
