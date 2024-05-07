package tech.okcredit.identity.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import okcredit.base.network.HEADER_BUSINESS_ID
import tech.okcredit.identity.remote.request.CreateBusinessRequest
import tech.okcredit.identity.remote.request.GetBusinessRequest
import tech.okcredit.identity.remote.request.UpdateBusinessRequest
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.BusinessTypeResponse
import tech.okcredit.identity.remote.response.CategoryResponse
import tech.okcredit.identity.remote.response.GetBusinessResponse
import tech.okcredit.identity.remote.response.GetIndividualResponse

interface IdentityApiClient {
    @POST("identity/v1/GetBusinessUser")
    suspend fun getBusiness(
        @Body request: GetBusinessRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetBusinessResponse>

    @POST("identity/v1/CreateBusinessUser")
    suspend fun createBusiness(
        @Body request: CreateBusinessRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetBusinessResponse>

    @POST("identity/v1/UpdateBusinessUser")
    suspend fun updateBusiness(
        @Body request: UpdateBusinessRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<GetBusinessResponse>

    @GET("identity/v1/mobile/migrate")
    suspend fun checkNewNumber(
        @Query("mobile") key: String?,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    )

    @GET("identity/legacy/v1.0/categories")
    suspend fun getCategories(
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<CategoryResponse>

    @GET("identity/legacy/v2.0/business-types")
    suspend fun getBusinessTypes(
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): Response<BusinessTypeResponse>

    @GET("identity/v2/me")
    suspend fun getIndividual(
        @Header(OKC_LOGIN_FLOW_ID) flowId: String,
    ): Response<GetIndividualResponse>

    @POST("identity/v1/UpdateIndividualUser")
    suspend fun updateIndividual(
        @Body request: UpdateIndividualRequest,
    ): Response<Unit>
}

const val OKC_LOGIN_FLOW_ID = "okc-login-flow-id"
