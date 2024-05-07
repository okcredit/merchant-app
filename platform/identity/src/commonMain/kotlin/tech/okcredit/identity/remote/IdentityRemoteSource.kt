package tech.okcredit.identity.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.network.getOrThrow
import tech.okcredit.identity.remote.request.CreateBusinessRequest
import tech.okcredit.identity.remote.request.GetBusinessRequest
import tech.okcredit.identity.remote.request.UpdateBusinessRequest
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.*

@Inject
class IdentityRemoteSource(private val apiClient: IdentityApiClient) {

    suspend fun getBusiness(businessId: String): GetBusinessResponseWrapper {
        return apiClient.getBusiness(GetBusinessRequest(businessId), businessId)
            .getOrThrow().business_user
    }

    suspend fun getCategories(businessId: String): List<Category> {
        return apiClient.getCategories(businessId).getOrThrow().categories
    }

    suspend fun getBusinessTypes(businessId: String): List<BusinessType> {
        return apiClient.getBusinessTypes(businessId).getOrThrow().business_type
    }

    suspend fun updateBusiness(request: UpdateBusinessRequest, businessId: String) {
        apiClient.updateBusiness(request, businessId).getOrThrow()
    }

    suspend fun createBusiness(name: String, businessId: String): GetBusinessResponseWrapper {
        return apiClient.createBusiness(
            request = CreateBusinessRequest(
                name = name,
            ),
            businessId = businessId,
        ).getOrThrow().business_user
    }

    suspend fun getIndividual(flowId: String): GetIndividualResponse {
        return apiClient.getIndividual(flowId).getOrThrow()
    }

    suspend fun updateIndividual(request: UpdateIndividualRequest) {
        apiClient.updateIndividual(request).getOrThrow()
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
        apiClient.updateIndividual(request).getOrThrow()
    }
}
