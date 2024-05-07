package tech.okcredit.identity.contract.usecase

interface SyncIndividual {
    suspend fun execute(flowId: String?): Response

    data class Response(val individualId: String, val businessIdList: List<String>)
}
