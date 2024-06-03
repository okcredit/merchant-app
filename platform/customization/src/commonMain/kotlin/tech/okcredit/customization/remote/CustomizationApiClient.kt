package tech.okcredit.customization.remote

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import okcredit.base.network.HEADER_BUSINESS_ID
import tech.okcredit.customization.models.TargetComponent

interface CustomizationApiClient {

    @POST("dynamicui/v2/ListCustomizations")
    suspend fun listCustomizations(
        @Body req: GetCustomizationRequest,
        @Header(HEADER_BUSINESS_ID) businessId: String,
    ): List<TargetComponent>
}
