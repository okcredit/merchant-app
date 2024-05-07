package tech.okcredit.identity.contract.usecase

interface SetActiveBusinessId {
    suspend fun execute(businessId: String)
}
