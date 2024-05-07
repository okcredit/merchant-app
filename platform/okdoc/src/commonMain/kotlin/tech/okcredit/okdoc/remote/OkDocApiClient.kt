package tech.okcredit.okdoc.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import io.ktor.http.content.PartData
import okcredit.base.network.HEADER_BUSINESS_ID

interface OkDocApiClient {

    @Multipart
    @POST("app/okdoc/v2/file/upload")
    suspend fun uploadFile(
        @Header(HEADER_BUSINESS_ID) businessId: String,
        @Part("") file: List<PartData>,
        @Part("request_id") requestId: String,
        @Part("use_case") useCase: String,
        @Part("is_secured") isSecured: Boolean,
    ): Response<OkDocApiResponse>
}
