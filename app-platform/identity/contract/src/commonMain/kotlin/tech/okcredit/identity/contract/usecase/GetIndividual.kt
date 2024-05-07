package tech.okcredit.identity.contract.usecase

import kotlinx.coroutines.flow.Flow
import tech.okcredit.identity.contract.model.Individual

interface GetIndividual {
    fun execute(): Flow<Individual>
}
