package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.PreferenceKey
import tech.okcredit.identity.contract.usecase.GetIndividualPreference

@Inject
class GetIndividualPreferenceImpl(private val repository: IdentityRepository) : GetIndividualPreference {

    override fun execute(preference: PreferenceKey): Flow<String> {
        return repository.getIndividualPreference(preference)
    }
}
