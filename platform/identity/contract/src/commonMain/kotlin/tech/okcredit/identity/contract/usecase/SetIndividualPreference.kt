package tech.okcredit.identity.contract.usecase

interface SetIndividualPreference {
    suspend fun execute(
        key: String,
        value: String,
        businessId: String,
    )
}
