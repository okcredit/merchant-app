package tech.okcredit.identity.contract.usecase

import tech.okcredit.identity.contract.model.BusinessType

interface UpdateBusiness {
    suspend fun execute(request: Request)
}

data class Request(
    val inputType: Int,
    val updatedValue: String? = null,
    val address: Triple<String, Double, Double>? = null,
    val category: Pair<String, String>? = null,
    val businessType: BusinessType? = null,
)
