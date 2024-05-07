package tech.okcredit.auth.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import okcredit.base.network.HEADER_BUSINESS_ID
import tech.okcredit.auth.remote.model.request.SetPasswordRequest
import tech.okcredit.auth.remote.model.request.SignOutRequest
import tech.okcredit.auth.remote.model.response.CheckPasswordSetResponse

interface Protected {
    @POST("v3/logout")
    suspend fun logout(@Body request: SignOutRequest)

    @GET("v3/password")
    suspend fun checkPasswordSet(): Response<CheckPasswordSetResponse>

    @POST("v3/password")
    suspend fun setPassword(
        @Body req: SetPasswordRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )
}
