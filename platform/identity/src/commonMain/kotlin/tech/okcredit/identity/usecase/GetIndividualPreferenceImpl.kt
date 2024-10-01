package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.PreferenceKey
import tech.okcredit.identity.contract.usecase.GetIndividualPreference

@Inject
@ContributesBinding(AppScope::class)
class GetIndividualPreferenceImpl(private val repository: IdentityRepository) : GetIndividualPreference {

    override fun execute(preference: PreferenceKey): Flow<String> {
        return repository.getIndividualPreference(preference)
    }
}
