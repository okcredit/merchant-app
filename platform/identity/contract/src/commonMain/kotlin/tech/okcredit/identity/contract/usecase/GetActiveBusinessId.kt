package tech.okcredit.identity.contract.usecase

interface GetActiveBusinessId {
    /**
     * Returns default business id if user is logged in. If user is not logged in, returns a blank string
     */
    suspend fun execute(): String

    /**
     * [thisOrActiveBusinessId] returns @param businessId if it is not null or else returns the default business id
     */
    suspend fun thisOrActiveBusinessId(businessId: String?): String
}
