package tech.okcredit.identity.contract.usecase

import kotlinx.coroutines.flow.Flow
import tech.okcredit.identity.contract.model.Business

interface GetActiveBusiness {
    fun execute(): Flow<Business>
}
