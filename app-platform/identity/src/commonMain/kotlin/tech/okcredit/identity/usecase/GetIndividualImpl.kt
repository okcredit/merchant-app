package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.model.Individual
import tech.okcredit.identity.contract.usecase.GetIndividual

@Inject
class GetIndividualImpl(private val repository: IdentityRepository) : GetIndividual {

    override fun execute(): Flow<Individual> {
        return repository.getIndividual(randomUUID())
    }
}
