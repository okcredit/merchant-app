package tech.okcredit.identity.contract.usecase

import kotlinx.coroutines.flow.Flow
import tech.okcredit.identity.contract.PreferenceKey

interface GetIndividualPreference {
    fun execute(preference: PreferenceKey): Flow<String>
}
