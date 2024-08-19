package tech.okcredit.identity.contract.usecase

import kotlinx.coroutines.flow.Flow
import tech.okcredit.identity.contract.model.Business

interface GetAllBusinesses {

    fun execute(): Flow<List<Business>>
}