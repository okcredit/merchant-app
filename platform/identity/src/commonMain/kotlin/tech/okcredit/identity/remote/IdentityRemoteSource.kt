package tech.okcredit.identity.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.get
import okcredit.base.network.getOrThrow
import okcredit.base.network.post
import tech.okcredit.identity.remote.request.CreateBusinessRequest
import tech.okcredit.identity.remote.request.GetBusinessRequest
import tech.okcredit.identity.remote.request.UpdateBusinessRequest
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.*

@Inject
class IdentityRemoteSource(
    private val baseUrl: BaseUrl,
    private val authorizedHttpClient: AuthorizedHttpClient,
) {

    companion object {
        const val OKC_LOGIN_FLOW_ID = "okc-login-flow-id"
    }

    suspend fun getBusiness(businessId: String): GetBusinessResponseWrapper {
        return authorizedHttpClient.post<GetBusinessRequest, GetBusinessResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/v1/GetBusinessUser",
            requestBody = GetBusinessRequest(businessId),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().business_user
    }

    suspend fun getCategories(businessId: String): List<Category> {
        return authorizedHttpClient.get<CategoryResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/legacy/v1.0/categories",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().categories
    }

    suspend fun getBusinessTypes(businessId: String): List<BusinessType> {
        return authorizedHttpClient.get<BusinessTypeResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/legacy/v2.0/business-types",
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().business_type
    }

    suspend fun updateBusiness(request: UpdateBusinessRequest, businessId: String) {
        authorizedHttpClient.post<UpdateBusinessRequest, GetBusinessResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/v1/UpdateBusinessUser",
            requestBody = request,
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    suspend fun createBusiness(name: String, businessId: String): GetBusinessResponseWrapper {
        return authorizedHttpClient.post<CreateBusinessRequest, GetBusinessResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/v1/CreateBusinessUser",
            requestBody = CreateBusinessRequest(name),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow().business_user
    }

    suspend fun getIndividual(flowId: String): GetIndividualResponse {
        return authorizedHttpClient.get<GetIndividualResponse>(
            baseUrl = baseUrl,
            endPoint = "identity/v2/me",
            headers = mapOf(OKC_LOGIN_FLOW_ID to flowId),
        ).getOrThrow()
    }

    suspend fun updateIndividual(request: UpdateIndividualRequest) {
        authorizedHttpClient.post<UpdateIndividualRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "identity/v1/UpdateIndividualUser",
            requestBody = request,
        ).getOrThrow()
    }

    suspend fun updateBusinessMobile(
        mobile: String,
        currentMobileOtpToken: String,
        newMobileOtpToken: String,
        individualId: String,
    ) {
        val request = UpdateIndividualRequest(
            current_mobile_otp_token = currentMobileOtpToken,
            new_mobile_otp_token = newMobileOtpToken,
            update_mobile = true,
            individual_user_id = individualId,
            individual_user = IndividualUser(
                user = User(
                    id = individualId,
                    mobile = mobile,
                ),
            ),
        )
        authorizedHttpClient.post<UpdateIndividualRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "identity/v1/UpdateIndividualUser",
            requestBody = request,
        ).getOrThrow()
    }
}
